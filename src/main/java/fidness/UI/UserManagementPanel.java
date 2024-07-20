package fidness.UI;

import fidness.User;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private App app;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton createUserButton;
    private JButton editUserButton;
    private JButton deleteUserButton;

    public UserManagementPanel(App app) {
        this.app = app;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        createTable();
        createButtons();
        refreshUserList();
    }

    private void createTable() {
        tableModel = new DefaultTableModel(new String[]{"Usuario", "Nombre", "Apellido", "Email", "Admin"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createUserButton = new JButton("Crear Usuario");
        editUserButton = new JButton("Editar Usuario");
        deleteUserButton = new JButton("Eliminar Usuario");

        createUserButton.addActionListener(e -> showCreateUserDialog());
        editUserButton.addActionListener(e -> showEditUserDialog());
        deleteUserButton.addActionListener(e -> deleteSelectedUser());

        buttonPanel.add(createUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshUserList() {
        tableModel.setRowCount(0);
        List<User> users = app.getUserManager().getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.isAdmin()
            });
        }
    }

    private void showCreateUserDialog() {
        JTextField usernameField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JCheckBox isAdminCheckbox = new JCheckBox("Admin");

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
        panel.add(isAdminCheckbox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Crear Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                app.getUserManager().createUser(
                    usernameField.getText(),
                    nameField.getText(),
                    lastNameField.getText(),
                    new String(passwordField.getPassword()),
                    emailField.getText(),
                    isAdminCheckbox.isSelected()
                );
                refreshUserList();
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = (String) tableModel.getValueAt(selectedRow, 0);
        User user = app.getUserManager().getUserByUsername(username);

        JTextField nameField = new JTextField(user.getName(), 15);
        JTextField lastNameField = new JTextField(user.getLastName(), 15);
        JTextField emailField = new JTextField(user.getEmail(), 15);
        JCheckBox isAdminCheckbox = new JCheckBox("Admin", user.isAdmin());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Apellido:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email"));
        panel.add(emailField);
        panel.add(isAdminCheckbox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                app.getUserManager().updateUser(user, nameField.getText(), lastNameField.getText(), emailField.getText());
                user.setAdmin(isAdminCheckbox.isSelected());
                refreshUserList();
                JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = (String) tableModel.getValueAt(selectedRow, 0);
        User user = app.getUserManager().getUserByUsername(username);

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el usuario?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            app.getUserManager().deleteUser(user);
            refreshUserList();
            JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }


}
