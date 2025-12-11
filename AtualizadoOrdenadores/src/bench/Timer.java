package bench;

public final class Timer {
    private Timer() { }

    /** Mede e retorna o tempo em nanos do bloco fornecido. */
    public static long time(Runnable block) {
        long t0 = System.nanoTime();
        block.run();
        long t1 = System.nanoTime();
        return t1 - t0;
    }
}
