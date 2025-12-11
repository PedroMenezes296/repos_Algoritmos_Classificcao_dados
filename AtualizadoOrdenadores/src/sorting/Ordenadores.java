package sorting;

import bench.ColetorPassos;

public final class Ordenadores {

    private Ordenadores() { }

    // ----------------------- MÉTODOS REAIS -----------------------

    public static <T extends Comparable<? super T>> void bubbleSort(T[] a) {
        if (a == null || a.length < 2) return;
        int n = a.length;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (a[i - 1].compareTo(a[i]) > 0) {
                    swap(a, i - 1, i);
                    swapped = true;
                }
            }
            n--; // último já está na posição
        } while (swapped);
    }

    /** Insertion Sort (estável), in-place. */
    public static <T extends Comparable<? super T>> void insertionSort(T[] a) {
        if (a == null || a.length < 2) return;

        for (int i = 1; i < a.length; i++) {
            T chave = a[i];
            int j = i - 1;
            while (j >= 0 && a[j].compareTo(chave) > 0) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = chave;
        }
    }

    public static <T extends Comparable<? super T>> void selectionSort(T[] a) {
        if (a == null || a.length < 2) return;
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j].compareTo(a[min]) < 0) min = j;
            }
            if (min != i) swap(a, i, min);
        }
    }

    // ---------- Shell sort (gaps de Ciura + extensão) ----------
    public static <T extends Comparable<? super T>> void shellSort(T[] a) {
        if (a == null || a.length < 2) return;
        final int n = a.length;

        int[] gaps = ciuraGapsUpTo(n);
        for (int g = gaps.length - 1; g >= 0; g--) {
            int h = gaps[g];
            for (int i = h; i < n; i++) {
                T key = a[i];
                int j = i;
                while (j >= h && a[j - h].compareTo(key) > 0) {
                    a[j] = a[j - h];
                    j -= h;
                }
                a[j] = key;
            }
        }
    }

    private static int[] ciuraGapsUpTo(int n) {
        int[] base = {1, 4, 10, 23, 57, 132, 301, 701};
        java.util.ArrayList<Integer> gs = new java.util.ArrayList<>(base.length + 8);
        for (int v : base) if (v < n) gs.add(v);
        int last = gs.isEmpty() ? 1 : gs.get(gs.size() - 1);
        while (last < n) {
            last = (int) Math.floor(last * 2.25);
            if (last < n) gs.add(last);
        }
        if (gs.isEmpty()) gs.add(1);
        int[] out = new int[gs.size()];
        for (int i = 0; i < out.length; i++) out[i] = gs.get(i);
        return out;
    }

    // ---------- Heap sort ----------
    public static <T extends Comparable<? super T>> void heapSort(T[] a) {
        if (a == null || a.length < 2) return;
        final int n = a.length;

        for (int i = (n >>> 1) - 1; i >= 0; i--) {
            siftDown(a, i, n);
        }
        for (int end = n - 1; end > 0; end--) {
            swap(a, 0, end);
            siftDown(a, 0, end);
        }
    }

    private static <T extends Comparable<? super T>> void siftDown(T[] a, int i, int heapSize) {
        while (true) {
            int left = (i << 1) + 1;
            if (left >= heapSize) break;
            int right = left + 1;
            int child = left;
            if (right < heapSize && a[right].compareTo(a[left]) > 0) child = right;

            if (a[child].compareTo(a[i]) > 0) {
                swap(a, i, child);
                i = child;
            } else break;
        }
    }

    // ---------- Merge sort (bottom-up, estável) ----------
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> void mergeSort(T[] a) {
        if (a == null || a.length < 2) return;
        final int n = a.length;

        T[] src = a;
        T[] dst = a.clone(); // buffer

        for (int width = 1; width < n; width <<= 1) {
            for (int i = 0; i < n; i += (width << 1)) {
                int mid = Math.min(i + width, n);
                int hi  = Math.min(i + (width << 1), n);
                mergeRuns(src, dst, i, mid, hi);
            }
            T[] tmp = src; src = dst; dst = tmp;
        }
        if (src != a) System.arraycopy(src, 0, a, 0, n);
    }

    private static <T extends Comparable<? super T>>
    void mergeRuns(T[] src, T[] dst, int lo, int mid, int hi) {
        int i = lo, j = mid, k = lo;
        while (i < mid && j < hi) {
            if (src[i].compareTo(src[j]) <= 0) dst[k++] = src[i++];
            else                               dst[k++] = src[j++];
        }
        while (i < mid) dst[k++] = src[i++];
        while (j < hi)  dst[k++] = src[j++];
    }

    // ---------- Quick sort (mediana-de-três + Hoare + cutoff p/ inserção) ----------
    public static <T extends Comparable<? super T>> void quickSort(T[] a) {
        if (a == null || a.length < 2) return;
        quick(a, 0, a.length - 1);
    }

    private static final int INSERTION_CUTOFF = 24;

    private static <T extends Comparable<? super T>> void quick(T[] a, int lo, int hi) {
        while (hi - lo + 1 > INSERTION_CUTOFF) {
            int m = medianOf3(a, lo, lo + ((hi - lo) >>> 1), hi);
            T pivot = a[m];
            int i = lo - 1, j = hi + 1;
            while (true) {
                do { i++; } while (a[i].compareTo(pivot) < 0);
                do { j--; } while (a[j].compareTo(pivot) > 0);
                if (i >= j) break;
                swap(a, i, j);
            }
            if (j - lo < hi - (j + 1)) {
                quick(a, lo, j);
                lo = j + 1;
            } else {
                quick(a, j + 1, hi);
                hi = j;
            }
        }
        insertionRange(a, lo, hi);
    }

    private static <T extends Comparable<? super T>> int medianOf3(T[] a, int i, int j, int k) {
        if (a[i].compareTo(a[j]) > 0) { int t=i; i=j; j=t; }
        if (a[j].compareTo(a[k]) > 0) { int t=j; j=k; k=t; }
        if (a[i].compareTo(a[j]) > 0) { int t=i; i=j; j=t; }
        return j;
    }

    private static <T extends Comparable<? super T>> void insertionRange(T[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            T key = a[i];
            int j = i - 1;
            while (j >= lo && a[j].compareTo(key) > 0) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    private static void swap(Object[] a, int i, int j) {
        Object t = a[i]; a[i] = a[j]; a[j] = t;
    }

    // ----------------------- DEMOS (passo-a-passo) -----------------------

    // Bubble: emite alguns swaps da varredura
    public static void demoBubble(Integer[] a, ColetorPassos<Integer> out, int maxPassos) {
        if (a == null || out == null || maxPassos <= 0) return;
        int n = a.length, passos = 0;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n && passos < maxPassos; i++) {
                if (a[i - 1] > a[i]) {
                    swap(a, i - 1, i);
                    swapped = true;
                    passos++;
                    out.passo(a.clone(), "swap " + (i - 1) + "<->" + i);
                }
            }
            n--;
        } while (swapped && passos < maxPassos);
    }

    // Selection: mostra a escolha do mínimo e a troca
    public static void demoSelection(Integer[] a, ColetorPassos<Integer> out, int maxPassos) {
        if (a == null || out == null || maxPassos <= 0) return;
        int n = a.length, passos = 0;
        for (int i = 0; i < n - 1 && passos < maxPassos; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) if (a[j] < a[min]) min = j;
            if (min != i) {
                swap(a, i, min);
                passos++;
                out.passo(a.clone(), "coloca menor em i=" + i + " (min=" + min + ")");
            }
        }
    }

    public static void demoInsertion(Integer[] a, ColetorPassos<Integer> out, int maxPassos) {
        if (a == null || out == null || maxPassos <= 0) return;
        if (a.length < 2) return;

        int passos = 0;
        for (int i = 1; i < a.length && passos < maxPassos; i++) {
            int key = a[i];
            int j = i - 1;
            int desloc = 0;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
                desloc++;
            }
            a[j + 1] = key;
            passos++;
            String desc = (desloc == 0)
                    ? ("inseriu " + key + " (já no lugar)")
                    : ("inseriu " + key + " em " + (j + 1) + " (deslocamentos=" + desloc + ")");
            out.passo(a.clone(), desc);
        }
    }

    public static void demoShell(Integer[] a, bench.ColetorPassos<Integer> out, int maxPassos) {
        final int n = a.length;
        if (out == null || n < 2 || maxPassos <= 0) return;

        // helper local para emitir passo com corte
        final java.util.concurrent.atomic.AtomicInteger passos = new java.util.concurrent.atomic.AtomicInteger(0);

        java.util.function.BiFunction<Integer[], String, Boolean> emit = (arr, desc) -> {
            if (passos.get() >= maxPassos) return false;
            out.passo(arr.clone(), desc);
            return passos.incrementAndGet() < maxPassos;
        };


        int[] gaps = ciuraGapsUpTo(n);
        if (!emit.apply(a, "início")) return;

        for (int g = gaps.length - 1; g >= 0; g--) {
            int h = gaps[g];
            if (!emit.apply(a, "gap h=" + h)) return;

            for (int i = h; i < n; i++) {
                int key = a[i];
                int j = i;
                while (j >= h && a[j - h] > key) {
                    a[j] = a[j - h];
                    j -= h;
                    if (!emit.apply(a, "move j=" + j + " (h=" + h + ")")) return;
                }
                a[j] = key;
                if (!emit.apply(a, "insere key em j=" + j + " (h=" + h + ")")) return;
            }
        }
        emit.apply(a, "fim");
    }


    // Limita a quantidade de snapshots emitidos a 'maxPassos'
    public static void demoHeap(Integer[] a, bench.ColetorPassos passos, int maxPassos) {
        final int n = a.length;
        if (passos == null || n < 2 || maxPassos <= 0) return;

        // contador mutável para ser usado dentro da lambda (evita erro de "effectively final")
        final int[] passo = {0};

        // emissor com corte em maxPassos
        java.util.function.BiFunction<Integer[], String, Boolean> emit = (arr, desc) -> {
            if (passo[0] >= maxPassos) return false;
            passos.passo(arr.clone(), desc);
            passo[0]++;
            return passo[0] < maxPassos;
        };

        // heapify
        if (!emit.apply(a, "inicio heapify")) return;
        for (int i = (n >>> 1) - 1; i >= 0; i--) {
            if (!siftDownDemo(a, i, n, emit, maxPassos, passo)) return;
        }

        // sort
        if (!emit.apply(a, "inicio sort")) return;
        for (int end = n - 1; end > 0; end--) {
            swap(a, 0, end);
            if (!emit.apply(a, "swap raiz com " + end)) return;
            if (!siftDownDemo(a, 0, end, emit, maxPassos, passo)) return;
        }
    }

    private static boolean siftDownDemo(
            Integer[] a,
            int i,
            int heapSize,
            java.util.function.BiFunction<Integer[], String, Boolean> emit,
            int maxPassos,
            int[] passo) {

        while (true) {
            int left = (i << 1) + 1;
            if (left >= heapSize) break;

            int right = left + 1;
            int child = (right < heapSize && a[right] > a[left]) ? right : left;

            if (a[child] > a[i]) {
                swap(a, i, child);
                if (!emit.apply(a, "sift i=" + i + " -> " + child)) return false;
                i = child;

                // corta se atingiu o máximo
                if (passo[0] >= maxPassos) return false;
            } else {
                break;
            }
        }
        return true;
    }

    // util simples
    private static void swap(Integer[] a, int i, int j) {
        Integer t = a[i]; a[i] = a[j]; a[j] = t;
    }


    public static void demoMerge(Integer[] a, ColetorPassos<Integer> out, int maxPassos) {
        if (a == null || out == null || maxPassos <= 0) return;
        final int n = a.length;
        if (n < 2) { out.passo(a.clone(), "início/fim"); return; }

        out.passo(a.clone(), "início");
        Integer[] src = a.clone();
        Integer[] dst = a.clone();
        int passos = 1;

        for (int width = 1; width < n && passos < maxPassos; width <<= 1) {
            for (int i = 0; i < n && passos < maxPassos; i += (width << 1)) {
                int mid = Math.min(i + width, n);
                int hi  = Math.min(i + (width << 1), n);
                int p = i, q = mid, k = i;

                while (k < hi && passos < maxPassos) {
                    if (q >= hi || (p < mid && src[p] <= src[q])) dst[k++] = src[p++];
                    else                                          dst[k++] = src[q++];
                    out.passo(dst.clone(), "merge [" + i + "," + mid + ") + [" + mid + "," + hi + ") (width=" + width + ")");
                    passos++;
                }
            }
            Integer[] tmp = src; src = dst; dst = tmp;
            if (passos < maxPassos) { out.passo(src.clone(), "após pass width=" + (Integer.highestOneBit(width) << 1)); passos++; }
        }
        System.arraycopy(src, 0, a, 0, n);
        if (passos < maxPassos) out.passo(a.clone(), "fim");
    }

    public static void demoQuick(Integer[] a, ColetorPassos<Integer> out, int maxPassos) {
        if (a == null || out == null || maxPassos <= 0) return;
        final int n = a.length;
        if (n < 2) { out.passo(a.clone(), "início/fim"); return; }

        out.passo(a.clone(), "início");
        int[] cnt = {0};
        demoQuickRec(a, 0, n - 1, out, cnt, maxPassos);
        if (cnt[0] < maxPassos) out.passo(a.clone(), "fim");
    }

    private static void demoQuickRec(Integer[] a, int lo, int hi,
                                     ColetorPassos<Integer> out, int[] cnt, int max) {
        if (lo >= hi || cnt[0] >= max) return;

        int m = lo + ((hi - lo) >>> 1);
        int pidx = idxMedianOf3(a, lo, m, hi);
        int pivot = a[pidx];
        if (cnt[0] < max) { out.passo(a.clone(), "pivot=" + pivot + " (mediana de três) ["+lo+","+hi+"]"); cnt[0]++; }

        int i = lo - 1, j = hi + 1;
        while (cnt[0] < max) {
            do { i++; } while (a[i] < pivot);
            do { j--; } while (a[j] > pivot);
            if (i >= j) {
                demoQuickRec(a, lo, j, out, cnt, max);
                demoQuickRec(a, j + 1, hi, out, cnt, max);
                return;
            }
            swap(a, i, j);
            out.passo(a.clone(), "swap i=" + i + " j=" + j + " (pivot=" + pivot + ")");
            cnt[0]++;
        }
    }

    private static int idxMedianOf3(Integer[] a, int i, int j, int k) {
        Integer ai = a[i], aj = a[j], ak = a[k];
        if (ai.compareTo(aj) > 0) { int t=i; i=j; j=t; Integer tmp=ai; ai=aj; aj=tmp; }
        if (aj.compareTo(ak) > 0) { int t=j; j=k; k=t; Integer tmp=aj; aj=ak; ak=tmp; }
        if (ai.compareTo(aj) > 0) { int t=i; i=j; j=t; }
        return j;
    }

    private static void sleep(int ms){ try { Thread.sleep(ms); } catch (InterruptedException ignored) {} }
}
