# Second-level intro: trigger 134 and script 133

The second level opens with a short scripted scene, like level 0's trigger 82/script 76. Kale's
spawn (1391,816) already overlaps the 30x30 type-20 trigger 134 at (1376,791), so the scene starts on
the first world tick. The native intro owner was generalized to run both scripts. Normal gameplay still
constructs level 0 only: `LevelDescriptor::openingScripts()` remains level 0, so the frontend does not load
this scene yet.

| Track | Owner | Commands |
| --- | --- | --- |
| 0 | camera (kind 1) | command 12 world-focus keys at ticks 0, 12 and 20 |
| 1 | player 131 | position, pose, facing flag 23/24, two dialogues |
| 2 | type-1 enemy 215 (zombie) | position, pose, flag 23 `0x20000000`, two dialogues |
| 3 | type-2 flyer 135 (behavior 0) | position, pose, flag 23 `0x20000000`, two dialogues |

Opcodes 12/21/22/23/24/116 only, all already supported by the interpreter. The trigger has the same
record shape as trigger 82 (animation 8, count 12, skip operand 0 meaning skippable) with script UID 133.

## Original evidence

- Dialogues use string table 3 (seven Czech entries; entry 1 is unused) and common speaker entries
  `77+n` of table 0 (0 Kale, 3 Zombie, 4 Rarach). The timeline, by script tick: 26 Kale (entry 0), 41 Rarach
  (6), 47 Zombie (2), 54 Rarach (3), 58 Zombie (4), 61 Kale (5). Each dialogue pauses the script counter.
- Command 23 with operand `0x20000000` adds flag 8192 (`flags |= v>>16`). In the original 8192 makes an actor
  respawnable in `isRespawnable()` (already true for these actors) and makes `Game.removeActor` hide it
  (flag 1024) instead of deleting it; the scene itself never removes an actor. The native enemies keep the raw flag.
- Original `Game.updateScript`/`executeScriptOpcode` semantics (old-counter read, equality-gated extended
  commands, 23/24 flag promotion) are unchanged from NATIVE_INTRO.md.

## Native implementation

- `IntroTrigger` accepts script UIDs 76 and 133 and keeps its own `scriptUid`.
- `IntroCutscene` pins each script's track layout (76: enemy 19, flyers 83/84, camera, player 186, prompt 267;
  133: camera, player 131, enemy 215, flyer 135). Dialogue ids 0..3 are the table-2 entries; table-3 entries 0..6
  are ids 4..10, with a new `RARACH` speaker.
- `CombatWorld::loadIntro` picks the script named by the single placed trigger. Actor resolution reuses
  the existing player/enemy/flyer bindings.
- English text is a desktop translation of the Czech strings (not an official localization); speaker names
  (Kale, Zombie, Rarach) are kept. Entry 5 ("I will tell you how great our bomb is!") is never shown.

## Verification

- `LevelOneIntroCheck.java` runs the unchanged original and regenerated interpreter on the real level-1 map,
  actors and trigger for seven scenarios of 170 frames: normal, pause, skip at frame 0, tick 20, during
  the first dialogue and during the last, and a disabled skip. It compares the script counter, dialogue id,
  camera, dialogue anchor, every track offset and the position/flags/pose/facing/presence of player 131,
  zombie 215 and flyer 135. **4760 rows** match the native probe (1249 original/reference expectations);
  independent assertions require six dialogue acknowledgements, completion and the released input/camera flag.
- Eight data-free assertions (player suite now 575): English text/speaker, rejection of unknown text and
  speakers, trigger 134's script/rectangle, rejection of other script UIDs and pinned track layouts.
- `--level1-intro-world-manifest` drives a real `CombatWorld` with the shipped player, zombie 215, flyer 135 and
  trigger 134: the scene starts at tick 1 from the spawn overlap, shows six dialogues, consumes the trigger and
  leaves Kale at (1445,819), the zombie at (1528,819) with flags 8201 and the flyer at (1406,736) with flags 8200.
  Skipping after tick 20 ends at the same state much earlier; ten paused ticks delay everything by ten; a disallowed
  skip changes nothing.

```
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_level1_intro.py --exe soul/port/build/Debug/soul_assets.exe
python tools/check_native_intro.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

## Boundaries

- The scene is not wired into the host: no desktop camera/viewport/HUD run for level 1, so widths 240/569 are
  not exercised and the dialogue panel layout is shared with level 0 unchanged. Entering level 1 stays disabled.
- Flag 8192's removal exemption is not modeled in the enemy owners (the intro never removes actors).
- Dialogues are driven with explicit acknowledge/skip inputs, not the host's keyboard path; only the three
  intro actors are constructed.
