@echo off
title CraveDash Frontend Web Server (Port 3000)
color 0A

echo ===============================================================================
echo     CRAVEDASH MOBILITY SERVICES • LOCALHOST WEB SERVER (:3000)
echo ===============================================================================
echo.

cd /d "%~dp0frontend"

:: Check for python
where python >nul 2>&1
if %errorlevel% equ 0 (
    timeout /t 1 /nobreak >nul
    start "" http://localhost:3000
    echo [*] Starting Python HTTP Server on http://localhost:3000...
    python -m http.server 3000
    goto :eof
)

if exist "E:\python 3.14\python.exe" (
    timeout /t 1 /nobreak >nul
    start "" http://localhost:3000
    echo [*] Starting Python HTTP Server on http://localhost:3000...
    "E:\python 3.14\python.exe" -m http.server 3000
    goto :eof
)

where py >nul 2>&1
if %errorlevel% equ 0 (
    timeout /t 1 /nobreak >nul
    start "" http://localhost:3000
    echo [*] Starting Python HTTP Server on http://localhost:3000...
    py -m http.server 3000
    goto :eof
)

where npx >nul 2>&1
if %errorlevel% equ 0 (
    timeout /t 1 /nobreak >nul
    start "" http://localhost:3000
    echo [*] Starting via npx serve...
    npx -y serve -l 3000
    goto :eof
)

echo [!] Python or Node not found in PATH. Opening index.html directly in browser...
start "" "%~dp0frontend\index.html"
pause
