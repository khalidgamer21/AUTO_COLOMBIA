package edu.iudigital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RegistroDAO {

    public boolean tieneIngresoActivo(String placa) {
        String sql = "SELECT id FROM registro WHERE placa_vehiculo = ? AND salida IS NULL";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error al validar ingreso activo: " + e.getMessage());
            return false;
        }
    }

    public void registrarEntrada(String placa, int numeroCelda, LocalDateTime entrada) {
        String sql = "INSERT INTO registro (placa_vehiculo, numero_celda, entrada) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            ps.setInt(2, numeroCelda);
            ps.setTimestamp(3, Timestamp.valueOf(entrada));

            ps.executeUpdate();
            System.out.println("Entrada registrada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al registrar entrada: " + e.getMessage());
        }
    }

    public Integer obtenerCeldaVehiculoActivo(String placa) {
        String sql = "SELECT numero_celda FROM registro WHERE placa_vehiculo = ? AND salida IS NULL LIMIT 1";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("numero_celda");
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener celda activa: " + e.getMessage());
        }

        return null;
    }

    public void registrarSalida(String placa, LocalDateTime salida) {
        String sql = "UPDATE registro SET salida = ? WHERE placa_vehiculo = ? AND salida IS NULL";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(salida));
            ps.setString(2, placa);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Salida registrada correctamente");
            } else {
                System.out.println("No existe ingreso activo para ese vehículo");
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar salida: " + e.getMessage());
        }
    }

    public void verVehiculosEnParqueaderoConPagos() {
        String sql = """
                SELECT v.placa,
                       r.numero_celda,
                       p.valor,
                       p.fecha_pago,
                       p.estado
                FROM registro r
                JOIN vehiculo v ON r.placa_vehiculo = v.placa
                LEFT JOIN pago p ON p.id = (
                    SELECT id
                    FROM pago
                    WHERE placa_vehiculo = v.placa
                    ORDER BY fecha_pago DESC, id DESC
                    LIMIT 1
                )
                WHERE r.salida IS NULL
                """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=== VEHÍCULOS EN EL PARQUEADERO ===");
            boolean hayDatos = false;

            while (rs.next()) {
                hayDatos = true;

                System.out.println("Placa: " + rs.getString("placa")
                        + " | Celda: " + rs.getInt("numero_celda")
                        + " | Valor: " + rs.getDouble("valor")
                        + " | Fecha pago: " + rs.getDate("fecha_pago")
                        + " | Estado: " + rs.getString("estado"));
            }

            if (!hayDatos) {
                System.out.println("No hay vehículos dentro del parqueadero");
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar vehículos en parqueadero: " + e.getMessage());
        }
    }
}