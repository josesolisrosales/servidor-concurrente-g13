package fidness.UI;

import fidness.User;
import fidness.UserManager;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private LoginPanel loginPanel;
    private MainPanel mainPanel;
    private UserManager userManager;
    private User currentUser;

    public App() {
        super("Gimansio Fidness");
        userManager = new UserManager();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        contentPanel.add(loginPanel, "LOGIN");

        add(contentPanel);
        setVisible(true);
    }

    public void showLoginPanel() {
        cardLayout.show(contentPanel, "LOGIN");
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

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App();
        });
    }
}
