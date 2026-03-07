
package UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PanelAdminTienda extends JFrame {

    private int idSucursal;
    private String nombreSucursal;

    public PanelAdminTienda(int idSucursal, String nombreSucursal) {
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;

        setTitle("Panel Administrador - " + nombreSucursal);
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JLabel titulo = new JLabel("Panel Administrador - " + nombreSucursal, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(200, 70, 0));
        titulo.setBorder(new EmptyBorder(25, 0, 15, 0));
        mainPanel.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 15, 15));
        panelBotones.setBackground(new Color(240, 240, 240));
        panelBotones.setBorder(new EmptyBorder(20, 50, 30, 50));

        JButton btnProductos = crearBoton("Gestionar Productos");
        btnProductos.addActionListener(e -> new GestionProductosFrame(idSucursal).setVisible(true));

        JButton btnEstadisticas = crearBoton("Estadísticas");
        btnEstadisticas.addActionListener(e -> new EstadisticasFrame(idSucursal, nombreSucursal).setVisible(true));             

        JButton btnRanking = crearBoton("Ranking Jugadores");
        btnRanking.addActionListener(e -> new RankingFrame(idSucursal, false).setVisible(true));

        JButton btnReportes = crearBoton("Exportar Reportes");
        btnReportes.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Módulo en desarrollo"));

        panelBotones.add(btnProductos);
        panelBotones.add(btnEstadisticas);
        panelBotones.add(btnRanking);
        panelBotones.add(btnReportes);

        mainPanel.add(panelBotones, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(200, 70, 0));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 70, 0), 2),
            new EmptyBorder(25, 10, 25, 10)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}