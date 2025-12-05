import java.util.Scanner;

/* ============================================================
 *                    ÁRVORE B — IMPLEMENTAÇÃO
 * ------------------------------------------------------------
 * Versão com foco pedagógico e LOGS de remoção:
 *  - indica quando ocorre:
 *      • empréstimo à esquerda / direita
 *      • fusão (merge)
 *      • remoção via predecessor / sucessor
 *
 * Grau mínimo (t):
 *    - cada nó possui entre t-1 e 2t-1 chaves
 *    - a raiz é exceção e pode ter menos
 *
 * Opera operações clássicas: busca, inserção, split, remoção.
 * ============================================================
 */
class BTreeNode {

    /* ============================================================
     *                       ATRIBUTOS DO NÓ
     * ============================================================
     */
    int[] keys;               // Vetor de chaves (ordenado)
    BTreeNode[] children;     // Ponteiros para filhos
    int numKeys;              // Número atual de chaves
    boolean leaf;             // Indica se é nó folha
    final int t;              // Grau mínimo

    /* ============================================================
     *                         CONSTRUTOR
     * ============================================================
     * Cada nó é alocado com:
     *   - até 2t-1 chaves
     *   - até 2t filhos
     *   - flag indicando se é folha
     */
    BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        this.keys = new int[2 * t - 1];
        this.children = new BTreeNode[2 * t];
        this.numKeys = 0;
    }

    /* ============================================================
     *                          BUSCA
     * ------------------------------------------------------------
     * Busca recursiva clássica de Árvore B.
     * A busca percorre o nó até encontrar posição adequada.
     *
     * Custo: O(t * log n) mas como t é constante => O(log n)
     * ============================================================
     */
    BTreeNode search(int k) {
        int i = 0;

        // Avança enquanto k é maior que keys[i]
        while (i < numKeys && k > keys[i])
            i++;

        // Encontrou a chave no próprio nó
        if (i < numKeys && keys[i] == k)
            return this;

        // Se for folha, não existe
        if (leaf)
            return null;

        // Busca recursivamente no filho apropriado
        return children[i].search(k);
    }


    /* ============================================================
     * Conta o número de chaves na B-Tree
     * ============================================================
     */
    int countKeys() {
        int total = numKeys;
        if (!leaf)
            for (int i = 0; i <= numKeys; i++)
                total += children[i].countKeys();
        return total;
    }



    /* ============================================================
     *                   IMPRESSÃO HIERÁRQUICA DIDÁTICA
     * ============================================================
     */
    void print(int level) {
        for (int i = 0; i < level; i++)
            System.out.print("\t");

        System.out.print("[ ");
        for (int i = 0; i < numKeys; i++)
            System.out.print(keys[i] + " ");
        System.out.println("]");

        if (!leaf) {
            for (int i = 0; i <= numKeys; i++)
                children[i].print(level + 1);
        }
    }


    /* ============================================================
     *                         INSERÇÃO
     * ------------------------------------------------------------
     * Precondição: O nó não está cheio ao chamar insertNonFull().
     * ============================================================
     */
    void insertNonFull(int k) {

        int i = numKeys - 1;

        // Caso 1: nó folha => inserir diretamente
        if (leaf) {

            // Move chaves para abrir espaço
            while (i >= 0 && keys[i] > k) {
                keys[i + 1] = keys[i];
                i--;
            }

            // Insere no local
            keys[i + 1] = k;
            numKeys++;
        }

        // Caso 2: nó interno
        else {

            // Encontra o filho onde devemos descer
            while (i >= 0 && keys[i] > k)
                i--;

            i++;

            // Se o filho está cheio, é necessário dividir antes de descer
            if (children[i].numKeys == 2 * t - 1) {
                splitChild(i, children[i]);

                // Após o split, decidir em qual dos dois filhos descer
                if (keys[i] < k)
                    i++;
            }

            children[i].insertNonFull(k);
        }
    }

    /* ============================================================
     *                         SPLIT (DIVISÃO)
     * ------------------------------------------------------------
     * Divide um nó filho cheio (y) em dois nós:
     *   y: primeira metade
     *   z: segunda metade
     * A chave mediana sobe para o nó pai.
     * ============================================================
     */
    void splitChild(int index, BTreeNode y) {

        // Novo nó z contém as t-1 chaves superiores de y
        BTreeNode z = new BTreeNode(t, y.leaf);
        z.numKeys = t - 1;

        // Copia chaves superiores para z
        for (int j = 0; j < t - 1; j++)
            z.keys[j] = y.keys[j + t];

        // Copia filhos (se não for folha)
        if (!y.leaf) {
            for (int j = 0; j < t; j++)
                z.children[j] = y.children[j + t];
        }

        // Ajusta y para conter apenas t-1 chaves
        y.numKeys = t - 1;

        // Desloca filhos do pai para abrir espaço
        for (int j = numKeys; j >= index + 1; j--)
            children[j + 1] = children[j];

        children[index + 1] = z;

        // Desloca chaves do pai e insere a mediana
        for (int j = numKeys - 1; j >= index; j--)
            keys[j + 1] = keys[j];

        keys[index] = y.keys[t - 1];
        numKeys++;
    }


    /* ============================================================
     *                        REMOÇÃO COMPLETA
     * ============================================================
     * A remoção é o ponto mais delicado da Árvore B.
     * Sempre que descemos na árvore, garantimos que o próximo nó
     * terá pelo menos t chaves (nunca t-1), evitando subfluxos.
     * ============================================================
     */
    void remove(int k) {

        System.out.println("\n[REMOVE] Tentando remover chave " + k +
                " neste nó: " +
                java.util.Arrays.toString(java.util.Arrays.copyOf(keys, numKeys)));

        int idx = findKey(k);

        /* ----------------------------
         * Caso 1: chave está neste nó
         * ----------------------------
         */
        if (idx < numKeys && keys[idx] == k) {

            if (leaf) {
                System.out.println("   -> Chave " + k + " encontrada em nó FOLHA. Remoção simples.");
                removeFromLeaf(idx);
            }
            else {
                System.out.println("   -> Chave " + k + " encontrada em nó INTERNO. Delegando para removeFromInternal().");
                removeFromInternal(idx);
            }
        }

        /* ----------------------------------------
         * Caso 2: chave está em uma subárvore
         * ----------------------------------------
         */
        else {

            if (leaf) {
                System.out.println("   -> Chave " + k + " não encontrada e nó é folha. Nada a remover.");
                return;
            }

            boolean removeFromLastChild = (idx == numKeys);

            // Antes de descer, garantir que o nó filho terá >= t chaves
            if (children[idx].numKeys < t) {
                System.out.println("   -> Filho em posição " + idx + " tem menos que t chaves. Chamando fill(" + idx + ").");
                fill(idx);
            }

            // Ajuste após possível fusão
            if (removeFromLastChild && idx > numKeys) {
                System.out.println("   -> Após fill/merge, removendo pela subárvore à esquerda (idx-1).");
                children[idx - 1].remove(k);
            }
            else {
                System.out.println("   -> Removendo pela subárvore de índice " + idx + ".");
                children[idx].remove(k);
            }
        }
    }


    /* ============================================================
     *               FUNÇÃO AUXILIAR: LOCALIZA CHAVE
     * ============================================================
     */
    int findKey(int k) {
        int idx = 0;
        while (idx < numKeys && keys[idx] < k)
            idx++;
        return idx;
    }

    /* ============================================================
     *                  REMOÇÃO EM NÓ FOLHA (simples)
     * ============================================================
     */
    void removeFromLeaf(int idx) {

        System.out.println("      removeFromLeaf(): removendo chave " + keys[idx] +
                " da posição " + idx + " em um nó folha.");

        for (int i = idx + 1; i < numKeys; i++)
            keys[i - 1] = keys[i];

        numKeys--;
    }

    /* ============================================================
     *            REMOÇÃO EM NÓ INTERNO (casos conceituais)
     * ------------------------------------------------------------
     * Caso A: o filho à esquerda possui >= t chaves
     *    => substitui pela chave predecessora
     * Caso B: o filho à direita possui >= t chaves
     *    => substitui pela chave sucessora
     * Caso C: ambos têm t-1 chaves
     *    => funde y + chave + z em um único nó
     * ============================================================
     */
    void removeFromInternal(int idx) {

        int k = keys[idx];
        System.out.println("      removeFromInternal(): removendo chave interna " + k +
                " na posição " + idx + ".");

        // Filho à esquerda tem chaves suficientes -> usa PREDECESSOR
        if (children[idx].numKeys >= t) {

            System.out.println("         Caso PREDECESSOR: filho esquerdo tem >= t chaves.");
            int pred = getPredecessor(idx);
            System.out.println("         Predecessor de " + k + " é " + pred + ". Substituindo e removendo do filho esquerdo.");
            keys[idx] = pred;
            children[idx].remove(pred);
        }

        // Filho à direita tem chaves suficientes -> usa SUCESSOR
        else if (children[idx + 1].numKeys >= t) {

            System.out.println("         Caso SUCESSOR: filho direito tem >= t chaves.");
            int succ = getSuccessor(idx);
            System.out.println("         Sucessor de " + k + " é " + succ + ". Substituindo e removendo do filho direito.");
            keys[idx] = succ;
            children[idx + 1].remove(succ);
        }

        // Ambos têm t-1 chaves -> MERGE
        else {

            System.out.println("         Caso MERGE: ambos os filhos têm t-1 chaves. Fazendo merge antes de remover " + k + ".");
            merge(idx);
            children[idx].remove(k);
        }
    }


    /* ============================================================
     *                   PREDECESSOR E SUCESSOR
     * ============================================================
     */
    int getPredecessor(int idx) {
        BTreeNode cur = children[idx];
        while (!cur.leaf)
            cur = cur.children[cur.numKeys];
        return cur.keys[cur.numKeys - 1];
    }

    int getSuccessor(int idx) {
        BTreeNode cur = children[idx + 1];
        while (!cur.leaf)
            cur = cur.children[0];
        return cur.keys[0];
    }


    /* ============================================================
     *                         FILL (GARANTIA)
     * ------------------------------------------------------------
     * Garante que o filho children[idx] terá ao menos t chaves.
     * Se um irmão puder emprestar, ok.
     * Senão, precisa fundir.
     * ============================================================
     */
    void fill(int idx) {

        System.out.println("      fill(" + idx + "): garantindo que o filho tenha pelo menos t chaves.");

        if (idx > 0 && children[idx - 1].numKeys >= t) {
            System.out.println("         -> Irá emprestar do IRMÃO ESQUERDO (borrowFromPrev).");
            borrowFromPrev(idx);
        }
        else if (idx < numKeys && children[idx + 1].numKeys >= t) {
            System.out.println("         -> Irá emprestar do IRMÃO DIREITO (borrowFromNext).");
            borrowFromNext(idx);
        }
        else {

            if (idx < numKeys) {
                System.out.println("         -> Nenhum irmão pode emprestar. Fazendo MERGE com o irmão direito.");
                merge(idx);
            }
            else {
                System.out.println("         -> Nenhum irmão pode emprestar. Fazendo MERGE com o irmão esquerdo (idx-1).");
                merge(idx - 1);
            }
        }
    }


    /* ============================================================
     *         EMPRESTAR DO IRMÃO ANTERIOR (borrowFromPrev)
     * ============================================================
     */
    void borrowFromPrev(int idx) {

        System.out.println("         borrowFromPrev(): emprestando chave do IRMÃO ESQUERDO para o filho de índice " + idx + ".");

        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx - 1];

        // desloca chaves em child para direita
        for (int i = child.numKeys - 1; i >= 0; i--)
            child.keys[i + 1] = child.keys[i];

        // desloca filhos se necessário
        if (!child.leaf)
            for (int i = child.numKeys; i >= 0; i--)
                child.children[i + 1] = child.children[i];

        // passa chave do pai para o início do filho
        child.keys[0] = keys[idx - 1];

        if (!child.leaf)
            child.children[0] = sibling.children[sibling.numKeys];

        // sobe chave do irmão para o pai
        keys[idx - 1] = sibling.keys[sibling.numKeys - 1];

        child.numKeys++;
        sibling.numKeys--;
    }


    /* ============================================================
     *         EMPRESTAR DO IRMÃO PRÓXIMO (borrowFromNext)
     * ============================================================
     */
    void borrowFromNext(int idx) {

        System.out.println("         borrowFromNext(): emprestando chave do IRMÃO DIREITO para o filho de índice " + idx + ".");

        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];

        child.keys[child.numKeys] = keys[idx];

        if (!child.leaf)
            child.children[child.numKeys + 1] = sibling.children[0];

        keys[idx] = sibling.keys[0];

        for (int i = 1; i < sibling.numKeys; i++)
            sibling.keys[i - 1] = sibling.keys[i];

        if (!sibling.leaf)
            for (int i = 1; i <= sibling.numKeys; i++)
                sibling.children[i - 1] = sibling.children[i];

        child.numKeys++;
        sibling.numKeys--;
    }


    /* ============================================================
     *                             MERGE
     * ------------------------------------------------------------
     * Funde children[idx] + chave[idx] + children[idx+1]
     * em um único nó.
     * ============================================================
     */
    void merge(int idx) {

        System.out.println("         merge(): realizando fusão dos filhos " + idx + " e " + (idx + 1) +
                " usando a chave " + keys[idx] + " do nó pai.");

        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];

        child.keys[t - 1] = keys[idx];

        for (int i = 0; i < sibling.numKeys; i++)
            child.keys[i + t] = sibling.keys[i];

        if (!child.leaf)
            for (int i = 0; i <= sibling.numKeys; i++)
                child.children[i + t] = sibling.children[i];

        for (int i = idx + 1; i < numKeys; i++)
            keys[i - 1] = keys[i];

        for (int i = idx + 2; i <= numKeys; i++)
            children[i - 1] = children[i];

        child.numKeys += sibling.numKeys + 1;
        numKeys--;
    }
}


