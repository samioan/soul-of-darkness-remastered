# Second-level platform variants

The remaining level-1 type-33 and type-50 records now have controlled original-bytecode/native
comparisons on the shipped pack-4 collision map. The existing native handlers match these records;
no handler or generated Java change was required. This completes the platform-variant verification
part of port-roadmap task 8. Normal frontend entry to level 1 remains disabled.

## Shipped records and original evidence

| Type | UIDs | Parameters and behavior |
| --- | --- | --- |
| 33 | 219, 241, 288 | Rider-controlled direction 8, speed 5, delay 20; individual rectangular travel bounds. UID 219 starts in animation 8, the others in 9. All have fourteen parameters, including the unused trailing -1. |
| 50 | 25, 33, 145, 190 | Cuttable mode 0, zero angular speed/acceleration/excursion; rope lengths 100/100/140/100. Boarding enters the controller even though the angle stays zero; cutting drops the chandelier. |
| 50 | 169, 510 | Continuous mode 1, excursion 30, speed 256, acceleration 16; opposite initial directions, rope lengths 120/160. |
| 50 | 189 | Continuous mode 1, excursion 45, speed 384, acceleration 24, rope length 140, initial direction -1. Earlier remaining-work lists omitted this record. |

The level also has the already verified script-owned type-50 UIDs 89/90/91; see
[ambush coverage](NATIVE_LEVEL1_AMBUSH.md). Together these cover all ten placed type-50 records.

Inspected the original type-33/type-50 construction and dispatch call sites, support helper
`b.d(int,int[])`, pendulum handler `b.br`, reset `b.T`, chain-cut helper `b.U` and angular controller
`b.V`, with `python tools/bytecode.py b`. These are the existing handlers described in
[moving platforms](NATIVE_MOVING_PLATFORMS.md) and [pendulums](NATIVE_SWINGING_PLATFORMS.md).
No additional Java rename or decompiler patch is needed. In particular, reset restores the cached
angular parameters, position and support latch without inventing a new initial continuous action.

## Verification

- `SwingingPlatformCheck.java` adds eight 240-tick scenarios per remaining type-50 record: idle,
  boarding, chain cut, jump, pause, explicit reset, hidden and frozen. Original and regenerated Java
  compare player and chandelier fixed-point state, bounds, animation, support identity, health,
  angular fields and all twelve controller slots before and after animation advancement.
  Independent assertions require boarding, continuous motion only in mode 1, suppression while
  hidden/frozen, mode-dependent chain cutting, and landing/breaking on the real map.
  **53,760 new snapshots** bring the oracle to **69,600 rows**, with **1,840,871** total
  original/reference expectations including the existing fifteen arenas.
- `MovingPlatformCheck.java` adds eight 160-tick scenarios for each of the three type-33 records:
  boarding, crouch, left/right steering, jump, player above/below the travel bounds and pause.
  Independent assertions require boarding, climbing and crouch-driven descent. These add
  **15,360 snapshots**, bringing that oracle to **26,880 rows** and **618,261** total
  original/reference expectations including the existing eighteen arenas.
- Native probes load the same records, level-1 maps and level-indexed player. The Python checkers
  compare all **96,480** combined Java/native rows and require every new UID/scenario combination.
  The upper chandeliers' boarding fixtures start five pixels above the platform; the old generic
  eighty-pixel gap would place Kale's head inside this map's ceiling.
- The probes draw **1,280** additional surfaces at widths 240/569 (896 pendulum, 384 rider-platform
  surfaces). Repeated draws must preserve serialized simulation state and pixels. The 240-wide
  canvas must match the centered crop of the 569-wide canvas. These are native rendering checks,
  not original-Java framebuffer comparisons.
- Existing level-0 host rides remain checked at widths 240/569, 30/60/144 FPS and interpolation
  on/off. Both Debug and Release pass all 109 data-free CTests and both expanded native checkers.
  Default Java regeneration passes compilation and every included comparison through the final
  lightning check. The Release distribution passes its timing/self-test and asset-exclusion validation.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_moving_platforms.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_swinging_platforms.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

## Boundaries

These are isolated player/platform fixtures on the real collision map, with explicit cut/reset inputs
and animation advancement. The Java checks use the ImageIO test adapter, not a MIDP runtime.
They do not run the whole second-level actor queue, bridges, enemies,
region transitions, save restoration or a route from spawn to exit. The added width checks exercise
the platform renderer directly; they do not establish second-level host camera/HUD behavior.
The full second-level task still needs its other existing-type variants, host script/intro wiring,
staggered ambush starts, passages and route/presentation checks before frontend progression is enabled.
