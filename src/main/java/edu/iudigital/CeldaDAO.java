package edu.iudigital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CeldaDAO {

    public Integer buscarCeldaLibre() {
        String sql = "SELECT numero FROM celda WHERE ocupada = false LIMIT 1";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("numero");
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar celda libre: " + e.getMessage());
        }

        return null;
    }

    public void ocuparCelda(int numero) {
        String sql = "UPDATE celda SET ocupada = true WHERE numero = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numero);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al ocupar celda: " + e.getMessage());
        }
    }

    public void liberarCelda(int numero) {
        String sql = "UPDATE celda SET ocupada = false WHERE numero = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numero);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al liberar celda: " + e.getMessage());
        }
    }

    public void guardarCelda(Celda celda) {
        String sql = "INSERT INTO celda(numero, ocupada) VALUES (?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, celda.getNumero());
            ps.setString(2, celda.getOcupada().toString());

            ps.executeUpdate();
            System.out.println("Celda registrada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al guardar celda: " + e.getMessage());
        }
    }

    public boolean existeCelda(int numero) {
        String sql = "SELECT numero FROM celda WHERE numero = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numero);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al validar celda: " + e.getMessage());
        }

        return false;
    }

    public void listarCeldas() {
        String sql = "SELECT * FROM celda ORDER BY numero";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=== LISTA DE CELDAS ===");
            while (rs.next()) {
                System.out.println("Celda: " + rs.getInt("numero") +
                        " | Estado: " + (rs.getBoolean("ocupada") ? "Ocupada" : "Libre"));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar celdas: " + e.getMessage());
        }
    }

    public void actualizarEstadoCelda(int numero, boolean ocupada) {
        String sql = "UPDATE celda SET ocupada = ? WHERE numero = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, ocupada);
            ps.setInt(2, numero);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Estado de la celda actualizado correctamente.");
            } else {
                System.out.println("No existe una celda con ese número.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de celda: " + e.getMessage());
        }
    }

    public void eliminarCelda(int numero) {
        String sql = "DELETE FROM celda WHERE numero = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numero);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Celda eliminada correctamente.");
            } else {
                System.out.println("No existe una celda con ese número.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar celda: " + e.getMessage());
        }
    }
}