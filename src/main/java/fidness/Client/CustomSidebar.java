package fidness.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CustomSidebar extends JPanel {
    private boolean isExpanded = true;
    private int expandedWidth = 200;
    private int collapsedWidth = 50;
    private List<SidebarButton> buttons = new ArrayList<>();
    private JButton toggleButton;

    public CustomSidebar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50, 50, 50));
        setPreferredSize(new Dimension(expandedWidth, getPreferredSize().height));

        toggleButton = new JButton("<<");
        toggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleButton.setOpaque(false);
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.addActionListener(e -> toggleSidebar());

        add(Box.createVerticalStrut(10));
        add(toggleButton);
        add(Box.createVerticalStrut(10));
    }

    public void addButton(String text, Runnable action) {
        SidebarButton button = new SidebarButton(text, action);
        buttons.add(button);
        add(button);
        add(Box.createVerticalStrut(5));
    }

    public void removeAll() {
        super.removeAll();
        buttons.clear();
        add(Box.createVerticalStrut(10));
        add(toggleButton);
        add(Box.createVerticalStrut(10));
    }

    private void toggleSidebar() {
        isExpanded = !isExpanded;
        int width = isExpanded ? expandedWidth : collapsedWidth;
        setPreferredSize(new Dimension(width, getPreferredSize().height));
        toggleButton.setText(isExpanded ? "<<" : ">>");
        for (SidebarButton button : buttons) {
            button.setExpanded(isExpanded);
        }
        revalidate();
        repaint();
    }

    private class SidebarButton extends JPanel {
        private JLabel iconLabel;
        private JLabel textLabel;
        private Runnable action;

        public SidebarButton(String text, Runnable action) {
            this.action = action;
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBackground(new Color(50, 50, 50));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            iconLabel = new JLabel(text.substring(0, 1));
            iconLabel.setForeground(Color.WHITE);
            iconLabel.setPreferredSize(new Dimension(30, 30));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

            textLabel = new JLabel(text);
            textLabel.setForeground(Color.WHITE);

            add(Box.createHorizontalStrut(10));
            add(iconLabel);
            add(Box.createHorizontalStrut(10));
            add(textLabel);
            add(Box.createHorizontalGlue());

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    action.run();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(new Color(70, 70, 70));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(new Color(50, 50, 50));
                }
            });
        }

        public void setExpanded(boolean expanded) {
            textLabel.setVisible(expanded);
        }
    }
}