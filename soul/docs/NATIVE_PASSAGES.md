# First-level passages and room presentation

The level-0 map is 1820 by 1680 pixels and packs separate rooms into one tile
grid. The original joins these spaces with invisible type-59 actors. Drawing
every adjacent room as one continuous widescreen scene exposed that packing;
without the passage handlers the first route also stopped at a solid wall.
The reported screenshot is near source UID 81 at (1228,37), linked to UID 80
at (1542,100). The wall is part of the original collision map, not missing tiles.

## Original handler and native ownership

`Actor.bt` resolves its target UID lazily, then checks inclusive player overlap.
Mode 1 requires left facing and negative X velocity; mode 2 requires right
facing and positive X velocity. It sets player X to the destination X and steps
one pixel in the travel direction while the complete player/destination
rectangles overlap. This loop uses the player's old Y, so a vertically distant
destination can require zero exit steps. It then maps relative vertical position
between the two passage rectangles using an 8-bit fraction and integer truncation
(`Actor.bs`). It clears platform/camera attachment and, unless a region change
is already pending, requests immediate destination region selection.

The original updates integer position but leaves fixed coordinates for next
tick's synchronization. Bounds are refreshed inside the exit loop, before the
height change; that stale Y rectangle lasts until the normal bounds refresh.
These details are preserved and checked, including inclusive edge contact.
Original bytecode is in ignored `analysis/b.bytecode.txt`, methods `bt/bs`.
No Java rename or decompiler patch was needed.

`LinkedPassage` loads all 16 first-level records and uses stable placed target
addresses. `CombatWorld` dispatches passages in record order, with the existing
frozen eligibility queue and default `Game.shouldUpdateActor` bucket limit 2.
Hidden/frozen/paused sources skip their handlers; target UID resolution can
still find hidden targets. Transfers detach player support and immediately
refresh supported region ownership when permitted. Later enemies see the new
player target, while region/transient cleanup remains at the next tick boundary.
Selected first-level camera controllers are now ported; see NATIVE_CAMERA_CONTROLLERS.md.

Only horizontal modes 1/2 are supported. Missing targets fail before mutating
player position; malformed records, zero heights and unsupported directions are
rejected. The exit loop has a bounded failure path for unsupported player bounds.
This is not a general registry, save restoration or all-level passage claim.

## Widescreen rooms

Normal game presentation now uses active region bounds for both camera clamping
and world clipping. Outdoor region 214 is 1240 pixels wide: width 569 reveals
more of that region than width 240, and stops before the unrelated packed room.
The destination room is 280 pixels wide. At width 569 it is centered with plain
margins, revealing all 280 pixels without stretching tiles or showing neighbors.
HUD and pause UI use the complete viewport. No tile or collision cells change.

`gameplay_view.h` implements this presentation policy independently of the 83 ms
simulation and existing reference-width update eligibility. Explicit asset and
movement preview modes retain their map inspection cameras. This is a native
room-view policy, not an equivalence claim for the original animated camera.

## Verification

Default regeneration runs `PassageCheck.java` against original and regenerated
classes and writes ignored `analysis/passage-oracle.tsv`. There are 464 snapshots
covering 13 boundary scenarios and every first-level link, with two independent
first-passage position expectations (466 original/reference expectations).
Scenarios include facing, velocity, inclusive edges, scaled destination heights,
pause, hidden/frozen sources, late link resolution and hidden targets.

`check_native_passages.py` compares these rows, then runs 24 actual host fixtures
at widths 240/569, FPS 30/60/144 and both interpolation settings. The fixtures
place Kale immediately before source 81 and use normal movement to cross it;
they do not claim an automated full-level play-through. Twelve fixtures use the
movement host; twelve traverse logo/menu/prologue/loading into the game host.
Independent image comparisons verify that shared room pixels agree between
portrait/widescreen and that neighboring rooms do not leak into the margins.

Eighteen data-free assertions cover crossing/lookup/flags, fixed and bounds
timing, ordered world dispatch, destination region timing and room camera
arithmetic. Player checks total 302; CTest totals 54 including three new CLI
guards. The original handler checks use the test ImageIO adapter, not a MIDP
runtime. Captures remain in ignored analysis.

```powershell
python tools/check_native_passages.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

The ability-2 reward/chest-57 unlock sequence is now implemented; see NATIVE_CHEST_UNLOCK.md.
The complete first-level route and full game remain unfinished.

Second-level verification now covers all twenty placed links and eleven cases per link,
plus destination tile surfaces at widths 240/569; see [NATIVE_LEVEL1_PASSAGES.md](NATIVE_LEVEL1_PASSAGES.md).
These are isolated fixtures; second-level progression remains gated.
