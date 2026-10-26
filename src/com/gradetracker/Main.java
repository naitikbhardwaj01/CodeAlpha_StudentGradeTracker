package com.gradetracker;

import com.gradetracker.service.GradeTracker;
import com.gradetracker.ui.StudentGradeTrackerConsole;
import com.gradetracker.ui.StudentGradeTrackerGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Main application launcher for Student Grade Tracker.
 * Supports both Graphical User Interface (GUI) and Command-Line Interface (CLI).
 */
public class Main {
    public static void main(String[] args) {
        // Parse command line arguments
        boolean forceConsole = false;
        boolean forceGui = false;

        for (String arg : args) {
            if ("--console".equalsIgnoreCase(arg) || "-c".equalsIgnoreCase(arg)) {
                forceConsole = true;
            } else if ("--gui".equalsIgnoreCase(arg) || "-g".equalsIgnoreCase(arg)) {
                forceGui = true;
            } else if ("--help".equalsIgnoreCase(arg) || "-h".equalsIgnoreCase(arg)) {
                printHelp();
                return;
            }
        }

        GradeTracker tracker = new GradeTracker();

        // Check if environment is headless (no display / remote server)
        boolean isHeadless = GraphicsEnvironment.isHeadless();

        if (forceConsole || (isHeadless && !forceGui)) {
            if (isHeadless && forceGui) {
                System.err.println("[!] Warning: Cannot launch GUI in headless environment. Defaulting to Console mode.");
            }
            launchConsole(tracker);
        } else {
            // Default to GUI when a graphical desktop is available
            launchGuiWithFallback(tracker);
        }
    }

    private static void launchGuiWithFallback(GradeTracker tracker) {
        try {
            // Attempt to apply system look and feel for native OS appearance
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fall back to default Swing look and feel
            }

            System.out.println("==========================================================================");
            System.out.println("              STUDENT GRADE TRACKER - DESKTOP GUI LAUNCHED                ");
            System.out.println("==========================================================================");
            System.out.println("[i] The graphical desktop application is now active.");
            System.out.println("[i] Tip: To run in Console/CLI mode instead, use:");
            System.out.println("         java -cp bin com.gradetracker.Main --console");
            System.out.println("==========================================================================");

            SwingUtilities.invokeLater(() -> {
                StudentGradeTrackerGUI gui = new StudentGradeTrackerGUI(tracker);
                gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gui.setVisible(true);
            });
        } catch (Throwable t) {
            System.err.println("[!] Failed to launch GUI: " + t.getMessage());
            System.out.println("[i] Automatically falling back to Console mode...\n");
            launchConsole(tracker);
        }
    }

    private static void launchConsole(GradeTracker tracker) {
        StudentGradeTrackerConsole console = new StudentGradeTrackerConsole(tracker);
        console.start();
    }

    private static void printHelp() {
        System.out.println("==========================================================================");
        System.out.println("                     STUDENT GRADE TRACKER - HELP                         ");
        System.out.println("==========================================================================");
        System.out.println("Usage:");
        System.out.println("  java -cp bin com.gradetracker.Main [OPTIONS]\n");
        System.out.println("Options:");
        System.out.println("  --gui, -g       Launch Graphical User Interface (GUI) [Default if display available]");
        System.out.println("  --console, -c   Launch Command-Line Interface (CLI / Console mode)");
        System.out.println("  --help, -h      Display this help message and exit\n");
        System.out.println("Examples:");
        System.out.println("  java -cp bin com.gradetracker.Main --gui");
        System.out.println("  java -cp bin com.gradetracker.Main --console");
        System.out.println("==========================================================================");
    }
}
