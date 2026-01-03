package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Clinician;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/* Clinician Management Panel */
public class ClinicianPanel extends JPanel {

    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField clinicianIDField, firstNameField, lastNameField, titleField;
    private JTextField specialtyField, gmcNumberField, workplaceIDField, workplaceTypeField;
    private JTextField employmentStatusField, startDateField, emailField, phoneField;

    public ClinicianPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250)); // SAME AS PATIENT

        /* TABLE */
        String[] columns = {
                "Clinician ID", "First Name", "Last Name", "Title",
                "Specialty", "GMC Number", "Workplace ID", "Workplace Type",
                "Employment Status", "Start Date", "Email", "Phone"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(227, 242, 253));
        table.setSelectionForeground(Color.BLACK);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedClinician();
        });

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(236, 239, 241));
        header.setForeground(new Color(55, 71, 79));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 13f));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Clinicians"));
        add(scrollPane, BorderLayout.CENTER);

        /* FORM */
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Clinician Details"));
        formPanel.setBackground(new Color(250, 250, 250));
        add(formPanel, BorderLayout.SOUTH);

        /* BUTTONS */
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

        addField(panel, gbc, row, 0, "Clinician ID:", clinicianIDField = new JTextField(15));
        addField(panel, gbc, row, 2, "First Name:", firstNameField = new JTextField(15));
        addField(panel, gbc, row, 4, "Last Name:", lastNameField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Title:", titleField = new JTextField(15));
        addField(panel, gbc, row, 2, "Specialty:", specialtyField = new JTextField(15));
        addField(panel, gbc, row, 4, "GMC Number:", gmcNumberField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Workplace ID:", workplaceIDField = new JTextField(15));
        addField(panel, gbc, row, 2, "Workplace Type:", workplaceTypeField = new JTextField(15));
        addField(panel, gbc, row, 4, "Employment Status:", employmentStatusField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Start Date:", startDateField = new JTextField(15));
        addField(panel, gbc, row, 2, "Email:", emailField = new JTextField(15));
        addField(panel, gbc, row, 4, "Phone:", phoneField = new JTextField(15));

        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, int col,
                          String label, JTextField field) {
        gbc.gridx = col;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = col + 1;
        panel.add(field, gbc);
    }

        /* BUTTON PANEL */

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton add = new JButton("Add");
        add.addActionListener(e -> addClinician());
        panel.add(add);

        JButton update = new JButton("Update");
        update.addActionListener(e -> updateClinician());
        panel.add(update);

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> deleteClinician());
        panel.add(delete);

        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> clearForm());
        panel.add(clear);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshData());
        panel.add(refresh);

        return panel;
    }
        /* CRUD */

    private void addClinician() {
        Clinician c = getClinicianFromForm();
        if (c != null) {
            controller.addClinician(c);
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Clinician added successfully!");
        }
    }

    private void updateClinician() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        controller.deleteClinician(tableModel.getValueAt(r, 0).toString());
        controller.addClinician(getClinicianFromForm());
        refreshData();
        clearForm();
    }

    private void deleteClinician() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        controller.deleteClinician(tableModel.getValueAt(r, 0).toString());
        refreshData();
        clearForm();
    }

    private Clinician getClinicianFromForm() {
        if (clinicianIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Clinician ID is required");
            return null;
        }

        return new Clinician(
                clinicianIDField.getText().trim(),
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                titleField.getText().trim(),
                specialtyField.getText().trim(),
                gmcNumberField.getText().trim(),
                workplaceIDField.getText().trim(),
                workplaceTypeField.getText().trim(),
                employmentStatusField.getText().trim(),
                startDateField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim()
        );
    }

    private void loadSelectedClinician() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        clinicianIDField.setText(tableModel.getValueAt(r, 0).toString());
        firstNameField.setText(tableModel.getValueAt(r, 1).toString());
        lastNameField.setText(tableModel.getValueAt(r, 2).toString());
        titleField.setText(tableModel.getValueAt(r, 3).toString());
        specialtyField.setText(tableModel.getValueAt(r, 4).toString());
        gmcNumberField.setText(tableModel.getValueAt(r, 5).toString());
        workplaceIDField.setText(tableModel.getValueAt(r, 6).toString());
        workplaceTypeField.setText(tableModel.getValueAt(r, 7).toString());
        employmentStatusField.setText(tableModel.getValueAt(r, 8).toString());
        startDateField.setText(tableModel.getValueAt(r, 9).toString());
        emailField.setText(tableModel.getValueAt(r, 10).toString());
        phoneField.setText(tableModel.getValueAt(r, 11).toString());
    }

    private void clearForm() {
        clinicianIDField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        titleField.setText("");
        specialtyField.setText("");
        gmcNumberField.setText("");
        workplaceIDField.setText("");
        workplaceTypeField.setText("");
        employmentStatusField.setText("");
        startDateField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Clinician> list = controller.getAllClinicians();
        for (Clinician c : list) {
            tableModel.addRow(new Object[]{
                    c.getClinicianID(), c.getFirstName(), c.getLastName(),
                    c.getTitle(), c.getSpecialty(), c.getGmcNumber(),
                    c.getWorkplaceID(), c.getWorkplaceType(),
                    c.getEmploymentStatus(), c.getStartDate(),
                    c.getEmail(), c.getPhone()
            });
        }
    }
}