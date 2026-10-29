# Native ordinary-enemy death, reset and loot

Ordinary type-1 death handling now selects loot, restores placed spawn records,
removes transient actors and executes finite/repeating respawn fades. Native
pickups also support hidden enemy-drop delay, signed spawn positions and inherited
facing. These paths now also connect to the [live combat preview](NATIVE_LIVE_COMBAT.md),
with the remaining world/level boundaries documented there.

## Cleanup and reset

Action 3 integrates its last velocity first, drops loot at that position and
stops motion. It clears the side-contact enabled flag and decrements the stat's
respawn counter. A placed enemy with nonnegative UID and record[12] <= 0 resets
from its owned spawn record: position/fixed coordinates, facing, flags, health,
activation area and interaction bounds. It stays registered but sets inactive
flag 1024. Runtime flag 512 survives record reset. Spawned enemies (UID -1) and
records with positive record[12] leave the registry and release their bounds.
Native `alive` reports registry presence; `enabled` mirrors original O.

The reset preserves the original field reuse: timers, tile-probe state and
cooldown are not zeroed wholesale. `setAnimationImmediate` resets the animation
clock/pending request only when the recorded animation differs. The same-pose
fixture verifies that a pending animation can survive record reset. Placed
respawn counters clamp at -1; a removed actor does not run that record override.

A remaining respawn budget or flag 4 starts hidden action 5 at timer 1500,
clears inactivity and clears record flag 32. Action 5 decrements by 83 until
<=800, action 6 emits type-39 animation 28 at the body's vertical midpoint,
and action 7 resumes the countdown. On expiry it copies the current player's
collision rectangle into the activation area, returns to action 0 and unhides.
This takes twenty updates after entering action 5. The next update can activate
pursuit. A count of one permits one fade; a second death becomes inactive.
Flag 4 repeats even after the counter is exhausted.

The native owner stores the constructor's record/health for this ordinary
level-0 slice. It does not implement Game's shared spawn-record pool, alias/reuse
rules, global UID/removal/save tables, linked ownership or camera reactivation.
Serialized hidden variants 4+ and script-linked flags remain rejected. The
existing Java lifecycle checks cover broader original pool behavior separately.

## Loot and delayed pickup behavior

Health-drop selection uses the original health-percentage polynomial and always
consumes its decision roll. At full health the threshold is 1250 out of 10000;
at zero health it is 10000. Magic drops additionally require unlocked magic and
magic <=40% of maximum, then use `100 - magic*60/maximum` as the threshold.
Health selection precedes magic selection. Random state is shared with combat.
The supported meter maxima are positive, as in normal game state.

Spawn offsets preserve randomRange(-40,40) and randomRange(-20,-100). The second
range is reversed in the original. Because its signed remainder follows the
numerator, ordinary rolls produce Y offsets -20..59. The port preserves this;
it does not swap the bounds or invent an upward launch.

Drop events have type 35, sprite 33, animation 0/2, priority 308, UID -1 and
inherited facing. `Pickup::enemyDrop` sets hidden flag 128, action 106 and delay
1162, with zero factory fixed coordinates and lifetime 6000. The common update
and action 106 each subtract 83 from the delay. The seventh update reaches
zero, unhides and enters action 0; attraction and lifetime countdown begin on
the following update. Hidden pickups still advance their animation clock.
Pause freezes both handler and clock. Negative pixel spawn coordinates are
preserved, unlike unsigned coordinates in serialized records.

Enemy drops retain facing through reveal, attraction, expiry and their collection
effect. This is separate from the existing chest-spawn fixture's explicit
facing reset. Spawned health/magic collection then follows the existing +6/capped
meter and disappearance rules. No new game asset parsing or decoding changed.
CombatEffect now also handles respawn animation 28 with ordinary ending removal.

## Verification

Default regeneration runs DeathCheck against unmodified original and generated
Java with the ImageIO test adapter. No bytecode or drawing hooks are suppressed.
The native checker compares **2,639 oracle rows**:

- 144 loot-selection combinations and their subsequent random-stream values;
- 1,760 snapshots over 880 ticks across eleven death/reset/fade fixtures;
- 557 pickup states across seven delay/collection/expiry fixtures;
- 109 loot, collection and respawn spawn events;
- 58 animation-28 effect states, with pause/frozen clocks;
- eleven final death-fixture random-stream values.

Original/reference comparisons total 67,454. The death fixtures cover placed,
spawned and positive-link records, repeated/finite respawns, persistence/flag32,
same-animation reset, motion-before-cleanup and pause. Independent assertions
require respawn and resumed pursuit. The player remains stationary with synthetic
non-damaging bounds; reciprocal combat and full actor queues are not executed.
Sixteen new data-free assertions bring the combined native executable to 149;
CTest remains 27 tests in Debug and Release.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_deaths.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
python tools/check_native_deaths.py --exe soul/port/build/Release/soul_assets.exe
```

Next: connect first-level ordinary enemy ownership and original update order,
player body contacts, reciprocal damage, slash hiding, ticked invulnerability,
loot/effect ownership and player-death transitions. Live combat must be checked
at widths 240/569 and independent FPS before calling the preview playable.
Platforms, scripts, saves, remaining enemies, menus/HUD and audio are unfinished.
