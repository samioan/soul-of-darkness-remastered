# Tooling

The extraction/decompilation scripts, Java 17 discovery and symbol-table renamer
are adapted from `../gow-betrayal-decomp/tools`, itself following the TES/Ratchet
workflow. `soul/port/vcvars.bat` is the same generic VS discovery used there.
All scripts resolve project paths relative to their own location.

| Command | Result |
|---|---|
| `python tools/bootstrap.py` | Verify/provision pinned tools, locate Java 17+ |
| `python tools/extract_jar.py` | CRC-checked extraction and ignored JSON inventory with JAR hash |
| `python tools/decompile.py` | Raw source only in `soul/decompiled/` |
| `python tools/rename_sod.py` | Symbol renames, asserted patches, javac and original-bytecode behavior checks |
| `python tools/outline.py Game` | Member line ranges; `--big N` filters large methods; `--calls NAME` approximates callers/callees |
| `python tools/bytecode.py e` | Original instructions in ignored `soul/analysis/e.bytecode.txt` |
| `python tools/inspect_assets.py` | Validate numeric pack offsets/bounds and dataIGP, stdout only |
| `python tools/check_native_assets.py --exe soul/port/build/Debug/soul_assets.exe` | Compare 252 native/Python container manifest rows; requires game data and a native build |
| `python tools/check_native_sprites.py --exe soul/port/build/Debug/soul_assets.exe` | Compare 18082 native/Python sprite rows after checking Python against the regenerated Java oracle |
| `python tools/check_native_render.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe` | Compare 15224 rendered surfaces and 14 preview runs at widths 240/569 |
| `python tools/check_native_fonts.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe` | Compare 334 original-Java font rows, 240 text surfaces and 12 preview runs |
| `python tools/check_native_tiles.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe` | Compare 45 original-Java map/binding rows, 324 tile surfaces and 28 level previews |
| `python tools/check_native_player.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe` | Compare 1200 original-Java movement snapshots, 930 actor records and twelve native movement runs |
| `python tools/check_native_pickups.py --exe soul/port/build/Release/soul_assets.exe` | Compare nine original-Java health/magic pickup scenarios, including attraction, collection, limits and pause |
| `python tools/check_native_containers.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe` | Compare 816 original-Java chest/spawned-pickup rows and twelve controlled native chest runs |
| `python tools/check_native_crystals.py --exe soul/port/build/Release/soul_assets.exe` | Compare 448 original-bytecode crystal reward/beam state rows with isolated drawing hooks suppressed |
| `python tools/check_native_effects.py --exe soul/port/build/Release/soul_assets.exe` | Compare 88 original-bytecode collection-effect snapshots across 24 scenarios |
| `python tools/check_native_contacts.py --exe soul/port/build/Release/soul_assets.exe` | Compare 628 contact bounds, 7056 original-bytecode arenas and 384 controlled player ticks |
| `python tools/check_native_melee.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe` | Compare 1920 original-bytecode melee/weapon snapshots and twelve first-level host runs |
| `python tools/parse_objects.py` | Validate all 930 object envelopes and 20 actor stat tables; included in setup |
| `python tools/parse_levels.py` | Validate 12 map packs and 17 scripts; `--oracle soul/analysis/script-oracle.tsv` compares decoded tracks |
| `python tools/parse_sprites.py` | Decode/validate all sprites; `--sheet soul/analysis/sprites.png` previews them |
| `python tools/actor_shots.py` | Draw verified Actor poses on a synthetic test map from ignored actor-trace.tsv |

