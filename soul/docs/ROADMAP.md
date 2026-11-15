# Soul of Darkness roadmap

Updated 2026-11-15 after porting the full 1.0.1 build. This is the current
remaining-work list. Historical implementation notes are in
[ROADMAP_HISTORY.md](ROADMAP_HISTORY.md); their old pending/next descriptions
are superseded. Update the lists below when work lands instead of appending
another conflicting progress narrative.

## Target and current status

- **Primary target since 2026-11-15: Soul of Darkness 1.0.1, 240x320 (HTC P3450 build)**,
  `roms/SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101_S8000.jar`, SHA256
  `3da9ea7fe8dfaa2c090e8b3714eaed15127f51dc8b67ed9ef9fed51172e4bea4`. It is the full-featured
  game: native English, eleven chapters, weapon upgrades, transformations, parallax backgrounds,
  ten music tracks and sound effects. `soul_of_darkness.exe` plays it, with widescreen,
  interpolated frame rates, keyboard/gamepad and an in-game OPTIONS screen in the game's own
  style. Reference tree `soul_full/`; details, artifact fixes and evidence in
  [BUILD_101.md](../../soul_full/docs/BUILD_101.md).
- Its shipped Release engine at width 240 matches the original class files over 187,499 fuzzed
  ticks in 44 scenarios across all eleven levels; the unpatched translation over 202,630.
  State comparison under random input, not pixels: **no complete playthrough has been recorded.**
- The 1.3.7 JAR below is a cut-down edition (Czech text, nine shorter levels, no upgrades). Its
  translation still builds as `soul_of_darkness_v137.exe` and still passes; the rest of this
  section describes it.
- Secondary target: Gameloft Soul of Darkness J2ME 1.3.7, nine gameplay levels.
- Input: `roms/Soul-of-Darkness_J2ME_EN_v137.jar`, 347896 bytes,
  SHA256 `9d839e59d6cb2f80575736fbf867babe0da852312652490dd9025d0a0654dd6b`.
- All six classes decompile; generated reference Java compiles and has selected
  original-bytecode checks.
- **`soul_of_darkness.exe` runs the whole game natively**: the reference Java is
  translated to C++ (`tools/gen_port.py` -> `soul/port/gen`) on a small Java/MIDP
  runtime (`soul/port/engine`). All nine levels, menus, cutscenes, both bosses,
  the original save record and music are present because they are the original
  code. See [NATIVE_ENGINE.md](NATIVE_ENGINE.md).
- The shipped Release engine at width 240 with original text matches the original
  class files field for field over 197,897 fuzzed ticks in 45 scenarios across all
  nine levels (`tools/check_engine.py`); the unpatched translation matches over
  237,353 ticks in 54 scenarios. This compares game state, not pixels, and fuzzing
  does not reach every room: **no complete playthrough has been recorded.**
- Desktop features on top: widescreen (auto, 240, 427, 569, 747), display-rate
  presentation with render interpolation, keyboard and XInput gamepad with
  rebinding, MIDI music, English text, direct JAR loading with identity check.
- `soul_port.exe`/`soul_assets.exe` and the NATIVE_*.md handler ports remain as
  independent per-handler evidence and analysis tooling for level 0 and parts of
  level 1. They are no longer the path to a playable game, and their unfinished
  handler lists are closed below as superseded rather than as ported by hand.

Indices below are zero-based: level 0 is the first level; gameplay pack =
level index + 3.

## Done by the translated engine

Each item was an open ticket; it is satisfied by running the original code
natively, with the evidence and limits in [NATIVE_ENGINE.md](NATIVE_ENGINE.md).

- [x] Second-level actors, handlers, passages, scripts and route; entry from the
  frontend. (Superseded: all levels construct and run from the original loaders.)
- [x] Enemy types 13/14/70, later variants of types 1/2/3, and every placed
  controller/object type (37, 61/79, 64, 68, 69, 72, 73, 74, 75, 78, 82, 95).
- [x] Bosses 90 (level 3) and 100 (level 8) with phases, projectiles and
  scripted entry/exit; level 3 paired mode; level 5 controller/hazard/group state.
- [x] Type 48/39/35 rewards and effects, all type-40/20/49 trigger and camera modes.
- [x] All 17 scripts: camera, global and cinematic actor tracks, dialogue,
  input locks/skip, QTEs, fades, shakes, letterbox.
- [x] Original UID/pool, dispatch and painter ordering; dynamic prompts; original
  HUD, boss bar, weather and backgrounds.
