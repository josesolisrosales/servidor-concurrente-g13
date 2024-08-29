package fidness.Client;

import fidness.Objects.Exercise;
import fidness.Objects.Routine;
import fidness.Client.Utility.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExerciseDetailPanel extends JPanel {
    private App app;
    private Exercise exercise;
    private MainPanel mainPanel;
    private boolean openedFromRoutine;
    private Routine routineOrigin;

    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField videoUrlField;
    private JComboBox<String> muscleGroupComboBox;
    private JButton saveButton;
    private JButton backButton;

    public ExerciseDetailPanel(App app, Exercise exercise, MainPanel mainPanel) {
        this.app = app;
        this.exercise = exercise;
        this.mainPanel = mainPanel;
        this.openedFromRoutine = false;
        setLayout(new BorderLayout());
        initComponents();
    }

    public ExerciseDetailPanel(App app, Exercise exercise, MainPanel mainPanel, Routine routineOrigin) {
        this.app = app;
        this.exercise = exercise;
        this.mainPanel = mainPanel;
        this.openedFromRoutine = true;
        this.routineOrigin = routineOrigin;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Nombre del ejercicio
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Nombre:"));
        nameField = new JTextField(exercise.getName(), 20);
        nameField.setEditable(Client.getInstance().getCurrentUser().isAdmin());
        namePanel.add(nameField);
        contentPanel.add(namePanel);

        // Grupo muscular
        JPanel muscleGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        muscleGroupPanel.add(new JLabel("Grupo Muscular:"));
        String[] muscleGroups = {"Chest", "Legs", "Back", "Arms", "Core", "Cardio", "Full Body"};
        muscleGroupComboBox = new JComboBox<>(muscleGroups);
        muscleGroupComboBox.setSelectedItem(exercise.getMuscleGroup());
        muscleGroupComboBox.setEnabled(Client.getInstance().getCurrentUser().isAdmin());
        muscleGroupPanel.add(muscleGroupComboBox);
        contentPanel.add(muscleGroupPanel);

        // Descripción
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(new JLabel("Descripción:"), BorderLayout.NORTH);
        descriptionArea = new JTextArea(exercise.getDescription(), 5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(Client.getInstance().getCurrentUser().isAdmin());
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);
        contentPanel.add(descriptionPanel);

        // URL del video
        JPanel videoUrlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        videoUrlPanel.add(new JLabel("URL del Video:"));
        videoUrlField = new JTextField(exercise.getVideoUrl(), 20);
        videoUrlField.setEditable(Client.getInstance().getCurrentUser().isAdmin());
        videoUrlPanel.add(videoUrlField);
        contentPanel.add(videoUrlPanel);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if (Client.getInstance().getCurrentUser().isAdmin()) {
            saveButton = new JButton("Guardar Cambios");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveChanges();
                }
            });
            buttonPanel.add(saveButton);
        }

        backButton = new JButton("Volver");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        buttonPanel.add(backButton);

        contentPanel.add(buttonPanel);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }

    private void saveChanges() {
        String newName = nameField.getText().trim();
        String newDescription = descriptionArea.getText().trim();
        String newVideoUrl = videoUrlField.getText().trim();
        String newMuscleGroup = (String) muscleGroupComboBox.getSelectedItem();

        if (newName.isEmpty() || newDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y la descripción son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Exercise updatedExercise = new Exercise(newName, newDescription, newVideoUrl, newMuscleGroup);
        boolean success = Client.getInstance().updateExercise(updatedExercise);

        if (success) {
            JOptionPane.showMessageDialog(this, "Ejercicio actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.exercise = updatedExercise;
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el ejercicio", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBack() {
        if (openedFromRoutine) {
            mainPanel.showRoutineDetail(routineOrigin);
        } else {
            mainPanel.showExercises();
        }
    }
}