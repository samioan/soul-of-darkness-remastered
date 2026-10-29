# Native chandelier tutorial-removal links

Level 0 now constructs type-40 triggers 43 and 61. Their single source is a
chandelier (3 and 34 respectively); their single target is a placed prompt
(187 and 59). These are condition 1 / actor-target mode 0 / operation 5.
Camera controllers and the final exit/completion flow remain unfinished.

## Original evidence and behavior

The shipped records give trigger 43 a 30x30 rectangle at (1682,410) and trigger
61 a 30x30 rectangle at (128,1148). Actor.bh resolves source and target UIDs
lazily, retries missing links, and caches successful references. Condition 1
calls Actor.g(source), which refreshes the source bounds before testing inclusive
rectangle overlap. Point rectangles do not overlap. Source hidden/frozen flags
are not tested here: they can suppress the source handler without suppressing
this bounds check. This is not the condition-5 hidden-source branch.

Operation 5 calls Actor.o(target), then Actor.G removes the trigger and clears
its cached links. A missing target does not prevent a matching source from
consuming the trigger. The target cannot be recovered by adding it afterward.
A prompt that removed itself after being cached remains safe to remove again.
World pause and trigger flags 1024/16384 defer resolution and activation.

Inspected original b bytecode using `python tools/bytecode.py b`: bh's lazy
lookup, condition-1 dispatch at offset 394, operation-5 removal, g's bounds
refresh and o/G's removal paths agree with generated Java. Game.removeActor
also nulls an already constructed update slot; it does not dispatch the removed
prompt later that tick. No Java naming or decompiler patch change was needed.

## Native ownership

PromptRemovalTrigger supports this single-source/single-target branch. The
world constructs it only when the record links a placed type-50 chandelier to
a type-21 prompt. Other condition/effect combinations remain with their own
handlers or unsupported. Platforms live in the world's stable list; prompts
are loaded once before dispatch and retain their vector addresses. Cached
references never cross reconstructed worlds.

CombatWorld inserts the triggers in placed-record order, caches canonical
240x320 camera buckets, and uses the original strict type-40 radius of seven
with forced-update precedence. Removal hides the prompt immediately, clears
its bounds, and prevents its already queued handler/animation from running.
Queue inspection reports removed slots as -999, matching null original slots.
Existing self-removal counters remain counters of the placed prompt handler;
linked removal is visible through prompt/trigger liveness.

Removed prompts/triggers join checkpoint actor bookkeeping. The existing
original flag rules still apply: these shipped records lack persistent flag
512, so checkpoint reconstruction can create them again. No new disk format,
serialized cached pointer, or permanently removed tutorial UID was introduced.

## Verification

PromptRemovalCheck runs original and generated classes in isolated loaders
with the existing test ImageIO/RMS adapters; this is not a MIDP runtime.
There are 364 original/reference expectations and 352 native comparison rows:

- 312 handler rows use both shipped links across thirteen scenarios: normal,
  outside, exact edge, one-pixel miss, hidden source, pause, hidden/frozen
  trigger, late/missing source, late/missing target, and an already removed target.
- 40 rows invoke actual Game.W in four worlds: prompt before trigger, trigger
  before prompt, world pause across overlap, and a target excluded from the
  update queue. They compare liveness, prompt status and all update UID slots.
- Eight data-free assertions cover branch validation and paused/hidden/frozen/
  missing-link resolution. The player suite now has 483 assertions; CTest has 92 tests.

The existing chandelier checker now also requires prompt 59 and trigger 61 to
be removed while prompt 187/trigger 43 remain, across its 24 raw/game runs at
widths 240/569, 30/60/144 FPS and interpolation on/off. It retains 3000 script/
actor oracle rows and four intermediate host milestones. The placed-prompt
checker retains 704 rows, seven milestones and 24 host runs. These are controlled
fixtures, not a complete first-level route test.

Validation on 2026-10-29: default regeneration compiled with zero errors;
ReferenceCheck passed 4218 comparisons and every registered original/reference
fixture reported success. Debug and Release passed all 92 CTests and 483 player
assertions. Both builds matched the new oracle and passed the prompt/chandelier
host checks. Intro playback/skip, checkpoint/restarts and linked-area-trigger
regressions passed. The rebuilt data-free distribution passed package validation
and its asset tool matched the new 352-row oracle.

Validation commands:

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_prompt_removal.py --exe soul/port/build/Debug/soul_assets.exe
python tools/check_native_prompts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_chandelier.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

Oracles/logs remain in ignored analysis/. Camera controllers
63/167 are now implemented in [NATIVE_CAMERA_CONTROLLERS.md](NATIVE_CAMERA_CONTROLLERS.md).
[Exit 173/completion UI](NATIVE_COMPLETION.md) is also implemented.
Progression/save/advance is next. Full game completion and native gameplay beyond
level 0 remain pending.
