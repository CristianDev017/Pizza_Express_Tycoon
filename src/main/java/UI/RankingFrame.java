
package UI;

import service.RankingService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RankingFrame extends JFrame {

    public RankingFrame(int idSucursal, boolean esGlobal) {
        String titulo = esGlobal ? "Ranking Global" : "Ranking de Jugadores";
        setTitle(titulo);
        setSize(780, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(28, 28, 28));

        // Título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTitulo.setBackground(new Color(28, 28, 28));
        panelTitulo.setBorder(new EmptyBorder(20, 25, 5, 25));

        JLabel lblTrofeo = new JLabel("🏆");
        lblTrofeo.setFont(new Font("SansSerif", Font.PLAIN, 22));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        panelTitulo.add(lblTrofeo);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Columnas según tipo de ranking
        String[] columnas = esGlobal
            ? new String[]{"Posición", "Jugador", "Sucursal", "Partidas Jugadas", "Mejor Puntaje", "Promedio"}
            : new String[]{"Posición", "Jugador", "Partidas Jugadas", "Mejor Puntaje", "Promedio"};

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabla = new JTable(modelo);
        tabla.setBackground(new Color(35, 35, 35));
        tabla.setForeground(new Color(220, 220, 220));
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(32);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new Color(50, 50, 50));
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setSelectionBackground(new Color(220, 80, 20, 80));
        tabla.setSelectionForeground(Color.WHITE);

        // Header
        tabla.getTableHeader().setBackground(new Color(20, 20, 20));
        tabla.getTableHeader().setForeground(new Color(160, 160, 160));
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tabla.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 80, 20)));

        // Centrar columna posición y resaltarla
        DefaultTableCellRenderer renderPosicion = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (col == 0) {
                    int pos = (int) value;
                    if (pos == 1) setForeground(new Color(255, 200, 50));
                    else if (pos == 2) setForeground(new Color(180, 180, 180));
                    else if (pos == 3) setForeground(new Color(180, 120, 60));
                    else setForeground(new Color(180, 180, 180));
                    setFont(new Font("SansSerif", Font.BOLD, 14));
                } else {
                    setForeground(new Color(220, 220, 220));
                    setFont(new Font("SansSerif", Font.PLAIN, 13));
                }
                setBackground(isSelected ? new Color(220, 80, 20, 80) : (row % 2 == 0 ? new Color(35, 35, 35) : new Color(42, 42, 42)));
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return this;
            }
        };

        for (int i = 0; i < columnas.length; i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderPosicion);
        }
        tabla.getColumnModel().getColumn(0).setPreferredWidth(70);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));
        scroll.getViewport().setBackground(new Color(35, 35, 35));
        scroll.setBackground(new Color(28, 28, 28));
        add(scroll, BorderLayout.CENTER);

        // Cargar datos
        RankingService service = new RankingService();
        List<Object[]> datos = esGlobal ? service.rankingGlobal() : service.rankingSucursal(idSucursal);
        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
    }
}