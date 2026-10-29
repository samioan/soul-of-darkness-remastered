# Native first-level checkpoints and session restart

All six first-level type-23 volumes now activate during gameplay. R and the
pause menu's Restart Level reconstruct the level from the latest checkpoint
in the current session. Restart restores position/facing, equipped blade,
ported meters/fragments and ice/magic ownership, fills health to the saved
maximum and retains saved MP. New Game clears the session checkpoint.

This semantic snapshot is not the original 1313-byte RMS record. The subsequent
[NATIVE_SAVES.md](NATIVE_SAVES.md) pass persists it in a versioned desktop file
and adds Continue. Native upgrade arrays and other levels' special snapshots remain open.
The complete game remains unfinished.

## Shipped volumes and original evidence

| UID | Anchor X/Y | Width/height |
|---|---|---|
| 11 | 100 / 181 | 40 / 80 |
| 128 | 732 / 877 | 20 / 80 |
| 203 | 73 / 598 | 20 / 120 |
| 137 | 1245 / 420 | 10 / 80 |
| 106 | 1563 / 638 | 10 / 80 |
| 107 | 63 / 1477 | 10 / 80 |

Original `Actor.ao/updateCheckpoint` uses inclusive collision overlap, writes
UID+1 and signed-short player coordinates/facing, serializes progression,
removes the checkpoint, copies actor persistence, then writes RMS. Its own
removal is therefore captured. Points do not overlap; touching a rectangle
edge does. The original common update suppresses pause and hidden/frozen actors.
These volumes have no additional visible artwork or activation animation.

`Game.I/staysRemovedOnRestart` retains removed types 35/23/48/82. Separately,
removed actors with flag 512 are omitted by their saved UID. Ordinary removed
enemies are reconstructed; removal does not mean every actor should stay gone.
Flag 8192 hides an actor without writing the ordinary removal marker. A normal
health chest gains flag 512 in its constructor, so runtime flags matter when
capturing removals. Original b/e dumps from `tools/bytecode.py` confirm these
branches and `Game.al/restartLevel`'s construction, restore, HP refill, script
reset, camera and input order.

## Native ownership

`Checkpoint` accepts the shipped rectangular type-23 envelope and implements
its overlap/one-shot behavior. `CombatWorld::loadCheckpoints` adds the volumes
to record-ordered world dispatch using the existing previous-camera buckets.
The callback observes the checkpoint as removed and captures current supported
actor removals before later world handlers. Placed pickups and containers are
included by their external host owner; transient loot is discarded on restart.
General external-container/actor interleaving remains within the existing host
boundary, rather than establishing a complete original world loop.

`CheckpointSnapshot` stores owned values, with no sprite, map or actor pointers.
It narrows saved coordinates to signed shorts and distinguishes persistent
removal from ordinary reconstruction. Live changes after capture cannot mutate
the snapshot. R and pause Restart Level use the same reconstruction helper;
the frontend distinguishes this request from New Game.

The host reloads the original map and creates fresh actors, scripts and
transient owners. It then applies the saved player/meters and removal policy,
rebuilds region/camera selection and clears input. Consumed actors remain
allocated but inactive in their native owners so UID/source lookup remains
stable; they do not update, render or re-trigger. Restoration requires a freshly
constructed native world. Bounds and fixed coordinates are primed before the
first desktop draw; the Java restart leaves fixed-coordinate synchronization
to the following actor update. Full mutable actor/save equivalence is not claimed.

## Validation

`CheckpointCheck` runs in default regeneration's separate lifecycle harness
with the existing ImageIO and in-memory RMS adapters. The original/reference
game classes retain their checkpoint, serialization and complete restart code.
Twenty-three controlled fixtures provide 82 comparison expectations and 6785
native-comparable rows: all six placements, facing, four inclusive edges,
miss/point/pause/hidden/frozen gates, pickup/chest/enemy removals, flag-512 and
8192 cases, changes after capture and replacement by a second checkpoint.
Successful cases execute two original restart calls. The native rows compare
the supported player/progression subset and presence decisions for every
first-level placed actor, including types whose live native handlers are unported.
They do not compare original RMS bytes with the native session snapshot.

```
python tools/check_native_checkpoints.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

Seven milestones and 24 host chains test widths 240/569, 30/60/144 FPS and both
interpolation settings. Explicit headless `--checkpoint-script` and
`--game-checkpoint-script` fixtures position Kale beside shipped checkpoint 11
with controlled HP/MP, fragments and weapon unlocks. Normal left input enters
its edge; normal right input leaves it. A deliberate lethal meter injection
then exercises the actual defeat sequence. The host invokes the R restart
path, followed by a second restart; the game variant uses pause Restart Level
for that second request. This fixture does not claim an enemy caused its death.
It verifies the consumed checkpoint, saved MP=17, HP refill to 80, blade/unlock
retention, position (148,259), subsequent MP recovery and unrelated gate state.

Fifteen added data-free assertions bring player tests to 430; three CLI guards
bring CTest to 78 tests. The frontend assertion now distinguishes session
restart from New Game. Ignored captures at both widths show resumed gameplay
with HP=80 and MP=17.

Validation on 2026-10-29: default Java regeneration passed compilation,
ReferenceCheck and all existing fixtures. Debug and Release passed all 78
CTests, the checkpoint checker and existing ice/fire/melee/player, ordinary/
flying combat, world/region, platform/bridge, chest/ability/arena, gate/script
and passage regressions. The startup/English-story/HUD/pause checker passed.
The data-free package matches Release and passes all 6785 checkpoint rows,
seven milestones and 24 host chains against the regenerated oracle.

Disk-backed native saves and Continue are now covered in NATIVE_SAVES.md. Next: remaining first-level actor
progression and level completion/loading. Levels 3/5's paired/controller data,
broader script/cutscene behavior and full level-2 play are still unfinished.
