package algoritmos;

import representacao.Grafo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Implementação crua de BFS (Busca em Largura).
 * Vértices numerados de 1..N. Sem tratamento de erro.
 */
public class AlgoritmoBuscaEmLargura {

    /**
     * Executa BFS a partir de um vértice de origem.
     *
     * @param grafo            grafo a ser percorrido
     * @param idVerticeOrigem  vértice de início (1..N)
     * @return ordem de visita dos vértices
     */
    public static List<Integer> executarBuscaEmLargura(Grafo grafo, int idVerticeOrigem) {
        int quantidadeDeVertices = grafo.getNumeroDeVertices();

        boolean[] vetorDeVerticesVisitados = new boolean[quantidadeDeVertices + 1];
        List<Integer> ordemDeVisitaDosVertices = new ArrayList<>();
        Deque<Integer> filaDeVertices = new ArrayDeque<>();

        vetorDeVerticesVisitados[idVerticeOrigem] = true;
        filaDeVertices.addLast(idVerticeOrigem);

        while (!filaDeVertices.isEmpty()) {
            int idVerticeAtual = filaDeVertices.removeFirst();
            ordemDeVisitaDosVertices.add(idVerticeAtual);

            for (int idVerticeVizinho : grafo.getVizinhosDoVertice(idVerticeAtual)) {
                if (!vetorDeVerticesVisitados[idVerticeVizinho]) {
                    vetorDeVerticesVisitados[idVerticeVizinho] = true;
                    filaDeVertices.addLast(idVerticeVizinho);
                }
            }
        }

        return ordemDeVisitaDosVertices;
    }
}
