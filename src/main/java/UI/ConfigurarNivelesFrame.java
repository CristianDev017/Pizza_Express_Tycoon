
package UI;

import service.NivelService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConfigurarNivelesFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    public ConfigurarNivelesFrame() {
        setTitle("Configurar Niveles");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Configurar Niveles", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nivel", "Tiempo Base (seg)", "Pedidos para Subir"};
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

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarNivel());

        panelBotones.add(btnEditar);
        add(panelBotones, BorderLayout.SOUTH);

        cargarNiveles();
    }

    private void cargarNiveles() {
        NivelService service = new NivelService();
        List<Object[]> niveles = service.listarNiveles();
        modelo.setRowCount(0);
        for (Object[] fila : niveles) {
            modelo.addRow(fila);
        }
    }

    private void editarNivel() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un nivel primero");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String tiempoActual = String.valueOf(modelo.getValueAt(fila, 2));
        String pedidosActual = String.valueOf(modelo.getValueAt(fila, 3));

        String nuevoTiempo = JOptionPane.showInputDialog(this, "Tiempo base en segundos:", tiempoActual);
        if (nuevoTiempo == null || nuevoTiempo.trim().isEmpty()) return;

        String nuevosPedidos = JOptionPane.showInputDialog(this, "Pedidos para subir de nivel:", pedidosActual);
        if (nuevosPedidos == null || nuevosPedidos.trim().isEmpty()) return;

        try {
            int tiempo = Integer.parseInt(nuevoTiempo);
            int pedidos = Integer.parseInt(nuevosPedidos);

            NivelService service = new NivelService();
            boolean exito = service.actualizarNivel(id, tiempo, pedidos);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Nivel actualizado correctamente");
                cargarNiveles();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar nivel");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los valores deben ser números enteros");
        }
    }
}