package com.gradetracker.service;

import com.gradetracker.model.Student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service managing student grade collections, statistical computations,
 * and reporting.
 * Utilizes both ArrayLists and arrays to demonstrate comprehensive data management.
 */
public class GradeTracker {
    // Primary collection using ArrayList
    private final List<Student> students;

    public GradeTracker() {
        this.students = new ArrayList<>();
    }

    /**
     * Adds a student to the tracker.
     *
     * @param student Student to add
     * @return true if added, false if student with same ID already exists
     * @throws IllegalArgumentException if student is null
     */
    public boolean addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }
        if (findStudentById(student.getId()) != null) {
            return false; // Duplicate ID
        }
        return students.add(student);
    }

    /**
     * Convenience method to create and add a student.
     */
    public boolean addStudent(String id, String name, double score) {
        return addStudent(new Student(id, name, score));
    }

    /**
     * Updates an existing student's name and score.
     *
     * @param id       Existing student ID
     * @param newName  New name
     * @param newScore New score
     * @return true if updated, false if student not found
     */
    public boolean updateStudent(String id, String newName, double newScore) {
        Student student = findStudentById(id);
        if (student == null) {
            return false;
        }
        student.setName(newName);
        student.setScore(newScore);
        return true;
    }

    /**
     * Removes a student by ID.
     *
     * @param id Student ID to remove
     * @return true if removed, false if not found
     */
    public boolean removeStudent(String id) {
        if (id == null) return false;
        return students.removeIf(s -> s.getId().equalsIgnoreCase(id.trim()));
    }

    /**
     * Finds a student by their unique ID (case-insensitive).
     *
     * @param id Student ID
     * @return Student object or null if not found
     */
    public Student findStudentById(String id) {
        if (id == null) return null;
        for (Student s : students) {
            if (s.getId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Searches students by name query (case-insensitive substring).
     *
     * @param query Search query
     * @return List of matching students
     */
    public List<Student> searchByName(String query) {
        List<Student> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }
        String lowerQuery = query.trim().toLowerCase();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(lowerQuery)) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * Returns an unmodifiable view of all students.
     */
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    /**
     * Returns the total count of students currently managed.
     */
    public int getStudentCount() {
        return students.size();
    }

    /**
     * Clears all students.
     */
    public void clearAll() {
        students.clear();
    }

    // =========================================================================
    // ARRAY-BASED DATA EXTRACTION
    // Explicitly satisfies the requirement to use arrays as well as ArrayLists.
    // =========================================================================

    /**
     * Extracts all scores into a primitive double array.
     * Demonstrates array conversion and manipulation.
     *
     * @return double[] containing all scores
     */
    public double[] getScoresArray() {
        double[] scores = new double[students.size()];
        for (int i = 0; i < students.size(); i++) {
            scores[i] = students.get(i).getScore();
        }
        return scores;
    }

    /**
     * Extracts all student records into a Student array.
     *
     * @return Student[] array
     */
    public Student[] getStudentsArray() {
        return students.toArray(new Student[0]);
    }

    // =========================================================================
    // STATISTICAL CALCULATIONS
    // Required: Calculate average, highest, and lowest scores.
    // Plus: Median, Standard Deviation, Passing Rate, Grade Distribution.
    // =========================================================================

    /**
     * Calculates the arithmetic mean (average) score of all students.
     * Uses array operations to demonstrate array traversal.
     *
     * @return average score, or 0.0 if list is empty
     */
    public double calculateAverage() {
        double[] scores = getScoresArray();
        if (scores.length == 0) {
            return 0.0;
        }
        double sum = 0.0;
        for (double score : scores) {
            sum += score;
        }
        return Math.round((sum / scores.length) * 100.0) / 100.0;
    }

    /**
     * Finds the highest score among all students.
     *
     * @return highest score, or 0.0 if no students exist
     */
    public double getHighestScore() {
        double[] scores = getScoresArray();
        if (scores.length == 0) {
            return 0.0;
        }
        double max = scores[0];
        for (int i = 1; i < scores.length; i++) {
            if (scores[i] > max) {
                max = scores[i];
            }
        }
        return max;
    }

    /**
     * Finds the lowest score among all students.
     *
     * @return lowest score, or 0.0 if no students exist
     */
    public double getLowestScore() {
        double[] scores = getScoresArray();
        if (scores.length == 0) {
            return 0.0;
        }
        double min = scores[0];
        for (int i = 1; i < scores.length; i++) {
            if (scores[i] < min) {
                min = scores[i];
            }
        }
        return min;
    }

    /**
     * Returns all students who share the highest score.
     */
    public List<Student> getHighestScoringStudents() {
        List<Student> topStudents = new ArrayList<>();
        if (students.isEmpty()) return topStudents;

        double maxScore = getHighestScore();
        for (Student s : students) {
            if (Double.compare(s.getScore(), maxScore) == 0) {
                topStudents.add(s);
            }
        }
        return topStudents;
    }

    /**
     * Returns all students who share the lowest score.
     */
    public List<Student> getLowestScoringStudents() {
        List<Student> lowestStudents = new ArrayList<>();
        if (students.isEmpty()) return lowestStudents;

        double minScore = getLowestScore();
        for (Student s : students) {
            if (Double.compare(s.getScore(), minScore) == 0) {
                lowestStudents.add(s);
            }
        }
        return lowestStudents;
    }

    /**
     * Calculates the median score.
     */
    public double calculateMedian() {
        double[] scores = getScoresArray();
        if (scores.length == 0) {
            return 0.0;
        }
        double[] sorted = scores.clone();
        Arrays.sort(sorted);
        int mid = sorted.length / 2;
        if (sorted.length % 2 == 1) {
            return sorted[mid];
        } else {
            return Math.round(((sorted[mid - 1] + sorted[mid]) / 2.0) * 100.0) / 100.0;
        }
    }

    /**
     * Calculates standard deviation of scores.
     */
    public double calculateStandardDeviation() {
        double[] scores = getScoresArray();
        if (scores.length <= 1) {
            return 0.0;
        }
        double mean = calculateAverage();
        double sumSqDiff = 0.0;
        for (double score : scores) {
            sumSqDiff += Math.pow(score - mean, 2);
        }
        return Math.round(Math.sqrt(sumSqDiff / scores.length) * 100.0) / 100.0;
    }

    /**
     * Returns total number of passing students.
     */
    public int getPassingCount() {
        int count = 0;
        for (Student s : students) {
            if (s.isPassing()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns total number of failing students.
     */
    public int getFailingCount() {
        return students.size() - getPassingCount();
    }

    /**
     * Returns the passing rate percentage.
     */
    public double getPassingPercentage() {
        if (students.isEmpty()) return 0.0;
        double pct = ((double) getPassingCount() / students.size()) * 100.0;
        return Math.round(pct * 100.0) / 100.0;
    }

    /**
     * Returns a distribution map of Letter Grades to student count.
     */
    public Map<String, Integer> getGradeDistribution() {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        distribution.put("A", 0);
        distribution.put("B", 0);
        distribution.put("C", 0);
        distribution.put("D", 0);
        distribution.put("F", 0);

        for (Student s : students) {
            String grade = s.getLetterGrade();
            distribution.put(grade, distribution.getOrDefault(grade, 0) + 1);
        }
        return distribution;
    }

    /**
     * Returns a new sorted list of students.
     *
     * @param descending true for highest-to-lowest, false for lowest-to-highest
     */
    public List<Student> getStudentsSortedByScore(boolean descending) {
        List<Student> copy = new ArrayList<>(students);
        copy.sort((a, b) -> descending ? Double.compare(b.getScore(), a.getScore())
                                       : Double.compare(a.getScore(), b.getScore()));
        return copy;
    }

    /**
     * Returns students sorted alphabetically by name.
     */
    public List<Student> getStudentsSortedByName() {
        List<Student> copy = new ArrayList<>(students);
        copy.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        return copy;
    }

    // =========================================================================
    // REPORT GENERATION & EXPORT
    // Required: Display a summary report of all students.
    // =========================================================================

    /**
     * Generates a comprehensive, formatted text summary report.
     */
    public String generateSummaryReport() {
        StringBuilder sb = new StringBuilder();
        String line = "=".repeat(78);
        String subLine = "-".repeat(78);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        sb.append(line).append("\n");
        sb.append(String.format("%48s\n", "STUDENT GRADE TRACKER - SUMMARY REPORT"));
        sb.append(String.format("%45s\n", "Generated: " + timestamp));
        sb.append(line).append("\n\n");

        if (students.isEmpty()) {
            sb.append("No student records available.\n");
            sb.append(line).append("\n");
            return sb.toString();
        }

        // Summary Statistics Box
        sb.append("--- PERFORMANCE OVERVIEW ---\n");
        sb.append(String.format("Total Students Registered : %d\n", getStudentCount()));
        sb.append(String.format("Class Average Score       : %.2f / 100.0\n", calculateAverage()));
        sb.append(String.format("Class Median Score        : %.2f / 100.0\n", calculateMedian()));
        sb.append(String.format("Standard Deviation        : %.2f\n", calculateStandardDeviation()));
        sb.append(String.format("Highest Score             : %.2f  (Student: %s)\n",
                getHighestScore(), formatStudentNames(getHighestScoringStudents())));
        sb.append(String.format("Lowest Score              : %.2f  (Student: %s)\n",
                getLowestScore(), formatStudentNames(getLowestScoringStudents())));
        sb.append(String.format("Passing Count             : %d / %d (%.1f%%)\n",
                getPassingCount(), getStudentCount(), getPassingPercentage()));
        sb.append(String.format("Failing Count             : %d / %d (%.1f%%)\n",
                getFailingCount(), getStudentCount(), (100.0 - getPassingPercentage())));
        sb.append("\n");

        // Grade Distribution with Bar Chart
        sb.append("--- GRADE DISTRIBUTION ---\n");
        Map<String, Integer> dist = getGradeDistribution();
        int total = getStudentCount();
        for (Map.Entry<String, Integer> entry : dist.entrySet()) {
            String grade = entry.getKey();
            int count = entry.getValue();
            double pct = total > 0 ? ((double) count / total) * 100.0 : 0.0;
            int barLength = total > 0 ? (int) Math.round((pct / 100.0) * 30) : 0;
            String bar = "#".repeat(Math.max(0, barLength));
            sb.append(String.format("Grade %-2s : %2d students (%5.1f%%) | %-30s\n", grade, count, pct, bar));
        }
        sb.append("\n");

        // Complete Student Roster Table (sorted by Rank)
        sb.append("--- COMPLETE STUDENT ROSTER (RANKED) ---\n");
        sb.append(String.format("%-5s | %-10s | %-24s | %-7s | %-5s | %-4s | %-6s | %-12s\n",
                "Rank", "ID", "Name", "Score", "Grade", "GPA", "Status", "Remarks"));
        sb.append(subLine).append("\n");

        List<Student> ranked = getStudentsSortedByScore(true);
        int currentRank = 1;
        for (int i = 0; i < ranked.size(); i++) {
            Student s = ranked.get(i);
            // Handle tied rank
            if (i > 0 && Double.compare(s.getScore(), ranked.get(i - 1).getScore()) != 0) {
                currentRank = i + 1;
            }
            sb.append(String.format("#%-4d | %-10s | %-24s | %6.2f | %-5s | %3.1f | %-6s | %-12s\n",
                    currentRank,
                    truncate(s.getId(), 10),
                    truncate(s.getName(), 24),
                    s.getScore(),
                    s.getLetterGrade(),
                    s.getGpa(),
                    s.getStatus(),
                    s.getRemarks()
            ));
        }
        sb.append(line).append("\n");

        return sb.toString();
    }

    /**
     * Exports the summary report to a text file.
     */
    public boolean exportReportToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(generateSummaryReport());
            return true;
        } catch (IOException e) {
            System.err.println("Error saving report: " + e.getMessage());
            return false;
        }
    }

    /**
     * Exports the student list to CSV format for spreadsheets.
     */
    public boolean exportToCsv(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("ID,Name,Score,Letter_Grade,GPA,Status,Remarks\n");
            for (Student s : students) {
                writer.write(String.format("\"%s\",\"%s\",%.2f,\"%s\",%.1f,\"%s\",\"%s\"\n",
                        s.getId().replace("\"", "\"\""),
                        s.getName().replace("\"", "\"\""),
                        s.getScore(),
                        s.getLetterGrade(),
                        s.getGpa(),
                        s.getStatus(),
                        s.getRemarks()
                ));
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Prepopulates diverse realistic student records for testing & demonstration.
     */
    public void loadSampleData() {
        students.clear();
        addStudent(new Student("S101", "Emma Watson", 96.5));
        addStudent(new Student("S102", "Liam Johnson", 88.0));
        addStudent(new Student("S103", "Sophia Martinez", 92.5));
        addStudent(new Student("S104", "Noah Brown", 74.0));
        addStudent(new Student("S105", "Olivia Davis", 81.5));
        addStudent(new Student("S106", "Ethan Wilson", 58.0));
        addStudent(new Student("S107", "Ava Taylor", 65.5));
        addStudent(new Student("S108", "Lucas Anderson", 92.5)); // tied score
        addStudent(new Student("S109", "Mia Thomas", 45.0));
        addStudent(new Student("S110", "Oliver White", 79.0));
    }

    private String formatStudentNames(List<Student> list) {
        if (list.isEmpty()) return "N/A";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(list.get(i).getName()).append(" (").append(list.get(i).getId()).append(")");
        }
        return sb.toString();
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 2) + "..";
    }
}
