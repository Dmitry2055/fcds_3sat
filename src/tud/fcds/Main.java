package tud.fcds;

public class Main {

    public static void main(String[] args) {

        Stopwatch swMain = new Stopwatch("main");
        swMain.start();

        long solution = SatSolver.solveSat();

        swMain.stop();
        System.out.println(swMain.getInfoMsg());
//        int i;
//        if (solution >= 0) {
//            System.out.printf("Solution found [%d]: ", solution);
//            for (i = 0; i < solver.getnVar(); i++)
//                System.out.printf("%d ", (int) ((solution & (long) Math.pow(2,i)) / Math.pow(2, i)));
//            System.out.printf("\n");
//        } else
//            System.out.printf("Solution not found.\n");
    }


}




