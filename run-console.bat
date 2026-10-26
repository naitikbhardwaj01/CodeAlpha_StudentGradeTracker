@echo off
title Student Grade Tracker - CLI Console
if not exist bin\com\gradetracker\Main.class (
    echo [i] Binaries not found. Compiling first...
    if not exist bin mkdir bin
    javac -d bin src\com\gradetracker\model\*.java src\com\gradetracker\service\*.java src\com\gradetracker\ui\*.java src\com\gradetracker\test\*.java src\com\gradetracker\Main.java
)
java -cp bin com.gradetracker.Main --console
pause
