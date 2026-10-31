# Native type-3 actors (levels 1 and 2)

`enemy_type3.{h,cpp}` ports the shipped type-3 ground enemies: the explosive charger
(variant slot 7), the thrower (slot 9) and the linked-chest runner (slot 24). It combines
the [decision layer](NATIVE_TYPE3_DECISIONS.md), the [type-16 shot owner](NATIVE_THROWN_OBJECTS.md)
and the container owner under `CombatWorld::loadType3Enemies`. Levels 1 and 2 (16 shipped
records) are covered. Level 3's two records require the paired-player mode and are rejected
at construction. Normal gameplay still constructs level 0 only.

## Original evidence

Inspected with `tools/bytecode.py` and the regenerated Java; no names or patches changed.

- `Actor.initializeFromRecord` (type 3): record slot 10 selects `cE[slot]` (7 -> 0, 9 -> 2,
  24 -> 5, i.e. behavior 0/1/2 and flag bit 0/0/1), slot 11 is the stat row of table 3, slot
  12 is the linked chest UID or -1. It calls `snapDownToGround` before the custom bounds are
  built, so fixed coordinates keep the record's y while the activation box uses the snapped y.
  It also re-enters the respawn fade whenever the stat row allows respawns and the record has
  flag 128, even when the later reset does not request a fade.
- `Actor.updateEnemyType3` (action table in the decision doc) resolves its linked chest first
  (`setAttachedActor`, chest flags 256 and 32), then runs the decision, positions the child at
  (+/-5,-31), applies gravity and finally contact. A charger touching the player explodes its
  held bomb (`bI`) and resets itself. Type 3 never clears the touched field (`Q`).
- `Game.W` dispatches `update()` for every queued actor and then updates the parent's
  attached actor if it has not run this tick. A held shot (flag 16) is therefore dispatched
  by its parent and again by its own queue entry. Actors born during a tick are not in that
  tick's queue. A chest that was queued at tick start and linked during the tick runs a
  second, carried update. `Actor.refreshBounds` reads the flags field, so a carried chest
  flips one update after its holder turns.
- `Game.removeActor` clears the holder's attachment (flag 256 only, no cooldown). A runner
  that respawns re-links its chest because the record's link survives `initializeFromRecord`.

## Native implementation

- `EnemyType3` derives from `EnemyType1` for lifecycle, hurt, loot and region reset. The
  base now routes hurt animation, death-effect offset, respawn eligibility (`type != 1` or
  unlinked) and a `finishReinit` hook through virtuals; the type-3 override snaps the actor
  down, rebuilds its activation box and applies the flag-128 respawn-fade entry.
- Each tick builds the decision input (positions, saved runner direction, animation ending,
  wall flags, activation and charge reach for both orientations, attachment kind), calls
  `decideType3` and applies the events: animation requests with restart semantics, held shot
  birth, release, chest drop and post-death cleanup. `Type3DecisionEvents` gained the spawn
  facing and signed spawn offset because the original `m(horizontalSpeedTowardPlayer(5),-31)`
  uses the facing at spawn time.
- `ThrownObjects::spawnHeld` takes that offset and a `fresh` flag. Fresh shots are skipped by
  the ordinary shot queue once, so they join the next tick. `EnemyType3::dispatchAttached`
  is the parent's post-update dispatch of its held shot or carried chest.
- `Container` gained `carry`, `carriedUpdate` and `release` (clear flag 32, fall with a 500 ms
  timer, full tile resolution, clear flag 256). A chest with flags 256 and 32 that is
  dispatched through the container queue runs the carried update.
- `CombatWorld` stores type-3 actors in a stable deque, orders them in record order, includes
  them in eligibility, contact bodies, update queue, region reset, checkpoint removal, fire/
  ice/projectile target loops and drawing, and builds `Type3Services` (shots, live chest
  lookup, shot contact handlers) for each dispatch. Shot-contact code was factored into
  `shotHitsActors` and `shotHitsPlayer`, shared with the ordinary shot loop.

## Verification

`Type3ActorCheck.java` compares original bytecode with regenerated Java for 24 integrated
scenarios of 140 ticks each, with two snapshot phases per tick (handler, then bounds and
animation). The oracle has **11487 rows**: charger pursuit/charge/rest/blast/flip,
thrower aim/release/recovery at several distances and a walking player, hurt and lethal
damage, respawn fade for thrower and charger, runner wall stop, chest carry and drop,
ledge fall for runner/charger/thrower, pause, hidden, frozen, flag-32, slope floor,
reflected shot and player weapon hits. Rows contain the actor, every live type-16 shot, the
linked chest, birth events (sorted), player health in the melee case and the final random
stream. The native probe reproduces the same queue order (actor, chest, shots that existed at
tick start, parent-attached dispatch) and matches every row:

```
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_type3_actors.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
```

The same check runs **48 controlled world fixtures**, the level-2 trigger-308 enable fixture: each shipped type-3 record of levels
1 and 2 is constructed through `CombatWorld` with only its owned records (player, type 3,
type-52 chests), parked in or away from its activation box, observed, or killed after 20
ticks. It asserts the thrower cycle, runner chest carry and drop, remote-player inactivity
and lethal-damage accounting. Four presentation fixtures at widths 240/569 with and without
repeated rendering prove width and drawing leave simulation, ownership and RNG unchanged.
Twenty data-free assertions (`type3_actors` CTest) cover construction, held birth, release,
death release, chest carry/drop, removal, queued birth and teardown.

## Boundaries

These are controlled fixtures, not a level route or runtime parity.

- Level 3's two type-3 records and the paired-player contact path (`Actor.o`) are rejected.
- Level 1 and 2 world construction still rejects other owners (type-1 variants, switches,
  platforms and so on), so fixtures pass only the owned records. Player-overlap enable
  triggers now accept type-3 targets (lazy lookup, flag 32 and 128 clearing as for type 1);
  a fixture runs shipped level-2 trigger 308, which enables actors 62 and 74 on first
  overlap. Level 1's trigger 100 (targets 96 type 1 and 97 type 3) and removal trigger 104
  are now routed (see NATIVE_LEVEL1_ENCOUNTER.md). Level 2's trigger 187 (targets 86/47/214,
  including a type-2 and a non-enemy target) and the removed-source/linked triggers 322 and
  123 are not routed; those chains remain pending.
- Non-player actor separation (`resolveActorSeparation`) and the full actor-slot reuse order
  are not modelled: simultaneous shots update in birth order and same-tick birth rows are
  compared sorted. The `Game.W` reset of off-queue enemies with a non-zero action and the
  clamp to the active camera region are exercised only by the existing world paths.
- The oracle keeps the player invulnerable and remote except for the melee/reflection
  cases; player contact knockback is not part of its rows. Chest animation while carried and
  free-chest bounds are compared after the animation phase, as in the container oracle.
- Palette timers, audio, the dynamic prompts and the actual level 1/2 routes are not ported.
