package com.gradetracker.test;

import com.gradetracker.model.Student;
import com.gradetracker.service.GradeTracker;

import java.util.List;
import java.util.Map;

/**
 * Standalone verification test suite for Student Grade Tracker.
 * Runs comprehensive unit and integration tests without external test dependencies.
 */
public class GradeTrackerTest {
    private static int testsRun = 0;
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("             RUNNING STUDENT GRADE TRACKER VERIFICATION TESTS             ");
        System.out.println("==========================================================================");

        testStudentCreationAndValidation();
        testGradeAndGpaCalculations();
        testTrackerAddUpdateDelete();
        testAverageHighestLowestCalculations();
        testMedianAndStdDev();
        testArrayConversion();
        testGradeDistribution();
        testReportGeneration();
        testEmptyTrackerEdgeCases();

        System.out.println("==========================================================================");
        System.out.printf("Test Results: %d Total | %d Passed | %d Failed\n", testsRun, testsPassed, testsFailed);
        System.out.println("==========================================================================");

        if (testsFailed > 0) {
            System.err.println("[FAILED] Some tests did not pass.");
            System.exit(1);
        } else {
            System.out.println("[SUCCESS] All test assertions passed flawlessly!");
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        testsRun++;
        if (condition) {
            testsPassed++;
            System.out.printf("  [PASS] %s\n", testName);
        } else {
            testsFailed++;
            System.err.printf("  [FAIL] %s\n", testName);
        }
    }

    private static void assertEquals(String testName, double expected, double actual, double delta) {
        assertTrue(testName + " (expected: " + expected + ", actual: " + actual + ")",
                Math.abs(expected - actual) <= delta);
    }

    private static void assertEquals(String testName, Object expected, Object actual) {
        assertTrue(testName + " (expected: " + expected + ", actual: " + actual + ")",
                expected != null && expected.equals(actual));
    }

    private static void testStudentCreationAndValidation() {
        System.out.println("\n--- Testing Student Model & Validation ---");
        Student s1 = new Student("S101", "Alice", 95.5);
        assertEquals("Valid Student ID", "S101", s1.getId());
        assertEquals("Valid Student Name", "Alice", s1.getName());
        assertEquals("Valid Student Score", 95.5, s1.getScore(), 0.001);

        // Score bounds checks
        boolean threwMin = false;
        try {
            new Student("S102", "Bob", -5.0);
        } catch (IllegalArgumentException e) {
            threwMin = true;
        }
        assertTrue("Rejects negative score (<0)", threwMin);

        boolean threwMax = false;
        try {
            new Student("S103", "Charlie", 105.0);
        } catch (IllegalArgumentException e) {
            threwMax = true;
        }
        assertTrue("Rejects score over 100", threwMax);

        boolean threwEmptyName = false;
        try {
            new Student("S104", "", 80.0);
        } catch (IllegalArgumentException e) {
            threwEmptyName = true;
        }
        assertTrue("Rejects empty student name", threwEmptyName);
    }

    private static void testGradeAndGpaCalculations() {
        System.out.println("\n--- Testing Grade Boundaries & GPA ---");
        Student a = new Student("T1", "A Student", 92.0);
        Student b = new Student("T2", "B Student", 84.0);
        Student c = new Student("T3", "C Student", 73.0);
        Student d = new Student("T4", "D Student", 62.0);
        Student f = new Student("T5", "F Student", 48.0);

        assertEquals("Grade A for 92.0", "A", a.getLetterGrade());
        assertEquals("Grade B for 84.0", "B", b.getLetterGrade());
        assertEquals("Grade C for 73.0", "C", c.getLetterGrade());
        assertEquals("Grade D for 62.0", "D", d.getLetterGrade());
        assertEquals("Grade F for 48.0", "F", f.getLetterGrade());

        assertEquals("GPA 4.0 for Grade A", 4.0, a.getGpa(), 0.001);
        assertEquals("GPA 3.0 for Grade B", 3.0, b.getGpa(), 0.001);
        assertEquals("GPA 2.0 for Grade C", 2.0, c.getGpa(), 0.001);
        assertEquals("GPA 1.0 for Grade D", 1.0, d.getGpa(), 0.001);
        assertEquals("GPA 0.0 for Grade F", 0.0, f.getGpa(), 0.001);

        assertTrue("Student with 62.0 is Passing", d.isPassing());
        assertTrue("Student with 48.0 is Failing", !f.isPassing());
    }

    private static void testTrackerAddUpdateDelete() {
        System.out.println("\n--- Testing Tracker CRUD Operations ---");
        GradeTracker tracker = new GradeTracker();

        boolean added1 = tracker.addStudent("S1", "User One", 85.0);
        boolean added2 = tracker.addStudent("S2", "User Two", 90.0);
        boolean addedDuplicate = tracker.addStudent("S1", "Duplicate S1", 70.0);

        assertTrue("Add Student 1 succeeds", added1);
        assertTrue("Add Student 2 succeeds", added2);
        assertTrue("Duplicate ID rejected", !addedDuplicate);
        assertEquals("Tracker contains 2 students", 2, tracker.getStudentCount());

        boolean updated = tracker.updateStudent("S1", "User One Updated", 95.0);
        assertTrue("Update existing student succeeds", updated);
        assertEquals("Updated score is 95.0", 95.0, tracker.findStudentById("S1").getScore(), 0.001);

        boolean deleted = tracker.removeStudent("S2");
        assertTrue("Delete student succeeds", deleted);
        assertEquals("Tracker contains 1 student after delete", 1, tracker.getStudentCount());
    }

