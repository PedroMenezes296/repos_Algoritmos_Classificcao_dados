package Circuitos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Series extends Circuit {
    private final List<Circuit> elements = new ArrayList<>();

    public Series(List<Circuit> elements) {
        if (elements == null || elements.isEmpty())
            throw new IllegalArgumentException("Série precisa de pelo menos um elemento.");
        this.elements.addAll(elements);
    }

    public List<Circuit> getElements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public double getResistance() {
        double sum = 0.0;
        for (Circuit c : elements) sum += c.getResistance(); // Req = R1 + R2 + ... + RN
        return sum;
    }

    @Override
    public String toString() {
        return "Series" + elements;
    }
}
