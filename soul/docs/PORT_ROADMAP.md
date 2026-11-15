# Windows port implementation roadmap

Updated 2026-11-15 after porting the full 1.0.1 build. [ROADMAP.md](ROADMAP.md) is
the overall project checklist. [PORT_ROADMAP_HISTORY.md](PORT_ROADMAP_HISTORY.md)
preserves the old notes; their claims that gameplay, platforms or Continue are
still absent are superseded. Maintain these lists in place as tasks finish.

Level indices are zero-based; level 0 is the first level and uses pack 3.

## Two native code paths

1. **Translated engine (the game).** The reference Java is translated to C++ by
   `tools/j2cpp` and runs on `soul/port/engine`. Two builds share it:
   `soul_of_darkness.exe` is the full 1.0.1 game (`soul_full/src` -> `soul/port/gen_full`,
   eleven levels, in-game OPTIONS screen; see [BUILD_101.md](../../soul_full/docs/BUILD_101.md));
   `soul_of_darkness_v137.exe` is the cut-down 1.3.7 game (`soul/src` -> `soul/port/gen`).
   Evidence, design and limits: [NATIVE_ENGINE.md](NATIVE_ENGINE.md).
2. **Hand-ported handlers (evidence and tooling).** `soul_port.exe`,
   `soul_assets.exe`, `soul/port/src`, the `check_native_*.py` oracles and the
   NATIVE_*.md documents. Level 0 and controlled level-1 fixtures, each compared
   with original bytecode. Kept because they are independent verification of the
   same original behavior and useful for analysis; not extended further.

## Implemented baseline

- Win32 C++17 executables, MSVC/CMake builds, data-free CI and package.
- Translated engine: every original state, level, script, boss, save and music
  path. Shipped Release build identical to the original class files over 197,897
  fuzzed ticks at width 240 with original text; unpatched translation over
  237,353. State comparison, not pixels; no recorded full playthrough.
- Widescreen 427/569/747/auto with the original camera untouched and a derived
  wide view; rooms narrower than the screen are centred; panels keep 240 layout.
- Display-rate presentation with layer interpolation from recorded draw calls;
  replay at alpha 1 reproduces tick frames exactly in every checked run.
- Keyboard + XInput with context mapping and `settings.ini` bindings; MIDI via
  MCI; original RMS record on disk; JAR loaded in place with SHA-256 check;
  English text with F7 toggle.
- Hand-port baseline (unchanged, 109 of the 111 CTests): the modules and counts
  listed in the NATIVE_*.md documents.

## Closed as superseded

These were open hand-port tasks. They are not ported by hand and will not be;
the translated engine runs the original code for each. Listed so nobody reads
their absence from `soul/port/src` as missing gameplay.

- [x] Second-level gameplay wiring, staggered ambush starts, passage/region handoff.
- [x] Types 13/14/70, 37, 64, 72, 61/79, 68, 69, 73, 74, 75, 78, 82, 95, 90, 100.
- [x] Level-3 paired mode and level-5 controller/hazard/group state and snapshots.
- [x] Remaining type-1/2, platform, bridge, gate, beam and passage variants.
- [x] Type-35/48 rewards, placed type-39 effects, type-40/20/49 later-level modes.
- [x] General UID/pool and ordered dispatch/painter ownership.
- [x] All 17 scripts / 47 tracks, dialogue, QTEs, skip, fades, bars.
- [x] Selection map, upgrade/progression data, Help, pause, game over, ending.
- [x] Original HUD, boss bar, weather and backgrounds.
- [x] English text for all visible strings.
- [x] Render interpolation with snap boundaries; 240/569 then 427/747 checks.
- [x] Full save record; original RMS layout by construction.
- [x] Music backend, mute/volume, focus-loss suspension and delayed resume.
- [x] Configurable keyboard/controller bindings.
- [x] User-owned JAR loading with identity check; data-free package.

## Remaining

- [ ] Recorded complete playthrough and save round trip across all nine levels.
- [ ] Human verification of display modes, fullscreen, live resize, gamepad and
  audio output; clean-machine test.
- [ ] Widescreen activation-distance decision (see ROADMAP.md).
- [x] In-game settings/rebinding UI (1.0.1 build: OPTIONS screen, PortMenu).
- [ ] Mouse for the 1.0.1 build's touch input; OPTIONS for the 1.3.7 program; desktop tutorial icons.
- [ ] Interpolation refinements (per-effect layers, sub-pixel).
- [ ] Supply a real publishing/updater destination before configuring hosting.

## Acceptance rule for each task

- Game behavior comes from the reference Java. Fix decompiler artifacts in
  `names.map`/`PATCHES` (tools/rename_sod.py) with bytecode evidence, regenerate
  `soul/src`, then `python tools/gen_port.py`. Never edit `soul/src` or `soul/port/gen`.
- Desktop behavior goes in `tools/port_patches_full.py` + `tools/port_java_full` (1.0.1) or
  `tools/port_patches.py` + `tools/port_java` (1.3.7): exact text, exact match count. Add no field to Game/Actor/BSprite. Every helper
  must be a no-op at width 240 with original text.
- `python tools/check_engine.py` must report identical fingerprints for the
  Release `soul_engine_check.exe`. A translator or runtime change that needs a
  longer run to trust should get one (`--ticks`, `--seeds`).
- Run `soul_engine_check --verify-replay` at 240 and a wide width after touching
  drawing or recording; look at frames (`--shot`) after touching layout.
- Pass Debug/Release builds, CTest and package validation. Keep status statements
  accurate: state what was measured and what was only inspected or not run.
