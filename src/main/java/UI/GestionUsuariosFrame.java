package UI;

import service.UsuarioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionUsuariosFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    public GestionUsuariosFrame() {
        setTitle("Gestión de Usuarios");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Username", "Rol", "Sucursal", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarUsuario());

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarUsuario());

        JButton btnCambiarEstado = new JButton("Cambiar Estado");
        btnCambiarEstado.addActionListener(e -> cambiarEstadoUsuario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnCambiarEstado);
        add(panelBotones, BorderLayout.SOUTH);

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        UsuarioService service = new UsuarioService();
        List<Object[]> usuarios = service.listarUsuarios();
        modelo.setRowCount(0);
        for (Object[] fila : usuarios) {
            modelo.addRow(fila);
        }
    }

    private void agregarUsuario() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese nombre completo:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        String username = JOptionPane.showInputDialog(this, "Ingrese username:");
        if (username == null || username.trim().isEmpty()) return;

        String password = JOptionPane.showInputDialog(this, "Ingrese password:");
        if (password == null || password.trim().isEmpty()) return;

        UsuarioService service = new UsuarioService();

        List<Object[]> roles = service.listarRoles();
        if (roles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay roles disponibles");
            return;
        }
        String[] nombresRoles = roles.stream()
            .map(r -> r[0] + " - " + r[1])
            .toArray(String[]::new);

        String rolElegido = (String) JOptionPane.showInputDialog(
            this, "Seleccione el rol:", "Rol",
            JOptionPane.QUESTION_MESSAGE, null, nombresRoles, nombresRoles[0]
        );
        if (rolElegido == null) return;
        int idRol = Integer.parseInt(rolElegido.split(" - ")[0].trim());

        List<Object[]> sucursales = service.listarSucursales();
        if (sucursales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay sucursales activas");
            return;
        }
        String[] nombresSucursales = sucursales.stream()
            .map(s -> s[0] + " - " + s[1])
            .toArray(String[]::new);

        String sucursalElegida = (String) JOptionPane.showInputDialog(
            this, "Seleccione la sucursal:", "Sucursal",
            JOptionPane.QUESTION_MESSAGE, null, nombresSucursales, nombresSucursales[0]
        );
        if (sucursalElegida == null) return;
        int idSucursal = Integer.parseInt(sucursalElegida.split(" - ")[0].trim());

        boolean exito = service.insertarUsuario(nombre, username, password, idRol, idSucursal);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Usuario agregado correctamente");
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar usuario");
        }
    }

    private void editarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario primero");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String usernameActual = (String) modelo.getValueAt(fila, 1);

        String nuevoUsername = JOptionPane.showInputDialog(this, "Nuevo username:", usernameActual);
        if (nuevoUsername == null || nuevoUsername.trim().isEmpty()) return;

        String nuevaPassword = JOptionPane.showInputDialog(this, "Nueva password:");
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) return;

        UsuarioService service = new UsuarioService();

        List<Object[]> sucursales = service.listarSucursales();
        if (sucursales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay sucursales activas");
            return;
        }
        String[] nombresSucursales = sucursales.stream()
            .map(s -> s[0] + " - " + s[1])
            .toArray(String[]::new);

        String sucursalElegida = (String) JOptionPane.showInputDialog(
            this, "Seleccione la nueva sucursal:", "Sucursal",
            JOptionPane.QUESTION_MESSAGE, null, nombresSucursales, nombresSucursales[0]
        );
        if (sucursalElegida == null) return;
        int idSucursal = Integer.parseInt(sucursalElegida.split(" - ")[0].trim());

        boolean exito = service.actualizarUsuario(id, nuevoUsername, nuevaPassword, idSucursal);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente");
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar usuario");
        }
    }

    private void cambiarEstadoUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario primero");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String estadoActual = (String) modelo.getValueAt(fila, 4);
        boolean nuevoEstado = estadoActual.equalsIgnoreCase("Inactivo");

        UsuarioService service = new UsuarioService();
        boolean exito = service.cambiarEstado(id, nuevoEstado);
        if (exito) {
            String msg = nuevoEstado ? "Usuario activado correctamente" : "Usuario desactivado correctamente";
            JOptionPane.showMessageDialog(this, msg);
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar estado");
        }
    }
}