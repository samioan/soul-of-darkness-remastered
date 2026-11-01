# Native opening cutscene: trigger 82 and script 76

Normal New Game now starts the original first-level intro when Kale enters the
shipped type-20 trigger 82. Its six tracks move the player, enemy 19, flying
actors 83/84, prompt 267 and the camera. Four English dialogue panels pause the
timeline. Enter/E/J or Down advances a three-line page; Backspace skips while
allowed. Ordinary player control, AI, pickups and world handlers stop during
the scene; animation and presentation still advance on 83 ms ticks.

This completes the first item in both current roadmap checklists. It does not
implement every cutscene or the complete game. Script 160/trigger 161 is now
implemented separately in
[NATIVE_CHANDELIER.md](NATIVE_CHANDELIER.md).

## Original branch and timing

Trigger 82 is animation 8 at (89,182), with a 60x80 inclusive volume, script UID
76 and skip operand 0. Actor.updateTrigger -> Actor.o -> Game.startCutscene sets
player facing from the trigger, clears input, stops motion, resets weapon-switch
cooldown, clears combat effects and consumes the trigger. Points do not overlap;
pause/hidden/frozen gates leave the trigger available.

| Track | Owner | Commands used |
|---|---|---|
| 0 | type-1 enemy 19 | Position, pose, encoded flags, anchored dialogue |
| 1 | flying actor 83 | Position, pose, final removal |
| 2 | flying actor 84 | Position |
| 3 | Camera | Command 12 world-focus targets |
| 4 | Player 186 | Position, pose, encoded flags, anchored dialogue |
| 5 | Placed prompt 267 | Position, pose, hide/reveal flags |

The original interpreter reads the old signed-short tick before incrementing
it, processes one pending keyframe per track per call, applies immediate poses
at/past their key tick and executes extended commands only at equality. Position
interpolation divides by targetTick-currentTick+1. Encoded 23/24 flags use the
original high-word extraction and low facing-bit promotion. Both normal and
fast-forward paths preserve these rules, including post-removal positioning of
a still-referenced actor whose registry slot has just been cleared.

| Script tick | Dialogue |
|---|---|
| 41 | Kale sees the castle |
| 72 | Zombie welcomes his dinner |
| 77 | Kale asks about the vampire master |
| 82 | Zombie answers |
| 86 | Final camera/player/prompt keyframes complete |

Dialogue pauses the script counter rather than advancing a wall-clock timer.
Skip follows the original background-script fast-forward: all remaining keyframes
execute without intervening physics/animation steps, so actor removals, final
poses/flags and prompt reveal are retained. A blocked-skip fixture confirms
that a disabled skip policy does not fast-forward. Completion clears dialogue,
releases camera/input ownership and returns to regular simulation.

The original b/e bytecode was checked for trigger dispatch, start/finish,
interpreter flags, anchored dialogue and skip. No Java renames, decompiler
patches or manual generated-source edits are introduced by this pass.

## Native ownership and presentation

IntroCutscene executes the selected actual script 76 data through typed actor
bindings. CombatWorld resolves only the supported existing owners; no raw
sprite/map/actor pointers are saved to disk. Removed flying actor 83 stays
allocated but inactive, while actor 84 retains its final live placement. Prompt
267 is hidden during the intro and revealed at completion. The world releases
all scene state when rebuilding for New Game, Restart or Continue.

The background-script branch suspends normal handlers and gives the cutscene
its own camera. Portrait commands retain their canonical 240x320 focus/clamps;
introView expands the same world focus to width 569 and clips to the active room.
The HUD is suppressed while the scene owns control. A centered 240-wide dialogue
panel expands from its recorded actor anchor; native letterbox bars and keyboard
hints fit both widths. English text is a desktop translation of table 2 entries
0..3, with original common speakers Kale/Zombie. It is not an official English
resource or original MIDP Graphics pixel-equivalence claim.

Dialogue paging, native bars and the final camera handoff use desktop presentation
state. Complete Game.paint timing and the mixed painter are not claimed equivalent;
normal actor animation primitives were ported in the earlier movement/sprite
passes. General global/cinematic-actor tracks, other dialogue commands, QTEs,
audio and other script IDs remain in the roadmaps.

New Game starts this owner automatically. Existing explicitly controlled combat/
platform/reward fixtures retain their isolated owners; explicit intro fixtures
exercise the new owner from the real level spawn. Raw movement-preview mode
keeps its diagnostic/free-movement scope unless --intro-script is requested.

Checkpoint semantics remain unchanged: ordinary type-20 removal is not one of
Restart's persistent removed types, nor a flag-512 removed UID. Restart/Continue
near checkpoint 11 can reconstruct and activate the intro again. Later checkpoints
outside its volume do not. The save checker now verifies preserved HP/MP/weapon
state while that resumed intro runs; it no longer assumes an inert trigger.

## Verification

IntroCheck.java uses fresh original/reference class loaders, shipped objects,
scripts, maps, sprites and string tables in the existing lifecycle analysis
harness. It runs the actual trigger and background interpreter without a MIDP
renderer. Normal play, explicit pause, early skip, mid-timeline skip, dialogue
skip, late-dialogue skip and disabled skip produce 969 original/reference
expectations and 5460 native-comparable rows. Rows cover all six track offsets,
script counter, camera, dialogue ID/anchor and five actors' positions, flags,
poses, facing and registry presence. Trigger-start checks also assert input
clearing and consumption. This is a script/handler oracle, not a complete
original Game.updateGameplay/render-loop comparison.

```
python tools/check_native_intro.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

Seven milestones and 48 actual-map host runs cover playback/skip in raw-preview
and normal game flow at 240/569, 30/60/144 FPS and both interpolation settings.
These begin at the ordinary spawn and let collision activate the shipped trigger.
The headless fixtures press real dialogue/skip keys, attempt movement while locked,
then walk right and left after completion. They verify one start/finish, four
normal dialogue openings, actor-83 removal, prompt reveal, no leaked attacks,
HP preservation and final settled control at (167,259). Simulation diagnostics
and per-width render CRCs are invariant across display settings.

The default --game-script fixture separately verifies automatic intro entry
without a dedicated intro option. Frontend tests retain pause/intro freeze/resume
and add twelve post-intro HUD runs. Fifteen data-free trigger/camera/text checks
bring player assertions to 466; three CLI guards bring CTest to 89 tests.


Validation on 2026-10-29: default regeneration passed compilation, ReferenceCheck
and all original/reference fixtures. Debug and Release passed all 89 CTests and
the seven/48 intro host checks. Release matched the regenerated 5460-row oracle;
the rebuilt package matched those rows and its executable hash matches Release.
Existing Release combat, world, progression, checkpoint and placed-prompt checks
passed. The frontend checker passed pause/intro freeze/resume and twelve completed-
intro HUD runs. Updated cross-process saves passed in Debug and Release. Portrait/
widescreen captures were inspected, including English dialogue and control return.

Level 1 reuses this owner for its own intro (trigger 134/script 133); see [NATIVE_LEVEL1_INTRO.md](NATIVE_LEVEL1_INTRO.md). The text above describes level 0.
