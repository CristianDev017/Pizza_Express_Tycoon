package UI;

import service.PartidaService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PedidoPanel extends JPanel {

    private int idPedido;
    private String nombreProducto;
    private int tiempoTotal;
    private int tiempoRestante;
    private String estadoActual = "RECIBIDA";

    private PantallaJuego juego;
    private PartidaService service = new PartidaService();

    private JLabel lblEstado;
    private JLabel lblTiempo;
    private Timer temporizador;

    private static final String[] ESTADOS = {"RECIBIDA", "PREPARANDO", "EN_HORNO", "ENTREGADA"};

    private static final Color COLOR_FONDO = new Color(45, 45, 45);
    private static final Color COLOR_BORDE = new Color(220, 80, 20);
    private static final Color COLOR_TEXTO = new Color(230, 230, 230);
    private static final Color COLOR_ACENTO = new Color(220, 80, 20);

    public PedidoPanel(int idPedido, String nombreProducto, int tiempoLimite, PantallaJuego juego) {
        this.idPedido = idPedido;
        this.nombreProducto = nombreProducto;
        this.tiempoTotal = tiempoLimite;
        this.tiempoRestante = tiempoLimite;
        this.juego = juego;

        setPreferredSize(new Dimension(170, 230));
        setBackground(COLOR_FONDO);
        setBorder(new LineBorder(COLOR_BORDE, 2, true));
        setLayout(new BorderLayout(0, 0));

        // Panel de información
        JPanel panelInfo = new JPanel(new GridLayout(4, 1, 2, 2));
        panelInfo.setBackground(COLOR_FONDO);
        panelInfo.setBorder(new EmptyBorder(10, 10, 6, 10));

        JLabel lblId = new JLabel("Pedido #" + idPedido, SwingConstants.CENTER);
        lblId.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblId.setForeground(COLOR_ACENTO);

        JLabel lblProducto = new JLabel(nombreProducto, SwingConstants.CENTER);
        lblProducto.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblProducto.setForeground(COLOR_TEXTO);

        lblEstado = new JLabel(estadoActual, SwingConstants.CENTER);
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblEstado.setForeground(new Color(100, 210, 120));

        lblTiempo = new JLabel("⏱ " + tiempoRestante + "s", SwingConstants.CENTER);
        lblTiempo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTiempo.setForeground(COLOR_TEXTO);

        panelInfo.add(lblId);
        panelInfo.add(lblProducto);
        panelInfo.add(lblEstado);
        panelInfo.add(lblTiempo);
        add(panelInfo, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 4, 0));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(0, 8, 10, 8));

        JButton btnAvanzar = new JButton("Avanzar");
        btnAvanzar.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnAvanzar.setBackground(COLOR_ACENTO);
        btnAvanzar.setForeground(Color.WHITE);
        btnAvanzar.setFocusPainted(false);
        btnAvanzar.setBorderPainted(false);
        btnAvanzar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAvanzar.addActionListener(e -> avanzarEstado());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnCancelar.setBackground(new Color(70, 70, 70));
        btnCancelar.setForeground(new Color(200, 200, 200));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> cancelar());

        panelBotones.add(btnAvanzar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        iniciarTemporizador();
    }

    private void iniciarTemporizador() {
        temporizador = new Timer(1000, e -> {
            tiempoRestante--;
            lblTiempo.setText("⏱ " + tiempoRestante + "s");

            if (tiempoRestante <= 10) {
                lblTiempo.setForeground(new Color(230, 60, 60));
            }

            if (tiempoRestante <= 0) {
                temporizador.stop();
                service.avanzarEstadoPedido(idPedido, "NO_ENTREGADO");
                JOptionPane.showMessageDialog(null, "Pedido #" + idPedido + " no entregado a tiempo. -50 puntos");
                juego.pedidoNoEntregado(this);
            }
        });
        temporizador.start();
    }

    private void avanzarEstado() {
        if (estadoActual.equals("ENTREGADA") ||
            estadoActual.equals("CANCELADA") ||
            estadoActual.equals("NO_ENTREGADO")) return;

        int indiceActual = 0;
        for (int i = 0; i < ESTADOS.length; i++) {
            if (ESTADOS[i].equals(estadoActual)) {
                indiceActual = i;
                break;
            }
        }

        String nuevoEstado = ESTADOS[indiceActual + 1];
        estadoActual = nuevoEstado;
        service.avanzarEstadoPedido(idPedido, nuevoEstado);
        lblEstado.setText(nuevoEstado);

        if (nuevoEstado.equals("ENTREGADA")) {
            temporizador.stop();
            boolean eficiente = tiempoRestante >= (tiempoTotal / 2);
            juego.pedidoEntregado(this, eficiente);
        }
    }

    private void cancelar() {
        if (!estadoActual.equals("RECIBIDA") && !estadoActual.equals("PREPARANDO")) {
            JOptionPane.showMessageDialog(null, "Solo puedes cancelar en RECIBIDA o PREPARANDO");
            return;
        }
        temporizador.stop();
        service.avanzarEstadoPedido(idPedido, "CANCELADA");
        JOptionPane.showMessageDialog(null, "Pedido #" + idPedido + " cancelado. -30 puntos");
        juego.pedidoCancelado(this);
    }

    public void detener() {
        if (temporizador != null) temporizador.stop();
    }
}