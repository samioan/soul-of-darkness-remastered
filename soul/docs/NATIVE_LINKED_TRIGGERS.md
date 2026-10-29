# Native linked area trigger

The first-level preview now loads type-40 UID 182. Its 23 by 23 rectangle at
(200,718) controls the forced-update flags of chandeliers 25 and 177. This is
the area-condition / actor-target / continuous-update branch (record parameters
10/11/12 = 0/0/2). The other seven first-level type-40 records and the type-31
gate remain outside this pass; several depend on scripts or unported actors.
Full level progression remains unfinished.

## Original evidence

`Actor.bh` (`updateLinkedTrigger`) resolves target UIDs lazily and caches the
references. Missing links retry on later handler calls. Condition zero compares
the trigger rectangle against **the active region rectangle**, using inclusive
edges. It does not test the player's body. The trigger sets its own flag 16
while overlapping and clears it otherwise.

For operation two, overlap calls `Actor.k(Actor)` (`forceLinkedActorUpdate`),
clearing target flag 32 and setting 16. Outside overlap, `Actor.m(Actor)`
(`disableLinkedActor`) sets 32 and clears 16. Other target bits survive; in
particular this operation does not clear draw-hidden flag 128 or update-pause
flags 1024/16384. The native target subset contains platforms, so the type-49
camera-controller removal special case is not exercised.

`Game.h(Actor)` gives type 40 a strict distance radius of seven, independent of
its constructor's activationRadius = -1. Flag 16 retains its normal precedence.
`Game.W` freezes eligibility before dispatch: a newly enabled target waits for
the next snapshot, while a target disabled after queue construction still gets
its already scheduled handler. Regions and triggers retain record order, so
a trigger before a region can see the previous region for one more tick.
Original `b`/`e` bytecode confirms these branches. No generated Java edits,
renames or decompiler patches were needed.

## Native integration

`LinkedAreaTrigger` owns rectangular placement and borrows stable platform flag
addresses resolved by UID. `CombatWorld` includes supported triggers in its
typed record-order queue, caches their reference-camera buckets and dispatches
them using the region selected at that slot. Platforms remain in their stable
list owner, preserving existing rider pointers. Loading the displayed first
level automatically connects the shipped trigger to both chandeliers.

The supported record requires rectangular placement, no condition-source UIDs,
and the exact continuous actor-target branch above. Other trigger branches are
left unloaded, as with other unported actor types. Links currently resolve only
native platform owners. Native startup can temporarily have no selected region;
the trigger waits until one exists. The original handler assumes one is present.
There is no general UID registry, removed-UID persistence, linked gate animation,
script-trigger lifecycle or camera-controller integration in this pass.

## Checks

Default regeneration runs `LinkedTriggerCheck.java` against both original and
generated classes. It emits 384 handler snapshots across twelve scenarios:
overlap/exit/reentry, outside, exact edges and corner, a one-pixel miss, world
pause, hidden and individually paused handlers, late/missing UID resolution,
and preservation of unrelated target flags. Target handlers are isolated.

Another 128 rows invoke actual original/reference `Game.W` in four controlled
worlds with regions, the trigger and two individually paused chandeliers.
These cover region-first, trigger-first, platform-first and world pause,
including cached links, full update-UID snapshots and deferred flag effects.
No animation/paint step runs in these world fixtures; initial camera buckets
are held fixed to isolate eligibility and dispatch. Across both sets, 515
original/reference expectations pass, and native output matches all 512 rows.

Twelve additional data-free assertions cover flag preservation, inclusive edges,
pause/hidden skips, late links, rejection of script branches, stable live owner
resolution, frozen queues, distant forced targets and strict radius seven.
The combined player checks total 238; CTest remains 45 tests. Existing real-level
chandelier rides also require the shipped trigger to be loaded and verify
unchanged rider state at widths 240/569 and 30/60/144 FPS, interpolation on/off.

Completed validation: default Java regeneration compiles and passes all
registered original/reference checks. All 45 CTests pass in Debug and Release.
Both native configurations pass the new trigger oracle plus ordered-world,
region, live combat, transient cleanup, bridge, swinging and moving platform
regressions. Their live host fixtures cover widths 240/569, 30/60/144 FPS and
both interpolation settings. The rebuilt local distribution passes its package
checks and the new trigger oracle; it contains no game data.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_linked_triggers.py --exe soul/port/build/Debug/soul_assets.exe
python tools/check_native_swinging_platforms.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

Next: linked gates and further trigger/script owner integration, followed by
checkpoint/save-backed progression. The full game remains unfinished.

The subsequent [gate pass](NATIVE_GATES.md) adds type-31 construction, collision,
clipped drawing and selected controllers. Its first-level activation still
depends on native script execution and additional actor/trigger ownership.
