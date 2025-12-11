package bench;

import sorting.BucketSort;
import sorting.CountingSort;
import sorting.Ordenadores;
import sorting.RadixSort;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.*;
/**
 * RunnerTerminal: benchmark interativo e justo (mesmos vetores para todos os algoritmos).
 * - Escolhe algoritmos
 * - Escolhe n, repetições e seed base
 * - Imprime tempos (um por linha) e a média ao final
 *
 * Observação: aqui medimos os algoritmos de comparação (Integer[]).
 * Counting/Radix/Bucket são tratáveis à parte (int[]/double[]).
 */
public class RunnerTerminalBackup {

    private static final Locale LOCALE = Locale.ROOT;
    private final Scanner in = new Scanner(System.in);

    /** Assinatura de sorters que trabalham com Integer[] */
    @FunctionalInterface
    public interface SorterComparable { void sort(Integer[] a); }

    @FunctionalInterface interface SorterInt    { void sort(int[] a); }
    @FunctionalInterface interface SorterDouble { void sort(double[] a); }

    /** Entrada do catálogo: nome + implementação */
    private static final class Algo {
        final String nome;
        final SorterComparable sorter;
        Algo(String nome, SorterComparable sorter) { this.nome = nome; this.sorter = sorter; }
    }
    static class AlgoInt    { final String nome; final SorterInt sorter;    AlgoInt(String n, SorterInt s){nome=n; sorter=s;} }
    static class AlgoDouble { final String nome; final SorterDouble sorter;  AlgoDouble(String n, SorterDouble s){nome=n; sorter=s;} }


    private List<Algo> catalogo() {
        List<Algo> list = new ArrayList<>();
        list.add(new Algo("bubble",    Ordenadores::bubbleSort));
        list.add(new Algo("insertion", Ordenadores::insertionSort));
        list.add(new Algo("selection", Ordenadores::selectionSort));
        list.add(new Algo("shell",     Ordenadores::shellSort));
        list.add(new Algo("heap",      Ordenadores::heapSort));
        list.add(new Algo("merge",     Ordenadores::mergeSort));
        list.add(new Algo("quick",     Ordenadores::quickSort));
        return list;
    }

    private List<AlgoInt> catalogoInt(int min, int max, int baseRadix) {
    List<AlgoInt> list = new ArrayList<>();
    list.add(new AlgoInt("counting", a -> sorting.CountingSort.sort(a, min, max)));
    list.add(new AlgoInt("radix",    a -> sorting.RadixSort.sort(a, baseRadix)));
    return list;
}

    private List<AlgoDouble> catalogoDouble(int numBuckets) {
        List<AlgoDouble> list = new ArrayList<>();
        list.add(new AlgoDouble("bucket", a -> sorting.BucketSort.sort(a, numBuckets)));
        return list;
    }

