package service;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PartidaService {


    public int crearPartida(int idUsuario, int idSucursal) {
        String sql = "INSERT INTO partida (id_usuario, id_sucursal, puntaje, nivel_alcanzado, estado) VALUES (?, ?, 0, 1, 'EN_CURSO')";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idSucursal);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error creando partida: " + e.getMessage());
        }
        return -1;
    }


    public void actualizarPartida(int idPartida, int puntaje, int nivel) {
        String sql = "UPDATE partida SET puntaje = ?, nivel_alcanzado = ? WHERE id_partida = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, puntaje);
            ps.setInt(2, nivel);
            ps.setInt(3, idPartida);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error actualizando partida: " + e.getMessage());
        }
    }


    public void terminarPartida(int idPartida, int puntajeFinal, int nivelFinal) {
        String sql = "UPDATE partida SET estado = 'TERMINADA', puntaje = ?, nivel_alcanzado = ? WHERE id_partida = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, puntajeFinal);
            ps.setInt(2, nivelFinal);
            ps.setInt(3, idPartida);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error terminando partida: " + e.getMessage());
        }
    }


    public int obtenerTiempoNivel(int numeroNivel) {
        String sql = "SELECT tiempo_base_segundos FROM nivel WHERE numero_nivel = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroNivel);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("tiempo_base_segundos");
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo tiempo nivel: " + e.getMessage());
        }
        return 60;
    }


    public int obtenerPedidosParaSubir(int numeroNivel) {
        String sql = "SELECT pedidos_para_subir FROM nivel WHERE numero_nivel = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroNivel);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("pedidos_para_subir");
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo pedidos para subir: " + e.getMessage());
        }
        return 5;
    }


    public int crearPedido(int idPartida, int idUsuario, int tiempoLimite) {
        String sql = "INSERT INTO pedido (id_partida, id_usuario, tiempo_limite, estado) VALUES (?, ?, ?, 'RECIBIDA')";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idPartida);
            ps.setInt(2, idUsuario);
            ps.setInt(3, tiempoLimite);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error creando pedido: " + e.getMessage());
        }
        return -1;
    }


    public void avanzarEstadoPedido(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
            registrarHistorial(idPedido, nuevoEstado);
        } catch (Exception e) {
            System.out.println("Error avanzando estado: " + e.getMessage());
        }
    }


    private void registrarHistorial(int idPedido, String estado) {
        String sql = "INSERT INTO historial_estado (id_pedido, estado) VALUES (?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.setString(2, estado);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error registrando historial: " + e.getMessage());
        }
    }


    public List<Object[]> obtenerProductosActivos(int idSucursal) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre FROM producto WHERE id_sucursal = ? AND estado = true";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_producto"),
                    rs.getString("nombre")
                });
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo productos: " + e.getMessage());
        }
        return lista;
    }


    public void agregarDetallePedido(int idPedido, int idProducto, int cantidad) {
        String sql = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error agregando detalle: " + e.getMessage());
        }
    }
}