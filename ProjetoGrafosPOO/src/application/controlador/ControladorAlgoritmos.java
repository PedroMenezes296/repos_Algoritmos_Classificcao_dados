package application.controlador;

import algoritmos.*;
import modelo.Aresta;
import representacao.Grafo;
import representacao.GrafoListaAdj;
import representacao.GrafoMatrizAdj;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Controlador com exemplos mínimos.
 * Por enquanto, as saídas dos algoritmos são simuladas para “ver” o fluxo.
 */
public class ControladorAlgoritmos {

    private static final String BASE_RECURSOS = "main/resources/grafos/";

    // Representação: 1 = LA, 2 = MA
    private static Grafo criarGrafoExemploPequeno(int codigoRepresentacao, boolean grafoDirecionado) {
        int numeroDeVertices = 5;

        Grafo grafo;
        if (codigoRepresentacao == 2) {
            grafo = new GrafoMatrizAdj(numeroDeVertices, grafoDirecionado);
        } else {
            grafo = new GrafoListaAdj(numeroDeVertices, grafoDirecionado);
        }

        // Exemplo simples (1--2, 1--3, 2--4, 3--5, 4--5) com peso 1.0
        grafo.adicionarAresta(1, 2, 1.0);
        grafo.adicionarAresta(1, 3, 1.0);
        grafo.adicionarAresta(2, 4, 1.0);
        grafo.adicionarAresta(3, 5, 1.0);
        grafo.adicionarAresta(4, 5, 1.0);

        return grafo;
    }

    public static void executarBFSNoGrafoExemplo(int codigoRepresentacao) {
        Grafo grafoExemplo = criarGrafoExemploPequeno(codigoRepresentacao, false);
        int idVerticeOrigem = 1;

        System.out.println();
        System.out.println("[INFO] Rodando BFS em grafo-exemplo (" + nomeRep(codigoRepresentacao) + ")...");
        // List<Integer> ordemVisitacao = AlgoritmoBuscaEmLargura.executarBuscaEmLargura(grafoExemplo, idVerticeOrigem);
        List<Integer> ordemVisitacao = AlgoritmoBuscaEmLargura.executarBuscaEmLargura(grafoExemplo, idVerticeOrigem);
        System.out.println("[SAÍDA] Ordem de visita: " + formatarListaDeInteiros(ordemVisitacao));
    }

    public static void executarDFSNoGrafoExemplo(int codigoRepresentacao) {
        Grafo grafoExemplo = criarGrafoExemploPequeno(codigoRepresentacao, false);
        int idVerticeOrigem = 1;

        System.out.println();
        System.out.println("[INFO] Rodando DFS em grafo-exemplo (" + nomeRep(codigoRepresentacao) + ")...");
        // List<Integer> ordemVisitacao = AlgoritmoBuscaEmProfundidade.executarBuscaEmProfundidade(grafoExemplo, idVerticeOrigem);
        List<Integer> ordemVisitacao = AlgoritmoBuscaEmProfundidade.executarBuscaEmProfundidade(grafoExemplo, idVerticeOrigem);
        System.out.println("[SAÍDA] Ordem de visita: " + formatarListaDeInteiros(ordemVisitacao));
    }

    public static void executarDijkstraNoGrafoExemplo(int codigoRepresentacao) {
        Grafo grafoExemplo = criarGrafoExemploPequeno(codigoRepresentacao, true);
        int idVerticeOrigem = 1;

        System.out.println();
        System.out.println("[INFO] Rodando Dijkstra em grafo-exemplo (" + nomeRep(codigoRepresentacao) + ")...");
        double[] distancias = AlgoritmoDijkstra.executarDijkstra(grafoExemplo, idVerticeOrigem);
        imprimirVetorDeDistancias(distancias);
    }


