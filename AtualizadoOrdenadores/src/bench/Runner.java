package bench;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Orquestrador dos experimentos.
 * - Recebe um Scenario e um "SortRegistry" (catálogo de funções de ordenação).
 * - Gera o vetor-base apropriado, clona antes de cada repetição,
 * - Invoca o sorter registrado, mede com Timer, valida com Validator,
 * - Retorna um Result com tempos e flags de validação.
 *
 * Observação: projetado para funcionar MESMO antes de implementar os ordenadores.
 * Basta registrar placeholders (ex.: Arrays.sort) e o fluxo já funciona.
 */
public final class Runner {

    // Conjunto de algoritmos "comparativos" (trabalham com T extends Comparable).
    private static final EnumSet<Scenario.Algorithm> COMPARATIVES =
            EnumSet.of(Scenario.Algorithm.BUBBLE, Scenario.Algorithm.INSERTION,
                       Scenario.Algorithm.SELECTION, Scenario.Algorithm.SHELL,
                       Scenario.Algorithm.HEAP, Scenario.Algorithm.MERGE,
                       Scenario.Algorithm.QUICK);

    public Runner() { }

    // -------------------- Interfaces funcionais para padronizar "quem ordena" --------------------

    /** Sorter que trabalha com arrays de objetos comparáveis (ex.: Integer[]). */
    @FunctionalInterface
    public interface SorterComparable {
        <T extends Comparable<T>> void sort(T[] a);
    }

    /** Sorter para int[]. (Counting/Radix típicos) */
    @FunctionalInterface
    public interface IntSorter {
        void sort(int[] a);
    }

    /** Sorter para double[]. (Bucket típico) */
    @FunctionalInterface
    public interface DoubleSorter {
        void sort(double[] a);
    }

    // -------------------- Catálogo de sorters (injeção de dependência simples) --------------------

    /**
     * Registro que mapeia "qual algoritmo" -> "qual função sorter" correspondente.
     * Você pode registrar placeholders agora e substituir por implementações reais depois.
     */
    public static final class SortRegistry {
        private final Map<Scenario.Algorithm, SorterComparable> cmpSorts = new HashMap<>();
        private final Map<Scenario.Algorithm, IntSorter> intSorts = new HashMap<>();
        private final Map<Scenario.Algorithm, DoubleSorter> dblSorts = new HashMap<>();

        public SortRegistry registerComparable(Scenario.Algorithm alg, SorterComparable sorter) {
            cmpSorts.put(alg, sorter);
            return this; // fluent API
        }

        public SortRegistry registerInt(Scenario.Algorithm alg, IntSorter sorter) {
            intSorts.put(alg, sorter);
            return this;
        }

        public SortRegistry registerDouble(Scenario.Algorithm alg, DoubleSorter sorter) {
            dblSorts.put(alg, sorter);
            return this;
        }

        public SorterComparable getComparable(Scenario.Algorithm alg) { return cmpSorts.get(alg); }
        public IntSorter        getInt(Scenario.Algorithm alg)        { return intSorts.get(alg); }
        public DoubleSorter     getDouble(Scenario.Algorithm alg)     { return dblSorts.get(alg); }
    }

    // -------------------- Execução de UM cenário --------------------

