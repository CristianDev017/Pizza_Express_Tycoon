package UI;

import service.PartidaService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PantallaJuego extends JFrame {

    private int idUsuario;
    private int idSucursal;
    private String nombreUsuario;
    private int idPartida;

    private int puntaje = 0;
    private int nivel = 1;
    private int pedidosCompletados = 0;
    private static final int MAX_PEDIDOS_ACTIVOS = 5;

    private PartidaService partidaService = new PartidaService();
    private List<PedidoPanel> pedidosActivos = new ArrayList<>();
    private Timer timerGenerador;

    private JLabel lblPuntaje;
    private JLabel lblNivel;
    private JPanel panelPedidos;

    public PantallaJuego(int idUsuario, int idSucursal, String nombreUsuario) {
        this.idUsuario = idUsuario;
        this.idSucursal = idSucursal;
        this.nombreUsuario = nombreUsuario;

        setTitle("Pizza Express Tycoon - En Juego");
        setSize(960, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(28, 28, 28));

        idPartida = partidaService.crearPartida(idUsuario, idSucursal);

        construirUI();
        iniciarGeneradorPedidos();
    }

    private void construirUI() {
        // Barra superior
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBackground(new Color(20, 20, 20));
        panelInfo.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("PIZZA EXPRESS TYCOON");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(new Color(220, 80, 20));

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelCentro.setBackground(new Color(20, 20, 20));

        JLabel lblJugador = new JLabel("Jugador: " + nombreUsuario);
        lblJugador.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblJugador.setForeground(new Color(200, 200, 200));

        lblPuntaje = new JLabel("Puntaje: 0");
        lblPuntaje.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPuntaje.setForeground(new Color(255, 200, 50));

        lblNivel = new JLabel("Nivel: 1");
        lblNivel.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNivel.setForeground(new Color(100, 210, 120));

        panelCentro.add(lblJugador);
        panelCentro.add(lblPuntaje);
        panelCentro.add(lblNivel);

        JButton btnTerminar = new JButton("Terminar");
        btnTerminar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnTerminar.setBackground(new Color(180, 40, 20));
        btnTerminar.setForeground(Color.WHITE);
        btnTerminar.setFocusPainted(false);
        btnTerminar.setBorderPainted(false);
        btnTerminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTerminar.addActionListener(e -> terminarPartida());

        panelInfo.add(lblTitulo, BorderLayout.WEST);
        panelInfo.add(panelCentro, BorderLayout.CENTER);
        panelInfo.add(btnTerminar, BorderLayout.EAST);
        add(panelInfo, BorderLayout.NORTH);

        // Área de pedidos
        panelPedidos = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 14));
        panelPedidos.setBackground(new Color(35, 35, 35));

        JScrollPane scroll = new JScrollPane(panelPedidos);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(35, 35, 35));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);

        // Barra inferior con instrucciones
        JLabel lblInstrucciones = new JLabel("  Presiona Avanzar para preparar cada pedido. Entrega antes de que el tiempo expire.", SwingConstants.LEFT);
        lblInstrucciones.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblInstrucciones.setForeground(new Color(130, 130, 130));
        lblInstrucciones.setBorder(new EmptyBorder(6, 10, 6, 10));
        lblInstrucciones.setBackground(new Color(20, 20, 20));
        lblInstrucciones.setOpaque(true);
        add(lblInstrucciones, BorderLayout.SOUTH);
    }

    private void iniciarGeneradorPedidos() {
        timerGenerador = new Timer(8000, e -> generarPedido());
        timerGenerador.start();
        generarPedido();
    }

    private void generarPedido() {
        if (pedidosActivos.size() >= MAX_PEDIDOS_ACTIVOS) return;

        List<Object[]> productos = partidaService.obtenerProductosActivos(idSucursal);
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos activos en esta sucursal.");
            return;
        }

        Object[] producto = productos.get(new Random().nextInt(productos.size()));
        int idProducto = (int) producto[0];
        String nombreProducto = (String) producto[1];

        int tiempoLimite = partidaService.obtenerTiempoNivel(nivel);
        int idPedido = partidaService.crearPedido(idPartida, idUsuario, tiempoLimite);
        partidaService.agregarDetallePedido(idPedido, idProducto, 1);
        partidaService.avanzarEstadoPedido(idPedido, "RECIBIDA");

        PedidoPanel panel = new PedidoPanel(idPedido, nombreProducto, tiempoLimite, this);
        pedidosActivos.add(panel);
        panelPedidos.add(panel);
        panelPedidos.revalidate();
        panelPedidos.repaint();
    }

    public void pedidoEntregado(PedidoPanel panel, boolean eficiente) {
        puntaje += 100;
        if (eficiente) puntaje += 50;
        pedidosCompletados++;
        eliminarPedido(panel);
        verificarSubidaNivel();
        actualizarUI();
        partidaService.actualizarPartida(idPartida, puntaje, nivel);
    }

    public void pedidoCancelado(PedidoPanel panel) {
        puntaje -= 30;
        eliminarPedido(panel);
        actualizarUI();
        partidaService.actualizarPartida(idPartida, puntaje, nivel);
    }

    public void pedidoNoEntregado(PedidoPanel panel) {
        puntaje -= 50;
        eliminarPedido(panel);
        actualizarUI();
        partidaService.actualizarPartida(idPartida, puntaje, nivel);
    }

    private void eliminarPedido(PedidoPanel panel) {
        pedidosActivos.remove(panel);
        panelPedidos.remove(panel);
        panelPedidos.revalidate();
        panelPedidos.repaint();
    }

    private void verificarSubidaNivel() {
        if (nivel >= 3) return;
        int pedidosParaSubir = partidaService.obtenerPedidosParaSubir(nivel);
        if (pedidosCompletados >= pedidosParaSubir) {
            nivel++;
            pedidosCompletados = 0;
            JOptionPane.showMessageDialog(this, "Subiste al nivel " + nivel + "!");
            actualizarUI();
        }
    }

    private void actualizarUI() {
        lblPuntaje.setText("Puntaje: " + puntaje);
        lblNivel.setText("Nivel: " + nivel);
    }

    private void terminarPartida() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas terminar la partida?", "Terminar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        timerGenerador.stop();
        for (PedidoPanel p : new ArrayList<>(pedidosActivos)) {
            p.detener();
        }

        partidaService.terminarPartida(idPartida, puntaje, nivel);
        JOptionPane.showMessageDialog(this, "Partida terminada.\nPuntaje final: " + puntaje + "\nNivel alcanzado: " + nivel);
        this.dispose();
        new LoginFrame().setVisible(true);
    }
}
