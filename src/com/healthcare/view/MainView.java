package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import javax.swing.*;
import java.awt.*;

/* Main View class - Main GUI window with tabs for different entities */
public class MainView extends JFrame {

    private HealthcareController controller;
    private JTabbedPane tabbedPane;

    private PatientPanel patientPanel;
    private ClinicianPanel clinicianPanel;
    private FacilityPanel facilityPanel;
    private AppointmentPanel appointmentPanel;
    private PrescriptionPanel prescriptionPanel;
    private ReferralPanel referralPanel;
    private StaffPanel staffPanel;

    public MainView(HealthcareController controller) {
        this.controller = controller;
        initializeGUI();
        refreshAllPanels();
    }

    private void initializeGUI() {

        setTitle("Healthcare Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 820);
        setLocationRelativeTo(null);

        // App background
        getContentPane().setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(getWidth(), 110));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color (0, 150, 136));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Healthcare Management System");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Gerogia", Font.BOLD, 30));

        JLabel subTitleLabel = new JLabel(
        "Patient, Clinician & Clinical Operations Management Platform"
        );
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subTitleLabel.setForeground(new Color(230, 245, 230));
        subTitleLabel.setFont(new Font("Gerogia", Font.PLAIN, 14));

        header.add(titleLabel);
        header.add(Box.createVerticalStrut(8));
        header.add(subTitleLabel);

        add(header, BorderLayout.NORTH);

        // MENU 
        createMenuBar();

        // TABS 
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Gerogia", Font.PLAIN, 14));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));

        patientPanel = new PatientPanel(controller);
        clinicianPanel = new ClinicianPanel(controller);
        facilityPanel = new FacilityPanel(controller);
        appointmentPanel = new AppointmentPanel(controller);
        prescriptionPanel = new PrescriptionPanel(controller);
        referralPanel = new ReferralPanel(controller);
        staffPanel = new StaffPanel(controller);

        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Clinicians", clinicianPanel);
        tabbedPane.addTab("Facilities", facilityPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        tabbedPane.addTab("Prescriptions", prescriptionPanel);
        tabbedPane.addTab("Referrals", referralPanel);
        tabbedPane.addTab("Staff", staffPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

        //  MENU BAR 
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem loadMenuItem = new JMenuItem("Load Data");
        loadMenuItem.addActionListener(e -> loadData());

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        fileMenu.add(loadMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

        // LOAD DATA 
    private void loadData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String dataDirectory = fileChooser.getSelectedFile().getAbsolutePath();
            controller.loadData(dataDirectory);
            refreshAllPanels();
            JOptionPane.showMessageDialog(
                    this,
                    "Data loaded successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

        //  REFRESH 
    public void refreshAllPanels() {
        patientPanel.refreshData();
        clinicianPanel.refreshData();
        facilityPanel.refreshData();
        appointmentPanel.refreshData();
        prescriptionPanel.refreshData();
        referralPanel.refreshData();
        staffPanel.refreshData();
    }
}