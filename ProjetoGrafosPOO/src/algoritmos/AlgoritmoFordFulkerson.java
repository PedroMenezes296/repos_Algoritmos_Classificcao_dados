package algoritmos;

import representacao.Grafo;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Edmonds-Karp (Ford-Fulkerson com BFS) usando matriz residual simples.
 * Mantém exatamente os nomes já utilizados no projeto.
 */
public class AlgoritmoFordFulkerson {

    private static final double EPS = 1e-9;

    // === NOVO: estrutura igual ao padrão usado em Prim e Kruskal ===
    public static class ResultadoFluxo {
        public final double fluxoMaximo;

        public ResultadoFluxo(double fluxoMaximo) {
            this.fluxoMaximo = fluxoMaximo;
        }
    }

    // === NOVO: versão "padrão" que o controlador irá chamar ===
    public static ResultadoFluxo executarFordFulkerson(Grafo grafo, int fonte, int sorvedouro) {
        double fluxo = executarFluxoMaximo(grafo, fonte, sorvedouro);
        return new ResultadoFluxo(fluxo);
    }

    // === O SEU CÓDIGO ORIGINAL: NÃO ALTERADO ===
    public static double executarFluxoMaximo(Grafo grafo, int fonte, int sorvedouro) {
        int totalVertices = grafo.getNumeroDeVertices();

        // Constrói matriz de capacidades a partir do grafo
        double[][] capacidade = new double[totalVertices + 1][totalVertices + 1];
        for (int u = 1; u <= totalVertices; u++) {
            for (int v : grafo.getVizinhosDoVertice(u)) {
                double cap = grafo.getPesoDaAresta(u, v);
                capacidade[u][v] += cap;
            }
        }

        // Matriz residual começa igual às capacidades
        double[][] residual = new double[totalVertices + 1][totalVertices + 1];
        for (int i = 1; i <= totalVertices; i++) {
            System.arraycopy(capacidade[i], 1, residual[i], 1, totalVertices);
        }

        int[] pai = new int[totalVertices + 1];
        double fluxoMax = 0.0;

        while (existeCaminhoAumentanteBFS(residual, fonte, sorvedouro, pai)) {

            double gargalo = Double.POSITIVE_INFINITY;
            for (int v = sorvedouro; v != fonte; v = pai[v]) {
                int u = pai[v];
                gargalo = Math.min(gargalo, residual[u][v]);
            }

            for (int v = sorvedouro; v != fonte; v = pai[v]) {
                int u = pai[v];
                residual[u][v] -= gargalo;
                residual[v][u] += gargalo;
            }

            fluxoMax += gargalo;
        }

        return fluxoMax;
    }

    private static boolean existeCaminhoAumentanteBFS(double[][] residual, int fonte, int sorvedouro, int[] pai) {
        int n = residual.length - 1;
        boolean[] visitado = new boolean[n + 1];
        Deque<Integer> fila = new ArrayDeque<>();

        fila.add(fonte);
        visitado[fonte] = true;
        pai[fonte] = -1;

        while (!fila.isEmpty()) {
            int u = fila.poll();

            for (int v = 1; v <= n; v++) {
                if (!visitado[v] && residual[u][v] > EPS) {
                    pai[v] = u;
                    visitado[v] = true;
                    if (v == sorvedouro) return true;
                    fila.add(v);
                }
            }
        }

        return false;
    }
}