    public void runInteractive() {
    limparTela();
    System.out.println("== Benchmark (RunnerTerminal) ==");

    int tamanho = escolherTamanho();
    int repeticoes = lerIntOuPadrao("Repetições [padrão 5]: ", 5);
    long seedBase = lerLongOuPadrao("Seed base [padrão 42]: ", 42L);

    List<Algo> escolhidos = escolherAlgoritmos(catalogo());
    System.out.printf("%nUsando: n=%d, repetições=%d, seeds=%d..%d%n%n",
            tamanho, repeticoes, seedBase, seedBase + repeticoes - 1);

    // Gera MESMOS vetores-base por repetição para todos os algoritmos
    List<Integer[]> basesAleatorio = gerarBasesInteger(tamanho, repeticoes, seedBase, 0, tamanho);

    // ---- CSV detalhado (uma linha por execução) ----
    Path destino = Path.of("resultados", lerNomeArquivoCsv());
    try (BufferedWriter csv = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
        // cabeçalho
        csv.write("algoritmo,tempo_ms,n,execucao,cenario");
        csv.newLine();

        // para cada algoritmo escolhido, mede e grava as execuções
        for (Algo algo : escolhidos) {
            medirEImprimirCsv(algo.nome, algo.sorter, basesAleatorio, tamanho, "aleatorio", csv);
            System.out.println();
        }

        System.out.println("\nCSV gerado em: " + destino.toAbsolutePath());

    } catch (IOException e) {
        System.out.println("Falha ao escrever CSV: " + e.getMessage());
        // mesmo com erro no CSV, ainda mostramos as métricas no console
        for (Algo algo : escolhidos) {
            medirEImprimir(algo.nome, algo.sorter, basesAleatorio);
            System.out.println();
        }
    }
}

public void runLinearInteractive() {
    limparTela();
    System.out.println("== Benchmark (LINEARES: counting, radix, bucket) ==");

    int n         = escolherTamanho();
    int repeticoes= lerIntOuPadrao("Repetições [padrão 5]: ", 5);
    long seedBase = lerLongOuPadrao("Seed base [padrão 42]: ", 42L);

    // parâmetros dos algoritmos (ajuste se quiser perguntar na UI)
    int min = 0, max = Math.max(10, n);  // counting
    int baseRadix = 10;                  // radix
    int numBuckets = 100;                // bucket

    // gera as MESMAS bases por repetição para cada algoritmo (aleatório)
    java.util.List<int[]> basesInt = new java.util.ArrayList<>(repeticoes);
    for (int r = 0; r < repeticoes; r++) basesInt.add(genIntArray(n, min, max, seedBase + r));
    java.util.List<double[]> basesDouble = new java.util.ArrayList<>(repeticoes);
    for (int r = 0; r < repeticoes; r++) basesDouble.add(genDouble01(n, seedBase + r));

    // onde salvar
    java.nio.file.Path destino = java.nio.file.Path.of("resultados", "resultados_lineares.csv");
    try {
        java.nio.file.Files.createDirectories(destino.getParent());
    } catch (java.io.IOException e) {
        System.out.println("Não foi possível criar a pasta de resultados: " + e.getMessage());
        return;
    }

    try (java.io.BufferedWriter csv = java.nio.file.Files.newBufferedWriter(
            destino, java.nio.charset.StandardCharsets.UTF_8)) {

        // cabeçalho
        csv.write("algoritmo,tempo_ms,n,execucao,cenario");
        csv.newLine();

        // COUNTING
        medirSalvarInt(
                "counting",
                arr -> sorting.CountingSort.sort(arr, min, max),
                basesInt, n, "aleatorio", csv);

        System.out.println();

        // RADIX
        medirSalvarInt(
                "radix",
                arr -> sorting.RadixSort.sort(arr, baseRadix),
                basesInt, n, "aleatorio", csv);

        System.out.println();

        // BUCKET
        medirSalvarDouble(
                "bucket",
                arr -> sorting.BucketSort.sort(arr, numBuckets),
                basesDouble, n, "aleatorio", csv);

        System.out.println("\nCSV gerado em: " + destino.toAbsolutePath());

    } catch (java.io.IOException e) {
        System.out.println("Falha ao escrever CSV: " + e.getMessage());
    }
}

private static void medirSalvarInt(String nome,
                                   SorterInt sorter,
                                   java.util.List<int[]> bases,
                                   int n,
                                   String cenario,
                                   java.io.BufferedWriter csv) throws java.io.IOException {
    System.out.println("Algoritmo: " + nome);
    double somaMs = 0.0;
    for (int i = 0; i < bases.size(); i++) {
        int[] arr = bases.get(i).clone();
        long nanos = bench.Timer.time(() -> sorter.sort(arr));
        double ms = nanos / 1e6;
        somaMs += ms;
        System.out.printf(LOCALE, "  tempo %d: %.3f ms%n", i + 1, ms);

        csv.write(nome + "," + String.format(LOCALE, "%.3f", ms) + "," + n + "," + (i + 1) + "," + cenario);
        csv.newLine();
    }
    System.out.printf(LOCALE, "  média: %.3f ms%n", somaMs / bases.size());
}

private static void medirSalvarDouble(String nome,
                                      SorterDouble sorter,
                                      java.util.List<double[]> bases,
                                      int n,
                                      String cenario,
                                      java.io.BufferedWriter csv) throws java.io.IOException {
    System.out.println("Algoritmo: " + nome);
    double somaMs = 0.0;
    for (int i = 0; i < bases.size(); i++) {
        double[] arr = bases.get(i).clone();
        long nanos = bench.Timer.time(() -> sorter.sort(arr));
        double ms = nanos / 1e6;
        somaMs += ms;
        System.out.printf(LOCALE, "  tempo %d: %.3f ms%n", i + 1, ms);

        csv.write(nome + "," + String.format(LOCALE, "%.3f", ms) + "," + n + "," + (i + 1) + "," + cenario);
        csv.newLine();
    }
    System.out.printf(LOCALE, "  média: %.3f ms%n", somaMs / bases.size());
}


    // ------------------------- lógica de benchmark -------------------------

    private List<Integer[]> gerarBasesInteger(int n, int repeticoes, long seedBase, int min, int maxExclusive) {
        List<Integer[]> bases = new ArrayList<>(repeticoes);
        for (int r = 0; r < repeticoes; r++) {
            long seed = seedBase + r;
            bases.add(Generator.genRandomIntBoxed(n, min, maxExclusive, seed));
        }
        return bases;
    }

    private double medirEImprimir(String nome, SorterComparable sorter, List<Integer[]> bases) {
        System.out.println("Algoritmo: " + nome);
        double somaMs = 0.0;

        for (int i = 0; i < bases.size(); i++) {
            Integer[] arr = bases.get(i).clone();
            long nanos = Timer.time(() -> sorter.sort(arr));
            double ms = nanos / 1e6;
            somaMs += ms;
            System.out.printf(LOCALE, "  tempo %d: %.3f ms%n", i + 1, ms);
        }
        double media = somaMs / bases.size();
        System.out.printf(LOCALE, "  média: %.3f ms%n", media);
        return media; // <<=== devolve a média para o caller escrever no CSV
    }

