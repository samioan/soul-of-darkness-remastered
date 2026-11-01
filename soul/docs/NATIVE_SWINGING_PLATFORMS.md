# Native type-50 swinging platforms

The first-level preview now owns all six shipped type-50 chandeliers, alongside
its type-33 rider-controlled platform. Both use stable `MovingPlatform` owners
and `ContactBody` support identities. Their handlers follow original actor
registry order; type 50 draws at priority 2 before type 33 at priority 3.
This remains a level-0 gameplay slice, not complete level progression.

## Controller and reference evidence

`Actor.initializeFromRecord` type 50 sets pivot X from placement X, rope length
from parameter 6 and pivot Y to placement Y plus that signed length. Parameters
7/8/9 are angular speed, angular acceleration and excursion; 10 selects
boarding-triggered/cuttable mode 0 or continuous mode 1, and 11 is direction.
The twelve-element `moduleMask` here is controller state, not a render mask.
`Actor.n()` constructs the wider pivot-to-platform visibility envelope.

Original `Actor.s/br/V/T/U/j` bytecode confirms:

- Carry the supported player by the previous velocity before fixed-point
  integration. Refresh bounds and run support contact once before the controller,
  except in broken states 103/104. Retained type-50 support uses the exact top.
- Boarded mode 0 starts action 101 with a 2000 ms timer. Mode 1 starts in 101
  during construction. The shared support latch is cleared on a new landing.
- Angle uses sixteen fractional bits; angular speed advances it with a further
  left shift of eight. Preserve the center/turn latch ordering and speed changes.
  Positions use the original 256-entry table from pack 17/resource 0, integer
  degree indexing and signed shifts. Computed velocity applies on the next tick.
- Mode 0 resets at exact angle zero when detached or its timer is negative.
  Original `T()` restores placement/controller values and stops motion, while
  preserving the original fields it does not reset.
- Weapon-0 poses 13..15 can cut mode 0's narrow chain rectangle. Action 102
  falls with acceleration 768 and velocity cap 4608, checks falling impacts,
  then resolves vertical tiles without horizontal correction. Grounding enters
  animation 12/action 103; its ending enters 104. The next tick hides the platform
  and moves Y to the pivot. Action 103 clears the global support link, as original.
- Falling platforms apply 100 damage through the existing type-1 damage entry,
  respecting the original damageable-state exclusions. Repeated falling hits do
  not use the normal weapon cooldown. Player hits use stat-table 0/index 1 and
  existing hurt/effect ownership. World kill counters and effects include impacts.

No Java names or decompiler patches changed. The trigonometry resource is read
with exact byte consumption and copied into each controller, avoiding borrowed
table lifetime requirements. Sprite/maps remain borrowed from the scene owner.
World teardown clears an owned support link before releasing platforms.

## Rendering and timing

Chains use original six-pixel fixed-point segments, vertical colored strokes,
and animation 13's spark on cuttable chains. Broken states retain the upper chain;
state 104 suppresses the chandelier sprite. Rendering never advances angle,
support, animation or spark clocks. Simulation remains 83 ms, independent of
presentation FPS and interpolation. UI/startup behavior is unchanged.

The world retains its 240-wide simulation camera for activation; drawing reveals
the extra world at width 569. Type-50 animation visibility uses its original
four-bucket limit and custom pivot envelope. This does not port the complete
original render queue, backgrounds, HUD or region lifecycle.

## Checks and boundaries

`SwingingPlatformCheck.java` executes unchanged original and regenerated
`Actor.update` in fifteen controlled arenas: idle, boarding, both continuous
directions, low speed, jump, pause, forced timeout boundary, explicit reset,
chain cut, player crush, uncuttable mode, enemy kill/hurt and protected enemy.
At 240 ticks each, 416171 original/reference comparisons produce **15840**
player/platform/enemy snapshots before/after animation advancement. Independent
assertions require boarding, falling/breaking, player damage, uncuttable mode and
enemy damage outcomes. The enemy fixtures deliberately do not run enemy AI.
The timeout fixture seeds the exact center/timer boundary; it is not an organic
play-through. Projectile queues and full `Game.updateWorldActors` are not run.

The native inspector matches all rows. `--swing-script` places the player above
real level-0 chandelier UID 4. Twelve ten-second runs retain support and match
state at widths 240/569, 30/60/144 FPS and interpolation on/off; render CRCs match
within each width. Screenshots are inspected locally under ignored analysis/.
Six data-free assertions cover missing trig, unsupported angles, construction,
pause, drawing purity and owner teardown, bringing combined player checks to
181. Three argument checks bring CTest to 39 tests.

Full default Java regeneration passes compilation and all included comparisons.
Debug/Release each pass all 39 CTests and the swinging, type-33, support,
live-combat, chest and frontend checkers. The local Release distribution is
rebuilt and verified to contain no game data.

Remaining boundaries: projectile/special-weapon chain attacks, falling activation
of type-45 objects, other damageable actor types, general UID/pool/save ownership,
region restoration and other level collision modes. Type-45 actors and projectile
queues are not yet loaded by the native preview. No full phone-framebuffer or
complete-game equivalence is claimed.

```powershell
python tools/rename_sod.py
python tools/check_native_swinging_platforms.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --movement-preview --swing-script --data soul/extracted --headless --fps 60 --frames 600
```

Level 1 update: the constructor now accepts a zero excursion (`p[9]==0`, still at most 80). The
verified level-1 case is the mode-1, zero-speed, zero-excursion chandeliers 89/90/91 that hang still
until script 23/24/114 assigns the falling action; see [NATIVE_LEVEL1_AMBUSH.md](NATIVE_LEVEL1_AMBUSH.md).
Other level-1 zero-excursion or high-speed type-50 records are accepted but not covered by that oracle.
