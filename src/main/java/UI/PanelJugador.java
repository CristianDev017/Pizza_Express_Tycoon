package UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PanelJugador extends JFrame {

    private int idUsuario;
    private int idSucursal;
    private String nombreUsuario;
    private String nombreSucursal;

    public PanelJugador(int idUsuario, int idSucursal, String nombreUsuario, String nombreSucursal) {
        this.idUsuario = idUsuario;
        this.idSucursal = idSucursal;
        this.nombreUsuario = nombreUsuario;
        this.nombreSucursal = nombreSucursal;

        setTitle("Pizza Express Tycoon - Jugador");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(new EmptyBorder(30, 50, 10, 50));

        JLabel lblTitulo = new JLabel("Pizza Express Tycoon", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(200, 70, 0));

        JLabel lblJugador = new JLabel("Jugador: " + nombreUsuario, SwingConstants.CENTER);
        lblJugador.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblSucursal = new JLabel("Sucursal: " + nombreSucursal, SwingConstants.CENTER);
        lblSucursal.setFont(new Font("Arial", Font.PLAIN, 16));

        infoPanel.add(lblTitulo);
        infoPanel.add(lblJugador);
        infoPanel.add(lblSucursal);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(240, 240, 240));
        panelBoton.setBorder(new EmptyBorder(20, 50, 30, 50));

        JButton btnIniciar = new JButton("Iniciar Partida");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 18));
        btnIniciar.setBackground(new Color(200, 70, 0));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setPreferredSize(new Dimension(200, 60));
        btnIniciar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(150, 50, 0), 2),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIniciar.addActionListener(e -> iniciarPartida());

        panelBoton.add(btnIniciar);
        mainPanel.add(panelBoton, BorderLayout.SOUTH);
    }

    private void iniciarPartida() {
        new PantallaJuego(idUsuario, idSucursal, nombreUsuario).setVisible(true);
        this.dispose();
    }
}