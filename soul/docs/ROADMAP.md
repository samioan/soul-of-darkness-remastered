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
  State comparison under random input, not pixels. The game has been played to the end credits
  on the desktop build (2026-11-17).
- **Version 1.0.0 (2026-11-17) is the complete release.** The items under Remaining are possible
  future updates, not missing pieces; no further work is planned for now.

Indices are zero-based: level 0 is the first level.

## Done

- [x] Decompile, rename and compile-check the build; fix its decompiler artifacts (two known
  ones, unboxing, 18 narrowed locals, 21 mis-resolved overloads) with bytecode evidence;
  `tools/check_narrowing.py` finds such artifacts mechanically.
- [x] Translate and run it natively on the Java/MIDP runtime; tick-by-tick oracle
  (`tools/check_engine.py`).
- [x] Widescreen (auto, 240, 427, 569, 747) with the original camera untouched, three
  independently widened tile layers and the bottom icon bar centred. Parallax layers follow the
  wide view (they slid at about 7/8 of the camera speed, backwards, wherever the view was clamped
  to a room). Rooms and levels narrower than the screen (no horizontal scrolling) fill their
  sides with a blurred copy of the room, fading in on entry.
  Sprites decoded to pixel arrays are cropped at the wide screen's edge, not the 240 window's
  (actors left of the original window were cut off or vanished); upgrade and pickup messages,
  the ending's white fade and the closing message before the credits cover the full width.
- [x] Fixes for flaws of the original, active only with the desktop menus (`PortConfig.desktopFixes`,
  off in the `check_engine.py` configuration): the level after a boss no longer starts with
  boss music, the upgrade window's black panel fits its frame, and the upgrade screen no longer
  opens with a stale message left over from the end-of-level upgrade screen, and the level or
  boss music restarts after a death even while the death jingle is still playing.
- [x] Render-only interpolation from recorded draw calls; replay at alpha 1 verified at
  240/427/569/747.
- [x] Keyboard and XInput gamepad with context mapping (including the upgrade screen, which needs
  FIRE held); OPTIONS screen in the main and pause menus (display, audio, keyboard, gamepad).
- [x] Music and effects, original RMS save record, user-JAR loading with SHA-256 identity check,
  data-free package (`soul/port/build_dist.bat`).
- [x] Launcher (`SoulOfDarkness.exe`, `soul/port/launcher`): banner artwork, JAR pick/auto-detect
  with the game's own identity check, window size or fullscreen, Play, and self-update from the
  GitHub releases of samioan/soul-of-darkness-remastered (tag `vX.Y.Z` ->
  `.github/workflows/release.yml`). Headless smoke test in CTest; window, auto-detect, Play and
  return checked in an unattended run.

- [x] Played start to credits by a person on the desktop build (2026-11-17), bosses, deaths and
  restarts included; fixes from that pass are in v0.1.3 to v1.0.0.
- [x] Releases v0.1.0 to v1.0.0 published from tags; v1.0.0 is the complete release.

## Remaining

Possible future updates; none is needed to play the game through.


- [ ] Wire touch input (pointer events) to the mouse, if wanted.
- [ ] Human check of the OPTIONS screen flows: rebinding by capture, gamepad page, returning to
  the pause menu. Only the top page and the display page were seen in an unattended run.
- [ ] Human pass in each display mode (240, 427, 569, 747, auto with live resize, fullscreen)
  and at native/30/60/144 fps.
- [ ] Test gamepad input and audio output on real hardware; both are implemented but were only
  exercised as far as an unattended run can.
- [ ] Decide whether widescreen activation distance should scale with the view (current
  behavior) or stay at the original 240-pixel radius.
- [ ] Interpolation refinements: per-effect layers and sub-pixel motion.
- [ ] Test on a clean Windows machine; non-Windows hosts.
- [ ] Check a launcher update end to end on a fresh install. The update check is unauthenticated,
  so it sees releases only while the repository is public.
- [ ] A scripted, replayable playthrough checked against the original (the human playthrough is
  not recorded as input).

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
