# Soul of Darkness roadmap

Updated 2026-10-31 after the level-1 first-encounter pass. This is the current
remaining-work list. Historical implementation notes are in
[ROADMAP_HISTORY.md](ROADMAP_HISTORY.md); their old pending/next descriptions
are superseded. Update the lists below when work lands instead of appending
another conflicting progress narrative.

## Target and current status

- Target: Gameloft Soul of Darkness J2ME 1.3.7, nine gameplay levels.
- Input: `roms/Soul-of-Darkness_J2ME_EN_v137.jar`, 347896 bytes,
  SHA256 `9d839e59d6cb2f80575736fbf867babe0da852312652490dd9025d0a0654dd6b`.
- All six classes decompile; generated reference Java compiles and has selected
  original-bytecode checks. This does not establish full runtime equivalence.
- Native Windows startup reaches English title/menu/credits/prologue and the
  playable first-level slice. The executable is no longer just a frame test.
- Native level 0 supports movement/collision, ground/flying enemies, melee,
  fire/ice magic, pickups/chests/rewards, platforms/chandeliers/bridges, regions,
  all 16 passages, the arena/reward/gate chain, six beam hazards and nine placed
  tutorial prompts. Intro trigger 82/script 76 now plays with English dialogue,
  camera control, skip and verified return to gameplay. Trigger 161/script 160
  now runs the enemy/chandelier sequence during ordinary play. Linked triggers
  43/61 remove their chandelier tutorial prompts. Camera controllers 63/167 now
  provide linked-enemy framing and falling-camera tracking. Exit 173 now enters
  closing bars/completion and acknowledges to the current selection card.
  Completion now saves progress, unloads the world and selects chapter 2 with
  entry disabled until its native gameplay is ready; chapter 1 remains replayable.
  See the NATIVE_*.md docs.
- Six first-level checkpoints support R/menu Restart and desktop autosave/Continue.
  The desktop format covers the supported native subset; it is not original RMS.
- All nine selected data bundles and players load against 983 bytecode rows;
  the shared owner keeps maps, records, scripts, assets and checkpoints aligned.
  Cached links and projectile/rider references detach before targets unload.
  See [NATIVE_SELECTED_LEVELS.md](NATIVE_SELECTED_LEVELS.md).
- All nine gameplay maps decode/render, but normal gameplay constructs **level 0
  only**. The other eight levels are not yet playable through the frontend.
- Rendering supports portrait/widescreen widths; simulation runs at 83 ms.
  Gameplay interpolation and audio are not implemented. The full game is unfinished.

Indices below are zero-based: level 0 is the first level, level 1 the second;
gameplay pack = level index + 3.

## Next: finish the first-level flow

- [x] Implement type-20 intro trigger **UID 82** and cutscene **script 76**.
  Its tracks control enemy 19, flying actors 83/84, player 186, prompt 267 and
  the camera; implement flags, positions, actor removal, anchored dialogue,
  input lock/skip and return to ordinary play. Completed and verified in
  [NATIVE_INTRO.md](NATIVE_INTRO.md).
- [x] Implement type-40 **UID 161 -> script 160**: the player-overlap trigger
  starts the track for enemy **205** and chandelier **34**. Add its flag/action/
  animation/position commands and completion cleanup. Completed and verified in
  [NATIVE_CHANDELIER.md](NATIVE_CHANDELIER.md); script 165 already runs.
- [x] Implement type-40 **UIDs 43 and 61**, which link chandeliers **3/34** to
  removal of prompts **187/59**. Completed with original-bytecode handler and
  ordered-world comparisons; see [NATIVE_PROMPT_REMOVAL.md](NATIVE_PROMPT_REMOVAL.md).
- [x] Implement type-49 camera controller **UID 63**, including its link to
  **UID 202**, and controller **UID 167**. Check activation/release, linked-source
  removal and falling-camera behavior at widths 240/569. Completed with
  handler, active-camera and ordered-world comparisons; see
  [NATIVE_CAMERA_CONTROLLERS.md](NATIVE_CAMERA_CONTROLLERS.md).
- [x] Implement type-20 exit **UID 173**: normal overlap enters completion state
  15, a defeated player cannot trigger it, and the closing-bars/acknowledge flow
  runs once. Completed with original exit/bar/input checks and a native
  selection handoff; see [NATIVE_COMPLETION.md](NATIVE_COMPLETION.md).
  Its completion transition is implemented in [NATIVE_FINISH.md](NATIVE_FINISH.md).
- [x] Implement `finishLevel`: update completion/max-unlock progression, persist
  it, unload the old world and select the next level. Add original-bytecode
  checks for the completion/save/Continue branch. Completed for native level 0;
  version-2 desktop saves retain version-1 migration. Next-level selection is
  gated until its construction/handlers are playable; see [NATIVE_FINISH.md](NATIVE_FINISH.md).
