package sorting;

import bench.ColetorPassosDouble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bucket Sort para valores em [0,1).
 * Distribui em 'numBuckets' baldes, ordena cada balde (Collections.sort) e concatena.
 */
public final class BucketSort {
    private BucketSort() {}

    /** Ordena in-place os valores (assumindo [0,1)). */
    public static void sort(double[] valores, int numBuckets) {
        if (valores == null || valores.length < 2) return;
        if (numBuckets < 1) numBuckets = Math.max(1, valores.length / 2);

        @SuppressWarnings("unchecked")
        List<Double>[] buckets = new ArrayList[numBuckets];
        for (int i = 0; i < numBuckets; i++) buckets[i] = new ArrayList<>();

        // distribuição
        for (double v : valores) {
            int idx = indiceBucket(v, numBuckets);
            buckets[idx].add(v);
        }

        // ordena cada balde e concatena
        int k = 0;
        for (int i = 0; i < numBuckets; i++) {
            Collections.sort(buckets[i]);
            for (double v : buckets[i]) valores[k++] = v;
        }
    }

    /**
     * Demonstração do Bucket Sort.
     * Emite até 'maxPassos' snapshots: alguns durante a distribuição e durante a concatenação.
     */
    public static void demo(double[] valores, ColetorPassosDouble out, int maxPassos, int numBuckets) {
        if (valores == null || out == null || maxPassos <= 0 || valores.length < 2) return;
        if (numBuckets < 1) numBuckets = Math.max(1, valores.length / 2);

        @SuppressWarnings("unchecked")
        List<Double>[] buckets = new ArrayList[numBuckets];
        for (int i = 0; i < numBuckets; i++) buckets[i] = new ArrayList<>();

        int passos = 0;

        // distribuição com snapshots ocasionais
        for (int i = 0; i < valores.length && passos < maxPassos; i++) {
            double v = valores[i];
            int idx = indiceBucket(v, numBuckets);
            buckets[idx].add(v);

            // cria um "preview" do estado atual: valores já distribuídos ficam no início (ordenados por bucket)
            double[] preview = valores.clone();
            int k = 0;
            for (int b = 0; b < numBuckets; b++) {
                for (double x : buckets[b]) preview[k++] = x;
            }
            out.passo(preview, "distribuição: i=" + i + " → bucket " + idx);
            passos++;
        }

        // ordena cada balde
        for (int i = 0; i < numBuckets; i++) Collections.sort(buckets[i]);

        // concatena com snapshots
        int k = 0;
        for (int b = 0; b < numBuckets; b++) {
            for (double v : buckets[b]) {
                valores[k++] = v;
                if (passos < maxPassos) {
                    out.passo(valores.clone(), "concatenação: bucket=" + b);
                    passos++;
                }
            }
        }
    }

    private static int indiceBucket(double valor, int numBuckets) {
        if (valor < 0.0) valor = 0.0;
        if (valor >= 1.0) valor = Math.nextDown(1.0);
        int idx = (int) (valor * numBuckets);
        if (idx >= numBuckets) idx = numBuckets - 1;
        return idx;
    }
}
