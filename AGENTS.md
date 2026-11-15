# Soul of Darkness decompilation and Windows port

Read README.md and soul/docs/ROADMAP.md first. soul/docs/BUILD_101.md documents the supported
build (Soul of Darkness 1.0.1, HTC P3450, 240x320), its decompiler fixes and its evidence;
soul/docs/NATIVE_ENGINE.md documents the translator, runtime, widescreen and interpolation.
1.0.1 is the only supported version. Reference approaches:
../tes-travels-decomp, ../gow-betrayal-decomp, ../rac-j2me-decomp.

- After completing and verifying a roadmap task, check it off in soul/docs/ROADMAP.md and keep
  its status/remaining lists accurate. Do not append contradictory pending summaries.
- Keep JARs, extracted/decoded assets, screenshots and tool binaries out of git.
- soul/decompiled is raw Vineflower output. soul/src is generated reference Java. Both are
  written by tools/build_reference.py. Change soul/docs/names.map or the asserting entries in
  tools/build_reference.py, then regenerate. Do not edit generated trees directly.
- Rename only after inspecting call sites.
- Check decompiler artifacts against original bytecode with tools/bytecode.py.
  A compile pass alone does not prove runtime equivalence (watch byte counters).
- The original is Gameloft, not the Metaflow RP engine of the sibling projects.
  Reuse infrastructure, verify all game-specific loaders independently.
- The playable game is soul_of_darkness.exe: soul/src translated to C++. soul/port/gen is generated
  by tools/gen_port.py from soul/src + tools/port_patches.py + tools/port_java. Do not edit
  generated trees.
- New decompiler artifacts are found with tools/check_engine.py (first differing tick and fields)
  and tools/check_narrowing.py (narrowed locals, mis-resolved overloads); fix them as asserting
  entries in tools/build_reference.py with bytecode evidence, never in generated source.
- soul/port/engine is the hand-written runtime and host.
- Simulation uses the original Game.run() tick: 50 ms, 66 ms while a cutscene script runs.
  Presentation FPS is independent. Interpolate render state only; collision, animation events
  and saves stay on ticks.
- Widescreen must reveal more world; update camera/culling/buffers/HUD explicitly.
  Verify both 240 and 569 logical widths. Do not globally replace 240 constants.
- Desktop changes go in tools/port_patches.py / tools/port_java, as exact text with match counts.
  Add no field to Game/Actor/BSprite, and keep every helper a no-op at width 240 with original
  menus: python tools/check_engine.py compares that configuration with the original class files
  and must stay identical. Run it after touching the translator, runtime, patches or port Java.
- Widescreen keeps the original 240 camera and derives the wide view (PortConfig); do not rewrite
  camera logic. Frame interpolation replays recorded draw calls; never re-run game draw code
  between ticks (Actor.paint advances animation, timers and random numbers).
- Full-game equivalence is measured under fuzzed input and compares state, not pixels. No complete
  playthrough is recorded. Keep status statements accurate. Do not add publishing/updating to a
  guessed repo.

Commands: .\setup.ps1 [-Jar PATH]; python tools/build_reference.py [--jar PATH];
python tools/gen_port.py; .\soul\port\build.bat [Debug|Release]; .\soul\port\build_dist.bat;
python tools/check_engine.py; python tools/check_narrowing.py. CI builds without any game data.
