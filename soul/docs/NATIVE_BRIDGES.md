# Native breakable bridges

Both level-0 type-45 bridges are now loaded by the gameplay preview, with their
original sprite-36 segments, solid collision and collapse sequence. A falling
type-50 chandelier can break either bridge. The full game remains unfinished.

## Original evidence and behavior

The shipped records are UID 42 at (960,1100), width 200, and UID 1 at
(1560,720), width 160. Both use animation 0, flag 8 and a 20-pixel-high custom
rectangle. Actor initialization selects sprite 36, draw priority 2 and activation
radius 4. The bridge collision bounds come from the custom rectangle rather
than the sprite's per-segment bounds.

`Actor.bf` (`updateBreakableBridge`) increments the idle timer by 83 ms, wrapping
at 1660 ms, and writes solid tile 24 every eligible idle tick. State 100 advances
the collapse timer: animation 0 reaches state 101 at 498 ms (six ticks), while
animation 1 takes 1660 ms (twenty ticks). State 101 removes the actor on the next
eligible update. Animation clocks advance separately, including on the final
state-101 frame, but the custom draw path hides that state.

`Actor.a(int[],int)` / `fillCollisionTiles` calculates inclusive cell ranges
from `(left+10)/20`, `(right-10)/20`, `(top+10)/20` and `(bottom-10)/20`.
Java division truncates toward zero. Writes outside the grid are ignored by
`Game.c(int,int,int)` / `setCollisionTile`. `setCollisionRectEmpty` selects 0
or 24; it does not alter either visual tile layer.

In type-50 falling state 102, the original update queue is scanned in order.
An idle bridge can be hit across its full width and from 17 pixels above its
top through its bottom. The impact stores the chandelier's X, clears the bridge
collision immediately and enters state 100, before the chandelier resolves its
own tile contact. A bridge or damageable-enemy hit ends this target scan. Native
enemy/bridge targets retain their relative record order. The impact does not
stop the chandelier's horizontal velocity; the enemy impact branch does.

The custom draw path repeats 40-pixel segments. Collapse phase is
`(timer/83)>>1`, offset by each segment's distance from the impact. Later
phases move debris downward and cycle frames 2..4. The original draws both the
intact segment and the current debris phase when the distance exceeds the
current phase; this behavior is preserved. Drawing does not advance clocks.

The handler, fill loop, type-50 bridge branch and custom draw arithmetic were
checked against original `b` bytecode using `tools/bytecode.py`. No generated
Java changes or additional semantic renames were needed.

## Native ownership and validation

`BreakableBridge` borrows immutable sprite data and the same mutable collision
map read by the player, enemies and platforms. `TileScene::mutableMaps` exposes
that map to the gameplay owner; visual layers remain unchanged. Collision edits
require equal stored/logical dimensions, so pack 9's known mismatch is explicitly
outside this mutable-map policy. The native bridge owner currently accepts
flag-8 rectangular records, animations 0/1 and complete 40-pixel segments.

`CombatWorld` retains bridges in stable list slots and includes their eligible
updates and chandelier targets in record order. The original type-45 update
distance is less than four buckets; animation visibility uses the reference
240-pixel camera while drawing can reveal more world at wider widths. Full
render queues and camera/region ordering are still unfinished.

`BridgeCheck.java` runs eleven original/reference scenarios: idle, early/late
activation, paused activation, long animation-1 collapse, falling impact with
either bridge/chandelier update order, long falling collapse, miss, inclusive
edge and hidden bridge. Sixty ticks per case produce 2040 native-matching rows
of bridge state, whole collision-grid CRC and selected complete chandelier
snapshots. All 2051 original/reference expectations pass. These are controlled
fixtures, not a full level or phone rendering oracle.

`--bridge-script` is a headless fixture that releases actual level-0 chandelier
UID 29 directly into falling state beside bridge UID 42. It verifies that one
of the two bridges disappears after the impact. This is an explicit release
fixture rather than a recorded chain-cut input. The existing weapon-0 chain-cut
path remains covered by the swinging-platform oracle. Host runs compare final
state across widths 240/569, 30/60/144 FPS and interpolation on/off, and pixels
per width. Intact and collapsing real-level screenshots were visually inspected;
original bridge pixel equivalence is not established by those screenshots.

Twelve new data-free assertions cover collision write timing, pause, exact
rectangle cells, pure drawing, the contact strip and inclusive edge, repeat
impact rejection, collapse/removal timing, clipped writes, dimension mismatch
and partial-segment rejection. Combined player checks reach 200 assertions;
three CLI rejection tests bring CTest to 42 tests.

Completed validation: default Java regeneration compiles and passes all
registered reference checkers; all 42 CTests pass in Debug and Release. Both
configurations pass the new bridge oracle/live runs and existing region, combat,
chest, rail/swinging platform and startup regressions. The local distribution
was rebuilt with executables/settings only, without game data.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_bridges.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

## Remaining branches

Projectile/spell-driven bridge activation, scripts and persistent removed-UID
reconstruction remain pending. Other mutable/destructible actor types, general
spawn/UID pools and exact mixed actor dispatch still need integration.
Transient pickup/effect cleanup on region changes now runs through the separate
owners; see [native transient scope](NATIVE_TRANSIENTS.md). Next: exact mixed
region dispatch and further shared world/object integration.

## Second-level bridges

The four shipped level-1 bridges (UIDs 107/108/109/197, widths 80/80/80/120) and the chandeliers that fall on them now
match the original on the real pack-4 collision map (oracle 6360 rows, 288 widescreen surfaces); see [NATIVE_LEVEL1_VARIANTS.md](NATIVE_LEVEL1_VARIANTS.md).