    /**
     * Executa um único Scenario usando o registro fornecido.
     * Faz: gerar base -> para cada repetição: clonar, sortear, medir, validar -> acumular Result.
     */
    public Result run(Scenario scenario, SortRegistry registry) {
        List<Long> times = new ArrayList<>();
        boolean sortedOk = true;      // assume ok, invalida se achar erro
        boolean sameMultiset = true;  // idem
        String notes = "";            // anotações (ex.: sorter não registrado)

        // Decide o domínio e gera o vetor-base de acordo com o tipo de algoritmo
        if (COMPARATIVES.contains(scenario.getAlgorithm())) {
            // ---------- Caso: algoritmos comparativos (Integer[] como dado de teste) ----------
            Integer[] base;
            switch (scenario.getInputType()) {
                case ASC:
                    base = Generator.genAscending(scenario.getSize());
                    break;
                case DESC:
                    base = Generator.genDescending(scenario.getSize());
                    break;
                case DUPS:
                    // "muitos repetidos": usa uma fração de valores distintos (ex.: n/20)
                    base = genManyDuplicatesLocal(
                        scenario.getSize(),
                        algumaQtd,
                        scenario.getSeed().longValue());

                    break;
                case RAND:
                default:
                    // RAND: faixa padrão 0..n (ou usa min/max passados no Scenario)
                    base = Generator.genRandomIntBoxed(
                            scenario.getSize(),
                            scenario.getMinValue() == null ? 0 : scenario.getMinValue(),
                            scenario.getMaxValue() == null ? Math.max(1, scenario.getSize()) : scenario.getMaxValue(),
                            scenario.getSeed());
            }

            // Busca o sorter correspondente no registro (pode ser placeholder neste momento)
            SorterComparable sorter = registry.getComparable(scenario.getAlgorithm());

            // Se não houver sorter registrado, já retorna Result com notas e flags de falha
            if (sorter == null) {
                notes = "Sorter não registrado para " + scenario.getAlgorithm();
                sortedOk = false;
                sameMultiset = false;
                return new Result(scenario, times, average(times), sortedOk, sameMultiset, notes);
            }

            // Repete o experimento 'repeats' vezes
            for (int rep = 0; rep < scenario.getRepeats(); rep++) {
                Integer[] a = Cloner.copyOf(base);   // cópia para o algoritmo
                Integer[] before = Cloner.copyOf(base); // cópia para validação de multiconjunto

                long dt = Timer.time(() -> sorter.sort(a)); // mede somente a ordenação
                times.add(dt);

                // validações
                boolean ok = Validator.isSorted(a);
                boolean same = Validator.sameMultiset(before, a);
                if (!ok)   sortedOk = false;
                if (!same) sameMultiset = false;
            }

        } else if (scenario.getAlgorithm() == Scenario.Algorithm.COUNTING) {
            // ---------- Caso: COUNTING (int[]) ----------
            int min = scenario.getMinValue() == null ? 0 : scenario.getMinValue();
            int max = scenario.getMaxValue() == null ? Math.max(1, scenario.getSize()) : scenario.getMaxValue();
            int[] base = genRandomIntPrimitiveLocal(
                scenario.getSize(), min, max, scenario.getSeed().longValue());


            IntSorter sorter = registry.getInt(Scenario.Algorithm.COUNTING);
            if (sorter == null) {
                notes = "Sorter não registrado para COUNTING";
                sortedOk = false;
                sameMultiset = false;
                return new Result(scenario, times, average(times), sortedOk, sameMultiset, notes);
            }

            for (int rep = 0; rep < scenario.getRepeats(); rep++) {
                int[] a = Cloner.copyOf(base);
                int[] before = Cloner.copyOf(base);

                long dt = Timer.time(() -> sorter.sort(a));
                times.add(dt);

                boolean ok = Validator.isSorted(a);
                boolean same = Validator.sameMultiset(before, a);
                if (!ok)   sortedOk = false;
                if (!same) sameMultiset = false;
            }

        } else if (scenario.getAlgorithm() == Scenario.Algorithm.RADIX) {
            // ---------- Caso: RADIX (int[]) ----------
            int min = scenario.getMinValue() == null ? 0 : scenario.getMinValue();
            int max = scenario.getMaxValue() == null ? Math.max(1, scenario.getSize()) : scenario.getMaxValue();
            int[] base = Generator.genRandomIntPrimitive(scenario.getSize(), min, max, scenario.getSeed());

            IntSorter sorter = registry.getInt(Scenario.Algorithm.RADIX);
            if (sorter == null) {
                notes = "Sorter não registrado para RADIX";
                sortedOk = false;
                sameMultiset = false;
                return new Result(scenario, times, average(times), sortedOk, sameMultiset, notes);
            }

            for (int rep = 0; rep < scenario.getRepeats(); rep++) {
                int[] a = Cloner.copyOf(base);
                int[] before = Cloner.copyOf(base);

                long dt = Timer.time(() -> sorter.sort(a));
                times.add(dt);

                boolean ok = Validator.isSorted(a);
                boolean same = Validator.sameMultiset(before, a);
                if (!ok)   sortedOk = false;
                if (!same) sameMultiset = false;
            }

        } else if (scenario.getAlgorithm() == Scenario.Algorithm.BUCKET) {
            // ---------- Caso: BUCKET (double[]) ----------
            double[] base = genRandomDouble01Local(scenario.getSize(), scenario.getSeed().longValue());


            DoubleSorter sorter = registry.getDouble(Scenario.Algorithm.BUCKET);
            if (sorter == null) {
                notes = "Sorter não registrado para BUCKET";
                sortedOk = false;
                sameMultiset = false;
                return new Result(scenario, times, average(times), sortedOk, sameMultiset, notes);
            }

            for (int rep = 0; rep < scenario.getRepeats(); rep++) {
                double[] a = Cloner.copyOf(base);
                double[] before = Cloner.copyOf(base);

                long dt = Timer.time(() -> sorter.sort(a));
                times.add(dt);

                boolean ok = Validator.isSorted(a);
                boolean same = Validator.sameMultiset(before, a);
                if (!ok)   sortedOk = false;
                if (!same) sameMultiset = false;
            }
        }

        // Retorna o pacote completo: tempos, média calculada, flags e notas.
        return new Result(scenario, times, average(times), sortedOk, sameMultiset, notes);
    }

