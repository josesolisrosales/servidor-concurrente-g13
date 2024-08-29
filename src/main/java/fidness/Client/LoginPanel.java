package fidness.Client;

import fidness.Client.Utility.Client;
import fidness.Objects.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginPanel extends JPanel {
    private App app;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel(App app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
    }

    private void initComponents() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(createInputPanel());
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(createButtonPanel());
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        };

        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Iniciar sesión");
        registerButton = new JButton("Registrarse");

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        panel.add(loginButton);
        panel.add(registerButton);
        return panel;
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor, ingrese usuario y contraseña.");
            return;
        }

        if (Client.getInstance().authenticateUser(username, password)) {
            User user = Client.getInstance().getUserByUsername(Client.getInstance().getCurrentUser().getUsername());
            Client.getInstance().setCurrentUser(user);
            app.loginSuccessful();
        } else {
            showError("Usuario no existe o contraseña incorrecta.");
        }
    }

    private void register() {
        JTextField usernameField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Usuario:"));
        panel.add(usernameField);
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Apellido:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirmar Contraseña:"));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Registrarse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String name = nameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError("Todos los campos son obligatorios.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Las contraseñas no coinciden.");
                return;
            }

            if (Client.getInstance().registerUser(username, name, lastName, password, email, false)) {
                showMessage("Usuario creado con éxito, ingrese con sus credenciales para continuar.");
            } else {
                showError("Error al crear usuario: Nombre de usuario ya existe.");
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}