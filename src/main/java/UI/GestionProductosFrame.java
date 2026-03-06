package UI;

import service.ProductoService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionProductosFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private int idSucursal;

    public GestionProductosFrame(int idSucursal) {
        this.idSucursal = idSucursal;

        setTitle("Gestión de Productos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestión de Productos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Precio", "Estado"};
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
        btnAgregar.addActionListener(e -> agregarProducto());

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarProducto());

        JButton btnEstado = new JButton("Cambiar Estado");
        btnEstado.addActionListener(e -> cambiarEstado());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEstado);
        add(panelBotones, BorderLayout.SOUTH);

        cargarProductos();
    }

    private void cargarProductos() {
        ProductoService service = new ProductoService();
        List<Object[]> productos = service.listarProductos(idSucursal);
        modelo.setRowCount(0);
        for (Object[] fila : productos) {
            modelo.addRow(fila);
        }
    }

    private void agregarProducto() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese nombre del producto:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        String precioStr = JOptionPane.showInputDialog(this, "Ingrese precio:");
        if (precioStr == null || precioStr.trim().isEmpty()) return;

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido");
            return;
        }

        ProductoService service = new ProductoService();
        boolean exito = service.insertarProducto(nombre, precio, idSucursal);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Producto agregado correctamente");
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar producto");
        }
    }

    private void editarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String nombreActual = (String) modelo.getValueAt(fila, 1);
        String precioActual = String.valueOf(modelo.getValueAt(fila, 2));

        String nuevoNombre = JOptionPane.showInputDialog(this, "Editar nombre:", nombreActual);
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

        String nuevoPrecioStr = JOptionPane.showInputDialog(this, "Editar precio:", precioActual);
        if (nuevoPrecioStr == null || nuevoPrecioStr.trim().isEmpty()) return;

        double nuevoPrecio;
        try {
            nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido");
            return;
        }

        ProductoService service = new ProductoService();
        boolean exito = service.actualizarProducto(id, nuevoNombre, nuevoPrecio);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente");
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar producto");
        }
    }

    private void cambiarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String estadoActual = (String) modelo.getValueAt(fila, 3);
        boolean nuevoEstado = estadoActual.equalsIgnoreCase("Inactivo");

        ProductoService service = new ProductoService();
        boolean exito = service.cambiarEstado(id, nuevoEstado);

        if (exito) {
            String msg = nuevoEstado ? "Producto activado correctamente" : "Producto desactivado correctamente";
            JOptionPane.showMessageDialog(this, msg);
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar estado");
        }
    }
}