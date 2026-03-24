package edu.iudigital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public void guardarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario(nombre, identificacion, telefono, correo) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getIdentificacion());
            ps.setString(3, usuario.getTelefono());
            ps.setString(4, usuario.getCorreo());

            ps.executeUpdate();
            System.out.println("Usuario registrado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }
    }

    public boolean existeUsuarioPorIdentificacion(String identificacion) {
        String sql = "SELECT id FROM usuario WHERE identificacion = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, identificacion);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
        }

        return false;
    }

    public void listarUsuarios() {
        String sql = "SELECT * FROM usuario";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=== LISTA DE USUARIOS ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        " | Nombre: " + rs.getString("nombre") +
                        " | Identificación: " + rs.getString("identificacion") +
                        " | Teléfono: " + rs.getString("telefono") +
                        " | Correo: " + rs.getString("correo"));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
    }

    public Usuario buscarUsuarioPorIdentificacion(String identificacion) {
        String sql = "SELECT * FROM usuario WHERE identificacion = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, identificacion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("identificacion"),
                            rs.getString("telefono"),
                            rs.getString("correo")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }

    public void actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre = ?, telefono = ?, correo = ? WHERE identificacion = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getTelefono());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getIdentificacion());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Usuario actualizado correctamente.");
            } else {
                System.out.println("No se encontró usuario con esa identificación.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    public void eliminarUsuario(String identificacion) {
        String sql = "DELETE FROM usuario WHERE identificacion = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, identificacion);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Usuario eliminado correctamente.");
            } else {
                System.out.println("No se encontró usuario con esa identificación.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
    }
}