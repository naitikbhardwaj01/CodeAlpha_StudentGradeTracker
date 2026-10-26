@echo off
echo ==========================================================================
echo                COMPILING STUDENT GRADE TRACKER
echo ==========================================================================
if not exist bin mkdir bin

javac -d bin src\com\gradetracker\model\*.java src\com\gradetracker\service\*.java src\com\gradetracker\ui\*.java src\com\gradetracker\test\*.java src\com\gradetracker\Main.java

if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilation finished without errors.
    echo Compiled class files are located in 'bin\' directory.
) else (
    echo [ERROR] Compilation failed. Please check the errors above.
)
pause
