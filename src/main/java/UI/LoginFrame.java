package UI;

import service.UsuarioService;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Pizza Express Tycoon - Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Pizza Express Tycoon");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(200, 70, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(lblTitle, gbc);

        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        loginPanel.add(txtUsuario, gbc);

        JLabel lblPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        loginPanel.add(txtPassword, gbc);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(230, 120, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 5, 8);
        loginPanel.add(btnLogin, gbc);

        mainPanel.add(loginPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> login());
        txtUsuario.addActionListener(e -> txtPassword.requestFocus());
        txtPassword.addActionListener(e -> login());
    }

    private void login() {
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        UsuarioService service = new UsuarioService();
        String rol = service.obtenerRol(usuario, password);

        if (rol == null) {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            return;
        }

        JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario);
        this.dispose();

        switch (rol) {
            case "Super Admin" -> new PanelAdmin().setVisible(true);
            case "Administrador" -> {
                Object[] datos = service.obtenerDatosSucursal(usuario, password);
                int idSucursal = (int) datos[0];
                String nombreSucursal = (String) datos[1];
                new PanelAdminTienda(idSucursal, nombreSucursal).setVisible(true);
            }
           case "Jugador" -> {
                Object[] datos = service.obtenerDatosJugador(usuario, password);
                int idUsuario = (int) datos[0];
                int idSucursal = (int) datos[1];
                String nombre = (String) datos[2];
                String sucursal = (String) datos[3];
                new PanelJugador(idUsuario, idSucursal, nombre, sucursal).setVisible(true);
            }
            default -> JOptionPane.showMessageDialog(null, "Rol no reconocido");
        }
    }
}