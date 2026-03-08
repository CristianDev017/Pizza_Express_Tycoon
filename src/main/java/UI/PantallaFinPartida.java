package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PantallaFinPartida extends JFrame {

    public PantallaFinPartida(int puntajeFinal, int nivelAlcanzado, int pedidosCompletados, int pedidosCancelados, int pedidosNoEntregados, int idUsuario, int idSucursal, String nombreUsuario) {
        setTitle("Fin de Partida");
        setSize(450, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(28, 28, 28));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(200, 70, 0));
        panelTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel lblTitulo = new JLabel("PARTIDA TERMINADA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelStats = new JPanel(new GridLayout(5, 1, 0, 12));
        panelStats.setBackground(new Color(28, 28, 28));
        panelStats.setBorder(new EmptyBorder(25, 40, 20, 40));

        panelStats.add(crearTarjeta("Puntaje Final", String.valueOf(puntajeFinal), new Color(255, 200, 50)));
        panelStats.add(crearTarjeta("Nivel Alcanzado", String.valueOf(nivelAlcanzado), new Color(100, 210, 120)));
        panelStats.add(crearTarjeta("Pedidos Completados", String.valueOf(pedidosCompletados), new Color(100, 210, 120)));
        panelStats.add(crearTarjeta("Pedidos Cancelados", String.valueOf(pedidosCancelados), new Color(220, 130, 50)));
        panelStats.add(crearTarjeta("No Entregados", String.valueOf(pedidosNoEntregados), new Color(220, 80, 80)));

        add(panelStats, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setBackground(new Color(28, 28, 28));
        panelBotones.setBorder(new EmptyBorder(10, 40, 30, 40));

        JButton btnJugarOtraVez = new JButton("Jugar de nuevo");
        btnJugarOtraVez.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnJugarOtraVez.setBackground(new Color(200, 70, 0));
        btnJugarOtraVez.setForeground(Color.WHITE);
        btnJugarOtraVez.setFocusPainted(false);
        btnJugarOtraVez.setBorderPainted(false);
        btnJugarOtraVez.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnJugarOtraVez.addActionListener(e -> {
            this.dispose();
            new PantallaJuego(idUsuario, idSucursal, nombreUsuario).setVisible(true);
        });

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSalir.setBackground(new Color(60, 60, 60));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        panelBotones.add(btnJugarOtraVez);
        panelBotones.add(btnSalir);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearTarjeta(String etiqueta, String valor, Color colorValor) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(new Color(45, 45, 45));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 60), 1, true),
            new EmptyBorder(8, 15, 8, 15)
        ));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEtiqueta.setForeground(new Color(160, 160, 160));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblValor.setForeground(colorValor);

        tarjeta.add(lblEtiqueta, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        return tarjeta;
    }
}