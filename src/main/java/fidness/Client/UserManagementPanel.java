package fidness.Client;

import fidness.Client.Utility.Client;
import fidness.Objects.User;

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
    private List<User> displayedUsers;

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
        userTable.setRowHeight(30);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
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
        displayedUsers = Client.getInstance().getAllUsers();
        for (User user : displayedUsers) {
            tableModel.addRow(new Object[]{
                    user.getUsername(),
                    user.getName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.isAdmin() ? "Sí" : "No"
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
            String username = usernameField.getText().trim();
            String name = nameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            boolean isAdmin = isAdminCheckbox.isSelected();

            if (username.isEmpty() || name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = Client.getInstance().createUser(username, name, lastName, password, email, isAdmin);
            if (success) {
                refreshUserList();
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear el usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User selectedUser = displayedUsers.get(selectedRow);

        JTextField nameField = new JTextField(selectedUser.getName(), 15);
        JTextField lastNameField = new JTextField(selectedUser.getLastName(), 15);
        JTextField emailField = new JTextField(selectedUser.getEmail(), 15);
        JCheckBox isAdminCheckbox = new JCheckBox("Admin", selectedUser.isAdmin());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Apellido:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(isAdminCheckbox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            boolean isAdmin = isAdminCheckbox.isSelected();

            if (name.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = Client.getInstance().updateUser(selectedUser.getUsername(), name, lastName, email);
            if (success) {
                // Actualizar el estado de admin
                success = Client.getInstance().changeUserAdminStatus(selectedUser.getUsername(), isAdmin);
                if (success) {
                    refreshUserList();
                    JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el estado de administrador", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User selectedUser = displayedUsers.get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar el usuario '" + selectedUser.getUsername() + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = Client.getInstance().deleteUser(selectedUser.getUsername());
            if (success) {
                refreshUserList();
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}