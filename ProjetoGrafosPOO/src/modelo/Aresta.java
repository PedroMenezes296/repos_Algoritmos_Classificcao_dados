package modelo;

/**
 * Modelo simples de aresta.
 * Vértices numerados de 1 a N.
 */
public class Aresta {
    private final int idVerticeOrigem;
    private final int idVerticeDestino;
    private final double pesoAresta;

    public Aresta(int idVerticeOrigem, int idVerticeDestino, double pesoAresta) {
        this.idVerticeOrigem = idVerticeOrigem;
        this.idVerticeDestino = idVerticeDestino;
        this.pesoAresta = pesoAresta;
    }

    public int getIdVerticeOrigem() {
        return idVerticeOrigem;
    }

    public int getIdVerticeDestino() {
        return idVerticeDestino;
    }

    public double getPesoAresta() {
        return pesoAresta;
    }

    // dentro de Aresta.java
    public int getOrigem() { return idVerticeOrigem; }
    public int getDestino() { return idVerticeDestino; }
    public double getPeso() { return pesoAresta; }


    @Override
    public String toString() {
        return "Aresta{" +
                "origem=" + idVerticeOrigem +
                ", destino=" + idVerticeDestino +
                ", peso=" + pesoAresta +
                '}';
    }
}
