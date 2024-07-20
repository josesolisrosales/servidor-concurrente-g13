package fidness.UI;

import fidness.User;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private App app;
    private User user;
    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JButton saveButton;

    public ProfilePanel(App app) {
        this.app = app;
        this.user = app.getCurrentUser();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre de usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(new JLabel(user.getUsername()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        nameField = new JTextField(user.getName(), 20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Apellido:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        lastNameField = new JTextField(user.getLastName(), 20);
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        emailField = new JTextField(user.getEmail(), 20);
        formPanel.add(emailField, gbc);

        saveButton = new JButton("Guardar cambios");
        saveButton.addActionListener(e -> saveChanges());

        add(formPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void saveChanges() {
        String newName = nameField.getText();
        String newLastName = lastNameField.getText();
        String newEmail = emailField.getText();

        try {
            app.getUserManager().updateUser(user, newName, newLastName, newEmail);
            JOptionPane.showMessageDialog(this, "Perfil actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}