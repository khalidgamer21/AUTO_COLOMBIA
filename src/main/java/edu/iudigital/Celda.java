package edu.iudigital;

public class Celda {
    private int numero;
    private boolean ocupada;

    public Celda() {
    }

    public Celda(int numero, boolean ocupada) {
        this.numero = numero;
        this.ocupada = ocupada;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    @Override
    public String toString() {
        return "Celda{" +
                "numero=" + numero +
                ", ocupada=" + ocupada +
                '}';
    }
}