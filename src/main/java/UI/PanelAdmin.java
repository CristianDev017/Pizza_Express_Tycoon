package UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PanelAdmin extends JFrame {

    public PanelAdmin() {
        setTitle("Panel Administrador - Pizza Express Tycoon");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JLabel titulo = new JLabel("Panel Administrador", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(200, 70, 0));
        titulo.setBorder(new EmptyBorder(25, 0, 15, 0));
        mainPanel.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 15, 15));
        panelBotones.setBackground(new Color(240, 240, 240));
        panelBotones.setBorder(new EmptyBorder(20, 50, 30, 50));

        JButton btnUsuarios = crearBoton("Gestionar Usuarios");
        btnUsuarios.addActionListener(e -> new GestionUsuariosFrame().setVisible(true));

        JButton btnSucursales = crearBoton("Gestionar Sucursales");
        btnSucursales.addActionListener(e -> new GestionSucursalesFrame().setVisible(true));

        JButton btnNiveles = crearBoton("Configurar Niveles");
        btnNiveles.addActionListener(e -> new ConfigurarNivelesFrame().setVisible(true));

        JButton btnRanking = crearBoton("Ranking Global");
        btnRanking.addActionListener(e -> new RankingFrame(0, true).setVisible(true));

        JButton btnEstadisticas = crearBoton("Estadísticas Globales");
        btnEstadisticas.addActionListener(e -> new EstadisticasGlobalesFrame().setVisible(true));

        JButton btnReportes = crearBoton("Ver Reportes");
        btnReportes.addActionListener(e -> new ReporteGlobalFrame().setVisible(true));

        panelBotones.add(btnUsuarios);
        panelBotones.add(btnSucursales);
        panelBotones.add(btnNiveles);
        panelBotones.add(btnRanking);
        panelBotones.add(btnEstadisticas);
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