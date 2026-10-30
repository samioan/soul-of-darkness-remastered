# Windows port implementation roadmap

Updated 2026-10-29 after the selected-level ownership pass. This file lists the
remaining native work and acceptance conditions. [ROADMAP.md](ROADMAP.md) is
the overall project checklist. [PORT_ROADMAP_HISTORY.md](PORT_ROADMAP_HISTORY.md)
preserves the old notes; their claims that gameplay, platforms or Continue are
still absent are superseded. Maintain these lists in place as tasks finish.

Level indices are zero-based; level 0 is the first level and uses pack 3.

## Implemented baseline

- Win32 C++17 executable, MSVC/CMake builds, data-free CI/package, asset discovery,
  independent FPS pacing, resize/fullscreen, display settings and headless captures.
- Native resource/sprite/font/tile readers and rendering; all nine gameplay maps
  can be previewed. Normal gameplay still constructs only level 0.
- English startup/title/menu/credits/prologue, first-level selection, health/MP
  HUD, pause/resume, focus-loss pause, checkpoint restart, disk autosave and Continue.
- Level-0 movement, tile/body contacts, melee, twenty type-1 and seven type-2
  enemies, loot, chests, fragment/ability rewards, fire/ice spells and weapon switch.
- First-arena trigger 129, waves 54/55, chest 57 reward, trigger 166 and gate
  script 165. All sixteen room passages and region-clipped widescreen rendering.
- Rider platform, six chandeliers, two bridges, six stationary/sweeping beam
  emitters and nine placed tutorial prompts. Original-handler limits remain in
  the corresponding NATIVE_*.md docs.
- Opening trigger 82/script 76, with four English dialogues, camera focus,
  actor/prompt ownership, skip and input handoff. See NATIVE_INTRO.md.
- Trigger 161/script 160 controls enemy 205 and chandelier 34 during normal play,
  with flag release and completed-trigger cleanup. See NATIVE_CHANDELIER.md.
- Linked triggers 43/61 remove prompts 187/59 on chandelier overlap, with lazy
  source/target lookup and ordered removal. See NATIVE_PROMPT_REMOVAL.md.
- Camera controllers 63/167 now own linked-enemy framing, removal/release and
  falling-camera tracking; 1368 original-bytecode/native rows and 58 host checks
  verify the supported modes. See NATIVE_CAMERA_CONTROLLERS.md.
- Exit 173/state 15 now runs inclusive contact, death-pose rejection, closing
  bars, English completion text and a one-time selection handoff. Completion
  now persists, unloads the world and selects the next (currently unavailable)
  chapter. Version-1 checkpoint saves migrate to version 2; Continue and replay
  keep distinct position/stat rules. See NATIVE_COMPLETION.md and NATIVE_FINISH.md.
- Shared selected-level data owner, player initialization and required assets
  compare across all nine levels in 983 bytecode rows. Explicit teardown clears
  cached links and projectile/rider support before targets unload. Normal
  gameplay remains gated to level 0; see NATIVE_SELECTED_LEVELS.md.
- Latest data-free suite: 104 CTests and 530 player assertions. Chandelier fixtures
  compare 3000 bytecode rows and 24 host runs; intro fixtures
  compare 5460 original-bytecode rows and 48 playback/skip host runs. Linked
  prompt removal adds 352 handler/world rows against original bytecode.

The full game is not yet ported. Asset decoding, Java comparisons and controlled
native fixtures do not establish a complete native play-through.

## Ordered first-level work

1. [x] **Intro script 76 and trigger 82.** Add type-20 animation-8 activation,
   cutscene enter/leave ownership, position/animation/flag commands for UIDs
   19/83/84/186/267, camera command 12, anchored dialogue command 116 and removal
   command 113. Accept when the original intro can play, be skipped where allowed,
   and hand control to Kale without stuck flags or orphaned actors. Implemented
   and verified; see [NATIVE_INTRO.md](NATIVE_INTRO.md).