- [x] Saves: the original 1313-byte RMS record with all level, upgrade, unlock,
  checkpoint and level-3/5 snapshot data, written to `dvseed.sav`.
- [x] Animated level-selection map, unlock navigation, Help, pause, game over,
  completion and ending flows, as the original draws them.
- [x] English text for every interface string and all nine levels' dialogue
  (`tools/port_java/PortEnglish.java`); credited names preserved; F7 shows the
  JAR's own text.
- [x] Music, mute/volume, focus-loss suspend and the original resume delay.
- [x] Keyboard and controller bindings in `settings.ini`, shared by menus and play.
- [x] User-JAR loading with SHA-256 identity check; data-free package
  (`soul/port/build_dist.bat`).
- [x] Render-only interpolation for tiles, world, actors and camera, snapping on
  jumps; collision, animation events, timers and saves stay on 83 ms ticks.
- [x] Widescreen that reveals more world at 427/569/747 with explicit camera view,
  culling, tile, HUD and panel handling; 240 stays identical to the original.

## Done for the 1.0.1 build (2026-11-15)

- [x] Decompile, rename and compile-check the 1.0.1 build; fix its decompiler artifacts (two
  known ones, unboxing, 18 narrowed locals, 21 mis-resolved overloads) with bytecode evidence;
  add `tools/check_narrowing.py` to find such artifacts mechanically.
- [x] Translate and run it natively; extend the runtime (Vector, plain Canvas, Font) and the
  oracle (`tools/check_engine.py --game full`).
- [x] Port widescreen, interpolation layers and input to its three-layer renderer and bottom
  icon bar; verify replay at 240/427/569/747.
- [x] In-game settings and rebinding screens in the game's art style (OPTIONS in the main and
  pause menus: display, audio, keyboard, gamepad).
- [x] Per-build JAR identity, save folders and package (`soul_of_darkness.exe` = 1.0.1,
  `soul_of_darkness_v137.exe` = 1.3.7).

## Remaining

- [ ] Wire the 1.0.1 build's touch input (pointer events) to the mouse, if wanted.
- [ ] Human check of the OPTIONS screen flows: rebinding by capture, gamepad page, returning to
  the pause menu. Only the top page and the display page were seen in an unattended run.
- [ ] Record and verify a complete playthrough, first spawn to credits, including
  every boss, exit, checkpoint death/restart and Continue. Current evidence is
  per-tick equivalence under fuzzed input plus inspected frames.
- [ ] Human pass in each display mode (240, 427, 569, 747, auto with live resize,
  fullscreen) and at native/30/60/144 fps. Unattended runs covered startup,
  cutscenes, gameplay, music start and saving; nobody has watched a whole level.
- [ ] Test gamepad input and audio output on real hardware; both are implemented
  but were only exercised as far as an unattended run can.
- [ ] Decide whether widescreen activation distance should scale with the view
  (current behavior) or stay at the original 240-pixel radius.
- [ ] An OPTIONS screen for the 1.3.7 program too (it still uses a file plus F-keys).
- [ ] Replace phone-key tutorial icons and Czech HUD bar labels, which are sprite
  art, if English/desktop art is wanted.
- [ ] Interpolation refinements: per-effect layers and sub-pixel motion.
- [ ] Test on a clean Windows machine; non-Windows hosts.
- [ ] Configure publishing/updating only after a real destination is supplied;
  no repository or hosting destination is currently configured.

## Verification and implementation detail

- 1.0.1: `python tools/build_full.py` after changing `soul_full/docs/names.map` or its artifact
  fixes, then `python tools/gen_port.py`; `python tools/check_narrowing.py soul_full` and
  `python tools/check_engine.py` must stay clean.
- 1.3.7: `python tools/rename_sod.py`, then `python tools/gen_port.py --game v137` and
  `python tools/check_engine.py --game v137`.
- `soul\port\build.bat` (Debug + 112 CTests) and `soul\port\build_dist.bat`
  (Release, tests, package validation). CI needs no game data.
- `python tools/check_engine.py` (needs the extracted data and a JDK) after any
  change to the translator, runtime, port patches or port Java. It must stay
  identical; a divergence names the first differing tick and fields.
- `soul_engine_check --verify-replay` after touching recording or drawing.
- [PORT_ROADMAP.md](PORT_ROADMAP.md) gives the native layout and acceptance rules;
  feature docs retain detailed evidence and limits.