- [x] Make native world construction accept a selected level instead of hardcoded
  pack 3/level 0. Keep player, assets, actor records, scripts and checkpoints on
  the same selected level; detach all borrowed actor/platform references on unload.
  Implemented with a shared data owner, 983 original-bytecode input rows and
  explicit teardown. Gameplay beyond level 0 stays gated; see [NATIVE_SELECTED_LEVELS.md](NATIVE_SELECTED_LEVELS.md).
- [ ] Construct the second-level actors and resolve their unsupported handlers
  before enabling entry to that level. Its newly required placed types include
  **3, 80 and 89**; existing types also need their level-1 variants checked.
  Types 89, 80 and 3 now have verified handlers and controlled link/contact
  integration. Type 80 is an energy barrier, not a collapsing platform. Type-3
  actors match 11487 bytecode rows and 48 world fixtures (see NATIVE_TYPE3_ACTORS.md);
  player-overlap trigger 308 now enables actors 62/74. The level-1 first encounter (triggers 100/104/62/103,
  chest 59, script 95/gate 87) runs in controlled worlds; see NATIVE_LEVEL1_ENCOUNTER.md. The other enabling
  chains (115/118/123, 187, 322), the type-2 behavior-5 records (UIDs 136/137/181) and the actual route remain
  pending. The type-1 respawn-fade record (UID 96) is verified in NATIVE_ENEMIES.md.
- [ ] Record and verify an actual run from first-level spawn through its arena,
  reward/gate, passages, beams, final room and exit. The current controlled host
  fixtures start at selected locations; they are not that full play-through.

## Remaining gameplay across levels 1-8

- [x] Port the **type-16 thrown-object foundation**: held ownership, flight,
  reflection, explosion, target contacts and cleanup. Verified in 6507 bytecode
  rows and twelve controlled world fixtures; see [NATIVE_THROWN_OBJECTS.md](NATIVE_THROWN_OBJECTS.md).
- [x] Port the **type-3 decision foundation**: all three behavior decisions,
  action entries and fixed-point throw aiming. Verified with 10922 bytecode
  rows; see [NATIVE_TYPE3_DECISIONS.md](NATIVE_TYPE3_DECISIONS.md).
- [x] Integrate enemy **type 3** in levels 1-2: charger, thrower and linked-chest
  runner with held-shot birth/release, parent/child dispatch order, death/loot,
  respawn fade and region reset. Verified against 11487 original-bytecode rows and
  48 controlled shipped-record world fixtures; see [NATIVE_TYPE3_ACTORS.md](NATIVE_TYPE3_ACTORS.md).
  Level 3's two type-3 actors stay with the paired-mode item below.
- [ ] Port enemy **types 13/14/70** and their damage/projectile variants; complete
  later-level variants of the already ported type-1/type-2 enemies.
- [x] Port **type 89** timed-switch handlers: fire activation, timers, linked-gate
  conditions and animation transitions. Verified in 4153 bytecode rows and
  controlled native gate worlds; see [NATIVE_TIMED_SWITCHES.md](NATIVE_TIMED_SWITCHES.md).
- [x] Port **type 80** energy barriers: lifecycle, priority collision, selected
  second-level enable/release triggers and cached lightning. Verified with 3776
  bytecode rows and twelve controlled world runs; see [NATIVE_ENERGY_BARRIERS.md](NATIVE_ENERGY_BARRIERS.md).
- [ ] Port remaining placed controllers/objects: **37** gate controller,
  **61/79** destructibles, **64** springs, **68** wave spawners, **69** paired
  owners, **72** linked platforms, **73** hazards, **74** hazard spawners,
  **75** traps, **78** hazard areas, **82** platform groups and **95** crushers.
- [ ] Port bosses **90** (level 3) and **100** (level 8), including complete
  phases, projectiles, scripted entry/exit and boss-to-level-completion ownership.
- [ ] Integrate level **3** paired-actor mode (including its two type-3 actors) and
  level **5** controller/hazard/platform-group state natively. Their Java checkpoint checks are not native ports.
- [ ] Construct placed **type 48** ability actors and **type 39** effects used
  outside level 0; finish remaining pickup/reward and moving-object variants.
- [ ] Expand type-40 trigger ownership beyond the selected first-arena/chest/gate
  and chandelier chains; verify each later-level source/target condition.
- [ ] Walk every level's shipped records and scripts through their full route;
  compare behavior against the original and test each boss/exit/restart boundary.

## Scripts, rendering and ownership still to finish

