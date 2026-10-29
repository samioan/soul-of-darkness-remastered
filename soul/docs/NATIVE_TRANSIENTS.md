# Native region cleanup for dropped items and effects

The preview now discards existing spawned pickups and collection effects when a
pending region transition is consumed. Placed pickups remain in their original
slots, and pickups/effects born after the sweep survive that tick. This extends
the ordinary-enemy region lifecycle; full shared world ownership remains unfinished.

## Original evidence

The region-change branch of `Game.W` (`updateWorldActors`) runs after the player
update and before rebuilding the nonplayer update queue. Respawnable actors use
their separate region reset path; basic enemies use their own reset path. For
other actors, UID -1 is removed unless the type is 84 or 85, or the actor is
type 39 with animation 13. UID -2 does not satisfy this rule. Hidden, paused and
out-of-update-range spawned actors are still swept: the loop scans actor slots
before update eligibility is checked.

When the player is already in action 0/animation 9 after its update, `Game.W`
returns before the sweep and leaves the pending change unconsumed. A normal tick
without a pending change does not sweep. `Game.removeActor` releases bounds and
links without running pickup handlers or awarding health/crystal rewards.

Original `e` bytecode was inspected for UID equality, the 84/85/39-animation-13
exceptions, the death guard and sweep-before-queue ordering. No generated Java
changes or new semantic renames were needed.

## Native integration

`CombatWorld::finishPlayer` now returns whether its region-change sweep ran.
`MovementPreview` uses that result to call `discardRegionTransients` before
pickup/effect handlers and before inserting this tick's enemy/chest drops.
Combat effects already belong to `CombatWorld`; their cleanup uses the same
removal predicate. Collection effects are currently constructed only as spawned
type-39 animations 11/12 and can all be released by their owner.

The pickup vector retains its slots so `placedPickups` and crystal indices stay
valid. A swept pickup becomes inactive, loses collision and queued spawn events,
and clears its native crystal-modal request. Original action, movement, timers,
delay and lifetime values are retained rather than running a collection or expiry
handler. Native crystal-boundary queries also require the source to be alive.
Existing crystal modals continue to freeze world updates; this pass does not
consume a transition in the middle of a displayed crystal reward.

`removeTransientOnRegionChange` describes the final transient branch, after
respawnable/basic-enemy dispatch. It is not a replacement for either earlier
ownership branch. Flag-8192 pickups are excluded from deletion; their complete
respawnable lifecycle remains outside the supported pickup constructors.

## Checks

`TransientCheck.java` invokes the original/reference `Game.W` with ordinary
handlers paused to isolate the sweep. Its matrix covers types 35/39/84/85,
UIDs -2/-1/19, animations 0/11/12/13, flags 0/32/1024/8192, and changed-region,
same-region and player-death boundaries. All 576 removal decisions match the
native predicate. There are 4032 original/reference expectations, including
independent assertions that health and the seeded actor timer remain unchanged.
These minimal metadata actors test the sweep; they do not execute type-84/85
handlers, real saves, a MIDP runtime or complete world ticks.

Fourteen additional data-free assertions exercise the actual native owners:
placed-slot identity, delayed loot, crystal request cleanup, retained UIDs/flags,
collision/event release, effect accounting, repeat sweeps, new births, persistent
type exceptions and the exact world notification/death guard. Combined player
checks reach 214 assertions. Three CLI rejection tests bring CTest to 45 tests.

`--region-script` is a controlled headless first-level fixture. It creates two
old drops, moves the player into another real region, inserts an existing
collection effect before the sweep and creates a new enemy drop afterward.
Twelve runs verify two removed pickups, one removed effect and one surviving
newborn at widths 240/569, 30/60/144 FPS and interpolation on/off. Simulation
state is compared across all settings; final pixels are compared per width.
The teleport and explicit births are fixture setup, not recorded player inputs.

Completed validation: default Java regeneration compiles and passes all reference
checkers; all 45 CTests pass in Debug and Release. Both configurations pass the
new transient matrix/live fixture and existing region, pickup/effect, chest/reward,
combat, bridge/platform and startup checks. The local distribution is rebuilt
with executables/settings only and no game assets.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_transients.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

## Remaining ownership

The native owners still cover selected actor types. Type-84/85 and animation-13
exceptions are verified as policy decisions, without porting their handlers or
rendering. Exact mixed actor/region dispatch, attached actors, UID/spawn-pool
aliasing, persistent removed UIDs, checkpoints/saves, scripts and remaining
objects are unfinished. Supported placed actor/region dispatch now runs in record order; see
[NATIVE_WORLD_ORDER.md](NATIVE_WORLD_ORDER.md). Next: linked objects/triggers and
the remaining shared world owners. The Windows executable still provides a first-level preview.
