package application;

import bench.RunnerTerminal;

public class Climain {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(
                "Uso:\n" +
                "  java -cp out application.CliMain bench --n <int> --reps <int> --seed <long> --csv resultados/resultados.csv\n" +
                "  java -cp out application.CliMain demo  --alg <shell|heap|merge|quick|insertion|selection|bubble> --n <int> --seed <long> --passos <int>\n"
            );
            System.exit(1);
        }

        String cmd = args[0];
        if ("bench".equalsIgnoreCase(cmd)) {
            // Defaults
            int n = 10000, reps = 5; long seed = 42L;
            String csv = "resultados/resultados.csv";

            // parse simples
            for (int i = 1; i < args.length; i++) {
                switch (args[i]) {
                    case "--n":    n    = Integer.parseInt(args[++i]); break;
                    case "--reps": reps = Integer.parseInt(args[++i]); break;
                    case "--seed": seed = Long.parseLong(args[++i]);   break;
                    case "--csv":  csv  = args[++i];                   break;
                    default: System.out.println("Parâmetro desconhecido: " + args[i]);
                }
            }

            // roda benchmark não-interativo e grava CSV detalhado (uma linha por execução)
            try {
                RunnerTerminal.runBenchNonInteractive(n, reps, seed, csv);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
        else if ("demo".equalsIgnoreCase(cmd)) {
            String alg = "shell"; int n = 30; long seed = 7L; int passos = 10;
            for (int i = 1; i < args.length; i++) {
                switch (args[i]) {
                    case "--alg":   alg    = args[++i]; break;
                    case "--n":     n      = Integer.parseInt(args[++i]); break;
                    case "--seed":  seed   = Long.parseLong(args[++i]);   break;
                    case "--passos":passos = Integer.parseInt(args[++i]); break;
                    default: System.out.println("Parâmetro desconhecido: " + args[i]);
                }
            }
            try {
                RunnerTerminal.runDemoNonInteractive(alg, n, seed, passos);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
        else {
            System.out.println("Comando inválido: " + cmd);
            System.exit(1);
        }
    }
}
