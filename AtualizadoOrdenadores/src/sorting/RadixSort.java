package sorting;

import bench.ColetorPassosInt;

/**
 * Radix Sort (LSD) para inteiros NÃO-NEGATIVOS.
 * Base configurável (ex.: 10). Implementado via counting sort por dígito.
 */
public final class RadixSort {
    private RadixSort() {}

    /** Ordena in-place 'valores' assumindo apenas NÃO-NEGATIVOS. */
    public static void sort(int[] valores, int base) {
        if (valores == null || valores.length < 2) return;
        if (base < 2) base = 10;

        int maxValor = max(valores);
        int exp = 1; // 1, base, base^2, ...
        int[] auxiliar = new int[valores.length];

        while (maxValor / exp > 0) {
            countingPorDigito(valores, auxiliar, base, exp);
            exp *= base;
        }
    }

    /**
     * Demonstração do Radix LSD. Emite snapshot após CADA PASSO DE DÍGITO (não a cada elemento),
     * limitando por 'maxPassos'.
     */
    public static void demo(int[] valores, ColetorPassosInt out, int maxPassos, int base) {
        if (valores == null || out == null || maxPassos <= 0 || valores.length < 2) return;
        if (base < 2) base = 10;

        int maxValor = max(valores);
        int exp = 1; 
        int[] auxiliar = new int[valores.length];
        int passos = 0;

        while (maxValor / exp > 0 && passos < maxPassos) {
            countingPorDigito(valores, auxiliar, base, exp);

            out.passo(valores.clone(), "após dígito exp=" + exp + " (base=" + base + ")");
            passos++;

            exp *= base;
        }
    }

    // ---------- helpers internos ----------

    private static int max(int[] a) {
        int m = 0;
        for (int v : a) if (v > m) m = v;
        return m;
    }

    /** Counting sort estável por um dígito (exp define o dígito atual). */
    private static void countingPorDigito(int[] valores, int[] auxiliar, int base, int exp) {
        int[] contagens = new int[base];

        for (int v : valores) contagens[(v / exp) % base]++;

        for (int i = 1; i < base; i++) contagens[i] += contagens[i - 1];

        for (int i = valores.length - 1; i >= 0; i--) {
            int v = valores[i];
            int dig = (v / exp) % base;
            auxiliar[--contagens[dig]] = v;
        }

        System.arraycopy(auxiliar, 0, valores, 0, valores.length);
    }
}
