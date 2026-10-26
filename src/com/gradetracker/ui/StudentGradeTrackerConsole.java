package com.gradetracker.ui;

import com.gradetracker.model.Student;
import com.gradetracker.service.GradeTracker;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Interactive Console-based User Interface for Student Grade Tracker.
 * Provides a menu-driven CLI with input validation and formatted reporting.
 */
public class StudentGradeTrackerConsole {
    private final GradeTracker tracker;
    private final Scanner scanner;

    public StudentGradeTrackerConsole(GradeTracker tracker) {
        this.tracker = tracker;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the console interaction loop.
     */
    public void start() {
        boolean running = true;
        printWelcomeBanner();

        while (running) {
            printMainMenu();
            String choice = promptString("Enter your choice [0-10]: ").trim();
            System.out.println();

            switch (choice) {
                case "1":
                    handleAddStudent();
                    break;
                case "2":
                    handleViewAllStudents();
                    break;
                case "3":
                    handleSearchStudent();
                    break;
                case "4":
                    handleUpdateStudent();
                    break;
                case "5":
                    handleDeleteStudent();
                    break;
                case "6":
                    handleViewSummaryStatistics();
                    break;
                case "7":
                    handleSortStudents();
                    break;
                case "8":
                    handleLoadSampleData();
                    break;
                case "9":
                    handleExportReport();
                    break;
                case "10":
                    handleLaunchGui();
                    break;
                case "0":
                    running = false;
                    System.out.println("Thank you for using Student Grade Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("[!] Invalid choice. Please choose a valid option (0-10).");
            }

            if (running && !choice.equals("10")) {
                pause();
            }
        }
    }

    private void printWelcomeBanner() {
        System.out.println("==========================================================================");
        System.out.println("                WELCOME TO STUDENT GRADE TRACKER (CLI)                    ");
        System.out.println("      Manage student scores, compute statistics, and generate reports     ");
        System.out.println("==========================================================================");
    }

    private void printMainMenu() {
        System.out.println("\n----------------------------- MAIN MENU ----------------------------------");
        System.out.printf(" Currently tracking: %d student(s)\n", tracker.getStudentCount());
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" [1]  Add New Student");
        System.out.println(" [2]  View All Students (Roster Table)");
        System.out.println(" [3]  Search Student (by ID or Name)");
        System.out.println(" [4]  Update Student Record");
        System.out.println(" [5]  Delete Student Record");
        System.out.println(" [6]  View Statistics & Summary Report");
        System.out.println(" [7]  Sort Students (Score / Name)");
        System.out.println(" [8]  Load Demo Sample Data (10 Students)");
        System.out.println(" [9]  Export Report to File (.txt / .csv)");
        System.out.println(" [10] Launch Modern Desktop GUI Mode");
        System.out.println(" [0]  Exit");
        System.out.println("--------------------------------------------------------------------------");
    }

    // =========================================================================
    // MENU HANDLERS
    // =========================================================================

    private void handleAddStudent() {
        System.out.println(">>> ADD NEW STUDENT");
        String id;
        while (true) {
            id = promptString("Enter Student ID (e.g., S101): ").trim();
            if (id.isEmpty()) {
                System.out.println("[!] ID cannot be empty. Please retry.");
                continue;
            }
            if (tracker.findStudentById(id) != null) {
                System.out.printf("[!] A student with ID '%s' already exists! Please use a unique ID.\n", id);
                return;
            }
            break;
        }

        String name;
        while (true) {
            name = promptString("Enter Student Full Name: ").trim();
            if (name.isEmpty()) {
                System.out.println("[!] Name cannot be empty.");
                continue;
            }
            break;
        }

        double score = promptDouble(String.format("Enter Score (%.1f - %.1f): ", Student.MIN_SCORE, Student.MAX_SCORE),
                Student.MIN_SCORE, Student.MAX_SCORE);

        try {
            Student s = new Student(id, name, score);
            tracker.addStudent(s);
            System.out.printf("[+] Student '%s' (ID: %s) added successfully with Grade %s (%s)!\n",
                    s.getName(), s.getId(), s.getLetterGrade(), s.getStatus());
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Failed to add student: " + e.getMessage());
        }
    }

    private void handleViewAllStudents() {
        System.out.println(">>> STUDENT ROSTER");
        List<Student> list = tracker.getAllStudents();
        if (list.isEmpty()) {
            System.out.println("[i] No students found. Tip: Choose option [8] to load demo data or [1] to add a student.");
            return;
        }
        renderTable(list);
    }

    private void handleSearchStudent() {
        System.out.println(">>> SEARCH STUDENT");
        System.out.println(" [1] Search by ID");
        System.out.println(" [2] Search by Name");
        String opt = promptString("Select search type [1-2]: ").trim();

        if (opt.equals("1")) {
            String id = promptString("Enter Student ID to find: ");
            Student s = tracker.findStudentById(id);
            if (s != null) {
                System.out.println("\n[+] Student Found:");
                renderSingleStudent(s);
            } else {
                System.out.printf("[!] No student found with ID '%s'.\n", id);
            }
        } else if (opt.equals("2")) {
            String query = promptString("Enter Name query: ");
            List<Student> results = tracker.searchByName(query);
            if (results.isEmpty()) {
                System.out.printf("[!] No students found matching '%s'.\n", query);
            } else {
                System.out.printf("\n[+] Found %d student(s):\n", results.size());
                renderTable(results);
            }
        } else {
            System.out.println("[!] Invalid option.");
        }
    }

    private void handleUpdateStudent() {
        System.out.println(">>> UPDATE STUDENT RECORD");
        String id = promptString("Enter Student ID to update: ");
        Student existing = tracker.findStudentById(id);
        if (existing == null) {
            System.out.printf("[!] Student with ID '%s' was not found.\n", id);
            return;
        }

        System.out.println("Current details:");
        renderSingleStudent(existing);

        String newName = promptString(String.format("Enter new Name (press Enter to keep '%s'): ", existing.getName())).trim();
        if (newName.isEmpty()) {
            newName = existing.getName();
        }

        String scoreStr = promptString(String.format("Enter new Score (press Enter to keep %.2f): ", existing.getScore())).trim();
        double newScore = existing.getScore();
        if (!scoreStr.isEmpty()) {
            try {
                double parsed = Double.parseDouble(scoreStr);
                if (parsed < Student.MIN_SCORE || parsed > Student.MAX_SCORE) {
                    System.out.printf("[!] Score must be between %.1f and %.1f. Keeping existing score.\n",
                            Student.MIN_SCORE, Student.MAX_SCORE);
                } else {
                    newScore = parsed;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid number format. Keeping existing score.");
            }
        }

        boolean updated = tracker.updateStudent(id, newName, newScore);
        if (updated) {
            System.out.printf("[+] Successfully updated student '%s' (ID: %s).\n", id, newName);
            renderSingleStudent(tracker.findStudentById(id));
        } else {
            System.out.println("[!] Update failed.");
        }
    }

    private void handleDeleteStudent() {
        System.out.println(">>> DELETE STUDENT RECORD");
        String id = promptString("Enter Student ID to delete: ");
        Student s = tracker.findStudentById(id);
        if (s == null) {
            System.out.printf("[!] Student with ID '%s' not found.\n", id);
            return;
        }

        System.out.printf("Are you sure you want to delete student '%s' (%s, Score: %.2f)?\n",
                s.getName(), s.getId(), s.getScore());
        String confirm = promptString("Type 'YES' to confirm deletion: ");
        if (confirm.equalsIgnoreCase("YES")) {
            boolean removed = tracker.removeStudent(id);
            if (removed) {
                System.out.printf("[+] Student '%s' has been deleted successfully.\n", id);
            } else {
                System.out.println("[!] Failed to delete student.");
            }
        } else {
            System.out.println("[i] Deletion cancelled.");
        }
    }

    private void handleViewSummaryStatistics() {
        System.out.println(tracker.generateSummaryReport());
    }

    private void handleSortStudents() {
        System.out.println(">>> SORT STUDENTS");
        System.out.println(" [1] Sort by Score: Highest to Lowest");
        System.out.println(" [2] Sort by Score: Lowest to Highest");
        System.out.println(" [3] Sort by Name: Alphabetical (A-Z)");
        String opt = promptString("Select sorting order [1-3]: ").trim();

        List<Student> list;
        if (opt.equals("1")) {
            list = tracker.getStudentsSortedByScore(true);
            System.out.println("\n[+] Students Sorted by Score (Highest First):");
            renderTable(list);
        } else if (opt.equals("2")) {
            list = tracker.getStudentsSortedByScore(false);
            System.out.println("\n[+] Students Sorted by Score (Lowest First):");
            renderTable(list);
        } else if (opt.equals("3")) {
            list = tracker.getStudentsSortedByName();
            System.out.println("\n[+] Students Sorted by Name (A-Z):");
            renderTable(list);
        } else {
            System.out.println("[!] Invalid sort option.");
        }
    }

    private void handleLoadSampleData() {
        tracker.loadSampleData();
        System.out.printf("[+] Successfully loaded %d sample student records!\n", tracker.getStudentCount());
        renderTable(tracker.getAllStudents());
    }

    private void handleExportReport() {
        System.out.println(">>> EXPORT REPORT");
        System.out.println(" [1] Export formatted Text Report (.txt)");
        System.out.println(" [2] Export spreadsheet CSV (.csv)");
        String opt = promptString("Choose export format [1-2]: ").trim();

        if (opt.equals("1")) {
            String filename = promptString("Enter file name (default: grade_summary_report.txt): ").trim();
            if (filename.isEmpty()) filename = "grade_summary_report.txt";
            if (!filename.endsWith(".txt")) filename += ".txt";
            File file = new File(filename);
            boolean ok = tracker.exportReportToFile(file);
            if (ok) {
                System.out.printf("[+] Summary report successfully saved to: %s\n", file.getAbsolutePath());
            } else {
                System.out.println("[!] Failed to save text report.");
            }
        } else if (opt.equals("2")) {
            String filename = promptString("Enter file name (default: students.csv): ").trim();
            if (filename.isEmpty()) filename = "students.csv";
            if (!filename.endsWith(".csv")) filename += ".csv";
            File file = new File(filename);
            boolean ok = tracker.exportToCsv(file);
            if (ok) {
                System.out.printf("[+] Student roster CSV successfully saved to: %s\n", file.getAbsolutePath());
            } else {
                System.out.println("[!] Failed to save CSV.");
            }
        } else {
            System.out.println("[!] Invalid option.");
        }
    }

    private void handleLaunchGui() {
        System.out.println("[i] Launching Graphical User Interface (GUI)...");
        try {
            javax.swing.SwingUtilities.invokeLater(() -> {
                StudentGradeTrackerGUI gui = new StudentGradeTrackerGUI(tracker);
                gui.setVisible(true);
            });
            System.out.println("[+] GUI window opened on your desktop.");
        } catch (Throwable t) {
            System.out.println("[!] Could not launch GUI: " + t.getMessage());
        }
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    private void renderTable(List<Student> list) {
        String subLine = "-".repeat(76);
        System.out.println(subLine);
        System.out.printf("%-10s | %-22s | %-7s | %-5s | %-4s | %-6s | %-12s\n",
                "ID", "Name", "Score", "Grade", "GPA", "Status", "Remarks");
        System.out.println(subLine);

        for (Student s : list) {
            System.out.printf("%-10s | %-22s | %6.2f | %-5s | %3.1f | %-6s | %-12s\n",
                    truncate(s.getId(), 10),
                    truncate(s.getName(), 22),
                    s.getScore(),
                    s.getLetterGrade(),
                    s.getGpa(),
                    s.getStatus(),
                    s.getRemarks()
            );
        }
        System.out.println(subLine);
        System.out.printf("Total Count: %d | Class Average: %.2f | Highest: %.2f | Lowest: %.2f\n",
                list.size(), tracker.calculateAverage(), tracker.getHighestScore(), tracker.getLowestScore());
    }

    private void renderSingleStudent(Student s) {
        System.out.println("+--------------------------------------------------+");
        System.out.printf("| Student ID   : %-33s |\n", s.getId());
        System.out.printf("| Full Name    : %-33s |\n", s.getName());
        System.out.printf("| Score        : %-33.2f |\n", s.getScore());
        System.out.printf("| Letter Grade : %-33s |\n", s.getLetterGrade());
        System.out.printf("| GPA (4.0)    : %-33.1f |\n", s.getGpa());
        System.out.printf("| Status       : %-33s |\n", s.getStatus());
        System.out.printf("| Remarks      : %-33s |\n", s.getRemarks());
        System.out.println("+--------------------------------------------------+");
    }

    private String promptString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private double promptDouble(String message, double min, double max) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(line);
                if (val < min || val > max) {
                    System.out.printf("[!] Value must be between %.1f and %.1f. Please re-enter.\n", min, max);
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid number format. Please enter a numerical score (e.g., 85.5).");
            }
        }
    }

    private void pause() {
        System.out.print("\nPress [Enter] to continue...");
        scanner.nextLine();
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 2) + "..";
    }
}
