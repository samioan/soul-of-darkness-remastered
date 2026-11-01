# Second-level ambush scripts (triggers 115, 118 and 123)

Three player-overlap triggers in the second level's top-left corridor each start a short
two-track script. The script repositions a placed ground enemy, releases it, and drops a
chandelier. This is the level-0 chandelier pattern (trigger 161 / script 160) with different
actors, so the verified `ChandelierScript`/`ChandelierTrigger` owners were generalized instead
of adding a second implementation. Normal gameplay still constructs level 0 only.

| Trigger | Rectangle (x, y, w, h) | Script | Enemy track | Chandelier track |
| --- | --- | --- | --- | --- |
| 115 | 92,160,175,100 | 23 | type-1 enemy 92 | type-50 chandelier 89 |
| 118 | 327,161,250,100 | 24 | type-1 enemy 93 | type-50 chandelier 90 |
| 123 | 328,160,250,100 | 114 | type-1 enemy 94 | type-50 chandelier 91 |

All three records are type 40, condition 1 (source overlaps), `p[11]==1` (script), action 0,
source 131 (the level-1 player UID) and no target UIDs. Triggers 118 and 123 overlap by almost
their whole area and differ by one pixel at the left edge, so they start in the same tick except
when the player's box first reaches that one-pixel strip between ticks.

## Original evidence

Inspected in the regenerated Java (`Game.updateScript`, `Game.executeScriptOpcode`,
`Actor.updateSwingingPlatform`/`V`, `Actor.a(byte)`/`H`/`c`) and checked against the original
bytecode; no names or patches changed.

- Each enemy track is `107` (set raw flag 16384, freezing the enemy), then five `21` position
  keys at ticks 0/2/4/6/8 walking the enemy about 40 px along the corridor, then at tick 8 `108`
  (clear 16384) and `106 0x65`. Each chandelier track is `21` (its own position), `22 5` (immediate
  animation 5) and `106 0x66` at tick 8. The chandelier therefore starts falling at tick 8.
- Command `106` is not a raw assignment for a basic enemy: `Game.executeScriptOpcode` calls
  `changeActionState(101)` when `isBasicEnemyType()`, and assigns `actionState` otherwise.
  Script 160 never used it on its enemy; the native owner now routes enemies through the
  existing `EnemyType1` action entry (`scriptAction`) and still assigns type 50 directly.
- Commands `23`/`24` (script 24 uses `23 01000000` at tick 0): the 32-bit operand is extended with
  `|= 65536` when bit 0 is set, and `flags |= v>>16` (23) or `flags &= ~(v>>16)` (24) is applied when
  `keyTick <= oldTick`. Command 23 recomputes `facingLeft = flags&1` inside that condition; command
  24 recomputes it unconditionally. Enemy 93 starts with flag bit 0 set, so the effect is to re-assert
  facing left.
- The level-1 chandeliers are `p[6]=-120`, `p[7..9]=0`, mode `p[10]=1`, direction `p[11]=1`: a
  zero-excursion pendulum. `V()` then flips direction every tick without moving the angle, so
  the platform hangs still in state 101 until the script assigns 102. The native constructor
  previously required excursion `p[9]>0`; it now accepts `p[9]==0` (still bounded by 80).
- `Actor.a(byte)` appends the selected script to `Game.ck[cj]` and `Actor.c` only does
  `Game.cj--`, so finishing a script frees the **last** slot even if an earlier script finished
  first. A script left outside `ck[0..cj)` while its clock is still running is updated by
  `updateBackgroundScripts` and `updateWorldActors` is skipped while it runs (a world-freezing
  cutscene). This matters here because triggers 118/123 can start one tick apart.

## Native implementation

- `ChandelierScript` accepts scripts 160/23/24/114 with their pinned target pairs
  (205/34, 92/89, 93/90, 94/91) and the opcode set 21/22/23/24/106/107/108 (unsupported targets,
  track kinds and commands still fail explicitly). Commands 23/24 act on an optional facing hook;
  106 uses an optional action hook, otherwise assigns raw.
- `ChandelierTrigger` keeps its own `scriptUid`. `CombatWorld::loadChandelierScript` now builds one
  script per placed supported trigger (requiring the owned player as source, rejecting two triggers
  on one script) and no longer assumes a single trigger.
- `SelectedScriptSlots` reproduces the original three-slot array: `select` appends,
  `release` decrements the count without clearing the stale entry. The world updates selected
  scripts in slot order and **refuses** (explicit `FormatError`) the case where a still-running
  script would fall out of the selected slots, rather than approximating the original's
  world-freezing background update. Gate script 95 and other script owners are not merged into
  this slot array; the chandelier scripts and the gate script act on disjoint actors.
- Resolved targets include type-1 enemies (with their facing flag and action hook) and type-50
  platforms. Falling chandeliers see the placed enemies through the existing target list.

## Verification

- `AmbushCheck.java` runs the original and regenerated handlers on the real level-1 collision
  grid (118x67 tiles) for the real records of each chain. The `Game.cj/ck` slot arrays, script
  clock, track offsets, trigger latch/liveness and the full enemy and chandelier snapshots are
  compared every tick (100 ticks, two phases). Twelve scenarios: scripts 23/24/114 alone,
  the 118+123 pair, no overlap, inclusive rectangle edge and one pixel outside, pause, a hidden
  trigger, the player leaving after the start, a hidden source (which still triggers) and a hidden enemy.
  The oracle holds **12800 rows** (2421 original/reference expectations); the native probe matches
  every row. Independent assertions pin the start-after-selected-update timing, enemy freeze, slot
  release, the same-tick two-slot pair and the never-started scenarios.
- Fourteen new data-free assertions (player suite now 557) cover the script set and target pinning,
  commands 23/24/106 (including the no-hook raw assignment), `SelectedScriptSlots` capacity,
  stale-slot and underflow behavior, and the level-1 trigger records.
- `--ambush-world-manifest` drives real `CombatWorld` fixtures with a frozen, positioned player:
  each script alone (start tick 0, end tick 9, chandelier 104 at its pivot y=20, enemy released into
  action 101, trigger removed, slots 1 -> 0), a 30-tick pause delaying everything by 30, the pair
  (both slots, both finish together), no overlap, and a staggered 118-then-123 start that is
  refused at tick 10 as a background-script order.

```
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_ambush.py --exe soul/port/build/Debug/soul_assets.exe
python tools/check_native_chandelier.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

## Boundaries

These are controlled fixtures, not a route through level 1 or runtime parity.

- Staggered starts of 118 and 123 (a one-pixel window when entering from the left) are rejected;
  the original would freeze the world while the later script finishes as a background script.
- The chandelier oracle does not include the enemy being hit by the falling chandelier and the
  player is not updated in the oracle (its fall damage uses the existing verified path).
- The other type-50 variants in level 1 (UIDs 25/33/145/190 mode 0 with zero excursion, UIDs 169/510
  with excursion/speed values) are now accepted by the constructor but are not covered by this
  oracle; only the mode-1 zero-excursion chandeliers 89/90/91 are.
- Triggers 187 and 322 belong to the third level (index 2), not level 1; every level-1 type-40 record now has a
  native owner. The sine flyers (UIDs 136/137/181) and beam variants are covered by
  [NATIVE_LEVEL1_ENVELOPE.md](NATIVE_LEVEL1_ENVELOPE.md). The intro script 133 is covered by [NATIVE_LEVEL1_INTRO.md](NATIVE_LEVEL1_INTRO.md); the level route
  is still pending. No camera/render path changed, so no new 240/569 presentation fixture was needed.
