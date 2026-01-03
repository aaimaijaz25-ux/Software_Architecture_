package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/* Appointment Management Panel */

public class AppointmentPanel extends JPanel {

    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField appointmentIDField, patientIDField, clinicianIDField, facilityIDField;
    private JTextField dateField, timeField, durationField, typeField, statusField;
    private JTextField reasonField, notesField, createdDateField, lastModifiedField;

    public AppointmentPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));

        /* TABLE */
        String[] columns = {
                "Appointment ID", "Patient ID", "Clinician ID", "Facility ID",
                "Date", "Time", "Duration (mins)", "Type", "Status",
                "Reason", "Notes", "Created", "Last Modified"
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
            if (!e.getValueIsAdjusting()) loadSelectedAppointment();
        });

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(236, 239, 241));
        header.setForeground(new Color(55, 71, 79));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 13f));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Appointments"));
        add(scrollPane, BorderLayout.CENTER);

        /* FORM */
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));
        formPanel.setBackground(new Color(250, 250, 250));
        add(formPanel, BorderLayout.SOUTH);

        /* BUTTONS */
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setBackground(new Color(245, 247, 250));
        add(buttonPanel, BorderLayout.NORTH);
    }

        /* FORM */
        private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        addField(panel, gbc, row, 0, "Appointment ID:", appointmentIDField = new JTextField(15));
        addField(panel, gbc, row, 2, "Patient ID:", patientIDField = new JTextField(15));
        addField(panel, gbc, row, 4, "Clinician ID:", clinicianIDField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Facility ID:", facilityIDField = new JTextField(15));
        addField(panel, gbc, row, 2, "Date:", dateField = new JTextField(15));
        addField(panel, gbc, row, 4, "Time:", timeField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Duration (mins):", durationField = new JTextField(15));
        addField(panel, gbc, row, 2, "Type:", typeField = new JTextField(15));
        addField(panel, gbc, row, 4, "Status:", statusField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Reason:", reasonField = new JTextField(15));
        addField(panel, gbc, row, 2, "Notes:", notesField = new JTextField(15));
        addField(panel, gbc, row, 4, "Created Date:", createdDateField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Last Modified:", lastModifiedField = new JTextField(15));

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
        add.addActionListener(e -> addAppointment());
        panel.add(add);

        JButton update = new JButton("Update");
        update.addActionListener(e -> updateAppointment());
        panel.add(update);

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> deleteAppointment());
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

    private void addAppointment() {
        Appointment a = getAppointmentFromForm();
        if (a != null) {
            controller.addAppointment(a);
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment added successfully!");
        }
    }

    private void updateAppointment() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        controller.deleteAppointment(tableModel.getValueAt(r, 0).toString());
        controller.addAppointment(getAppointmentFromForm());
        refreshData();
        clearForm();
    }

    private void deleteAppointment() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        controller.deleteAppointment(tableModel.getValueAt(r, 0).toString());
        refreshData();
        clearForm();
    }

    private Appointment getAppointmentFromForm() {
        if (appointmentIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Appointment ID is required");
            return null;
        }

        return new Appointment(
            appointmentIDField.getText().trim(),
            patientIDField.getText().trim(),
            clinicianIDField.getText().trim(),
            facilityIDField.getText().trim(),
            dateField.getText().trim(),
            timeField.getText().trim(),
            durationField.getText().trim(),
            typeField.getText().trim(),
            statusField.getText().trim(),
            reasonField.getText().trim(),
            notesField.getText().trim(),
            createdDateField.getText().trim(),
            lastModifiedField.getText().trim()
        );
    }

    private void loadSelectedAppointment() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        appointmentIDField.setText(tableModel.getValueAt(r, 0).toString());
        patientIDField.setText(tableModel.getValueAt(r, 1).toString());
        clinicianIDField.setText(tableModel.getValueAt(r, 2).toString());
        facilityIDField.setText(tableModel.getValueAt(r, 3).toString());
        dateField.setText(tableModel.getValueAt(r, 4).toString());
        timeField.setText(tableModel.getValueAt(r, 5).toString());
        durationField.setText(tableModel.getValueAt(r, 6).toString());
        typeField.setText(tableModel.getValueAt(r, 7).toString());
        statusField.setText(tableModel.getValueAt(r, 8).toString());
        reasonField.setText(tableModel.getValueAt(r, 9).toString());
        notesField.setText(tableModel.getValueAt(r, 10).toString());
        createdDateField.setText(tableModel.getValueAt(r, 11).toString());
        lastModifiedField.setText(tableModel.getValueAt(r, 12).toString());
    }

    private void clearForm() {
        appointmentIDField.setText("");
        patientIDField.setText("");
        clinicianIDField.setText("");
        facilityIDField.setText("");
        dateField.setText("");
        timeField.setText("");
        durationField.setText("");
        typeField.setText("");
        statusField.setText("");
        reasonField.setText("");
        notesField.setText("");
        createdDateField.setText("");
        lastModifiedField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Appointment a : controller.getAllAppointments()) {
            tableModel.addRow(new Object[]{
                a.getAppointmentID(), a.getPatientID(), a.getClinicianID(),
                a.getFacilityID(), a.getDate(), a.getTime(),
                a.getDurationMinutes(), a.getAppointmentType(),
                a.getStatus(), a.getReason(), a.getNotes(),
                a.getCreatedDate(), a.getLastModified()
            });
        }
    }
}