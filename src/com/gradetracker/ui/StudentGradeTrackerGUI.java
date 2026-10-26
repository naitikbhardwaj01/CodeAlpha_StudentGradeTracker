package com.gradetracker.ui;

import com.gradetracker.model.Student;
import com.gradetracker.service.GradeTracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.List;

/**
 * Modern Graphical User Interface for the Student Grade Tracker.
 * Built with Java Swing, featuring KPI dashboard stat cards, responsive table,
 * input validation, and full reporting capabilities.
 */
public class StudentGradeTrackerGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    // Primary Colors
    private static final Color COLOR_PRIMARY = new Color(79, 70, 229);      // Indigo 600
    private static final Color COLOR_PRIMARY_DARK = new Color(67, 56, 202); // Indigo 700
    private static final Color COLOR_HEADER = new Color(15, 23, 42);        // Slate 900
    private static final Color COLOR_BG = new Color(248, 250, 252);         // Slate 50
    private static final Color COLOR_CARD_BG = Color.WHITE;
    private static final Color COLOR_BORDER = new Color(226, 232, 240);     // Slate 200
    private static final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);
    private static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);

    private final GradeTracker tracker;

    // Dashboard KPI Labels
    private JLabel lblTotalStudents;
    private JLabel lblAverageScore;
    private JLabel lblHighestScore;
    private JLabel lblLowestScore;

    // Form Components
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtScore;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnClear;
    private JButton btnLoadDemo;

    // Table Components
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField txtSearch;

    // Bottom Action Buttons & Status
    private JLabel lblStatus;

    public StudentGradeTrackerGUI(GradeTracker tracker) {
        this.tracker = tracker;
        initializeUI();
        refreshAll();
    }

    private void initializeUI() {
        setTitle("Student Grade Tracker - Academic Analytics");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);

        // Main layout container
        JPanel rootPanel = new JPanel(new BorderLayout(0, 0));
        rootPanel.setBackground(COLOR_BG);

        // 1. Header Banner
        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Center Content Area (Dashboard KPIs + Split View: Form & Table)
        JPanel contentPanel = new JPanel(new BorderLayout(0, 12));
        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Top KPI Cards
        contentPanel.add(createKpiPanel(), BorderLayout.NORTH);

        // Center split (Left: Input Form, Right: Student Table)
        JPanel workspacePanel = new JPanel(new BorderLayout(16, 0));
        workspacePanel.setBackground(COLOR_BG);
        workspacePanel.add(createFormPanel(), BorderLayout.WEST);
        workspacePanel.add(createTablePanel(), BorderLayout.CENTER);

        contentPanel.add(workspacePanel, BorderLayout.CENTER);

        rootPanel.add(contentPanel, BorderLayout.CENTER);

        // 3. Bottom Status Bar & Export Actions
        rootPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    // =========================================================================
    // UI BUILDER METHODS
    // =========================================================================

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_HEADER);
        header.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel titleLabel = new JLabel("🎓 Student Grade Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Manage student records, calculate class performance metrics & generate reports");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(148, 163, 184)); // Slate 400

        JPanel textStack = new JPanel(new GridLayout(2, 1, 0, 4));
        textStack.setOpaque(false);
        textStack.add(titleLabel);
        textStack.add(subtitleLabel);

        header.add(textStack, BorderLayout.WEST);

        // Quick action button on header
        JButton btnQuickReport = createStyledButton("📋 View Full Report", COLOR_PRIMARY, Color.WHITE);
        btnQuickReport.addActionListener(e -> showSummaryReportDialog());
        header.add(btnQuickReport, BorderLayout.EAST);

        return header;
    }

    private JPanel createKpiPanel() {
        JPanel kpiContainer = new JPanel(new GridLayout(1, 4, 14, 0));
        kpiContainer.setOpaque(false);

        lblTotalStudents = new JLabel("0", SwingConstants.CENTER);
        lblAverageScore = new JLabel("0.0", SwingConstants.CENTER);
        lblHighestScore = new JLabel("0.0", SwingConstants.CENTER);
        lblLowestScore = new JLabel("0.0", SwingConstants.CENTER);

        kpiContainer.add(createStatCard("Total Students", lblTotalStudents, new Color(59, 130, 246), "👥"));
        kpiContainer.add(createStatCard("Class Average", lblAverageScore, new Color(99, 102, 241), "📊"));
        kpiContainer.add(createStatCard("Highest Score", lblHighestScore, new Color(16, 185, 129), "🏆"));
        kpiContainer.add(createStatCard("Lowest Score", lblLowestScore, new Color(239, 68, 68), "⚠️"));

        return kpiContainer;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor, String icon) {
        JPanel card = new JPanel(new BorderLayout(8, 6));
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));

        JLabel titleLbl = new JLabel(icon + " " + title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLbl.setForeground(COLOR_TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createFormPanel() {
        JPanel formCard = new JPanel(new BorderLayout(0, 14));
        formCard.setPreferredSize(new Dimension(300, 400));
        formCard.setBackground(COLOR_CARD_BG);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));

        // Form Title
        JLabel formTitle = new JLabel("📝 Student Information");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(COLOR_TEXT_MAIN);
        formCard.add(formTitle, BorderLayout.NORTH);

        // Fields
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 1, 0, 6));
        fieldsPanel.setOpaque(false);

        JLabel lblId = new JLabel("Student ID (Unique):");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtId = new JTextField();
        txtId.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblName = new JLabel("Student Full Name:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblScore = new JLabel("Score / Marks (0 - 100):");
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtScore = new JTextField();
        txtScore.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        fieldsPanel.add(lblId);
        fieldsPanel.add(txtId);
        fieldsPanel.add(lblName);
        fieldsPanel.add(txtName);
        fieldsPanel.add(lblScore);
        fieldsPanel.add(txtScore);

        formCard.add(fieldsPanel, BorderLayout.CENTER);

        // Button actions
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 1, 0, 8));
        buttonsPanel.setOpaque(false);

        btnAdd = createStyledButton("➕ Add Student", COLOR_PRIMARY, Color.WHITE);
        btnUpdate = createStyledButton("✏️ Update Selected", new Color(14, 116, 144), Color.WHITE);
        btnClear = createStyledButton("🧹 Clear Form", new Color(241, 245, 249), COLOR_TEXT_MAIN);
        btnLoadDemo = createStyledButton("⚡ Load Demo Data (10)", new Color(243, 244, 246), new Color(55, 65, 81));

        btnAdd.addActionListener(e -> onAddStudent());
        btnUpdate.addActionListener(e -> onUpdateStudent());
        btnClear.addActionListener(e -> clearForm());
        btnLoadDemo.addActionListener(e -> onLoadDemoData());

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnUpdate);
        buttonsPanel.add(btnClear);
        buttonsPanel.add(btnLoadDemo);

        formCard.add(buttonsPanel, BorderLayout.SOUTH);

        return formCard;
    }

    private JPanel createTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout(0, 10));
        tableContainer.setBackground(COLOR_CARD_BG);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));

        // Top bar: Search box and action buttons
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);

        JPanel searchBox = new JPanel(new BorderLayout(6, 0));
        searchBox.setOpaque(false);
        JLabel searchIcon = new JLabel("🔍 Search:");
        searchIcon.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(220, 30));
        txtSearch.setToolTipText("Filter by Name, ID, or Grade...");
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });

        searchBox.add(searchIcon, BorderLayout.WEST);
        searchBox.add(txtSearch, BorderLayout.CENTER);

        JPanel tableActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        tableActions.setOpaque(false);

        JButton btnDelete = createStyledButton("🗑️ Delete Selected", new Color(239, 68, 68), Color.WHITE);
        btnDelete.addActionListener(e -> onDeleteSelected());

        JButton btnClearAll = createStyledButton("Clear All", new Color(243, 244, 246), new Color(153, 27, 27));
        btnClearAll.addActionListener(e -> onClearAll());

        tableActions.add(btnDelete);
        tableActions.add(btnClearAll);

        topBar.add(searchBox, BorderLayout.WEST);
        topBar.add(tableActions, BorderLayout.EAST);

        tableContainer.add(topBar, BorderLayout.NORTH);

        // Table Model & JTable
        String[] columns = {"ID", "Student Name", "Score", "Grade", "GPA", "Status", "Remarks"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only directly in table, use form for updates
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(32);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        studentTable.getTableHeader().setBackground(new Color(241, 245, 249));
        studentTable.getTableHeader().setForeground(COLOR_TEXT_MAIN);
        studentTable.setGridColor(COLOR_BORDER);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        rowSorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(rowSorter);

        // Custom Cell Renderers for Grade and Status badges
        studentTable.getColumnModel().getColumn(3).setCellRenderer(new GradeBadgeRenderer());
        studentTable.getColumnModel().getColumn(5).setCellRenderer(new StatusBadgeRenderer());

        // Center align Score and GPA
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Selection Listener: populate form when a row is selected
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = studentTable.convertRowIndexToModel(selectedRow);
                    String id = (String) tableModel.getValueAt(modelRow, 0);
                    String name = (String) tableModel.getValueAt(modelRow, 1);
                    Object scoreVal = tableModel.getValueAt(modelRow, 2);

                    txtId.setText(id);
                    txtId.setEditable(false); // ID cannot be altered during edit
                    txtName.setText(name);
                    txtScore.setText(String.valueOf(scoreVal));
                    setStatus("Selected student: " + name + " (" + id + ")");
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(new LineBorder(COLOR_BORDER, 1));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        return tableContainer;
    }

    private JPanel createBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
                new EmptyBorder(10, 20, 10, 20)
        ));

        lblStatus = new JLabel("Ready. Start by adding student grades or loading demo data.");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(COLOR_TEXT_MUTED);

        JPanel actionGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionGroup.setOpaque(false);

        JButton btnExportTxt = createStyledButton("💾 Export Report (.txt)", COLOR_PRIMARY, Color.WHITE);
        btnExportTxt.addActionListener(e -> onExportTxtReport());

        JButton btnExportCsv = createStyledButton("📊 Export CSV", new Color(16, 185, 129), Color.WHITE);
        btnExportCsv.addActionListener(e -> onExportCsv());

        actionGroup.add(btnExportTxt);
        actionGroup.add(btnExportCsv);

        bottom.add(lblStatus, BorderLayout.WEST);
        bottom.add(actionGroup, BorderLayout.EAST);

        return bottom;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // =========================================================================
    // EVENT ACTIONS & HANDLERS
    // =========================================================================

    private void onAddStudent() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String scoreStr = txtScore.getText().trim();

        if (id.isEmpty()) {
            showError("Student ID is required.");
            txtId.requestFocus();
            return;
        }

        if (tracker.findStudentById(id) != null) {
            showError("Student ID '" + id + "' already exists. Please choose a unique ID.");
            txtId.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            showError("Student Name cannot be empty.");
            txtName.requestFocus();
            return;
        }

        double score;
        try {
            score = Double.parseDouble(scoreStr);
            if (score < Student.MIN_SCORE || score > Student.MAX_SCORE) {
                showError(String.format("Score must be between %.1f and %.1f.", Student.MIN_SCORE, Student.MAX_SCORE));
                txtScore.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid numeric score (e.g., 85.5).");
            txtScore.requestFocus();
            return;
        }

        Student s = new Student(id, name, score);
        tracker.addStudent(s);
        clearForm();
        refreshAll();
        setStatus("Successfully added student: " + s.getName() + " (" + s.getId() + ")");
    }

    private void onUpdateStudent() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            showError("Please select a student from the table to update.");
            return;
        }

        Student existing = tracker.findStudentById(id);
        if (existing == null) {
            showError("Student ID '" + id + "' does not exist.");
            return;
        }

        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            showError("Student Name cannot be empty.");
            return;
        }

        double score;
        try {
            score = Double.parseDouble(txtScore.getText().trim());
            if (score < Student.MIN_SCORE || score > Student.MAX_SCORE) {
                showError(String.format("Score must be between %.1f and %.1f.", Student.MIN_SCORE, Student.MAX_SCORE));
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid numeric score.");
            return;
        }

        tracker.updateStudent(id, name, score);
        clearForm();
        refreshAll();
        setStatus("Updated student record: " + name + " (" + id + ")");
    }

    private void onDeleteSelected() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a student record from the table to delete.");
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(selectedRow);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        String name = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete student: " + name + " (" + id + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tracker.removeStudent(id);
            clearForm();
            refreshAll();
            setStatus("Deleted student: " + name + " (" + id + ")");
        }
    }

    private void onClearAll() {
        if (tracker.getStudentCount() == 0) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear ALL " + tracker.getStudentCount() + " student records?\nThis cannot be undone.",
                "Confirm Clear All",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tracker.clearAll();
            clearForm();
            refreshAll();
            setStatus("All student records cleared.");
        }
    }

    private void onLoadDemoData() {
        tracker.loadSampleData();
        clearForm();
        refreshAll();
        setStatus("Loaded 10 sample student records.");
    }

    private void clearForm() {
        txtId.setText("");
        txtId.setEditable(true);
        txtName.setText("");
        txtScore.setText("");
        studentTable.clearSelection();
        suggestNextId();
    }

    private void suggestNextId() {
        int count = tracker.getStudentCount() + 1;
        String suggested = String.format("S%03d", count);
        // Ensure not already used
        while (tracker.findStudentById(suggested) != null) {
            count++;
            suggested = String.format("S%03d", count);
        }
        txtId.setText(suggested);
    }

    private void filterTable() {
        String query = txtSearch.getText().trim();
        if (query.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }

    private void refreshAll() {
        // 1. Refresh Table Model
        tableModel.setRowCount(0);
        List<Student> list = tracker.getAllStudents();
        for (Student s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getScore(),
                    s.getLetterGrade(),
                    s.getGpa(),
                    s.getStatus(),
                    s.getRemarks()
            });
        }

        // 2. Refresh KPI Cards
        int total = tracker.getStudentCount();
        lblTotalStudents.setText(String.valueOf(total));

        if (total > 0) {
            lblAverageScore.setText(String.format("%.1f", tracker.calculateAverage()));

            List<Student> top = tracker.getHighestScoringStudents();
            String topName = top.isEmpty() ? "" : " (" + top.get(0).getName() + ")";
            lblHighestScore.setText(String.format("%.1f%s", tracker.getHighestScore(), topName));

            List<Student> low = tracker.getLowestScoringStudents();
            String lowName = low.isEmpty() ? "" : " (" + low.get(0).getName() + ")";
            lblLowestScore.setText(String.format("%.1f%s", tracker.getLowestScore(), lowName));
        } else {
            lblAverageScore.setText("0.0");
            lblHighestScore.setText("0.0");
            lblLowestScore.setText("0.0");
        }

        suggestNextId();
    }

    private void setStatus(String msg) {
        lblStatus.setText(msg);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    // =========================================================================
    // REPORT MODAL DIALOG
    // =========================================================================

    private void showSummaryReportDialog() {
        JDialog dialog = new JDialog(this, "Academic Summary Report", true);
        dialog.setSize(750, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));
        panel.setBackground(COLOR_BG);

        JTextArea textArea = new JTextArea(tracker.generateSummaryReport());
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(textArea);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JButton btnCopy = createStyledButton("📋 Copy to Clipboard", COLOR_PRIMARY, Color.WHITE);
        btnCopy.addActionListener(e -> {
            StringSelection sel = new StringSelection(textArea.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
            JOptionPane.showMessageDialog(dialog, "Summary report copied to clipboard!");
        });

        JButton btnSave = createStyledButton("💾 Save to File", new Color(16, 185, 129), Color.WHITE);
        btnSave.addActionListener(e -> {
            onExportTxtReport();
            dialog.dispose();
        });

        JButton btnClose = createStyledButton("Close", new Color(241, 245, 249), COLOR_TEXT_MAIN);
        btnClose.addActionListener(e -> dialog.dispose());

        actions.add(btnCopy);
        actions.add(btnSave);
        actions.add(btnClose);

        panel.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private void onExportTxtReport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("student_grade_report.txt"));
        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            boolean ok = tracker.exportReportToFile(file);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Report saved to: " + file.getAbsolutePath());
                setStatus("Report saved to " + file.getName());
            } else {
                showError("Could not save report file.");
            }
        }
    }

    private void onExportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("students.csv"));
        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            boolean ok = tracker.exportToCsv(file);
            if (ok) {
                JOptionPane.showMessageDialog(this, "CSV exported to: " + file.getAbsolutePath());
                setStatus("CSV exported to " + file.getName());
            } else {
                showError("Could not export CSV file.");
            }
        }
    }

    // =========================================================================
    // CUSTOM TABLE CELL RENDERERS
    // =========================================================================

    private static class GradeBadgeRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));

            String grade = String.valueOf(value);
            if (!isSelected) {
                switch (grade) {
                    case "A":
                        label.setForeground(new Color(22, 163, 74)); // Green 600
                        break;
                    case "B":
                        label.setForeground(new Color(37, 99, 235)); // Blue 600
                        break;
                    case "C":
                        label.setForeground(new Color(202, 138, 4)); // Yellow 600
                        break;
                    case "D":
                        label.setForeground(new Color(217, 119, 6)); // Amber 600
                        break;
                    case "F":
                        label.setForeground(new Color(220, 38, 38)); // Red 600
                        break;
                    default:
                        label.setForeground(COLOR_TEXT_MAIN);
                }
            }
            return label;
        }
    }

    private static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));

            String status = String.valueOf(value);
            if (!isSelected) {
                if ("PASS".equalsIgnoreCase(status)) {
                    label.setForeground(new Color(22, 163, 74));
                } else {
                    label.setForeground(new Color(220, 38, 38));
                }
            }
            return label;
        }
    }
}