/* ============================================================
 *                         CLASSE ÁRVORE B
 * ============================================================
 */
public class BTree {

    BTreeNode root;
    final int t;

    public BTree(int t) {
        this.t = t;
        this.root = null;
    }

    public void print() {
        if (root != null){
            root.print(0);
            System.out.println("\nBTree (t=" + this.t + ") com " + root.countKeys() + " registro(s)\n");
        }
        else
            System.out.println("[Árvore vazia]");
    }

    public BTreeNode search(int k) {
        return (root == null) ? null : root.search(k);
    }

    /* ============================================================
     *                           INSERÇÃO
     * ============================================================
     */
    public void insert(int k) {

        if (root == null) {
            root = new BTreeNode(t, true);
            root.keys[0] = k;
            root.numKeys = 1;
            return;
        }

        if (root.search(k) != null)
            return; // evita duplicados

        if (root.numKeys == 2 * t - 1) {

            BTreeNode novaRaiz = new BTreeNode(t, false);

            novaRaiz.children[0] = root;
            novaRaiz.splitChild(0, root);

            int i = (novaRaiz.keys[0] < k) ? 1 : 0;
            novaRaiz.children[i].insertNonFull(k);

            root = novaRaiz;
        }
        else
            root.insertNonFull(k);
    }


    /* ============================================================
     *                           REMOÇÃO
     * ============================================================
     */
    public void remove(int k) {

        if (root == null) {
            System.out.println("A árvore está vazia. Nada a remover.");
            return;
        }

        root.remove(k);

        if (root != null && root.numKeys == 0) {
            if (root.leaf)
                root = null;
            else
                root = root.children[0];
        }
    }

    /* Função auxiliar para testes didáticos */
    private static void testarRemocao(BTree tree, int chave) {
        System.out.println("\n======================================");
        System.out.println("ANTES de remover " + chave + ":");
        tree.print();

        System.out.println("Removendo " + chave + "...");
        tree.remove(chave);

        System.out.println("DEPOIS de remover " + chave + ":");
        tree.print();
    }


    /* ============================================================
     *                               MAIN
     * ============================================================
     */
    public static void main(String[] args) {

        final int ORDEM = 3;
        final int N = 63;

        BTree tree = new BTree(ORDEM);

        // Inserção sequencial 1..N
        for (int i = 1; i <= N; i++)
            tree.insert(i);

        System.out.println("Árvore depois de inserir 1.." + N + ":");
        tree.print();

        Scanner sc = new Scanner(System.in);
        int chave;

        // Loop para testar remoções manualmente
        do {
            System.out.print("Digite chave a remover (0 para sair): ");
            chave = sc.nextInt();
            if (chave != 0) {
                testarRemocao(tree, chave);
            }
        } while (chave != 0);

        sc.close();
    }
}
