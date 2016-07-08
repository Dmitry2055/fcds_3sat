package tud.fcds;

public class Main {

    public static void main(String[] args) {

        if (args.length != 1)
        {
            System.out.println("Usage: [input_file]");
            System.exit(-1);
        }

        Stopwatch swMain = new Stopwatch("main");
        swMain.start();

        long solution = SatSolver.solveSat(args[0]);

        swMain.stop();
        System.out.println(swMain.getInfoMsg());

    }


}




