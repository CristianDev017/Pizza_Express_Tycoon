
package UI;

import service.EstadisticasService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class EstadisticasGlobalesFrame extends JFrame {

    public EstadisticasGlobalesFrame() {
        setTitle("Estadísticas Globales");
        setSize(500, 470);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(28, 28, 28));

        JLabel lblTitulo = new JLabel("Estadísticas Globales", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(220, 80, 20));
        lblTitulo.setBorder(new EmptyBorder(20, 0, 15, 0));
        lblTitulo.setOpaque(false);
        add(lblTitulo, BorderLayout.NORTH);

        EstadisticasService service = new EstadisticasService();
        Object[] datos = service.estadisticasGlobales();

        JPanel panelDatos = new JPanel(new GridLayout(6, 1, 0, 12));
        panelDatos.setBackground(new Color(28, 28, 28));
        panelDatos.setBorder(new EmptyBorder(10, 40, 30, 40));

        panelDatos.add(crearTarjeta("Total de partidas jugadas", String.valueOf(datos[0])));
        panelDatos.add(crearTarjeta("Puntaje promedio", String.valueOf(datos[1])));
        panelDatos.add(crearTarjeta("Mejor puntaje", String.valueOf(datos[2])));
        panelDatos.add(crearTarjeta("Nivel máximo alcanzado", String.valueOf(datos[3])));
        panelDatos.add(crearTarjeta("Jugador más activo", String.valueOf(datos[4])));
        panelDatos.add(crearTarjeta("Sucursal más activa", String.valueOf(datos[5])));

        add(panelDatos, BorderLayout.CENTER);
    }

    private JPanel crearTarjeta(String etiqueta, String valor) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(new Color(45, 45, 45));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 60), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEtiqueta.setForeground(new Color(160, 160, 160));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblValor.setForeground(new Color(220, 220, 220));

        tarjeta.add(lblEtiqueta, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        return tarjeta;
    }
}