`bootstrap.py` contains SHA256 pins for all tool JARs. Existing matching copies
are used first, then copies from `gow-betrayal-decomp`, `rac-j2me-decomp` or
`tes-travels-decomp` beside this project. Vineflower alone also has a pinned
download fallback:
[Vineflower 1.12.0](https://github.com/Vineflower/vineflower/releases/tag/1.12.0).
Vineflower is third-party software under Apache-2.0; its JAR is ignored.

API stub JARs (`midpapi20.jar`, `cldcapi11.jar`, `nokiaui.jar`) are copied from
`../rac-j2me-decomp/tools/midp-stubs/` here. These are compilation API definitions,
not a desktop runtime/emulator. The reference projects do not record a reliable
download source for these exact binaries; this project does not invent one.
On a machine without those siblings, provide the matching JARs in
`tools/midp-stubs/` before running setup; pins enforce the same environment.
The native scaffold/CI can build without Java, API stubs, Python or game data.

Renames go in `soul/docs/names.map`, using original class/member names and
Java-style parameter types. The reused `ChRenamer` operates on bytecode symbols,
including inherited members and Java keyword identifiers. Keep the `ch.map`
property name for compatibility. Every textual artifact fix in `PATCHES` must
match exactly once. Never hand-edit generated Java.

`tools/reference_checks/ReferenceCheck.java` is compiled into a temporary
directory during default regeneration. Separate class loaders execute the
original extracted class files and freshly compiled reference against the
same API definitions. It compares key mapping, static input latching, selected
non-graphical state transitions, binary helpers, music selection, strings and
trigonometry. It never constructs Canvas or calls audio/RMS services. This is
a partial behavioral oracle, not a game emulator; see INPUT.md and CLASS_MAP.md
for limits. `--no-check` skips both compilation and behavior checks.

Default regeneration also compiles/runs `SpriteCheck.java`. The sprite/Actor
checkers' isolated class loaders receive the test ImageIO-backed Image adapter in
`reference_checks/image_adapter/`; Canvas and Graphics remain unavailable as a
runtime. This lets original PNG-template replacement run while the main/core
checker retains its unchanged API-jar behavior. The sprite checker compares
original/recompiled metadata, decoded pixels, bounds, animation accessors, cache
registration and selected font helpers. It writes the ignored
`soul/analysis/sprite-oracle.tsv`, then invokes the independent Python decoder
to check its pixel hashes and bounds against that oracle. See BSPRITE.md for
what is and is not covered.

`ActorCheck.java` is also part of default regeneration. It executes original and
renamed Actor methods for geometry, fixed-point integration, update gates,
all 27 player animation clocks, animation queuing, 220 tile/slope probes and
700 scripted ticks (walk, jump, crouch, melee, wall, pause, ceiling). It creates
no Canvas and performs no Graphics drawing. See ACTOR.md for the controlled
fixture and omitted runtime paths. Before-animation poses are written to
ignored `soul/analysis/actor-trace.tsv`; `actor_shots.py` can render a contact
sheet from that trace. The sheet is analysis output, not a native game screenshot.

`game_assets.py` contains checked pack readers. `bsprite.py` contains the sprite
data model, pixel/PNG decoders and preview composer. `parse_sprites.py` validates
all sprites and can write explicit PNG previews only under ignored
`soul/analysis/`. No Pillow or other Python package is required.

`BehaviorCheck.java` is also run by default regeneration. It loads real actor
parameters, stats, sprite/font/string and trig resources and compares 16 selected
enemy/object types in controlled arenas, including spawned actors and selected
Game side effects. Independent expected-state assertions cover selected active
cases. Some fixtures remain idle; bosses cover opening sequences only.
See ENEMIES_AND_OBJECTS.md for setup and scope. No full Game update/paint loop,
Canvas, Graphics, RMS or media service is executed.

`ScriptCheck.java` adds map-loader, script-decoder/lifecycle and controlled actor/
camera timeline checks. It seeds checked single-part pack streams because the
original String.class resource lookup cannot see application assets on desktop
Java. It invokes unchanged original map readers, checks command timing,
interpolation, pause, flags, property/animation/action changes and loop/one-shot
completion, then writes ignored `soul/analysis/script-oracle.tsv`. Default
regeneration runs `parse_levels.py --oracle` to independently compare all
script IDs, copied track bytes/hashes and initial offsets. No full staged level
load, dialogue drawing, QTE, audio or RMS execution is implied. The checks found
a compiled-but-wrong Vineflower counter increment; PATCHES preserves the original
pre-increment frame value (see SCRIPTS_AND_LEVELS.md for bytecode evidence).

`SaveCheck.java` adds 2398 in-memory persistence checks: exact progress buffer
bytes, signed stats/upgrades, every progress flag (including the original
657/658 restore swap), removed-UID rows/counts, checkpoint coordinates/skip,
level 3 paired actors, level 5 Y correction and all three linked snapshot slots.
It invokes original and regenerated methods without RMS or full level loading.
See SAVES.md for the layout and remaining integration boundaries.

`LevelCheck.java` constructs actors from all nine shipped streams using real maps
and gameplay resources. Its 27 stream scenarios and selected level 5 linked-actor
reconstruction pass 17898 expectations and 6763 world comparisons.
It preserves inclusive removed-UID scanning, retained short-record buffer tails,
restart type filtering and dedicated type 69 ownership. No full staged loader,
gameplay ticks, graphics, audio or RMS is executed. See LEVEL_ACTORS.md.

`LifecycleCheck.java` runs original checkpoint activation and two complete
restarts per level across all nine shipped streams, then repeated loads through
packs 3/4/8. It checks 1737 expectations, 384 level comparisons and 7908 world
comparisons. A separate harness contains a minimal in-memory RMS adapter;
existing checkers keep their previous runtime boundaries. Tests include record
read/write bytes, removal-before-save, restored position, health/magic, scripts,
occupied cache clearing and retained UID-table/pool aliases. It does not provide
a MIDP runtime or native save backend. See CHECKPOINT_LIFECYCLE.md.

`MenuCheck.java` runs in the isolated lifecycle harness with test RMS. It checks
confirmed/cancelled New Game, the separate empty-save branch, Continue level
selection and selected loading stages 1/79/80, plus the stat helper for selected
levels. Its 560 expectations and scene comparisons preserve the original fresh
actor override on Continue. A pre-seeded image and opaque API Graphics instance
avoid rendering calls; no mutable MIDP Graphics runtime is supplied. See
NEW_GAME_AND_CONTINUE.md for exact fixture boundaries and source evidence.

`ControllerCheck.java` runs in the isolated lifecycle harness with test RMS.
It exercises the shipped level 5 controller and linked hazard, checkpoint capture
with three constructor-linked actors, Restart/Continue reconstruction and a
resumed common hazard update. Five signed camera/offset probes complement those
live fixtures. It passes 353 expectations, 132 level comparisons and 2575 world
comparisons. See LEVEL5_CONTROLLER.md for original arithmetic/ordering and scope.

`parse_objects.py` checks length-prefixed signed-short record envelopes from
packs 3..11 and the big-endian stat rows from pack 18 resource 0. Exact consumption
does not validate all type-specific parameters or script/UID references.

The outline tool is adapted from the sibling God of War project. Its call
search is lexical, so calls with the same name across overloads/classes may
be grouped together. Use it for navigation and verify real call sites before
adding a semantic rename.

No Java-to-C++ translator is enabled yet. The sibling translators target other
engines; adapting one before mapping this engine would silently encode wrong
assumptions. See `soul/docs/PORT_ROADMAP.md` for the staged native implementation.

The native build now includes `soul_assets`, a read-only numeric/promotion pack
validator. `soul/port/tests/resources_test.cpp` runs 113 synthetic assertions in
CTest without game data. Debug and Release also check tool help/bad arguments.
Setup compares native manifests to Python after building; CI builds/tests remain
data-free. See NATIVE_RESOURCES.md for borrowed-view lifetimes, unsupported split
packs and allocation limits.

The native `soul_sprites` library now decodes all 36 sprites, including original
PNG-template replacement and checked indexed PNG scanlines through Windows WIC.
`soul_assets --data DIRECTORY --sprites` validates them; `--sprite-manifest`
emits metadata/bounds and pixel fingerprints for the Python cross-checker.
Its 222 synthetic assertions run in CTest without game data. Setup runs both
native container and sprite comparisons after building. See NATIVE_SPRITES.md
for supported codecs, memory/graph bounds and remaining drawing work.

The native `soul_renderer` draws modules, frames, animation steps and standalone
images with clipping, alpha and masks. Its 40 synthetic assertions run data-free.
`soul_assets --render-manifest` and the Python render checker verify frame
composition; the optional host executable argument also tests the visible/headless
asset viewer's pixel output and independent animation clock. These are render
regressions, not original Graphics/device framebuffer comparisons. Setup includes
them after building. See NATIVE_RENDERING.md for preview controls and boundaries.

The controlled type-1 native AI checker runs after default Java regeneration:

```powershell
python tools/check_native_enemies.py --exe soul/port/build/Debug/soul_assets.exe
```

It compares 4,032 snapshots in 21 AI arenas with unmodified original bytecode.
`--enemy-manifest` is a read-only inspector mode; it does not load enemies into
the movement preview. See NATIVE_ENEMIES.md for the combat/queue boundaries.

Controlled damage, hit response and combat-effect comparisons:

```powershell
python tools/check_native_combat.py --exe soul/port/build/Debug/soul_assets.exe
```

Default regeneration exports its 2,591-row original-bytecode oracle. The
read-only `--combat-manifest` does not enable live combat in the movement
preview. See `soul/docs/NATIVE_COMBAT.md` for entry and death-cleanup boundaries.

Ordinary type-1 cleanup, record reset, respawn fade and delayed enemy loot:

```powershell
python tools/check_native_deaths.py --exe soul/port/build/Debug/soul_assets.exe
```

Default regeneration exports its 2,639-row original-bytecode oracle. The
read-only `--death-manifest` includes pickup reveal/collection/expiry and respawn
effects; it does not enable live enemies. See `soul/docs/NATIVE_DEATHS.md`.

Connected player/enemy combat and first-level host validation:

```powershell
python tools/check_native_live_combat.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

It compares 4,448 original Game.updateWorldActors snapshots plus twelve hazard
birth-effect rows and runs 24 native
kill/loot/defeat scenarios. See `soul/docs/NATIVE_LIVE_COMBAT.md` for live preview
controls, simulation-view policy and remaining gameplay boundaries.

Controlled type-33/50 platform landing, support and player actions:

```powershell
python tools/check_native_platforms.py --exe soul/port/build/Debug/soul_assets.exe
```

Default regeneration exports 10,944 original-bytecode snapshots. The read-only
`--platform-manifest` does not load platform handlers into the displayed preview;
see `soul/docs/NATIVE_PLATFORMS.md` for ownership and remaining integration.

Type-33 motion/live riding and native startup/artwork checks:

```powershell
python tools/check_native_moving_platforms.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_swinging_platforms.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_frontend.py --exe soul/port/build/Debug/soul_port.exe
```

Default Java regeneration includes MovingPlatformCheck's 11,520-row oracle.
These tools also run twelve live platform rides, twelve timed prologue runs and
twelve startup-to-game scripts, plus independent logo/title/story pixel checks
at widths 240/569. Startup scripts traverse credits, prologue and selection.
Default regeneration also runs SwingingPlatformCheck's fifteen arenas and
15840-row oracle; the native checker adds twelve real-level chandelier rides.

`RegionCheck.java` runs with default regeneration and emits the ignored
`analysis/region-oracle.tsv`: 176 selection/reset snapshots across eight
controlled scenarios, with ordinary actor updates paused. Compare native output
using `python tools/check_native_regions.py --exe soul/port/build/Debug/soul_assets.exe`.
See [native region scope](../soul/docs/NATIVE_REGIONS.md).

`BridgeCheck.java`, run during default regeneration, emits ignored
`analysis/bridge-oracle.tsv` with 2040 bridge/grid/chandelier rows across eleven
controlled cases. Run `python tools/check_native_bridges.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
for the oracle comparison and twelve real-level drop fixtures. See
[native bridges](../soul/docs/NATIVE_BRIDGES.md) for scope.

`TransientCheck.java` runs with default regeneration and emits ignored
`analysis/transient-oracle.tsv`: 576 original-world region-sweep decisions with
ordinary handlers paused. `python tools/check_native_transients.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
compares the policy and twelve live transition/cleanup/new-birth fixtures. See
[native transient scope](../soul/docs/NATIVE_TRANSIENTS.md).

`WorldOrderCheck.java` runs with default regeneration and emits ignored
`analysis/world-order-oracle.tsv`: 3840 original-world actor/region/queue snapshots
over 480 controlled ticks. Run `python tools/check_native_world_order.py --exe soul/port/build/Debug/soul_assets.exe`.
See [ordered world scope](../soul/docs/NATIVE_WORLD_ORDER.md) for player setup,
paint boundaries and remaining owners.

`LinkedTriggerCheck.java` runs with default regeneration and emits ignored
`analysis/linked-trigger-oracle.tsv`: 384 controlled type-40 handler rows and
128 actual original-world queue rows. Compare with
`python tools/check_native_linked_triggers.py --exe soul/port/build/Debug/soul_assets.exe`.
The existing real-level chandelier checks also require UID 182 to be loaded.
See [native linked trigger scope](../soul/docs/NATIVE_LINKED_TRIGGERS.md).

`GateCheck.java` runs with default regeneration and emits ignored
`analysis/gate-oracle.tsv`: 2304 original-bytecode state/grid snapshots across
sixteen linked-gate scenarios. Run
`python tools/check_native_gates.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
to compare those rows, 160 independently composed clipped surfaces and twelve
first-level gate-load/region host fixtures. See [native gate scope](../soul/docs/NATIVE_GATES.md)
for the pending script-165 activation and remaining UID/save boundaries.

`GateScriptCheck.java` runs with default regeneration and emits ignored
`analysis/gate-script-oracle.tsv`: 1536 original-bytecode timer/gate/grid rows
across eight timelines on the real first-level collision map. Run
`python tools/check_native_gate_scripts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
to compare these rows, all 17 script envelopes/47 raw tracks and twelve actual
host gate timelines. `--movement-preview --gate-script --headless` is controlled
setup that explicitly starts script 165; normal chest/trigger activation is
pending. See [native script scope](../soul/docs/NATIVE_SCRIPTS.md).

`RemovedGateTriggerCheck.java` runs with default regeneration and emits ignored
`analysis/removed-gate-trigger-oracle.tsv`: 1536 original-bytecode source/trigger/
selected-script/gate rows across eight scenarios. Run
`python tools/check_native_removed_gate_triggers.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
for those rows and twelve live controlled source-removal fixtures.
`--gate-trigger-script` explicitly removes source 57; it does not execute its
unported chest/ability reward. See [removed-source scope](../soul/docs/NATIVE_REMOVED_TRIGGERS.md).

`EncounterGateCheck.java` runs with default regeneration and emits ignored
`analysis/encounter-gate-oracle.tsv`: 2112 original-bytecode rows for the second level's
condition-4 removed-source script trigger 103, selected script 95 and gate 87 across eleven
scenarios. Run `python tools/check_native_encounter.py --exe soul/port/build/Debug/soul_assets.exe`
for those rows and six controlled world fixtures of the full 100/104/62/103 chain.
See [encounter scope](../soul/docs/NATIVE_LEVEL1_ENCOUNTER.md).

`AmbushCheck.java` runs with default regeneration and emits ignored `analysis/ambush-oracle.tsv`:
12800 original-bytecode rows for the second level's chandelier scripts 23/24/114 (triggers
115/118/123, enemies 92/93/94, chandeliers 89/90/91, selected-slot arrays) across twelve scenarios.
Run `python tools/check_native_ambush.py --exe soul/port/build/Debug/soul_assets.exe` for those rows
and seven controlled world fixtures. See [ambush scope](../soul/docs/NATIVE_LEVEL1_AMBUSH.md).

