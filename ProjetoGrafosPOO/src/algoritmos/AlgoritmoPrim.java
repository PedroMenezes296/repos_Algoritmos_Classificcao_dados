package algoritmos;

import representacao.Grafo;
import modelo.Aresta;

import java.util.ArrayList;
import java.util.List;

/**
 * Prim simples O(V^2), adequado para qualquer representação.
 * Para grafos NÃO direcionados. Se o grafo vier direcionado, o resultado não é MST.
 */
public class AlgoritmoPrim {

    private static final double INF = Double.POSITIVE_INFINITY;

    public static class ResultadoMST {
        public final List<Aresta> arestasDaMST;
        public final double custoTotal;

        public ResultadoMST(List<Aresta> arestasDaMST, double custoTotal) {
            this.arestasDaMST = arestasDaMST;
            this.custoTotal = custoTotal;
        }
    }

    public static ResultadoMST executarPrim(Grafo grafo, int verticeInicial) {
        int totalVertices = grafo.getNumeroDeVertices();

        boolean[] verticeJaIncluidoNaMST = new boolean[totalVertices + 1];
        double[] menorCustoParaChegar = new double[totalVertices + 1];
        int[] paiDoVertice = new int[totalVertices + 1];

        for (int v = 1; v <= totalVertices; v++) {
            menorCustoParaChegar[v] = INF;
            paiDoVertice[v] = -1;
        }

        menorCustoParaChegar[verticeInicial] = 0.0;

        for (int iter = 1; iter <= totalVertices; iter++) {
            // escolhe vértice u ainda fora da MST com menor custo
            int uEscolhido = -1;
            double melhorCusto = INF;
            for (int u = 1; u <= totalVertices; u++) {
                if (!verticeJaIncluidoNaMST[u] && menorCustoParaChegar[u] < melhorCusto) {
                    melhorCusto = menorCustoParaChegar[u];
                    uEscolhido = u;
                }
            }
            if (uEscolhido == -1) break; // componente desconexo

            verticeJaIncluidoNaMST[uEscolhido] = true;

            // relaxa vizinhos de uEscolhido
            for (int v : grafo.getVizinhosDoVertice(uEscolhido)) {
                double pesoUV = grafo.getPesoDaAresta(uEscolhido, v);
                if (!verticeJaIncluidoNaMST[v] && pesoUV < menorCustoParaChegar[v]) {
                    menorCustoParaChegar[v] = pesoUV;
                    paiDoVertice[v] = uEscolhido;
                }
            }
        }

        List<Aresta> arestas = new ArrayList<>();
        double custoTotal = 0.0;
        for (int v = 1; v <= totalVertices; v++) {
            int pai = paiDoVertice[v];
            if (pai != -1) {
                double peso = grafo.getPesoDaAresta(pai, v);
                arestas.add(new Aresta(pai, v, peso));
                custoTotal += peso;
            }
        }

        return new ResultadoMST(arestas, custoTotal);
    }
}
