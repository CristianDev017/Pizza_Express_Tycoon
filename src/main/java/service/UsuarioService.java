package service;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    public boolean validarLogin(String username, String password) {
        String sql = "SELECT * FROM usuario WHERE username = ? AND password = ? AND estado = true";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
        }
        return false;
    }

    public String obtenerRol(String username, String password) {
        String sql = """
            SELECT r.nombre 
            FROM usuario u
            INNER JOIN rol r ON u.id_rol = r.id_rol
            WHERE u.username = ? AND u.password = ? AND u.estado = true
        """;
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
        }
        return null;
    }

    public Object[] obtenerDatosSucursal(String username, String password) {
        String sql = """
            SELECT s.id_sucursal, s.nombre
            FROM usuario u
            INNER JOIN sucursal s ON u.id_sucursal = s.id_sucursal
            WHERE u.username = ? AND u.password = ? AND u.estado = true
        """;
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Object[]{
                    rs.getInt("id_sucursal"),
                    rs.getString("nombre")
                };
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo sucursal: " + e.getMessage());
        }
        return null;
    }

    public List<Object[]> listarUsuarios() {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
            SELECT u.id_usuario, u.username, r.nombre AS rol, s.nombre AS sucursal, u.estado
            FROM usuario u
            INNER JOIN rol r ON u.id_rol = r.id_rol
            INNER JOIN sucursal s ON u.id_sucursal = s.id_sucursal
        """;
        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_usuario"),
                    rs.getString("username"),
                    rs.getString("rol"),
                    rs.getString("sucursal"),
                    rs.getBoolean("estado") ? "Activo" : "Inactivo"
                });
            }
        } catch (Exception e) {
            System.out.println("Error listando usuarios: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> listarSucursales() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_sucursal, nombre FROM sucursal WHERE estado = true";
        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_sucursal"),
                    rs.getString("nombre")
                });
            }
        } catch (Exception e) {
            System.out.println("Error listando sucursales: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> listarRoles() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_rol, nombre FROM rol";
        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_rol"),
                    rs.getString("nombre")
                });
            }
        } catch (Exception e) {
            System.out.println("Error listando roles: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertarUsuario(String nombre, String username, String password, int idRol, int idSucursal) {
        String sql = "INSERT INTO usuario (nombre, username, password, id_rol, id_sucursal, estado) VALUES (?, ?, ?, ?, ?, true)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setInt(4, idRol);
            ps.setInt(5, idSucursal);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertando usuario: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarUsuario(int id, String username, String password, int idSucursal) {
        String sql = "UPDATE usuario SET username = ?, password = ?, id_sucursal = ? WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setInt(3, idSucursal);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizando usuario: " + e.getMessage());
        }
        return false;
    }

    public boolean cambiarEstado(int id, boolean nuevoEstado) {
        String sql = "UPDATE usuario SET estado = ? WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error cambiando estado: " + e.getMessage());
        }
        return false;
    }
    
    public Object[] obtenerDatosJugador(String username, String password) {
    String sql = """
        SELECT u.id_usuario, u.id_sucursal, u.nombre, s.nombre AS sucursal
        FROM usuario u
        INNER JOIN sucursal s ON u.id_sucursal = s.id_sucursal
        WHERE u.username = ? AND u.password = ? AND u.estado = true
    """;
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Object[]{
                rs.getInt("id_usuario"),
                rs.getInt("id_sucursal"),
                rs.getString("nombre"),
                rs.getString("sucursal")
            };
        }
    } catch (Exception e) {
        System.out.println("Error obteniendo datos jugador: " + e.getMessage());
    }
    return null;
}
}