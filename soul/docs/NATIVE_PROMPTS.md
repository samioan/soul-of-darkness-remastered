# Native placed tutorial prompts

All nine placed type-21 volumes in the first level now load. Movement/attack/
crouch hints appear while Kale overlaps their original custom rectangles and
remove after being visited and left. The fire hint appears when its volume is
in view, acknowledges a matching recent cast while overlapping, then removes
when Kale leaves. The original attention marker remains an animated sprite.

| UID | Animation | Desktop presentation |
|---|---|---|
| 267 | 8 | A / D - MOVE |
| 265 | 2 | SPACE + RIGHT |
| 271 | 4 | E / J - ATTACK |
| 187 | 4 | E / J - ATTACK |
| 59 | 14 | Original attention marker |
| 188 | 10 | RIGHT / D |
| 189 | 12 | SPACE + LEFT |
| 44 | 6 | DOWN / S - CROUCH |
| 8 | 17 | K / 0 - FIRE |

The desktop captions replace the original phone-key button pictures; they are
an intentional keyboard adaptation, not original sprite-pixel equivalence or
an official English resource. Caption anchors clamp within the active room's
viewport clip, including at width 569. The warning marker uses the original
sprite-18 animation and flags. Drawing does not update prompt state or clocks.

## Evidence and ownership

Original Actor.bN/updatePrompt, initializeFromRecord, isVisible and Game.removeActor
establish the implemented behavior. The bN bytecode was inspected; no generated
Java edits, patches or renames are introduced. Ordinary inclusive overlap
rejects points. The magic acknowledgement condition separately accepts rectangle
containment, so an interior point can acknowledge and immediately leave/remove
a magic hint in the same handler; the fixtures retain this original edge case.

`PlacedPrompt` supports the placed rectangular handler subset, overlap status,
optional removal delay, fire/ice acknowledgement and the original switch-marker
handler. Common animation advances on 83 ms ticks. Dynamic camera/player-follow
modes 1/2/3 and animations 36/37 are not loaded through this class. Placed first-
level prompts do not use those modes. Original visibility comparisons here use
a camera centered on the fixture; complete offscreen/painter equivalence is not
claimed for the adapted desktop captions.

`CombatWorld::loadPrompts` opts into record-ordered dispatch. Eligibility uses
the existing canonical camera buckets; gameplay remains independent of width
and FPS. Successful native fire/ice casts set the recent-spell marker for two
player ticks. The placed handler observes it at its normal world update slot.
The switch handler has controlled marker checks; full later-level switch-prompt
integration is not established. Pause/hidden/frozen handler gates and original
animation-clock behavior are covered by fixtures.

Removed prompts stay allocated but inactive in the native owner. Their placed
removal markers participate in source lookup and checkpoint snapshots. Type 21
normally reconstructs on Restart/Continue, unlike persistent pickups/checkpoints;
a flag-512 removed UID remains a separate filter. UIDs 267/265/271 reveal why
250 persistence slots must not be treated as a maximum UID. The desktop codec now
accepts removed UIDs through 32767 and tests UID 267 round trips. Existing version-1
saves remain readable; no original RMS compatibility is implied.

The [linked-removal pass](NATIVE_PROMPT_REMOVAL.md) connects type-40 UIDs
43/61 to chandeliers 3/34 and removes prompts 187/59 on source overlap. This
runs alongside the placed prompts' self-removal behavior. NATIVE_INTRO.md
implements script 76's movement/flag/animation ownership of prompt 267.

## Verification

`PromptCheck.java` runs in default regeneration's isolated lifecycle harness
with the existing analysis adapters. Nine shipped placements and thirteen
controlled cases produce 704 original/reference expectations and matching native
rows over sixteen ticks, before/after animation. Cases include inclusive edges,
point/miss/pause/hidden/frozen conditions, delayed removal, correct/wrong magic,
contained-point acknowledgement and switch-marker handling. The harness checks
actor registry membership after removal; the original clears its registry slot
and bounds without immediately shrinking actorCount.

```
python tools/check_native_prompts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

Seven milestones and 24 host runs cover 240/569, 30/60/144 FPS and interpolation
on/off. `--prompt-script` starts at the first-level spawn and walks through two
placed hints, then returns; initial damage immunity isolates those contacts.
`--game-prompt-script` goes through startup, seeds magic ownership/MP and damage
immunity beside enemy 19, and explicitly relocates the shipped fire prompt to
(440,200). Actual K input charges/spends 15 MP/casts fire; the hint acknowledges,
then disappears after normal rightward movement leaves its rectangle. This is
an explicitly controlled integration fixture, not a route recording to the
unmodified magic-hint location. Simulation diagnostics and per-width render CRCs
agree across display settings.

This pass added twelve data-free assertions and three CLI guards (451 player
assertions and 86 CTests at that point). Save codec tests additionally round-trip high prompt UIDs.
The full game remains unfinished. Intro script 76/trigger 82, script 160/trigger
161, linked prompt-removal triggers 43/61 and [cameras 63/167](NATIVE_CAMERA_CONTROLLERS.md)
are now complete. [Exit 173/completion UI](NATIVE_COMPLETION.md) is also implemented;
[first-level progression](NATIVE_FINISH.md) is also implemented. See ROADMAP.md
and PORT_ROADMAP.md for current checklists.


Validation on 2026-10-29: default Java regeneration passed compilation with zero
errors, ReferenceCheck's 4218 comparisons and all fixtures including PromptCheck.
Debug and Release passed all 86 CTests, seven prompt milestones and 24 prompt
host runs. Release matched all 704 regenerated prompt rows; existing combat,
world, progression, checkpoint/restart, beam, startup/English story/HUD/pause
regressions passed. Desktop save/Continue checks passed in both builds. Ignored
portrait/widescreen captures show the keyboard captions. The asset-free package
was rebuilt from Release.

## Second-level prompts

The five shipped level-1 prompts (UIDs 495/499/75/85/84, animations 17/6/14/14/18) match the original in 25 added
scenarios (oracle 1504 rows); see [NATIVE_LEVEL1_VARIANTS.md](NATIVE_LEVEL1_VARIANTS.md).
