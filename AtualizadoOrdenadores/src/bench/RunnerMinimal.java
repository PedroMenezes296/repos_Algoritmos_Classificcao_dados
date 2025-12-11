package bench;

import java.util.*;

/**
 * Runner minimalista para testar rapidamente seus métodos:
 * - Registre seus algoritmos comparativos (Comparable) via adicionarComparativo(...)
 * - Rode com rodarAleatorio/rodarCrescente/rodarDecrescente informando tamanhos e repetições
 * - Ele imprime tempos individuais e média, e valida se ficou ordenado
 *
 * Obs.: SEM exceções; imprime mensagens de aviso quando algo está faltando.
 */
public final class RunnerMinimal {

    // ---- contrato mínimo p/ comparativos (igual ao que você usará em Ordenadores) ----
    @FunctionalInterface
    public interface SorterComparable {
        <T extends Comparable<? super T>> void sort(T[] a);
    }

    // Nome -> função de ordenação
    private final LinkedHashMap<String, SorterComparable> comparativos = new LinkedHashMap<>();

    /** Registra um algoritmo comparativo (ex.: "merge", Ordenadores::mergeSort). */
    public RunnerMinimal adicionarComparativo(String nome, SorterComparable sorter) {
        comparativos.put(nome, sorter);
        return this;
    }

    // ------------------------- EXECUTORES PRONTOS -------------------------

    /** Roda em entradas ALEATÓRIAS (mesma base por tamanho) e imprime tempos. */
    public void rodarAleatorio(int[] tamanhos, int repeticoes, long seed) {
        if (comparativos.isEmpty()) {
            System.out.println("[AVISO] Nenhum algoritmo registrado. Registre com adicionarComparativo(...).");
            return;
        }
        System.out.println("=== ENTRADA ALEATÓRIA === (repeticoes=" + repeticoes + ", seed=" + seed + ")");
        for (int n : tamanhos) {
            Integer[] base = bench.Generator.genRandomIntBoxed(n, 0, Math.max(1, n), seed);
            rodarEmBase("RAND", base, repeticoes);
        }
    }

    /** Roda em entrada CRESCENTE. */
    public void rodarCrescente(int[] tamanhos, int repeticoes) {
        if (comparativos.isEmpty()) {
            System.out.println("[AVISO] Nenhum algoritmo registrado. Registre com adicionarComparativo(...).");
            return;
        }
        System.out.println("=== ENTRADA CRESCENTE === (repeticoes=" + repeticoes + ")");
        for (int n : tamanhos) {
            Integer[] base = bench.Generator.genAscending(n);
            rodarEmBase("ASC", base, repeticoes);
        }
    }

    /** Roda em entrada DECRESCENTE. */
    public void rodarDecrescente(int[] tamanhos, int repeticoes) {
        if (comparativos.isEmpty()) {
            System.out.println("[AVISO] Nenhum algoritmo registrado. Registre com adicionarComparativo(...).");
            return;
        }
        System.out.println("=== ENTRADA DECRESCENTE === (repeticoes=" + repeticoes + ")");
        for (int n : tamanhos) {
            Integer[] base = bench.Generator.genDescending(n);
            rodarEmBase("DESC", base, repeticoes);
        }
    }

    // ------------------------- CORE: roda num único vetor-base -------------------------

    private void rodarEmBase(String rotulo, Integer[] base, int repeticoes) {
        int n = base.length;
        System.out.println("\n[Input=" + rotulo + " | n=" + n + "]");
        System.out.println(preencher("Algoritmo", 14) + preencher("Tempos (ns)", 40) + "Média (ns)   OK?");
        System.out.println(repetir('-', 14 + 40 + 12));

        for (Map.Entry<String, SorterComparable> e : comparativos.entrySet()) {
            String nome = e.getKey();
            SorterComparable sorter = e.getValue();

            List<Long> tempos = new ArrayList<>(repeticoes);
            boolean okTodos = true;

            // mesma base para todos; clonar a cada repetição
            for (int rep = 0; rep < repeticoes; rep++) {
                Integer[] a = Arrays.copyOf(base, base.length);

                long dt = bench.Timer.time(() -> sorter.sort(a));
                tempos.add(dt);

                // validação simples: está ordenado?
                if (!estaOrdenado(a)) okTodos = false;
            }

            double avg = media(tempos);
            System.out.println(
                preencher(nome, 14) +
                preencher(juntar(tempos), 40) +
                preencher(String.format(java.util.Locale.ROOT, "%.0f", avg), 12) +
                (okTodos ? "OK" : "FAIL")
            );
        }
        System.out.println();
    }

    // ------------------------- utilitários (simples) -------------------------

    private static boolean estaOrdenado(Integer[] a) {
        if (a == null || a.length < 2) return true;
        for (int i = 1; i < a.length; i++) if (a[i - 1].compareTo(a[i]) > 0) return false;
        return true;
    }

    private static double media(List<Long> xs) {
        if (xs == null || xs.isEmpty()) return Double.NaN;
        long s = 0L;
        for (Long x : xs) s += x;
        return s / (double) xs.size();
    }

    private static String juntar(List<Long> xs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < xs.size(); i++) {
            sb.append(xs.get(i));
            if (i < xs.size() - 1) sb.append(" ");
        }
        return sb.toString();
    }

    private static String preencher(String s, int largura) {
        if (s == null) s = "";
        if (s.length() >= largura) return s;
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < largura) sb.append(' ');
        return sb.toString();
    }

    private static String repetir(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(c);
        return sb.toString();
    }
}
