package service;

import database.Conexion;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Reporteservice {

    public List<Object[]> obtenerReporteSucursal(int idSucursal) {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
            SELECT 
                u.nombre AS jugador,
                p.puntaje,
                p.nivel_alcanzado,
                (SELECT COUNT(*) FROM pedido pe WHERE pe.id_partida = p.id_partida AND pe.estado = 'ENTREGADA') AS completados,
                (SELECT COUNT(*) FROM pedido pe WHERE pe.id_partida = p.id_partida AND pe.estado = 'CANCELADA') AS cancelados,
                (SELECT COUNT(*) FROM pedido pe WHERE pe.id_partida = p.id_partida AND pe.estado = 'NO_ENTREGADO') AS no_entregados,
                DATE(p.fecha) AS fecha
            FROM partida p
            INNER JOIN usuario u ON p.id_usuario = u.id_usuario
            WHERE p.id_sucursal = ? AND p.estado = 'TERMINADA'
            ORDER BY p.fecha DESC
        """;
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("jugador"),
                    rs.getInt("puntaje"),
                    rs.getInt("nivel_alcanzado"),
                    rs.getInt("completados"),
                    rs.getInt("cancelados"),
                    rs.getInt("no_entregados"),
                    rs.getString("fecha")
                });
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo reporte: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> obtenerReporteGlobal() {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
            SELECT 
                u.nombre AS jugador,
                s.nombre AS sucursal,
                p.puntaje,
                p.nivel_alcanzado,
                (SELECT COUNT(*) FROM pedido pe WHERE pe.id_partida = p.id_partida AND pe.estado = 'ENTREGADA') AS completados,
                (SELECT COUNT(*) FROM pedido pe WHERE pe.id_partida = p.id_partida AND pe.estado = 'CANCELADA') AS cancelados,
                (SELECT COUNT(*) FROM pedido pe WHERE pe.id_partida = p.id_partida AND pe.estado = 'NO_ENTREGADO') AS no_entregados,
                DATE(p.fecha) AS fecha
            FROM partida p
            INNER JOIN usuario u ON p.id_usuario = u.id_usuario
            INNER JOIN sucursal s ON p.id_sucursal = s.id_sucursal
            WHERE p.estado = 'TERMINADA'
            ORDER BY p.fecha DESC
        """;
        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("jugador"),
                    rs.getString("sucursal"),
                    rs.getInt("puntaje"),
                    rs.getInt("nivel_alcanzado"),
                    rs.getInt("completados"),
                    rs.getInt("cancelados"),
                    rs.getInt("no_entregados"),
                    rs.getString("fecha")
                });
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo reporte global: " + e.getMessage());
        }
        return lista;
    }

    public boolean exportarCSV(List<Object[]> datos, String ruta) {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write("Jugador,Puntaje,Nivel Alcanzado,Pedidos Completados,Cancelados,No Entregados,Fecha\n");
            for (Object[] fila : datos) {
                fw.write(fila[0] + "," + fila[1] + "," + fila[2] + "," + fila[3] + "," + fila[4] + "," + fila[5] + "," + fila[6] + "\n");
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error exportando CSV: " + e.getMessage());
        }
        return false;
    }

    public boolean exportarCSVGlobal(List<Object[]> datos, String ruta) {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write("Jugador,Sucursal,Puntaje,Nivel Alcanzado,Pedidos Completados,Cancelados,No Entregados,Fecha\n");
            for (Object[] fila : datos) {
                fw.write(fila[0] + "," + fila[1] + "," + fila[2] + "," + fila[3] + "," + fila[4] + "," + fila[5] + "," + fila[6] + "," + fila[7] + "\n");
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error exportando CSV global: " + e.getMessage());
        }
        return false;
    }
}