# Native timed switches (type 89)

The native world now owns type-89 timed switches, resolves their action state
for linked gates, dispatches them in placed-record order, advances their
animations and draws them using sprite 28. The second level remains disabled:
its type-3 enemies, existing-type variants and full route are still unfinished.
The type-80 energy barrier is now covered in NATIVE_ENERGY_BARRIERS.md. This is one completed handler in that task.

## Original behavior

Original `Actor.I()` (`updateTimedSwitch`), its type-89 record constructor,
`Actor.n(int)` action entries, fire-attack predicates and animation/bounds
lifecycle were checked against `tools/bytecode.py b`. The handler decrements
timer `aw` by the original 83 ms before processing the current action.

- Action 0 accepts fire-sword animations 13-15 or overlap with a present fire
  pulse. Inclusive edges count; a rectangle reduced to a point does not. Ice
  sword animations and ice projectiles do not activate it. A pulse's hidden
  flag does not suppress this original overlap test.
- A hit enters action 101, requests animation 26 for the ordinary variant and
  loads duration `au`. Another hit replenishes that duration.
- Mode 0 counts down independently. Mode 1 remains active only while its linked
  actor is in action 102 or 105; another linked state resets to action 0 and
  requests warning animation 27. The UID lookup occurs on every handler call,
  allowing a replacement owner with the same UID to supply the current state.
- The final 1000 ms request warning animation 27; expiry enters action 102,
  requests idle animation 25 and waits for an animation ending before action 0.
  Duration -1 remains active, including without a usable linked actor.
- Pause, hidden-handler flag 1024 and frozen-handler flag 16384 prevent handler
  updates. Animation advance retains the original independent eligibility rules.

Variant 0 is selected by initial animation 25; another initial animation uses
the same state/timer logic without those animation requests. Initial flag 8
does not replace type-89 collision bounds with the record's custom rectangle.
Bounds use the sprite, including the refresh caused by a pending animation
request before that animation is applied. The oracle caught that timing detail.

A missing linked actor during a finite, unstruck mode-1 activation raises a
native format error at the original null-dereference boundary. No guessed
target or fallback state is substituted.

## Native ownership and checks

`CombatWorld::loadTimedSwitches` reserves and constructs the selected records
before dispatch. Gates may then borrow stable action-state addresses through
the existing UID resolver. Unload destroys gate borrowers before switch owners;
the selected data bundle outlives both. Switches participate in the frozen
update queue, camera buckets, animation visibility and sprite drawing.

The switch handler reads the world's current fire-pulse owners before their
existing external magic update. Full original mixed-actor/projectile birth and
dispatch order remains the limit documented in NATIVE_WORLD_ORDER.md and the
later-level roadmap. The current integration is not a second-level play-through
or a verification of all later script, removal or checkpoint variants.

`TimedSwitchCheck` compares original bytecode with regenerated Java using the
existing ImageIO test adapter. Its **4153 rows** cover 23 ninety-tick timelines,
with snapshots after handler and animation phases, plus construction of all
13 placed switches: three in level 1 and ten in level 4. Scenarios include fire
and ice attacks, fire pulses, repeated/held hits, edge/point/missed rectangles,
pause, hidden/frozen/stopped animation flags, independent/dependent timing,
closed/replaced linked owners, short/infinite durations and animation variants.
The native manifest matches every field, including all three bound rectangles.

The data-free suite adds **25 assertions** for attack/timer/link boundaries and
live native switch/gate worlds in both record orders. Those worlds verify
same-tick versus following-tick gate activation, queue identity, stable UID
links and teardown. They use synthetic assets/maps, not the second-level route.
The new CTest brings the suite to **105 tests**, alongside the existing
**530 player assertions**.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_timed_switches.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
```

Validation on 2026-10-30: default regeneration exited 0, compiled with zero
errors and passed ReferenceCheck's 4218 comparisons plus all registered checks,
including the new 4153 rows. Debug and Release passed all 105 CTests. The switch
manifest matched in Debug, Release and the rebuilt data-free package. Selected
level reconstruction, finish/Continue/replay and intro playback/skip host
regressions still passed at widths 240/569 across their FPS/interpolation cases.

Oracles, logs and binaries stay ignored. No game data is committed or packaged.
