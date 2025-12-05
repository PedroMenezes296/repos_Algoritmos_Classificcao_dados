package application;

import application.controlador.ControladorAlgoritmos;

import java.nio.file.Files;
import java.util.Scanner;

/**
 * Menu de console simples para acionar exemplos.
 * Por enquanto, as saídas dos algoritmos são simuladas no controlador.
 */


public class MenuPrincipal {

    // MenuPrincipal.java (no topo)
    private static final String BASE_RECURSOS = "main/resources/grafos/";


    public static void main(String[] argumentosLinhaDeComando) {
        Scanner leitorDeEntrada = new Scanner(System.in);
        boolean continuarExecutandoMenu = true;

        while (continuarExecutandoMenu) {
            imprimirMenuPrincipal();

            System.out.print("Escolha uma opção: ");
            String opcaoDigitada = leitorDeEntrada.nextLine().trim();

            switch (opcaoDigitada) {
                case "1" -> executarAcaoBFS(leitorDeEntrada);
                case "2" -> executarAcaoDFS(leitorDeEntrada);
                case "3" -> executarAcaoDijkstraEmArquivo(leitorDeEntrada);
                case "4" -> executarAcaoBellmanFordEmArquivo(leitorDeEntrada);
                case "5" -> executarAcaoPrim(leitorDeEntrada);
                case "6" -> executarAcaoKruskal(leitorDeEntrada);
                case "7" -> executarAcaoFordFulkerson(leitorDeEntrada);
                case "8" -> continuarExecutandoMenu = false;
                case "9" -> executarAcaoCarregarArquivo(leitorDeEntrada);
                case "10" -> application.controlador.ControladorAlgoritmos.gerarTodosCSVs();

                default -> System.out.println("[AVISO] Opção inválida. Tente novamente.");
            }

            if (continuarExecutandoMenu) {
                System.out.println("\nPressione ENTER para voltar ao menu...");
                leitorDeEntrada.nextLine();
            }
        }

        System.out.println("Encerrando aplicação. Até mais!");
    }

    private static void imprimirMenuPrincipal() {
        System.out.println("==================================================");
        System.out.println("           TP2 - Grafos (Menu Principal)          ");
        System.out.println("==================================================");
        System.out.println("1) Executar BFS ");
        System.out.println("2) Executar DFS ");
        System.out.println("3) Executar Dijkstra ");
        System.out.println("4) Executar Bellman-Ford ");
        System.out.println("5) Executar Prim ");
        System.out.println("6) Executar Kruskal ");
        System.out.println("7) Executar Ford-Fulkerson ");
        System.out.println("8) Sair");
        System.out.println("9) Carregar grafo (.gr) de resources e mostrar resumo");
        System.out.println("10) Gerar CSVs (lote)");
        System.out.println("==================================================");
    }

    // ===== AÇÕES DO MENU =====

