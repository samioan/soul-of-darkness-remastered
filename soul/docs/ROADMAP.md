# Soul of Darkness roadmap

The current remaining-work list. Update it in place when work lands instead of appending another
progress narrative.

## Target and current status

- **Soul of Darkness 1.0.1, 240x320 (HTC P3450 build)**,
  `roms/SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101_S8000.jar`, SHA256
  `3da9ea7fe8dfaa2c090e8b3714eaed15127f51dc8b67ed9ef9fed51172e4bea4`. It is the only supported
  build. Details, artifact fixes and evidence: [BUILD_101.md](BUILD_101.md).
- `soul_of_darkness.exe` plays it natively, with widescreen, interpolated frame rates,
  keyboard/gamepad and an in-game OPTIONS screen in the game's own style. Design and limits:
  [NATIVE_ENGINE.md](NATIVE_ENGINE.md).
- Its shipped Release engine at width 240 matches the original class files over 187,499 fuzzed
  ticks in 44 scenarios across all eleven levels; the unpatched translation over 202,630.
  State comparison under random input, not pixels: **no complete playthrough has been recorded.**

Indices are zero-based: level 0 is the first level.

## Done

- [x] Decompile, rename and compile-check the build; fix its decompiler artifacts (two known
  ones, unboxing, 18 narrowed locals, 21 mis-resolved overloads) with bytecode evidence;
  `tools/check_narrowing.py` finds such artifacts mechanically.
- [x] Translate and run it natively on the Java/MIDP runtime; tick-by-tick oracle
  (`tools/check_engine.py`).
- [x] Widescreen (auto, 240, 427, 569, 747) with the original camera untouched, three
  independently widened tile layers and the bottom icon bar centred.
- [x] Render-only interpolation from recorded draw calls; replay at alpha 1 verified at
  240/427/569/747.
- [x] Keyboard and XInput gamepad with context mapping; OPTIONS screen in the main and pause menus
  (display, audio, keyboard, gamepad).
- [x] Music and effects, original RMS save record, user-JAR loading with SHA-256 identity check,
  data-free package (`soul/port/build_dist.bat`).

## Remaining

- [ ] Wire touch input (pointer events) to the mouse, if wanted.
- [ ] Human check of the OPTIONS screen flows: rebinding by capture, gamepad page, returning to
  the pause menu. Only the top page and the display page were seen in an unattended run.
- [ ] Record and verify a complete playthrough, first spawn to credits, including every boss,
  exit, checkpoint death/restart and Continue.
- [ ] Human pass in each display mode (240, 427, 569, 747, auto with live resize, fullscreen)
  and at native/30/60/144 fps.
- [ ] Test gamepad input and audio output on real hardware; both are implemented but were only
  exercised as far as an unattended run can.
- [ ] Decide whether widescreen activation distance should scale with the view (current
  behavior) or stay at the original 240-pixel radius.
- [ ] Interpolation refinements: per-effect layers and sub-pixel motion.
- [ ] Test on a clean Windows machine; non-Windows hosts.
- [ ] Configure publishing/updating only after a real destination is supplied; no repository or
  hosting destination is currently configured.

## Verification

- `python tools/build_reference.py` after changing `soul/docs/names.map` or the artifact fixes,
  then `python tools/gen_port.py`; `python tools/check_narrowing.py` must show only the known
  harmless findings listed in BUILD_101.md.
- `soul\port\build.bat` (Debug + data-free CTests) and `soul\port\build_dist.bat` (Release,
  tests, package validation). CI needs no game data.
- `python tools/check_engine.py` (needs the extracted data and a JDK) after any change to the
  translator, runtime, port patches or port Java. It must stay identical; a divergence names the
  first differing tick and fields.
- `soul_engine_check --verify-replay` after touching recording or drawing.