    public static void executarBellmanFordNoGrafoExemplo(int codigoRepresentacao) {
        Grafo grafoExemplo = criarGrafoExemploPequeno(codigoRepresentacao, true);
        int idVerticeOrigem = 1;

        System.out.println();
        System.out.println("[INFO] Rodando Bellman-Ford em grafo-exemplo (" + nomeRep(codigoRepresentacao) + ")...");
        double[] distancias = AlgoritmoBellmanFord.executarBellmanFord(grafoExemplo, idVerticeOrigem);
        imprimirVetorDeDistancias(distancias);
    }


    public static void executarFloydWarshallExemplo(int codigoRep) {
        boolean dirigido = true;
        representacao.Grafo g = criarGrafoExemploPequeno(codigoRep, dirigido);

        double[][] dist = algoritmos.AlgoritmoFloydWarshall.executarFloydWarshall(g);

        System.out.println("[INFO] Floyd–Warshall (exemplo pequeno, " + nomeRep(codigoRep) + "):");
        imprimirMatrizDistancias(dist);
    }

    public static void executarPrimExemplo(int codigoRepresentacao) {
        // cria grafo simples de teste
        Grafo g = criarGrafoExemploPequeno(codigoRepresentacao, false);

        int verticeInicial = 1;

        System.out.println();
        System.out.println("[INFO] Executando Prim em grafo-exemplo (" + nomeRep(codigoRepresentacao) + ")...");

        AlgoritmoPrim.ResultadoMST resultado = AlgoritmoPrim.executarPrim(g, verticeInicial);

        System.out.println("[SAÍDA] Arestas da MST:");
        for (modelo.Aresta e : resultado.arestasDaMST) {
            System.out.println(e.getIdVerticeOrigem() + " - " + e.getIdVerticeDestino() +
                    " (peso " + e.getPesoAresta() + ")");
        }

        System.out.println("Custo total da MST: " + resultado.custoTotal);
    }



    // ControladorAlgoritmos.java
    public static void executarKruskalExemplo(int codigoRep) {
        // Kruskal requer grafo NÃO-direcionado
        boolean dirigido = false;
        representacao.Grafo g = criarGrafoExemploPequeno(codigoRep, dirigido);

        System.out.println();
        System.out.println("[INFO] Executando Kruskal em grafo-exemplo (" + nomeRep(codigoRep) + ")...");

        algoritmos.AlgoritmoKruskal.ResultadoMST resultado =
                algoritmos.AlgoritmoKruskal.executarKruskal(g);

        System.out.println("[SAÍDA] Arestas da MST (Kruskal):");
        for (modelo.Aresta e : resultado.arestasDaMST) {
            System.out.println(e.getIdVerticeOrigem() + " - " + e.getIdVerticeDestino()
                    + " (peso " + e.getPesoAresta() + ")");
        }
        System.out.println("Custo total da MST: " + resultado.custoTotal);
    }


    public static void executarFordFulkersonExemplo(int codigoRep) {
        boolean dirigido = true;
        Grafo g = criarGrafoExemploPequeno(codigoRep, dirigido);

        int fonte = 1;
        int sorvedouro = g.getNumeroDeVertices();

        AlgoritmoFordFulkerson.ResultadoFluxo res =
                AlgoritmoFordFulkerson.executarFordFulkerson(g, fonte, sorvedouro);

        System.out.println("[INFO] Ford-Fulkerson (exemplo, " + nomeRep(codigoRep) + "):");
        System.out.println("[SAÍDA] Fluxo máximo = " + res.fluxoMaximo);
    }


    private static representacao.Grafo criarGrafoExemploFluxo(int codigoRep, boolean dirigido) {
        representacao.Grafo g = (codigoRep == 2)
                ? new representacao.GrafoMatrizAdj(6, dirigido)
                : new representacao.GrafoListaAdj(6, dirigido);

        // Exemplo clássico
        g.adicionarAresta(1, 2, 16);
        g.adicionarAresta(1, 3, 13);
        g.adicionarAresta(2, 3, 10);
        g.adicionarAresta(3, 2, 4);
        g.adicionarAresta(2, 4, 12);
        g.adicionarAresta(3, 5, 14);
        g.adicionarAresta(4, 3, 9);
        g.adicionarAresta(5, 4, 7);
        g.adicionarAresta(4, 6, 20);
        g.adicionarAresta(5, 6, 4);
        return g;
    }

