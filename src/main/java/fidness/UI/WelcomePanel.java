package fidness.UI;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    private App app;

    public WelcomePanel(App app) {
        this.app = app;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Bienvenido " + app.getCurrentUser().getName() + " " + app.getCurrentUser().getLastName());
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel appNameLabel = new JLabel("Gimnasio Fidness");
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        appNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(appNameLabel);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }
}