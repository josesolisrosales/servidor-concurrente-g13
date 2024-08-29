package fidness.Client;

import fidness.Client.Utility.Client;
import fidness.Objects.User;
import fidness.Objects.Routine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class WelcomePanel extends JPanel {
    private App app;
    private MainPanel mainPanel;
    private JLabel welcomeLabel;
    private JLabel statsLabel;
    private JPanel quickAccessPanel;

    public WelcomePanel(App app, MainPanel mainPanel) {
        this.app = app;
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        initComponents();
    }

    private void initComponents() {
        User currentUser = Client.getInstance().getCurrentUser();

        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Bienvenido, " + currentUser.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(welcomeLabel, BorderLayout.NORTH);

        statsLabel = new JLabel("Cargando estadísticas...");
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(statsLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Panel central con acceso rápido
        quickAccessPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        quickAccessPanel.setBorder(BorderFactory.createTitledBorder("Acceso Rápido"));

        addQuickAccessButton("Ver Ejercicios", e -> mainPanel.showExercises());
        addQuickAccessButton("Ver Rutinas", e -> mainPanel.showRoutines());
        addQuickAccessButton("Mi Perfil", e -> mainPanel.showProfile());

        if (currentUser.isAdmin()) {
            addQuickAccessButton("Gestionar Usuarios", e -> mainPanel.showUserManagement());
        }

        add(new JScrollPane(quickAccessPanel), BorderLayout.CENTER);

        // Cargar estadísticas de forma asíncrona
        SwingUtilities.invokeLater(this::loadStats);
    }

    private void addQuickAccessButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        quickAccessPanel.add(button);
    }

    private void loadStats() {
        User currentUser = Client.getInstance().getCurrentUser();
        List<Routine> userRoutines = Client.getInstance().getRoutines(currentUser.getUsername());
        int totalExercises = Client.getInstance().getExercises().size();

        SwingUtilities.invokeLater(() -> {
            statsLabel.setText(String.format("Tienes %d rutinas | Total de ejercicios disponibles: %d",
                    userRoutines.size(), totalExercises));
        });
    }

    // Método para actualizar el panel cuando se vuelve a mostrar
    public void refresh() {
        User currentUser = Client.getInstance().getCurrentUser();
        welcomeLabel.setText("Bienvenido, " + currentUser.getName() + "!");
        loadStats();
    }
}