@echo off
title CraveDash Frontend Server (Port 3000)
cd /d "%~dp0frontend"
echo ===================================================
echo   Starting CraveDash Web Frontend on Port 3000...
echo ===================================================
echo.
start "" "http://localhost:3000"
"E:\python 3.14\python.exe" -m http.server 3000
pause
