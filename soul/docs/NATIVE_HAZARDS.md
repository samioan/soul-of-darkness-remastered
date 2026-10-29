# Native tile hazards and buffered Windows presentation

The live combat owner now supplies player damage, meters, RNG and effect events
at the tile-dispatch boundary. Type-2 tiles select death pose 9 immediately;
health becomes zero at death frame 1, tick 0. Type-3 tiles call the ordinary
one-point hit path with the player as its own source, including invulnerability,
blood and knockback. Type 2 takes precedence when both occur. These decisions
run after tile resolution and before pose logic on the original 83 ms tick.
Death finishes at the existing preview defeat/restart boundary.

`Actor.aw()` original bytecode offsets 324–386 confirm feet/head/above-feet
tests, the priority, and `a(4,1,0,this)` for type 3. Its pose-9 branch confirms
the deferred health write. No generated Java or bytecode is changed.
An isolated `Player.update` without combat ownership still stops explicitly at
hazards; `CombatWorld.updatePlayer` supplies ownership for the displayed preview.

`LiveCombatCheck` adds six controlled arenas: lethal tile at above-feet or head,
mixed lethal/damage tiles, ordinary tile damage, initial invulnerability and a
one-health lethal hit. All run original `Game.updateWorldActors` with the same
enemy registry and explicit bounds/animation paint boundary as the combat pass.
The combined thirteen scenarios cover 1,052 ticks and 4,448 snapshots, with
99,745 original/reference comparisons. They compare health, invulnerability,
state and full actor snapshots with native output. Twelve additional birth-effect
rows verify that player-spawned blood runs its first handler in the same tick,
before its first animation clock. Six additional data-free
assertions bring the combined player executable to 160 assertions.

Level 0 contains no type-2 or type-3 collision cells. The displayed first-level
preview is connected, but hazard checks use controlled maps; this does not claim
a play-through of later levels, object traps or platform support.

## Black flashing fix

Previously each `WM_PAINT` filled the visible client black, then drew the scaled
game canvas, then desktop text. Those visible intermediate operations could
produce continuous black flashes independently of interpolation. `WindowBuffer`
now owns a reusable compatible GDI bitmap/DC, recreated when client size changes.
The canvas, letterbox bars and overlays compose there before one visible BitBlt.
Empty/minimized clients skip painting. Background erase stays suppressed.
Interpolation still affects only the calibration marker; gameplay timing is unchanged.

The data-free `buffered_window_presentation` CTest exercises 90 actual GDI paints
at logical widths 240/569, repeated frames and resize. It verifies offscreen
composition leaves the target untouched, then presents canvas/bars/overlay
together, and handles an empty client followed by restoration. This brings the
suite to 32 tests. This is automated GDI validation, not a measurement of tearing
or a guarantee about every display/driver. Debug, Release and dist executables
are rebuilt with the fix.

Next: remaining object contacts/platforms and fuller region/world ownership,
followed by original HUD/menu/save/audio integration.
