@echo off
title Student Grade Tracker - Automated Tests
if not exist bin\com\gradetracker\test\GradeTrackerTest.class (
    echo [i] Binaries not found. Compiling first...
    if not exist bin mkdir bin
    javac -d bin src\com\gradetracker\model\*.java src\com\gradetracker\service\*.java src\com\gradetracker\ui\*.java src\com\gradetracker\test\*.java src\com\gradetracker\Main.java
)
java -cp bin com.gradetracker.test.GradeTrackerTest
pause