2. [x] **Script 160 and trigger 161.** Implement the player-overlap script-start
   condition, enemy-205 flag commands 107/108 and chandelier-34 position,
   animation/action tracks. Accept when actual overlap starts the sequence once
   and its finished actors return to normal world ownership. Implemented and
   verified; see [NATIVE_CHANDELIER.md](NATIVE_CHANDELIER.md).
3. [x] **Linked prompt-removal triggers 43/61.** Resolve chandelier UIDs 3/34,
   evaluate their original trigger conditions and remove prompt UIDs 187/59.
   Source lookup, trigger removal and update-queue timing now match bytecode;
   see [NATIVE_PROMPT_REMOVAL.md](NATIVE_PROMPT_REMOVAL.md).
4. [x] **Camera controllers 63/167.** Implement the link from 63 to enemy 202,
   missing/removed-link release, strict overlap, active-controller selection and
   controller 167's falling-camera branch. Adapt targets/clamps to 240/569
   explicitly; never replace every 240/120 constant globally. Implemented and
   verified; see [NATIVE_CAMERA_CONTROLLERS.md](NATIVE_CAMERA_CONTROLLERS.md).
5. [x] **Exit trigger 173 and completion state 15.** Compare inclusive contact,
   defeated-player rejection, one-time entry, closing bars, completion message
   and acknowledge input against the original flow. Implemented and verified;
   see [NATIVE_COMPLETION.md](NATIVE_COMPLETION.md). Task 6 now implements its
   save/unload/next-selection handoff, with entry to unported levels disabled.
6. [x] **Complete/unlock/save transition.** Port `finishLevel` effects, including
   the completion flag, maximum unlocked level, state unload/reset and next-level
   selection. Extend desktop saves with version migration and test restart vs
   Continue vs completed-level Continue after exiting/reopening the executable.
   Implemented for level 0, including version-1 migration and a guarded chapter-2
   selection; see [NATIVE_FINISH.md](NATIVE_FINISH.md). Level 1 remains unavailable
   until task 8 supplies its world handlers and verifies its variants.
7. [x] **Selected-level construction.** Replace hardcoded level-0 scene/object/
   script loads with one level descriptor shared by player, sprites, records,
   scripts, regions and checkpoints. Rebuild all owners on load; release platform,
   projectile, script and cached UID links before destroying their targets.
   Implemented and verified for shared inputs across all nine selections and
   controlled unload/restart fixtures; see [NATIVE_SELECTED_LEVELS.md](NATIVE_SELECTED_LEVELS.md).
   Later native actor construction and gameplay remain task 8.
8. [ ] **Second-level gameplay.** Add its new type-3 enemies, type-80 collapsing
   platform and type-89 switches, then verify its existing-type variants and all
   twenty passages. Enable New Game/Continue progression into level 1 only after
   its construction and required controllers work.
9. [ ] **Full first-level route test.** Start at the ordinary spawn, run the intro,
   traverse rooms/platforms, complete the arena/reward/gate, pass the beams and
   final room, then complete/unload. Exercise checkpoint death/restart/Continue
   along the route at 240/569 and native/30/60/144 FPS. Current fixtures start at
   controlled positions and do not replace this test.

## Remaining owners and handlers for levels 1-8

- [ ] Type-3 AI plus type-16 thrown objects and its projectile/effect births.
- [ ] Type-13/type-14/type-70 AI, projectile targeting, damage and respawn variants.
- [ ] Type-37 gate controller, type-64 springs and type-72 linked platforms.
- [ ] Type-61/type-79 destructibles and their mutable collision/render state.
- [ ] Type-68 spawners and type-69 paired owners; native level-3 paired-player mode.
- [ ] Type-73 hazards, type-74 spawners, type-75 traps and type-78 hazard areas.
- [ ] Type-80 collapsing platforms, type-82 groups, type-89 switches and type-95 crushers.
- [ ] Native level-5 controller/hazard/group links and their saved state, using
  LEVEL5_CONTROLLER.md's original-Java fixtures as reference rather than calling
  that Java coverage a native implementation.
- [ ] Type-90/type-100 complete boss controllers, projectiles, script handoffs,
  health HUD and terminal/ending transitions.
- [ ] Remaining type-1/type-2, platform, bridge, gate, beam and passage variants
  present in later records; support for a type in level 0 is not coverage of all variants.
