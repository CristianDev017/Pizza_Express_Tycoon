
package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Statement;
import java.sql.ResultSet;
import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class GestionSucursalesFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

public GestionSucursalesFrame() {

    setTitle("Gestión de Sucursales");
    setSize(800, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    JLabel titulo = new JLabel("Gestión de Sucursales", SwingConstants.CENTER);
    titulo.setFont(new Font("Arial", Font.BOLD, 22));
    titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

    add(titulo, BorderLayout.NORTH);

    String[] columnas = {"ID", "Nombre", "Dirección", "Estado"};
    modelo = new DefaultTableModel(columnas, 0);
    tabla = new JTable(modelo);

    add(new JScrollPane(tabla), BorderLayout.CENTER);

    JPanel panelBotones = new JPanel();

    JButton btnAgregar = new JButton("Agregar");
    btnAgregar.addActionListener(e -> agregarSucursal());
    JButton btnEditar = new JButton("Editar");
    btnEditar.addActionListener(e -> editarSucursal());
    JButton btnDesactivar = new JButton("Cambiar estado");  
    btnDesactivar.addActionListener(e -> desactivarSucursal());

    panelBotones.add(btnAgregar);
    panelBotones.add(btnEditar);
    panelBotones.add(btnDesactivar);

    add(panelBotones, BorderLayout.SOUTH);

    cargarSucursales();
}
    private void cargarSucursales() {

    modelo.setRowCount(0); 

    String sql = "SELECT * FROM sucursal";

    try (Connection con = Conexion.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {

            Object[] fila = {
                rs.getInt("id_sucursal"),
                rs.getString("nombre"),
                rs.getString("direccion"),
                rs.getBoolean("estado") ? "Activa" : "Inactiva"
            };

            modelo.addRow(fila);
        }

    } catch (Exception e) {
        System.out.println("Error cargando sucursales: " + e.getMessage());
    }
}
    private void agregarSucursal() {

    String nombre = JOptionPane.showInputDialog(this, "Ingrese nombre de la sucursal:");
    String direccion = JOptionPane.showInputDialog(this, "Ingrese dirección:");

    if (nombre == null || direccion == null) return;

    String sql = "INSERT INTO sucursal (nombre, direccion, estado) VALUES (?, ?, true)";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setString(2, direccion);

        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Sucursal agregada correctamente");

        cargarSucursales();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private void editarSucursal() {

    // Obtener la fila seleccionada
    int fila = tabla.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una sucursal para editar");
        return;
    }

    // Obtener los datos actuales
    int id = (int) modelo.getValueAt(fila, 0);
    String nombreActual = (String) modelo.getValueAt(fila, 1);
    String direccionActual = (String) modelo.getValueAt(fila, 2);

    // Pedir nuevos valores
    String nuevoNombre = JOptionPane.showInputDialog(this, "Editar nombre:", nombreActual);
    String nuevaDireccion = JOptionPane.showInputDialog(this, "Editar direccion:", direccionActual);

    if (nuevoNombre == null || nuevaDireccion == null) {
        return; 
    }

    String sql = "UPDATE sucursal SET nombre = ?, direccion = ? WHERE id_sucursal = ?";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nuevoNombre);
        ps.setString(2, nuevaDireccion);
        ps.setInt(3, id);

        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Sucursal actualizada correctamente");

        // Recargar tabla
        cargarSucursales();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
private void desactivarSucursal() {

    // Obtener fila seleccionada
    int fila = tabla.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una sucursal");
        return;
    }

    int id = (int) modelo.getValueAt(fila, 0);
    String estadoActual = (String) modelo.getValueAt(fila, 3);

    // Determinar nuevo estado
    boolean nuevoEstado;

    if (estadoActual.equalsIgnoreCase("Activa")) {
        nuevoEstado = false;
    } else {
        nuevoEstado = true;
    }

    String sql = "UPDATE sucursal SET estado = ? WHERE id_sucursal = ?";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setBoolean(1, nuevoEstado);
        ps.setInt(2, id);

        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Estado actualizado correctamente");

        cargarSucursales();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
}