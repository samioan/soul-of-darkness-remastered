# Actor core: movement, collision, animation and player actions

This pass maps 69 Actor fields and 75 methods, plus the world-list and collision
accessors they call. It covers construction/common lifecycle, animation and
bounds, common integration, player control and geometry. It does **not** finish
the many enemy/object AI handlers or every branch of the type-specific action
transition switch. Remaining scratch fields deliberately keep their original
names. See `names.map` and `python tools/outline.py Actor` for the exact mapping.

Evidence uses original `b.java` line numbers in `soul/decompiled`, and the original
bytecode exported with `python tools/bytecode.py b`. Tests execute the original
and recompiled reference directly; recovered source is not treated as its own
sole oracle.

## Construction, identity and ownership

`initializeFromRecord` (1314..1848) starts with this short-record prefix:

| Offset in shorts | Meaning |
|---|---|
| 0 | Actor type |
| 1 | UID, assigned on fresh initialization |
| 2, 3 | Pixel X/Y, read as unsigned 16-bit values |
| 4 | Initial animation/module selection |
| 5 | Flags, including initial facing in bit 0 |
| 6 onward | Type/flag-dependent parameters; not yet a fully mapped schema |

Pixel coordinates initialize the fixed-point accumulators with a left shift
by 8. `spriteIdByType` selects the BSprite from Game's sprite table. Animation
selection resets the animation counters. Some records define custom bounds,
health/variant/stat-table parameters or links to other UIDs. `type=0` initializes
the player and fills the global health/magic meters.

The game's object loader reuses a 20-short parameter buffer. Records in its
serialized object stream are length-prefixed and do not necessarily populate
every slot. Do not replace the original buffer behavior with a short array of
exact record length without auditing consumers of the remaining slots. The
controlled tests use a zero-filled 20-short player record.

Respawnable actor records are indexed by UID with a byte offset and short count
into the original object stream. `isRespawnable` is the original type/flag
predicate used by that registration/reset path; it is not a universal property
inferred from an enemy's appearance. Its registry/pool lifecycle still needs
the full level-loading pass.

`spawnActor` creates a transient actor, selects sprite/animation, applies relative
position and facing, records `lastSpawnedActor`, then adds it to Game's actor
array. Game reuses vacant array slots through an index stack. Removal clears
links and removes references from update/effect lists; flag `0x2000` actors are
marked inactive instead of being removed outright. Persistence uses the original
`-98` transient and `-99` removed markers. The save schema is still pending.

`setAttachedActor` clears `0x100` on the previous attachment and sets it on the
new one. `supportActor` / `supportedActor` describe vertical stacking. The global
`playerPlatform` carries the player by the supporting actor's velocity before
normal actor integration. These references are separate from sprite resources.

## Frame order and fixed-point movement

The common `update` method is original `s()` (2222..2581). It first checks
`isUpdatePaused`, which includes screen states 14 (pause) and 32 (crystal reward)
and type-dependent scripted-interaction rules. Flags `0x400` and `0x4000` also
skip the common update body.

For an actor that updates:

1. Synchronize the integer part of fixedX/fixedY with positionX/positionY while
   preserving the stored fractional bits. This absorbs direct collision,
   camera/script and platform corrections from earlier operations.
2. Add velocity to fixed-point position. Actors marked scrollWithWorld also
   receive worldScrollPixelsPerTick shifted left by 8 on the horizontal axis.
3. Add acceleration to velocity, then clear both acceleration fields.
4. Shift fixed position right by 8 to obtain pixel position.
5. Decrease the shared type-specific timer and dispatch the actor's type handler.
6. Rebuild dirty bounds, synchronize facing into flags bit 0, and apply eligible
   camera/region constraints to non-player actors.

Position is integrated **before** acceleration, and control can change velocity
again afterward. Do not replace this with a different integrator. Velocities
and accelerations use 1/256 pixel units per simulation tick; position accumulators
are signed 32-bit integers with eight fractional bits. Negative shifts and
overflow must follow Java integer semantics in the native port.