- [ ] Execute camera tracks, global tracks and cinematic actor tracks, and extend
  existing-actor tracks beyond the selected scripts 76/160/165. Decoding
  all 17 scripts is already done; executing them all is not.
- [ ] Implement dialogue, cutscene input locks/skip, QTEs and the remaining
  script global/property commands, fades, shakes and letterbox transitions.
- [ ] Connect dynamic type-21 prompts owned by chests/enemies/camera/player-follow
  interactions beyond the existing chest prompt. Placed tutorials are now done.
- [ ] Complete original UID/pool and mixed actor dispatch/painter ownership;
  remove the remaining external container/pickup and endpoint-glow timing limits
  documented in NATIVE_WORLD_ORDER.md, NATIVE_CHECKPOINTS.md and NATIVE_BEAMS.md.
- [ ] Port remaining non-tile background/weather effects and boss/HUD/cutscene visuals.
  The existing health/magic HUD is a desktop design, not original HUD parity.
- [ ] Add render-only interpolation for player, actors, platforms and camera,
  snapping at passages/restarts/scene changes. Keep collision/events/saves on ticks.

## Saves, interface, audio and distribution still to finish

- [ ] Extend the existing version-2 level-0 completion/unlock saves to later
  levels, upgrade arrays and remaining flags; support checkpoints/Continue
  across every playable level while retaining version-1 migration.
- [ ] Port original level-3/5 special save snapshots and implement original
  1313-byte RMS import/compatibility if that compatibility is to be offered.
- [ ] Implement the animated level-selection map, unlocked-level selection and
  the selected-level stats/checkpoint-position decision.
- [ ] Add weapon/magic upgrade screens and their native rank/progression ownership.
- [ ] Finish Help/settings/pause/game-over and remaining completion/ending menu flows and expose
  supported display/input options through the interface.
- [ ] Translate remaining dialogue/tutorial/menu resources that become visible;
  preserve credited names. The supplied resource set contains Czech text.
- [ ] Implement music/effects, volume/mute and focus-loss/resume audio behavior.
- [ ] Add remappable keyboard and controller input; verify menus and gameplay
  share consistent bindings and edge/held semantics.
- [ ] Add a user-JAR import launcher with identity checks and an asset-free
  Windows package. The current data-free executable package already builds.
- [ ] Complete continuous play-through and save round-trip checks across all nine
  levels at widths 240/569, then 427/747, live resize/fullscreen and FPS presets.
- [ ] Configure publishing/updating only after a real destination is supplied;
  no repository or hosting destination is currently configured.

## Verification and implementation detail

- Run default `python tools/rename_sod.py` after Java naming/patch/check changes.
- Run Debug/Release builds, checks appropriate to each native change and
  `soul/port/build_dist.bat`; retain all data and screenshots outside git.
- Linked prompt removal adds 352 original-bytecode/native rows and verified
  removal in the chandelier host fixtures; see [NATIVE_PROMPT_REMOVAL.md](NATIVE_PROMPT_REMOVAL.md).
- Camera controllers add 1368 handler/camera/world rows and 58 controlled host
  checks at 240/569; see [NATIVE_CAMERA_CONTROLLERS.md](NATIVE_CAMERA_CONTROLLERS.md).
- Exit/completion adds 1008 original-bytecode/native rows and 58 host checks;
  see [NATIVE_COMPLETION.md](NATIVE_COMPLETION.md).
- Completion/progression adds 444 original/reference expectations, including
  actual RMS writes and unload, plus 24 native decision rows and 36 cross-process
  finish/Continue/replay checks; see [NATIVE_FINISH.md](NATIVE_FINISH.md).
- Current baseline: 543 player assertions, 25 timed-switch assertions, 17 barrier
  assertions, 15 thrown-object assertions, 26 type-3 decision assertions, 20 type-3
  actor assertions and 109 CTests. Type-3 decisions add 10922 bytecode rows and the
  integrated actor adds 11487 rows plus 48 world and 4 presentation fixtures; see
  [NATIVE_TYPE3_DECISIONS.md](NATIVE_TYPE3_DECISIONS.md) and [NATIVE_TYPE3_ACTORS.md](NATIVE_TYPE3_ACTORS.md). Script 160 adds 3000
  original-bytecode rows and 24 host runs at both widths; see
  [NATIVE_CHANDELIER.md](NATIVE_CHANDELIER.md). The intro retains its 5460 rows
  and 48 playback/skip host runs; see [NATIVE_INTRO.md](NATIVE_INTRO.md).
- [PORT_ROADMAP.md](PORT_ROADMAP.md) gives the native implementation order and
  concrete acceptance conditions; feature docs retain detailed evidence/limits.
