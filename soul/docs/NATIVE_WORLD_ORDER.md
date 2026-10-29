# Native ordered region and actor dispatch

The gameplay preview now includes type-22 regions in the supported placed-actor
update queue. A region selected before an enemy updates affects that enemy's
same-tick velocity clamp. The previous after-all-actors selection scan could
only affect the following tick. This pass covers ordinary type-1 enemies and
the currently loaded regions/platforms/bridges, not the complete original world.

## Original evidence

`Game.W` (`updateWorldActors`) updates the player, consumes any pending region
lifecycle change, then snapshots eligible actors in registry order. The snapshot
is retained through dispatch. `Game.h(Actor)` (`shouldUpdateActor`) checks flag 16
before pause/distance flags 256/32. Region initialization adds flag 16, so region
records remain queued regardless of camera distance. Handler pause flags still
apply after an actor has entered the queue.

`Actor.aD` (`updateCameraRegion`) tests open left/top and closed right/bottom
edges. `Game.a(Actor)` first clears `ai`; if selection changes, it retains the
old region in `av`, selects `au` and sets `ai` again. `Game.W` skips further
region handlers once a change occurs, while continuing other actor types. An
active region can run before a later overlapping region changes the selection.
The queued slots are not removed when their handlers are skipped.

The region handler publishes bounds R/T/S/U when no camera controller is active.
`Actor.aS` (`clampActorToActiveRegion`) runs at the end of an eligible nonplayer
enemy update and therefore uses whichever region has been selected by that
point in the queue. It stops outward velocity at the region's 12-pixel margins.
Cleanup for a newly selected region remains deferred to the next world tick.

The queue construction, flag precedence, skip predicate, region selection and
final enemy clamp were inspected in original `e`/`b` bytecode. No generated Java
changes or additional semantic renames were needed.

## Native owner changes

`CombatWorld` now uses typed slots for enemies, platforms, bridges and regions.
They are rebuilt from record order after loading additional owners, replacing
the previous encoded negative platform/bridge indices. A single eligibility
snapshot supplies dispatch, enemy contact handoff and ordered chandelier
targets. Region handlers run in that snapshot rather than an end-of-tick scan.
The explicit `refreshRegionSelection` helper remains available for the separate
loading/reset-style `Game.k` boundary and its original controlled checker.

Paused world updates still consume pending lifecycle changes and build the
queue, but do not execute the paused actor/region handlers. Enemy clamps are
also skipped when their handler did not run. The player's contact preparation
now preserves previous enemy bounds when the player itself is paused: the
original skipped player update does not run that bounds-refreshing contact pass.
This detail matters when an animation frame changed during the preceding paint.

Published region bounds are retained for state verification. The presentation
camera still follows the existing map-clamped prototype; this pass does not
implement the original camera solver or restrict widescreen to a phone viewport.
Simulation eligibility continues to use the reference 240-pixel camera, and
presentation width remains independent of the 83 ms simulation.

## Checks

`WorldOrderCheck.java` invokes actual original/reference `Game.W` with two live
ordinary enemies and interleaved regions. Twelve scenarios exercise regions
before/after enemies, interleaving, three overlapping regions, gaps and exact
edges, a paused interval, no regions, hidden/flagged regions, and a distant
enemy whose previous camera bucket delays eligibility after a teleport.

The player is held with its individual update-pause flag so controlled positions
can isolate dispatch; the enemy/world handlers run normally. Animation clocks
are advanced through the existing test paint helper, with explicit reference
camera/bucket maintenance. The distant scenario freezes enemy animation clocks
to avoid claiming a full offscreen paint runtime. No real MIDP framebuffer is
executed by this fixture.

Across 480 ticks, 3840 native-matching rows cover player and both enemy movement,
clocks/bounds/controllers, active/previous region, pending cleanup, published
camera bounds, health/invulnerability and the complete fixture update-UID queue.
All 1042 original/reference expectations pass. Independent assertions require
the early region to produce enemy velocity 256 where late region order produces
0 on the same controlled tick, and require the first changed region to suppress
the third overlapping region.

Twelve new data-free assertions verify mixed queue order, same-tick clamp,
overlap selection, pending bounds, paused dispatch/lifecycle handling, hidden
regions and the skipped player bounds refresh. Player checks now total 226;
CTest remains 45 tests.

Completed validation: default Java regeneration compiles and passes all
registered reference checks; all 45 CTests pass in Debug and Release. Both
configurations pass the new ordered-world oracle and existing region, combat,
transient cleanup, bridge, platform, chest and startup regressions, including
live host runs at widths 240/569 and 30/60/144 FPS with interpolation on/off.
The local distribution is rebuilt with executables/settings and no game assets.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_world_order.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
```

## Remaining integration

This is the supported placed-actor queue. Combat effects and pickup/container
owners still have separate update/birth boundaries; a complete common actor
registry, UID/pool reuse, attachments, persistent removals and script actors are
unfinished. Camera controllers and scrolling level modes 2/3 are excluded.
Type-22 records require rectangular flag-8 placement, with selected pause/hidden
bits accepted for controlled coverage. Camera-controller suppression of bounds
updates remains pending alongside the controllers themselves.

Next: first-level linked object/trigger integration and the remaining world
owners, followed by checkpoints/save-backed progression. The executable still
provides a first-level preview rather than the completed game.

The subsequent [linked area trigger pass](NATIVE_LINKED_TRIGGERS.md) adds UID 182
to this typed queue. Its active-region test and target flag writes preserve the
same frozen snapshot boundary; gates and script-backed triggers remain pending.
