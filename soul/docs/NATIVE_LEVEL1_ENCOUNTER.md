# Second-level first encounter (triggers 100, 104, 62 and 103)

The second level's first arena is the level-0 arena pattern with different actors: a player-overlap
trigger releases a two-enemy wave, their removal releases a flying pair, and the pair's removal
opens a chest and a gate. The native owners existed for level 0 but were wired to its exact shape
(one container source, script 165, flying/chest targets). This pass generalizes them and verifies
the shipped chain. Normal gameplay still constructs level 0 only.

| UID | Record | Role |
| --- | --- | --- |
| 100 | type 40, mode 1, action 0 | player overlap enables enemy 96 (type 1) and thrower 97 (type 3) |
| 96, 97 | `p[5]` 2216/2217, stat row 7 / table-3 row 4 | one respawn each (`respawnCount` 1), then removed (flag 2048) |
| 104 | type 40, condition 4, targets in slots B/C | 96 and 97 removed -> enables flying enemies 98 and 102 |
| 62 | type 40, condition 4 | 98 and 102 removed -> enables chest 59 (ice reward, -5) |
| 103 | type 40, condition 4, script flag | 98 and 102 removed -> selected script 95 |
| 95 | one existing-actor track | moves free-standing gate 87 from y=380 to y=300, action 103 |

## Original evidence

Inspected with the regenerated Java (`Actor.updateLinkedTrigger`, `Actor.a(int,int,boolean)` and the
selected-script helpers); no names or patches changed.

- Record fields: `p[10]` condition, `p[11]` target-versus-script flag, `p[12]` action, `p[13..15]` source
  UIDs, `p[16..18]` target UIDs or the script UID. A condition-4 trigger with `p[11]==0` enables all
  three target slots, including slot B. Level 0's chain happened to leave slot B at -1, which the
  old native `supported()` required.
- Condition 3 is "any source removed", condition 4 is "all sources removed". An unused source slot
  contributes `false` for condition 3 and `true` for condition 4 (`Actor.a` returns its third
  argument for UID -1). A removed index or persistence index zero satisfies a slot, even for a
  live source. Condition 4 drops an unresolved first source UID for good; every other miss retries
  each tick, so an unresolved second source blocks the trigger forever.
- `p[11]==1` with action 0 starts selected script `p[16]` through `a((byte)1)` and polls `H()` on
  every tick, condition true or not. `H()` waits for script clock -2, stops/rewinds, releases the
  selected slot and removes the trigger. Key zero therefore runs the tick after the trigger
  fires (`updateSelectedScripts` precedes the world update).
- Target enabling (`enableLinkedActor`) clears flag 32, clears flag 128 unless the target's
  action is 5, and spawns the type-39 appearance effect for chests.
- The stale note that trigger 100 "names non-enemy targets" was wrong: 96 is type 1 and 97 is type 3.
  It was already routable by the type-3 enable lookup.

## Native implementation

- `RemovedEnemyTrigger::supported` accepts targets in any of slots B/C/D (at least one). The world
  routes it when both sources are placed type 1/2/3 enemies and every target is a placed type 1/2/3
  enemy or a type-52 chest, so level 2's triggers aimed at unported type 13 are still skipped.
  Source lookup now includes type-3 actors, and target resolution includes type-1 and type-3
  actors beside flying enemies and containers (`CombatWorld::liveEnemyIndex` / `enemyEnableTarget`).
- `RemovedScriptTrigger` models up to three sources with the original per-slot cache for conditions 3
  and 4. `updateSources` takes a lookup, while the old single-source `update` is kept for the
  container bridge and delegates to it. Container-owned triggers stay deferred behind the container
  handler (`deferred`); enemy-owned triggers run in their own queue slot, so removals earlier in
  record order are visible the same tick, as in the original array.
- `loadGateScripts` selects the script named by the placed trigger (165 when none exists) instead
  of a hard-coded UID, and `GateScript` already accepted script 95's opcodes (22, 21, 106).
  Mixed container/enemy sources and more than one distinct selected script are rejected.

## Verification

- `EncounterGateCheck.java` runs the original and regenerated type-40 handler, selected-script
  bookkeeping and gate 87 on the real level-1 collision grid (118x67 tiles, camera at 720/240) with
  placeholder type-2 sources. Eleven 48-tick scenarios: both removal orders, one survivor, missing
  first source, missing second source, both removed before first lookup, index-zero, pause, hidden,
  frozen and hidden sources. The oracle has **2112 rows** and the native probe matches every one;
  independent assertions pin the key-zero timing, slot release, index-zero shortcut, blocked
  second source and first-source clearing.
- `RemovedEnemyCheck` gained a `slot-b` scenario (the shipped trigger-104 shape with a type-3
  source), so the removed-source enable oracle is now 224 rows and the flying-enemy comparison
  4671 native rows.
- Thirteen new data-free assertions (player suite now 543) cover condition 3/4 slot rules, the
  vacuous unused slot, first-source clearing, the single-source delegate, mixed-source rejection,
  and a real `CombatWorld` where two lethal strikes on placed type-1 enemies start the selected
  script from the enemy's own queue slot and remove the trigger when it completes.
- `--encounter-world-manifest` builds the real level-1 records for this chain (player, 96, 97,
  98, 102, chest 59, gate 87, triggers 100/104/62/103 and script 95) through `CombatWorld` and
  drives six controlled scenarios: full chain in both strike orders and with 30 paused ticks,
  one survivor in each wave, and no player overlap. Each first-wave enemy takes two strikes (one
  respawn behind a fade). The chain completes with chest 59 enabled, gate 87 at y=300/action 103
  and script 95 run for 11 ticks; partial removals and a distant player never advance it; pausing
  delays every event by the pause length and leaves the random stream unchanged.

```
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_encounter.py --exe soul/port/build/Debug/soul_assets.exe
python tools/check_native_flying_enemies.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
```

## Boundaries

These are controlled fixtures with direct lethal strikes, not a level route or runtime parity.

- Only this chain is constructed; the rest of level 1 (other triggers, type-10 beams, platforms,
  prompts, passages, the type-2 behavior-5 records UIDs 136/137/181 and the intro camera script
  133) is not routed, and gameplay still stops at level 0.
- Chest 59's reward flow reuses the verified container owner; its ice-ability presentation is not
  exercised in this fixture. Gate 87's collision effect uses the existing gate owner.
- The player-overlap triggers 115/118/123 (scripts 23/24/114, mode 1) and level 2's removal
  triggers 322/319 (action 7, barrier animation) and 123/125 (type-13 targets) remain unported.
- No camera/render path changed, so widths 240 and 569 need no new presentation fixture here.
