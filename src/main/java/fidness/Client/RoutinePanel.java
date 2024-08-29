package fidness.Client;

import fidness.Objects.Routine;
import fidness.Client.Utility.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoutinePanel extends JPanel {
    private App app;
    private MainPanel mainPanel;
    private JTable routineTable;
    private DefaultTableModel tableModel;
    private JButton addRoutineButton;
    private JButton deleteRoutineButton;
    private List<Routine> displayedRoutines;

    public RoutinePanel(App app, MainPanel mainPanel) {
        this.app = app;
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        createTable();
        createButtons();
        refreshRoutineList();
    }

    private void createTable() {
        tableModel = new DefaultTableModel(new String[]{"Nombre", "Descripción", "Número de ejercicios"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        routineTable = new JTable(tableModel);
        routineTable.setRowHeight(40);
        routineTable.setFont(new Font("Arial", Font.PLAIN, 14));

        routineTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = routineTable.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    showRoutineDetail(displayedRoutines.get(row));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(routineTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRoutineButton = new JButton("Agregar Rutina");
        deleteRoutineButton = new JButton("Eliminar Rutina");

        addRoutineButton.addActionListener(e -> showAddRoutineDialog());
        deleteRoutineButton.addActionListener(e -> deleteSelectedRoutine());

        buttonPanel.add(addRoutineButton);
        buttonPanel.add(deleteRoutineButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    void refreshRoutineList() {
        tableModel.setRowCount(0);
        displayedRoutines = Client.getInstance().getRoutines(Client.getInstance().getCurrentUser().getUsername());
        for (Routine routine : displayedRoutines) {
            tableModel.addRow(new Object[]{
                    routine.getName(),
                    routine.getDescription(),
                    routine.getExcercises().size()
            });
        }
    }

    private void showAddRoutineDialog() {
        JTextField nameField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Agregar Rutina", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre de la rutina no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Routine newRoutine = new Routine(name, description);
            boolean success = Client.getInstance().addRoutine(newRoutine, Client.getInstance().getCurrentUser().getUsername());
            if (success) {
                refreshRoutineList();
                JOptionPane.showMessageDialog(this, "Rutina agregada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar la rutina", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedRoutine() {
        int selectedRow = routineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una rutina para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Routine selectedRoutine = displayedRoutines.get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar la rutina '" + selectedRoutine.getName() + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = Client.getInstance().removeRoutine(selectedRoutine, Client.getInstance().getCurrentUser().getUsername());
            if (success) {
                refreshRoutineList();
                JOptionPane.showMessageDialog(this, "Rutina eliminada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la rutina", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRoutineDetail(Routine routine) {
        mainPanel.showRoutineDetail(routine);
    }
}