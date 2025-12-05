package algoritmos;

import representacao.Grafo;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação crua de DFS (Busca em Profundidade) recursiva.
 * Vértices numerados de 1..N. Sem tratamento de erro.
 */
public class AlgoritmoBuscaEmProfundidade {

    /**
     * Executa DFS a partir de um vértice de origem.
     *
     * @param grafo            grafo a ser percorrido
     * @param idVerticeOrigem  vértice de início (1..N)
     * @return ordem de visita dos vértices
     */
    public static List<Integer> executarBuscaEmProfundidade(Grafo grafo, int idVerticeOrigem) {
        int quantidadeDeVertices = grafo.getNumeroDeVertices();

        boolean[] vetorDeVerticesVisitados = new boolean[quantidadeDeVertices + 1];
        List<Integer> ordemDeVisitaDosVertices = new ArrayList<>();

        visitarProfundidade(grafo, idVerticeOrigem, vetorDeVerticesVisitados, ordemDeVisitaDosVertices);

        return ordemDeVisitaDosVertices;
    }

    // Visita recursiva simples
    private static void visitarProfundidade(
            Grafo grafo,
            int idVerticeAtual,
            boolean[] vetorDeVerticesVisitados,
            List<Integer> ordemDeVisitaDosVertices
    ) {
        vetorDeVerticesVisitados[idVerticeAtual] = true;
        ordemDeVisitaDosVertices.add(idVerticeAtual);

        for (int idVerticeVizinho : grafo.getVizinhosDoVertice(idVerticeAtual)) {
            if (!vetorDeVerticesVisitados[idVerticeVizinho]) {
                visitarProfundidade(grafo, idVerticeVizinho, vetorDeVerticesVisitados, ordemDeVisitaDosVertices);
            }
        }
    }
}
