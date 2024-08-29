package fidness.Client;

import fidness.Client.Utility.Client;
import fidness.Objects.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePanel extends JPanel {
    private App app;
    private JTextField usernameField;
    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton saveButton;
    private JButton changePasswordButton;

    public ProfilePanel(App app) {
        this.app = app;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        User currentUser = Client.getInstance().getCurrentUser();

        // Username (no editable)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre de usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(currentUser.getUsername());
        usernameField.setEditable(false);
        formPanel.add(usernameField, gbc);

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        nameField = new JTextField(currentUser.getName());
        formPanel.add(nameField, gbc);

        // Apellido
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Apellido:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        lastNameField = new JTextField(currentUser.getLastName());
        formPanel.add(lastNameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        emailField = new JTextField(currentUser.getEmail());
        formPanel.add(emailField, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("Guardar cambios");
        changePasswordButton = new JButton("Cambiar contraseña");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChangePasswordDialog();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(changePasswordButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveChanges() {
        String newName = nameField.getText().trim();
        String newLastName = lastNameField.getText().trim();
        String newEmail = emailField.getText().trim();

        if (newName.isEmpty() || newLastName.isEmpty() || newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = Client.getInstance().updateUser(
                Client.getInstance().getCurrentUser().getUsername(),
                newName,
                newLastName,
                newEmail
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Perfil actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // Actualizar el usuario actual en el cliente
            User updatedUser = Client.getInstance().getUserByUsername(Client.getInstance().getCurrentUser().getUsername());
            if (updatedUser != null) {
                Client.getInstance().setCurrentUser(updatedUser);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el perfil", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showChangePasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        currentPasswordField = new JPasswordField(10);
        newPasswordField = new JPasswordField(10);
        confirmPasswordField = new JPasswordField(10);

        panel.add(new JLabel("Contraseña actual:"));
        panel.add(currentPasswordField);
        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirmar nueva contraseña:"));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Cambiar contraseña",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            changePassword();
        }
    }

    private void changePassword() {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Las nuevas contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = Client.getInstance().changeUserPassword(
                Client.getInstance().getCurrentUser().getUsername(),
                currentPassword,
                newPassword
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Contraseña cambiada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar la contraseña. Verifique su contraseña actual.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}