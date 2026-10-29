# Ice and moving-object links

The native type-33 attachment branch now has explicit ownership and cleanup,
with snapshots checked against original bytecode. Ordinary ice shots retain
their original pass-through behavior against these objects. This is a bounded
handler/ownership pass, not a new interaction reachable with the shipped shot.
The complete game remains unfinished.

## What the original actually does

`Actor.A(int)` / `applyWeaponEffectHits` requires a type-33 target in animation
0 and an overlap with the shot's **attack** rectangle. Sprite 1/animation 7 has
a point attack rectangle in the supplied game data. `Actor.b(int[],int[])` /
`rectsOverlapInclusive` rejects such points. Six natural original-world fixtures
therefore pass through without attachment, including left-facing shots,
hidden/locked/alternate-pose targets and reversed actor order.

Original `b` bytecode confirms the attack-bounds read (`k`), the type/pose
gates, immediate animation 8, `setAttachedActor`, reverse `s` link, facing-based
edge snap and offset write. This is not a decompiler error to repair or a
reason to widen the shipped hitbox. Original `Game.f(4,...)` still births the
sprite-1 shot. The separate type-37 gate and type-75 trap branches need their
own native controllers; there is no direct type-50 chandelier attachment
branch in this weapon-effect dispatch.

The controlled branch fixtures deliberately replace a shot's collision and
attack rectangles with the target rectangle, then invoke the unchanged
original hit handler once. Those synthetic rectangles are fixture setup only.
Subsequent ticks execute actual original `Game.W` world updates, ordinary
bounds refreshes and animation clocks. No asset or generated Java edits are
used to make this branch reachable.

## Ownership and ordering

`IceProjectile::tryAttach` preserves the original target/rectangle gates,
immediate freeze pose, flags and facing-dependent edge position. Type 33
positions its active child at its collision edge before final contact handling.
The original writes the vertical offset on the child but reads the parent's
offset while following; the separate offset fixture preserves this asymmetry.

`Game.W` updates an unprocessed child immediately after its parent. A child
with flag 16 also remains in the frozen ordinary update queue. Parent-first
order can consequently process it twice in one tick; child-first order does
not add that immediate update. The native world resets the per-tick marker,
runs the immediate child update at the parent boundary, and retains the
ordinary projectile update. Pause suppresses both simulation calls.

Standable ice still checks wall support. An attachment by itself does not
provide a solid tile: in the controlled empty-wall arenas the ice starts
melting shortly after reaching pose 9. The rider fixture lands during that
brief interval and is released when melting starts. This pass does not claim
a ten-second ride on a moving object.

Melting clears the parent link and flag 256 and releases the rider. Region
cleanup and either owner's destruction clear borrowed pointers before their
storage disappears. The native backlink registry also retains overwritten
reverse links, because the original parent setter clears the previous child's
flag without clearing its reverse `s` pointer. Teardown safely clears those
remaining references; broad multi-child runtime equivalence is not established.

## Validation and next work

`IceAttachmentCheck` runs in default regeneration using the existing ImageIO
test adapter, not a MIDP runtime. Seventeen 180-tick original/reference world
arenas produce 6157 expectations and 18411 native-comparable rows, including
birth and pre/post-animation snapshots of player, parent, child, timers,
rectangles, links, support and removal. Eleven explicitly controlled-link
arenas cover following, facing, vertical travel, both orders, pause, early
melt/replacement, rider release, automatic wall-loss melt and offset handling.

```
python tools/check_native_ice_attachments.py --exe soul/port/build/Debug/soul_assets.exe
```

The checker requires every row to match, no links in natural cases, point
attack bounds throughout natural flight and attachment/cleanup in controlled
cases. Seven additional data-free assertions exercise the actual native world
update boundary, pause, cleanup, point rejection and parent destruction with
overwritten backlinks. Player assertions now total 415; CTest remains 75 tests.
The existing projectile host chains continue to verify actual enemy hits,
wall footholds and natural jumping at widths 240/569 across FPS/interpolation.

Validation on 2026-10-29: full default regeneration passed compilation,
ReferenceCheck and all original/reference fixtures. Debug and Release passed
all 75 CTests, the new attachment checker and existing ice/fire/melee/player,
ordinary/flying combat, world/region, platform/bridge, chest/ability/arena,
gate/script and passage regressions. Startup/English-story/HUD/pause checks
also passed. The data-free package matches Release and passes the attachment
checker plus all 17602 existing projectile rows and 24 gameplay host chains.

Next: return to remaining first-level progression, checkpoint/restart ownership
and level completion/loading. Other weapon/object targets remain bounded by
their individual unported handlers. Native saves and full level-2 play are
still unfinished.