Ground walking starts at 1280 (5 pixels/tick), increases by 320 (1.25 pixels/tick)
and caps at 2560 (10 pixels/tick). Standard jump velocity is -4608 (-18 pixels/tick),
ordinary gravity is 768 (3 pixels/tick²), short-jump gravity is 1536, and the
player's downward velocity is capped at 4608. These are values from the original
control code, not new tuning parameters. Special modes, water-like interactions,
platforms, attacks and knockback override them.

## Animation and bounds

The animation clock is separate from the actor's action state:

| Original | Reference | Meaning |
|---|---|---|
| f | animationId | Normally the animation; also reused as a direct module index in another draw mode |
| g | pendingAnimationId | Queued selection applied by advanceAnimation |
| h | animationFrame | Frame within animation, or direct frame index in another draw mode |
| i | animationTick | Duration counter; negative values select nonanimated drawing |
| ao | actionState | Type-specific behavioral state, not a BSprite animation number |

`requestAnimation` queues a changed selection if an animation is already active;
the first selection is immediate. `requestAnimationRestart` can queue the same
animation again. `setAnimationImmediate` switches and resets counters directly.
`advanceAnimation` applies a pending selection first; otherwise it increments
the duration counter unless flag `0x40` freezes it. Duration zero holds, and
reaching the final frame wraps to frame zero. `isAnimationEnding` recognizes the
last tick of the final frame, before that wrap.

Crucially, animation advancement is called at the end of Actor.paint, not from
the common update. Offscreen actors can also advance in Game.drawActors.
Actor.paint also mutates invulnerability timers and type-specific effects.
Higher display FPS therefore requires separating all these operations onto the
simulation clock, beyond merely moving Game's update switch out of paint.

`refreshBounds` (680..766) obtains collision rectangle 0, attack rectangle 1,
and default visual bounds from BSprite, then offsets them into world pixels.
An absent collision rectangle falls back to visual bounds. Some actor types use
custom absolute bounds, so they do not all pass through the same offset logic.
See BSPRITE.md for the original stored/computed bounds distinctions.

## Tile sampling and response

The collision grid uses 20×20 pixel cells. `getCollisionTile` is Game's original
signed-byte accessor. Its outside-map response depends on the level mode, and
negative vertical coordinates are handled separately. Do not clamp every lookup
to the nearest tile.

`probeVerticalTiles` (2984..3065) samples the head, one row below the head,
feet+1, rows above/below the feet and both neighboring feet columns. It computes
a vertical correction, grounded state and whether vertical correction should
run. `resolveTileCollisions` (3067..3162) optionally corrects vertical position,
then scans left/right walls along the body and resolves horizontal penetration.
It does not use swept collision or a variable timestep.

Observed code meanings, pending a full per-level tile legend:

| Tile ID | Core response |
|---|---|
| 0..14 | Generally below the supporting-surface threshold; player handler additionally treats 2 as death and 3 as damage |
| 15 | Supporting/one-way surface, distinct from hard-wall threshold |
| 16, 17 | Opposing full-height slopes |
| 18, 19 | Two halves of one shallow slope direction |
| 20, 21 | Two halves of the opposing shallow direction |
| 24 and above | Hard collision threshold in wall/ceiling logic |

For local X `u` in 0..19, the six slope heights subtracted from a flat vertical
correction are `u`, `20-u`, `u/2`, `u/2+10`, `20-u/2`, `10-u/2`, using integer
division. The head/wall probes contain additional exceptions for slopes, wide
actors, fast upward travel and a specific platform type. Preserve these branches.
The core tests check all 20 horizontal offsets for each slope and supporting/
nonsupporting samples (220 probe cases in total).

## Geometry conventions and preserved edge behavior

- `rectsOverlapInclusive` accepts touching edges; `rectsOverlapStrict` rejects
  them. Both reject a rectangle collapsed to a single point, but do not simply
  reject every zero-width or zero-height rectangle.
- `pointInRect` excludes left/top and includes right/bottom. A conventional
  half-open rectangle test would change triggers at boundaries.
- `segmentCrossesRectEdges` tests rectangle edges; a segment fully inside can
  return false because it crosses no edge.
- `intersectRaySegment` allows the first directed line to extend past its second
  supplied point. It differs from the fully bounded `segmentsIntersect` helper.
  Its collinear path can divide by zero; the reference and tests preserve that
  original behavior rather than silently changing gameplay math.
