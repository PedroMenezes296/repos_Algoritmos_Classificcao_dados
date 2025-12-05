package Circuitos;

//Contrato comum para QUALQUER elemento de circuito.
public abstract class Circuit {
 // Toda subclasse deve saber calcular sua resistência equivalente (Ω).
 public abstract double getResistance();

 // Útil para imprimir com 2 casas.
 protected static String fmt(double x) {
     return String.format("%.2f", x);
 }
}

