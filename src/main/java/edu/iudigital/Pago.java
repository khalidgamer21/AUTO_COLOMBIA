package edu.iudigital;

import java.time.LocalDate;

public class Pago {

    private String placaVehiculo;
    private double valor;
    private LocalDate fechaPago;
    private String estado;

    public Pago(String placaVehiculo, double valor, LocalDate fechaPago, String estado) {
        this.placaVehiculo = placaVehiculo;
        this.valor = valor;
        this.fechaPago = fechaPago;
        this.estado = estado;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public String getEstado() {
        return estado;
    }
}