- `raycastTiles` walks grid edges with a bounded iteration count. It is not
  interchangeable with an unrestricted raycast from a desktop physics library.

Math helpers preserve the original signed-int abs/min/max/sign tests and the
nonnegative integer square-root algorithm. Changing overflow or rounding in a
native translation requires an explicit, separately validated decision.

## Player controls and action selection

Player input/control is original `aw()` (3259..3751), with grounded movement in
`ay()/az()/aA()` (3870..4012). It queries the existing latched pressed/held masks;
see INPUT.md for the one-paint edge-latching order. Core animation roles:

| Animation | Role verified from control paths |
|---|---|
| 0, 1 | Idle / walk |
| 2, 3 | Moving / vertical jump |
| 4, 5 | Falling transition / fall |
| 6, 7 | Landing variants |
| 8 | Hit/knockback or a forced control pause |
| 9 | Death; final frame enters Game state 12 |
| 10, 11, 12 | Crouch hold / stand-up / crouch-down transitions |
| 13..15 | Weapon-0 melee family, including aerial attack |
| 19..21 | Weapon-1 melee family, including aerial attack |
| 16, 18, 22, 23 | Special/magic paths with additional game/effect handling |
| 24, 25 | Special movement/environment and reward poses; detailed interaction semantics pending |

`activeWeapon` is independent of animation and actionState. Pressing keypad 9
requests a weapon switch with a 450 ms cooldown; keypad 5/fire selects a melee
action; keypad 0 requests an eligible magic action if sufficient magic remains.
Up/2 and diagonal keypad 1/3 jump through different control branches. Short-jump
behavior also depends on the input hold counter latched by Game.

The player mode (`changePlayerMode`) is distinct from enemy `changeActionState`:
type-specific transitions can set animations, timers, links and spawn effects
before assigning actionState. The full large transition switch and enemy AI are
the next read-through; numeric IDs alone do not establish an enemy's state name.

## Reproducible verification and limits

Default regeneration now runs `ActorCheck.java` in original/reference class
loaders. The test Image adapter supplies sprite data; no Canvas, Graphics or
desktop gameplay runtime is involved. Checks cover geometry, fixed-point
integration including negative/overflow cases and update gates, all 27 player
animation clocks, queued animation changes, tile/slope probes, and 700 scripted
ticks across seven controlled maps/scenarios:

| Scenario | Independent expectation in addition to original/reference equality |
|---|---|
| Walk | Position exceeds X 200; original observed maximum X 697 |
| Jump | Moves upward; observed Y 199 to 118 (81-pixel rise) |
| Crouch | Pose/state sequence compared on a flat floor |
| Melee | Pose/state sequence compared without enemy targets or weapon effect actors |
| Wall | Wall begins at X 180; actor center stops at X 170 |
| Pause | Paused/unpaused core sequence; separate update-gate tests assert motion is prevented |
| Ceiling | Jump rise limited to Y 135 by a ceiling at Y 60..79 |

The traces are written before animation advancement to ignored
`soul/analysis/actor-trace.tsv`. `tools/actor_shots.py` combines those verified
poses with decoded sprites and a synthetic collision-map drawing. Those images
are analysis artifacts, not screenshots of the native port or a real level.

The harness does not run full Game.paint, render-driven timer/effect mutations,
enemy AI, spell/projectile damage, all constructors, real levels, saves or audio.
Its player record is controlled, not a claim that every serialized object format
is understood. The native executable remains the calibration scaffold.
The separate BehaviorCheck now covers selected enemy/object paths; see
ENEMIES_AND_OBJECTS.md for those scenarios and their limitations.
LevelCheck separately constructs actors from all nine shipped streams; see
LEVEL_ACTORS.md. SAVES.md covers selected in-memory persistence paths.

```powershell
python tools/rename_sod.py
python tools/actor_shots.py --scenario jump --output soul/analysis/jump.png
python tools/actor_shots.py --scenario melee --frames 4,5,6,7,8,9,10,11 --output soul/analysis/melee.png
python tools/outline.py Actor --calls updatePlayer
```
