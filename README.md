# Soul of Darkness Remastered

Decompilation and Windows port workspace for Gameloft's **Soul of Darkness**,
J2ME v1.3.7. Follows the approach in `../tes-travels-decomp`,
`../gow-betrayal-decomp` and `../rac-j2me-decomp`: recover Java, verify and rename
it, document assets, then build a native C++17 port with CMake, Ninja and MSVC.

**Launching the Windows port now opens a logo/title/menu flow when game data is found. New Game enters the playable first-level slice with an in-game HUD, movement, combat, loot and a rider-controlled platform. The complete game remains unfinished.**
All six classes are decompiled and the renamed Java compiles with zero errors.
The Game/BSprite/Actor read-throughs map 236 fields and 354 methods,
with behavioral checks against the original bytecode. All 36 sprites decode
and validate, including PNG templates, custom palettes, frame bounds and
animation tables. Preview them with `python tools/parse_sprites.py --sheet soul/analysis/sprites.png`.
The Actor checker now reproduces walking, jumping, crouching, melee poses,
wall/ceiling response and pause across 700 controlled reference ticks. These
are Java analysis scenarios; they do not establish complete gameplay equivalence.
Selected enemy/object fixtures add 320385 state/world comparisons across 16
types, including pickups, containers, gates, traps and opening boss sequences.
Object-stream envelopes validate across 930 records; all 20 actor stat tables
also consume exactly. See [enemy/object coverage](soul/docs/ENEMIES_AND_OBJECTS.md)
for the controlled setup and remaining branches.
All 12 map packs and 17 scripts now parse; 852 Java checks cover map loading
and selected script timelines, with 64 independent Python oracle comparisons.
The timeline checks caught and corrected a decompiler error that advanced
script commands one tick early. See [scripts and levels](soul/docs/SCRIPTS_AND_LEVELS.md).
In-memory save serialization/restoration now passes 2398 checks, preserving
the original flag-order quirk. See [save layout and boundaries](soul/docs/SAVES.md).
All nine level actor streams construct against the original bytecode in fresh,
restart and removed-UID fixtures; selected level 5 saved links also restore.
See [construction coverage](soul/docs/LEVEL_ACTORS.md) for the tested boundaries.
Checkpoint activation and two successive restarts now run against original
bytecode on all nine levels with an isolated in-memory RMS adapter. Repeated
loads also verify respawn-pool reuse; see [lifecycle coverage](soul/docs/CHECKPOINT_LIFECYCLE.md).
New Game, Continue and selected loading stages add 560 checks; see
[startup save decisions](soul/docs/NEW_GAME_AND_CONTINUE.md) for their boundaries.
Level 5's controller, hazard and three live linked snapshots now round-trip
through Restart/Continue; see [controller coverage](soul/docs/LEVEL5_CONTROLLER.md).
The 19 numeric asset containers and promotion container validate with exact
byte consumption. The Windows executable provides native startup and explicit asset/movement
previews, with a resizable widescreen viewport, fullscreen and FPS options.
Interpolation currently applies to the calibration marker.
The native resource library now validates the containers too; its separate
`soul_assets` tool matches all 252 Python manifest rows. See
[native resources](soul/docs/NATIVE_RESOURCES.md) for supported formats and checks.
Native BSprite decoding now covers all 36 sprites, matching 18082 Python manifest
rows after the original-Java oracle check. See [native sprites](soul/docs/NATIVE_SPRITES.md).
Native sprite rendering and an explicit asset viewer now work, with 15224 render
comparisons at widths 240/569. See [rendering and preview](soul/docs/NATIVE_RENDERING.md).
Native bitmap text adds wrapping, palette controls, anchors and inline icons;
334 original-Java layout rows and 240 text surfaces pass. See
[native fonts](soul/docs/NATIVE_FONTS.md).
Native tile drawing now displays all nine gameplay maps at widths 240/569,
with 45 original-Java map rows and 324 pixel comparisons. See
[native tiles](soul/docs/NATIVE_TILES.md). A first-level native player prototype
now supports walking, jumping, crouching and a following camera. Its 600 controlled
ticks match 1200 original-Java snapshots. See [native player](soul/docs/NATIVE_PLAYER.md).
The first level also constructs and renders its twelve health pickups, with
attraction, collection and capped meter updates. See [native pickups](soul/docs/NATIVE_PICKUPS.md).
Four ordinary first-level chests now open with E/J/Enter and emit crystals;
two more wait for the arena waves and their unlock trigger. Crystal rewards now count fragments,
update meters and return to gameplay. See [native chests](soul/docs/NATIVE_CONTAINERS.md)
and [crystal rewards](soul/docs/NATIVE_CRYSTALS.md).
Collection effects now follow the original spawn/removal timing, and collected
pickups hide immediately; see [collection effects](soul/docs/NATIVE_COLLECTION_EFFECTS.md).
Native player side-contact helpers now match controlled original-bytecode
scenarios; see [contact coverage](soul/docs/NATIVE_CONTACTS.md). They now connect to the preview
through live ordinary-enemy body queues.
Standing/combo/aerial attacks and their slash animation now work on E/J/Enter;
see [native melee](soul/docs/NATIVE_MELEE.md). A controlled type-1 AI slice now matches the original; see
[native enemies](soul/docs/NATIVE_ENEMIES.md). Damage, hurt/death poses and
combat effects now have controlled native checks; see [native combat](soul/docs/NATIVE_COMBAT.md).
Ordinary enemy cleanup, respawn fades and delayed loot now have native checks;
see [native deaths and loot](soul/docs/NATIVE_DEATHS.md). The preview now loads
twenty type-1 enemies, including two enabled by the first arena trigger, with damage, loot and defeat/restart; see
[live combat](soul/docs/NATIVE_LIVE_COMBAT.md). Full level gameplay remains unfinished.
Seven first-level flying enemies now load as well; the first arena's final
removals enable its second wave through trigger 54. See
[flying enemies and second wave](soul/docs/NATIVE_FLYING_ENEMIES.md).
The following chest-unlock trigger now reveals both arena chests; chest 57's
magic reward and removal open the gate through the original trigger sequence.
See [arena progression](soul/docs/NATIVE_CHEST_UNLOCK.md).
After acquiring magic, K/0 now casts the original radial fire burst, with enemy
damage, MP cost and timed recovery. See [fire magic](soul/docs/NATIVE_FIRE_MAGIC.md).
The ice reward owner, ice melee and Q/9 switching now have native checks too;
the original pickup is in level 2. See [ice weapons](soul/docs/NATIVE_ICE_WEAPONS.md).
Equipped ice magic now fires damaging shots and creates temporary wall footholds,
including jumping onto them and rider detachment on replacement or expiry.
See [ice projectiles](soul/docs/NATIVE_ICE_PROJECTILES.md) for verified branches
and its remaining handler boundaries.
Moving-object ice links now have separate original-bytecode checks; the shipped
shot passes through type-33 objects because its attack rectangle is a point.
See [attachment behavior and limits](soul/docs/NATIVE_ICE_ATTACHMENTS.md).
The original opening cutscene now plays in the first level, with four English
dialogues, camera moves and Backspace to skip. See [native intro](soul/docs/NATIVE_INTRO.md).
The first-level enemy/chandelier sequence (trigger 161/script 160) also runs
during normal gameplay; see [native chandelier script](soul/docs/NATIVE_CHANDELIER.md).
The first level now loads all nine placed keyboard tutorial hints; see
[native prompts](soul/docs/NATIVE_PROMPTS.md). Linked chandelier triggers now
remove their two tutorials through the original overlap conditions; see
[prompt-removal links](soul/docs/NATIVE_PROMPT_REMOVAL.md). The two first-level
camera controllers now frame Kale with their linked enemy or track his fall;
see [camera controllers](soul/docs/NATIVE_CAMERA_CONTROLLERS.md). The first-level
exit now closes the bars and shows LEVEL COMPLETE; Enter/E/J returns to the
current selection card. Completion saves/unlocks and next-level advancement
remain pending; see [exit/completion](soul/docs/NATIVE_COMPLETION.md).
The first level now loads all six timed/sweeping beam hazards, with tile-clipped
endpoints, damage and knockback. See [native beams](soul/docs/NATIVE_BEAMS.md).
All six first-level checkpoints now work and autosave to the desktop user slot. R or
pause Restart Level restores the latest checkpoint, saved weapon/abilities and
MP, and refills health. See [native checkpoints](soul/docs/NATIVE_CHECKPOINTS.md).
Continue resumes the latest first-level checkpoint after closing the executable. See [desktop saves](soul/docs/NATIVE_SAVES.md) for the format and remaining boundaries.

