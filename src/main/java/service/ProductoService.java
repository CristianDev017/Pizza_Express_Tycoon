
package service;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    public List<Object[]> listarProductos(int idSucursal) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, precio, estado FROM producto WHERE id_sucursal = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getBoolean("estado") ? "Activo" : "Inactivo"
                });
            }
        } catch (Exception e) {
            System.out.println("Error listando productos: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertarProducto(String nombre, double precio, int idSucursal) {
        String sql = "INSERT INTO producto (nombre, precio, id_sucursal, estado) VALUES (?, ?, ?, true)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, idSucursal);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertando producto: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarProducto(int id, String nombre, double precio) {
        String sql = "UPDATE producto SET nombre = ?, precio = ? WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizando producto: " + e.getMessage());
        }
        return false;
    }

    public boolean cambiarEstado(int id, boolean nuevoEstado) {
        String sql = "UPDATE producto SET estado = ? WHERE id_producto = ?";
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
}