`SineFlyerCheck.java` runs with default regeneration and emits ignored `analysis/sine-flyer-oracle.tsv`:
2663 original-bytecode rows for type-2 behavior 6 (the second level's sine-wave flyers, a real region
actor as `Game.au`) across eleven scenarios. `BeamCheck.java` now also covers the six level-1 emitters
(13152 rows). Run `python tools/check_native_sine_flyers.py --exe soul/port/build/Debug/soul_assets.exe` and
`python tools/check_native_beams.py --exe ...`; `soul_assets --level-audit-manifest` lists which native loaders
accept each level. See [loader envelope](../soul/docs/NATIVE_LEVEL1_ENVELOPE.md).

`LevelOneIntroCheck.java` runs with default regeneration and emits ignored `analysis/level1-intro-oracle.tsv`:
4760 original-bytecode rows for the second level's intro (trigger 134, script 133) across seven scenarios.
Run `python tools/check_native_level1_intro.py --exe soul/port/build/Debug/soul_assets.exe` for those rows and
four controlled world fixtures. See [intro scope](../soul/docs/NATIVE_LEVEL1_INTRO.md).


`PassageCheck.java` runs in default regeneration and writes ignored
`analysis/passage-oracle.tsv`: 464 original-bytecode player/passage rows over
13 boundary cases and all 16 first-level links. Run
`python tools/check_native_passages.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
for those rows and 24 controlled first-passage/room-view fixtures. The game fixture
uses `--game --game-passage-script --headless`; the movement fixture uses
`--movement-preview --passage-script --headless`. Both place the player immediately
before source 81, then move normally; neither bypasses or edits map collision.
See [passage scope](../soul/docs/NATIVE_PASSAGES.md).


`AbilityCheck.java` and `AbilityModalCheck.java` run in default regeneration and
emit ignored `analysis/ability-oracle.tsv` and `analysis/ability-modal-oracle.tsv`.
The latter uses isolated drawing-only hooks, retaining original timer/input
bytecode. Run
`python tools/check_native_abilities.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
for all 2002 rows, 24 controlled chest/magic/trigger/gate host chains and 12 timed
English tutorial/HUD captures. `--movement-preview --ability-script --headless`
and `--game --game-ability-script --headless` explicitly unlock chest 57 and place
the player there; normal activation remains pending. See NATIVE_ABILITIES.md.


