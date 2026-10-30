# Native platform landing and support

The player now understands an owned support surface independently of tile
grounding. This pass supplies the landing/contact foundation for type 33 moving
and type 50 swinging platforms. **Their movement handlers and actor loading are
not connected to the displayed level by this initial pass.** The first-level preview continues
to run the existing enemy/pickup/chest subset.

## Original behavior

`updatePlatformContact` follows original `Actor.d(int,int[])`. The caller refreshes
and supplies the platform surface after moving it. Attachment uses player X/Y,
nonnegative vertical velocity, a five-pixel tolerance and predicted Y compared
against the surface's top and vertical velocity. It tests player X inclusively;
it does not require the player's whole collision box to fit on the platform.

First attachment queues landing pose 6 and positions the player at top + 1.
Type 50 also clears `moduleMask[11]`, represented by `ContactBody.platformLatch`.
When already attached, type 33 stays at top + 1 and type 50 uses the exact top.
Jumping away or leaving the horizontal extent detaches according to the original
pose/overlap/relative-velocity conditions. Edge detachment resets both velocities.
A different surface cannot steal the current support link. The helper preserves
Java signed shifts and wrapped position/velocity arithmetic.

`Player` now checks its support link during idle/walking, falling/landing, hurt,
crouch/drop restrictions, standing melee/combo and jump selection. Jump clears
the link immediately. Late side separation excludes the supporting body.
`grounded` still describes tile collision; platform support does not set it.
Pixel corrections enter fixed-point coordinates on the following tick, preserving
the original bounds-refresh timing. Drawing does not change support or physics.

The surface owner must keep its `ContactBody` at a stable address and clear
`Player.platform` before removing it. A per-tick copied enemy body is unsuitable
for this ownership. Horizontal carrying is not inferred from a moving rectangle:
it belongs to the original handler/integration order and remains pending.
Spring launches (64), grouped platforms (82), type-14 pushing,
nonplayer support/attachment chains and full world ownership are outside this pass.
Type 80 was mislabelled a collapsing platform; it is now implemented as an
energy barrier in [NATIVE_ENERGY_BARRIERS.md](NATIVE_ENERGY_BARRIERS.md).

## Validation

`PlatformCheck.java` calls the unmodified original helper and player update,
then compares against generated Java using the existing ImageIO test adapter.
Original bytecode for `d(int,int[])` was inspected with `python tools/bytecode.py b`;
player call sites in `aw`, `ay`, `az` and `aA` establish how support affects actions.
No generated Java edits, patches or renames are needed.

Default regeneration emits ignored `analysis/platform-oracle.tsv`:

- 4,320 contact setups, each called twice, cover both types, exact horizontal
  edges, height tolerance, ascending/stationary/descending player and surface
  velocities, unbound/same/other ownership, three poses and contact offsets.
- Twelve scenarios add 1,152 player ticks with before/after-clock snapshots:
  standing, jump, walk off, crouch, melee/combo, rising/sinking/sliding surfaces,
  pause and the type-50 standing/jump height distinction.

The native checker matches all **10,944 snapshots**, including complete player
movement/animation/bounds state, support identity and the type-50 latch.
Original/reference checks total **251,727 comparisons**, with independent landing
and detachment assertions. Nine new data-free assertions bring the combined
player executable to **169 checks**; CTest remains **32 tests** per configuration.
Existing host regressions retain widths 240/569 and independent FPS/interpolation;
these controlled platform fixtures do not claim a platform play-through.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_platforms.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
```

Next: type-33 movement/controller logic, including level 0's rider-controlled
variant 8, and stable live platform ownership in the native world. Then connect
type-50 movement and the remaining contacts/region ownership.

The subsequent NATIVE_MOVING_PLATFORMS.md pass now connects type 33 to level 0;
type-50 swinging movement is still pending. NATIVE_STARTUP.md adds the native
logo/title/menu/credits entry into that preview.
