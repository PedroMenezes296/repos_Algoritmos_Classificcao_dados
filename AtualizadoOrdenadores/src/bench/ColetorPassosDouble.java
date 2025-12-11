package bench;

/** Coleta snapshots para arrays primitivos de double. */
@FunctionalInterface
public interface ColetorPassosDouble {
    void passo(double[] snapshot, String descricao);
}
