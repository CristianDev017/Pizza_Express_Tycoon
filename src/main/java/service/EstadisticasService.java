package service;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class EstadisticasService {

    public Object[] estadisticasSucursal(int idSucursal) {
        String sql = """
            SELECT 
                COUNT(p.id_partida) AS total_partidas,
                COALESCE(AVG(p.puntaje), 0) AS promedio_puntaje,
                COALESCE(MAX(p.puntaje), 0) AS mejor_puntaje,
                COALESCE(MAX(p.nivel_alcanzado), 0) AS nivel_max,
                (SELECT u.nombre FROM partida p2 
                 INNER JOIN usuario u ON p2.id_usuario = u.id_usuario
                 WHERE p2.id_sucursal = ? AND p2.estado = 'TERMINADA'
                 GROUP BY u.id_usuario, u.nombre
                 ORDER BY COUNT(p2.id_partida) DESC
                 LIMIT 1) AS jugador_activo
            FROM partida p
            WHERE p.id_sucursal = ? AND p.estado = 'TERMINADA'
        """;
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ps.setInt(2, idSucursal);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Object[]{
                    rs.getInt("total_partidas"),
                    (int) rs.getDouble("promedio_puntaje"),
                    rs.getInt("mejor_puntaje"),
                    rs.getInt("nivel_max"),
                    rs.getString("jugador_activo") != null ? rs.getString("jugador_activo") : "N/A"
                };
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo estadisticas: " + e.getMessage());
        }
        return new Object[]{0, 0, 0, 0, "N/A"};
    }

    public Object[] estadisticasGlobales() {
        String sql = """
            SELECT 
                COUNT(p.id_partida) AS total_partidas,
                COALESCE(AVG(p.puntaje), 0) AS promedio_puntaje,
                COALESCE(MAX(p.puntaje), 0) AS mejor_puntaje,
                COALESCE(MAX(p.nivel_alcanzado), 0) AS nivel_max,
                (SELECT u.nombre FROM partida p2
                 INNER JOIN usuario u ON p2.id_usuario = u.id_usuario
                 WHERE p2.estado = 'TERMINADA'
                 GROUP BY u.id_usuario, u.nombre
                 ORDER BY COUNT(p2.id_partida) DESC
                 LIMIT 1) AS jugador_activo,
                (SELECT s.nombre FROM partida p3
                 INNER JOIN sucursal s ON p3.id_sucursal = s.id_sucursal
                 WHERE p3.estado = 'TERMINADA'
                 GROUP BY s.id_sucursal, s.nombre
                 ORDER BY COUNT(p3.id_partida) DESC
                 LIMIT 1) AS sucursal_activa
            FROM partida p
            WHERE p.estado = 'TERMINADA'
        """;
        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return new Object[]{
                    rs.getInt("total_partidas"),
                    (int) rs.getDouble("promedio_puntaje"),
                    rs.getInt("mejor_puntaje"),
                    rs.getInt("nivel_max"),
                    rs.getString("jugador_activo") != null ? rs.getString("jugador_activo") : "N/A",
                    rs.getString("sucursal_activa") != null ? rs.getString("sucursal_activa") : "N/A"
                };
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo estadisticas globales: " + e.getMessage());
        }
        return new Object[]{0, 0, 0, 0, "N/A", "N/A"};
    }
}