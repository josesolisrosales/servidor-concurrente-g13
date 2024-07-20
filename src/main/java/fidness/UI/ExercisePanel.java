package fidness.UI;

import fidness.Exercise;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class ExercisePanel extends JPanel {
    private App app;
    private MainPanel mainPanel;
    private JTable exerciseTable;
    private DefaultTableModel tableModel;
    private JButton addExerciseButton;
    private JButton deleteExercisesButton;
    private JButton addToRoutineButton;
    private JComboBox<String> muscleGroupFilter;
    private List<Exercise> displayedExercises;

    public ExercisePanel(App app, MainPanel mainPanel) {
        this.app = app;
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        createTable();
        createFilterAndButtons();
        refreshExerciseList();
    }

    private void createTable() {
        tableModel = new DefaultTableModel(new String[]{"", "Nombre", "Grupo Muscular"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        exerciseTable = new JTable(tableModel);
        exerciseTable.setRowHeight(50);
        exerciseTable.setFont(new Font("Arial", Font.PLAIN, 16));
        exerciseTable.getColumnModel().getColumn(0).setMaxWidth(30);

        exerciseTable.setDefaultRenderer(String.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    label.setBackground(table.getBackground());
                    label.setForeground(table.getForeground());
                }
                return label;
            }
        });

        exerciseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = exerciseTable.rowAtPoint(evt.getPoint());
                int col = exerciseTable.columnAtPoint(evt.getPoint());
                if (row >= 0 && col > 0) {
                    showExerciseDetail(displayedExercises.get(row));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(exerciseTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createFilterAndButtons() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String[] muscleGroups = {"Todos", "Chest", "Legs", "Back", "Arms", "Core", "Cardio", "Full Body"};
        muscleGroupFilter = new JComboBox<>(muscleGroups);
        muscleGroupFilter.addActionListener(e -> refreshExerciseList());

        addExerciseButton = new JButton("Agregar Ejercicio");
        addExerciseButton.addActionListener(e -> showAddExerciseDialog());
        addExerciseButton.setVisible(app.getCurrentUser().isAdmin());

        deleteExercisesButton = new JButton("Eliminar Seleccionados");
        deleteExercisesButton.addActionListener(e -> deleteSelectedExercises());
        deleteExercisesButton.setVisible(app.getCurrentUser().isAdmin());

        addToRoutineButton = new JButton("Añadir a Rutina");
        addToRoutineButton.addActionListener(e -> addSelectedToRoutine());

        controlPanel.add(new JLabel("Filtrar por grupo muscular: "));
        controlPanel.add(muscleGroupFilter);
        controlPanel.add(addExerciseButton);
        controlPanel.add(deleteExercisesButton);
        controlPanel.add(addToRoutineButton);

        add(controlPanel, BorderLayout.NORTH);
    }

    private void refreshExerciseList() {
        tableModel.setRowCount(0);
        String selectedMuscleGroup = (String) muscleGroupFilter.getSelectedItem();

        if (selectedMuscleGroup.equals("Todos")) {
            displayedExercises = app.getExerciseManager().getAllExercises();
        } else {
            displayedExercises = app.getExerciseManager().getExercisesByMuscleGroup(selectedMuscleGroup);
        }

        for (Exercise exercise : displayedExercises) {
            tableModel.addRow(new Object[]{
                    false,
                    exercise.getName(),
                    exercise.getMuscleGroup()
            });
        }
    }

    private void showAddExerciseDialog() {
        if (!app.getCurrentUser().isAdmin()) {
            JOptionPane.showMessageDialog(this, "Solo los administradores pueden agregar ejercicios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField nameField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField videoUrlField = new JTextField(20);
        JComboBox<String> muscleGroupCombo = new JComboBox<>(new String[]{"Chest", "Legs", "Back", "Arms", "Core", "Cardio", "Full Body"});

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descriptionField);
        panel.add(new JLabel("URL del Video (opcional):"));
        panel.add(videoUrlField);
        panel.add(new JLabel("Grupo Muscular:"));
        panel.add(muscleGroupCombo);

        int result = JOptionPane.showConfirmDialog(null, panel, "Agregar Ejercicio", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            app.getExerciseManager().addExercise(
                    nameField.getText(),
                    descriptionField.getText(),
                    videoUrlField.getText(),
                    (String) muscleGroupCombo.getSelectedItem()
            );
            refreshExerciseList();
            JOptionPane.showMessageDialog(this, "Ejercicio agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showExerciseDetail(Exercise exercise) {
        mainPanel.showExerciseDetail(exercise);
    }

    private void deleteSelectedExercises() {
        if (!app.getCurrentUser().isAdmin()) {
            JOptionPane.showMessageDialog(this, "Solo los administradores pueden eliminar ejercicios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Exercise> exercisesToDelete = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Boolean) tableModel.getValueAt(i, 0)) {
                exercisesToDelete.add(displayedExercises.get(i));
            }
        }

        if (exercisesToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione al menos un ejercicio para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar los ejercicios seleccionados?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            for (Exercise exercise : exercisesToDelete) {
                app.getExerciseManager().deleteExercise(exercise);
            }
            refreshExerciseList();
            JOptionPane.showMessageDialog(this, "Ejercicios eliminados exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addSelectedToRoutine() {
        List<Exercise> selectedExercises = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Boolean) tableModel.getValueAt(i, 0)) {
                selectedExercises.add(displayedExercises.get(i));
            }
        }

        if (selectedExercises.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione al menos un ejercicio para añadir a la rutina.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Placeholder for adding to routine functionality
        JOptionPane.showMessageDialog(this, "Funcionalidad de añadir a rutina aún no implementada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
}