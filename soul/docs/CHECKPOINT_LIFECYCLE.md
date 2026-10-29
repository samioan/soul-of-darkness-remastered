# Checkpoint, restart and respawn-pool lifecycle

`LifecycleCheck.java` executes the original checkpoint handler and complete
`restartLevel` method against all nine shipped actor streams. It uses the same
real maps/sprites/stat tables as LevelCheck, plus a separate in-memory RMS test
adapter. This is controlled Java integration coverage; no native gameplay or
full MIDP runtime is provided.

## Checkpoint to record to restart

Type 23 dispatches to `updateCheckpoint` (original `b.ao()`, b.java:2127).
An inclusive collision-bounds overlap with the player activates it. The handler
stores UID+1 and player position/facing, serializes progression, removes the
checkpoint, then copies persistence bytes. Consequently its own -99 removal
marker is included in the saved record. Level 3 additionally stores paired-actor
state, and level 5 stores linked snapshots/controller state (SAVES.md).

The checker calls the handler first with non-overlapping bounds, then after
placing the player at the first checkpoint in the actual object stream. It
asserts absence of writes/removal in the first case, then the complete record,
UID+1, removal marker, world removal and position-restore flag in the second.
It directly invokes the handler; walking to the checkpoint and the Game update
queue are outside this fixture.

`accessSaveRecord` adds record 1 on the first write and replaces it on subsequent
writes. Reading an empty store adds no record and leaves `saveLoaded` false.
Writing also leaves that flag unchanged. Only reading an existing record sets
it true. The fixture reads back into a different zeroed buffer and compares every
byte, then exercises replacement and checks successful handles are closed.

The original `restartLevel` (e.al(), e.java:3856) performs this order:

1. Clear actors and related lists; restore recorded tile changes.
2. Reset shared Actor world state, including the respawn-pool cursor.
3. Restore removed UIDs and persistence bytes from the save buffer.
4. Reconstruct actors with restart filtering and level-specific saved links.
5. Restore checkpoint position, progression and stats.
6. Fill health to its restored maximum; retain saved magic.
7. Rewind/disable scripts and initialize the camera.
8. Reset letterbox counters and empty image cache 0.
9. Enter gameplay state 8, clearing input through `changeState`, and request redraw.

Original bytecode e.al() offsets 0..71 confirms this ordering. Two consecutive
restart calls per level check new player identity, saved checkpoint removal,
position/facing (including level 5's -20 Y correction), health refill, saved
magic, stable respawn count, script clock/offset rewind, cleared input and
occupied image-cache entries, and unchanged buffer/store bytes. Original and
regenerated worlds are compared after activation and after each restart.

These tests supply decoded scripts, loaded sprites, initialized cache storage
and sound disabled. They exercise camera initialization, but do not independently
establish all camera constraints, scripted camera modes or widescreen behavior.

## Pool and UID-table reuse across levels

Actor `resetWorldActorState` (original b.m(), b.java:1280) zeros
`spawnRecordCount` without clearing `spawnRecordsByUid` or replacing its pool
arrays. Bytecode offset 74 stores zero to `ah`; there is no UID-table clear in
the reset path. New construction assigns each active UID one of the reused
two-int pool arrays holding byte offset and short count.

The checker loads packs 3, 4, 3, 8, 3 in the same isolated world, clearing actors
between loads and using the original reset/construction functions. It verifies
the zeroed cursor, retained table object, stable counts when revisiting a pack,
and original/reference world equality. Entries for old UIDs absent from the
current stream retain their pool-array references. Some referenced arrays acquire
new byte offsets/counts when construction reuses those slots.

Across both original/reference loaders, the fixture observes 168 retained old
UID entries and 114 changed slices. These are observations across loads, not
distinct UIDs. They establish aliasing behavior; they do not show that gameplay
ever dereferences an obsolete UID or that the original has a reachable bug.
The Java reference preserves it. A native port needs an explicit ownership and
lifetime decision rather than silently treating this table as independent data.

## Test adapter and coverage limits

The RMS adapter lives in `tools/reference_checks/rms_adapter/`. Default
regeneration compiles a separate lifecycle harness containing it and the ImageIO
test Image adapter. Existing Reference/Sprite/Actor/Behavior/Script/Save/Level
checks retain their prior API boundaries and do not see this RMS adapter.
Both the original and generated Game classes call their unchanged RMS accessor.
The adapter accepts only `/dvseed.sav`, record 1 and the methods this path uses;
it copies bytes in memory and counts operations. It writes no files and is not
a production save backend, RMS emulator, or test of phone storage durability.

The pass checks **1737 expectations, 384 level comparisons and 7908 world
comparisons**. Run it with the complete default `python tools/rename_sod.py`.

NEW_GAME_AND_CONTINUE.md now covers confirmed reset, Continue's override and
selected loading stages. Still pending: full boot/staged loading and game-over/
menu drawing/input paths, disk-backed native save behavior,
storage failures/truncation and general gameplay ticks. Existing LevelCheck
separately covers selected nonempty level 5 snapshot reconstruction.
LEVEL5_CONTROLLER.md now tests live linked checkpoint capture and controller/
hazard restoration through Restart/Continue, plus a resumed common actor update.

NATIVE_CHECKPOINTS.md now adds first-level live checkpoint activation and
in-session R/pause-menu restart. Its semantic snapshot is separate from RMS:
disk persistence, Continue and other levels' special state remain unfinished.
