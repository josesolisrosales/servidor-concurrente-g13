package fidness.UI;

import fidness.User;
import fidness.UserManager;
import fidness.ExerciseManager;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private LoginPanel loginPanel;
    private MainPanel mainPanel;
    private UserManager userManager;
    private ExerciseManager exerciseManager;
    private User currentUser;

    public App() {
        super("Gimnasio Fidness");
        userManager = new UserManager();
        exerciseManager = new ExerciseManager();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        contentPanel.add(loginPanel, "LOGIN");

        add(contentPanel);
        setVisible(true);
    }

    public void showLoginPanel() {
        setCurrentUser(null);

        if (mainPanel != null) {
            contentPanel.remove(mainPanel);
            mainPanel = null;
        }

        cardLayout.show(contentPanel, "LOGIN");

        loginPanel.resetFields();
    }

    public void showMainPanel() {
        if (mainPanel == null) {
            mainPanel = new MainPanel(this);
            contentPanel.add(mainPanel, "MAIN");
        }
        cardLayout.show(contentPanel, "MAIN");
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ExerciseManager getExerciseManager() {
        return exerciseManager;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}