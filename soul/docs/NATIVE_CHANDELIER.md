# Native chandelier sequence: trigger 161 and script 160

The first-level player-overlap trigger now selects the shipped two-track script
160 during ordinary gameplay. Enemy 205 is frozen and repositioned; chandelier
34 enters its existing falling/breaking handler. The sequence does not suspend
player input, physics, combat, camera following or the rest of the world.

## Original behavior and native ownership

The shipped type-40 record is UID 161 at (70,1060), with a custom 80x120 rectangle,
condition 1 (any source overlaps), target mode 1 (script), effect 0 (start once),
source player 186 and target script 160. Actor.g refreshes the source bounds and
uses inclusive rectangle overlap, rejecting point rectangles. Source visibility
is not a condition. Trigger pause, hidden flag 1024 and frozen flag 16384 defer
both activation and completion cleanup.

Actor.a(byte) selects the script with completion mode 1. Selection occurs in the
trigger's handler, after that tick's selected-script update: tick zero runs on
the following gameplay update. Leaving the rectangle does not cancel the script.
Actor.H watches for counter -2; stopAndRewindScript resets its track offsets,
Actor.c releases the selected slot and removes the trigger. This cleanup also
runs when the source no longer overlaps. A hidden trigger can defer cleanup
until its handler becomes active again.

The actual tracks are:

| Target | Key ticks | Commands |
| --- | --- | --- |
| Enemy 205 | 0,2,4,6,8 | 107 sets raw flag 16384; 21 positions (90,1040), (90,1040), (107,1035), (120,1032), (140,1040); 108 clears 16384 at tick 8 |
| Chandelier 34 | 0,8 | 21 positions (140,1041); 22 selects immediate animation 5; 106 assigns action 102 at tick 8 |

Position interpolation uses the old, post-incremented script counter and Java
integer division by (keyTick-oldTick+1). Each track considers one pending key per
tick. Flag commands 107/108 execute only at equality and do not change facing.
Command 106 assigns type-50 action directly: this actor is not a basic enemy.
Immediate animation preserves frame/tick when the animation is already selected.
Moving positions update canonical 240x320 distance buckets and refresh bounds
when nearby, independently of presentation width.

ChandelierScript is deliberately limited to these two existing actor tracks and
commands 21/22/106/107/108; unsupported targets, kinds and commands fail explicitly.
All live targets are resolved before mutating the script or either actor. World
bindings are rebuilt on each step; the script retains no borrowed actor pointer.
CombatWorld runs selected scripts before the player update and dispatches trigger
161 in placed-record order with the original trigger radius. Enemy flag 16384
suppresses its normal handler until release; the platform then uses the already
ported fall, collision, damage and break-animation logic.

Normal world construction loads the sequence. Checkpoint reconstruction rebuilds
its owner, offsets and targets; removed trigger bookkeeping follows the existing
original type-40/flag persistence rules. No active timeline is serialized.

## Verification

ChandelierCheck initializes the actual three placed records and original script
in isolated original/reference Java class loaders, using the test ImageIO/RMS
adapters. This is a bytecode oracle, not a MIDP runtime. It runs real enemy and
chandelier handlers and records their motion, animation, flags, action and bounds,
plus the script counter/offsets, trigger latch/liveness and selected slot count.
Ten scenarios cover normal play, leaving, outside, edge contact, missing source,
pause, hidden/frozen trigger, hidden player and deferred completion cleanup.
The check produces 1005 original/reference expectations and 3000 native rows.

The host checker runs 24 controlled raw/game fixtures at logical widths 240/569,
30/60/144 FPS and interpolation on/off. It verifies one activation/completion,
trigger removal, enemy release, ordinary player movement and the chandelier's
fall/break endpoint (action 104, y=901), with equal simulation state across all
presentation settings. Four earlier host milestones verify player movement while
the enemy is frozen, the exact release/fall tick and unchanged HP through that
short sequence. The fixtures relocate Kale to (100,1100); they are not a
full route from the initial spawn. Other first-level enemies remain live, so the
fixture also receives ordinary damage after the short scripted sequence.

Nine data-free assertions cover pause, atomic missing-target failure, raw flags,
completion, deferred cleanup, rewind, non-retriggering and unsupported track kinds.
The current suite has 475 player assertions and 92 CTests, including three CLI
guards. Default regeneration includes ChandelierCheck. Commands:

```powershell
python tools/rename_sod.py
.\soul\port\build.bat Debug
python tools/check_native_chandelier.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build.bat Release
python tools/check_native_chandelier.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe
.\soul\port\build_dist.bat
```

Verified on 2026-10-29: default regeneration compiled with zero errors and passed
ReferenceCheck's 4218 comparisons plus all original/reference fixtures. Debug and
Release passed all 92 CTests, 475 player assertions, 3000 chandelier oracle rows,
four intermediate host milestones and 24 final host runs. The existing Release
world/progression groups, intro, checkpoints, frontend and cross-process saves
also passed. The data-free distribution build passed; its executable matches
Release, and its asset tool passes the chandelier oracle. Portrait and widescreen
gameplay captures were inspected. Oracles, captures and binaries remain ignored.

Level 1 reuses this owner for scripts 23/24/114; see [NATIVE_LEVEL1_AMBUSH.md](NATIVE_LEVEL1_AMBUSH.md).
The sentence above and the single-script wording describe the level-0 slice.

This pass completes script 160/trigger 161. The subsequent
[linked prompt-removal pass](NATIVE_PROMPT_REMOVAL.md) completes triggers 43/61.
[Camera controllers 63/167](NATIVE_CAMERA_CONTROLLERS.md) are also implemented.
[Exit 173/completion UI](NATIVE_COMPLETION.md) is implemented. [First-level progression](NATIVE_FINISH.md) is also implemented; selected-level
construction and later-level scripts remain on the roadmaps.
