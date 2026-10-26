package com.gradetracker.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an individual student and their academic score.
 * Encapsulates student data and grade calculation logic.
 */
public class Student implements Serializable, Comparable<Student> {
    private static final long serialVersionUID = 1L;

    public static final double MIN_SCORE = 0.0;
    public static final double MAX_SCORE = 100.0;
    public static final double PASSING_SCORE = 60.0;

    private String id;
    private String name;
    private double score;

    /**
     * Constructs a new Student instance.
     *
     * @param id    Unique identifier for the student (e.g. S101)
     * @param name  Full name of the student
     * @param score Numerical score between 0.0 and 100.0
     * @throws IllegalArgumentException if id/name is invalid or score out of range
     */
    public Student(String id, String name, double score) {
        setId(id);
        setName(name);
        setScore(score);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty.");
        }
        this.id = id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        this.name = name.trim();
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException(
                String.format("Score must be between %.1f and %.1f. Received: %.2f", MIN_SCORE, MAX_SCORE, score)
            );
        }
        this.score = Math.round(score * 100.0) / 100.0; // round to 2 decimal places
    }

    /**
     * Determines the letter grade based on standard grading scale.
     *
     * @return Letter grade (A, B, C, D, F)
     */
    public String getLetterGrade() {
        if (score >= 90.0) return "A";
        if (score >= 80.0) return "B";
        if (score >= 70.0) return "C";
        if (score >= 60.0) return "D";
        return "F";
    }

    /**
     * Computes the Grade Point Average (GPA) on a 4.0 scale.
     *
     * @return GPA value (0.0 to 4.0)
     */
    public double getGpa() {
        if (score >= 90.0) return 4.0;
        if (score >= 80.0) return 3.0;
        if (score >= 70.0) return 2.0;
        if (score >= 60.0) return 1.0;
        return 0.0;
    }

    /**
     * Checks if the student meets or exceeds the passing threshold.
     *
     * @return true if score >= PASSING_SCORE, false otherwise
     */
    public boolean isPassing() {
        return score >= PASSING_SCORE;
    }

    /**
     * Returns the student's passing status.
     *
     * @return "PASS" or "FAIL"
     */
    public String getStatus() {
        return isPassing() ? "PASS" : "FAIL";
    }

    /**
     * Returns qualitative remarks based on the score.
     *
     * @return Remarks string
     */
    public String getRemarks() {
        if (score >= 90.0) return "Outstanding";
        if (score >= 80.0) return "Very Good";
        if (score >= 70.0) return "Good";
        if (score >= 60.0) return "Satisfactory";
        return "Needs Improvement";
    }

    /**
     * Natural ordering: sorts descending by score, then ascending by name.
     */
    @Override
    public int compareTo(Student other) {
        if (other == null) return 1;
        int scoreCompare = Double.compare(other.score, this.score);
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id.toUpperCase(), student.id.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.toUpperCase());
    }

    @Override
    public String toString() {
        return String.format(
            "Student[ID=%s, Name=%s, Score=%.2f, Grade=%s, Status=%s]",
            id, name, score, getLetterGrade(), getStatus()
        );
    }
}
