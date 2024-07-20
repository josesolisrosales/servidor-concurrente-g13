package fidness.UI;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private App app;
    private JPanel contentPanel;
    private CustomSidebar sidebar;
    private CardLayout contentLayout;

    public MainPanel(App app) {
        this.app = app;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        sidebar = new CustomSidebar();
        contentPanel = new JPanel();
        contentLayout = new CardLayout();
        contentPanel.setLayout(contentLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        sidebar.addButton("Perfil", this::showProfile);
        sidebar.addButton("Ejercicios", this::showExercises);
        sidebar.addButton("Rutinas", this::showRoutines);

        if (app.getCurrentUser().isAdmin()) {
            sidebar.addButton("Usuarios", this::showUserManagement);
        }

        sidebar.addButton("Cerrar Sesión", this::logout);

        contentPanel.add(new JPanel(), "PERFIL");
        contentPanel.add(new JPanel(), "EJERCICIOS");
        contentPanel.add(new JPanel(), "RUTINAS");

        if (app.getCurrentUser().isAdmin()) {
            contentPanel.add(new UserManagementPanel(app), "MANEJO DE USUARIOS");
        }

        showProfile();
    }

    private void showProfile() {
        contentLayout.show(contentPanel, "PERFIL");
    }

    private void showExercises() {
        contentLayout.show(contentPanel, "EJERCICIOS");
    }

    private void showRoutines() {
        contentLayout.show(contentPanel, "RUTINAS");
    }

    private void showUserManagement() {
        contentLayout.show(contentPanel, "MANEJO DE USUARIOS");
    }

    private void logout() {
        app.setCurrentUser(null);
        app.showLoginPanel();
    }
}