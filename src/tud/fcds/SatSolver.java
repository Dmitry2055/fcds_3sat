package tud.fcds;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by dmitr on 6/27/2016.
 */
public class SatSolver {

    private static final int SIZE_CONST = 3;
    private int nClauses;

    public int getnVar() {
        return nVar;
    }

    public int getnClauses() {
        return nClauses;
    }

    private int nVar;
    private short[][] clauses;


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

        long maxNumber = (long) Math.pow(2, nVar);
        long number;
        short var;
        int c;

        for (number = 0; number < maxNumber; number++) {

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
    void readClauses() {


        try {
            Scanner input = null;
            input = new Scanner(new File("src/input.txt"));

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