    private static representacao.Grafo carregarGrafoDeRecurso(String arquivo, int rep, boolean dirigido) {
        boolean usarLA = (rep != 2);
        io.LeitorDeGrafoGTGraph leitor = new io.LeitorDeGrafoGTGraph();
        return leitor.carregarDeRecursoClasspath(BASE_RECURSOS + arquivo, usarLA, dirigido);
    }
    // ===== utilitários =====
    public static void executarBFSEmRecursoClasspath(String arquivo, int rep, int origem) {
        representacao.Grafo g = carregarGrafoDeRecurso(arquivo, rep, true);
        System.out.println("[INFO] BFS em arquivo: " + arquivo + " (" + nomeRep(rep) + ")");
        java.util.List<Integer> ordem = algoritmos.AlgoritmoBuscaEmLargura.executarBuscaEmLargura(g, origem);
        System.out.println("[SAÍDA] Ordem de visita: " + formatarListaDeInteiros(ordem));
    }

    // ==================== DFS (.gr) ====================
    public static void executarDFSEmRecursoClasspath(String arquivo, int rep, int origem) {
        representacao.Grafo g = carregarGrafoDeRecurso(arquivo, rep, true);
        System.out.println("[INFO] DFS em arquivo: " + arquivo + " (" + nomeRep(rep) + ")");
        java.util.List<Integer> ordem = algoritmos.AlgoritmoBuscaEmProfundidade.executarBuscaEmProfundidade(g, origem);
        System.out.println("[SAÍDA] Ordem de visita: " + formatarListaDeInteiros(ordem));
    }

    // ============ Floyd–Warshall (.gr) ================
    public static void executarFloydWarshallEmRecursoClasspath(String arquivo, int rep) {
        representacao.Grafo g = carregarGrafoDeRecurso(arquivo, rep, true);
        System.out.println("[INFO] Floyd-Warshall em arquivo: " + arquivo + " (" + nomeRep(rep) + ")");
        double[][] dist = algoritmos.AlgoritmoFloydWarshall.executarFloydWarshall(g);
        imprimirMatrizDistancias(dist);
    }

    // ==================== Prim (.gr) ===================
    public static void executarPrimEmRecursoClasspath(String arquivo, int rep, int origem) {
        // MST requer grafo não-direcionado
        representacao.Grafo g = carregarGrafoDeRecurso(arquivo, rep, false);
        System.out.println("[INFO] Prim em arquivo: " + arquivo + " (" + nomeRep(rep) + "), origem=" + origem);
        algoritmos.AlgoritmoPrim.ResultadoMST r = algoritmos.AlgoritmoPrim.executarPrim(g, origem);
        System.out.println("[SAÍDA] Arestas da MST (Prim):");
        for (modelo.Aresta e : r.arestasDaMST) {
            System.out.println(e.getIdVerticeOrigem() + " - " + e.getIdVerticeDestino() + " (peso " + e.getPesoAresta() + ")");
        }
        System.out.println("Custo total da MST: " + r.custoTotal);
    }

    // ================== Kruskal (.gr) =================
    public static void executarKruskalEmRecursoClasspath(String arquivo, int rep) {
        // MST requer grafo não-direcionado
        representacao.Grafo g = carregarGrafoDeRecurso(arquivo, rep, false);
        System.out.println("[INFO] Kruskal em arquivo: " + arquivo + " (" + nomeRep(rep) + ")");
        algoritmos.AlgoritmoKruskal.ResultadoMST r = algoritmos.AlgoritmoKruskal.executarKruskal(g);
        System.out.println("[SAÍDA] Arestas da MST (Kruskal):");
        for (modelo.Aresta e : r.arestasDaMST) {
            System.out.println(e.getIdVerticeOrigem() + " - " + e.getIdVerticeDestino() + " (peso " + e.getPesoAresta() + ")");
        }
        System.out.println("Custo total da MST: " + r.custoTotal);
    }

