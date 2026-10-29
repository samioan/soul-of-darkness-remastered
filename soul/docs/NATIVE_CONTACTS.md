# Native player side contacts

`actor_contacts.h/.cpp` now ports the player side-contact passes, and
`Player::update` accepts an optional queue of body snapshots. This is a collision
foundation for native enemies and solid objects. **The interactive preview does
not yet construct these enemy/object bodies**, so this pass adds no enemies or
combat to the displayed first level. Existing pickup/chest interactions retain
their separate handlers.

## Verified order and semantics

Original `Actor.resolvePlayerActorContacts` (`b.x()`) refreshes player bounds
once, then visits the update queue twice in reverse order:

- Types 37 and 80 with field O set and flag 1024 clear use correction cap 2560.
- Other bodies with O set, flag 1024 clear and action not 5/6/7 use cap 9.
  The player itself and null queue entries are skipped.

`handlePlayerActorContact` (`b.a(b,int)`) refreshes the target's bounds, requires
inclusive rectangle overlap and player feet strictly below its top, then uses
prior grounded/wall/contact flags, relative side and velocity signs to decide
which body moves. Field P prevents pushing the target. Field Q records contact;
R/S record contacts on the player's left/right. These descriptions apply to the
inspected contact paths; the original fields remain unnamed in generated Java.

Preserved details include point-rectangle rejection, zero-width line acceptance,
Java integer overflow, signed velocity shifts, the asymmetric positive-player-
velocity test on both sides and stale player bounds throughout each pass.
Visibility flag 128 alone does not disable contacts. A priority gate can still
resolve in action 5, while an ordinary body in that state is skipped.

The nine-pixel cap is not a universal solid-wall constraint. A player moving ten
pixels per tick can cross a stationary controlled body; the original does this
too. Full enemy movement, response to field Q and damage are separate work.

The player branch of `resolveActorSeparation` (`b.ai()`) runs after input. It
visits N-enabled bodies in reverse order, excluding the current support platform,
type 82 and O-enabled bodies. This later pass uses full overlap correction and
its own filters; it does not independently test flag 1024 or action state.
The native player then clears its R/S contact markers as the original does.

`Player::update` calls the early pass after integration and before tile response;
the late pass follows movement selection. Corrections change pixel positions
without eagerly changing fixed-point coordinates. The following tick absorbs
the correction while preserving fractional bits. A paused player skips both
passes. Animation, collision and contact effects remain on the 83 ms tick.

## Ownership and bounds

`ContactBody` is a per-tick snapshot: type/action, pixel position, horizontal
velocity, flags, bounds and contact state. Actor owners must build the queue in
the original update order, refresh animation-relative bounds for the current
pose and copy corrected positions/contact markers back before their next
handler. Queues must not outlive their body owners. `Player::update` takes other
actors' bodies; it creates its own player snapshot internally.

`contactBounds` applies sprite frame rectangles, visual fallback, animation
offsets and flips. Absolute custom bounds are also supported in the snapshot.
The snapshot retains the original deferred bounds refresh after a position
change. It does not replace actor integration, activation queues or collision
tile storage.

Type-14 contact poses 2/3 use `pushActor`, tile probes and support/attachment
links. That branch explicitly fails if contacted; it is not approximated by
the ordinary side-contact solver. Standing on actors and moving-platform
carrying, non-player actor separation and general support-link ownership remain
unported. The native movement prototype has no platform link to pass to the
late resolver yet.

## Validation

`ContactCheck.java` uses the unmodified original classes and the generated
reference with the existing ImageIO test adapter. Original bytecode for `x`,
`a(b,int)` and `ai` was inspected with `python tools/bytecode.py b`.
No member renames or generated-source patches are required.

Default regeneration writes ignored `soul/analysis/contact-oracle.tsv`:

- 628 collision-bound rows cover every pose/frame and all four flips of sprites
  `2:0`, `2:2` and `2:3` (player and the first two enemy sprite resources).
- 7,056 controlled arenas cover both sides/equal X, velocity signs, grounded
  states, wall flags, push locks, prior R/S flags, type/action/inactive filters,
  null/self queue slots, two-body ordering and priority gates/platform bodies.
  Both passes emit three actor snapshots each: 42,336 rows.
- Six player scenarios add 768 before/after-animation snapshots over 384 ticks:
  locked and pushable bodies on each side, pause and a jump. These run original
  player updates against controlled bodies, not enemy AI or a real play-through.

The Java checker performs 1,185,172 original/reference comparisons. The native
checker matches all 43,732 rows. Twenty-one data-free contact assertions verify
independent outcomes and unsupported boundaries; three new player assertions
cover pause and deferred fixed-coordinate synchronization. The combined player
executable now has 82 assertions; CTest has 24 tests per configuration.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_contacts.py --exe soul/port/build/Debug/soul_assets.exe
```

Debug/Release also retain the movement, pickup, chest, reward and collection
effect comparisons. Existing host runs still cover 240/569 logical widths and
independent FPS/interpolation, but their world contains no new contact bodies.
They establish regressions for existing behavior, not live enemy-contact
coverage. [Weapon-0 melee](NATIVE_MELEE.md) now adds attack control and slash
presentation. [Type-1 AI](NATIVE_ENEMIES.md) now has a controlled native slice.
Next: damage/reactions, then live queue ownership, enemy response and hit timing.

The following [live combat pass](NATIVE_LIVE_COMBAT.md) now connects seventeen
ordinary enemy bodies to the preview using previous-tick contact queues. Its
original-world-update oracle adds reciprocal combat and two-enemy order; full
platform/support and remaining world queues are still pending.

[Platform landing and support](NATIVE_PLATFORMS.md) now adds the player support
link and controlled type-33/50 contact transitions. Moving-platform handlers,
live surface loading, type-14 pushing and nonplayer support chains remain pending.
