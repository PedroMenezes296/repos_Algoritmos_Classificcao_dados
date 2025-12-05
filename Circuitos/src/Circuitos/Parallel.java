package Circuitos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parallel extends Circuit {
    private final List<Circuit> elements = new ArrayList<>();

    public Parallel(List<Circuit> elements) {
        if (elements == null || elements.isEmpty())
            throw new IllegalArgumentException("Paralelo precisa de pelo menos um elemento.");
        this.elements.addAll(elements);
    }

    public List<Circuit> getElements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public double getResistance() {
        double invSum = 0.0;
        for (Circuit c : elements) {
            double r = c.getResistance();
            if (r == 0.0) return 0.0;      // curto em qualquer ramo → Req = 0
            invSum += 1.0 / r;              // 1/Req = Σ (1/Ri)
        }
        return 1.0 / invSum;
    }

    @Override
    public String toString() {
        return "Parallel" + elements;
    }
}
