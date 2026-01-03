package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/* Staff Management Panel */
public class StaffPanel extends JPanel {

    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField staffIDField, firstNameField, lastNameField, roleField, departmentField;
    private JTextField facilityIDField, emailField, phoneField, employmentStatusField;
    private JTextField startDateField, lineManagerField, accessLevelField;

    public StaffPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));

        // TABLE 
        String[] columns = {
                "Staff ID", "First Name", "Last Name", "Role", "Department",
                "Facility ID", "Email", "Phone", "Employment Status",
                "Start Date", "Line Manager", "Access Level"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(227, 242, 253));
        table.setSelectionForeground(Color.BLACK);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedStaff();
        });

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(236, 239, 241));
        header.setForeground(new Color(55, 71, 79));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 13f));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Staff"));
        add(scrollPane, BorderLayout.CENTER);

        // FORM 
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Staff Details"));
        formPanel.setBackground(new Color(250, 250, 250));
        add(formPanel, BorderLayout.SOUTH);

        // BUTTONS
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setBackground(new Color(245, 247, 250));
        add(buttonPanel, BorderLayout.NORTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Row 1
        add(panel, gbc, row, 0, "Staff ID:", staffIDField = new JTextField(15));
        add(panel, gbc, row, 2, "First Name:", firstNameField = new JTextField(15));
        add(panel, gbc, row++, 4, "Last Name:", lastNameField = new JTextField(15));

        // Row 2
        add(panel, gbc, row, 0, "Role:", roleField = new JTextField(15));
        add(panel, gbc, row, 2, "Department:", departmentField = new JTextField(15));
        add(panel, gbc, row++, 4, "Facility ID:", facilityIDField = new JTextField(15));

        // Row 3
        add(panel, gbc, row, 0, "Email:", emailField = new JTextField(15));
        add(panel, gbc, row, 2, "Phone:", phoneField = new JTextField(15));
        add(panel, gbc, row++, 4, "Employment Status:", employmentStatusField = new JTextField(15));

        // Row 4
        add(panel, gbc, row, 0, "Start Date:", startDateField = new JTextField(15));
        add(panel, gbc, row, 2, "Line Manager:", lineManagerField = new JTextField(15));
        add(panel, gbc, row++, 4, "Access Level:", accessLevelField = new JTextField(15));

        return panel;
    }

    private void add(JPanel panel, GridBagConstraints gbc, int row, int col, String label, JTextField field) {
        gbc.gridy = row;
        gbc.gridx = col;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = col + 1;
        panel.add(field, gbc);
    }

        /* BUTTON PANEL */
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton add = new JButton("Add");
        add.addActionListener(e -> addStaff());
        panel.add(add);

        JButton update = new JButton("Update");
        update.addActionListener(e -> updateStaff());
        panel.add(update);

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> deleteStaff());
        panel.add(delete);

        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> clearForm());
        panel.add(clear);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshData());
        panel.add(refresh);

        return panel;
    }


    private void addStaff() {
        Staff staff = createStaffFromForm();
        if (staff != null) {
            controller.addStaff(staff);
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Staff added!");
        }
    }

    private void updateStaff() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        controller.deleteStaff((String) tableModel.getValueAt(row, 0));
        controller.addStaff(createStaffFromForm());
        refreshData();
        clearForm();
    }

    private void deleteStaff() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        controller.deleteStaff((String) tableModel.getValueAt(row, 0));
        refreshData();
        clearForm();
    }

    private Staff createStaffFromForm() {
        if (staffIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Staff ID required.");
            return null;
        }
        return new Staff(
                staffIDField.getText().trim(),
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                roleField.getText().trim(),
                departmentField.getText().trim(),
                facilityIDField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                employmentStatusField.getText().trim(),
                startDateField.getText().trim(),
                lineManagerField.getText().trim(),
                accessLevelField.getText().trim()
        );
    }

    private void loadSelectedStaff() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        staffIDField.setText((String) tableModel.getValueAt(r, 0));
        firstNameField.setText((String) tableModel.getValueAt(r, 1));
        lastNameField.setText((String) tableModel.getValueAt(r, 2));
        roleField.setText((String) tableModel.getValueAt(r, 3));
        departmentField.setText((String) tableModel.getValueAt(r, 4));
        facilityIDField.setText((String) tableModel.getValueAt(r, 5));
        emailField.setText((String) tableModel.getValueAt(r, 6));
        phoneField.setText((String) tableModel.getValueAt(r, 7));
        employmentStatusField.setText((String) tableModel.getValueAt(r, 8));
        startDateField.setText((String) tableModel.getValueAt(r, 9));
        lineManagerField.setText((String) tableModel.getValueAt(r, 10));
        accessLevelField.setText((String) tableModel.getValueAt(r, 11));
    }

    private void clearForm() {
        for (JTextField f : new JTextField[]{
                staffIDField, firstNameField, lastNameField, roleField, departmentField,
                facilityIDField, emailField, phoneField, employmentStatusField,
                startDateField, lineManagerField, accessLevelField
        }) {
            f.setText("");
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Staff s : controller.getAllStaff()) {
            tableModel.addRow(new Object[]{
                s.getStaffID(), s.getFirstName(), s.getLastName(),
                s.getRole(), s.getDepartment(), s.getFacilityID(),
                s.getEmail(), s.getPhone(), s.getEmploymentStatus(),
                s.getStartDate(), s.getLineManager(), s.getAccessLevel()
            });
        }
    }
}