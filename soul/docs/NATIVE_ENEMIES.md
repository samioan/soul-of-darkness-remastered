# Native type-1 enemy AI

`EnemyType1` now ports the non-combat level-0 controller for ordinary variants
0..3, using sprite `2:2`. It covers record initialization, stat-row selection,
activation, fixed-point integration, pursuit, facing, attack/rest animation
states, gravity, tile walls, slopes and ledge decisions. It is now also connected
to the [live combat preview](NATIVE_LIVE_COMBAT.md), within its explicit ordinary-
enemy and world-ownership boundaries. Controlled damage and
hurt/death entries are now covered in [NATIVE_COMBAT.md](NATIVE_COMBAT.md).

## Original behavior and timing

The constructor reads variant from record[10], stat set from record[11] and
health from stat row 0 with stride 3. Record[6..9] defines the activation area
relative to the initial position. That rectangle stays fixed while the enemy
moves. Ordinary variants set the side-contact O flag. Respawning variants
4+ and hidden/script-linked records are explicitly rejected by this slice.

Original `b.aH()` activates action 101 when the player's collision rectangle
enters the activation area. Pursuit sets velocity to +/-256 fixed units (one
pixel per tick), with a 256-unit dead zone, and changes facing beyond that
distance. Integration precedes control, so pursuit affects the following tick.
The variant table supplies values 0,1,2,3: its low bit permits walking off a
ledge, and values 2/3 select behavior variant 1.

Pursuit builds an interaction rectangle from the current collision bounds:
13 pixels ahead, or 50 for behavior variant 1, and ten pixels above/below.
Contact enters action 102 and stops horizontal motion. Behavior variant 0
requests attack animation 2; behavior variant 1 keeps its current animation.
At animation ending, action 103 requests idle animation 0, stops motion and
waits 400 ms (behavior 0) or 500 ms (behavior 1), then resumes pursuit.
Falling action 1 uses delayed acceleration 768 and returns to pursuit after
the previous tile probe reports support. The 83 ms tick and deferred animation
requests match the original; rendering advances neither state nor clocks.

Flag 32 does **not** pause this enemy type. The original type-1 update still
runs with that bit set, unlike the supported container branch. Pause and
inactive flags 1024/16384 stop the controller. Bounds refresh and animation
advance remain separate host operations, preserving the original pre-advance
bounds. `body()` and `applyContact()` bridge the existing player side-contact
snapshot format; fixed-position synchronization is deferred until integration.

No renames or generated-Java edits were needed. The controller, action entries,
interaction bounds and gravity branches were checked against original bytecode
with `python tools/bytecode.py b` before native translation.

## Verification

Default regeneration runs `EnemyCheck.java` against unmodified original and
generated classes using the existing ImageIO test adapter. It exports
`analysis/enemy-oracle.tsv`, kept out of git. The 21 arenas cover idle,
left/right pursuit, all four attack variants, falling, both ledge policies,
a wall, all six slope shapes, pause, inactivity, flag 32 and the touched flag.
Each runs 96 ticks with two snapshots per tick: **4,032 snapshots over 2,016
ticks**. Original/reference checks compare player damage guards and enemy
movement/bounds, animations, actions, timers, health, facing and rectangles:
124,997 comparisons in total.
Independent assertions require all four attack fixtures to reach both attack
and recovery, and the falling fixture to execute action 1.

The stationary player's rectangles are deliberately synthetic: pursuit cases
place a thin rectangle above the enemy's damaging area; attack fixtures move
it into the extended interaction strip above the collision box. This exercises
the AI without invoking player damage. Guards assert that player health and
pose remain unchanged. These are controlled AI fixtures, not gameplay inputs.
No bytecode rewriting or combat-hook suppression is used.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_enemies.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
python tools/check_native_enemies.py --exe soul/port/build/Release/soul_assets.exe
```

Fourteen new data-free assertions cover activation, delayed movement, pause,
flag 32, body handoff, draw purity and unsupported record/combat boundaries.
The combined player/object test now has 108 assertions; CTest remains 27 tests.

## Remaining integration

The slice assumes level 0 and an empty non-player collision queue, with no
active-region owner. It does not port non-player separation/support ownership,
attachments, hidden variants 4+, script activation or palette effects. Combat overlaps
raise an explicit unsupported-boundary error; they are never treated as harmless.
Attack action 102 alone does not imply damage has been applied.

The following [combat pass](NATIVE_COMBAT.md) ports weapon-0 damage, enemy
hurt/death entries, player hit/death response and combat effects.
[Death/loot/reset](NATIVE_DEATHS.md) now has controlled native coverage. Next, connect
first-level enemy ownership and body queues to the movement preview. Preserve
queue order and run that integration at widths 240/569 and independent FPS
settings before calling it live combat. The default executable remains a
calibration scaffold and the complete game is not playable.
