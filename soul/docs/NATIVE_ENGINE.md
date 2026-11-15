# Translated engine: the full game as native code

**Update 2026-11-15:** `soul_of_darkness.exe` now plays the full-featured 1.0.1 build
(`soul_full/`, see [BUILD_101.md](../../soul_full/docs/BUILD_101.md) for what that build is, its
decompiler fixes and its verification numbers). The 1.3.7 build described below still builds as
`soul_of_darkness_v137.exe` and still passes its checks. The translator, runtime, widescreen
model and interpolation are shared; what differs for 1.0.1 is noted in BUILD_101.md: three tile
layers, a 50/66 ms tick, an in-game OPTIONS screen instead of F-keys only, and no need for the
English text overlay. Commands: `python tools/build_full.py`, `python tools/gen_port.py`,
`python tools/check_engine.py` (add `--game v137` to the last two for this document's build).

Added 2026-11-14. `soul_of_darkness.exe` runs the complete original game, all nine
levels, menus, cutscenes, bosses, saves and music, as native C++. It is not an
emulator and it is not the hand-ported `soul_port` slice: the verified reference
Java in `soul/src` is translated to C++ mechanically and linked against a small
runtime.

## Why a translation

The hand port reached one level in sixteen verified passes; eight levels, two
bosses and a dozen handler families remained. The reference Java already
compiles and is checked against the original bytecode, so translating it whole
gives every handler at once and lets the result be compared with the original
tick for tick. The per-handler native modules and their oracles stay in the tree
as independent evidence and analysis tooling; they are no longer the route to a
playable game.

## Pipeline

```
soul/src/*.java  (generated reference, unchanged)
   + tools/port_patches.py   asserting desktop patches (exact text, exact match counts)
   + tools/port_java/*.java  PortConfig (wide view, layers), PortEnglish (text), PortNative (stub)
   -> tools/j2cpp/J2Cpp.java  Java -> C++17, driven by javac's attributed syntax trees
   -> soul/port/gen/          generated C++ (tracked, do not edit)
soul/port/engine/   jrt (Java core), midp (Graphics/Image/RMS/Player), engine_glue, host_win32
```

Regenerate with `python tools/gen_port.py` after changing `names.map`, the
reference patches, the port patches or the port Java. `--vanilla` writes an
unpatched translation to `soul/analysis/gen_vanilla` for comparison.

### What the translator preserves

`J2Cpp` reads types and symbols from javac itself, so nothing is guessed. Java
semantics that differ from C++ are made explicit in the output:

- strict left-to-right evaluation of operands, call arguments and array stores;
  where order is observable the expression is emitted as a sequenced lambda;
- compound assignment reads its target before evaluating the right-hand side;
- masked shift counts, `>>>`, division by zero and `INT_MIN / -1`;
- array bounds, null dereference and negative array size raise Java exceptions
  that the game's own `catch (Exception)` blocks see, as on the phone;
- string literals are interned (the game compares softkey labels with `!= ""`);
- labeled break/continue, switch-scoped locals, static initialization order;
- objects and arrays are reference counted; a method keeps its receiver alive.

The runtime implements only what the six classes use: `String`, `StringBuffer`,
`Random` (the exact `java.util.Random` generator), `Hashtable`, the stream
classes, and MIDP `Graphics`/`Image`/`DirectGraphics`/`RecordStore`/`Player`
drawing into software ARGB surfaces with a built-in PNG decoder. Gameloft's
promotion screens (`InGamePromotion`) are replaced by a disabled stub.

## Equivalence with the original bytecode

`python tools/check_engine.py` runs the **original class files** on a JVM
(`tools/reference_checks/EngineOracle.java`, drawing stubbed out) and the built
`soul_engine_check.exe` side by side: same `Random` seed, same scripted key
events, one `Game.paint` per tick. After every tick both print a fingerprint of
every static field of `Game`, `Actor` and `BSprite` and every field of every
live actor, in class-file order. The first differing tick fails the run and
lists the differing fields.

Scenarios boot through the sound prompt, title, menu, prologue and level map,
enter one of the nine levels (later ones by setting `Game.maxUnlockedLevel` on
both sides and walking the original map), then mash a random mix of movement,
jump, attack, magic, weapon-switch, softkey and cheat keys.

| Build | Scenarios | Ticks compared | Result |
|---|---|---|---|
| Unpatched translation, Debug | 54, all nine levels | 237,353 | identical |
| Shipped engine (port patches), Release, width 240, original text | 45, all nine levels | 197,897 | identical |

States visited include logo/loading, sound prompt, title, menu, prologue, level
map, level loading, gameplay, pause, game over and exit. The check found one
real defect while it was being built (string-literal interning).

Limits of this evidence, stated plainly:

- It compares game state, not pixels. Rendering was checked by eye on captured
  frames for all nine levels, not against a phone or emulator.
- Fuzzed input explores the first rooms of each level thoroughly and deeper
  rooms only by chance. No complete start-to-credits playthrough has been
  recorded, by a person or a script.
- The promotion class is stubbed identically on both sides.
- Widescreen and English are outside the comparison by construction (below).

## Desktop changes

All of them are in `tools/port_patches.py` and `tools/port_java`. Two rules
keep them honest: no field is added to `Game`/`Actor`/`BSprite`, and every
helper is a no-op at width 240 with original text. That configuration is what
the Release row above measures.

### Widescreen

The game's camera keeps its original meaning, the left edge of a 240 pixel
window, and every original camera, region and controller rule runs unchanged.
The wide view is derived from it (`PortConfig.computeView`): centred on the 240
window, clamped to the same room bounds the camera obeys (`Game.R`/`S`), and
centred with black bars when a room is narrower than the screen (the tower
level is about 280 pixels wide). When the camera crosses into a neighbouring room the
view slides at most 48 pixels per tick; jumps larger than a screen snap.

World drawing is translated by `cameraX - viewX`, so the original
`world - cameraX` draw code is untouched. Explicitly changed sites: the world
clip, the tile layer (visible cells drawn directly instead of through the
260x340 scroll buffer), full-screen fades/flashes/letterbox bars, rain, the
energy-barrier and hazard-area screen tests, and `Game.viewBounds`. Panels,
dialogue, menus and the boss bar keep their 240 layout, centred; the HUD stays
at the screen's left edge.

`viewBounds` and the activation distance in `Actor.isVisible` follow the
visible width. So in widescreen enemies wake, spawn at and leave the real
screen edges. That is a deliberate gameplay difference from the 240 original
and cannot be checked against it; at 240 nothing changes.

Checked at 427, 569 and 747: 27 fuzz runs of 8,000 ticks over all nine levels
raised no engine exception.

### Frame rate and interpolation

Simulation is always one `Game.paint` per 83 ms, as `Game.run` requested.
Presentation is independent. The original draw code changes state (animation
advance, timers, random numbers inside `Actor.paint`), so it is never re-run
between ticks. Instead each tick's draw calls are recorded and tagged by layer
(tiles, world, one per actor, screen); in-between frames replay them with each
layer moved part of the way back to where it was drawn on the previous tick.
Layers that jumped (more than 48 px for an actor, 64 px for the world) are
shown in place, which covers passages, restarts and scene cuts. HUD and panels
never move.

`soul_engine_check --verify-replay` confirms that replaying at alpha 1
reproduces the tick's own frame exactly: 0 mismatches over 145,001 world ticks
at 427/569/747 and 17,398 at 240/569, with three in-between frames replayed
per tick. Interpolation moves whole layers by whole pixels; it does not invent
animation frames.

### Input, audio, saves, data

- Keyboard and XInput gamepad map to the original phone keys by context
  (`sodapi::inputContext`): the same button jumps in play and confirms in menus
  and dialogue. Bindings are in `settings.ini`; digits and the numpad remain
  the phone keypad, so the original cheat sequence still works.
- Music is the original MIDI from pack 19, played through Windows MCI with
  volume and mute. The JAR contains no sound effects.
- The save is the original 1313-byte RMS record, stored as
  `%LOCALAPPDATA%\SoulOfDarkness\dvseed.sav`. It is separate from the earlier
  `soul_port` `checkpoint.sav`; nothing is migrated.
- Game data is read directly from the user's JAR (SHA-256 checked, other
  240x320 builds offered with a warning) or from an extracted folder.
- English text (`PortEnglish`) replaces the JAR's Czech strings; F7 switches
  back. Sprite art is untouched, so tutorial icons show phone keys and the HUD
  bars keep their original labels.

## Not done

- A recorded complete playthrough, and a human pass over every level, boss and
  the ending in each display mode. Fullscreen, live resize, gamepad and audio
  output were exercised only as far as an unattended run allows.
- In-game rebinding and display menus (settings are a file plus function keys).
- Sub-pixel or animation-frame interpolation; per-effect layers.
- Non-Windows hosts. The engine and runtime are portable C++17; the host is Win32.
- Publishing: no destination is configured.
