# Native ice magic and wall footholds

K/0 now casts ice when the ice blade is equipped and magic is unlocked. The
shot damages ordinary ground/flying enemies or freezes against a wall into a
temporary foothold that Kale can jump onto. Recasting melts the previous
foothold and releases its rider. The shipped ice pickup remains in level 2;
normal first-level placements have not gained a reward or an automatic unlock.
The complete game remains unfinished.

## Original behavior and implementation

`Player` ports the grounded mask-1 branch into charge pose 23 and release pose
22. Charge processing preserves the original four 80-unit increments and
twenty random draws per increment, then spends 15 MP on the fifth call. This
resets the shared magic regeneration timer. Holding the key does not invent
new press edges. Locked, crouched, airborne and insufficient-MP inputs retain
their original gates. Physical attack still has priority over magic and switching.

`IceMagic` owns stable-address projectiles and the two original `aR` slot
references. Birth uses sprite 1/animation 7, horizontal velocity +/-2560 and
the facing-dependent player offset. Original `Actor.bG/updateEffect` cases
7/8/9/10 establish flight, freezing, standable ice and melting. The foothold
starts with a 10000 ms timer; melting uses 1000 ms and waits for animation
completion. Expiry, loss of its wall, replacement and region cleanup release
player support. Ordinary enemy impact changes to animation 1, stops horizontal
motion and applies the rank-based 15/20/25 damage through existing enemy damage
and effect handlers.

Wall and six slope probes preserve the original asymmetric corrections,
deferred fixed coordinates and animation/bounds refresh order. Bytecode dumps
from `python tools/bytecode.py b` and `e` confirm the timer values, `aR[0]`
primary test and `aK` player-platform reference. One unusual branch removes an
older shot on wall impact but still runs its tile collision update that tick;
the native isolated probe preserves those resulting bounds.

`updatePlatformContact` now accepts type 39 only in standable animation 9.
Landing and continuing support use the original top+1 position. Drawing uses
the existing effect sprite and foreground layer, with a blue charge ring and
an English equipped-magic hint. Cast progression, random draws, hits and
support remain on 83 ms simulation ticks. The existing 240-wide reference
view controls projectile lifetime; presentation can reveal a 569-wide world
without changing simulation or collision.

## Validation

Default regeneration runs both new original/reference Java checks:

- `IceProjectileCheck`: nineteen 180-tick arenas, 6842 expectations and 6840
  native rows. Flight/facing, wall and all six slopes, old-shot wall impact,
  pause/hidden/frozen flags, wall loss, landing/walk-off/jump and view expiry.
- `IceMagicCheck`: twenty-one 180-tick world arenas, 7587 expectations and
  10762 native rows. Player/MP/charge, both slots, ordinary ground/flying
  enemy hits, rank, input gates, random stream, freezing/expiry, jumping,
  wall removal and replacement while riding. The isolated drawing adapter
  suppresses named graphics calls; it is not a MIDP runtime.

Totals: 14429 original/reference expectations and 17602 native comparisons.
The rows include snapshots before and after animation processing. They cover
these controlled branches, not full gameplay equivalence.

```
python tools/check_native_ice_projectiles.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

Seven timed host milestones and twenty-four runs cover direct gameplay and
startup/pause/resume at widths 240/569, 30/60/144 FPS, interpolation on/off.
`--ice-projectile-script` and `--game-ice-projectile-script` are explicit
headless fixtures: they relocate/unlock the original pack-4 ice chest beside
an invulnerable player with magic owned and MP=60. Normal chest input,
collection, tutorial and weapon switching grant/equip ice. Normal spell input
kills UID 19, walks back to the real left wall, freezes it, jumps onto its
foothold at Y=207, stands for 29 supported ticks and recasts. Melting detaches
the player, both footholds expire, and Kale returns to floor Y=259 with MP
recovered. Enemy death, support acquisition and rider detachment are not forced.
Unrelated arena/chest/gate progression stays unchanged by this fixture.

Eleven new data-free assertions cover spending/birth, slot replacement,
tick-time detachment, cleanup/destruction, insufficient MP and invalid assets.
Player checks now total 408; three CLI guards bring CTest to 75 tests.
Ignored game captures at both widths show the English equipped-ice HUD and
Kale standing on the wall foothold.

Validation on 2026-10-29: full default Java regeneration passed. Debug and
Release passed all 75 CTests, both ice checkers and existing fire/melee/player,
combat/flying-enemy, chest/ability/arena, world-order/region, platform/bridge,
gate/script and passage regressions. The startup/English-story/HUD/pause checker
also passed. The rebuilt data-free package matches Release and passes all
17602 projectile comparison rows and 24 host chains against regenerated oracles.

## Remaining boundaries

NATIVE_ICE_ATTACHMENTS.md now covers type-33 links, world update ordering and
cleanup. Its natural fixtures establish that the shipped shot's point attack
rectangle prevents attachment; linked branch fixtures use explicitly synthetic
rectangles. The weapon-effect dispatch has no direct type-50 chandelier
attachment branch. Other gate/trap targets need their own controllers.
General mixed-spell/actor queue ordering and selective modal updates remain
bounded by their existing fixtures. Rank is verified in probes; there is no
native upgrade/save backend yet. Next: remaining first-level progression,
checkpoint/restart ownership and level completion/loading. Full level-2 gameplay
is not established by relocating its chest.
