cmake_minimum_required(VERSION 3.20)
project(soul_of_darkness VERSION 0.1.0 LANGUAGES CXX)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>")
if(NOT WIN32)
  message(FATAL_ERROR "The host is Win32. Configure with the Windows MSVC toolchain.")
endif()

# `SOD_VERSION` is what the launcher prints in its corner. A local build leaves it at "-dev", which
# also turns the launcher's self-update off (launcher/updater.h); the release workflow sets it from
# the git tag (.github/workflows/release.yml). The owner/repo/prefix say where updates come from.
set(SOD_VERSION "0.1.0-dev" CACHE STRING "Version string shown by the launcher")
set(SOD_UPDATE_OWNER "samioan" CACHE STRING "GitHub owner to check for updates")
set(SOD_UPDATE_REPO "soul-of-darkness-remastered" CACHE STRING "GitHub repo to check for updates")
set(SOD_UPDATE_TAG_PREFIX "v" CACHE STRING "Release tag prefix")

# The reference Java translated to C++ (gen/, written by tools/gen_port.py) on a small Java/MIDP
# runtime (engine/). soul_of_darkness.exe is the playable game; soul_engine_check is its headless
# driver, compared against the original class files by tools/check_engine.py.
add_library(sod_engine STATIC gen/ErrorReporter.cpp gen/BSprite.cpp gen/Actor.cpp gen/Game.cpp
  gen/PortConfig.cpp gen/PortMenu.cpp
  engine/jrt.cpp engine/midp.cpp engine/engine_glue.cpp engine/host_util.cpp engine/host_zip.cpp engine/inflate.cpp)
target_include_directories(sod_engine PUBLIC engine gen)
target_compile_definitions(sod_engine PUBLIC SOD_PORT_BUILD)
add_executable(soul_of_darkness WIN32 engine/host_win32.cpp)
target_compile_definitions(soul_of_darkness PRIVATE _CRT_SECURE_NO_WARNINGS NOMINMAX WIN32_LEAN_AND_MEAN UNICODE _UNICODE)
target_link_libraries(soul_of_darkness PRIVATE sod_engine user32 gdi32 shell32 winmm comdlg32 ole32)
add_executable(soul_engine_check engine/engine_check.cpp)
target_link_libraries(soul_engine_check PRIVATE sod_engine dbghelp)

# ---- the launcher: SoulOfDarkness.exe, what a player opens. Picks and checks the JAR, chooses the
# window size, starts the game, and updates the install from the GitHub releases above. Ported from
# tes-travels-decomp's Dawnstar launcher (itself from shadowkey-decomp); see launcher/launcher_main.cpp.
# Its artwork (launcher/assets/banner.jpg, soul_of_darkness.ico) is generated from
# launcher/assets/banner_source.jpg by tools/make_banner.py and tools/make_icon.py.
configure_file(launcher/version.h.in "${CMAKE_BINARY_DIR}/generated/launcher/version.h" @ONLY)
# Everything but the window: shared with the smoke test.
add_library(sod_launcher_core STATIC
  launcher/banner.cpp launcher/jar_install.cpp launcher/launcher_config.cpp
  launcher/update_check.cpp launcher/updater.cpp launcher/zip_reader.cpp
  engine/host_util.cpp engine/inflate.cpp)
target_include_directories(sod_launcher_core PUBLIC . "${CMAKE_BINARY_DIR}/generated")
target_compile_definitions(sod_launcher_core PUBLIC UNICODE _UNICODE NOMINMAX WIN32_LEAN_AND_MEAN)
# windowscodecs is WIC (the banner), winhttp the update check and download.
target_link_libraries(sod_launcher_core PUBLIC windowscodecs ole32 winhttp)
add_executable(sod_launcher WIN32 launcher/launcher_main.cpp launcher/launcher.rc)
set_target_properties(sod_launcher PROPERTIES OUTPUT_NAME "SoulOfDarkness")
target_link_libraries(sod_launcher PRIVATE sod_launcher_core comctl32 shell32)
# The manifest is a resource in launcher.rc (see the comment there for why).
target_link_options(sod_launcher PRIVATE /MANIFEST:NO)

if(MSVC)
  # Java int arithmetic wraps; the translator emits it through unsigned helpers (jrt.h), so there is no signed overflow.
  target_compile_options(sod_engine PRIVATE /utf-8 /bigobj /W3 /wd4102 /wd4244 /wd4101 /wd4702)
  target_compile_options(soul_of_darkness PRIVATE /W4 /utf-8)
  target_compile_options(soul_engine_check PRIVATE /W4 /utf-8)
  target_compile_options(sod_launcher_core PRIVATE /W4 /utf-8)
  target_compile_options(sod_launcher PRIVATE /W4 /utf-8)
endif()
include(CTest)
if(BUILD_TESTING)
  # Data-free: without game data the engine must report missing resources and keep ticking, not crash.
  add_test(NAME engine_runs_without_data COMMAND soul_engine_check --data no-such-directory --ticks 40 --quiet)
  add_test(NAME engine_wide_without_data COMMAND soul_engine_check --data no-such-directory --ticks 40 --width 569 --quiet)
  # The launcher's logic without a window or a network: versions, release JSON, package shape, zip
  # paths, config round trip, JAR checks, the banner decode.
  add_executable(launcher_smoke tests/launcher_smoke.cpp)
  target_link_libraries(launcher_smoke PRIVATE sod_launcher_core)
  if(MSVC)
    target_compile_options(launcher_smoke PRIVATE /W4 /utf-8)
  endif()
  add_test(NAME launcher_smoke COMMAND launcher_smoke "${CMAKE_CURRENT_SOURCE_DIR}/launcher/assets/banner.jpg")
endif()

# Package layout: the launcher at the top, the game and its checker in bin/ (what
# launcher/updater.cpp's package check and FindGameExecutable expect).
install(TARGETS sod_launcher RUNTIME DESTINATION .)
install(TARGETS soul_of_darkness soul_engine_check RUNTIME DESTINATION bin)
install(FILES dist_readme.txt DESTINATION . RENAME README.txt)
install(FILES ../../NOTICE.md DESTINATION .)
