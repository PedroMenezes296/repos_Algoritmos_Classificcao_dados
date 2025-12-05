package representacao;

import modelo.Aresta;
import java.util.List;

/**
 * Interface que define os métodos essenciais para qualquer representação de grafo.
 * Usada por GrafoListaAdj e GrafoMatrizAdj.
 *
 * Todos os nomes são autoexplicativos e a lógica é a mais clara possível.
 */
public interface Grafo {

    /**
     * Retorna o número total de vértices do grafo.
     */
    int getNumeroDeVertices();

    /**
     * Retorna o número total de arestas do grafo.
     */
    int getNumeroDeArestas();

    /**
     * Indica se o grafo é direcionado (true) ou não-direcionado (false).
     */
    boolean isGrafoDirecionado();

    /**
     * Adiciona uma aresta entre dois vértices, com o peso informado.
     * Se o grafo não for direcionado, a implementação deve espelhar a aresta.
     */
    void adicionarAresta(int idVerticeOrigem, int idVerticeDestino, double pesoAresta);

    /**
     * Verifica se existe uma aresta entre origem e destino.
     */
    boolean existeArestaEntre(int idVerticeOrigem, int idVerticeDestino);

    /**
     * Retorna o peso da aresta entre origem e destino.
     * Implementações podem retornar Double.POSITIVE_INFINITY se não existir aresta.
     */
    double getPesoDaAresta(int idVerticeOrigem, int idVerticeDestino);

    /**
     * Retorna todos os vizinhos alcançáveis a partir de um vértice.
     * Para grafos direcionados: vizinhos = vértices para os quais existe aresta de saída.
     */
    List<Integer> getVizinhosDoVertice(int idVerticeOrigem);

    /**
     * Retorna uma lista com TODAS as arestas existentes no grafo.
     * Usada por algoritmos como Kruskal e Bellman-Ford.
     */
    List<Aresta> getListaDeArestas();
}
