# Second-level passage verification

All twenty shipped level-1 type-59 records now have original-bytecode/native
fixtures. The existing horizontal passage implementation matches these variants;
no runtime handler change or generated Java patch was necessary.

The linked pairs are 111/117, 511/512, 127/514, 139/180, 198/34,
86/110, 204/205, 64/65, 67/68 and 78/83. Both directions are exercised.
Original construction, dispatch, `b.bt` and height mapping `b.bs` were inspected
with `python tools/bytecode.py b`. The exit loop refreshes bounds before moving
vertically and leaves fixed coordinates stale. These are intentional original
behaviors, including transfers with zero horizontal exit steps when the old
player bounds do not intersect the vertically distant destination.

## Checks

- `PassageCheck.java` loads pack 4's actual maps and level-indexed player, then
  runs eleven eight-tick cases per source: crossing, wrong facing, stationary,
  wrong motion, outside, inclusive edge, pause/resume, hidden, frozen, delayed
  target registration and hidden target. Each tick captures state before and
  after the normal bounds refresh. These add **3,520 snapshots**, bringing the
  combined first/second-level oracle to **3,984 rows**.
- Independent expectations check all twenty records exist, camera release or
  retention, mapped height, stale fixed X, the inclusive horizontal exit solved
  from player bounds, and delayed/paused transfer resumption. These supplement
  original-versus-regenerated equality and native comparison. The final fixture
  passes **4,427 original/reference expectations** including the level-0 cases.
- The native probe requires every destination UID and uses the actual map and
  level-1 player. The Python checker requires the complete set of twenty UIDs
  times eleven scenarios before comparing every field of every oracle row.
- Forty destination tile surfaces cover widths **240/569**. Repeated drawing
  must preserve pixels and player state; shared world pixels must match between
  widths and pixels outside the room clip must retain the background color.
  The destination room is selected explicitly from the target passage volume.
- Existing first-level passage regressions retain their 24 host runs through
  movement/startup at both widths, 30/60/144 FPS and interpolation on/off.

Debug and Release each pass all 109 data-free CTests, the 3,984 Java/native rows,
forty destination surfaces and the 24 first-level host runs. The Release
distribution passes its timing/self-test and asset-exclusion checks.
Default Java regeneration compiles with zero errors and completes ReferenceCheck's
4,218 comparisons and the remaining fixtures through the final lightning check.
The compiler's existing unchecked-operation note is emitted on stderr; PowerShell's
redirected log labels that note `NativeCommandError` even though all checks complete.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_passages.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_passages.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe
.\soul\port\build_dist.bat
```

## Boundaries

These are isolated handlers on shipped data, using the ImageIO analysis adapter,
not a MIDP runtime or an actual second-level route. Rendering checks compare
native surfaces, not original Java framebuffers. They explicitly choose the
destination room and do not establish world-queue region handoff: source 514,
for example, transfers to X=20, exactly the destination region's strict left
boundary. Its ordinary region selection can retain the previous room until
subsequent movement. The first-level world-order checks remain separate.

No frontend progression was enabled. Second-level gates, bridges, prompt modes,
camera 125, staggered ambush starts, host script wiring and the continuous route
remain on the roadmap. The full game remains unfinished.