`ActivationCheck.java` runs in default regeneration and writes ignored
`analysis/activation-oracle.tsv`: 2146 one-shot trigger, actual world queue/enemy,
terminal death/respawn and final random-stream rows. Run
`python tools/check_native_activation.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
for the oracle and 24 first-wave melee/respawn/final-removal host fixtures.
`--movement-preview --activation-script --headless` and
`--game --game-activation-script --headless` explicitly place the player at the
arena with invulnerability; target activation and defeats use normal handlers
and input. No chest unlock or gate activation is forced. See NATIVE_ENEMY_ACTIVATION.md.

`FlyingEnemyCheck.java`, `FlyingWorldCheck.java` and `RemovedEnemyCheck.java`
run in default regeneration, comparing selected flight/combat/lifetime and
removed-source branches against original bytecode (6001 expectations). Their
ignored `analysis/flying-enemy-oracle.tsv`, `flying-world-oracle.tsv` and
`removed-enemy-oracle.tsv` contain 4671 native-comparable rows.
`python tools/check_native_flying_enemies.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
checks those rows and 24 two-wave arena fights at widths 240/569 across FPS and
interpolation settings. `--movement-preview --second-wave-script --headless`
and `--game --game-second-wave-script --headless` position an invulnerable
player at the arena, then use normal melee against both waves. Trigger 54 runs
normally; trigger 55 now reveals both chests after the second wave, without
opening them in this combat-only fixture. See NATIVE_FLYING_ENEMIES.md and
NATIVE_CHEST_UNLOCK.md.

