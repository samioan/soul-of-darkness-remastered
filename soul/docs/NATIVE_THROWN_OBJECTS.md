# Native type-16 thrown objects

The native world now owns the type-16 objects required by type-3 throwing
enemies. Held creation, release, bouncing, resting, reflection, explosion and
cleanup run on simulation ticks. This is the projectile foundation: type-3 AI,
its linked chests and its exact birth/update ownership are still pending.
Level 1 remains unavailable through the frontend.

## Original evidence

`Actor.updateEnemyType3` creates sprite-4/type-16 actors through `Actor.m(II)`,
attaches them with flag 256, and releases them through `Actor.n(II)`.
`Actor.bH` supplies the thrown-object handler, `bJ` supplies bounce physics and
`bK` supplies reflected/exploding target contacts. Action entries in `Actor.n(I)`
select animations/timers; `Actor.B(I)` finds a remaining holder through
`Game.d(Actor)`, sets its scratch cooldown to 1000 and clears its attachment
before removal. Original bytecode was checked with tools/bytecode.py b and e.
No reference-Java names or patches changed in this pass.

The held child has priority 101 and uses the holder's facing with offset
`(+/-5, -31)`. Its initial fixed coordinates and pre-placement bounds are retained
until the original update/animation phases synchronize them. These details are
covered by the reference snapshots rather than normalized at construction.

## States and contacts

- State 101 is held. Release enters 102, requests animation 1, loads an 800 ms
  timer, installs velocity and clears the attachment flag/link.
- State 102 applies drag, delayed gravity, wall/ceiling bounce and floor/slope
  settling. Player-body contact starts explosion; after the timer expires,
  overlap with the player's attack rectangle reflects the object.
- State 103 is resting, with animation 2 and a 3000 ms timer. Expiry or player
  contact starts explosion; an attack can reflect it immediately.
- State 104 flies back from the player with horizontal speed 3328 and vertical
  velocity -3328. Solid-tile impact starts explosion. Eligible queued enemies
  receive 500 damage; animation-0 bridges have their collision cells cleared
  and enter action 100 without resetting their existing impact/timer fields.
- State 105 immediately selects animation 3 and stops motion. Before its ending,
  overlap can deal 15 player damage and still contact actor targets. At the
  ending it removes itself and notifies a holder that still owns it.

The map lookup preserves level modes and the stored collision-grid boundary;
it does not pad the pack-9 mismatch. All six slope types, early/eligible sword
contacts, player hits, frozen/hidden targets and the paired-contact explosion
predicate have controlled checks. That predicate is not a native paired-player
mode implementation.

## Ownership and world integration

`ThrownObjects` owns stable list nodes and borrows selected sprite/maps.
`ThrowAttachment` supplies a holder identity and a borrowed cooldown field.
Release, replacement and holder destruction clear reciprocal links. Normal
held-object expiry writes cooldown 1000; region/unload cleanup detaches without
that gameplay notification. A holder's cooldown must outlive its attachment.
The collection destroys nodes only after clearing their holder references.

The native world loads sprite 4 through its selected descriptor, updates object
contacts against its frozen actor queue, forwards player damage/effects and
counts enemy kills. Existing fire pulses can reflect resting or expired flight
objects; the original external effect may do so even when the object is frozen
or the pulse is hidden. Ice effects do not take that fire-only path. Region
changes discard dynamic type-16 actors; unload clears them before borrowed
assets and enemy owners are destroyed. Rendering reads tick-owned animation.

The collection currently runs after placed-actor handlers and before fire/ice
updates. Full original mixed actor birth/pool order, parent-child double dispatch
and painter eligibility are still unfinished. Type-3 AI must supply its held
birth/release/positioning and linked-container paths before the encounter can be
enabled. Later destructible type-79 targets and full original RMS/pool state
remain outside the current native world.

## Checks

`ThrownObjectCheck` compares original bytecode and regenerated Java using the
existing ImageIO analysis adapter. **6507 rows** cover 27 scenarios over 120
ticks, with snapshots after both handler and animation phases and a final RNG
comparison. They cover held/released objects, left/right flight, walls, ceiling,
landing, six slopes, reflection timing, explosion, holder cooldown, pause/freeze/
hidden flags, mode-2/3 map behavior, player damage, enemy damage and bridges.
Type-3 AI is not executed in these fixtures.

Native checks add fifteen data-free assertions for physics, pause, holder
replacement/destruction, cleanup, immutable rendering, wide visibility, invalid
maps, world damage dispatch, external fire reflection and region ownership.
Twelve controlled selected-level world fixtures compare widths 240/569 and
30/60/144 FPS with frequent versus sparse rendering. Each completes a held birth,
release and removal in 100 ticks; simulation, rendering and RNG remain stable.
The current suite contains 107 CTests, alongside 530 player, 25 timed-switch,
17 barrier and 15 thrown-object assertions.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_thrown_objects.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
```

Validation on 2026-10-30: default regeneration exited 0, compiled with zero
errors and passed ReferenceCheck's 4218 comparisons plus every registered check,
including the 6507 new rows. Debug and Release passed all 107 CTests and the
new native manifest. The packaged build matched the same oracle and twelve
world fixtures. Existing selected-level reconstruction, checkpoint/Restart,
finish/Continue/replay, bridge and intro regressions passed, including their
240/569 and FPS matrices. The rebuilt package contains no game data.

Oracles, logs, captures, assets and binaries remain ignored.
