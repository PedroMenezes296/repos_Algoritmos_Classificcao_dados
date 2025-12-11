package bench;

import sorting.Ordenadores;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import java.util.*;

// Simplifique para o essencial

public class RunnerTerminal {

    // === já existe: seu runInteractive() ===
    public void runInteractive() { /* ... o que você já tem ... */ }

    // === NOVO: benchmark não-interativo que gera CSV detalhado ===
    public static void runBenchNonInteractive(int n, int repeticoes, long seedBase, String csvPath) throws IOException {
        // catálogo de algoritmos a medir (adicione os que quiser)
        List<Algo> escolhidos = catalogoBasico();

        // bases iguais para todos (aleatório)
        List<Integer[]> bases = gerarBasesInteger(n, repeticoes, seedBase, 0, n);

        Path destino = Path.of(csvPath);
        Files.createDirectories(destino.getParent());
        try (BufferedWriter csv = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            csv.write("algoritmo,tempo_ms,n,execucao,cenario");
            csv.newLine();

            for (Algo algo : escolhidos) {
                medirEImprimirCsv(algo.nome, algo.sorter, bases, n, "aleatorio", csv);
                System.out.println();
            }
        }
        System.out.println("CSV gerado em: " + destino.toAbsolutePath());
    }

    // === NOVO: demo não-interativo (apenas imprime passos no stdout) ===
    public static void runDemoNonInteractive(String algoritmo, int n, long seed, int maxPassos) {
        Integer[] base = Generator.genRandomIntBoxed(n, 0, n, seed);
        System.out.println(algoritmo.toUpperCase() + " — n=" + n);
        System.out.println("Passo 0: " + Arrays.toString(base));

        ColetorPassos<Integer> out = (snap, desc) ->
            System.out.println("Passo: " + Arrays.toString(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));

        Integer[] demoArr = base.clone();

        switch (algoritmo.toLowerCase(Locale.ROOT)) {
            case "shell":     Ordenadores.demoShell(demoArr, out, maxPassos);     break;
            case "heap":      Ordenadores.demoHeap(demoArr, out, maxPassos);      break;
            case "merge":     Ordenadores.demoMerge(demoArr, out, maxPassos);     break;
            case "quick":     Ordenadores.demoQuick(demoArr, out, maxPassos);     break;
            case "insertion": Ordenadores.demoInsertion(demoArr, out, maxPassos); break;
            case "selection": Ordenadores.demoSelection(demoArr, out, maxPassos); break;
            case "bubble":    Ordenadores.demoBubble(demoArr, out, maxPassos);    break;
            default: System.out.println("Algoritmo desconhecido: " + algoritmo);
        }

        System.out.println("--------- final ------------");
        Integer[] finalArr = base.clone();
        switch (algoritmo.toLowerCase(Locale.ROOT)) {
            case "shell":     Ordenadores.shellSort(finalArr);     break;
            case "heap":      Ordenadores.heapSort(finalArr);      break;
            case "merge":     Ordenadores.mergeSort(finalArr);     break;
            case "quick":     Ordenadores.quickSort(finalArr);     break;
            case "insertion": Ordenadores.insertionSort(finalArr); break;
            case "selection": Ordenadores.selectionSort(finalArr); break;
            case "bubble":    Ordenadores.bubbleSort(finalArr);    break;
        }
        System.out.println(Arrays.toString(finalArr));
    }

    // ====== utilidades mínimas ======

    public static class Algo {
        public final String nome;
        public final SorterComparable sorter;
        public Algo(String n, SorterComparable s) { nome=n; sorter=s; }
    }

    public interface SorterComparable { void sort(Integer[] a); }

    public static List<Algo> catalogoBasico() {
        List<Algo> list = new ArrayList<>();
        list.add(new Algo("shell",     Ordenadores::<Integer>shellSort));
        // list.add(new Algo("heap",   Ordenadores::<Integer>heapSort));
        // list.add(new Algo("merge",  Ordenadores::<Integer>mergeSort));
        // list.add(new Algo("quick",  Ordenadores::<Integer>quickSort));
        // list.add(new Algo("bubble", Ordenadores::<Integer>bubbleSort));
        // list.add(new Algo("insertion", Ordenadores::<Integer>insertionSort));
        // list.add(new Algo("selection", Ordenadores::<Integer>selectionSort));
        return list;
    }

    public static List<Integer[]> gerarBasesInteger(int n, int repeticoes, long seedBase, int min, int maxExclusivo) {
        List<Integer[]> out = new ArrayList<>(repeticoes);
        for (int r=0; r<repeticoes; r++) out.add(Generator.genRandomIntBoxed(n, min, maxExclusivo, seedBase + r));
        return out;
    }

    public static java.util.Locale LOCALE = java.util.Locale.ROOT;

    public static double medirEImprimirCsv(String nome, SorterComparable sorter, List<Integer[]> bases,
                                           int n, String cenario, BufferedWriter csv) throws IOException {
        System.out.println("Algoritmo: " + nome);
        double soma = 0.0;
        for (int i = 0; i < bases.size(); i++) {
            Integer[] arr = bases.get(i).clone();
            long nanos = Timer.time(() -> sorter.sort(arr));
            double ms = nanos / 1e6;
            soma += ms;
            System.out.printf(LOCALE, "  tempo %d: %.3f ms%n", i+1, ms);
            csv.write(nome + "," + String.format(LOCALE, "%.3f", ms) + "," + n + "," + (i+1) + "," + cenario);
            csv.newLine();
        }
        double media = soma / bases.size();
        System.out.printf(LOCALE, "  média: %.3f ms%n", media);
        return media;
    }
}
