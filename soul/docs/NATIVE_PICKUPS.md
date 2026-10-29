# Native health/magic pickups

The first-level movement prototype now constructs and renders all twelve placed
type-35 health pickups from pack 3, using sprite 2:33. Walk into a pickup to
collect it. R resets the prototype, including items and meters. The Win32 overlay
shows health, magic and collected count; this is a temporary readout, not the
original HUD. Meters start full, as in fresh player construction, so collecting
at full health still removes the item but does not exceed the maximum.

`src/pickup.cpp` ports the health/magic subset (animations 0/2) of
`Actor.initializeFromRecord`, `updatePickup` (original `b.bF`), the common
integrator and animation clock. Other pickup animations and unsupported record
flags fail explicitly. The sprite owner outlives all pickup instances; temporary
sprite borrowing is disabled. Collision and visual fallback bounds follow the
same sprite tables as the player.

Placed items attract within distance 10240 in fixed-point units (approximately
40 pixels); UID -1 dropped items use 40960. Distance uses original `e.e(int,int)`'s
weighted integer approximation, not Euclidean distance. The native arithmetic
preserves modulo-32-bit products and explicit signed shifts. Attraction targets
the player's vertical center and may inherit player velocity. Integration occurs
before the handler; contact tests use the previous refreshed item rectangle.

Contact enters action 100. The meter effect occurs when the current animation
frame is 1 or its timer reaches 830 ms, adds six health/magic with a maximum
clamp, then enters action 101. The item leaves the active set when its animation
ends. Simulation and animation advance only on 83 ms ticks. Drawing includes
the original alternating one-pixel horizontal offset and does not advance state.
Pickups draw before the player, matching their priorities (75 versus 101).

## Validation

Default regeneration extends `BehaviorCheck.java` with nine controlled scenarios
and writes ignored `soul/analysis/pickup-oracle.tsv`. Original and regenerated
Java are compared for every actor field and the existing selected world fields.
The 381 snapshots emitted from original bytecode include positions, velocities, flags,
animation, action, timer, distance, presence, collision bounds and meter values.
Scenarios cover health, magic, maximum clamp, far/near attraction, moving player,
pause, dropped UID radius and frozen animation. Tutorial flags are explicitly
pre-seeded as seen. Contact scenarios reposition the player's pixel X at tick 3
without changing fixed X, following the existing controlled behavior fixture;
they are unit scenarios, not recorded gameplay inputs.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_pickups.py --exe soul/port/build/Debug/soul_assets.exe
python tools/check_native_player.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --movement-preview --data soul/extracted --width 569
```

Eleven new data-free assertions check collection delay, one-time reward/removal,
magic maximum, attraction radius, dropped UID, pause, unsupported upgrade and
pure drawing. The combined player/input/pickup executable has 42 assertions;
the suite remains 21 CTests per configuration. Debug and Release pass both
the 381-row comparison and all CTests; the data-free Release package also passes. Existing twelve real-level host
runs still check widths 240/569, FPS 30/60/144 and both interpolation settings.
Their route does not reach a pickup, so collection coverage comes from the
controlled Java/native traces and data-free assertions.

## Remaining boundaries

This subset omits type-13 enemy attraction, upgrade/cutscene pickup animations,
multi-reward states and persistence. The [chest pass](NATIVE_CONTAINERS.md) adds
spawned-item lifetime and crystal contact; [crystal rewards](NATIVE_CRYSTALS.md)
now continue through the reward screen and resume gameplay. The [collection effect
pass](NATIVE_COLLECTION_EFFECTS.md) now simulates and draws the shipped type-39
effects and hides pickups in collection actions 100/101. First-time tutorial overlays are
suppressed in this prototype. There is no enemy damage yet, so ordinary play
starts and remains at full meters. All twelve first-level placed items use the
supported health animation and flags. Beyond ordinary chests, platforms, collision-writing
objects, combat, scripts, death and level completion remain unported.

The prototype updates its small pickup list directly. Original Game activation
queues, camera regions, registry reuse and save removal bookkeeping are not
implemented by this list. Broader object ownership and spawning must account for
those systems. Next: relevant object collision/contact
behavior and combat/enemy ticks.

[NATIVE_DEATHS.md](NATIVE_DEATHS.md) now adds an explicit enemy-drop factory:
signed positions, hidden action 106, seven-tick reveal and inherited facing.
Its separate oracle covers delayed collection/expiry; ordinary/chest pickup
oracles retain their previous schemas and scenarios. Live enemy/loot queues
remain pending.
