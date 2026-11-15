cmake_minimum_required(VERSION 3.20)
project(soul_of_darkness VERSION 0.1.0 LANGUAGES CXX)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>")
if(NOT WIN32)
  message(FATAL_ERROR "The host is Win32. Configure with the Windows MSVC toolchain.")
endif()
# The reference Java translated to C++ (gen/, written by tools/gen_port.py) on a small Java/MIDP
# runtime (engine/). soul_of_darkness.exe is the playable game; soul_engine_check is its headless
# driver, compared against the original class files by tools/check_engine.py.
add_library(sod_engine STATIC gen/ErrorReporter.cpp gen/BSprite.cpp gen/Actor.cpp gen/Game.cpp
  gen/PortConfig.cpp gen/PortMenu.cpp
  engine/jrt.cpp engine/midp.cpp engine/engine_glue.cpp engine/host_util.cpp)
target_include_directories(sod_engine PUBLIC engine gen)
target_compile_definitions(sod_engine PUBLIC SOD_PORT_BUILD)
add_executable(soul_of_darkness WIN32 engine/host_win32.cpp)
target_compile_definitions(soul_of_darkness PRIVATE _CRT_SECURE_NO_WARNINGS NOMINMAX WIN32_LEAN_AND_MEAN UNICODE _UNICODE)
target_link_libraries(soul_of_darkness PRIVATE sod_engine user32 gdi32 shell32 winmm comdlg32 ole32)
add_executable(soul_engine_check engine/engine_check.cpp)
target_link_libraries(soul_engine_check PRIVATE sod_engine dbghelp)
if(MSVC)
  # Java int arithmetic wraps; the translator emits it through unsigned helpers (jrt.h), so there is no signed overflow.
  target_compile_options(sod_engine PRIVATE /utf-8 /bigobj /W3 /wd4102 /wd4244 /wd4101 /wd4702)
  target_compile_options(soul_of_darkness PRIVATE /W4 /utf-8)
  target_compile_options(soul_engine_check PRIVATE /W4 /utf-8)
endif()
include(CTest)
if(BUILD_TESTING)
  # Data-free: without game data the engine must report missing resources and keep ticking, not crash.
  add_test(NAME engine_runs_without_data COMMAND soul_engine_check --data no-such-directory --ticks 40 --quiet)
  add_test(NAME engine_wide_without_data COMMAND soul_engine_check --data no-such-directory --ticks 40 --width 569 --quiet)
endif()
install(TARGETS soul_of_darkness soul_engine_check RUNTIME DESTINATION .)
install(FILES dist_readme.txt DESTINATION . RENAME README.txt)
