package util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Locale;

public class CSVWriterSimples implements AutoCloseable {
    private final PrintWriter out;

    public CSVWriterSimples(String caminhoCSV) {
        try {
            this.out = new PrintWriter(new FileWriter(caminhoCSV, false));
        } catch (Exception e) {
            throw new RuntimeException("Falha ao abrir CSV: " + caminhoCSV, e);
        }
    }

    /**
     * Ajusta automaticamente números para ponto decimal (Locale.US).
     * Strings normais continuam iguais.
     */
    private String normalizarValor(Object valor) {
        if (valor instanceof Number) {
            return String.format(Locale.US, "%s", valor);
        }
        return valor.toString().replace(",", "."); // fallback
    }

    public void escreverLinha(Object... colunas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < colunas.length; i++) {
            sb.append(normalizarValor(colunas[i]));
            if (i < colunas.length - 1) sb.append(',');
        }
        out.println(sb.toString());
    }

    @Override
    public void close() {
        out.flush();
        out.close();
    }
}
