# Native flying enemies and second arena wave

The first level now loads all seven type-2 records with sprite 3 and stat table
2. All seven use variant-data index 4, behavior 0, without an attached actor.
UIDs 113/48 wait for removed-source trigger 54. The trigger enables them after
UIDs 114/117 finish their last permitted lives. The following pass now connects
trigger 55 and chests 56/57 through the magic reward and gate; see
NATIVE_CHEST_UNLOCK.md. The verification below records the original flying pass.

## Behavior and ownership

`EnemyType2` shares the verified damage, sprite bounds, animation, respawn and
loot helpers with `EnemyType1`. Its handler integrates fixed-point flight without
tile collision or gravity. It activates on player/custom-bound overlap, aims
25 pixels above the player with the original approximate distance function,
and runs pursuit 101, windup 200, strike 102 and recovery 103. Windup/recovery
each start at 500 ms, decremented by the original 83 ms tick. Hurt uses animation
7; death emits effect 3 five pixels above the actor. Spawn facing is inherited
with the original toggle, so the death effect is unflipped even for a left-facing
parent. Damage cooldown, physical weapon exclusion of body damage, invulnerability,
loot RNG and final removal use existing world ownership.

The native world merges both enemy types into its placed-order dispatch and
previous-tick body-contact queue. Eligibility is frozen before handlers run:
clearing a waiting enemy's flags does not insert it into that tick's queue.
Region resets, animation clocks and culling use the reference 240-pixel view
independently of the presentation width. Negative off-map placement shorts retain
the original unsigned-coordinate construction behavior.

`RemovedEnemyTrigger` implements type 40, condition 4, actor mode 0, operation 0
for two source enemies and two enemy targets. It caches source persistence
indices, observes final removed-UID marks, clears target flag 32 and clears 128
outside respawn action 5, then removes itself. Preserve two original quirks:
index zero satisfies the removal test, and a failed first-source lookup changes
that source UID to -1, whereas failed second/third lookups retain their UIDs.
Missing targets do not postpone one-shot deletion. The world currently selects
the shipped type-1-to-type-2 trigger 54 and, through the later chest-owner
bridge, type-2-to-container trigger 55.

## Verification

Default `tools/rename_sod.py` regeneration compiles and runs these original versus
reference Java checks using the analysis ImageIO adapter:

- `FlyingEnemyCheck`: 4047 expectations; 2719 native-comparable rows covering
  pursuit, distance zero, both facings, diagonal flight, attack clocks, pause,
  hidden/frozen/waiting flags, clock freeze, hurt, final removal, fade, effect
  births, loot and final RNG state.
- `FlyingWorldCheck`: 1729 expectations; 1728 rows from real Game world dispatch
  and reciprocal player combat, including mixed flying/ground body queues.
  Initial camera buckets are explicitly seeded to zero as a controlled setup.
  Removed actors' null bounds are exported as zero rectangles.
- `RemovedEnemyCheck`: 209 expectations; 208 trigger rows covering source order,
  partial removal, missing/late links, index zero, pause, hidden/frozen state,
  fade targets and hidden sources.

These total 6001 original/reference expectations and 4671 native comparisons (the removed-enemy oracle gained the `slot-b` scenario for level 1's trigger 104).
They establish the selected paths, not general type-2/runtime equivalence.

```powershell
python tools/check_native_flying_enemies.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

The checker also runs 24 two-wave arena fights across widths 240/569,
30/60/144 FPS and interpolation on/off, through direct gameplay and native
startup/pause/resume. The headless `--second-wave-script` and
`--game-second-wave-script` fixtures position the player at the arena and grant
invulnerability; normal movement/melee inputs then produce six defeats: four
type-1 lifetimes and two type-2 final removals. Neither enemy health/flags nor
chest locks/gate scripts are forced. Short runs verify the second wave stays
locked at entry. Seventeen additional data-free assertions bring player checks
to 353; three argument guards bring CTest to 63 tests.

Validation on 2026-10-28: full default Java regeneration passed. Debug and
Release passed all 63 CTests, the new checker and existing linked/world-order,
region, combat, transient, bridge, platform, gate/script, passage, container,
ability, first-wave and frontend regressions. Native game captures at both
widths show the active flying wave, English HUD and clipped arena boundaries.
`build_dist.bat` rebuilt the data-free package; its binaries match Release and
pass the 4671-row comparison plus all 24 two-wave fights.

Remaining boundaries include other flying variants, linked/projectile enemies,
broader cutscenes, checkpoint persistence,
save integration and the rest of the game.

The combat-only host checker now expects trigger 55 to reveal both chests after
the second wave while leaving them unopened. The later arena fixture exercises
actual chest interaction, magic collection and gate opening; see NATIVE_CHEST_UNLOCK.md.
