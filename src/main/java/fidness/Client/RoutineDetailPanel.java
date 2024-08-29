package fidness.Client;

import fidness.Objects.Routine;
import fidness.Objects.Exercise;
import fidness.Client.Utility.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RoutineDetailPanel extends JPanel {
    private App app;
    private MainPanel mainPanel;
    private Routine routine;
    private JTable exerciseTable;
    private DefaultTableModel tableModel;
    private JButton addExerciseButton;
    private JButton removeExerciseButton;
    private JButton saveButton;
    private JButton backButton;
    private JTextField nameField;
    private JTextArea descriptionArea;

    public RoutineDetailPanel(App app, MainPanel mainPanel, Routine routine) {
        this.app = app;
        this.mainPanel = mainPanel;
        this.routine = routine;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        createHeaderPanel();
        createExerciseTable();
        createButtonPanel();
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = new JTextField(routine.getName());
        descriptionArea = new JTextArea(routine.getDescription(), 3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        boolean isEditable = routine.getOwner().equals(Client.getInstance().getCurrentUser().getUsername());
        nameField.setEditable(isEditable);
        descriptionArea.setEditable(isEditable);

        headerPanel.add(new JLabel("Nombre:"));
        headerPanel.add(nameField);
        headerPanel.add(new JLabel("Descripción:"));
        headerPanel.add(new JScrollPane(descriptionArea));

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createExerciseTable() {
        tableModel = new DefaultTableModel(new String[]{"Nombre", "Grupo Muscular"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        exerciseTable = new JTable(tableModel);
        exerciseTable.setRowHeight(40);
        exerciseTable.setFont(new Font("Arial", Font.PLAIN, 14));

        exerciseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = exerciseTable.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    showExerciseDetail(routine.getExcercises().get(row));
                }
            }
        });

        refreshExerciseList();

        JScrollPane scrollPane = new JScrollPane(exerciseTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boolean isOwner = routine.getOwner().equals(Client.getInstance().getCurrentUser().getUsername());

        if (isOwner) {
            addExerciseButton = new JButton("Agregar Ejercicio");
            removeExerciseButton = new JButton("Eliminar Ejercicio");
            saveButton = new JButton("Guardar Cambios");

            addExerciseButton.addActionListener(e -> showAddExerciseDialog());
            removeExerciseButton.addActionListener(e -> removeSelectedExercise());
            saveButton.addActionListener(e -> saveRoutineChanges());

            buttonPanel.add(addExerciseButton);
            buttonPanel.add(removeExerciseButton);
            buttonPanel.add(saveButton);
        }

        backButton = new JButton("Volver");
        backButton.addActionListener(e -> mainPanel.showRoutines());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshExerciseList() {
        tableModel.setRowCount(0);
        for (Exercise exercise : routine.getExcercises()) {
            tableModel.addRow(new Object[]{
                    exercise.getName(),
                    exercise.getMuscleGroup()
            });
        }
    }

    private void showAddExerciseDialog() {
        List<Exercise> allExercises = Client.getInstance().getExercises();
        Exercise[] exerciseArray = allExercises.toArray(new Exercise[0]);

        Exercise selectedExercise = (Exercise) JOptionPane.showInputDialog(
                this,
                "Seleccione un ejercicio para agregar:",
                "Agregar Ejercicio",
                JOptionPane.PLAIN_MESSAGE,
                null,
                exerciseArray,
                null);

        if (selectedExercise != null) {
            routine.addExcercise(selectedExercise);
            refreshExerciseList();
        }
    }

    private void removeSelectedExercise() {
        int selectedRow = exerciseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un ejercicio para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Exercise selectedExercise = routine.getExcercises().get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar el ejercicio '" + selectedExercise.getName() + "' de la rutina?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            routine.removeExcercise(selectedExercise);
            refreshExerciseList();
        }
    }

    private void saveRoutineChanges() {
        String newName = nameField.getText().trim();
        String newDescription = descriptionArea.getText().trim();

        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la rutina no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        routine.setName(newName);
        routine.setDescription(newDescription);

        boolean success = Client.getInstance().updateRoutine(routine);

        if (success) {
            JOptionPane.showMessageDialog(this, "Rutina actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar la rutina", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showExerciseDetail(Exercise exercise) {
        mainPanel.showExerciseDetail(exercise, routine);
    }
}