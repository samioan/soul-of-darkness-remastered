# Native region lifecycle

The first-level preview now restores ordinary enemies when leaving a camera
region, then permits their return when entering that region again. This is a
limited world-lifecycle pass; the complete original world owner remains unfinished.

## Original evidence

`Game.k` (`refreshCameraRegionSelection`) walks type-22 records, skips the active
region and stops at the first region that selects itself. `Actor.aD`
(`updateCameraRegion`) uses point containment with open left/top and closed
right/bottom edges. `Game.a(Actor)` stores the old region in `av`, the new region
in `au`, and sets `ai` for a change. Leaving all rectangles retains the last region.

The next `Game.W` (`updateWorldActors`) region-change pass checks respawnable
actors against the new region first. Inclusive rectangle overlap clears flag
1024. Otherwise, overlap with the old region calls `resetOrRemove(false)`.
New-region membership therefore wins for an enemy straddling both rectangles.
Non-respawnable basic enemies reset when their action is nonzero.

`Actor.b(false)` / `Actor.a(false,false)` restores the saved spawn record without
dropping loot, decrementing the respawn count or entering the death fade. It
clears movement, restores placement, initial health and the record's respawn
count, retains flag 512 and sets flag 1024. Fields that initialization does not
write, including damage cooldown and scratch timers, retain their values.
Animation clocks reset only if the requested animation changes. The native
enemy owner retains its existing stable slot and copied spawn record.

Original `e` and `b` bytecode was inspected using `tools/bytecode.py` for the
transition precedence, reset call and no-loot/no-decrement branch. No generated
Java changes or new semantic renames were needed.

## Native integration and checks

`CombatWorld` owns active/previous region indices and a pending transition.
The later [ordered dispatch pass](NATIVE_WORLD_ORDER.md) now selects regions
among supported actors in record order. A change is consumed on the next world
tick, after the player update and before rebuilding the enemy queue. Player-created combat effects join before this cleanup, as in
the original. Ordinary transient combat effects are discarded on transition.
Region coordinates preserve unsigned placement plus signed rectangle offsets.
Supported regions require the first-level flag-8 rectangular record format.

`RegionCheck.java` runs original/reference region selection and the original
world change pass with actor updates paused. Eight scenarios exercise outgoing,
incoming, straddling and unrelated enemies, hidden/retained flags, unchanged
animation, and a nonzero respawn count. Eleven positions include exact edges,
gaps, departure and return. The 176 snapshots include region identities, pending
state, complete ActorCheck movement/bounds/clocks, health, respawn count, enabled
state, cooldown, timer, facing and activation/interaction bounds. All match the
native probe; 354 original/reference expectations also pass. These controlled
fixtures do not run full gameplay or rendering.

Seven new data-free assertions cover deferred selection, reset/return, open
edges, no rewards/kill credit, live tick consumption and transient effect cleanup.
The combined player suite now has 188 assertions; CTest remains 39 tests.

Validation completed in Debug and Release: all 39 CTests, the region and enemy
death oracles, real-level combat/chest/platform runs at widths 240/569 and
30/60/144 FPS with interpolation on/off, and startup/story checks pass. Default
Java regeneration completes successfully with compilation and every registered
reference checker. The local distribution was rebuilt without game assets.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_regions.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
```

## Remaining ownership boundaries

The explicit `Game.k`-style scan remains a controlled loading/reset helper.
Normal preview ticks now dispatch regions among supported placed actors; see
NATIVE_WORLD_ORDER.md. Full mixed ownership and camera-controller bounds remain
unfinished. Scrolling level modes 2/3 are excluded. Native effects currently use
only ordinary combat animations; the original animation-13 exception remains
outside that effect owner's supported constructors.

Dropped pickups and collection effects now receive the region sweep through
their separate owners; see [native transient cleanup](NATIVE_TRANSIENTS.md). Spawner type 74 and other
enemy types, attached actors, global UID/pool aliasing, script-driven regions,
checkpoint/save reconstruction and full camera behavior remain pending.
Type-33/50 platforms retain their stable support ownership; this pass does not
reset platforms or implement flag-8192 variants. Type-45 bridges now run through
their separate owner; next are linked object/trigger integration and the remaining owners.


The normal game presentation now consumes the published region bounds for camera
clamping and world clipping (see NATIVE_PASSAGES.md). Type-59 room transfers can
request immediate destination selection; cleanup still occurs at the next world
boundary. The subsequent [camera-controller pass](NATIVE_CAMERA_CONTROLLERS.md)
adds the first-level type-49 modes, retaining separate camera constraints and
current-region presentation clipping. Other controller modes and full ordinary
camera-follow behavior remain pending.
