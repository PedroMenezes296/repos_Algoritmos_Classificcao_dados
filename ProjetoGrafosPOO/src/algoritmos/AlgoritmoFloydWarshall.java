package algoritmos;

import representacao.Grafo;
import java.util.ArrayList;
import java.util.List;

public class AlgoritmoFloydWarshall {

    private static final double INF = Double.POSITIVE_INFINITY;

    /**
     * MANTIDO: mesma assinatura que você já usa.
     * Retorna somente a matriz de distâncias mínimas dist[i][j].
     * Vértices numerados de 1..V.
     */
    public static double[][] executarFloydWarshall(Grafo grafo) {
        int n = grafo.getNumeroDeVertices();
        double[][] dist = new double[n + 1][n + 1];

        // inicialização
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) dist[i][j] = 0.0;
                else if (grafo.existeArestaEntre(i, j)) dist[i][j] = grafo.getPesoDaAresta(i, j);
                else dist[i][j] = INF;
            }
        }

        // relaxações
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                if (dist[i][k] == INF) continue;
                for (int j = 1; j <= n; j++) {
                    double via = dist[i][k] + dist[k][j];
                    if (via < dist[i][j]) dist[i][j] = via;
                }
            }
        }
        return dist;
    }

    // ======================== OPCIONAL (sem quebrar nada) ========================

    /** Resultado completo com distâncias e matriz de "próximos" para reconstruir caminho. */
    public static class ResultadoFW {
        public final double[][] dist;
        public final int[][] prox;
        public ResultadoFW(double[][] dist, int[][] prox) { this.dist = dist; this.prox = prox; }
    }

    /**
     * Versão opcional que também calcula a matriz prox para reconstrução de caminhos.
     * Não impacta quem usa apenas executarFloydWarshall(grafo).
     */
    public static ResultadoFW executarComCaminho(Grafo grafo) {
        int n = grafo.getNumeroDeVertices();
        double[][] dist = new double[n + 1][n + 1];
        int[][] prox = new int[n + 1][n + 1];

        // inicialização (dist e prox)
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    dist[i][j] = 0.0;
                    prox[i][j] = j;
                } else if (grafo.existeArestaEntre(i, j)) {
                    dist[i][j] = grafo.getPesoDaAresta(i, j);
                    prox[i][j] = j;
                } else {
                    dist[i][j] = INF;
                    prox[i][j] = -1;
                }
            }
        }

        // relaxações com prox
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                if (dist[i][k] == INF) continue;
                for (int j = 1; j <= n; j++) {
                    double via = dist[i][k] + dist[k][j];
                    if (via < dist[i][j]) {
                        dist[i][j] = via;
                        prox[i][j] = prox[i][k];
                    }
                }
            }
        }
        return new ResultadoFW(dist, prox);
    }

    /** Reconstrói o caminho i→j usando o resultado da versão "com caminho". */
    public static List<Integer> reconstruirCaminho(int i, int j, ResultadoFW r) {
        List<Integer> path = new ArrayList<>();
        if (i < 1 || j < 1 || i >= r.prox.length || j >= r.prox.length) return path;
        if (r.prox[i][j] == -1) return path;
        path.add(i);
        while (i != j) {
            i = r.prox[i][j];
            path.add(i);
        }
        return path;
    }
}