## Setup

Requirements: Python 3.10+, JDK 17+ with `javac`/`javap`, CMake 3.20+, and
Visual Studio Build Tools with Desktop development with C++ and Ninja.
The installed tools on this machine have been verified. Ninja comes from the
Visual Studio environment; it need not be on your ordinary PowerShell PATH.
No Python packages are required. Java 8 on PATH is bypassed automatically.

From PowerShell in this folder:

```powershell
.\setup.ps1
# If your local PowerShell execution policy blocks scripts:
powershell -NoProfile -ExecutionPolicy Bypass -File .\setup.ps1
```

This provisions hash-checked Vineflower 1.12.0 and API stubs from the sibling
projects, copies the supplied JAR into `roms/` if needed, extracts, decompiles,
regenerates/compile-checks the reference source, validates containers, and builds
and tests the Debug scaffold. `-SkipBuild` runs only the Java/data pipeline.
See [tools/README.md](tools/README.md) for dependency provenance and fresh-machine setup.

```powershell
.\soul\port\build.bat                 # Debug + CTest
.\soul\port\build.bat Release         # Release + CTest
.\soul\port\build_dist.bat            # package scaffold in soul/port/dist/
.\soul\port\build\Debug\soul_port.exe --width 569 --fps 60
.\soul\port\build\Debug\soul_assets.exe --data soul/extracted
python tools/check_native_assets.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build\Debug\soul_assets.exe --data soul/extracted --sprites
python tools/check_native_sprites.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build\Debug\soul_port.exe --sprite 2:0 --data soul/extracted --animation 1
.\soul\port\build\Debug\soul_port.exe --font-preview --data soul/extracted --width 569
.\soul\port\build\Debug\soul_port.exe --level-preview 0 --data soul/extracted --width 569 --camera-y 1320
.\soul\port\build\Debug\soul_port.exe --movement-preview --data soul/extracted --width 569
.\soul\port\build\Release\soul_port.exe --game --data soul/extracted
```