`ChestUnlockCheck.java` runs in default regeneration and writes ignored
`analysis/chest-unlock-oracle.tsv`: 2247 trigger/chest/reveal-effect/random rows
from seven actual world arenas, with 568 original/reference expectations.
`python tools/check_native_chest_unlock.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
checks those rows, seven milestones and 24 live arena/reward/gate chains across
widths/FPS/interpolation. `--movement-preview --arena-script --headless` and
`--game --game-arena-script --headless` position an invulnerable player at the
arena, defeat both waves via normal melee, approach/open chest 57 via normal
input and dismiss the reward tutorial. Neither enemies, chest locks nor gate
activation are forced by these host fixtures. See NATIVE_CHEST_UNLOCK.md.

`FireMagicCheck.java` runs in default regeneration with the existing isolated
ASM drawing adapter plus a named fillArc no-op. Original player/world/fire,
random and MP bytecode stays intact. Fourteen 120-tick arenas write ignored
`analysis/fire-magic-oracle.tsv`: 4790 native-comparable player, charge, pulse,
enemy and random-stream rows (3380 original/reference expectations).
`python tools/check_native_fire_magic.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
checks all rows, seven MP/control milestones and 36 host runs across widths,
FPS and interpolation. `--fire-script` and `--game-fire-script` explicitly set
an invulnerable player beside UID 19 with magic unlocked; normal mask-1 input
casts and defeats the enemy. `--game-arena-fire-script` acquires magic through
the normal arena/chest/tutorial/gate chain before casting. See NATIVE_FIRE_MAGIC.md.