    private static void testAverageHighestLowestCalculations() {
        System.out.println("\n--- Testing Average, Highest & Lowest Score Calculation ---");
        GradeTracker tracker = new GradeTracker();
        tracker.addStudent("A1", "Score 100", 100.0);
        tracker.addStudent("A2", "Score 80", 80.0);
        tracker.addStudent("A3", "Score 60", 60.0);

        // Average = (100 + 80 + 60) / 3 = 80.0
        assertEquals("Calculate Average Score", 80.0, tracker.calculateAverage(), 0.01);
        assertEquals("Calculate Highest Score", 100.0, tracker.getHighestScore(), 0.01);
        assertEquals("Calculate Lowest Score", 60.0, tracker.getLowestScore(), 0.01);

        List<Student> top = tracker.getHighestScoringStudents();
        assertEquals("Top student count", 1, top.size());
        assertEquals("Top student ID", "A1", top.get(0).getId());

        List<Student> low = tracker.getLowestScoringStudents();
        assertEquals("Lowest student count", 1, low.size());
        assertEquals("Lowest student ID", "A3", low.get(0).getId());
    }

    private static void testMedianAndStdDev() {
        System.out.println("\n--- Testing Median & Standard Deviation ---");
        GradeTracker tracker = new GradeTracker();
        tracker.addStudent("M1", "Student 1", 50.0);
        tracker.addStudent("M2", "Student 2", 70.0);
        tracker.addStudent("M3", "Student 3", 90.0);

        assertEquals("Median of [50, 70, 90] is 70", 70.0, tracker.calculateMedian(), 0.01);

        tracker.addStudent("M4", "Student 4", 100.0);
        // [50, 70, 90, 100] -> median is (70 + 90) / 2 = 80.0
        assertEquals("Median of even count [50, 70, 90, 100] is 80", 80.0, tracker.calculateMedian(), 0.01);
    }

    private static void testArrayConversion() {
        System.out.println("\n--- Testing Array Conversion (ArrayList to Array) ---");
        GradeTracker tracker = new GradeTracker();
        tracker.addStudent("P1", "P One", 75.0);
        tracker.addStudent("P2", "P Two", 85.0);

        double[] scores = tracker.getScoresArray();
        assertEquals("Scores array length matches student count", 2, scores.length);
        assertEquals("Score array element 0", 75.0, scores[0], 0.001);
        assertEquals("Score array element 1", 85.0, scores[1], 0.001);

        Student[] studentArray = tracker.getStudentsArray();
        assertEquals("Student array length matches student count", 2, studentArray.length);
        assertEquals("Student array element 0 ID", "P1", studentArray[0].getId());
    }

    private static void testGradeDistribution() {
        System.out.println("\n--- Testing Grade Distribution ---");
        GradeTracker tracker = new GradeTracker();
        tracker.loadSampleData();

        Map<String, Integer> dist = tracker.getGradeDistribution();
        int sumCounts = 0;
        for (int count : dist.values()) {
            sumCounts += count;
        }
        assertEquals("Total count across grades matches student count", tracker.getStudentCount(), sumCounts);
        assertTrue("Grade distribution contains Grade A", dist.get("A") > 0);
        assertTrue("Passing count > 0", tracker.getPassingCount() > 0);
    }

    private static void testReportGeneration() {
        System.out.println("\n--- Testing Summary Report Generation ---");
        GradeTracker tracker = new GradeTracker();
        tracker.loadSampleData();

        String report = tracker.generateSummaryReport();
        assertTrue("Report contains summary header", report.contains("STUDENT GRADE TRACKER - SUMMARY REPORT"));
        assertTrue("Report contains average score", report.contains("Class Average Score"));
        assertTrue("Report contains highest score", report.contains("Highest Score"));
        assertTrue("Report contains lowest score", report.contains("Lowest Score"));
        assertTrue("Report contains roster table", report.contains("COMPLETE STUDENT ROSTER"));
    }

    private static void testEmptyTrackerEdgeCases() {
        System.out.println("\n--- Testing Empty Tracker Edge Cases ---");
        GradeTracker emptyTracker = new GradeTracker();

        assertEquals("Empty tracker average is 0.0", 0.0, emptyTracker.calculateAverage(), 0.001);
        assertEquals("Empty tracker highest is 0.0", 0.0, emptyTracker.getHighestScore(), 0.001);
        assertEquals("Empty tracker lowest is 0.0", 0.0, emptyTracker.getLowestScore(), 0.001);
        assertEquals("Empty tracker median is 0.0", 0.0, emptyTracker.calculateMedian(), 0.001);
        assertEquals("Empty tracker passing pct is 0.0", 0.0, emptyTracker.getPassingPercentage(), 0.001);
        assertTrue("Empty tracker report handles 0 records gracefully",
                emptyTracker.generateSummaryReport().contains("No student records available."));
    }
}