    private static void executarAcaoBFS(Scanner leitor) {
        String arquivo = escolherArquivoDeRecursos(leitor);
        if (arquivo == null) return;
        int rep = perguntarRepresentacao(leitor);
        System.out.print("Vértice de origem (ex.: 1): ");
        int origem;
        try { origem = Integer.parseInt(leitor.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("[AVISO] Origem inválida."); return; }

        application.controlador.ControladorAlgoritmos
                .executarBFSEmRecursoClasspath(arquivo, rep, origem);
    }

    private static void executarAcaoDFS(Scanner leitor) {
        String arquivo = escolherArquivoDeRecursos(leitor);
        if (arquivo == null) return;
        int rep = perguntarRepresentacao(leitor);
        System.out.print("Vértice de origem (ex.: 1): ");
        int origem;
        try { origem = Integer.parseInt(leitor.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("[AVISO] Origem inválida."); return; }

        application.controlador.ControladorAlgoritmos
                .executarDFSEmRecursoClasspath(arquivo, rep, origem);
    }

    private static void executarAcaoDijkstra(Scanner leitor) {
        String arquivo = escolherArquivoDeRecursos(leitor);
        if (arquivo == null) return;
        int rep = perguntarRepresentacao(leitor);
        System.out.print("Vértice de origem (ex.: 1): ");
        int origem;
        try { origem = Integer.parseInt(leitor.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("[AVISO] Origem inválida."); return; }

        application.controlador.ControladorAlgoritmos
                .executarDijkstraEmRecursoClasspath(arquivo, rep, origem);
    }

    private static void executarAcaoBellmanFord(Scanner leitor) {
        String arquivo = escolherArquivoDeRecursos(leitor);
        if (arquivo == null) return;
        int rep = perguntarRepresentacao(leitor);
        System.out.print("Vértice de origem (ex.: 1): ");
        int origem;
        try { origem = Integer.parseInt(leitor.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("[AVISO] Origem inválida."); return; }

        application.controlador.ControladorAlgoritmos
                .executarBellmanFordEmRecursoClasspath(arquivo, rep, origem);
    }

    private static void executarAcaoFloydWarshall(Scanner leitor) {
        String arquivo = escolherArquivoDeRecursos(leitor);
        if (arquivo == null) return;
        int rep = perguntarRepresentacao(leitor);

        application.controlador.ControladorAlgoritmos
                .executarFloydWarshallEmRecursoClasspath(arquivo, rep);
    }

    private static void executarAcaoPrim(Scanner leitor) {
        String arquivo = escolherArquivoDeRecursos(leitor);
        if (arquivo == null) return;
        int rep = perguntarRepresentacao(leitor);
        System.out.print("Vértice inicial (ex.: 1): ");
        int origem;
        try { origem = Integer.parseInt(leitor.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("[AVISO] Origem inválida."); return; }

        application.controlador.ControladorAlgoritmos
                .executarPrimEmRecursoClasspath(arquivo, rep, origem);
    }

    private static void executarAcaoKruskal(Scanner leitor) {
        String arquivo = escolherArquivoDeRecursos(leitor);
        if (arquivo == null) return;
        int rep = perguntarRepresentacao(leitor);

        application.controlador.ControladorAlgoritmos
                .executarKruskalEmRecursoClasspath(arquivo, rep);
    }

    private static void executarAcaoFordFulkerson(Scanner leitor) {
        String arquivo = escolherArquivoDeRecursos(leitor);
        if (arquivo == null) return;
        int rep = perguntarRepresentacao(leitor);

        System.out.print("Vértice fonte (s): ");
        int fonte;
        try { fonte = Integer.parseInt(leitor.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("[AVISO] Fonte inválida."); return; }

        System.out.print("Vértice sorvedouro (t): ");
        int sorvedouro;
        try { sorvedouro = Integer.parseInt(leitor.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("[AVISO] Sorvedouro inválido."); return; }

        application.controlador.ControladorAlgoritmos
                .executarFordFulkersonEmRecursoClasspath(arquivo, rep, fonte, sorvedouro);
    }

    private static String escolherArquivoDeRecursos(Scanner leitor) {
        String[] arquivos = listarArquivosDeResourcesClasspath(BASE_RECURSOS);
        if (arquivos == null || arquivos.length == 0) {
            System.out.println("[AVISO] Nenhum arquivo encontrado em " + BASE_RECURSOS + " (classpath).");
            return null;
        }
        System.out.println("\nArquivos disponíveis:");
        for (int i = 0; i < arquivos.length; i++) {
            System.out.println("  " + (i + 1) + ") " + arquivos[i]);
        }
        System.out.print("Escolha um número: ");
        int idx;
        try { idx = Integer.parseInt(leitor.nextLine().trim()) - 1; }
        catch (NumberFormatException e) { System.out.println("[AVISO] Entrada inválida."); return null; }
        if (idx < 0 || idx >= arquivos.length) { System.out.println("[AVISO] Número fora do intervalo."); return null; }
        return arquivos[idx];
    }

    // ===== 11) Dijkstra em recurso (.gr via classpath) =====
    private static void executarAcaoDijkstraEmArquivo(Scanner leitor) {
        String[] arquivos = listarArquivosDeResourcesClasspath(BASE_RECURSOS);
        if (arquivos == null || arquivos.length == 0) {
            System.out.println("[AVISO] Nenhum arquivo encontrado em " + BASE_RECURSOS + " (classpath).");
            return;
        }

        System.out.println("\nArquivos disponíveis:");
        for (int i = 0; i < arquivos.length; i++) {
            System.out.println("  " + (i + 1) + ") " + arquivos[i]);
        }
        System.out.print("Escolha um número: ");
        int idx;
        try { idx = Integer.parseInt(leitor.nextLine().trim()) - 1; }
        catch (NumberFormatException e) { System.out.println("[AVISO] Entrada inválida."); return; }
        if (idx < 0 || idx >= arquivos.length) { System.out.println("[AVISO] Número fora do intervalo."); return; }

        int rep = perguntarRepresentacao(leitor);

        System.out.print("Vértice de origem (ex.: 1): ");
        int origem;
        try { origem = Integer.parseInt(leitor.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("[AVISO] Origem inválida."); return; }

        application.controlador.ControladorAlgoritmos.executarDijkstraEmRecursoClasspath(
                BASE_RECURSOS + arquivos[idx], rep, origem
        );
    }

    // ===== 12) Bellman–Ford em recurso (.gr via classpath) =====
    private static void executarAcaoBellmanFordEmArquivo(Scanner leitor) {
        String[] arquivos = listarArquivosDeResourcesClasspath(BASE_RECURSOS);
        if (arquivos == null || arquivos.length == 0) {
            System.out.println("[AVISO] Nenhum arquivo encontrado em " + BASE_RECURSOS + " (classpath).");
            return;
        }

        System.out.println("\nArquivos disponíveis:");
        for (int i = 0; i < arquivos.length; i++) {
            System.out.println("  " + (i + 1) + ") " + arquivos[i]);
        }
        System.out.print("Escolha um número: ");
        int idx;
        try { idx = Integer.parseInt(leitor.nextLine().trim()) - 1; }
        catch (NumberFormatException e) { System.out.println("[AVISO] Entrada inválida."); return; }
        if (idx < 0 || idx >= arquivos.length) { System.out.println("[AVISO] Número fora do intervalo."); return; }

        int rep = perguntarRepresentacao(leitor);

        System.out.print("Vértice de origem (ex.: 1): ");
        int origem;
        try { origem = Integer.parseInt(leitor.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("[AVISO] Origem inválida."); return; }

        application.controlador.ControladorAlgoritmos.executarBellmanFordEmRecursoClasspath(
                BASE_RECURSOS + arquivos[idx], rep, origem
        );
    }

    /**
     * Lista arquivos .gr dentro de uma pasta no classpath (ex.: "grafos/").
     */
    public static String[] listarArquivosDeResourcesClasspath(String pastaClasspath) {
        try {
            java.net.URL url = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(pastaClasspath);
            if (url == null) return null;

            java.nio.file.Path dir = java.nio.file.Paths.get(url.toURI());
            try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.list(dir)) {
                return stream
                        .filter(java.nio.file.Files::isRegularFile)
                        .map(p -> p.getFileName().toString())
                        .filter(n -> n.endsWith(".gr"))
                        .sorted()
                        .toArray(String[]::new);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static void executarAcaoCarregarArquivo(Scanner leitor) {
        System.out.println();

        // você está listando direto do filesystem (ok para dev local)
        String[] arquivos = listarArquivosDeResources("main/resources/grafos/");
        String nomeArquivo;

        if (arquivos != null && arquivos.length > 0) {
            System.out.println("Arquivos disponíveis em resources/grafos:");
            for (int i = 0; i < arquivos.length; i++) {
                System.out.println("  " + (i + 1) + ") " + arquivos[i]);
            }
            System.out.print("Escolha um número: ");
            String escolha = leitor.nextLine().trim();

            int indice;
            try {
                indice = Integer.parseInt(escolha) - 1;
            } catch (NumberFormatException ex) {
                System.out.println("[AVISO] Entrada inválida. Voltando ao menu.");
                return;
            }

            if (indice < 0 || indice >= arquivos.length) {
                System.out.println("[AVISO] Número fora do intervalo. Voltando ao menu.");
                return;
            }

            nomeArquivo = arquivos[indice];
        } else {
            System.out.print("Digite o nome do arquivo .gr (ex.: sample100-1980.gr): ");
            nomeArquivo = leitor.nextLine().trim();
        }

        int codigoRepresentacaoEscolhida = perguntarRepresentacao(leitor);

        // 🔎 detecta automaticamente onde está a pasta grafos no seu projeto
        String baseDir = detectarBaseDirGrafos(); // ex.: "src/main/resources/grafos/"
        String caminhoLocal = baseDir + nomeArquivo;

        // 👀 mostra as primeiras linhas para validar leitura local
        mostrarPrimeirasLinhasArquivoLocal(caminhoLocal, 8);

        // carrega via leitor LOCAL (não via classpath)
        application.controlador.ControladorAlgoritmos
                .carregarGrafoDeArquivoLocalEImprimirResumo(caminhoLocal, codigoRepresentacaoEscolhida);

    }

    /** Tenta, na ordem: src/main/resources/grafos/ → main/resources/grafos/ → resources/grafos/ */
    private static String detectarBaseDirGrafos() {
        String[] candidatos = {
                "src/main/resources/grafos/",
                "main/resources/grafos/",
                "resources/grafos/"
        };
        for (String c : candidatos) {
            java.nio.file.Path p = java.nio.file.Paths.get(c);
            if (java.nio.file.Files.isDirectory(p)) {
                System.out.println("[DEBUG] Base de grafos detectada: " + c
                        + "  (user.dir=" + System.getProperty("user.dir") + ")");
                return c;
            }
        }
        throw new RuntimeException("Pasta 'grafos' não encontrada. Crie em src/main/resources/grafos/ (ou mova seus .gr para lá).");
    }

    /** Mostra as primeiras N linhas do arquivo local para inspeção rápida. */
    private static void mostrarPrimeirasLinhasArquivoLocal(String caminho, int maxLinhas) {
        System.out.println("[DEBUG] Lendo: " + caminho);
        try (java.io.BufferedReader br =
                     java.nio.file.Files.newBufferedReader(java.nio.file.Paths.get(caminho))) {
            String linha;
            int count = 0;
            while ((linha = br.readLine()) != null && count < maxLinhas) {
                System.out.println(">> " + linha);
                count++;
            }
            if (count == 0) System.out.println("[DEBUG] (arquivo vazio?)");
        } catch (Exception e) {
            System.out.println("[DEBUG] Falha ao ler " + caminho + ": " + e.getMessage());
            throw new RuntimeException("Falha ao ler arquivo local: " + caminho, e);
        }
    }



    private static String[] listarArquivosDeResources(String pasta) {
        try {
            // tenta resolver a pasta no classpath (funciona no IntelliJ/Gradle em dev)
            java.net.URL url = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(pasta);

            if (url == null) return null;

            java.nio.file.Path dir = java.nio.file.Paths.get(url.toURI());
            try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.list(dir)) {
                return stream
                        .filter(Files::isRegularFile)
                        .map(p -> p.getFileName().toString())
                        .filter(n -> n.endsWith(".gr"))
                        .sorted()
                        .toArray(String[]::new);
            }
        } catch (Exception e) {
            // ex.: quando empacotado em .jar, listar diretório via FileSystem não funciona
            return null;
        }
    }



    private static int perguntarRepresentacao(Scanner leitor) {
        System.out.println();
        System.out.println("Representação do grafo:");
        System.out.println("  1) Lista de Adjacência (LA)");
        System.out.println("  2) Matriz de Adjacência (MA)");
        System.out.print("Escolha: ");
        String resposta = leitor.nextLine().trim();

        if ("1".equals(resposta)) return 1;
        if ("2".equals(resposta)) return 2;
        return 1; // padrão simples: LA
    }


//    private static void executarAcaoDijkstraEmArquivo(Scanner leitor) {
//        String nomeArquivo = escolherArquivoGr(leitor);         // mostra lista ou pede nome
//        if (nomeArquivo == null) return;
//
//        int representacao = perguntarRepresentacao(leitor);     // 1=LA, 2=MA
//        int origem = perguntarOrigem(leitor);                   // padrao 1
//
//        application.controlador.ControladorAlgoritmos
//                .executarDijkstraDeArquivo(nomeArquivo, representacao, origem);
//    }
//
//    private static void executarAcaoBellmanFordEmArquivo(Scanner leitor) {
//        String nomeArquivo = escolherArquivoGr(leitor);
//        if (nomeArquivo == null) return;
//
//        int representacao = perguntarRepresentacao(leitor);
//        int origem = perguntarOrigem(leitor);
//
//        application.controlador.ControladorAlgoritmos
//                .executarBellmanFordDeArquivo(nomeArquivo, representacao, origem);
//    }

    // Lista arquivos em resources/grafos/ (quando rodando no IntelliJ).
// Se não conseguir listar, cai no fallback pedindo o nome.
    private static String escolherArquivoGr(Scanner leitor) {
        String[] arquivos = listarArquivosDeResources("grafos/"); // se já tem, use o seu
        if (arquivos != null && arquivos.length > 0) {
            System.out.println("Arquivos disponíveis em resources/grafos:");
            for (int i = 0; i < arquivos.length; i++) {
                System.out.println("  " + (i + 1) + ") " + arquivos[i]);
            }
            System.out.print("Escolha um número: ");
            String escolha = leitor.nextLine().trim();
            try {
                int idx = Integer.parseInt(escolha) - 1;
                if (idx >= 0 && idx < arquivos.length) return arquivos[idx];
            } catch (NumberFormatException ignore) {}
            System.out.println("[AVISO] Entrada inválida.");
            return null;
        } else {
            System.out.print("Digite o nome do arquivo .gr (ex.: sample100-1980.gr): ");
            String nome = leitor.nextLine().trim();
            if (nome.isEmpty()) {
                System.out.println("[AVISO] Nome vazio.");
                return null;
            }
            return nome;
        }
    }

    private static int perguntarOrigem(Scanner leitor) {
        System.out.print("Vértice de origem (padrão 1): ");
        String s = leitor.nextLine().trim();
        if (s.isEmpty()) return 1;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 1; }
    }



}
