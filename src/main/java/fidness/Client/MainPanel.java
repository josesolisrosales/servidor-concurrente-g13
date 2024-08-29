package fidness.Client;

import fidness.Client.Utility.Client;
import fidness.Objects.Exercise;
import fidness.Objects.Routine;
import fidness.Objects.User;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private App app;
    private JPanel contentPanel;
    private CustomSidebar sidebar;
    private CardLayout contentLayout;
    private WelcomePanel welcomePanel;
    private ProfilePanel profilePanel;
    private ExercisePanel exercisePanel;
    private RoutinePanel routinePanel;
    private UserManagementPanel userManagementPanel;

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

        if (Client.getInstance().getCurrentUser().isAdmin()) {
            sidebar.addButton("Usuarios", this::showUserManagement);
        }

        sidebar.addButton("Cerrar Sesión", this::logout);

        welcomePanel = new WelcomePanel(app, this);
        contentPanel.add(welcomePanel, "WELCOME");
        profilePanel = new ProfilePanel(app);
        contentPanel.add(profilePanel, "PROFILE");
        exercisePanel = new ExercisePanel(app, this);
        contentPanel.add(exercisePanel, "EXERCISES");
        routinePanel = new RoutinePanel(app, this);
        contentPanel.add(routinePanel, "ROUTINES");

        if (Client.getInstance().getCurrentUser().isAdmin()) {
            userManagementPanel = new UserManagementPanel(app);
            contentPanel.add(userManagementPanel, "USER_MANAGEMENT");
        }

        showWelcome();
    }

    public void showWelcome() {
        welcomePanel.refresh();
        contentLayout.show(contentPanel, "WELCOME");
    }

    public void showProfile() {
        contentLayout.show(contentPanel, "PROFILE");
    }

    public void showExercises() {
        exercisePanel.refreshExerciseList();
        contentLayout.show(contentPanel, "EXERCISES");
    }

    public void showRoutines() {
        routinePanel.refreshRoutineList();
        contentLayout.show(contentPanel, "ROUTINES");
    }

    public void showUserManagement() {
        if (Client.getInstance().getCurrentUser().isAdmin()) {
            contentLayout.show(contentPanel, "USER_MANAGEMENT");
        } else {
            JOptionPane.showMessageDialog(this, "No tienes permisos para acceder a esta sección.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showExerciseDetail(Exercise exercise) {
        ExerciseDetailPanel detailPanel = new ExerciseDetailPanel(app, exercise, this);
        contentPanel.add(detailPanel, "EXERCISE_DETAIL");
        contentLayout.show(contentPanel, "EXERCISE_DETAIL");
    }

    public void showExerciseDetail(Exercise exercise, Routine routineOrigin) {
        ExerciseDetailPanel detailPanel = new ExerciseDetailPanel(app, exercise, this, routineOrigin);
        contentPanel.add(detailPanel, "EXERCISE_DETAIL");
        contentLayout.show(contentPanel, "EXERCISE_DETAIL");
    }

    public void showRoutineDetail(Routine routine) {
        RoutineDetailPanel detailPanel = new RoutineDetailPanel(app, this, routine);
        contentPanel.add(detailPanel, "ROUTINE_DETAIL");
        contentLayout.show(contentPanel, "ROUTINE_DETAIL");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Client.getInstance().logout();
            app.showLoginPanel();
        }
    }

    // Método para actualizar la interfaz después de cambios en el perfil del usuario
    public void updateUserInterface() {
        User currentUser = Client.getInstance().getCurrentUser();

        // Actualizar sidebar si el estado de admin ha cambiado
        sidebar.removeAll();
        sidebar.addButton("Inicio", this::showWelcome);
        sidebar.addButton("Perfil", this::showProfile);
        sidebar.addButton("Ejercicios", this::showExercises);
        sidebar.addButton("Rutinas", this::showRoutines);

        if (currentUser.isAdmin()) {
            sidebar.addButton("Usuarios", this::showUserManagement);
            if (userManagementPanel == null) {
                userManagementPanel = new UserManagementPanel(app);
                contentPanel.add(userManagementPanel, "USER_MANAGEMENT");
            }
        } else {
            if (userManagementPanel != null) {
                contentPanel.remove(userManagementPanel);
                userManagementPanel = null;
            }
        }

        sidebar.addButton("Cerrar Sesión", this::logout);

        sidebar.revalidate();
        sidebar.repaint();

        // Actualizar el panel de bienvenida
        welcomePanel.refresh();
    }
}