@echo off
setlocal
echo ========================================================
echo   Pushing Student Grade Tracker to GitHub
echo ========================================================
echo.

:: Ensure Git is in PATH
set "PATH=%LOCALAPPDATA%\Microsoft\WinGet\Packages\Git.MinGit_Microsoft.Winget.Source_8wekyb3d8bbwe\cmd;C:\Program Files\Git\cmd;%PATH%"

echo Checking remote configuration...
git remote -v
echo.

echo Pushing main branch to origin...
echo (If a GitHub sign-in window appears, please authorize or log in)
echo.
git push -u origin main

if %errorlevel% equ 0 (
    echo.
    echo ========================================================
    echo   SUCCESS: Project successfully uploaded to GitHub!
    echo ========================================================
) else (
    echo.
    echo ========================================================
    echo   Push failed or was cancelled. Check the message above.
    echo ========================================================
)

echo.
pause
