package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Prescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Prescription Management Panel 
 */

public class PrescriptionPanel extends JPanel {

    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField prescriptionIDField, patientIDField, clinicianIDField;
    private JTextField appointmentIDField, medicationField, dosageField;
    private JTextField frequencyField, durationDaysField, quantityField;
    private JTextField instructionsField, pharmacyField, datePrescribedField; 
    private JTextField issueDateField, collectionStatusField, collectionDateField;

    public PrescriptionPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));

        /* ================= TABLE ================= */
        String[] columns = {
                "Prescription ID", "Patient ID", "Clinician ID", "Appointment ID",
                "Medication", "Dosage", "Frequency", "Duration Days",
                "Quantity", "Instructions", "Pharmacy", "Date Prescribed", "Issue Date",
                "Collection Status", "Collection Date"
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
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedPrescription();
        });

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(236, 239, 241));
        header.setForeground(new Color(55, 71, 79));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 13f));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Prescriptions"));
        add(scrollPane, BorderLayout.CENTER);

        /* ================= FORM ================= */
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Prescription Details"));
        formPanel.setBackground(new Color(250, 250, 250));
        add(formPanel, BorderLayout.SOUTH);

        /* ================= BUTTONS ================= */
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setBackground(new Color(245, 247, 250));
        add(buttonPanel, BorderLayout.NORTH);
    }

    /* ================= FORM ================= */

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        addField(panel, gbc, row, 0, "Prescription ID:", prescriptionIDField = new JTextField(15));
        addField(panel, gbc, row, 2, "Patient ID:", patientIDField = new JTextField(15));
        addField(panel, gbc, row, 4, "Clinician ID:", clinicianIDField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Appointment ID:", appointmentIDField = new JTextField(15));
        addField(panel, gbc, row, 2, "Medication:", medicationField = new JTextField(15));
        addField(panel, gbc, row, 4, "Dosage:", dosageField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Frequency:", frequencyField = new JTextField(15));
        addField(panel, gbc, row, 2, "Duration Days:", durationDaysField = new JTextField(15));
        addField(panel, gbc, row, 4, "Quantity:", quantityField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Instructions:", instructionsField = new JTextField(15));
        addField(panel, gbc, row, 2, "Pharmacy:", pharmacyField = new JTextField(15));
        addField(panel, gbc, row, 4, "Date Prescribed:", datePrescribedField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Issue Date:", issueDateField = new JTextField(15));
        addField(panel, gbc, row, 2, "Collection Status:", collectionStatusField = new JTextField(15));
        addField(panel, gbc, row, 4, "Collection Date:", collectionDateField = new JTextField(15));
        
        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, int col, String label, JTextField field) {
        gbc.gridx = col;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = col + 1;
        panel.add(field, gbc);
    }

    /* ================= BUTTON PANEL ================= */

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton add = new JButton("Add");
        add.addActionListener(e -> addPrescription());
        panel.add(add);

        JButton update = new JButton("Update");
        update.addActionListener(e -> updatePrescription());
        panel.add(update);

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> deletePrescription());
        panel.add(delete);

        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> clearForm());
        panel.add(clear);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshData());
        panel.add(refresh);

        return panel;
    } 

    /* ================= CRUD ================= */

    private void addPrescription() {
        Prescription p = getPrescriptionFromForm();
        if (p != null) {
            controller.addPrescription(p);
            refreshData();
            clearForm();
        }
    }

    private void updatePrescription() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        controller.deletePrescription(tableModel.getValueAt(r, 0).toString());
        controller.addPrescription(getPrescriptionFromForm());
        refreshData();
        clearForm();
    }

    private void deletePrescription() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        controller.deletePrescription(tableModel.getValueAt(r, 0).toString());
        refreshData();
        clearForm();
    }

    private Prescription getPrescriptionFromForm() {
        if (prescriptionIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Prescription ID is required");
            return null;
        }

        return new Prescription(
                prescriptionIDField.getText().trim(),
                patientIDField.getText().trim(),
                clinicianIDField.getText().trim(),
                appointmentIDField.getText().trim(),
                medicationField.getText().trim(),
                dosageField.getText().trim(),
                frequencyField.getText().trim(),
                durationDaysField.getText().trim(),
                quantityField.getText().trim(),
                instructionsField.getText().trim(),
                pharmacyField.getText().trim(),
                datePrescribedField.getText().trim(),
                issueDateField.getText().trim(),
                collectionStatusField.getText().trim(),
                collectionDateField.getText().trim()
         );
        
    }

    private void loadSelectedPrescription() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        prescriptionIDField.setText(tableModel.getValueAt(r, 0).toString());
        patientIDField.setText(tableModel.getValueAt(r, 1).toString());
        clinicianIDField.setText(tableModel.getValueAt(r, 2).toString());
        appointmentIDField.setText(tableModel.getValueAt(r, 3).toString());
        medicationField.setText(tableModel.getValueAt(r, 4).toString());
        dosageField.setText(tableModel.getValueAt(r, 5).toString());
        frequencyField.setText(tableModel.getValueAt(r, 6).toString());
        durationDaysField.setText(tableModel.getValueAt(r, 7).toString());
        quantityField.setText(tableModel.getValueAt(r, 8).toString());
        instructionsField.setText(tableModel.getValueAt(r, 14).toString());
        pharmacyField.setText(tableModel.getValueAt(r, 9).toString());
        datePrescribedField.setText(tableModel.getValueAt(r, 10).toString());
        issueDateField.setText(tableModel.getValueAt(r, 11).toString());
        collectionStatusField.setText(tableModel.getValueAt(r, 13).toString());
        collectionDateField.setText(tableModel.getValueAt(r, 12).toString());
    }

    private void clearForm() {
        prescriptionIDField.setText("");
        patientIDField.setText("");
        clinicianIDField.setText("");
        appointmentIDField.setText("");
        medicationField.setText("");
        dosageField.setText("");
        frequencyField.setText("");
        durationDaysField.setText("");
        quantityField.setText("");
        instructionsField.setText("");
        pharmacyField.setText("");
        datePrescribedField.setText("");
        issueDateField.setText("");
        collectionStatusField.setText("");
        collectionDateField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Prescription p : controller.getAllPrescriptions()) {
            tableModel.addRow(new Object[]{
            p.getPrescriptionID(), p.getPatientID(), p.getClinicianID(),
            p.getAppointmentID(), p.getMedication(), p.getDosage(),
            p.getFrequency(), p.getDurationDays(), p.getQuantity(),
            p.getInstructions(),p.getPharmacy(), p.getDatePrescribed(),
            p.getIssueDate(),p.getCollectionStatus(),p.getCollectionDate(), 
            });
        }
    }
}
