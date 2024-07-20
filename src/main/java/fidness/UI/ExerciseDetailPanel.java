package fidness.UI;

import fidness.Exercise;

import javax.swing.*;
import java.awt.*;

public class ExerciseDetailPanel extends JPanel {
    private App app;
    private Exercise exercise;
    private MainPanel mainPanel;

    public ExerciseDetailPanel(App app, Exercise exercise, MainPanel mainPanel) {
        this.app = app;
        this.exercise = exercise;
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel(exercise.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel muscleGroupLabel = new JLabel("Grupo Muscular: " + exercise.getMuscleGroup());
        muscleGroupLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        muscleGroupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea descriptionArea = new JTextArea(exercise.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBorder(BorderFactory.createTitledBorder("Descripción"));

        JLabel videoLabel = new JLabel("Video URL: " + (exercise.getVideoUrl().isEmpty() ? "No disponible" : exercise.getVideoUrl()));
        videoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        videoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(e -> mainPanel.showExercises());
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(nameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(muscleGroupLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(descriptionScrollPane);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(videoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(backButton);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }
}