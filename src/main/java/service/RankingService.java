package service;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RankingService {

    public List<Object[]> rankingSucursal(int idSucursal) {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
            SELECT u.nombre, COUNT(p.id_partida) AS partidas,
                   MAX(p.puntaje) AS mejor_puntaje,
                   COALESCE(AVG(p.puntaje), 0) AS promedio
            FROM partida p
            INNER JOIN usuario u ON p.id_usuario = u.id_usuario
            WHERE p.id_sucursal = ? AND p.estado = 'TERMINADA'
            GROUP BY u.id_usuario, u.nombre
            ORDER BY mejor_puntaje DESC
        """;
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            int posicion = 1;
            while (rs.next()) {
                lista.add(new Object[]{
                    posicion++,
                    rs.getString("nombre"),
                    rs.getInt("partidas"),
                    rs.getInt("mejor_puntaje"),
                    (int) rs.getDouble("promedio")
                });
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo ranking sucursal: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> rankingGlobal() {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
            SELECT u.nombre, s.nombre AS sucursal, COUNT(p.id_partida) AS partidas,
                   MAX(p.puntaje) AS mejor_puntaje,
                   COALESCE(AVG(p.puntaje), 0) AS promedio
            FROM partida p
            INNER JOIN usuario u ON p.id_usuario = u.id_usuario
            INNER JOIN sucursal s ON p.id_sucursal = s.id_sucursal
            WHERE p.estado = 'TERMINADA'
            GROUP BY u.id_usuario, u.nombre, s.nombre
            ORDER BY mejor_puntaje DESC
        """;
        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            int posicion = 1;
            while (rs.next()) {
                lista.add(new Object[]{
                    posicion++,
                    rs.getString("nombre"),
                    rs.getString("sucursal"),
                    rs.getInt("partidas"),
                    rs.getInt("mejor_puntaje"),
                    (int) rs.getDouble("promedio")
                });
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo ranking global: " + e.getMessage());
        }
        return lista;
    }
}