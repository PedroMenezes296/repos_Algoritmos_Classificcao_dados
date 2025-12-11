package application;

import bench.RunnerTerminalBackup;
import bench.ColetorPassos;
import bench.ColetorPassosInt;
import bench.ColetorPassosDouble;
import bench.Generator;
import bench.Timer;

import sorting.Ordenadores;
import sorting.CountingSort;
import sorting.RadixSort;
import sorting.BucketSort;

import java.util.*;

public class Program {

    private static final Scanner SC = new Scanner(System.in);
    private static final Locale LOCALE = Locale.ROOT;

    public static void main(String[] args) {
        while (true) {
            limpa();
            System.out.println("===== MENU PRINCIPAL =====");
            System.out.println("1) Benchmark (RunnerTerminal) — escolher algoritmos e ver tempos");
            System.out.println("2) Demonstração passo a passo (com tempos e média)");
            System.out.println("0) Sair");
            System.out.print("Escolha: ");
            String opt = SC.nextLine().trim();

            if ("1".equals(opt)) {
                System.out.println("1) Rodar Metodos Não-Linares");
                System.out.println("2) Rodar Metodos Lineares");
                System.out.print("Escolha: ");
                String metodo = SC.nextLine().trim();
                if("1".equals(metodo)){
                new RunnerTerminalBackup().runInteractive();
                }else if("2".equals(metodo)){
                    // Implementar aqui o resto do codigo para os métodos Lineares  
                new RunnerTerminalBackup().runLinearInteractive();
                }else{
                    System.out.println("Opção inválida.");
                }
            } else if ("2".equals(opt)) {
                runSingleMethodDemoMenu();
                pause();
            } else if ("0".equals(opt)) {
                break;
            } else {
                System.out.println("Opção inválida.");
                pause();
            }
        }
        System.out.println("Encerrado.");
    }

    // =========================================================
    // 2) SUB-MENU: DEMO (com tempos por repetição e média)
    // =========================================================
    private static void runSingleMethodDemoMenu() {
        limpa();
        System.out.println("== Demonstração passo a passo ==");

        int n = escolherTamanho();
        long seedBase = lerLongOuPadrao("Seed base [padrão 42]: ", 42L);
        int maxPassos = lerIntOuPadrao("Máx. passos [padrão 20]: ", 20);
        int repeticoesTempo = lerIntOuPadrao("Repetições para medir tempo [padrão 5]: ", 5);

        System.out.println("\nEscolha o método:");
        System.out.println("1) Bubble Sort");
        System.out.println("2) Insertion Sort");
        System.out.println("3) Selection Sort");
        System.out.println("4) Shell Sort");
        System.out.println("5) Heap Sort");
        System.out.println("6) Merge Sort");
        System.out.println("7) Quick Sort");
        System.out.println("8) Counting Sort");
        System.out.println("9) Radix Sort");
        System.out.println("10) Bucket Sort");
        System.out.print("Escolha: ");
        String opt = SC.nextLine().trim();

        limpa();
        switch (opt) {
            case "1":  demoBubble(n, seedBase, maxPassos, repeticoesTempo);    break;
            case "2":  demoInsertion(n, seedBase, maxPassos, repeticoesTempo); break;
            case "3":  demoSelection(n, seedBase, maxPassos, repeticoesTempo); break;
            case "4":  demoShell(n, seedBase, maxPassos, repeticoesTempo);     break;
            case "5":  demoHeap(n, seedBase, maxPassos, repeticoesTempo);      break;
            case "6":  demoMerge(n, seedBase, maxPassos, repeticoesTempo);     break;
            case "7":  demoQuick(n, seedBase, maxPassos, repeticoesTempo);     break;
            case "8":  demoCounting(n, seedBase, maxPassos, repeticoesTempo);  break;
            case "9":  demoRadix(n, seedBase, maxPassos, repeticoesTempo);     break;
            case "10": demoBucket(n, seedBase, maxPassos, repeticoesTempo);    break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    // ---------------------- Demos (com tempos e média) ----------------------

    private static void demoBubble(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("BUBBLE SORT — n=" + n);
        Integer[] base = Generator.genRandomIntBoxed(n, 0, n, seedBase);

        System.out.println("Passo 0: " + preview(base));
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);
        ColetorPassos<Integer> out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num +": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        Integer[] demoArr = base.clone();
        Ordenadores.demoBubble(demoArr, out, maxPassos);

        System.out.println("--------- final (vetor ordenado) ------------");
        Integer[] finalArr = base.clone();
        Ordenadores.bubbleSort(finalArr);
        System.out.println(preview(finalArr));

        medirRepeticoes("bubble", n, seedBase, repeticoesTempo, Ordenadores::bubbleSort);
    }

    private static void demoInsertion(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("INSERTION SORT — n=" + n);
        Integer[] base = Generator.genRandomIntBoxed(n, 0, n, seedBase);

        System.out.println("Passo 0: " + preview(base));
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);
        ColetorPassos<Integer> out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num +": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        Integer[] demoArr = base.clone();
        Ordenadores.demoInsertion(demoArr, out, maxPassos);

        System.out.println("--------- final (vetor ordenado) ------------");
        Integer[] finalArr = base.clone();
        Ordenadores.insertionSort(finalArr);
        System.out.println(preview(finalArr));

        medirRepeticoes("insertion", n, seedBase, repeticoesTempo, Ordenadores::insertionSort);
    }

