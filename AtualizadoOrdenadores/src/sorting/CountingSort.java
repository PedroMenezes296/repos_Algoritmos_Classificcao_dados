package sorting;

import bench.ColetorPassosInt;

/**
 * Counting Sort para inteiros em intervalo conhecido [min, max] (inclusivo).
 * - Estável quando reconstruído pela ordem original.
 * - O(n + K), onde K = max - min + 1.
 */
public final class CountingSort {
    private CountingSort() {}

    /** Ordena in-place o vetor 'valores' assumindo domínio [minValor, maxValor] (INCLUSIVO). */
    public static void sort(int[] valores, int minValor, int maxValor) {
        if (valores == null || valores.length < 2) return;
        if (minValor > maxValor) { int t = minValor; minValor = maxValor; maxValor = t; }

        final int tamanho = valores.length;
        final int faixa = maxValor - minValor + 1;

        int[] contagens = new int[faixa];
        for (int numero : valores) contagens[numero - minValor]++;

        // prefixo cumulativo para posições finais
        for (int i = 1; i < faixa; i++) contagens[i] += contagens[i - 1];

        int[] ordenado = new int[tamanho];
        // varre de trás p/ frente para ser estável
        for (int i = tamanho - 1; i >= 0; i--) {
            int numero = valores[i];
            int idxFaixa = numero - minValor;
            int posFinal = --contagens[idxFaixa];
            ordenado[posFinal] = numero;
        }

        // copia de volta
        System.arraycopy(ordenado, 0, valores, 0, tamanho);
    }

    /**
     * Demonstração do Counting Sort.
     * Emite até 'maxPassos' snapshots do vetor, após cada escrita no vetor final (durante a reconstrução).
     */
    public static void demo(int[] valores, ColetorPassosInt out, int maxPassos, int minValor, int maxValor) {
        if (valores == null || out == null || maxPassos <= 0 || valores.length < 2) return;
        if (minValor > maxValor) { int t = minValor; minValor = maxValor; maxValor = t; }

        final int tamanho = valores.length;
        final int faixa = maxValor - minValor + 1;

        int[] contagens = new int[faixa];
        for (int numero : valores) contagens[numero - minValor]++;

        for (int i = 1; i < faixa; i++) contagens[i] += contagens[i - 1];

        int[] ordenado = new int[tamanho];
        int passos = 0;

        for (int i = tamanho - 1; i >= 0 && passos < maxPassos; i--) {
            int numero = valores[i];
            int idxFaixa = numero - minValor;
            int posFinal = --contagens[idxFaixa];
            ordenado[posFinal] = numero;

            // snapshot: como ainda não copiamos para 'valores', mostramos o vetor "parcial" reconstruído
            int[] snapshot = ordenado.clone();
            out.passo(snapshot, "coloca " + numero + " na posição " + posFinal);
            passos++;
        }

        System.arraycopy(ordenado, 0, valores, 0, tamanho);
    }
}