With the local extracted data present, double-clicking the Debug, Release or dist
executable now opens the Gameloft logo, original title art and a native menu.
Choose **Continue** when a saved checkpoint exists, or **New Game** to read the English prologue, then press Enter for the
first-level selection and Enter again to enter the first level. The opening
cutscene plays automatically: Enter/E/J advances dialogue and Backspace skips. Checkpoints autosave to `%LOCALAPPDATA%/SoulOfDarkness/checkpoint.sav`; New Game confirms before replacing that progress. Up/Down or
W/S accelerate the story scroll while held. **Original credits** opens the
credits. Arrows/W/S navigate and Enter selects. Escape pauses gameplay; Resume
continues the same session, Restart Level restores its latest checkpoint, and Main Menu leaves play.
Losing window focus also pauses. The native HUD displays health and magic. After acquiring magic, press K or 0 to cast the equipped blade's fire or ice spell; it costs 15 MP and MP recovers over time. Q/9 switches weapons after the second-level ice pickup is acquired. Pass `--calibration` for the old frame test.
Missing game data now produces a clear startup error instead of silently opening
the frame test. `--calibration` and ordinary headless runs remain data-free.
The supplied JAR contains Czech text; currently displayed story, title prompt,
chapter labels and credit headings use a desktop English translation.
See [startup scope](soul/docs/NATIVE_STARTUP.md); audio, the animated
selection map and complete original menu behavior remain pending.

## Display and timing

