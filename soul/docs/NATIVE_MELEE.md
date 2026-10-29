# Native weapon-0 melee

The first-level movement preview now supports standing weapon-0 strikes,
the second combo strike, aerial attacks and the original attached slash sprite.
Press E/J/Enter to attack; press again during the first ground strike to queue
the second. The same input still opens nearby ordinary chests. Walking, jumping,
crouching and crystal rewards remain available.

The following [live combat pass](NATIVE_LIVE_COMBAT.md) now connects ordinary
enemy AI, damage, hit response, loot and a native defeat boundary.
This pass establishes attack control, presentation and hit queries for that
integration. The live pass now supplies ordinary enemy bodies and damage dispatch.
Later passes now supply fire magic, ice physical attacks, Q/9 switching and a
desktop HUD. Sound and ice projectiles remain unported; see NATIVE_FIRE_MAGIC.md,
NATIVE_ICE_WEAPONS.md and NATIVE_STARTUP.md.

## Original behavior

The port follows the weapon-0 branches of `Actor.updatePlayer` (`b.aw()`):

- Animation 13 is the first standing strike, 14 the queued second strike and
  15 the aerial strike. Fire stops horizontal/vertical velocity on entry.
- `aj()` and the `aU` table allow a second pressed edge during ground pose 13
  to queue 14. The transition checks the final animation frame, not merely
  the final tick. Holding fire does not repeatedly queue attacks.
- The original `db` recovery counter gates new attacks for three ticks after
  a normal ground strike, or eight while the current animation is fall 4/5.
  It starts at zero in the controlled fresh-player fixtures. Combo transitions
  bypass that recovery by restoring -1.
- The aerial strike stops movement until its ending, then returns through
  falling/landing. Midair steering excludes active melee poses. The original
  same-tick control order, queued animation and delayed acceleration remain.
- Crouched attacks require standing headroom. Tests start the low-ceiling case
  crouched beneath the ceiling; they do not begin inside a solid tile.

`Player::meleeHits` ports the weapon-0 `Actor.b(int,int[])` predicate: pose
13..15, inclusive attack-rectangle overlap and point-rectangle rejection.
It queries simulation bounds; it does not recalculate them from presentation
frames or apply damage to any actor.

The original type-28 record in level 0 is UID 191, position 407/308, animation
0 and flags 16. Its constructor adds hidden flag 128 and priority 102.
`Actor.a(int,int,int)` activates sprite `2:25`, animation `playerPose-13`, at
player frame 0/tick 0. It resets the weapon clock and clears flags 128/1024.
`followPlayer` updates pixel position/facing after common integration. The
slash hides at its ending; it is not restarted every presentation frame.
`MeleeWeapon` preserves that lifecycle, including its initial record position,
deferred fixed coordinates, hidden/inactive clock gates and independent sprite
animation. Crystal display entry hides it as the original pickup path does.

All state changes and animation clocks use the original 83 ms tick. Drawing
the player or slash changes no state. Existing world/player interpolation is
still pending; the interpolation option continues to affect the calibration
marker only.

## Validation

Original bytecode for the player melee branches, combo helper, weapon activation,
type-28 dispatch and follower was inspected with `python tools/bytecode.py b`.
No Java renames, generated-source edits or decompiler patches were required.

Default regeneration now runs `MeleeCheck.java` against unmodified original
classes and the generated reference, with the existing ImageIO test adapter.
It writes ignored `soul/analysis/melee-oracle.tsv`: **1,920 snapshots over 960
ticks**, before and after animation advancement. Twelve fixtures cover standing,
combo timing, late input, holding fire, walking, crouching, low headroom, jumping,
air steering, falling, pause and left-facing attacks. Snapshots include full
player core state/bounds, queued combo, recovery, melee timer, weapon lifecycle
and eight attack-target queries. Independent assertions require a visible
standing slash, second combo pose and aerial attack, and reject a blocked
standing attack. The checker performs **86,404 original/reference comparisons**.

`check_native_melee.py` matches every native snapshot, then optionally checks
twelve real-level host runs at widths 240/569, 30/60/144 FPS and both interpolation
settings. `--melee-script` begins at normal spawn, performs exactly three attack
entries (poses 13/14/15), lands and returns to idle. Presentation checks compare
final state and per-width pixels; they are not a phone-framebuffer oracle.
The ignored `melee-569.png` capture additionally shows the original slash sprite
in the first-level scene.

Twelve new data-free assertions cover attack entry/recovery, combo transitions,
weapon activation/hiding, pure drawing, hit-query edges and unsupported sprites.
The combined player executable now has **94 assertions**, alongside 21 contact
assertions. Three argument checks bring the suite to **27 CTests**. Debug and
Release also retain movement, side-contact, pickup, chest, reward and collection
effect comparisons. The Release package remains data-free.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_melee.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --movement-preview --data soul/extracted --width 569
.\soul\port\build\Debug\soul_port.exe --movement-preview --melee-script --data soul/extracted --headless --width 569 --fps 60 --frames 300
```

Next: construct the first native type-1 enemy, retain queue/body ownership and
integrate AI, field-Q contact response, damage rolls, invulnerability/reaction
timing and death/loot. Static attack rectangles alone do not complete combat.
The current oracle includes the type-28 actor but does not run enemy handlers,
full Game activation/camera queues, RMS, audio or a level play-through.

The following [type-1 AI pass](NATIVE_ENEMIES.md) now verifies activation,
pursuit and attack/recovery states separately. The displayed movement preview
still needs damage/reactions and live enemy/body queue integration.
