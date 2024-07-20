package fidness.UI;

import fidness.Exercise;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private App app;
    private JPanel contentPanel;
    private CustomSidebar sidebar;
    private CardLayout contentLayout;
    private ExercisePanel exercisePanel;

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

        sidebar.addButton("Inicio", this::showWelcome);
        sidebar.addButton("Perfil", this::showProfile);
        sidebar.addButton("Ejercicios", this::showExercises);
        sidebar.addButton("Rutinas", this::showRoutines);

        if (app.getCurrentUser().isAdmin()) {
            sidebar.addButton("Usuarios", this::showUserManagement);
        }

        sidebar.addButton("Cerrar Sesión", this::logout);

        contentPanel.add(new WelcomePanel(app), "INICIO");
        contentPanel.add(new ProfilePanel(app), "PERFIL");
        exercisePanel = new ExercisePanel(app, this);
        contentPanel.add(exercisePanel, "EJERCICIOS");
        contentPanel.add(new JPanel(), "RUTINAS");

        if (app.getCurrentUser().isAdmin()) {
            contentPanel.add(new UserManagementPanel(app), "MANEJO DE USUARIOS");
        }

        showWelcome();
    }

    private void showWelcome() {
        contentLayout.show(contentPanel, "INICIO");
    }

    private void showProfile() {
        contentLayout.show(contentPanel, "PERFIL");
    }

    public void showExercises() {
        contentLayout.show(contentPanel, "EJERCICIOS");
    }

    private void showRoutines() {
        contentLayout.show(contentPanel, "RUTINAS");
    }

    private void showUserManagement() {
        contentLayout.show(contentPanel, "MANEJO DE USUARIOS");
    }

    public void showExerciseDetail(Exercise exercise) {
        ExerciseDetailPanel detailPanel = new ExerciseDetailPanel(app, exercise, this);
        contentPanel.add(detailPanel, "EXERCISE_DETAIL");
        contentLayout.show(contentPanel, "EXERCISE_DETAIL");
    }

    private void logout() {
        app.setCurrentUser(null);
        app.showLoginPanel();
    }
}