package io;

import representacao.Grafo;
import representacao.GrafoListaAdj;
import representacao.GrafoMatrizAdj;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Leitor simples de grafos no formato GTGraph (.gr) a partir do classpath.
 * - Comentários começam com 'c'
 * - Linha de cabeçalho: p sp V E
 * - Arestas: a u v w   (direcionadas por padrão)
 * Sem tratamento avançado de erros; foco em lógica crua e nomes claros.
 */
public class LeitorDeGrafoGTGraph {

    public Grafo carregarDeRecursoClasspath(
            String caminhoRecursoDentroDeResources,
            boolean usarListaDeAdjacencia,
            boolean grafoDirecionado
    ) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = cl.getResourceAsStream(caminhoRecursoDentroDeResources);
            if (inputStream == null) {
                throw new RuntimeException("Recurso não encontrado: " + caminhoRecursoDentroDeResources);
            }
            BufferedReader leitor = new BufferedReader(new InputStreamReader(inputStream));

            int numeroDeVertices = -1;

            String linha;
            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("c")) {
                    continue; // ignora comentários e linhas vazias
                }

                if (linha.startsWith("p")) {
                    // Ex.: p sp 5 8
                    String[] partes = linha.split("\\s+");
                    // partes[0] = "p", partes[1] = "sp", partes[2] = V, partes[3] = E
                    numeroDeVertices = Integer.parseInt(partes[2]);
                    break;
                }
            }

            // cria grafo conforme representação
            Grafo grafo = usarListaDeAdjacencia
                    ? new GrafoListaAdj(numeroDeVertices, grafoDirecionado)
                    : new GrafoMatrizAdj(numeroDeVertices, grafoDirecionado);

            // continua lendo arestas
            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("c")) {
                    continue;
                }
                if (linha.startsWith("a")) {
                    // Ex.: a u v w
                    String[] partes = linha.split("\\s+");
                    int u = Integer.parseInt(partes[1]);
                    int v = Integer.parseInt(partes[2]);
                    double w = Double.parseDouble(partes[3]);
                    grafo.adicionarAresta(u, v, w);
                }
            }

            leitor.close();
            return grafo;

        } catch (Exception e) {
            // Versão crua: sem tratamento elaborado
            throw new RuntimeException("Falha ao ler recurso: " + caminhoRecursoDentroDeResources, e);
        }
    }

    // Lê de um caminho local (ex.: "main/resources/grafos/sample100-1980.gr")
    public Grafo carregarDeArquivoLocal(String caminhoArquivoLocal,
                                        boolean usarListaDeAdjacencia,
                                        boolean grafoDirecionado) {
        try (java.io.BufferedReader leitor =
                     java.nio.file.Files.newBufferedReader(java.nio.file.Paths.get(caminhoArquivoLocal))) {

            int numeroDeVertices = -1;
            String linha;

            // lê cabeçalho "p sp V E"
            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("c")) continue;
                if (linha.startsWith("p")) {
                    String[] partes = linha.split("\\s+");
                    numeroDeVertices = Integer.parseInt(partes[2]);
                    break;
                }
            }

            Grafo grafo = usarListaDeAdjacencia
                    ? new representacao.GrafoListaAdj(numeroDeVertices, grafoDirecionado)
                    : new representacao.GrafoMatrizAdj(numeroDeVertices, grafoDirecionado);

            // lê arestas "a u v w"
            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("c")) continue;
                if (linha.startsWith("a")) {
                    String[] partes = linha.split("\\s+");
                    int u = Integer.parseInt(partes[1]);
                    int v = Integer.parseInt(partes[2]);
                    double w = Double.parseDouble(partes[3]);
                    grafo.adicionarAresta(u, v, w);
                }
            }
            return grafo;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao ler arquivo local: " + caminhoArquivoLocal, e);
        }
    }

}
