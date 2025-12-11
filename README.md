Pastas e arquivos src/ ├─ application/ │ ├─ Climain.java │ └─ Program.java ├─ bench/ │ ├─ Cloner.java │ ├─ ColetorPassos.java │ ├─ ColetorPassosDouble.java │ ├─ ColetorPassosInt.java │ ├─ Generator.java │ ├─ Report.java │ ├─ Runner.java │ ├─ RunnerMinimal.java │ ├─ RunnerTerminal.java │ ├─ RunnerTerminalBackup.java (opcional) │ ├─ Scenario.java │ ├─ Timer.java │ └─ Validator.java └─ sorting/ ├─ BucketSort.java ├─ CountingSort.java ├─ Ordenadores.java └─ RadixSort.java (+ se existirem: BubbleSort.java, InsertionSort.java, SelectionSort.java, ShellSort.java, HeapSort.java, MergeSort.java, QuickSort.java)

Declarações de pacote (headers) Garanta que cada arquivo comece com o pacote correto exatamente assim:

Em src/application/*.java

package application;

Em src/bench/*.java

package bench;

Em src/sorting/*.java

package sorting;

Imports e referências 3.1 application.Program e/ou application.Climain
Para comparativos (Bubble, Insertion, Selection, Shell, Heap, Merge, Quick) você chama via fachada:

import sorting.Ordenadores; // agora a fachada está em sorting (não mais sorting.api)

Para os lineares (Counting, Radix, Bucket) você chama direto:

import sorting.CountingSort; import sorting.RadixSort; import sorting.BucketSort;

Mantém o padrão que já vinha usando: comparativos pela Ordenadores, lineares direto.

3.2 bench.RunnerTerminal / bench.RunnerMinimal (se usam os algoritmos) Mesmo racional dos imports: import sorting.Ordenadores; import sorting.CountingSort; import sorting.RadixSort; import sorting.BucketSort;

3.3 sorting.Ordenadores Como agora tudo está no mesmo pacote sorting, os imports de comparativos ficam simples (ou nem precisa importar — pode usar pelo nome simples se as classes estão no mesmo pacote). Exemplo de topo do arquivo: package sorting;

import bench.ColetorPassos; // coletores seguem no pacote bench

// As classes comparativas estão em sorting.* também // import sorting.BubbleSort; // opcional (mesmo pacote)

E internamente, suas delegações ficam assim: public final class Ordenadores { private Ordenadores() {}

public static <T extends Comparable<? super T>> void bubbleSort(T[] a) {
    new BubbleSort<T>().sort(a);
}
public static void demoBubble(Integer[] a, ColetorPassos<Integer> out, int maxPassos) {
    new BubbleSort<Integer>().demo(a, out, maxPassos);
}

// idem para insertion, selection, shell, heap, merge, quick
}

Importante: não coloque Counting/Radix/Bucket dentro da Ordenadores (eles têm assinaturas diferentes e usam primitivos). Continue chamando-os direto do Program/RunnerTerminal.

Ajustes de chamadas (se necessário)
Em qualquer lugar que antes referenciava:

sorting.api.Ordenadores → mude para sorting.Ordenadores

sorting.comparativos.XxxSort → mude para sorting.XxxSort

sorting.linear.YyySort → mude para sorting.YyySort

As demos com limite de passos (Shell/Heap) continuam com assinatura: public void demo(Integer[] a, bench.ColetorPassos out, int maxPassos)

E as chamadas no Program: Ordenadores.demoShell(demoArr, out, maxPassos); Ordenadores.demoHeap(demoArr, out, maxPassos);

Compilação e execução Na raiz (onde está src): mkdir out -ErrorAction SilentlyContinue Get-ChildItem -Recurse -Filter *.java -Path src | Where-Object { $_.Name -ne 'module-info.java' } | ForEach-Object FullName | Out-File -FilePath sources.txt -Encoding ascii
javac -encoding UTF-8 -d out "@sources.txt" java -cp out application.Program

Checklist rápido de erros comuns ao migrar
package ... errado no topo do arquivo → ajuste para application, bench ou sorting.

cannot find symbol Ordenadores → verifique import import sorting.Ordenadores; e o package correto em Ordenadores.java.

Lineares no catálogo comparativo → não colocar Counting/Radix/Bucket no catalogo() dos comparativos; eles entram no runner de lineares.

Lambdas com contador nas demos → use AtomicInteger ou int[] {0} para maxPassos.
