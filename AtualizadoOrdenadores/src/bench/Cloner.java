package bench;

import java.util.Arrays;

/**
 * Fornece cópias (clones) dos arrays.
 * A ideia é garantir que cada algoritmo receba exatamente os mesmos dados de entrada.
 */
public final class Cloner {

    private Cloner() { } // Utilitário

    /** Cópia rasa de T[]. */
    public static <T> T[] copyOf(T[] a) {
        return (a == null) ? null : Arrays.copyOf(a, a.length);
    }

    /** Cópia de int[]. */
    public static int[] copyOf(int[] a) {
        return (a == null) ? null : Arrays.copyOf(a, a.length);
    }

    /** Cópia de double[]. */
    public static double[] copyOf(double[] a) {
        return (a == null) ? null : Arrays.copyOf(a, a.length);
    }
}
