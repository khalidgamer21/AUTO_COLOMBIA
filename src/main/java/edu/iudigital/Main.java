package edu.iudigital;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        VehiculoDAO vehiculoDAO = new VehiculoDAO();
        CeldaDAO celdaDAO = new CeldaDAO();
        RegistroDAO registroDAO = new RegistroDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        int opcion;

        do {
            System.out.println("\n=== PARQUEADERO AUTOS COLOMBIA ===");
            System.out.println("1. Registrar entrada del vehículo");
            System.out.println("2. Registrar salida del vehículo");
            System.out.println("3. Gestión de usuarios");
            System.out.println("4. Gestión de celdas");
            System.out.println("5. Salir");
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
                    } else {
                        registroDAO.registrarEntrada(placa, celdaLibre, LocalDateTime.now());
                        celdaDAO.ocuparCelda(celdaLibre);
                        System.out.println("Vehículo ingresado en la celda " + celdaLibre);
                    }
                    break;

                case 2:
                    System.out.print("Placa: ");
                    String placaSalida = sc.nextLine().toUpperCase();

                    Integer celdaOcupada = registroDAO.obtenerCeldaVehiculoActivo(placaSalida);

                    if (celdaOcupada == null) {
                        System.out.println("El vehículo no tiene ingreso activo");
                    } else {
                        registroDAO.registrarSalida(placaSalida, LocalDateTime.now());
                        celdaDAO.liberarCelda(celdaOcupada);
                        System.out.println("Salida registrada. Celda liberada: " + celdaOcupada);
                    }
                    break;

                case 3:
                    menuUsuarios(sc, usuarioDAO);
                    break;

                case 4:
                    menuCeldas(sc, celdaDAO);
                    break;

                case 5:
                    System.out.println("Programa finalizado");
                    break;

                default:
                    System.out.println("Opción inválida");
            }

        } while (opcion != 5);

        sc.close();
    }

    public static void menuUsuarios(Scanner sc, UsuarioDAO usuarioDAO) {
        int opcion;

        do {
            System.out.println("\n=== GESTIÓN DE USUARIOS ===");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Listar usuarios");
            System.out.println("3. Buscar usuario");
            System.out.println("4. Actualizar usuario");
            System.out.println("5. Eliminar usuario");
            System.out.println("6. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();

                    System.out.print("Identificación: ");
                    String identificacion = sc.nextLine();

                    if (usuarioDAO.existeUsuarioPorIdentificacion(identificacion)) {
                        System.out.println("Ya existe un usuario con esa identificación.");
                        break;
                    }

                    System.out.print("Teléfono: ");
                    String telefono = sc.nextLine();

                    System.out.print("Correo: ");
                    String correo = sc.nextLine();

                    Usuario usuario = new Usuario(nombre, identificacion, telefono, correo);
                    usuarioDAO.guardarUsuario(usuario);
                    break;

                case 2:
                    usuarioDAO.listarUsuarios();
                    break;

                case 3:
                    System.out.print("Ingrese la identificación del usuario: ");
                    String idBuscar = sc.nextLine();

                    Usuario encontrado = usuarioDAO.buscarUsuarioPorIdentificacion(idBuscar);

                    if (encontrado != null) {
                        System.out.println(encontrado);
                    } else {
                        System.out.println("Usuario no encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Identificación del usuario a actualizar: ");
                    String idActualizar = sc.nextLine();

                    Usuario usuarioActual = usuarioDAO.buscarUsuarioPorIdentificacion(idActualizar);

                    if (usuarioActual == null) {
                        System.out.println("Usuario no encontrado.");
                        break;
                    }

                    System.out.print("Nuevo nombre: ");
                    String nuevoNombre = sc.nextLine();

                    System.out.print("Nuevo teléfono: ");
                    String nuevoTelefono = sc.nextLine();

                    System.out.print("Nuevo correo: ");
                    String nuevoCorreo = sc.nextLine();

                    Usuario actualizado = new Usuario(nuevoNombre, idActualizar, nuevoTelefono, nuevoCorreo);
                    usuarioDAO.actualizarUsuario(actualizado);
                    break;

                case 5:
                    System.out.print("Identificación del usuario a eliminar: ");
                    String idEliminar = sc.nextLine();
                    usuarioDAO.eliminarUsuario(idEliminar);
                    break;

                case 6:
                    System.out.println("Volviendo al menú principal...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 6);
    }

    public static void menuCeldas(Scanner sc, CeldaDAO celdaDAO) {
        int opcion;

        do {
            System.out.println("\n=== GESTIÓN DE CELDAS ===");
            System.out.println("1. Registrar celda");
            System.out.println("2. Listar celdas");
            System.out.println("3. Actualizar estado de celda");
            System.out.println("4. Eliminar celda");
            System.out.println("5. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Número de celda: ");
                    int numero = sc.nextInt();
                    sc.nextLine();

                    if (celdaDAO.existeCelda(numero)) {
                        System.out.println("Ya existe una celda con ese número.");
                        break;
                    }

                    System.out.print("¿Está ocupada? (true/false): ");
                    boolean ocupada = sc.nextBoolean();
                    sc.nextLine();

                    Celda celda = new Celda(numero, ocupada);
                    celdaDAO.guardarCelda(celda);
                    break;

                case 2:
                    celdaDAO.listarCeldas();
                    break;

                case 3:
                    System.out.print("Número de celda: ");
                    int numActualizar = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nuevo estado (true = ocupada, false = libre): ");
                    boolean nuevoEstado = sc.nextBoolean();
                    sc.nextLine();

                    celdaDAO.actualizarEstadoCelda(numActualizar, nuevoEstado);
                    break;

                case 4:
                    System.out.print("Número de celda a eliminar: ");
                    int numEliminar = sc.nextInt();
                    sc.nextLine();

                    celdaDAO.eliminarCelda(numEliminar);
                    break;

                case 5:
                    System.out.println("Volviendo al menú principal...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 5);
    }
}