The original canvas is 240×320. Width presets are 240 (portrait reference),
427 (4:3), 569 (16:9) and 747 (21:9); the logical height stays 320.
The host letterboxes its logical canvas without stretching. Normal gameplay
reveals more of the active room horizontally; narrow rooms are centered without
showing unrelated packed map sections. Full original scripted camera behavior
and gameplay interpolation remain pending.

FPS can be `native` (1000/83), or any integer 1–360; the keyboard cycles native,
30, 60, 120 and 144. The original `e.run()` requests 83 ms per iteration, about
12.05 updates/second. The scaffold keeps that simulation rate independent of
presentation and interpolates its calibration marker between ticks. This is
timing infrastructure, not a claim of completed game interpolation.

F8 cycles aspect, F9 FPS, F10 interpolation, F11 fullscreen, F5 saves settings,
Esc pauses during gameplay. Settings load from `display.ini` beside the executable. Copy
`soul/port/display.example.ini` there, or pass `--config PATH`. CLI overrides
the file. Settings are written only on F5; automated runs ignore the saved
file unless `--config` is supplied.

```powershell
.\soul\port\build\Debug\soul_port.exe --help
.\soul\port\build\Debug\soul_port.exe --self-test
.\soul\port\build\Debug\soul_port.exe --headless --width 569 --fps 144 --frames 144 --dump soul/analysis/scaffold.ppm
```

## Layout and next work

- `roms/`, `soul/extracted/`: original input/data, ignored by git.
- `soul/decompiled/`: raw Vineflower Java; regenerate with `tools/decompile.py`.
- `soul/src/`: renamed, compile-checked reference, generated by `tools/rename_sod.py`.
- `soul/docs/`: [roadmap](soul/docs/ROADMAP.md), [class map](soul/docs/CLASS_MAP.md),
  [asset formats](soul/docs/ASSET_FORMATS.md), [native port plan](soul/docs/PORT_ROADMAP.md).
- `soul/port/`: Windows scaffold; `src/main.cpp` is hand-written C++.
- `.vscode/`: build/regeneration tasks and MSVC debugger configuration.
- `.github/workflows/ci.yml`: data-free Debug/Release builds, timing checks and scaffold artifact.

