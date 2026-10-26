# 🎓 TASK 1: Student Grade Tracker (Java)

A modular and feature-rich Java application to input, manage, analyze, and report student academic grades.

This project fulfills all requirements of **Task 1: Student Grade Tracker** with dual interface options: a **Command-Line Interface (CLI)** and a **Graphical User Interface (GUI)** with dashboard cards, table view, search filters, and report generation.

---

## 📋 Features & Requirements Checklist

| Requirement | Implementation Details | Status |
| :--- | :--- | :---: |
| **Input & Manage Student Grades** | Full CRUD operations: Add, Edit, Delete, Search students with ID, Name, and Score (0.0 to 100.0) and validation. | ✅ Complete |
| **Calculate Average, Highest, Lowest** | Accurate computations of Class Average, Highest Score & Top Student(s), Lowest Score & Student(s), plus Median and Standard Deviation. | ✅ Complete |
| **Use Arrays or ArrayLists** | Employs `ArrayList<Student>` for dynamic collection management and primitive `double[]` / `Student[]` arrays for calculations and data extraction. | ✅ Complete |
| **Display Summary Report** | Formatted report featuring performance KPIs, letter grade distribution with visual bar chart, and ranked student roster. Exportable to `.txt` and `.csv`. | ✅ Complete |
| **Console or GUI Interface** | **Both provided!** An interactive ANSI/text Console menu and a Java Swing desktop GUI. | ✅ Complete |

---

## 🏗️ Project Architecture

```
task1/
├── src/
│   └── com/
│       └── gradetracker/
│           ├── model/
│           │   └── Student.java                   # Student entity with validation, grade & GPA logic
│           ├── service/
│           │   └── GradeTracker.java              # Core business logic, ArrayList/array operations, stats & reporting
│           ├── ui/
│           │   ├── StudentGradeTrackerConsole.java # Interactive CLI menu system
│           │   └── StudentGradeTrackerGUI.java     # Modern Swing GUI with KPI cards and table
│           ├── test/
│           │   └── GradeTrackerTest.java          # 54-assertion standalone automated test suite
│           └── Main.java                          # Unified application launcher (GUI & CLI)
├── bin/                                           # Compiled .class files
├── build.bat                                      # One-click Windows build script
├── run-gui.bat                                    # One-click launcher for GUI Desktop mode
├── run-console.bat                                # One-click launcher for CLI Console mode
├── run-tests.bat                                  # One-click launcher for test suite
└── README.md                                      # Documentation & Guide
```

---

## 📊 Grading Scale & Metric Formulas

- **A** : $90.0 \le \text{Score} \le 100.0$ (GPA: 4.0 | Remarks: Outstanding)
- **B** : $80.0 \le \text{Score} < 90.0$ (GPA: 3.0 | Remarks: Very Good)
- **C** : $70.0 \le \text{Score} < 80.0$ (GPA: 2.0 | Remarks: Good)
- **D** : $60.0 \le \text{Score} < 70.0$ (GPA: 1.0 | Remarks: Satisfactory)
- **F** : $\text{Score} < 60.0$ (GPA: 0.0 | Remarks: Needs Improvement - Failing)

### Calculations Implemented
1. **Average Score**:
   $$\mu = \frac{1}{N} \sum_{i=1}^{N} \text{score}_i$$
2. **Highest & Lowest**: Linear search scanning the score array to find maximum and minimum values, supporting tied winners.
3. **Median Score**: Middle value computed from sorted primitive array.
4. **Standard Deviation**:
   $$\sigma = \sqrt{\frac{1}{N} \sum_{i=1}^{N} (\text{score}_i - \mu)^2}$$
5. **Passing Rate**: Percentage of students with $\text{score} \ge 60.0$.

---

## 🚀 How to Run

### Option 1: Quick Launch Using Windows Batch Files
Double-click any of the provided batch files:
- **`run-gui.bat`**: Launches the desktop Graphical User Interface.
- **`run-console.bat`**: Launches the interactive Command-Line Interface.
- **`run-tests.bat`**: Runs the 54 automated verification tests.
- **`build.bat`**: Compiles all Java source files into `bin/`.

---

### Option 2: Command Line (PowerShell / Command Prompt)

#### 1. Compile the Source Code:
```bash
javac -d bin src/com/gradetracker/model/*.java src/com/gradetracker/service/*.java src/com/gradetracker/ui/*.java src/com/gradetracker/test/*.java src/com/gradetracker/Main.java
```

#### 2. Launch Desktop GUI Mode:
```bash
java -cp bin com.gradetracker.Main --gui
```
*(Or simply run `java -cp bin com.gradetracker.Main`)*

#### 3. Launch CLI Console Mode:
```bash
java -cp bin com.gradetracker.Main --console
```

#### 4. Run Automated Verification Tests:
```bash
java -cp bin com.gradetracker.test.GradeTrackerTest
```

---

## 🖥️ User Interfaces Preview

### 1. Graphical User Interface (GUI)
- **Header Banner**: Clean navy header with quick "View Full Report" button.
- **Top KPI Cards**: 
  - 👥 Total Students
  - 📊 Class Average Score
  - 🏆 Highest Score & Top Student
  - ⚠️ Lowest Score & Lowest Student
- **Input Form**: Clean input for ID, Full Name, and Score with auto-suggest ID, validation, and demo data loader.
- **Table View**: Dynamic `JTable` with color-coded Grade and Status badges (Pass/Fail) and live search/filter bar.
- **Summary Dialog**: Modal pop-up with full formatted statistical report, clipboard copy, and file export options.

### 2. Command-Line Interface (CLI)
```text
==========================================================================
                WELCOME TO STUDENT GRADE TRACKER (CLI)                    
      Manage student scores, compute statistics, and generate reports     
==========================================================================

----------------------------- MAIN MENU ----------------------------------
 Currently tracking: 10 student(s)
--------------------------------------------------------------------------
 [1]  Add New Student
 [2]  View All Students (Roster Table)
 [3]  Search Student (by ID or Name)
 [4]  Update Student Record
 [5]  Delete Student Record
 [6]  View Statistics & Summary Report
 [7]  Sort Students (Score / Name)
 [8]  Load Demo Sample Data (10 Students)
 [9]  Export Report to File (.txt / .csv)
 [10] Launch Modern Desktop GUI Mode
 [0]  Exit
--------------------------------------------------------------------------
```

---

## 🧪 Verification & Test Results

The standalone test suite ([GradeTrackerTest.java](file:///c:/Users/NAITIK/OneDrive/Desktop/task1/src/com/gradetracker/test/GradeTrackerTest.java)) validates:
- Model validation (empty names, out-of-range scores $<0$ and $>100$).
- Grade boundary classification (A, B, C, D, F) and GPA calculation.
- Dynamic addition, modification, duplicate prevention, and deletion.
- Average, highest, and lowest score computations.
- Tied top/lowest scores.
- Even and odd median computations.
- Standard deviation calculation.
- Array conversions (`getScoresArray()` and `getStudentsArray()`).
- Grade distribution tally.
- Empty collection safety (gracefully handles zero students without errors).

**All 54 assertions pass with 100% success rate.**
