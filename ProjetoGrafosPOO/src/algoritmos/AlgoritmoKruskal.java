package algoritmos;

import representacao.Grafo;
import modelo.Aresta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Kruskal com Union-Find (sem path compression fancy, simples e claro).
 * Para grafos NÃO direcionados. Se for direcionado, use com cautela.
 */
public class AlgoritmoKruskal {

    public static class ResultadoMST {
        public final List<Aresta> arestasDaMST;
        public final double custoTotal;

        public ResultadoMST(List<Aresta> arestasDaMST, double custoTotal) {
            this.arestasDaMST = arestasDaMST;
            this.custoTotal = custoTotal;
        }
    }

    private static class UnionFind {
        final int[] parent;
        final int[] rank;

        UnionFind(int n) {
            parent = new int[n + 1];
            rank = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            while (x != parent[x]) {
                x = parent[x];
            }
            return x;
        }

        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;
            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
            return true;
        }
    }

    public static ResultadoMST executarKruskal(Grafo grafo) {
        int totalVertices = grafo.getNumeroDeVertices();

        // coleta arestas sem duplicar (para não-dirigido: pega (u,v) só quando u < v)
        List<Aresta> todasArestas = new ArrayList<>();
        boolean naoDirecionado = !grafo.isGrafoDirecionado();

        for (int u = 1; u <= totalVertices; u++) {
            for (int v : grafo.getVizinhosDoVertice(u)) {
                if (naoDirecionado && u > v) continue; // evita duplicidade
                double peso = grafo.getPesoDaAresta(u, v);
                todasArestas.add(new Aresta(u, v, peso));
            }
        }

        todasArestas.sort(Comparator.comparingDouble(Aresta::getPeso));

        UnionFind uf = new UnionFind(totalVertices);
        List<Aresta> mst = new ArrayList<>();
        double custoTotal = 0.0;

        for (Aresta e : todasArestas) {
            if (uf.union(e.getOrigem(), e.getDestino())) {
                mst.add(e);
                custoTotal += e.getPeso();
                if (mst.size() == totalVertices - 1) break;
            }
        }

        return new ResultadoMST(mst, custoTotal);
    }
}
