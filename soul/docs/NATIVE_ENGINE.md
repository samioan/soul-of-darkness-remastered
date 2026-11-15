# Translated engine: the game as native code

`soul_of_darkness.exe` runs the complete original game (all eleven levels, menus, cutscenes,
bosses, upgrades, transformations, saves, music and sound effects) as native C++. It is not an
emulator: the verified reference Java in `soul/src` is translated to C++ mechanically and linked
against a small runtime. What the 1.0.1 build is, its decompiler fixes and its verification
numbers are in [BUILD_101.md](BUILD_101.md).

## Pipeline

```
soul/src/*.java  (generated reference, unchanged)
   + tools/port_patches.py   asserting desktop patches (exact text, exact match counts)
   + tools/port_java/*.java  PortConfig (wide view, layers), PortMenu (OPTIONS screen), PortNative (stub)
   -> tools/j2cpp/J2Cpp.java  Java -> C++17, driven by javac's attributed syntax trees
   -> soul/port/gen/          generated C++ (tracked, do not edit)
soul/port/engine/   jrt (Java core), midp (Graphics/Image/RMS/Player), engine_glue, host_win32
```

Regenerate with `python tools/gen_port.py` after changing `names.map`, the reference fixes, the
port patches or the port Java. `--vanilla` writes an unpatched translation to
`soul/analysis/gen_vanilla` for comparison.

### What the translator preserves

`J2Cpp` reads types and symbols from javac itself, so nothing is guessed. Java semantics that
differ from C++ are made explicit in the output:

- strict left-to-right evaluation of operands, call arguments and array stores; where order is
  observable the expression is emitted as a sequenced lambda;
- compound assignment reads its target before evaluating the right-hand side;
- masked shift counts, `>>>`, division by zero and `INT_MIN / -1`;
- array bounds, null dereference and negative array size raise Java exceptions that the game's
  own `catch (Exception)` blocks see, as on the phone;
- string literals are interned (the game compares softkey labels with `!= ""`);
- labeled break/continue, switch-scoped locals, static initialization order;
- objects and arrays are reference counted; a method keeps its receiver alive.

The runtime implements only what the six classes use: `String`, `StringBuffer`, `Random` (the
exact `java.util.Random` generator), `Hashtable`, `Vector`, the stream classes, and MIDP
`Canvas`/`Graphics`/`Image`/`Font`/`RecordStore`/`Player` drawing into software ARGB surfaces
with a built-in PNG decoder. Gameloft's promotion screens (`InGamePromotion`) are replaced by a
disabled stub.

### Game loop

`Game.run()` ticks every 50 ms (66 ms while a cutscene script runs) and passes the frame time to
the game. The glue feeds exactly those values and holds the 2.5 s wall-clock stall watchdog at
zero, so the simulation depends only on ticks. Collision, animation events, timers and saves stay
on ticks; presentation is independent (below).

## Equivalence with the original bytecode

`python tools/check_engine.py` runs the **original class files** on a JVM
(`tools/reference_checks/EngineOracle.java`, drawing stubbed out) and the built
`soul_engine_check.exe` side by side: same `Random` seed, same scripted key events, one
`Game.paint` per tick with the same loop inputs. After every tick both print a fingerprint of
every static field of `Game`, `Actor` and `BSprite` and every field of every live actor, in
class-file order. The first differing tick fails the run and lists the differing fields.

Scenarios boot through the sound prompt, title, menu, prologue and level map, enter one of the
eleven levels (later ones by setting `Game.maxUnlockedLevel` on both sides and walking the
original map), then mash a random mix of movement, jump, attack, magic, weapon-switch, softkey
and cheat keys. Results are in [BUILD_101.md](BUILD_101.md#verification-2026-11-15).

Limits of this evidence, stated plainly:

- It compares game state, not pixels. Rendering was checked by eye on captured frames, not
  against a phone or emulator.
- Fuzzed input explores the first rooms of each level thoroughly and deeper rooms only by
  chance. No complete start-to-credits playthrough has been recorded, by a person or a script.
- The promotion class is stubbed identically on both sides.
- Widescreen and the OPTIONS screen are outside the comparison by construction (below).

## Desktop changes

All of them are in `tools/port_patches.py` and `tools/port_java`. Two rules keep them honest:
no field is added to `Game`/`Actor`/`BSprite`, and every helper is a no-op at width 240 with the
original menus. That configuration is what `check_engine.py` measures.

### Widescreen

The game's camera keeps its original meaning, the left edge of a 240 pixel window, and every
original camera, region and controller rule runs unchanged. The wide view is derived from it
(`PortConfig.computeView`): centred on the 240 window, clamped to the same room bounds the camera
obeys, and centred with black bars when a room is narrower than the screen. When the camera
crosses into a neighbouring room the view slides at most 48 pixels per tick; jumps larger than a
screen snap.

World drawing is translated by `cameraX - viewX`, so the original `world - cameraX` draw code is
untouched. The three tile layers (far background, parallax middle, main map) widen independently;
the far layer bypasses its 260x340 scroll buffer when the view is wide. Full-screen fills, the
world clip and on-screen tests are changed explicitly, site by site. Panels, dialogue, menus and
the boss bar keep their 240 layout, centred; the bottom icon bar keeps its 240 layout, centred,
over a full-width strip.

The visible-area bounds and the activation distance follow the visible width, so in widescreen
enemies wake, spawn at and leave the real screen edges. That is a deliberate gameplay difference
from the 240 original and cannot be checked against it; at 240 nothing changes.

### Frame rate and interpolation

Presentation is independent of the tick. The original draw code changes state (animation
advance, timers, random numbers inside `Actor.paint`), so it is never re-run between ticks.
Instead each tick's draw calls are recorded and tagged by layer (tiles, world, one per actor,
screen); in-between frames replay them with each layer moved part of the way back to where it was
drawn on the previous tick. Layers that jumped (more than 48 px for an actor, 64 px for the world)
are shown in place, which covers passages, restarts and scene cuts. HUD and panels never move.

`soul_engine_check --verify-replay` confirms that replaying at alpha 1 reproduces the tick's own
frame exactly. Interpolation moves whole layers by whole pixels; it does not invent animation
frames. The `native` frame-rate setting shows one frame per tick with interpolation off.

### Input, audio, saves, data

- Keyboard and XInput gamepad map to the original phone keys by context
  (`sodapi::inputContext`): the same button jumps in play and confirms in menus and dialogue.
  Bindings are edited on the OPTIONS screen or in `settings.ini`; digits and the numpad remain
  the phone keypad, so the original cheat sequence still works.
- The OPTIONS screen (`tools/port_java/PortMenu.java`, game state 40) is reached from an entry
  the host installs in the main and pause menus and is drawn with the game's font, framed panel
  and softkey buttons. Headless checks never install it.
- Music and effects (the original MIDI and WAV) play through Windows MCI with volume and mute.
- The save is the original RMS record, stored as `%LOCALAPPDATA%\SoulOfDarkness101\dvseed.sav`.
- Game data is read directly from the user's JAR (SHA-256 checked, other 240x320 builds offered
  with a warning) or from an extracted folder.

## Not done

- A recorded complete playthrough, and a human pass over every level, boss and the ending in each
  display mode. Fullscreen, live resize, gamepad and audio output were exercised only as far as
  an unattended run allows.
- Touch input (pointer events) is not wired to the mouse.
- Sub-pixel or animation-frame interpolation; per-effect layers.
- Non-Windows hosts. The engine and runtime are portable C++17; the host is Win32.
- Publishing: no destination is configured.
