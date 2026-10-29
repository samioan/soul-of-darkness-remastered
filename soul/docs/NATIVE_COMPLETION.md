# Native first-level exit and completion UI

The first-level type-20 exit, UID 173, now starts state 15 on inclusive player
contact. Gameplay stops after the entry tick, the bars close on simulation ticks,
and LEVEL COMPLETE appears with Enter/E/J confirmation. One fresh confirmation
hands off once to finishLevel and the next chapter selection.

The subsequent [finishLevel pass](NATIVE_FINISH.md) now saves completion/unlocks,
unloads the old world and selects chapter 2. Its entry is disabled until that
level is playable; Left selects chapter 1 for replay. The full game remains
unfinished. This document's original-code UI fixtures still stop at a counted
finishLevel boundary; the new progression fixtures execute the real routine.

## Original behavior and evidence

The shipped record is `(20,173,1780,770,4,8,0,0,23,23,0,1,-1,0,0)`.
Actor.bL/updateTrigger's animation-4 branch tests the player's animation against
9, then inclusive rectangle contact. Point rectangles are rejected; exact edges
are accepted. It does not inspect health directly: the controlled zero-health,
non-death-pose fixture still enters. World pause and flags 1024/16384 defer the
handler. The trigger is not removed on entry. State ownership prevents ordinary
world dispatch from invoking it again while the completion UI is active.

Original b and e bytecode were inspected with tools/bytecode.py, including
Actor.bL, Game.a/changeState, Game.ac/updateLetterbox and Game.J/updateLevelComplete.
changeState(15) initializes current bars aN=1 and target aM=160, resets stateFrame
and clears pending/held/frame input. It preserves delay aQ. The entering gameplay
frame still draws the world and advances letterbox once; subsequent ticks run
state 15 rather than physics or AI.

The bar clock waits until aQ<0, then moves toward its target by 20 pixels,
snapping when the remaining difference is less than 20. While current bars are
positive it decrements aQ. With the normal delay of 2, the entry frame consumes
one delay step and the bars meet on the tenth subsequent state-15 tick. The
message is unavailable during closing, so early/held confirmation cannot skip it.

Once the bars meet, Game.J draws the world again, which invokes updateLetterbox
again before drawing the full-screen panel, softkey and string 85. The native
clock preserves this second delay decrement even on the first ready tick.
Confirmation mask 327712 (0x50020) clears current/target bars and calls finishLevel.
Down and right-softkey/cancel masks do not acknowledge. Native Enter/E/J already
map to the accepted 65536 fire bit; repeated keydown events are suppressed.

The native ready panel uses the original black fill and border colors e89d1b /
9e4f09, expanded to the desktop viewport. Its English message and keyboard caption
are desktop translations/adaptations. Bars span the viewport at widths 240/569;
logical height and all UI clocks stay fixed at 320 and 83 ms respectively.

## Native ownership and boundaries

ExitTrigger supports the shipped level-0 animation-4 record subset. It joins
CombatWorld's placed-record queue with normal strict radius-two eligibility and
forced-update precedence. LevelCompletion owns one entry, the bar/delay clock,
ready state and one acknowledged state-19 handoff. The host clears gameplay input
on entry and routes only Enter/E/J while frontend state 15 owns input. Escape,
movement, attacks and R cannot bypass this modal state. Drawing never advances
its clock. The raw preview exposes the same modal clock and handoff diagnostics.

After the entry tick, the native world and its sprite animations remain frozen
while the UI advances. The original completed-screen drawWorld can mutate other
paint-owned animation/HUD/weather state; general paint/runtime equivalence is not
claimed. Those owners remain in the rendering/script roadmap. Intro and reward
modals retain their existing ownership; later-level exit countdown/boss variants
are not enabled by this placed-only loader.

Inspection also corrected the earlier type-49 camera eligibility assumption:
Game.shouldUpdateActor uses radius seven plus inclusive volume overlap, after
forced/disabled/unspawned precedence. Eight new original/native eligibility rows
cover it; see NATIVE_CAMERA_CONTROLLERS.md. Handler activation remains strict.

## Verification

CompletionCheck compares original/generated classes in isolated loaders with
test ImageIO/RMS adapters. It preserves original exit, state entry, letterbox,
message dispatch and input bytecode. Named graphics calls are suppressed, and
drawWorld is replaced by its real letterbox call. finishLevel is replaced by a
counted state-19 boundary, explicitly excluding progression/save/unload checks.
The Game instance is an opaque Unsafe-allocated Canvas sentinel; no MIDP runtime
or real Canvas rendering is claimed. Hooks assert their exact method matches.

There are 1012 original/reference expectations and 1008 native rows across 21
scenarios, with snapshots before/after UI work: ordinary/late contact, inclusive
edge, one-pixel miss, point/outside, death pose, zero health, pause, hidden/frozen
trigger, early/held/entry confirmation, exact ready-tick confirmation, wrong
Down/cancel input, left softkey, numeric fire, waiting, and a longer initial delay.
Rows include state, current/target bars, delay, entry/ack counts, trigger liveness/
flags and input clearing.

Thirteen data-free assertions verify death/point/pause rejection, inclusive edge,
one-time entry, entry delay, closing timing, wrong/early/repeated input and
unsupported variants. Three CLI guards cover invalid exit fixtures. The current
baseline is 508 player assertions and 101 CTests.

The host checker runs ten milestones and 48 raw/game wait/ack runs at 240/569,
30/60/144 FPS and interpolation on/off. The fixture starts at (1740,799), freezes
unrelated enemies, beams and checkpoints, and uses actual rightward movement to
reach the shipped exit at (1777,799). It verifies entry, closing/message timing,
one-time acknowledgement, frozen player state, frontend ownership/world unload and equal
simulation/per-width pixels across presentation settings. It is not a route test
from the original spawn.

Validation on 2026-10-29: default regeneration exited 0, compiled with zero
errors and passed ReferenceCheck's 4218 comparisons plus all registered checks.
Debug and Release passed 98 CTests and 508 player assertions. Both builds match
the 1008-row completion and 1368-row camera oracles and pass their host checks.
Checkpoint/restart, cross-process Continue, intro playback/skip, startup/frontend,
chandelier and prompt-removal regressions passed. Closing and completed UI
captures were inspected at 240/569. The rebuilt data-free distribution passed
package validation and its asset tool matches the completion oracle.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_completion.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

Oracles, logs and captures remain in ignored analysis/. Assets, JARs and binaries
remain outside git and the package.
