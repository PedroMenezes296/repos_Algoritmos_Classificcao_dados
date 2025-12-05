package algoritmos;

import representacao.Grafo;

import java.util.List;

/**
 * Dijkstra O(V^2), simples, sem heap.
 * Assume pesos não negativos. Vértices 1..N.
 */
public class AlgoritmoDijkstra {

    public static double[] executarDijkstra(Grafo grafo, int idVerticeOrigem) {
        int quantidadeDeVertices = grafo.getNumeroDeVertices();

        double[] vetorDeDistancias = new double[quantidadeDeVertices + 1];
        boolean[] vetorDeVerticesVisitados = new boolean[quantidadeDeVertices + 1];

        // inicializa distâncias com +inf
        for (int id = 1; id <= quantidadeDeVertices; id++) {
            vetorDeDistancias[id] = Double.POSITIVE_INFINITY;
        }
        vetorDeDistancias[idVerticeOrigem] = 0.0;

        // repete N vezes: escolhe o não-visitado com menor distância e relaxa vizinhos
        for (int passo = 1; passo <= quantidadeDeVertices; passo++) {
            int idVerticeComMenorDistancia = selecionarVerticeNaoVisitadoComMenorDistancia(vetorDeDistancias, vetorDeVerticesVisitados);
            if (idVerticeComMenorDistancia == -1) {
                break; // não há mais vértices alcançáveis
            }

            vetorDeVerticesVisitados[idVerticeComMenorDistancia] = true;

            List<Integer> listaDeVizinhos = grafo.getVizinhosDoVertice(idVerticeComMenorDistancia);
            for (int idVerticeVizinho : listaDeVizinhos) {
                if (vetorDeVerticesVisitados[idVerticeVizinho]) continue;

                double pesoAresta = grafo.getPesoDaAresta(idVerticeComMenorDistancia, idVerticeVizinho);
                double novaDistancia = vetorDeDistancias[idVerticeComMenorDistancia] + pesoAresta;

                if (novaDistancia < vetorDeDistancias[idVerticeVizinho]) {
                    vetorDeDistancias[idVerticeVizinho] = novaDistancia;
                }
            }
        }

        return vetorDeDistancias;
    }

    // Seleciona o índice do vértice não visitado com menor distância atual
    private static int selecionarVerticeNaoVisitadoComMenorDistancia(double[] vetorDeDistancias, boolean[] vetorDeVerticesVisitados) {
        int indiceSelecionado = -1;
        double menorValor = Double.POSITIVE_INFINITY;

        for (int id = 1; id < vetorDeDistancias.length; id++) {
            if (!vetorDeVerticesVisitados[id] && vetorDeDistancias[id] < menorValor) {
                menorValor = vetorDeDistancias[id];
                indiceSelecionado = id;
            }
        }
        return indiceSelecionado;
    }
}