    /**
 * Mede todas as execuções, imprime tempos e média no console
 * e grava UMA LINHA POR EXECUÇÃO no CSV no formato:
 * algoritmo,tempo_ms,n,execucao,cenario
 */
    private void medirEImprimirCsv(String nome,
                               SorterComparable sorter,
                               List<Integer[]> bases,
                               int n,
                               String cenario,
                               BufferedWriter csv) throws IOException {
    System.out.println("Algoritmo: " + nome);
    double somaMs = 0.0;

        for (int i = 0; i < bases.size(); i++) {
            Integer[] arr = bases.get(i).clone();
            long nanos = Timer.time(() -> sorter.sort(arr));
            double ms = nanos / 1e6;
            somaMs += ms;

            // console (igual ao seu)
            System.out.printf(LOCALE, "  tempo %d: %.3f ms%n", i + 1, ms);

            // CSV detalhado para boxplot
            // algoritmo,tempo_ms,n,execucao,cenario
            csv.write(nome + "," + String.format(LOCALE, "%.3f", ms) + "," + n + "," + (i + 1) + "," + cenario);
            csv.newLine();
        }
        double media = somaMs / bases.size();
        System.out.printf(LOCALE, "  média: %.3f ms%n", media);
    }



    // ------------------------- UI / entrada -------------------------

    private List<Algo> escolherAlgoritmos(List<Algo> todos) {
        System.out.println("\nEscolha os algoritmos (separe por vírgula)");
        for (int i = 0; i < todos.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, todos.get(i).nome);
        }
        System.out.println("8) Todos");

        System.out.print("Opção(s): ");
        String s = in.nextLine().trim();

        if (s.equalsIgnoreCase("8")) return todos;

        Set<Integer> indices = new LinkedHashSet<>();
        for (String tk : s.split("[,; ]+")) {
            if (tk.isEmpty()) continue;
            try {
                int idx = Integer.parseInt(tk);
                if (idx >= 1 && idx <= todos.size()) indices.add(idx - 1);
            } catch (NumberFormatException ignored) {}
        }
        if (indices.isEmpty()) {
            System.out.println("Nenhum algoritmo válido selecionado. Usando 'shell' por padrão.");
            return Collections.singletonList(new Algo("shell", Ordenadores::shellSort));
        }
        List<Algo> result = new ArrayList<>();
        for (int idx : indices) result.add(todos.get(idx));
        return result;
    }

    private int escolherTamanho() {
        final int[] opcoes = {100, 1_000, 10_000, 100_000, 1_000_000};
        while (true) {
            System.out.println("\nTamanho do vetor:");
            System.out.println("1) 100");
            System.out.println("2) 1.000");
            System.out.println("3) 10.000");
            System.out.println("4) 100.000");
            System.out.println("5) 1.000.000");
            System.out.println("6) Outro (digitar)");
            System.out.print("Opção: ");
            String s = in.nextLine().trim();
            switch (s) {
                case "1": return opcoes[0];
                case "2": return opcoes[1];
                case "3": return opcoes[2];
                case "4": return opcoes[3];
                case "5": return opcoes[4];
                case "6":
                    int n = lerIntOuPadrao("Informe n (>=1): ", 100);
                    if (n >= 1) return n;
                    System.out.println("Valor inválido. Tente novamente.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private int lerIntOuPadrao(String prompt, int padrao) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) return padrao;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, usando padrão: " + padrao);
            return padrao;
        }
    }

    private long lerLongOuPadrao(String prompt, long padrao) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) return padrao;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, usando padrão: " + padrao);
            return padrao;
        }
    }

    private String lerNomeArquivoCsv() {
        System.out.print("Nome do arquivo CSV de saída [padrão resultados.csv]: ");
        String s = in.nextLine().trim();
        if (s.isEmpty()) return "resultados.csv";
        if (!s.toLowerCase().endsWith(".csv")) s += ".csv";
        return s;
    }

    private void limparTela() {
        System.out.println("\n".repeat(10));
    }
    private static int[] genIntArray(int n, int min, int max, long seed) {
        if (max < min) { int t = min; min = max; max = t; }
        java.util.Random rnd = new java.util.Random(seed);
        int[] a = new int[n];
        int bound = (max - min) + 1;
        for (int i = 0; i < n; i++) a[i] = min + rnd.nextInt(bound);
        return a;
    }
    private static double[] genDouble01(int n, long seed) {
        java.util.Random rnd = new java.util.Random(seed);
        double[] a = new double[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextDouble();
        return a;
    }

}