    // ============ Ford–Fulkerson (.gr) ===============
    public static void executarFordFulkersonEmRecursoClasspath(String arquivo, int rep, int fonte, int sorvedouro) {
        // Fluxo em grafo dirigido
        representacao.Grafo g = carregarGrafoDeRecurso(arquivo, rep, true);
        System.out.println("[INFO] Ford-Fulkerson em arquivo: " + arquivo + " (" + nomeRep(rep) + "), s=" + fonte + ", t=" + sorvedouro);
        double fluxo = algoritmos.AlgoritmoFordFulkerson.executarFluxoMaximo(g, fonte, sorvedouro);
        System.out.println("[SAÍDA] Fluxo máximo: " + fluxo);
    }

    // --------- Impressões auxiliares padronizadas ---------
    private static void imprimirVetorDistancias(double[] dist) {
        StringBuilder sb = new StringBuilder();
        sb.append("[SAÍDA] Distâncias: ");
        for (int i = 1; i < dist.length; i++) {
            sb.append(i).append("=").append(
                    Double.isInfinite(dist[i]) ? "INF" : String.format("%.0f", dist[i])
            );
            if (i < dist.length - 1) sb.append(", ");
        }
        System.out.println(sb.toString());
    }

    private static void imprimirMatrizDistancias(double[][] dist) {
        System.out.println("[SAÍDA] Matriz de distâncias mínimas:");
        int n = dist.length - 1;
        for (int i = 1; i <= n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 1; j <= n; j++) {
                sb.append(Double.isInfinite(dist[i][j]) ? "INF" : String.format("%.0f", dist[i][j]));
                if (j < n) sb.append("\t");
            }
            System.out.println(sb.toString());
        }
    }

    private static String nomeRep(int codigoRepresentacao) {
        return (codigoRepresentacao == 2) ? "MA" : "LA";
    }

    private static String formatarListaDeInteiros(List<Integer> listaDeInteiros) {
        return String.join(" -> ", listaDeInteiros.stream().map(Object::toString).toList());
    }

    private static void imprimirVetorDeDistancias(double[] vetorDeDistancias) {
        System.out.println("[SAÍDA] Distâncias a partir da origem:");
        for (int idVertice = 1; idVertice < vetorDeDistancias.length; idVertice++) {
            System.out.println("  Vertice " + idVertice + ": " + vetorDeDistancias[idVertice]);
        }
    }


    public static void carregarGrafoDeRecursoEImprimirResumo(String nomeArquivoGr, int codigoRepresentacao) {
        boolean usarListaDeAdjacencia = (codigoRepresentacao != 2);
        boolean grafoDirecionado = true; // padrão recomendado para GTGraph

        io.LeitorDeGrafoGTGraph leitor = new io.LeitorDeGrafoGTGraph();
        String caminho = "grafos/" + nomeArquivoGr; // dentro de src/main/resources/grafos/

        representacao.Grafo grafo = leitor.carregarDeRecursoClasspath(
                caminho,
                usarListaDeAdjacencia,
                grafoDirecionado
        );

        System.out.println();
        System.out.println("[INFO] Recurso carregado: " + caminho);
        System.out.println("[INFO] Representação: " + (usarListaDeAdjacencia ? "LA" : "MA"));
        System.out.println("[INFO] Direcionado: " + grafo.isGrafoDirecionado());
        System.out.println("[INFO] Vértices: " + grafo.getNumeroDeVertices());
        System.out.println("[INFO] Arestas:  " + grafo.getNumeroDeArestas());

        int limiteParaMostrar = Math.min(5, grafo.getNumeroDeVertices());
        for (int v = 1; v <= limiteParaMostrar; v++) {
            System.out.println("  Vizinhos de " + v + ": " + grafo.getVizinhosDoVertice(v));
        }
    }

    // ===================== ARQUIVOS (.gr) =====================

    public static void executarDijkstraEmArquivoLocal(String caminhoLocal, int codigoRep, int origem) {
        boolean dirigido = true;
        boolean usarLA = (codigoRep != 2);

        io.LeitorDeGrafoGTGraph leitor = new io.LeitorDeGrafoGTGraph();
        representacao.Grafo g = leitor.carregarDeArquivoLocal(caminhoLocal, usarLA, dirigido);

        double[] distancias = algoritmos.AlgoritmoDijkstra.executarDijkstra(g, origem);

        System.out.println("[INFO] Dijkstra arquivo: " + caminhoLocal + " (" + nomeRep(codigoRep) + ")");
        System.out.println("[SAÍDA] Distâncias mínimas a partir da origem " + origem + ":");
        for (int v = 1; v < distancias.length; v++) {
            System.out.println("  até " + v + " = " + distancias[v]);
        }

    }

    public static void executarDijkstraEmRecursoClasspath(String caminhoRecurso, int codigoRep, int origem) {
        boolean dirigido = true;
        boolean usarLA = (codigoRep != 2);

        io.LeitorDeGrafoGTGraph leitor = new io.LeitorDeGrafoGTGraph();
        representacao.Grafo g = leitor.carregarDeRecursoClasspath(caminhoRecurso, usarLA, dirigido);

        double[] dist = algoritmos.AlgoritmoDijkstra.executarDijkstra(g, origem);

        System.out.println("[INFO] Dijkstra (classpath): " + caminhoRecurso + " (" + nomeRep(codigoRep) + ")");
        System.out.println("[SAÍDA] Distâncias mínimas a partir da origem " + origem + ":");
        for (int v = 1; v < dist.length; v++) {
            System.out.println("  até " + v + " = " + dist[v]);
        }
    }

    public static void executarBellmanFordEmRecursoClasspath(String caminhoRecurso, int codigoRep, int origem) {
        boolean dirigido = true;
        boolean usarLA = (codigoRep != 2);

        io.LeitorDeGrafoGTGraph leitor = new io.LeitorDeGrafoGTGraph();
        representacao.Grafo g = leitor.carregarDeRecursoClasspath(caminhoRecurso, usarLA, dirigido);

        double[] dist = algoritmos.AlgoritmoBellmanFord.executarBellmanFord(g, origem);

        System.out.println("[INFO] Bellman-Ford (classpath): " + caminhoRecurso + " (" + nomeRep(codigoRep) + ")");
        System.out.println("[SAÍDA] Distâncias mínimas a partir da origem " + origem + ":");
        for (int v = 1; v < dist.length; v++) {
            System.out.println("  até " + v + " = " + dist[v]);
        }
    }


    public static void executarBellmanFordEmArquivoLocal(String caminhoLocal, int codigoRep, int origem) {
        boolean dirigido = true;
        boolean usarLA = (codigoRep != 2);

        io.LeitorDeGrafoGTGraph leitor = new io.LeitorDeGrafoGTGraph();
        representacao.Grafo g = leitor.carregarDeArquivoLocal(caminhoLocal, usarLA, dirigido);

        double[] dist = algoritmos.AlgoritmoBellmanFord.executarBellmanFord(g, origem);
        System.out.println("[INFO] Bellman–Ford arquivo: " + caminhoLocal + " (" + nomeRep(codigoRep) + ")");
        System.out.print("[SAÍDA] Distâncias (origem=" + origem + "): ");
        for (int v = 1; v < dist.length; v++) {
            System.out.print(v + "=" + (Double.isInfinite(dist[v]) ? "INF" : (long) dist[v]));
            if (v < dist.length - 1) System.out.print(", ");
        }
        System.out.println();
    }

    public static void executarDijkstraDeArquivo(String nomeArquivoGr, int codigoRepresentacao, int idVerticeOrigem) {
        boolean usarListaAdjacencia = (codigoRepresentacao != 2); // 1=LA, 2=MA
        boolean grafoDirecionado = true; // GTGraph é dirigido

        io.LeitorDeGrafoGTGraph leitor = new io.LeitorDeGrafoGTGraph();
        representacao.Grafo grafo = leitor.carregarDeRecursoClasspath(
                "grafos/" + nomeArquivoGr,
                usarListaAdjacencia,
                grafoDirecionado
        );

        System.out.println();
        System.out.println("[INFO] Arquivo: " + nomeArquivoGr);
        System.out.println("[INFO] Representação: " + (usarListaAdjacencia ? "LA" : "MA"));
        System.out.println("[INFO] Origem: " + idVerticeOrigem);

        double[] distancias = algoritmos.AlgoritmoDijkstra.executarDijkstra(grafo, idVerticeOrigem);
        imprimirVetorDeDistanciasArquivo(distancias);
    }

    public static void executarBellmanFordDeArquivo(String nomeArquivoGr, int codigoRepresentacao, int idVerticeOrigem) {
        boolean usarListaAdjacencia = (codigoRepresentacao != 2); // 1=LA, 2=MA
        boolean grafoDirecionado = true;

        io.LeitorDeGrafoGTGraph leitor = new io.LeitorDeGrafoGTGraph();
        representacao.Grafo grafo = leitor.carregarDeRecursoClasspath(
                "grafos/" + nomeArquivoGr,
                usarListaAdjacencia,
                grafoDirecionado
        );

        System.out.println();
        System.out.println("[INFO] Arquivo: " + nomeArquivoGr);
        System.out.println("[INFO] Representação: " + (usarListaAdjacencia ? "LA" : "MA"));
        System.out.println("[INFO] Origem: " + idVerticeOrigem);

        double[] distancias = algoritmos.AlgoritmoBellmanFord.executarBellmanFord(grafo, idVerticeOrigem);
        imprimirVetorDeDistanciasArquivo(distancias);
    }

    private static void imprimirVetorDeDistanciasArquivo(double[] dist) {
        StringBuilder sb = new StringBuilder("[SAÍDA] Distâncias: ");
        for (int i = 1; i < dist.length; i++) {
            if (i > 1) sb.append(" | ");
            sb.append(i).append("=").append(dist[i]);
        }
        System.out.println(sb.toString());
    }

    public static void carregarGrafoDeArquivoLocalEImprimirResumo(String caminhoCompleto, int codigoRep) {
        boolean usarLA = (codigoRep != 2);
        boolean dirigido = true;

        io.LeitorDeGrafoGTGraph leitor = new io.LeitorDeGrafoGTGraph();
        representacao.Grafo g = leitor.carregarDeArquivoLocal(caminhoCompleto, usarLA, dirigido);

        System.out.println("[INFO] Arquivo local: " + caminhoCompleto);
        System.out.println("[INFO] Vértices: " + g.getNumeroDeVertices()
                + " | Arestas: " + g.getNumeroDeArestas()
                + " | Direcionado: " + g.isGrafoDirecionado());
    }

    // ===== Batch helpers =====
    private static String[] listarGrClasspath() {
        return application.MenuPrincipal.listarArquivosDeResourcesClasspath(BASE_RECURSOS);
    }

    public static void gerarTodosCSVs() {
        new java.io.File("out").mkdirs(); // garante pasta local
        gerarCSVTempoDijkstraEmLote();
        gerarCSVTempoBellmanFordEmLote();
        gerarCSVTempoFloydWarshallEmLote();
        gerarCSVTempoPrimEmLote();
        gerarCSVTempoKruskalEmLote();
        gerarCSVTempoFordFulkersonEmLote();
        System.out.println("[FIM] Todos os CSVs gerados em /out");
    }

    private static int ultimoVertice(representacao.Grafo g) { return g.getNumeroDeVertices(); }

    // ====== Dijkstra -> out/dijkstra.csv ======
    public static void gerarCSVTempoDijkstraEmLote() {
        String[] arquivos = listarGrClasspath();
        if (arquivos == null || arquivos.length == 0) {
            System.out.println("[AVISO] Sem arquivos em " + BASE_RECURSOS);
            return;
        }
        try (util.CSVWriterSimples csv = new util.CSVWriterSimples("out/dijkstra.csv")) {
            csv.escreverLinha("arquivo","rep","origem","tempo_ms");
            for (String arq : arquivos) {
                for (int repCod : new int[]{1,2}) {
                    representacao.Grafo g = carregarGrafoDeRecurso(arq, repCod, true);
                    int origem = 1;
                    long t0 = util.MedidorTempo.agoraNanos();
                    double[] dist = algoritmos.AlgoritmoDijkstra.executarDijkstra(g, origem);
                    double ms = util.MedidorTempo.nanosParaMillis(System.nanoTime() - t0);
                    csv.escreverLinha(arq, nomeRep(repCod), String.valueOf(origem), String.format("%.3f", ms));
                }
            }
        }
        System.out.println("[OK] CSV gerado: out/dijkstra.csv");
    }

    // ====== Bellman-Ford -> out/bellman_ford.csv ======
    public static void gerarCSVTempoBellmanFordEmLote() {
        String[] arquivos = listarGrClasspath();
        if (arquivos == null || arquivos.length == 0) { System.out.println("[AVISO] Sem arquivos."); return; }
        try (util.CSVWriterSimples csv = new util.CSVWriterSimples("out/bellman_ford.csv")) {
            csv.escreverLinha("arquivo","rep","origem","tempo_ms");
            for (String arq : arquivos) {
                for (int repCod : new int[]{1,2}) {
                    representacao.Grafo g = carregarGrafoDeRecurso(arq, repCod, true);
                    int origem = 1;
                    long t0 = util.MedidorTempo.agoraNanos();
                    double[] dist = algoritmos.AlgoritmoBellmanFord.executarBellmanFord(g, origem);
                    double ms = util.MedidorTempo.nanosParaMillis(System.nanoTime() - t0);
                    csv.escreverLinha(arq, nomeRep(repCod), String.valueOf(origem), String.format("%.3f", ms));
                }
            }
        }
        System.out.println("[OK] CSV gerado: out/bellman_ford.csv");
    }

    // ====== Floyd-Warshall -> out/floyd_warshall.csv ======
    public static void gerarCSVTempoFloydWarshallEmLote() {
        String[] arquivos = listarGrClasspath();
        if (arquivos == null || arquivos.length == 0) { System.out.println("[AVISO] Sem arquivos."); return; }
        try (util.CSVWriterSimples csv = new util.CSVWriterSimples("out/floyd_warshall.csv")) {
            csv.escreverLinha("arquivo","rep","tempo_ms");
            for (String arq : arquivos) {
                for (int repCod : new int[]{1,2}) {
                    representacao.Grafo g = carregarGrafoDeRecurso(arq, repCod, true);
                    long t0 = util.MedidorTempo.agoraNanos();
                    double[][] dist = algoritmos.AlgoritmoFloydWarshall.executarFloydWarshall(g);
                    double ms = util.MedidorTempo.nanosParaMillis(System.nanoTime() - t0);
                    csv.escreverLinha(arq, nomeRep(repCod), String.format("%.3f", ms));
                }
            }
        }
        System.out.println("[OK] CSV gerado: out/floyd_warshall.csv");
    }

    // ====== Prim -> out/prim.csv  (não-direcionado) ======
    public static void gerarCSVTempoPrimEmLote() {
        String[] arquivos = listarGrClasspath();
        if (arquivos == null || arquivos.length == 0) { System.out.println("[AVISO] Sem arquivos."); return; }
        try (util.CSVWriterSimples csv = new util.CSVWriterSimples("out/prim.csv")) {
            csv.escreverLinha("arquivo","rep","origem","tempo_ms","custo_total");
            for (String arq : arquivos) {
                for (int repCod : new int[]{1,2}) {
                    representacao.Grafo g = carregarGrafoDeRecurso(arq, repCod, false);
                    int origem = 1;
                    long t0 = util.MedidorTempo.agoraNanos();
                    algoritmos.AlgoritmoPrim.ResultadoMST r = algoritmos.AlgoritmoPrim.executarPrim(g, origem);
                    double ms = util.MedidorTempo.nanosParaMillis(System.nanoTime() - t0);
                    csv.escreverLinha(arq, nomeRep(repCod), String.valueOf(origem),
                            String.format("%.3f", ms), String.format("%.3f", r.custoTotal));
                }
            }
        }
        System.out.println("[OK] CSV gerado: out/prim.csv");
    }

    // ====== Kruskal -> out/kruskal.csv (não-direcionado) ======
    public static void gerarCSVTempoKruskalEmLote() {
        String[] arquivos = listarGrClasspath();
        if (arquivos == null || arquivos.length == 0) { System.out.println("[AVISO] Sem arquivos."); return; }
        try (util.CSVWriterSimples csv = new util.CSVWriterSimples("out/kruskal.csv")) {
            csv.escreverLinha("arquivo","rep","tempo_ms","custo_total");
            for (String arq : arquivos) {
                for (int repCod : new int[]{1,2}) {
                    representacao.Grafo g = carregarGrafoDeRecurso(arq, repCod, false);
                    long t0 = util.MedidorTempo.agoraNanos();
                    algoritmos.AlgoritmoKruskal.ResultadoMST r = algoritmos.AlgoritmoKruskal.executarKruskal(g);
                    double ms = util.MedidorTempo.nanosParaMillis(System.nanoTime() - t0);
                    csv.escreverLinha(arq, nomeRep(repCod), String.format("%.3f", ms), String.format("%.3f", r.custoTotal));
                }
            }
        }
        System.out.println("[OK] CSV gerado: out/kruskal.csv");
    }

    // ====== Ford–Fulkerson -> out/ford_fulkerson.csv ======
    public static void gerarCSVTempoFordFulkersonEmLote() {
        String[] arquivos = listarGrClasspath();
        if (arquivos == null || arquivos.length == 0) { System.out.println("[AVISO] Sem arquivos."); return; }
        try (util.CSVWriterSimples csv = new util.CSVWriterSimples("out/ford_fulkerson.csv")) {
            csv.escreverLinha("arquivo","rep","fonte","sorvedouro","tempo_ms","fluxo_max");
            for (String arq : arquivos) {
                for (int repCod : new int[]{1,2}) {
                    representacao.Grafo g = carregarGrafoDeRecurso(arq, repCod, true);
                    int s = 1, t = ultimoVertice(g);
                    long t0 = util.MedidorTempo.agoraNanos();
                    double fluxo = algoritmos.AlgoritmoFordFulkerson.executarFluxoMaximo(g, s, t);
                    double ms = util.MedidorTempo.nanosParaMillis(System.nanoTime() - t0);
                    csv.escreverLinha(arq, nomeRep(repCod), String.valueOf(s), String.valueOf(t),
                            String.format("%.3f", ms), String.format("%.3f", fluxo));
                }
            }
        }
        System.out.println("[OK] CSV gerado: out/ford_fulkerson.csv");
    }


}
