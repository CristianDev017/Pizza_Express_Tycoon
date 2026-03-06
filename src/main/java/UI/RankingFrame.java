
package UI;

import service.RankingService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RankingFrame extends JFrame {

    private int idSucursal;
    private boolean esGlobal;

    public RankingFrame(int idSucursal, boolean esGlobal) {
        this.idSucursal = idSucursal;
        this.esGlobal = esGlobal;

        String titulo = esGlobal ? "Ranking Global" : "Ranking de Sucursal";
        setTitle(titulo);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(28, 28, 28));

        // Título
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(220, 80, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        lblTitulo.setOpaque(false);
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla
        DefaultTableModel modelo;
        if (esGlobal) {
            String[] columnas = {"#", "Jugador", "Sucursal", "Mejor Puntaje", "Nivel Máx.", "Partidas"};
            modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
        } else {
            String[] columnas = {"#", "Jugador", "Mejor Puntaje", "Nivel Máx.", "Partidas"};
            modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
        }

        JTable tabla = new JTable(modelo);
        tabla.setBackground(new Color(40, 40, 40));
        tabla.setForeground(new Color(220, 220, 220));
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(28);
        tabla.getTableHeader().setBackground(new Color(220, 80, 20));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabla.setSelectionBackground(new Color(220, 80, 20));
        tabla.setGridColor(new Color(60, 60, 60));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scroll.getViewport().setBackground(new Color(40, 40, 40));
        add(scroll, BorderLayout.CENTER);

        // Cargar datos
        RankingService service = new RankingService();
        List<Object[]> datos = esGlobal ? service.rankingGlobal() : service.rankingSucursal(idSucursal);
        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
    }
}