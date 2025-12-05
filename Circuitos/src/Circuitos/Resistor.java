package Circuitos;

public class Resistor extends Circuit {
    private final double resistance; // em ohms

    public Resistor(double resistance) {
        if (resistance < 0) throw new IllegalArgumentException("Resistência negativa.");
        this.resistance = resistance;
    }

    @Override
    public double getResistance() {
        return resistance;
    }

    @Override
    public String toString() {
        return fmt(resistance) + "Ω";
    }
}

