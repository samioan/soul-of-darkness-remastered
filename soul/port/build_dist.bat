@echo off
setlocal
call "%~dp0build.bat" Release
if errorlevel 1 exit /b 1
cmake --install "%~dp0build\Release" --prefix "%~dp0dist"
if errorlevel 1 exit /b 1
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0check_dist.ps1"
exit /b %errorlevel%
