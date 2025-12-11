package bench;

import java.util.List;

/**
 * Mantém o RESULTADO de executar um Scenario:
 * - lista de tempos individuais (em nanos),
 * - média,
 * - flags de validação (ordenado? multiconjunto preservado?),
 * - observações livres (ex.: "Sorter não registrado").
 */
public final class Result {

    private final Scenario scenario;
    private final List<Long> times;      // tempos por repetição (nanos)
    private final Double average;        // média dos tempos (nanos)
    private final boolean sortedOk;      // passou isSorted
    private final boolean sameMultiset;  // preservou conteúdo (quando aplicável)
    private final String notes;          // observação textual

    public Result(Scenario scenario,
                  List<Long> times,
                  Double average,
                  boolean sortedOk,
                  boolean sameMultiset,
                  String notes) {
        this.scenario = scenario;
        this.times = times;
        this.average = average;
        this.sortedOk = sortedOk;
        this.sameMultiset = sameMultiset;
        this.notes = notes;
    }

    // Getters: objeto imutável, apenas leitura.
    public Scenario getScenario() { return scenario; }
    public List<Long> getTimes() { return times; }
    public Double getAverage() { return average; }
    public boolean isSortedOk() { return sortedOk; }
    public boolean isSameMultiset() { return sameMultiset; }
    public String getNotes() { return notes; }
}
