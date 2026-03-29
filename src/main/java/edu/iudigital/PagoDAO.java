package edu.iudigital;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PagoDAO {

    public void registrarPago(Pago pago) {
        String sql = "INSERT INTO pago (placa_vehiculo, valor, fecha_pago, estado) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pago.getPlacaVehiculo());
            ps.setDouble(2, pago.getValor());
            ps.setDate(3, Date.valueOf(pago.getFechaPago()));
            ps.setString(4, pago.getEstado());

            ps.executeUpdate();
            System.out.println("Pago registrado correctamente");

        } catch (SQLException e) {
            System.out.println("Error al registrar pago: " + e.getMessage());
        }
    }

    public String obtenerUltimoEstadoPago(String placa) {
        String sql = """
                SELECT estado
                FROM pago
                WHERE placa_vehiculo = ?
                ORDER BY fecha_pago DESC, id DESC
                LIMIT 1
                """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("estado");
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar estado de pago: " + e.getMessage());
        }

        return null;
    }
}