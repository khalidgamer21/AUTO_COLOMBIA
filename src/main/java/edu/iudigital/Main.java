package edu.iudigital;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        VehiculoDAO vehiculoDAO = new VehiculoDAO();
        CeldaDAO celdaDAO = new CeldaDAO();
        RegistroDAO registroDAO = new RegistroDAO();
        PagoDAO pagoDAO = new PagoDAO();


        int opcion;

        do {
            System.out.println("\n=== PARQUEADERO AUTOS COLOMBIA ===");
            System.out.println("1. Registrar ingreso del vehículo");
            System.out.println("2. Registrar salida del vehículo");
            System.out.println("3. Ver vehículos en parqueadero con pagos");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {

                case 1:
                    System.out.print("Placa: ");
                    String placa = sc.nextLine().toUpperCase();

                    if (!vehiculoDAO.existeVehiculo(placa)) {
                        System.out.print("Tipo: ");
                        String tipo = sc.nextLine();

                        System.out.print("Marca: ");
                        String marca = sc.nextLine();

                        System.out.print("Color: ");
                        String color = sc.nextLine();

                        Vehiculo vehiculo = new Vehiculo(placa, tipo, marca, color);
                        vehiculoDAO.guardarVehiculo(vehiculo);
                    }

                    if (registroDAO.tieneIngresoActivo(placa)) {
                        System.out.println("El vehículo ya está dentro del parqueadero");
                        break;
                    }

                    Integer celdaLibre = celdaDAO.buscarCeldaLibre();

                    if (celdaLibre == null) {
                        System.out.println("No hay celdas disponibles");
                        break;
                    }

                    System.out.print("Valor del pago: ");
                    double valor = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Estado del pago (PAGADO/PENDIENTE): ");
                    String estadoPago = sc.nextLine().toUpperCase();

                    Pago pago = new Pago(placa, valor, LocalDate.now(), estadoPago);
                    pagoDAO.registrarPago(pago);

                    registroDAO.registrarEntrada(placa, celdaLibre, LocalDateTime.now());
                    celdaDAO.ocuparCelda(celdaLibre);

                    System.out.println("Ingreso registrado. Vehículo en celda " + celdaLibre);
                    break;

                case 2:
                    System.out.print("Placa: ");
                    String placaSalida = sc.nextLine().toUpperCase();

                    Integer celdaOcupada = registroDAO.obtenerCeldaVehiculoActivo(placaSalida);

                    if (celdaOcupada == null) {
                        System.out.println("El vehículo no tiene ingreso activo");
                        break;
                    }

                    String estado = pagoDAO.obtenerUltimoEstadoPago(placaSalida);

                    if (estado == null || estado.equals("PENDIENTE")) {
                        System.out.println("El vehículo tiene pago pendiente");

                        System.out.print("Ingrese valor a pagar: ");
                        double valorPago = sc.nextDouble();
                        sc.nextLine();

                        Pago nuevoPago = new Pago(placaSalida, valorPago, LocalDate.now(), "PAGADO");
                        pagoDAO.registrarPago(nuevoPago);

                        System.out.println("Pago realizado correctamente");
                    }

                    registroDAO.registrarSalida(placaSalida, LocalDateTime.now());
                    celdaDAO.liberarCelda(celdaOcupada);

                    System.out.println("Salida registrada. Celda liberada: " + celdaOcupada);
                    break;

                case 3:
                    registroDAO.verVehiculosEnParqueaderoConPagos();
                    break;

                case 4:
                    System.out.println("Programa finalizado");
                    break;

                default:
                    System.out.println("Opción inválida");
            }

        } while (opcion != 4);

        sc.close();
    }
}