`IcePickupCheck.java`, `IceWeaponCheck.java` and `IceWorldCheck.java` run in
default regeneration and write ignored `analysis/ice-pickup-oracle.tsv`,
`ice-weapon-oracle.tsv` and `ice-world-oracle.tsv`: 6106 native rows and 6118
original/reference expectations for selected ice reward, physical weapon,
switch/query and reciprocal world-combat branches.
`python tools/check_native_ice_weapons.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
checks those rows, seven milestones and 24 host chains across widths/FPS and
interpolation. `--ice-script` / `--game-ice-script` explicitly relocate/unlock
the original second-level ice chest in the first-level test arena and position
an invulnerable player there. Normal interaction grants ice, dismisses its
tutorial, switches weapons and defeats an enemy with ice melee. Ordinary
first-level placements do not gain an ice pickup. See NATIVE_ICE_WEAPONS.md.

`IceProjectileCheck.java` and `IceMagicCheck.java` now run in default
regeneration and write ignored `analysis/ice-projectile-oracle.tsv` and
`ice-magic-oracle.tsv`: 17602 native rows and 14429 original/reference
expectations across forty controlled arenas. These cover ice casting, MP/RNG,
ordinary enemy hits, wall/slope impacts, temporary footholds and rider cleanup.
`python tools/check_native_ice_projectiles.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
checks those rows, seven milestones and 24 host chains. Explicit headless
`--ice-projectile-script` / `--game-ice-projectile-script` fixtures reuse the
relocated chest, then normal movement/magic/jump input defeats UID 19, freezes
the real left wall, lands on its foothold and replaces it while riding.
See NATIVE_ICE_PROJECTILES.md for fixture setup and remaining attachment boundaries.

