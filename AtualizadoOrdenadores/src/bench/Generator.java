package bench;

import java.util.Random;

public final class Generator {
    private Generator() { }

    /** Integer[] crescente 0..n-1 (para algoritmos comparativos com Comparable) */
    public static Integer[] genAscending(int n) {
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }

    /** Integer[] decrescente n-1..0 */
    public static Integer[] genDescending(int n) {
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) a[i] = n - 1 - i;
        return a;
    }

    /** Integer[] aleatório no intervalo [min, max] */
    public static Integer[] genRandomIntBoxed(int n, int min, int max, long seed) {
        Random r = new Random(seed);
        Integer[] a = new Integer[n];
        int bound = (max - min + 1);
        for (int i = 0; i < n; i++) a[i] = min + r.nextInt(bound);
        return a;
    }
}