    private static void demoSelection(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("SELECTION SORT — n=" + n);
        Integer[] base = Generator.genRandomIntBoxed(n, 0, n, seedBase);

        System.out.println("Passo 0: " + preview(base));
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);
        ColetorPassos<Integer> out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num +": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        Integer[] demoArr = base.clone();
        Ordenadores.demoSelection(demoArr, out, maxPassos);

        System.out.println("--------- final (vetor ordenado) ------------");
        Integer[] finalArr = base.clone();
        Ordenadores.selectionSort(finalArr);
        System.out.println(preview(finalArr));

        medirRepeticoes("selection", n, seedBase, repeticoesTempo, Ordenadores::selectionSort);
    }

    private static void demoShell(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("SHELL SORT — n=" + n);
        Integer[] base = Generator.genRandomIntBoxed(n, 0, n, seedBase);

        System.out.println("Passo 0: " + preview(base));
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);
        ColetorPassos<Integer> out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num +": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        Integer[] demoArr = base.clone();
        Ordenadores.demoShell(demoArr, out, maxPassos); // agora respeita maxPassos

        System.out.println("--------- final (vetor ordenado) ------------");
        Integer[] finalArr = base.clone();
        Ordenadores.shellSort(finalArr);
        System.out.println(preview(finalArr));

        medirRepeticoes("shell", n, seedBase, repeticoesTempo, Ordenadores::shellSort);
    }

    private static void demoHeap(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("HEAP SORT — n=" + n);
        Integer[] base = Generator.genRandomIntBoxed(n, 0, n, seedBase);

        System.out.println("Passo 0: " + preview(base));
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);
        ColetorPassos<Integer> out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num +": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        Integer[] demoArr = base.clone();
        Ordenadores.demoHeap(demoArr, out, maxPassos);

        System.out.println("--------- final (vetor ordenado) ------------");
        Integer[] finalArr = base.clone();
        Ordenadores.heapSort(finalArr);
        System.out.println(preview(finalArr));

        medirRepeticoes("heap", n, seedBase, repeticoesTempo, Ordenadores::heapSort);
    }

    private static void demoMerge(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("MERGE SORT — n=" + n);
        Integer[] base = Generator.genRandomIntBoxed(n, 0, n, seedBase);

        System.out.println("Passo 0: " + preview(base));
        // contador mutável para numerar os passos emitidos pela demo
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);
        ColetorPassos<Integer> out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num +": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        Integer[] demoArr = base.clone();
        Ordenadores.demoMerge(demoArr, out, maxPassos);

        System.out.println("--------- final (vetor ordenado) ------------");
        Integer[] finalArr = base.clone();
        Ordenadores.mergeSort(finalArr);
        System.out.println(preview(finalArr));

        medirRepeticoes("merge", n, seedBase, repeticoesTempo, Ordenadores::mergeSort);
    }

    private static void demoQuick(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("QUICK SORT — n=" + n);
        Integer[] base = Generator.genRandomIntBoxed(n, 0, n, seedBase);

        System.out.println("Passo 0: " + preview(base));
        // contador mutável para numerar os passos emitidos pela demo
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);
        ColetorPassos<Integer> out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num +": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        Integer[] demoArr = base.clone();
        Ordenadores.demoQuick(demoArr, out, maxPassos);

        System.out.println("--------- final (vetor ordenado) ------------");
        Integer[] finalArr = base.clone();
        Ordenadores.quickSort(finalArr);
        System.out.println(preview(finalArr));

        medirRepeticoes("quick", n, seedBase, repeticoesTempo, Ordenadores::quickSort);
    }

    private static void demoCounting(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("COUNTING SORT — n=" + n);
        int min = 0, max = Math.max(10, n);
        int[] base = genIntArray(n, min, max, seedBase);

        System.out.println("Passo 0: " + preview(base));
        // contador mutável para numerar os passos emitidos pela demo
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);
        ColetorPassosInt out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num +": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        int[] demoArr = base.clone();
        CountingSort.demo(demoArr, out, maxPassos, min, max);

        System.out.println("--------- final (vetor ordenado) ------------");
        int[] finalArr = base.clone();
        CountingSort.sort(finalArr, min, max);
        System.out.println(preview(finalArr));

        double somaMs = 0.0;
        for (int i = 0; i < repeticoesTempo; i++) {
            int[] arr = genIntArray(n, min, max, seedBase + i);
            long nanos = Timer.time(() -> CountingSort.sort(arr, min, max));
            double ms = nanos / 1e6;
            somaMs += ms;
            System.out.printf(LOCALE, "  tempo %d: %.3f ms%n", i + 1, ms);
        }
        System.out.printf(LOCALE, "  média: %.3f ms%n", somaMs / repeticoesTempo);
    }

    private static void demoRadix(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("RADIX SORT (base 10) — n=" + n);
        int[] base = genIntArray(n, 0, Math.max(10, n), seedBase);

        System.out.println("Passo 0: " + preview(base));

        // contador mutável para numerar os passos emitidos pela demo
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);

        ColetorPassosInt out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num + ": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        int[] demoArr = base.clone();
        RadixSort.demo(demoArr, out, maxPassos, 10);
            
        System.out.println("--------- final (vetor ordenado) ------------");
        int[] finalArr = base.clone();
        RadixSort.sort(finalArr, 10);
        System.out.println(preview(finalArr));

        double somaMs = 0.0;
        for (int i = 0; i < repeticoesTempo; i++) {
            int[] arr = genIntArray(n, 0, Math.max(10, n), seedBase + i);
            long nanos = Timer.time(() -> RadixSort.sort(arr, 10));
            double ms = nanos / 1e6;
            somaMs += ms;
            System.out.printf(LOCALE, "  tempo %d: %.3f ms%n", i + 1, ms);
        }
        System.out.printf(LOCALE, "  média: %.3f ms%n", somaMs / repeticoesTempo);
    }

    private static void demoBucket(int n, long seedBase, int maxPassos, int repeticoesTempo) {
        System.out.println("BUCKET SORT (100 buckets) — n=" + n);
        double[] base = genDouble01(n, seedBase);

        System.out.println("Passo 0: " + preview(base));
        // contador mutável para numerar os passos emitidos pela demo
        java.util.concurrent.atomic.AtomicInteger passo = new java.util.concurrent.atomic.AtomicInteger(1);
        ColetorPassosDouble out = (snap, desc) ->{
            int num = passo.getAndIncrement(); // 1, 2, 3, ...
            System.out.println("Passo "+ num + ": " + preview(snap) + (desc == null || desc.isEmpty() ? "" : "  // " + desc));
        };
        double[] demoArr = base.clone();
        BucketSort.demo(demoArr, out, maxPassos, 100);

        System.out.println("--------- final (vetor ordenado) ------------");
        double[] finalArr = base.clone();
        BucketSort.sort(finalArr, 100);
        System.out.println(preview(finalArr));

        double somaMs = 0.0;
        for (int i = 0; i < repeticoesTempo; i++) {
            double[] arr = genDouble01(n, seedBase + i);
            long nanos = Timer.time(() -> BucketSort.sort(arr, 100));
            double ms = nanos / 1e6;
            somaMs += ms;
            System.out.printf(LOCALE, "  tempo %d: %.3f ms%n", i + 1, ms);
        }
        System.out.printf(LOCALE, "  média: %.3f ms%n", somaMs / repeticoesTempo);
    }

    // ---------------------- utils visuais e geração ----------------------

    private static String preview(Integer[] v) {
        int show = Math.min(v.length, 30);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < show; i++) { sb.append(v[i]); if (i < show - 1) sb.append(", "); }
        if (v.length > show) sb.append(", ... (n=").append(v.length).append(")");
        sb.append("]");
        return sb.toString();
    }
    private static String preview(int[] v) {
        int show = Math.min(v.length, 30);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < show; i++) { sb.append(v[i]); if (i < show - 1) sb.append(", "); }
        if (v.length > show) sb.append(", ... (n=").append(v.length).append(")");
        sb.append("]");
        return sb.toString();
    }
    private static String preview(double[] v) {
        int show = Math.min(v.length, 30);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < show; i++) {
            sb.append(String.format(LOCALE, "%.3f", v[i]));
            if (i < show - 1) sb.append(", ");
        }
        if (v.length > show) sb.append(", ... (n=").append(v.length).append(")");
        sb.append("]");
        return sb.toString();
    }

    private static int[] genIntArray(int n, int minInclusive, int maxInclusive, long seed) {
        Random rnd = new Random(seed);
        int[] a = new int[n];
        int bound = (maxInclusive - minInclusive + 1);
        for (int i = 0; i < n; i++) a[i] = minInclusive + rnd.nextInt(bound);
        return a;
    }
    private static double[] genDouble01(int n, long seed) {
        Random rnd = new Random(seed);
        double[] a = new double[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextDouble();
        return a;
    }

    // ---------------------- entrada/UX ----------------------

    private static int escolherTamanho() {
        final int[] opcoes = {100, 1_000, 10_000, 100_000, 1_000_000};
        while (true) {
            System.out.println("Escolha o tamanho do vetor (aleatório):");
            System.out.println("1) 100");
            System.out.println("2) 1.000");
            System.out.println("3) 10.000");
            System.out.println("4) 100.000");
            System.out.println("5) 1.000.000");
            System.out.println("6) Outro (digitar)");
            System.out.print("Opção: ");
            String s = SC.nextLine().trim();
            switch (s) {
                case "1": return opcoes[0];
                case "2": return opcoes[1];
                case "3": return opcoes[2];
                case "4": return opcoes[3];
                case "5": return opcoes[4];
                case "6":
                    int n = lerIntOuPadrao("Informe n (>=1): ", 100);
                    if (n >= 1) return n;
                    System.out.println("Valor inválido. Tente novamente.\n");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.\n");
            }
        }
    }

    private static int lerIntOuPadrao(String prompt, int padrao) {
        System.out.print(prompt);
        String s = SC.nextLine().trim();
        if (s.isEmpty()) return padrao;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, usando padrão: " + padrao);
            return padrao;
        }
    }

    private static long lerLongOuPadrao(String prompt, long padrao) {
        System.out.print(prompt);
        String s = SC.nextLine().trim();
        if (s.isEmpty()) return padrao;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, usando padrão: " + padrao);
            return padrao;
        }
    }

    private static void limpa() {
        System.out.println("\n".repeat(10));
    }
    private static void pause() {
        System.out.println("\nPressione ENTER para continuar...");
        SC.nextLine();
    }
    // ... suas outras funções utilitárias aqui ...

    // Mede N repetições e imprime cada tempo + média (para Integer[])
    private static void medirRepeticoes(
            String nomeAlgoritmo,
            int tamanho,
            long seedBase,
            int repeticoes,
            java.util.function.Consumer<Integer[]> sorter) {

        System.out.println("Tempos (" + nomeAlgoritmo + "):");
        double somaMs = 0.0;

        for (int i = 0; i < repeticoes; i++) {
            long seed = seedBase + i;
            Integer[] arr = bench.Generator.genRandomIntBoxed(tamanho, 0, tamanho, seed);

            long nanos = bench.Timer.time(() -> sorter.accept(arr));
            double ms = nanos / 1e6;

            somaMs += ms;
            System.out.printf(java.util.Locale.ROOT, "  tempo %d: %.3f ms%n", i + 1, ms);
        }

        double media = somaMs / repeticoes;
        System.out.printf(java.util.Locale.ROOT, "  média: %.3f ms%n", media);
    }
    
} // <- ESTE é o '}' final da classe Program