- [ ] Remaining type-35/type-48 rewards, placed type-39 effects, rank/progression
  pickups and all later-level container outputs.
- [ ] Type-40 conditions/effects and type-20/type-49 modes used by later levels.
- [ ] Complete general UID/pool, ordered actor/contact/transient/painter ownership.
  Integrate external containers/pickups and independent beam-cap animation with
  that owner; retain stable lifetime and original birth/removal/update order.

## Script, UI and presentation work

- [ ] Run all 17 scripts / 47 decoded tracks: camera and global tracks, cinematic
  actor creation, UID-targeted properties, flags, commands and completion modes.
  Selected scripts 76 (intro), 160 (enemy/chandelier) and 165 (gate) now run
  natively; the other scripts and general command ownership remain unfinished.
- [ ] Dialogue layout/English text, interaction locks, QTE success/failure, allowed
  skip, audio/shake/fade commands and cutscene bars. Complete the original
  Game.paint mutations on simulation ticks before adding faster presentation.
- [ ] Dynamic prompt camera/player-follow modes and script-spawned prompts.
  The nine placed type-21 handlers and desktop key captions are implemented;
  other owners must not be silently routed through that placed-only loader.
- [ ] Original animated selection map, unlock-dependent navigation and the
  selected-level stat/checkpoint-position decision. Only level 0 is playable from the current card.
- [ ] Weapon/magic upgrade menus, all upgrade arrays/ranks and their save ownership.
- [ ] Help/options, remaining pause/game-over actions, completion and ending screens;
  retain the working keyboard display controls until equivalent menu options exist.
- [ ] Remaining non-tile background/weather effects, original HUD/boss display and
  overlays; the two visual tile layers and first-level backdrop already render. Keep the existing desktop HP/MP HUD accurately described as such.
- [ ] English replacements for remaining visible Czech text; preserve credit names.
- [ ] Player/actor/platform/camera render interpolation with explicit snap boundaries
  at passage transfer, restart, script reposition and scene load. Keep collision,
  animation events, damage, timers and save capture on 83 ms simulation ticks.
- [ ] Verify 240/569 through all new camera/script/HUD paths, then 427/747 and live
  resize/fullscreen. Widescreen must reveal world without exposing disconnected
  packed rooms or stretching the 240-wide startup panels.

## Persistence, audio, controls and delivery

- [ ] Extend version-2 level/completion/unlock saves beyond level 0, including
  upgrade arrays and remaining flags; retain version-1 migration. The next
  selected level is already derived from completion, with construction gated.
- [ ] Native level-3 paired and level-5 controller snapshots through checkpoint,
  Restart, Continue and completed-level advancement.
- [ ] Decide original RMS compatibility scope; before offering it, implement and
  compare the full 1313-byte layout and its original flag-order/restoration quirks.
  Desktop checkpoint persistence already works and must not be listed as absent.
- [ ] Music and effects backend, mute/volume, focus-loss suspension and delayed resume.
- [ ] Configurable keyboard/controller bindings with consistent menu and gameplay
  edge/held input; preserve the current working A/D/arrows/Space/E/J/K/Q controls.
- [ ] User-owned JAR import with expected identity checks, asset extraction/location
  errors and an installer/launcher. Keep assets/JARs/saves out of git and packages.
- [ ] Continuous play-through, save round-trip, boss/exit and scene-unload regressions
  across all nine levels and supported display settings; test on a clean Windows machine.
- [ ] Supply a real publishing/updater destination before configuring remote hosting.
  No remote repository, automatic updater or playable-release target is configured.

## Acceptance rule for each task

- Inspect the original call sites/bytecode; use names.map or asserting patches
  for generated Java changes and regenerate rather than editing generated trees.
- Add meaningful original/reference/native fixtures for the newly ported behavior;
  explicitly separate controlled fixtures from actual route tests and runtime parity.
- Pass applicable Debug/Release checks and package validation. Record detailed
  counts, evidence and limitations in the feature doc; mark this current checklist
  rather than appending an outdated statement that the feature is still missing.
