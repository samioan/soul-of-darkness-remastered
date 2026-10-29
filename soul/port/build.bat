@echo off
setlocal
call "%~dp0vcvars.bat"
if errorlevel 1 exit /b 1
set "CONFIG=%~1"
if not defined CONFIG set "CONFIG=Debug"
if /I not "%CONFIG%"=="Debug" if /I not "%CONFIG%"=="Release" exit /b 2
cmake -G Ninja -S "%~dp0." -B "%~dp0build\%CONFIG%" -DCMAKE_BUILD_TYPE=%CONFIG%
if errorlevel 1 exit /b 1
cmake --build "%~dp0build\%CONFIG%"
if errorlevel 1 exit /b 1
ctest --test-dir "%~dp0build\%CONFIG%" --output-on-failure
exit /b %errorlevel%
