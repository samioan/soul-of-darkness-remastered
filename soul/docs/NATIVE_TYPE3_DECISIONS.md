# Native type-3 decision foundation

`enemy_type3_ai.{h,cpp}` implements the decision layer for the three shipped
type-3 enemy behaviors: explosive charger (variant 0), thrower (variant 1),
and linked-chest runner (variant 2). `EnemyType3` now owns these decisions inside
`CombatWorld` for levels 1 and 2; see [NATIVE_TYPE3_ACTORS.md](NATIVE_TYPE3_ACTORS.md).
Normal gameplay still constructs level 0 only.

## Original evidence and behavior

`Actor.updateEnemyType3` (original `b.aG()V`) selects actions 0/1/2/3 and
101-109. The type-3 branch of `changeActionStateInternal` supplies the action
entries. The original six-int `b.a(IIIIII)I` helper computes throw speed. Its
bytecode subtracts coordinates as 32-bit integers before widening to `long`,
then uses integer square root and truncating division. Native aiming preserves
that wraparound and specializes the helper to type 3's launch speed 3584 and
gravity -512. It does not use floating point or frame duration.

The decision state contains action, timer, stored throw speed, motion and
facing. Inputs supply positions, variant, saved runner direction, animation
ending, grounding/wall flags, activation/reach overlap and attachment kind.
One call advances decision timers by the original 83 ms. The owner must call
it only during an admitted simulation tick, after its ordinary integration.

Action entries and decisions retain these details:

- The charger pursues at 512 fixed units, winds up for 700 ms, charges at 2048
  for 1000 ms, then rests for 500 ms.
- Aiming accepts speeds from 1024 through 2304 inclusive. Beyond that range
  the thrower approaches; below it the thrower retreats and spends its 1500 ms
  timer. Timeout selects a facing-dependent minimum speed. A zero solution
  leaves the current motion and timer intact.
- Entering aiming preserves velocity. The release waits for animation ending,
  then requests the stored horizontal speed and vertical speed -3584. Recovery
  lasts 1800 ms and cannot replace an attached object.
- The runner stops at a wall, turns its visible facing back, and waits in 109.
  It does not toggle its saved direction or automatically resume running.
- Hurt entry restarts animation 13 and recoils. Death entry requests an effect;
  its next decision requests chest/held-shot release and owner reset.

Events are commands for the future actor owner: request/restart an animation,
spawn/release a shot, drop a chest, create a death effect or reset after death.
They do not themselves allocate actors, advance children or mutate containers.
Animation requests must retain the original same-animation/pending semantics.

## Verification

`Type3DecisionCheck.java` runs isolated original and regenerated Java loaders
with the existing test ImageIO adapter. Default regeneration runs this check.
Its ignored oracle has 10922 rows:

- 1456 ballistic cases, including signed coordinate wraparound and unreachable
  targets;
- 1690 action-entry cases covering every pair of supported actions, both saved
  directions and five player offsets; these also compare motion, animation
  requests, flags and death-effect creation;
- 7776 single-decision cases across the three variants, supported non-death
  actions, both directions, nine player distances, 83/84 ms timer boundaries,
  and six activation/attachment/animation/wall/reach arrangements.

The decision cases invoke the actual original handler on a controlled flat
floor, with body/weapon contacts separated. They compare decision fields and
child release state; they do not compare movement integration or full worlds.
The native asset-tool manifest independently repeats the inputs:

```
python tools/check_native_type3_decisions.py --exe soul/port/build/Debug/soul_assets.exe
```

A data-free CTest adds 26 assertions for threshold acceptance, animation-gated
release, charge/recovery timelines, replacement guards, runner stopping and
death commands. These command tests do not prove native chest-drop lifecycle.

Validation on 2026-10-30: default regeneration completed every registered check,
including compilation, ReferenceCheck's 4218 comparisons and the 10922 new rows.
Debug and Release passed all 108 CTests and matched the decision oracle. Existing
thrown-object, barrier and selected-level checks passed, including their
240/569 and presentation-FPS fixtures. The rebuilt distribution passed its
package check and contains no game data.

## Actor integration

Construction with ground snapping, tile/body movement, gravity, child positioning,
damage/contact, death/loot, respawn fade, region reset, linked chests and the original
parent/child update queue are implemented and verified in
[NATIVE_TYPE3_ACTORS.md](NATIVE_TYPE3_ACTORS.md). `Type3DecisionEvents` gained the spawn
facing and signed spawn offset there; the decision oracle rows above are unchanged.
This oracle still proves isolated decisions only. Level-3 paired mode, non-player
separation and palette timers remain unported.
