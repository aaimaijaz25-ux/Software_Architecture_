package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Facility;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Facility Management Panel 
 */
public class FacilityPanel extends JPanel {

    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField facilityIDField, nameField, typeField, addressField, postcodeField;
    private JTextField phoneField, emailField, openingHoursField, managerField, servicesField, capacityField;

    public FacilityPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250)); 

        /* ================= TABLE ================= */
        String[] columns = {
        "Facility ID", "Name", "Type", "Address", "Postcode", "Phone", "Email", "Opening Hours", "Manager", "Services", "Capacity"
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
            if (!e.getValueIsAdjusting()) loadSelectedFacility();
        });

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(236, 239, 241));
        header.setForeground(new Color(55, 71, 79));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 13f));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Facilities"));
        add(scrollPane, BorderLayout.CENTER);

        /* ================= FORM ================= */
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Facility Details"));
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

        addField(panel, gbc, row, 0, "Facility ID:", facilityIDField = new JTextField(15));
        addField(panel, gbc, row, 2, "Name:", nameField = new JTextField(15));
        addField(panel, gbc, row, 4, "Type:", typeField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Address:", addressField = new JTextField(15));
        addField(panel, gbc, row, 2, "Postcode:", postcodeField = new JTextField(15));
        addField(panel, gbc, row, 4, "Phone:", phoneField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Email:", emailField = new JTextField(15));
        addField(panel, gbc, row, 2, "Opening Hours:", openingHoursField = new JTextField(15));
        addField(panel, gbc, row, 4, "Manager:", managerField = new JTextField(15));

        row++;
        addField(panel, gbc, row, 0, "Services:", servicesField = new JTextField(15));
        addField(panel, gbc, row, 2, "Capacity:", capacityField = new JTextField(15));

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

    /* ================= BUTTON PANEL ================= */

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton add = new JButton("Add");
        add.addActionListener(e -> addFacility());
        panel.add(add);

        JButton update = new JButton("Update");
        update.addActionListener(e -> updateFacility());
        panel.add(update);

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> deleteFacility());
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

    private void addFacility() {
        Facility f = getFacilityFromForm();
        if (f != null) {
            controller.addFacility(f);
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Facility added successfully!");
        }
    }

    private void updateFacility() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        controller.deleteFacility(tableModel.getValueAt(r, 0).toString());
        controller.addFacility(getFacilityFromForm());
        refreshData();
        clearForm();
    }

    private void deleteFacility() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        controller.deleteFacility(tableModel.getValueAt(r, 0).toString());
        refreshData();
        clearForm();
    }

    private Facility getFacilityFromForm() {
        if (facilityIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Facility ID is required");
            return null;
        }

        return new Facility(
        facilityIDField.getText().trim(),
        nameField.getText().trim(),
        typeField.getText().trim(),
        addressField.getText().trim(),
        postcodeField.getText().trim(),
        phoneField.getText().trim(),
        emailField.getText().trim(),
        openingHoursField.getText().trim(),
        managerField.getText().trim(),
        servicesField.getText().trim(),
        capacityField.getText().trim()
        );
    }

    private void loadSelectedFacility() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        facilityIDField.setText(tableModel.getValueAt(r, 0).toString());
        nameField.setText(tableModel.getValueAt(r, 1).toString());
        typeField.setText(tableModel.getValueAt(r, 2).toString());
        addressField.setText(tableModel.getValueAt(r, 3).toString());
        postcodeField.setText(tableModel.getValueAt(r, 4).toString());
        phoneField.setText(tableModel.getValueAt(r, 5).toString());
        emailField.setText(tableModel.getValueAt(r, 6).toString());
        openingHoursField.setText(tableModel.getValueAt(r, 7).toString());
        managerField.setText(tableModel.getValueAt(r, 8).toString());
        servicesField.setText(tableModel.getValueAt(r, 9).toString());
        capacityField.setText(tableModel.getValueAt(r, 10).toString());
    }

    private void clearForm() {
        facilityIDField.setText("");
        nameField.setText("");
        typeField.setText("");
        addressField.setText("");
        postcodeField.setText("");
        phoneField.setText("");
        emailField.setText("");
        openingHoursField.setText("");
        managerField.setText("");
        servicesField.setText("");
        capacityField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Facility> list = controller.getAllFacilities();
        for (Facility f : list) {
            tableModel.addRow(new Object[]{
                f.getFacilityID(), f.getName(), f.getType(), f.getAddress(),
                f.getPostcode(), f.getPhone(), f.getEmail(),
                f.getOpeningHours(), f.getManagerName(),
                f.getServices(), f.getCapacity()
            });
        }
    }
}