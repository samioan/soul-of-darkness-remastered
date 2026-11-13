# Second-level bridges, gates, prompts and camera controller 125

The last four "constructed but unverified" second-level variants named in
[NATIVE_LEVEL1_ENVELOPE.md](NATIVE_LEVEL1_ENVELOPE.md) now have original-bytecode
comparisons on their shipped records and the real pack-4 collision map: the four type-45
bridges, the two type-31 gates with their real type-89 switches, the five type-21 prompts
and the type-49 camera controller UID 125. The existing native handlers match all of them;
no handler, generated Java or rename change was needed. Normal frontend entry to level 1 is
still disabled and this is controlled-fixture coverage, not a route.

## Shipped records

| Type | UIDs | Notes |
| --- | --- | --- |
| 45 | 107, 108, 109 | 80-pixel bridges (two 40-pixel segments) at (140/300/460, 240) in the top corridor. |
| 45 | 197 | 120-pixel bridge (three segments) at (40,1200). |
| 50 | 89/90/91, 190 | The chandeliers above them: the ambush chandeliers (x 180/340/500, y 140) fall onto 107/108/109, and cuttable chandelier 190 (x 120, y 1080) onto 197. |
| 31 | 72 | Gate at (1329,519), animation 4 (80-pixel upward travel), linked to switch 71. |
| 31 | 128 | Gate at (52,660), flag 1, linked to switches 275 and 277. |
| 89 | 71 / 275, 277 | Switch 71 is mode 0 (independent 3000 ms timer, linked back to gate 72). 275 and 277 are mode 1 (dependent on gate 128's state). |
| 21 | 495, 499, 75, 85, 84 | Animations 17 (fire), 6 (crouch), 14 and 14 (attention markers), 18 (ice). Custom rectangles with horizontal offsets -62/-40/-25/-162/0 and vertical offset 62 for UID 499. |
| 49 | 125 | Rectangle (980,200) 280x540, mode 1 (falling camera), no linked source, constraint mask 0. |

## Findings from the original

- Bridge 197 and the three corridor bridges differ from the level-0 bridges only in width and
  map. The original collapse timers, the chandelier impact scan (full width, 17 pixels above through
  the bottom) and the collision-cell writes are the verified level-0 behavior; the corridor
  chandeliers genuinely break their bridges when released, so the ambush scripts now have a
  verified landing target.
- Gate 128 can only be opened by striking **both** mode-1 switches in the same tick. A mode-1 switch
  returns to action 0 on its next handler unless its linked gate is in action 102 or 105, and the
  gate (earlier in record order) samples the switches before they update. A single strike, or two
  strikes four or twenty-eight ticks apart, leaves the gate shut; a simultaneous strike (as a fire pulse or
  wide attack covering both would make) sends the gate into action 102 on tick 3. Gate 72 does the same
  after a strike on its mode-0 switch 71 at tick 2: it travels in action 102 for 21 ticks, holds in action 105
  while the switch timer runs, then returns through actions 106/107. The fixtures assert the opening
  outcomes independently of the original/native comparison.
- The five prompts reuse animations whose handlers were already compared for level 0; only the records,
  the level-1 player and the real map are new. In the oracle a cast whose spell index matches animation 17/18
  moves the prompt to its acknowledged status (2) and removes it once the player leaves, while plain overlaps stay at status 1.
- Camera 125 is the same mode-1 branch as level-0 controller 167 with a different rectangle and a
  2360x1340 map: it takes the camera while the player falls inside its rectangle and releases when the player
  leaves; the oracle asserts the level-1 pixel dimensions.

## Verification

- `BridgeCheck.java` adds 24 sixty-tick scenarios (`level1-<bridge>-<idle|activate|fall|pause|hidden|miss>`)
  on the real pack-4 map with the real chandelier records (the "miss" case shifts the chandelier 300 pixels).
  The oracle grows from 2040 to **6360 rows** (6395 original/reference expectations) and every row matches the native probe,
  including the whole-map collision CRC. Independent assertions require collapse for activation/fall/pause and none for
  idle, miss and a hidden bridge. The native probe draws 288 surfaces at widths 240/569; repeated drawing preserves state and
  pixels and the 240-wide surface equals the centered crop of the 569-wide one.
- `GateCheck.java` adds twelve 120-tick chains (`level1-72-<idle|hit|repeat|miss|pause>`,
  `level1-128-<idle|one|both|late|second|same|pause>`) with gate and switch actors in placed-record order, a real fire-sword
  strike on each switch's own collision rectangle and the real collision map. The oracle grows from 2304 to
  **9744 rows** (gate `G` rows plus per-switch `T` rows; 9779 expectations), all matching the native probe. The checker
  requires the exact set of chains whose gate opens. `pixels()` in `check_native_gates.py` now takes the camera offset, so the
  independent clipped-sprite composer verifies **270** gate surfaces (110 new level-1 surfaces at both widths).
- `PromptCheck.java` adds 25 sixteen-frame scenarios (`level1-<uid>-<overlap|miss|pause|hidden|cast>`);
  the oracle grows from 704 to **1504 rows** and matches natively.
- `CameraControllerCheck.java` runs controller 125 through the same sixteen scenarios on pack 4;
  the oracle grows from 1368 to **2008 rows** and matches natively. Its world/order and eligibility fixtures are unchanged.

Validation on 2026-11-13: default regeneration exited 0 (generated Java unchanged), compiled with zero errors and
passed ReferenceCheck's 4218 comparisons plus every registered check. Debug and Release pass all 109 CTests and all four
extended checkers, including their existing host runs at widths 240/569, 30/60/144 FPS and both interpolation settings.
The Release distribution passes its timing/self-test and asset-exclusion validation. Oracles and logs stay in ignored analysis/.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_bridges.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_gates.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_prompts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_camera_controllers.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

## Boundaries

- All fixtures are isolated handlers on real records and the real pack-4 map, run through the ImageIO test adapter,
  not a MIDP runtime. The gate/switch probe hard-codes placed-record order; `CombatWorld`'s dispatch of this chain, the
  host wiring of the level-1 world, prompt captions and camera region handoff are not exercised here.
- Strikes are controlled sword inputs at fixed ticks. A fire pulse that overlaps both gate-128 switches, or a natural player
  position, has not been run through a level route.
- Camera 125 is checked at handler/active-camera level only. Its interplay with other level-1 regions,
  behavior-6 flyers and the passages is untested, and the host camera/HUD for level 1 is not wired.
- Only the four paired chandelier/bridge combinations above are fixtured. The impact scan is geometric, but other
  chandeliers were not run against other bridges, and no bridge was checked with a player standing on it.
- Still pending for task 8: passage world/region handoff, staggered 118/123 ambush starts, host wiring of the
  level-1 intro/scripts/chandeliers, the actual route and the 240/569 presentation checks. The full game remains unfinished.
