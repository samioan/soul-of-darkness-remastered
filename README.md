# Soul of Darkness Remastered

Decompilation and Windows port of Gameloft's **Soul of Darkness 1.0.1** for J2ME (the 240x320
HTC P3450 build). Follows the approach in `../tes-travels-decomp`, `../gow-betrayal-decomp` and
`../rac-j2me-decomp`: recover the Java, verify and rename it, then build a native C++17 port with
CMake, Ninja and MSVC.

**`soul_of_darkness.exe` plays the whole game natively: native English, eleven chapters, weapon
upgrades, transformations, parallax backgrounds, music and sound effects, with widescreen,
display-rate frame interpolation, keyboard and gamepad, and an in-game OPTIONS screen drawn in the
game's own style.** It is the verified reference Java translated to C++ on a small runtime, not
an emulator. The shipped Release engine at the original 240 width matches the original class
files field for field over 187,499 fuzzed ticks across all eleven levels. That compares game
state under random input, not pixels, and **no complete playthrough has been recorded yet**.

```powershell
.\setup.ps1 -Jar <SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101_S8000.jar>   # once: tools, reference Java, C++, build, check
python tools/gen_port.py              # regenerate soul/port/gen after Java or patch changes
.\soul\port\build_dist.bat            # Release build, tests, package in soul/port/dist
python tools/check_engine.py          # original bytecode vs. the built engine
.\soul\port\dist\soul_of_darkness.exe --jar <that jar>
```

Put your own JAR beside the executable, or pick it when asked; nothing is extracted. OPTIONS in
the main and pause menus covers display, audio, keyboard and gamepad. Press F1 in the game for
controls; F8 cycles width (auto, 240, 427, 569, 747), F9 frame rate, F10 interpolation, F11
fullscreen. See [soul/port/dist_readme.txt](soul/port/dist_readme.txt).

## Layout

| Path | Contents |
|---|---|
| `roms/` | The supplied JAR (ignored) |
| `soul/extracted/` | JAR contents (ignored) |
| `soul/decompiled/` | Raw Vineflower output (generated) |
| `soul/src/` | Renamed reference Java with bytecode-verified decompiler fixes (generated) |
| `soul/docs/` | [BUILD_101.md](soul/docs/BUILD_101.md) (the build, its artifact fixes and evidence), [NATIVE_ENGINE.md](soul/docs/NATIVE_ENGINE.md) (translator, runtime, widescreen, interpolation), [ROADMAP.md](soul/docs/ROADMAP.md), `names.map` |
| `soul/port/gen/` | The reference Java translated to C++ (generated, tracked) |
| `soul/port/engine/` | Hand-written Java/MIDP runtime, glue and Win32 host |
| `tools/` | Pipeline and checks; see [tools/README.md](tools/README.md) |

## Pipeline

1. `python tools/build_reference.py` extracts the JAR (SHA-256 checked), decompiles it, renames
   it with `soul/docs/names.map`, applies the asserting decompiler-artifact fixes and
   compile-checks `soul/src`.
2. `python tools/gen_port.py` applies the desktop patches (`tools/port_patches.py`), adds
   `tools/port_java`, and translates the result to `soul/port/gen` with `tools/j2cpp`.
3. `soul\port\build.bat [Debug|Release]` builds and runs the data-free tests;
   `soul\port\build_dist.bat` packages the Release build.
4. `python tools/check_engine.py` runs the original class files on a JVM and the built engine side
   by side and fails on the first tick whose state differs. `python tools/check_narrowing.py`
   finds decompiler type-narrowing and overload artifacts in `soul/src`.

CI builds without any game data.