    // -------------------- Execução em LOTE --------------------

    /**
     * Executa vários cenários em sequência, acumulando os resultados em uma lista.
     */
    public List<Result> runAll(List<Scenario> scenarios, SortRegistry registry) {
        List<Result> out = new ArrayList<>();
        for (Scenario s : scenarios) {
            out.add(run(s, registry));
        }
        return out;
    }

    // -------------------- Utilitário: média simples --------------------

    /** Média aritmética dos tempos (nanos). Retorna null se lista vazia. */
    private static Double average(List<Long> times) {
        if (times == null || times.isEmpty()) return null;
        long sum = 0L;
        for (Long t : times) sum += t;
        return sum / (double) times.size();
    }

    // ---------------------------------------------------------------------
// Helpers locais para não depender de métodos ausentes no Generator
// ---------------------------------------------------------------------

/** Gera Integer[] com MUITAS duplicatas: sorteia somente 'distinct' valores possíveis (0..distinct-1). */
private static Integer[] genManyDuplicatesLocal(int n, int distinct, long seed) {
    if (distinct <= 0) distinct = 1;
    java.util.Random rnd = new java.util.Random(seed);
    Integer[] out = new Integer[n];
    for (int i = 0; i < n; i++) {
        out[i] = rnd.nextInt(distinct); // 0..distinct-1
    }
    return out;
}

/** Converte Integer[] para int[]. */
private static int[] toPrimitive(Integer[] src) {
    int[] a = new int[src.length];
    for (int i = 0; i < src.length; i++) a[i] = src[i];
    return a;
}

/** Gera int[] aleatório em [min, max] (INCLUSIVO). */
private static int[] genRandomIntPrimitiveLocal(int n, int min, int max, long seed) {
    if (max < min) { int t = min; min = max; max = t; }
    java.util.Random rnd = new java.util.Random(seed);
    int[] a = new int[n];
    int bound = (max - min) + 1;
    for (int i = 0; i < n; i++) a[i] = min + rnd.nextInt(bound);
    return a;
}

/** Gera double[] uniformes em [0,1). */
private static double[] genRandomDouble01Local(int n, long seed) {
    java.util.Random rnd = new java.util.Random(seed);
    double[] a = new double[n];
    for (int i = 0; i < n; i++) a[i] = rnd.nextDouble();
    return a;
}



}
