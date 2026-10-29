# Native first-level beam emitters

All six type-10 emitters in level 0 now load, render and damage Kale. The first
three cycle vertical beams; the later three sweep rotating beams. Activation
uses the shipped custom player-overlap rectangles. These are gameplay hazards,
not enemy bodies or targets for the player's weapons.

| UID | Position | Behavior | Stat set |
|---|---|---|---|
| 53 | 167,1435 | stationary down | 8 |
| 66 | 251,1435 | stationary down | 0 |
| 67 | 330,1435 | stationary down | 8 |
| 68 | 508,1612 | rotating | 13 |
| 69 | 650,1612 | rotating | 1 |
| 71 | 788,1612 | rotating | 13 |

## Original evidence and native ownership

Original `Actor.aL/updateBeamEmitter`, its type-10 action-entry switch and stat
row 9 establish activation, warning/charge, active/recovery and rotating hold
states. Original bytecode confirms the handler and ray helper branches. No
Java rename or decompiler patch is introduced in this pass.

Stationary variants traverse 101..106; the initial delay, active duration and
rest come from the four-int stat stride. Action 103 charges for 500 ms. A zero
rest value makes stationary action 104 permanent. Rotating variants traverse
102/103 then 107..110, using the original packed trigonometry table, integer
rotation and degree indexing. Animation requests remain deferred. All simulation
and animation clocks advance on the original 83 ms ticks.

The ray helper is deliberately not a finite-segment clipper: it intersects tile
boundaries beyond the supplied aim point, stopping on blocking collision tiles
or after the original 57 traversal attempts. Tile 15 passes; the initial cell
only blocks at values >=24, whereas subsequent cells block at values >=16.
Horizontal boundaries are tried before vertical ones at each step. The endpoint
is retained during stationary/hold states and regenerated during sweep movement.
The player test checks rectangle edges, so a segment entirely contained inside
a rectangle does not count as crossing it. These quirks must not be replaced
with a generic finite ray or rectangle-overlap helper.

`BeamEmitter` owns the bounded controller and copies its trig/stat values while
borrowing stable sprites and the shared collision map. `CombatWorld` loads it
explicitly, places it in the frozen record-ordered queue and uses the canonical
240-wide camera buckets for simulation. Restart/Continue rebuild the emitters
from their placements, like other nonpersistent controllers. Rendering uses the
actual desktop viewport/clip at widths 240/569 and never changes simulation.

Beam hit kind 4 retains the existing scaled damage/invulnerability/RNG rules,
hurt pose and wall-aware horizontal knockback. Stationary horizontal sources
apply the original +/-15 facing offset. Permanent emitters with direction >=4
can push during damage immunity without taking HP or consuming blood RNG.
Defeated players ignore this push. All shipped emitters deal 10 base damage.

The emitter sprite and three red/white/red software lines draw behind the
player. The sprite-1 animation-13 endpoint glow draws with foreground effects.
The cap is owned by its emitter rather than inserted into a full Java-style
actor registry; its independent offscreen animation/painter ordering is not
claimed equivalent. It survives region cleanup while active, matching animation
13's existing cleanup exception. Native lines use Bresenham rasterization;
original MIDP Graphics pixels have no oracle here. Audio, other level modes,
shield/QTE interactions and the complete mixed actor painter remain unfinished.

## Verification

`BeamCheck.java` is part of default regeneration's isolated lifecycle harness
and uses the existing ImageIO/RMS test adapters. It executes original/reference
handlers, not Graphics calls. Six shipped placements, four controlled direction
variants, a synthetic permanent beam and a paused timeline run 360 ticks each,
with snapshots before/after animation. The player overlap is explicitly seeded
for activation then moved outside the beam in these controller arenas. These
produce 8640 native-comparable controller/endpoint/animation rows.

A separate 192-case matrix invokes original beam damage directly, varying
direction, stationary/rotating behavior, permanent/cyclic rest, idle/hurt/death
pose, immunity and walls. It compares health, pose, facing, knockback, spawned
blood and the next random value. Together there are 8832 native rows and 8844
original/reference expectations. This is controlled subset coverage, not a
complete original game-loop or level-playthrough comparison.

```
python tools/check_native_beams.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

Seven milestones and 24 actual-map host runs cover widths 240/569, 30/60/144 FPS
and interpolation on/off. Explicit headless `--beam-script` starts below UID 53;
normal collision and input take one beam hit, then move along the lower route.
`--game-beam-script` starts beside the sweeping group after normal startup and
uses ordinary movement; six real beam hits exhaust HP and run player defeat.
Starting positions and input are controlled; no hit, damage, beam activation or
terminal state is injected. Simulation diagnostics agree across display settings;
render CRCs agree within each width. The fixtures do not record a route from spawn.

Nine data-free geometry/push assertions bring player checks to 439; three CLI
guards bring CTest to 83 tests. Next: the remaining first-level tutorial/camera/
script dependencies and level completion/loading. The full game is unfinished.


Validation on 2026-10-29: default regeneration passed compilation with zero
errors, ReferenceCheck's 4218 comparisons and all existing original/reference
fixtures. Debug and Release passed all 83 CTests, the seven beam milestones and
24 beam host runs. Debug and the packaged asset tool matched all 8832 regenerated
beam rows. Existing Release combat, world and progression regression groups,
checkpoint/restart and startup/English story/HUD/pause checks passed. Desktop
save/Continue checks passed in both builds. Captures of stationary hits and
sweeping beams were visually inspected at portrait/widescreen widths. The
rebuilt data-free package matches Release and contains no assets or saves.
