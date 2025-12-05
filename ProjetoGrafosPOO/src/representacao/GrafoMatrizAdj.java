package representacao;

import modelo.Aresta;
import java.util.ArrayList;
import java.util.List;

/**
 * Representação de grafo usando Matriz de Adjacência.
 * Vértices numerados de 1 até numeroDeVertices.
 * Valor +infinito representa ausência de aresta.
 */
public class GrafoMatrizAdj implements Grafo {

    private final int numeroDeVertices;
    private final boolean grafoDirecionado;
    private final double[][] matrizDePesos;

    public static final double VALOR_SEM_ARESTA = Double.POSITIVE_INFINITY;

    private int contadorDeArestas;

    public GrafoMatrizAdj(int numeroDeVertices, boolean grafoDirecionado) {
        this.numeroDeVertices = numeroDeVertices;
        this.grafoDirecionado = grafoDirecionado;

        // Matriz (1..N) mas criamos tamanho [N+1][N+1]
        this.matrizDePesos = new double[numeroDeVertices + 1][numeroDeVertices + 1];

        // Preencher com "sem aresta"
        for (int origem = 0; origem <= numeroDeVertices; origem++) {
            for (int destino = 0; destino <= numeroDeVertices; destino++) {
                matrizDePesos[origem][destino] = VALOR_SEM_ARESTA;
            }
        }

        // Distância zero na diagonal
        for (int v = 1; v <= numeroDeVertices; v++) {
            matrizDePesos[v][v] = 0.0;
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
        boolean arestaAindaNaoExistia =
                (matrizDePesos[idVerticeOrigem][idVerticeDestino] == VALOR_SEM_ARESTA);

        matrizDePesos[idVerticeOrigem][idVerticeDestino] = pesoAresta;

        if (!grafoDirecionado) {
            matrizDePesos[idVerticeDestino][idVerticeOrigem] = pesoAresta;
        }

        if (arestaAindaNaoExistia) {
            contadorDeArestas++;
        }
    }

    @Override
    public boolean existeArestaEntre(int idVerticeOrigem, int idVerticeDestino) {
        return matrizDePesos[idVerticeOrigem][idVerticeDestino] != VALOR_SEM_ARESTA;
    }

    @Override
    public double getPesoDaAresta(int idVerticeOrigem, int idVerticeDestino) {
        return matrizDePesos[idVerticeOrigem][idVerticeDestino];
    }

    @Override
    public List<Integer> getVizinhosDoVertice(int idVerticeOrigem) {
        List<Integer> listaDeVizinhos = new ArrayList<>();

        for (int destino = 1; destino <= numeroDeVertices; destino++) {
            if (destino != idVerticeOrigem) {
                if (matrizDePesos[idVerticeOrigem][destino] != VALOR_SEM_ARESTA) {
                    listaDeVizinhos.add(destino);
                }
            }
        }

        return listaDeVizinhos;
    }

    @Override
    public List<Aresta> getListaDeArestas() {
        List<Aresta> listaDeArestas = new ArrayList<>();

        for (int origem = 1; origem <= numeroDeVertices; origem++) {
            for (int destino = 1; destino <= numeroDeVertices; destino++) {

                if (origem == destino) continue;

                double peso = matrizDePesos[origem][destino];

                if (peso == VALOR_SEM_ARESTA) continue;

                if (grafoDirecionado) {
                    listaDeArestas.add(new Aresta(origem, destino, peso));
                } else {
                    if (origem < destino) {
                        listaDeArestas.add(new Aresta(origem, destino, peso));
                    }
                }
            }
        }

        return listaDeArestas;
    }
}
