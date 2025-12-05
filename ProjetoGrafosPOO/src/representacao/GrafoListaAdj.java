package representacao;

import modelo.Aresta;

import java.util.ArrayList;
import java.util.List;

/**
 * Representação de grafo usando Lista de Adjacência.
 * Vértices numerados de 1 até numeroDeVertices.
 * Valor +infinito representa ausência de aresta (para consulta de peso).
 */
public class GrafoListaAdj implements Grafo {

    public static final double VALOR_SEM_ARESTA = Double.POSITIVE_INFINITY;

    private final int numeroDeVertices;
    private final boolean grafoDirecionado;

    // listaDeAdjacencia[origem] = lista de arestas que saem de 'origem'
    private final List<List<Aresta>> listaDeAdjacencia;

    // contador lógico de arestas
    private int contadorDeArestas;

    public GrafoListaAdj(int numeroDeVertices, boolean grafoDirecionado) {
        this.numeroDeVertices = numeroDeVertices;
        this.grafoDirecionado = grafoDirecionado;

        this.listaDeAdjacencia = new ArrayList<>(numeroDeVertices + 1);
        // índice 0 não é usado; preenchemos 0..N com listas
        for (int i = 0; i <= numeroDeVertices; i++) {
            this.listaDeAdjacencia.add(new ArrayList<>());
        }

        this.contadorDeArestas = 0;
    }

    // ====================== Implementação da interface ======================

    @Override
    public int getNumeroDeVertices() {
        return numeroDeVertices;
    }

    @Override
    public int getNumeroDeArestas() {
        return contadorDeArestas;
    }

    @Override
    public boolean isGrafoDirecionado() {
        return grafoDirecionado;
    }

    @Override
    public void adicionarAresta(int idVerticeOrigem, int idVerticeDestino, double pesoAresta) {
        // Verifica se a aresta origem->destino já existia
        boolean arestaAindaNaoExistia = !existeArestaEntre(idVerticeOrigem, idVerticeDestino);

        // Adiciona a aresta na lista do vértice de origem
        listaDeAdjacencia.get(idVerticeOrigem)
                .add(new Aresta(idVerticeOrigem, idVerticeDestino, pesoAresta));

        if (!grafoDirecionado) {
            // Para grafo não direcionado, espelha a aresta
            listaDeAdjacencia.get(idVerticeDestino)
                    .add(new Aresta(idVerticeDestino, idVerticeOrigem, pesoAresta));
        }

        if (arestaAindaNaoExistia) {
            contadorDeArestas++;
        }
    }

    @Override
    public boolean existeArestaEntre(int idVerticeOrigem, int idVerticeDestino) {
        for (Aresta aresta : listaDeAdjacencia.get(idVerticeOrigem)) {
            if (aresta.getIdVerticeDestino() == idVerticeDestino) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getPesoDaAresta(int idVerticeOrigem, int idVerticeDestino) {
        for (Aresta aresta : listaDeAdjacencia.get(idVerticeOrigem)) {
            if (aresta.getIdVerticeDestino() == idVerticeDestino) {
                return aresta.getPesoAresta();
            }
        }
        return VALOR_SEM_ARESTA;
    }

    @Override
    public List<Integer> getVizinhosDoVertice(int idVerticeOrigem) {
        List<Integer> listaDeVizinhos = new ArrayList<>();
        for (Aresta aresta : listaDeAdjacencia.get(idVerticeOrigem)) {
            int idVerticeDestino = aresta.getIdVerticeDestino();
            if (idVerticeDestino != idVerticeOrigem) {
                listaDeVizinhos.add(idVerticeDestino);
            }
        }
        return listaDeVizinhos;
    }

    @Override
    public List<Aresta> getListaDeArestas() {
        List<Aresta> listaTodasArestas = new ArrayList<>();

        for (int idVerticeOrigem = 1; idVerticeOrigem <= numeroDeVertices; idVerticeOrigem++) {
            for (Aresta aresta : listaDeAdjacencia.get(idVerticeOrigem)) {
                int idVerticeDestino = aresta.getIdVerticeDestino();

                if (idVerticeOrigem == idVerticeDestino) {
                    continue; // ignora laços para si mesmo
                }

                if (grafoDirecionado) {
                    // Em grafo direcionado, adiciona todas as arestas
                    listaTodasArestas.add(aresta);
                } else {
                    // Em grafo não direcionado, evita duplicar (origem<destino)
                    if (idVerticeOrigem < idVerticeDestino) {
                        listaTodasArestas.add(aresta);
                    }
                }
            }
        }

        return listaTodasArestas;
    }
}
