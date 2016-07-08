package tud.fcds;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by dmitr on 6/27/2016.
 */
public class SatSolver extends RecursiveTask<Long> {

    private static final int SIZE_CONST = 3;
    private static final long SEQUENTIAL_THRESHOLD = 65_536;
    private static int nClauses;
    private long start;
    private long end;
    private static int nVar;
    private static short[][] clauses;


    public SatSolver(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public int getnVar() {
        return nVar;
    }

    public int getnClauses() {
        return nClauses;
    }


    // Solves the 3-SAT system using an exaustive search
// It finds all the possible values for the set of variables using
// a number. The binary representation of this number represents
// the values of the variables. Since a long variable has 64 bits,
// this implementations works with problems with up to 64 variables.
    long solveClauses() {

        long[] iVar = new long[nVar];
        int i;
        for (i = 0; i < nVar; i++)
            iVar[i] = (int) Math.pow(2, i);

        long number;
        short var;
        int c;

        for (number = start; number < end; number++) {

            for (c = 0; c < nClauses; c++) {

                var = clauses[0][c];
                if (var > 0 && (number & iVar[var - 1]) > 0)
                    continue; // clause is true
                else if (var < 0 && (number & iVar[-var - 1]) == 0)
                    continue; // clause is true

                var = clauses[1][c];
                if (var > 0 && (number & iVar[var - 1]) > 0)
                    continue; // clause is true
                else if (var < 0 && (number & iVar[-var - 1]) == 0)
                    continue; // clause is true

                var = clauses[2][c];
                if (var > 0 && (number & iVar[var - 1]) > 0)
                    continue; // clause is true
                else if (var < 0 && (number & iVar[-var - 1]) == 0)
                    continue; // clause is true

                break; // clause is false
            }

            if (c == nClauses)
                return number;

        }
        return -1;

    }

    // Read nClauses clauses of size 3. nVar represents the number of variables
// Clause[0][i], Clause[1][i] and Clause[2][i] contains the 3 elements of the i-esime clause.
// Each element of the caluse vector may contain values selected from:
// k = -nVar, ..., -2, -1, 1, 2, ..., nVar. The value of k represents the index of the variable.
// A negative value remains the negation of the variable.
    private static void readClauses(String file) {


        try {
            Scanner input = null;
            input = new Scanner(new File(file));

            nClauses = input.nextShort();   //  get the number of clauses
            nVar = input.nextShort();       //  and variables
            input.nextLine();

            clauses = new short[SIZE_CONST][nClauses];

            for (int i = 0; input.hasNextLine(); i++)
            {
                Scanner colReader = new Scanner(input.nextLine());
                for (int j = 0; colReader.hasNextShort(); j++)
                {
                    clauses[0][i] = colReader.nextShort();
                    clauses[1][i] = colReader.nextShort();
                    clauses[2][i] = colReader.nextShort();
                }
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
            System.exit(-1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error reading file");
            System.exit(-2);
        }

    }

    public static long solveSat(String file){
        readClauses(file);  //  get the data from a file
        long maxNumber = (long) Math.pow(2, nVar);  //  get initial max number
        SatSolver solver = new SatSolver(0, maxNumber); //  initial conditions
        ForkJoinPool pool = new ForkJoinPool();
        long solution = pool.invoke(solver);

        int i;
        if (solution >= 0) {
            System.out.printf("Solution found [%d]: ", solution);
            for (i = 0; i < nVar; i++)
                System.out.printf("%d ", (int) ((solution & (long) Math.pow(2,i)) / Math.pow(2, i)));
            System.out.printf("\n");
        } else
            System.out.printf("Solution not found.\n");


        return solution;
    }

    @Override
    protected Long compute() {

        if (end - start < SEQUENTIAL_THRESHOLD){
//        if (true){    //  for 1 thread
            //  do sequential
            return solveClauses();
        }
        else{
            // fork
            List<SatSolver> tasks = new ArrayList<>();    //  task pool
            long chunkSize = SEQUENTIAL_THRESHOLD;  //  size of one task

//  fork tasks
            for (long i = start; i < end; i += chunkSize){
                long newStart = i;
                long newEnd = Math.min(i + SEQUENTIAL_THRESHOLD - 1, end);
                SatSolver task = new SatSolver(newStart, newEnd);
                task.fork();
                tasks.add(task);
            }
//  join tasks
            long joinedRes = -1;
            for (SatSolver t :
                    tasks) {
                joinedRes = t.join();
                if (joinedRes != -1)
                    return joinedRes;
            }

            return  joinedRes > 0 ? joinedRes : -1;
        }


    }

}
