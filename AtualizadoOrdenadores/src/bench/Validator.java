package bench;

import java.util.Arrays;

/**
 * Conjunto de verificações para:
 * 1) Checar se o vetor está ORDENADO após o sort;
 * 2) Checar se o CONTEÚDO foi preservado (mesmo multiconjunto antes/depois).
 *    Isso ajuda a pegar bugs de "perda/duplicação" de elementos.
 */
public final class Validator {

    private Validator() { }

    // -------------------- Ordenado? (versões para T[], int[], double[]) --------------------

    /** Verifica se T[] (Comparable) está em ordem não-decrescente. */
    public static <T extends Comparable<T>> boolean isSorted(T[] a) {
        if (a == null || a.length < 2) return true; // vazio e unitário considerados ordenados
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1].compareTo(a[i]) > 0) return false;
        }
        return true;
    }

    /** Verifica se int[] está em ordem não-decrescente. */
    public static boolean isSorted(int[] a) {
        if (a == null || a.length < 2) return true;
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) return false;
        }
        return true;
    }

    /** Verifica se double[] está em ordem não-decrescente. */
    public static boolean isSorted(double[] a) {
        if (a == null || a.length < 2) return true;
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) return false;
        }
        return true;
    }

    // -------------------- Mesmo multiconjunto? (conteúdo preservado) --------------------

    /**
     * Compara multiconjuntos de T[]: copia, ordena e compara elemento a elemento.
     * Útil para garantir que o sort não "perde" nem "duplica" itens.
     */
    public static <T extends Comparable<T>> boolean sameMultiset(T[] before, T[] after) {
        if (before == null && after == null) return true;
        if (before == null || after == null) return false;
        if (before.length != after.length) return false;

        T[] b = Arrays.copyOf(before, before.length);
        T[] a = Arrays.copyOf(after,  after.length);
        Arrays.sort(b);
        Arrays.sort(a);
        return Arrays.equals(b, a);
    }

    /** Versão para int[]. */
    public static boolean sameMultiset(int[] before, int[] after) {
        if (before == null && after == null) return true;
        if (before == null || after == null) return false;
        if (before.length != after.length) return false;

        int[] b = Arrays.copyOf(before, before.length);
        int[] a = Arrays.copyOf(after,  after.length);
        Arrays.sort(b);
        Arrays.sort(a);
        return Arrays.equals(b, a);
    }

    /** Versão para double[]. */
    public static boolean sameMultiset(double[] before, double[] after) {
        if (before == null && after == null) return true;
        if (before == null || after == null) return false;
        if (before.length != after.length) return false;

        double[] b = Arrays.copyOf(before, before.length);
        double[] a = Arrays.copyOf(after,  after.length);
        Arrays.sort(b);
        Arrays.sort(a);
        // comparação direta de double é suficiente aqui (sem tolerância/epsilon por simplicidade)
        return Arrays.equals(b, a);
    }
}
