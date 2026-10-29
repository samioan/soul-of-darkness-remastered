# Native first-level camera controllers

Level 0 now loads both type-49 records: controller 63 frames Kale with enemy
202, and controller 167 tracks a falling player. Camera state advances on the
83 ms simulation tick; presentation width/FPS do not change activation, source
lookup, target selection, eligibility or easing.

## Records and original behavior

| UID | Rectangle | Mode | Source |
| --- | --- | --- | --- |
| 63 | (1590,321)-(1790,331) | 0: linked actor framing | Enemy 202 |
| 167 | (1220,500)-(1500,1400) | 1: falling camera | None |

Both records use custom bounds flag 8, constraint mask zero and speed zero.
Actor.bi/updateCameraController and bj/removeCameraController were inspected
against original b bytecode. The native subset accepts modes 0/1 with mask zero;
forced-scroll modes, mask-dependent constraints and later-level variants remain
unsupported.

The controller retries missing source UIDs and caches a successful lookup plus
the source persistence slot. A cached slot marked -99 removes the controller,
clears its link and releases the global active UID. A missing source in mode 0
disables camera ownership without consuming the controller. Source visibility
and update-freeze flags do not prevent lookup or source-bound refresh.

Activation uses strict rectangle overlap, excluding exact edge contact and
point rectangles. Overlap sets controller flag 16. Outside overlap, an actor
leaves another active controller alone; otherwise it clears its own forced
flag and releases ownership. Record order decides which overlapping controller
last supplies the active mode/target. Region-change state Game.ai defers the
whole handler, including removed-source cleanup. World pause and flags
1024/16384 also defer it.

Mode 0 refreshes the source bounds and computes the midpoint of the player/source
position envelope (10-pixel horizontal padding and 50-pixel space above them).
It changes targets only while player velocity is nonzero. Ownership is retained
only if the canonical target viewport strictly contains the player's position
with those margins; failure can leave flag 16 set while the active UID is -1.

Mode 1 owns the camera only while velocityY is positive. It sets targetY to
playerY-160, then falls through to mode 2's player-X clamp even when releasing.
The shipped zero mask makes no R/S/T/U constraint writes.

Original e.d/updateCamera(2), its private j easing helper, script-bound clamps
and map-bound clamps were also inspected. The active mode-0/1 native path uses
the same integer arithmetic. In particular mode 1 first computes ordinary
vertical easing (limit 32, acceleration 2), then applies its second easing
(limit 40, acceleration 15) using that newly computed speed. Its horizontal
target follows facing (playerX-160 or playerX-80). No animation, collision,
checkpoint or camera state advances from drawing.

## World and widescreen integration

CombatWorld constructs controllers in placed-record order, uses ordinary strict
radius-two eligibility with flag-16 precedence, and resolves source 202 from
stable enemy storage. The selected source remains available through hurt/death
poses and respawn states; final native removal releases the controller on its
next eligible handler. Native source liveness stands in for the tested original
persistence-slot removal; this does not implement a general reusable UID/pool
registry or every linked actor type.

Selected camera state supplies canonical 240x320 visibility/damage/script buckets
and advances after actor animation, before the next bucket cache. Region
selection still publishes the current room for presentation clipping. Its
camera constraint writes are suppressed while a controller owns them, matching
the original region handler. Passage transfers release active camera ownership;
reconstructed checkpoint worlds start with fresh controllers and no borrowed
link or active camera snapshot. Desktop save version 1 is unchanged.

Presentation expands around canonical cameraX+120 and retains cameraY. At 569
logical pixels it reveals more of the active room. Narrow rooms are centered
with clipped margins, keeping neighboring packed rooms hidden. Both portrait
and widescreen captures of the two active modes were inspected; captures and
oracles remain in ignored analysis/.

Outside these active modes, native ordinary following remains the existing
prototype player-follow camera. Release returns to that view. Full original
ordinary camera transitions, look-ahead, platform/player-follow branches,
shakes and general painter/bucket ownership are not established by this pass.
The intro retains its separate scripted camera owner. These checks are not a
complete first-level route or full runtime-equivalence claim.

## Verification

CameraControllerCheck runs original and generated classes in isolated loaders
with the existing test ImageIO/RMS adapters, not a MIDP runtime. It produces
1365 original/reference expectations and 1360 native rows:

- 1280 handler/active-camera rows cover both shipped records across sixteen
  scenarios, before and after selected camera advancement. Cases include
  stationary/moving player, exit/reentry, edges/points, missing/late/removed
  source, hidden source, pause, hidden/frozen controller, deferred region-change
  handling, another active owner, a distant source and ascending/falling player.
- 80 original Game.W rows cover linked-first and falling-first record order,
  pause and actual source removal. They compare controller liveness/flags,
  global mode/target/current camera/easing/bounds and the full update UID queue.
- Twelve data-free assertions cover strict contact, cached-source lifecycle,
  pause/region gating, fall/release/easing and unsupported branches. Three CLI
  guards reject invalid fixture combinations. The current baseline is 495
  player assertions and 95 CTests.

The host checker adds ten activation/release milestones at widths 240/569,
24 active-controller render runs across 30/60/144 FPS and interpolation on/off,
and 24 final raw/game runs through both controllers and source removal. The
fixture freezes unrelated actors and prescribes player motion/relocations to
isolate camera ownership. It is not a natural traversal from spawn. Simulation
state and per-width pixels agree across presentation settings.

Validation on 2026-10-29: default regeneration exited 0 after compilation,
ReferenceCheck's 4218 comparisons and all registered original/reference checks.
Debug and Release passed all 95 CTests and 495 player assertions, plus the
1360-row oracle and 58 camera host checks. Chandelier/prompt removal, passages,
ordered world dispatch, checkpoint/restart, cross-process Continue and intro
playback/skip regressions passed. The rebuilt data-free distribution passed
package validation; its asset tool matched the new camera oracle.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_camera_controllers.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

The next ordered task is exit 173/completion state 15. Full progression, later
levels and the complete game remain unfinished.
