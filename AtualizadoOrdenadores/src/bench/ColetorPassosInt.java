package bench;

/** Coleta snapshots para arrays primitivos de int. */
@FunctionalInterface
public interface ColetorPassosInt {
    void passo(int[] snapshot, String descricao);
}
