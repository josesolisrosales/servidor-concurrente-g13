package fidness.Client;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private LoginPanel loginPanel;
    private MainPanel mainPanel;

    public App() {
        super("Gimnasio Fidness");
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
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

    public void loginSuccessful() {
        showMainPanel();
    }

    public void logout() {
        showLoginPanel();
    }

    public void updateUserInterface() {
        if (mainPanel != null) {
            mainPanel.updateUserInterface();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}