package UI;

import service.Reporteservice;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReporteGlobalFrame extends JFrame {

    private List<Object[]> datos;

    public ReporteGlobalFrame() {
        setTitle("Reportes Globales");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Reporte Global de Partidas", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(200, 70, 0));
        lblTitulo.setBorder(new EmptyBorder(20, 25, 15, 25));
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"Jugador", "Sucursal", "Puntaje", "Nivel Alcanzado", "Pedidos Completados", "Cancelados", "No Entregados", "Fecha"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(200, 70, 0));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new EmptyBorder(0, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBorder(new EmptyBorder(5, 20, 15, 20));

        JButton btnExportar = new JButton("Exportar CSV");
        btnExportar.setFont(new Font("Arial", Font.BOLD, 13));
        btnExportar.setBackground(new Color(200, 70, 0));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportar.addActionListener(e -> exportarCSV());

        panelBoton.add(btnExportar);
        add(panelBoton, BorderLayout.SOUTH);

        Reporteservice service = new Reporteservice();
        datos = service.obtenerReporteGlobal();
        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
    }

    private void exportarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte CSV");
        fileChooser.setSelectedFile(new java.io.File("reporte_global.csv"));

        int resultado = fileChooser.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            if (!ruta.endsWith(".csv")) ruta += ".csv";

            Reporteservice service = new Reporteservice();
            boolean exito = service.exportarCSVGlobal(datos, ruta);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Reporte exportado correctamente en:\n" + ruta);
            } else {
                JOptionPane.showMessageDialog(this, "Error al exportar el reporte");
            }
        }
    }
}