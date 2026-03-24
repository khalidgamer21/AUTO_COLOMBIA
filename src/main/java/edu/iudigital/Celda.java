package edu.iudigital;

public class Celda {
    private int numero;
    private String ocupada;

    public Celda() {
    }

    public Celda(int numero, String ocupada) {
        this.numero = numero;
        this.ocupada = ocupada;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getOcupada() {
        return ocupada;
    }

    public void setOcupada(String ocupada) {
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