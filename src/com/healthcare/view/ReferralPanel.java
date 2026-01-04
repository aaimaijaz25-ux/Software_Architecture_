package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Referral;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/* Referral Management Panel */
public class ReferralPanel extends JPanel {

    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField referralIDField, patientIDField, referringClinicianIDField, receivingClinicianIDField;
    private JTextField referringFacilityIDField, receivingFacilityIDField, referralDateField, urgencyLevelField;
    private JTextField referralReasonField, clinicalSummaryField, requestedInvestigationsField;
    private JTextField appointmentIDField, notesField, statusField, createdDateField, lastUpdatedField;

    public ReferralPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));

        // Table
        String[] columns = {
                "Referral ID", "Patient ID", "Referring Clinician ID", "Receiving Clinician ID",
                "Referring Facility ID", "Receiving Facility ID", "Referral Date", "Urgency Level",
                "Referral Reason", "Clinical Summary", "Requested Investigations",
                "Appointment ID", "Notes", "Status", "Created Date", "Last Updated"
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
            if (!e.getValueIsAdjusting()) loadSelectedReferral();
        });

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(236, 239, 241));
        header.setForeground(new Color(55, 71, 79));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 13f));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Referrals"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Referral Details"));
        formPanel.setBackground(new Color(250, 250, 250));
        add(formPanel, BorderLayout.SOUTH);

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
        add(panel, gbc, row, 0, "Referral ID:", referralIDField = new JTextField(15));
        add(panel, gbc, row, 2, "Patient ID:", patientIDField = new JTextField(15));
        add(panel, gbc, row++, 4, "Referring Clinician ID:", referringClinicianIDField = new JTextField(15));

        // Row 2
        add(panel, gbc, row, 0, "Receiving Clinician ID:", receivingClinicianIDField = new JTextField(15));
        add(panel, gbc, row, 2, "Referring Facility ID:", referringFacilityIDField = new JTextField(15));
        add(panel, gbc, row++, 4, "Receiving Facility ID:", receivingFacilityIDField = new JTextField(15));
      
        // Row 3
        add(panel, gbc, row, 0, "Referral Date:", referralDateField = new JTextField(15));
        add(panel, gbc, row, 2, "Urgency Level:", urgencyLevelField = new JTextField(15));
        add(panel, gbc, row++, 4, "Referral Reason:", referralReasonField = new JTextField(15));

        // Row 4
        add(panel, gbc, row, 0, "Clinical Summary:", clinicalSummaryField = new JTextField(15));
        add(panel, gbc, row, 2, "Requested Investigations:", requestedInvestigationsField = new JTextField(15));
        add(panel, gbc, row++, 4, "Appointment ID:", appointmentIDField = new JTextField(15));

        // Row 5
        add(panel, gbc, row, 0, "Notes:", notesField = new JTextField(15));
        add(panel, gbc, row, 2, "Status:", statusField = new JTextField(15));
        add(panel, gbc, row++, 4, "Created Date:", createdDateField = new JTextField(15));

        // Row 6
        add(panel, gbc, row, 0, "Last Updated:", lastUpdatedField = new JTextField(15));

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
        add.addActionListener(e -> addReferral());
        panel.add(add);

        JButton update = new JButton("Update");
        update.addActionListener(e -> updateReferral());
        panel.add(update);

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> deleteReferral());
        panel.add(delete);
        
        JButton generatefile = new JButton("Generate File");
        generatefile.addActionListener(e -> generateReferralFile());
        panel.add(generatefile);

        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> clearForm());
        panel.add(clear);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshData());
        panel.add(refresh);

        return panel;
    } 
    

    private void addReferral() {
        Referral r = createReferralFromForm();
        if (r != null) {
            controller.addReferral(r);
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Referral added!");
        }
    }

    private void updateReferral() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        controller.deleteReferral((String) tableModel.getValueAt(row, 0));
        controller.addReferral(createReferralFromForm());
        refreshData();
        clearForm();
    }

    private void deleteReferral() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        controller.deleteReferral((String) tableModel.getValueAt(row, 0));
        refreshData();
        clearForm();
    }
    
    private void generateReferralFile() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a referral to generate the report.",
                    "No Referral Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Create Referral object from selected row
        Referral referral = new Referral(
                (String) tableModel.getValueAt(selectedRow, 0),  // Referral ID
                (String) tableModel.getValueAt(selectedRow, 1),  // Patient ID
                (String) tableModel.getValueAt(selectedRow, 2),  // Referring Clinician
                (String) tableModel.getValueAt(selectedRow, 3),  // Receiving Clinician
                (String) tableModel.getValueAt(selectedRow, 4),  // Referring Facility
                (String) tableModel.getValueAt(selectedRow, 5),  // Receiving Facility
                (String) tableModel.getValueAt(selectedRow, 6),  // Referral Date
                (String) tableModel.getValueAt(selectedRow, 7),  // Urgency
                (String) tableModel.getValueAt(selectedRow, 8),  // Reason
                (String) tableModel.getValueAt(selectedRow, 9),  // Clinical Summary
                (String) tableModel.getValueAt(selectedRow, 10), // Investigations
                (String) tableModel.getValueAt(selectedRow, 11), // Appointment ID
                (String) tableModel.getValueAt(selectedRow, 12), // Notes
                (String) tableModel.getValueAt(selectedRow, 13), // Status
                (String) tableModel.getValueAt(selectedRow, 14), // Created Date
                (String) tableModel.getValueAt(selectedRow, 15)  // Last Updated
        );

        // Output file name
        String fileName = "referral_" + referral.getReferralID() + ".txt";

        // Generate file using ReferralManager
        com.healthcare.referral.ReferralManager
                .getInstance()
                .generateReferralFile(referral, fileName);

        JOptionPane.showMessageDialog(
                this,
                "Referral report generated successfully:\n" + fileName,
                "File Generated",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    private Referral createReferralFromForm() {
        return new Referral(
                referralIDField.getText().trim(),
                patientIDField.getText().trim(),
                referringClinicianIDField.getText().trim(),
                receivingClinicianIDField.getText().trim(),
                referringFacilityIDField.getText().trim(),
                receivingFacilityIDField.getText().trim(),
                referralDateField.getText().trim(),
                urgencyLevelField.getText().trim(),
                referralReasonField.getText().trim(),
                clinicalSummaryField.getText().trim(),
                requestedInvestigationsField.getText().trim(),
                appointmentIDField.getText().trim(),
                notesField.getText().trim(),
                statusField.getText().trim(),
                createdDateField.getText().trim(),
                lastUpdatedField.getText().trim()
        );
    }

    private void loadSelectedReferral() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        referralIDField.setText((String) tableModel.getValueAt(r, 0));
        patientIDField.setText((String) tableModel.getValueAt(r, 1));
        referringClinicianIDField.setText((String) tableModel.getValueAt(r, 2));
        receivingClinicianIDField.setText((String) tableModel.getValueAt(r, 3));
        referringFacilityIDField.setText((String) tableModel.getValueAt(r, 4));
        receivingFacilityIDField.setText((String) tableModel.getValueAt(r, 5));
        referralDateField.setText((String) tableModel.getValueAt(r, 6));
        urgencyLevelField.setText((String) tableModel.getValueAt(r, 7));
        referralReasonField.setText((String) tableModel.getValueAt(r, 8));
        clinicalSummaryField.setText((String) tableModel.getValueAt(r, 9));
        requestedInvestigationsField.setText((String) tableModel.getValueAt(r, 10));
        appointmentIDField.setText((String) tableModel.getValueAt(r, 11));
        notesField.setText((String) tableModel.getValueAt(r, 12));
        statusField.setText((String) tableModel.getValueAt(r, 13));
        createdDateField.setText((String) tableModel.getValueAt(r, 14));
        lastUpdatedField.setText((String) tableModel.getValueAt(r, 15));
    }

    private void clearForm() {
        for (JTextField f : new JTextField[]{
                referralIDField, patientIDField, referringClinicianIDField, receivingClinicianIDField,
                referringFacilityIDField, receivingFacilityIDField, referralDateField, urgencyLevelField,
                referralReasonField, clinicalSummaryField, requestedInvestigationsField,
                appointmentIDField, notesField, statusField, createdDateField, lastUpdatedField
        }) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Referral r : controller.getAllReferrals()) {
            tableModel.addRow(new Object[]{
                    r.getReferralID(), r.getPatientID(),
                    r.getReferringClinicianID(), r.getReceivingClinicianID(),
                    r.getReferringFacilityID(), r.getReceivingFacilityID(),
                    r.getreferralDate(), r.getUrgencyLevel(), r.getReferralReason(),
                    r.getClinicalSummary(), r.getRequestedInvestigations(),
                    r.getAppointmentID(), r.getNotes(), r.getStatus(),
                    r.getCreatedDate(), r.getLastUpdated()
            });
        }
    }
}