The core read-through is documented in [STATE_MACHINE.md](soul/docs/STATE_MACHINE.md)
and [INPUT.md](soul/docs/INPUT.md); [BSPRITE.md](soul/docs/BSPRITE.md) covers the
completed sprite decoding pass. [ACTOR.md](soul/docs/ACTOR.md) covers the verified
movement/collision/player core. [ENEMIES_AND_OBJECTS.md](soul/docs/ENEMIES_AND_OBJECTS.md)
covers type handlers and selected AI scenarios. [SCRIPTS_AND_LEVELS.md](soul/docs/SCRIPTS_AND_LEVELS.md)
covers tile grids and the script interpreter. [SAVES.md](soul/docs/SAVES.md)
covers save buffers and selected restoration paths. [LEVEL_ACTORS.md](soul/docs/LEVEL_ACTORS.md)
covers shipped actor construction and selected relinking.
[CHECKPOINT_LIFECYCLE.md](soul/docs/CHECKPOINT_LIFECYCLE.md) covers controlled
checkpoint/restart and repeated loads. [NEW_GAME_AND_CONTINUE.md](soul/docs/NEW_GAME_AND_CONTINUE.md)
covers startup save decisions and selected loading stages.
[NATIVE_RESOURCES.md](soul/docs/NATIVE_RESOURCES.md) covers native readers/container
access. [NATIVE_SPRITES.md](soul/docs/NATIVE_SPRITES.md) covers the native decoder.
[NATIVE_RENDERING.md](soul/docs/NATIVE_RENDERING.md) covers sprite drawing and the
asset viewer. [NATIVE_FONTS.md](soul/docs/NATIVE_FONTS.md) covers bitmap text and
its preview. [NATIVE_TILES.md](soul/docs/NATIVE_TILES.md) covers maps, tile drawing
and camera panning. [NATIVE_PLAYER.md](soul/docs/NATIVE_PLAYER.md) covers the
first-level movement prototype and its limits. [NATIVE_PICKUPS.md](soul/docs/NATIVE_PICKUPS.md)
covers basic pickups. [NATIVE_CONTAINERS.md](soul/docs/NATIVE_CONTAINERS.md) covers
chest interaction and spawned crystals. [NATIVE_CRYSTALS.md](soul/docs/NATIVE_CRYSTALS.md)
covers rewards and return to gameplay. [Collection effects](soul/docs/NATIVE_COLLECTION_EFFECTS.md)
covers pickup disappearance and effect lifetime. [Side contacts](soul/docs/NATIVE_CONTACTS.md)
covers the new native collision helpers and controlled player integration.
[Native melee](soul/docs/NATIVE_MELEE.md) covers weapon-0 attack control and presentation.
Type-1 activation/pursuit/attack AI now has a verified native slice.
Damage/hit response and ordinary enemy death/loot/reset now have controlled
native coverage and now run in the first-level combat preview. Tile hazard
damage is now connected; next are remaining object contacts/platforms and fuller
world/region ownership. See [tile hazards and the black flashing fix](soul/docs/NATIVE_HAZARDS.md).
The player now has verified [platform landing/support](soul/docs/NATIVE_PLATFORMS.md).
[Type-33 movement](soul/docs/NATIVE_MOVING_PLATFORMS.md) now runs level 0's rider platform.
[Type-50 chandeliers](soul/docs/NATIVE_SWINGING_PLATFORMS.md) now swing, carry the
player, and fall/break when their cuttable chains are struck. All six are loaded.
[Region lifecycle](soul/docs/NATIVE_REGIONS.md) now restores ordinary enemies on
region exit and makes them available on return. General world ownership and
remaining object handlers are next. Both first-level [breakable bridges](soul/docs/NATIVE_BRIDGES.md)
now render, supply collision and collapse when struck by a falling chandelier.
[Transient cleanup](soul/docs/NATIVE_TRANSIENTS.md) now removes existing dropped
pickups and collection effects on region changes while preserving new births.
[Ordered world dispatch](soul/docs/NATIVE_WORLD_ORDER.md) now runs regions among
the supported placed actors, so later enemies use newly selected bounds in the same tick.
[Linked area triggers](soul/docs/NATIVE_LINKED_TRIGGERS.md) now connect the first
level's region-overlap trigger to two chandeliers' update flags.
[Linked gates](soul/docs/NATIVE_GATES.md) now provide the first-level doorway's
collision and clipped artwork, with verified opening/return controllers.
[Native scripts](soul/docs/NATIVE_SCRIPTS.md) now decode all script data and run
the gate's actual script-165 track in a controlled host fixture.
[Container-removal triggers](soul/docs/NATIVE_REMOVED_TRIGGERS.md) now connect
chest 57's removal to script 165 and clean up after completion. The native
[magic ability reward](soul/docs/NATIVE_ABILITIES.md) now completes its collection,
beam/tutorial and removal, with the repeated-unlock health fallback. Normal
chest activation still waits on its preceding enemy/trigger chain.
All sixteen first-level [horizontal passages](soul/docs/NATIVE_PASSAGES.md) now
connect the packed rooms, fixing the first tower wall that stopped the route.
Normal game cameras respect room bounds at portrait/widescreen widths; narrow
rooms are centered without showing unrelated neighboring sections.
The [first arena wave](soul/docs/NATIVE_ENEMY_ACTIVATION.md) now activates normally
through trigger 129, and its two type-1 enemies disappear after their final
allowed respawns. All 20 first-level type-1 actors load. The following type-2
wave and chest-unlock triggers are implemented; see NATIVE_FLYING_ENEMIES.md
and NATIVE_CHEST_UNLOCK.md.
For the current remaining-work lists, use [ROADMAP.md](soul/docs/ROADMAP.md)
and [PORT_ROADMAP.md](soul/docs/PORT_ROADMAP.md).
Do not copy the
Metaflow-specific translators or RP parsers from the siblings: this game's
engine and pack formats are different. No remote repository, updater or release
publishing target is configured. See [NOTICE.md](NOTICE.md).
