package service;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NivelService {

    public List<Object[]> listarNiveles() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_nivel, numero_nivel, tiempo_base_segundos, pedidos_para_subir FROM nivel ORDER BY numero_nivel";
        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_nivel"),
                    rs.getInt("numero_nivel"),
                    rs.getInt("tiempo_base_segundos"),
                    rs.getInt("pedidos_para_subir")
                });
            }
        } catch (Exception e) {
            System.out.println("Error listando niveles: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarNivel(int id, int tiempoBase, int pedidosParaSubir) {
        String sql = "UPDATE nivel SET tiempo_base_segundos = ?, pedidos_para_subir = ? WHERE id_nivel = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tiempoBase);
            ps.setInt(2, pedidosParaSubir);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizando nivel: " + e.getMessage());
        }
        return false;
    }
}