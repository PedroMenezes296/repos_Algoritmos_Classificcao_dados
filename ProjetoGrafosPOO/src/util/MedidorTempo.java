package util;

public class MedidorTempo {
    public static long agoraNanos() { return System.nanoTime(); }
    public static double nanosParaMillis(long nanos) { return nanos / 1_000_000.0; }
}
