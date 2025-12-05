package algoritmos;

import representacao.Grafo;

import java.util.List;

/**
 * Bellman-Ford "cru": V-1 relaxações percorrendo todas as arestas como (u -> v) via vizinhos.
 * Não faz detecção de ciclo negativo nesta versão base.
 * Vértices 1..N.
 */
public class AlgoritmoBellmanFord {

    public static double[] executarBellmanFord(Grafo grafo, int idVerticeOrigem) {
        int quantidadeDeVertices = grafo.getNumeroDeVertices();

        double[] vetorDeDistancias = new double[quantidadeDeVertices + 1];

        for (int id = 1; id <= quantidadeDeVertices; id++) {
            vetorDeDistancias[id] = Double.POSITIVE_INFINITY;
        }
        vetorDeDistancias[idVerticeOrigem] = 0.0;

        // Relaxa todas as arestas V-1 vezes
        for (int i = 1; i <= quantidadeDeVertices - 1; i++) {
            for (int idVerticeU = 1; idVerticeU <= quantidadeDeVertices; idVerticeU++) {
                if (vetorDeDistancias[idVerticeU] == Double.POSITIVE_INFINITY) continue;

                List<Integer> listaDeVizinhos = grafo.getVizinhosDoVertice(idVerticeU);
                for (int idVerticeV : listaDeVizinhos) {
                    double pesoAresta = grafo.getPesoDaAresta(idVerticeU, idVerticeV);
                    double novaDistancia = vetorDeDistancias[idVerticeU] + pesoAresta;

                    if (novaDistancia < vetorDeDistancias[idVerticeV]) {
                        vetorDeDistancias[idVerticeV] = novaDistancia;
                    }
                }
            }
        }

        return vetorDeDistancias;
    }
}
