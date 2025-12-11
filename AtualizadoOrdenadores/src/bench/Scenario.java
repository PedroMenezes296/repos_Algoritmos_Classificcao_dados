package bench;

/**
 * Descreve UM CENÁRIO de experimento: qual algoritmo, qual tipo de entrada,
 * tamanho do vetor, número de repetições e parâmetros opcionais
 * para algoritmos lineares (faixa/base/buckets) e seed de aleatoriedade.
 */
public final class Scenario {

    /** Tipos de entrada que serão gerados para o experimento. */
    public enum InputType { ASC, DESC, RAND, DUPS }

    /** Conjunto de algoritmos disponíveis (comparativos + lineares). */
    public enum Algorithm {
        // comparativos
        BUBBLE, INSERTION, SELECTION, SHELL, HEAP, MERGE, QUICK,
        // lineares
        COUNTING, RADIX, BUCKET
    }

    // ---------- Campos imutáveis do cenário ----------
    private final Algorithm algorithm;   // qual algoritmo será testado
    private final InputType inputType;   // ASC, DESC, RAND, DUPS
    private final int size;              // tamanho do vetor

    // Parâmetros opcionais para algoritmos lineares
    private final Integer minValue;      // counting/rand int (mínimo)
    private final Integer maxValue;      // counting/rand int (máximo)
    private final Integer radixBase;     // base para radix (ex.: 10 ou 256)
    private final Integer buckets;       // nº de buckets para bucket sort

    private final int repeats;           // repetições do mesmo cenário
    private final Long seed;             // seed opcional (reprodutibilidade)

    // ---------- Construtor: define todos os campos de uma vez ----------
    public Scenario(Algorithm algorithm,
                    InputType inputType,
                    int size,
                    Integer minValue,
                    Integer maxValue,
                    Integer radixBase,
                    Integer buckets,
                    int repeats,
                    Long seed) {
        this.algorithm = algorithm;
        this.inputType = inputType;
        this.size = size;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.radixBase = radixBase;
        this.buckets = buckets;
        this.repeats = repeats;
        this.seed = seed;
    }

    // ---------- Getters (objeto imutável) ----------
    public Algorithm getAlgorithm() { return algorithm; }
    public InputType getInputType() { return inputType; }
    public int getSize() { return size; }
    public Integer getMinValue() { return minValue; }
    public Integer getMaxValue() { return maxValue; }
    public Integer getRadixBase() { return radixBase; }
    public Integer getBuckets() { return buckets; }
    public int getRepeats() { return repeats; }
    public Long getSeed() { return seed; }
}
