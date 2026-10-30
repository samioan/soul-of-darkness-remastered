# Enemy and object behavior

This pass names the type dispatch and shared helpers in Actor, and compares
selected update sequences against the original class files. It does not complete
every variant, boss phase, script interaction or level. The Windows executable
now has explicit movement, melee and pickup/chest previews alongside its default
calibration scaffold. [Native type-1 AI](NATIVE_ENEMIES.md) covers a controlled
controller slice; live enemies and damage are not integrated into the preview.

## Dispatch and state model

`Actor.update` integrates fixed-point motion and dispatches on `type`.
`changeActionState` / `changeActionStateInternal` set action state and perform
type-specific entry operations: animation requests, velocities, timers, facing,
linked actors, effects and projectiles. Action IDs are not animation IDs, and
the same action number can mean different things for different types.
`advanceAnimation` is called separately; the original paint path drives it.

`variantData` (`b.aC`) often contains packed behavior bits:
`getBehaviorVariant` is `(variantData >> 1) & 15`, and `getVariantFlagBit` is
`variantData & 1`. This interpretation is not universal: type 70 also uses raw
variant values directly. `statSet` (`b.aD`) selects a stride within a row of
`Game.actorStatTables` (`e.bY`). `getStat` selects row/stride by actor type:

| Type | Row | Stride |
|---|---|---|
| 1 | 0 | 3 |
| 2 | 2 | 3 |
| 3 | 3 | 3 |
| 10 | 9 | 4 |
| 13 | 14 | 4 |
| 14 | 12 | 3 |
| 70 | 17 | 3 |
| 90 | 15 | 5 |
| 100 | 16 | 4 |

Scratch fields such as `aw` and `ax` remain unnamed: different types reuse them
as timers, counters or angles. `decrementTimerA/B` describe only those helpers'
operations, subtracting `Game.tickDeltaMs` from `aw`/`ax`.

## Mapped handlers

The original-to-reference mapping is in names.map. These roles follow the
constructor, update dispatcher and action-entry switch, rather than sprite
appearance alone. Numbered enemy names deliberately avoid guessing identities.

| Types | Handler / observed role |
|---|---|
| 1, 2, 3, 13, 14, 70 | `updateEnemyTypeN`; pursuit, melee, flying, thrown-object and projectile variants |
| 10 | `updateBeamEmitter`; activation region, timed animation/beam cycle |
| 90, 100 | `updateBoss90/100`; shared `activeBoss` and `bossInitialHealth` |
| 68 | `updateWaveSpawner` |
| 22, 23 | `updateCameraRegion`, `updateCheckpoint` |
| 20, 21, 40, 49 | `updateTrigger`, `updatePrompt`, `updateLinkedTrigger`, `updateCameraController` |
| 35, 48, 52 | `updatePickup`, `updateAbilityPickup`, `updateContainer` |
| 31, 37, 45 | `updateLinkedGate`, `updateGate37`, `updateBreakableBridge` |
| 33, 50, 64, 72, 82 | Moving, swinging, spring, linked and grouped platform handlers |
| 80 | `updateEnergyBarrier`: extended solid body, lightning endpoints and linked release |
| 73, 74, 75, 78, 89, 95 | Hazard, spawner, trap, area, timed switch and crushing hazard handlers |
| 18, 19, -37, -40, -100, -102 | Homing/straight/hit/explosion/decelerating/boss-orbit projectiles |
| 16, 39, 98 | Thrown objects, effects and one-shot effects |

For example, the controlled type 1 sequence reaches 101 (pursuit), 102
(attack), 103 (rest), then returns to pursuit. Type 2 also reaches 200 (an
attack preparation delay). A type 3 throwing variant cycles 105/106/107 and
creates a type 16 actor. These are examples for the selected serialized
variants, not exhaustive state definitions.

Container type 52 waits for contact and a fire press, opens and emits pickup
actors. Type 45 waits in state 0 until an external activation; state 100 counts
498 or 1660 ms depending on its animation, then state 101 removes it.
Checkpoint type 23 can write RMS and is intentionally excluded from the harness.
Trigger type 20 can change game state or start scripts; linked type 40 can
enable, disable or remove actors. Their surrounding script/save semantics still
need a dedicated pass.

## Data validation

`python tools/parse_objects.py` validates resource 0 of packs 3..11 as
length-prefixed records: one unsigned byte counting signed little-endian
16-bit values. The constructor receives these values, their count and the
byte offset after the count byte. Common prefix: type, UID, X, Y; later fields
depend on type. The checker accepts lengths 6..20 and types 0..100, checks
bounds and exact consumption, and requires one player record in each stream.
The shipped streams contain 930 records across 41 types (counts per stream:
115, 130, 114, 86, 122, 91, 125, 135, 12). These are envelope checks; they do
not establish the meaning or validity of every type-specific parameter.

Pack 18 resource 0 uses Java big-endian integers: row count, then repeated
row length and values. All 1096 bytes consume exactly: 20 rows, 253 values.
This is a separate byte order from the object records and outer pack offsets.

## Original-bytecode oracle

Default `python tools/rename_sod.py` compiles and runs `BehaviorCheck.java`.
Each fixture uses fresh isolated class loaders for the original and renamed
classes, real sprites, font/string tables, trigonometry and stat tables.
The first serialized record of each selected type supplies its parameters;
only X/Y are normalized to a controlled flat-floor arena. The player is held
stationary except for explicit contact setup; the container receives a fire
press, the bridge an explicit state-100 activation and the trap an attack
rectangle. These are unit scenarios, not captured gameplay inputs.

The 16 types are 1, 2, 3, 10, 13, 14, 70, 35, 52, 37, 45, 64, 75, 89, 90
and 100. Each runs up to 160 ticks (45 for each boss), stopping if the root
is removed/inactive. A queue snapshot updates existing actors; newly spawned
actors wait until the next step. Bounds and animation clocks are explicitly
advanced. This approximates selected host responsibilities without invoking
the entire Game update/paint loop.

After initialization and every step, checks compare every Actor instance
field on root/player/registered actors, every Actor static field, selected
Game globals, actor links/counts, save bytes and final random-stream state.
Reference actor links are normalized by registry slot or role, and sprites
by index. Independent assertions require selected active fixtures to reach
known state sequences, so an equally idle original/reference pair cannot
satisfy those cases. Other fixtures include idle coverage; their success
does not demonstrate activation or all transitions.

The ImageIO test Image adapter supports PNG/RGB decoding only. No Canvas,
Graphics drawing, media, RMS service or real level is executed. Player combat
timers that mutate during painting, all spell effects, alternate variants,
later boss phases, scripts and complete persistence are outside this check.
Matching the original in these arenas establishes only this tested behavior.

Script dispatch/opcodes and tile grids are now covered in SCRIPTS_AND_LEVELS.md.
LEVEL_ACTORS.md and SAVES.md now cover shipped stream construction and selected
restoration paths. CHECKPOINT_LIFECYCLE.md adds controlled checkpoint/restart
and repeated loads. Next: staged loading and deeper linked-actor action branches
with valid preconditions.