`IceAttachmentCheck.java` now runs in default regeneration and writes ignored
`analysis/ice-attachment-oracle.tsv`: 18411 native rows and 6157 original/reference
expectations across seventeen original-world arenas. Six natural fixtures prove
that shipped ice shots pass through type-33 objects; eleven controlled fixtures
set explicit rectangles to enter the otherwise gated attachment branch once,
then verify original world ordering, follow/melt/rider behavior and cleanup.
`python tools/check_native_ice_attachments.py --exe soul/port/build/Debug/soul_assets.exe`
checks every row and the point-rectangle rejection. No game asset or generated
Java changes make this branch reachable. See NATIVE_ICE_ATTACHMENTS.md.

`CheckpointCheck.java` runs in default regeneration's isolated lifecycle/RMS
harness and writes ignored `analysis/checkpoint-oracle.tsv`: 6785 semantic and
placed-actor-presence rows, with 82 original/reference comparison expectations.
Twenty-three fixtures cover all six first-level checkpoints, gates/edges,
saved removals, later changes, checkpoint replacement and two complete Java
restart calls. The native snapshot is in memory and is not an original RMS file.
`python tools/check_native_checkpoints.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
checks all rows, seven milestones and 24 host chains. Explicit headless
`--checkpoint-script` / `--game-checkpoint-script` fixtures seed position/meters/
unlocks, walk into the shipped checkpoint, inject lethal health, finish defeat
and exercise R plus a second restart (pause Restart Level in the game variant).
See NATIVE_CHECKPOINTS.md for runtime boundaries and NATIVE_SAVES.md for desktop persistence.


`python tools/check_native_saves.py --exe soul/port/build/Debug/soul_port.exe`
checks checkpoint autosave and twelve new-process Continue runs across widths,
FPS and interpolation, plus New Game reset, menu availability, malformed/missing
saves and automated user-slot isolation. Data-free `desktop_save_roundtrip` CTest
checks codec/replacement/failure handling, corruption/truncation and UI decisions.
The desktop SODPC format is separate from original RMS; see NATIVE_SAVES.md.


`BeamCheck.java` adds 8844 original/reference expectations and ignored
`analysis/beam-oracle.tsv` with 8832 controller/animation/endpoint and damage
rows. `python tools/check_native_beams.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
compares those rows, seven milestones and 24 actual-map stationary/sweeping beam
host runs. Headless --beam-script and --game-beam-script control only the initial
position and movement; hits and defeat come from live simulation. See NATIVE_BEAMS.md.


`PromptCheck.java` writes ignored `analysis/prompt-oracle.tsv`: 704 original/
reference lifecycle, visibility and animation rows across nine placed prompts
and thirteen controlled cases. `python tools/check_native_prompts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
compares those rows and checks seven milestones / 24 host runs across widths/FPS/
interpolation. See NATIVE_PROMPTS.md for the controlled magic-prompt relocation,
keyboard adaptation, high-UID save fix and remaining linked/script dependencies.


`IntroCheck.java` runs shipped trigger 82/script 76 in original/reference
lifecycle loaders and writes ignored `analysis/intro-oracle.tsv`: 5460 timeline,
camera, dialogue-anchor, flag/pose and actor-presence rows (969 expectations).
`python tools/check_native_intro.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe`
checks those rows, seven milestones and 48 playback/skip host runs at 240/569
across FPS/interpolation. --intro-script / --game-intro-script use the ordinary
spawn and real input; their skip variants fast-forward via Backspace's mask.
Normal New Game enables the intro automatically. NATIVE_INTRO.md records the
remaining interpreter, original-renderer and owner limits.
