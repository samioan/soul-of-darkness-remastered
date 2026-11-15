@echo off
rem The shipping build, and then the folder that gets zipped (ported from the Dawnstar/Shadowkey
rem launchers' build_dist.bat). Release with the static CRT (CMAKE_MSVC_RUNTIME_LIBRARY), so no
rem Visual C++ redistributable is needed; SOD_VERSION is what the launcher prints in its corner and
rem compares with GitHub releases.
rem
rem Usage:  soul\port\build_dist.bat [version]
rem Output: soul\port\dist\  (ready to zip)  and  soul\port\build-dist\  (objects)
setlocal
call "%~dp0vcvars.bat"
if errorlevel 1 exit /b 1
set "SOD_VER=%~1"
if not defined SOD_VER set "SOD_VER=0.0.0-local"
cmake -G Ninja -S "%~dp0." -B "%~dp0build-dist" -DCMAKE_BUILD_TYPE=Release -DSOD_VERSION=%SOD_VER%
if errorlevel 1 exit /b 1
cmake --build "%~dp0build-dist"
if errorlevel 1 exit /b 1
ctest --test-dir "%~dp0build-dist" --output-on-failure
if errorlevel 1 exit /b 1
rem The layout the launcher expects: itself at the top, the game in bin\. game\ (the player's JAR)
rem and launcher.cfg are created by the launcher on first run, never shipped.
if exist "%~dp0dist" rmdir /s /q "%~dp0dist"
cmake --install "%~dp0build-dist" --prefix "%~dp0dist"
if errorlevel 1 exit /b 1
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0check_dist.ps1"
if errorlevel 1 exit /b 1
echo Built %SOD_VER% into soul\port\dist\ -- zip that folder.
exit /b 0
