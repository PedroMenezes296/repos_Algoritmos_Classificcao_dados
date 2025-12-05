package Circuitos;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // EXEMPLOS DO ENUNCIADO ------------------------------

        // Série com 300, 500 e 1200 Ω → 2000 Ω
        Circuit serieEx = new Series(List.of(
                new Resistor(300),
                new Resistor(500),
                new Resistor(1200)
        ));
        System.out.println("Série (300, 500, 1200) Ω  → Req = "
                + Circuit.fmt(serieEx.getResistance()) + " Ω");

        // Paralelo com 50, 100 e 300 Ω → 30 Ω
        Circuit paraleloEx = new Parallel(List.of(
                new Resistor(50),
                new Resistor(100),
                new Resistor(300)
        ));
        System.out.println("Paralelo (50, 100, 300) Ω → Req = "
                + Circuit.fmt(paraleloEx.getResistance()) + " Ω");

        // EXEMPLOS ADICIONAIS (aninhamento) ------------------

        // (100) + (paralelo de 200 || 300) + (50) → 100 + 120 + 50 = 270 Ω
        Circuit aninhado1 = new Series(List.of(
                new Resistor(100),
                new Parallel(List.of(new Resistor(200), new Resistor(300))),
                new Resistor(50)
        ));
        System.out.println("Série(paralelo dentro)      → Req = "
                + Circuit.fmt(aninhado1.getResistance()) + " Ω");

        // Paralelo de: [ Série(10 + 20) = 30 Ω ] || [ Resistor(30) ] → 15 Ω
        Circuit aninhado2 = new Parallel(List.of(
                new Series(List.of(new Resistor(10), new Resistor(20))),
                new Resistor(30)
        ));
        System.out.println("Paralelo(série dentro)      → Req = "
                + Circuit.fmt(aninhado2.getResistance()) + " Ω");
    }
}

