package bench;

/** Coleta snapshots (estados) de passo-a-passo para arrays de referência (ex.: Integer[]). */
@FunctionalInterface
public interface ColetorPassos<T> {
    void passo(T[] snapshot, String descricao);
}
