package bench;

import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * Formata os resultados para consumo externo.
 * - toCsv: gera um CSV com colunas úteis para análise/plot (boxplot/barras).
 * - summary: string legível para inspeção rápida no console.
 */
public final class Report {

    private Report() { }

    /**
     * Retorna uma String CSV com cabeçalho + linhas de resultados.
     * Colunas:
     *  algorithm,input,size,repeats,avg_nanos,sorted_ok,same_multiset,notes,times_nanos
     * 'times_nanos' sai como uma lista "t1|t2|...|tk" dentro de aspas.
     */
    public static String toCsv(List<Result> results) {
        StringBuilder sb = new StringBuilder();

        // Cabeçalho
        sb.append("algorithm,input,size,repeats,avg_nanos,sorted_ok,same_multiset,notes,times_nanos\n");

        // Linhas
        for (Result r : results) {
            Scenario s = r.getScenario();

            String alg = s.getAlgorithm().name();
            String inp = s.getInputType().name();
            int size = s.getSize();
            int reps = s.getRepeats();
            String avg  = r.getAverage() == null ? "" : String.format(Locale.ROOT, "%.0f", r.getAverage());
            String ok   = r.isSortedOk() ? "1" : "0";
            String same = r.isSameMultiset() ? "1" : "0";
            String notes = r.getNotes() == null ? "" : r.getNotes().replace(',', ';'); // evita quebrar CSV

            // Serializa a lista de tempos como "t1|t2|...|tk"
            StringJoiner sj = new StringJoiner("|");
            for (Long t : r.getTimes()) sj.add(String.valueOf(t));

            // Monta a linha CSV
            sb.append(alg).append(',')
              .append(inp).append(',')
              .append(size).append(',')
              .append(reps).append(',')
              .append(avg).append(',')
              .append(ok).append(',')
              .append(same).append(',')
              .append(notes).append(',')
              .append('"').append(sj.toString()).append('"')
              .append('\n');
        }
        return sb.toString();
    }

    /**
     * Retorna um sumário textual simples para leitura rápida no console.
     * Exemplo:
     * [MERGE | RAND | n=10000 | reps=10] avg=123456 ns; ok=true; same=true
     */
    public static String summary(List<Result> results) {
        StringBuilder sb = new StringBuilder();
        for (Result r : results) {
            Scenario s = r.getScenario();
            sb.append("[")
              .append(s.getAlgorithm()).append(" | ")
              .append(s.getInputType()).append(" | n=").append(s.getSize()).append(" | reps=").append(s.getRepeats())
              .append("] avg=").append(r.getAverage() == null ? "NA" : String.format(Locale.ROOT, "%.0f", r.getAverage()))
              .append(" ns; ok=").append(r.isSortedOk())
              .append("; same=").append(r.isSameMultiset());
            if (r.getNotes() != null && !r.getNotes().isEmpty()) {
                sb.append(" ; notes=").append(r.getNotes());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
