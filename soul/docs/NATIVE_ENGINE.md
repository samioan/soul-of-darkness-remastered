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
- wrapping int/long `+`, `-`, `*` and negation (through unsigned arithmetic; signed overflow is
  undefined in C++), masked shift counts, `>>>`, division by zero and `INT_MIN / -1`;
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
  chance. A person has played the desktop build to the end credits, but no playthrough has been
  recorded as input and replayed against the original.
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
the far layer bypasses its 260x340 scroll buffer when the view is wide. Where the far layer is a
repeating strip (levels whose far origin is `cameraX >> 6`) it repeats across the wide view. Where
it is one picture panned about 20 pixels across the level (`camera * (layer - window) / (level -
window)`, e.g. level 1's 280x360 moonlit sky), repeating it showed two or three moons; instead it
is scaled up uniformly, nearest neighbour, until the screen fits inside it with the same 20 pixel
slack (`PortConfig.drawFarFit`, cached per level), and panned across the level with the camera.

Parallax origins (`camera >> 3` for the middle layer, `camera >> 6` for a repeating far strip)
follow the wide view, not the camera (`PortConfig.parallax`). The view stands still while the camera
moves inside a room narrower than the screen, or within 164 pixels of a room edge; a camera-based
origin then slid the layer at about 7/8 of the camera's speed in the wrong direction. The layer now
scrolls as if the camera were the one that centres the original window in the view, which is the
camera itself whenever the view tracks it, so open stretches draw exactly as before.

Rooms and levels narrower than the screen (no horizontal scrolling: the level 6 tower, the level 10
arena, single-screen rooms inside wider levels) do not leave black bars and do not show tiles past
the room: their sides show a blurred, dimmed copy of the room as drawn this frame, a native pixel
pass recorded as a draw op (`PortNative.sides`, since MIDP colours are opaque), fading in from black
over 12 ticks when the sides first appear. Actors, collision and activation stop at the room edge.
Screens without the world (menus, the level map) drop the wide view so their tile layers keep the
240 layout; the logo screen's side bars are white. Full-screen fills, the
world clip and on-screen tests are changed explicitly, site by site. Panels, dialogue, menus and
the boss bar keep their 240 layout, centred; the bottom icon bar keeps its 240 layout, centred,
over a full-width strip. Upgrade and pickup messages, the ending's white fade and the closing
message before the credits cover the full width.

`BSprite` crops frames it decodes to pixel arrays (most actors) to x >= 0 before `drawRGB` and skips
those wholly left of it, a guard for the phone's 240 pixel screen. Inside the world layer x = 0 is the
left edge of the original window, so actors left of it in the wide view were cut at that line or not
drawn at all (the right side was never affected). The crop now uses the left edge of the surface in
the current translation (`PortConfig.screenLeft`, 0 at width 240).

The visible-area bounds and the activation distance follow the visible width, so in widescreen
enemies wake, spawn at and leave the real screen edges. Auto-scrolling sections hold the player
inside the visible area on every edge (two edges of the original used the 240 window directly). That is a deliberate gameplay difference
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

### Fixes to the original game

Some flaws are the original's own, confirmed in its bytecode. Their fixes change game state, so they
are active only when the host installs the desktop menus (`PortConfig.desktopFixes`, set with
`desktopText`) and are off in the configuration `check_engine.py` compares; `soul_engine_check
--options-menu` runs with them.

- Music: `Game.U()` replays the boss theme whenever the last music played (`fN`) was the boss theme,
  and only the main menu plays a different music id directly. After a boss the next level therefore
  started with boss music (first visible going from level 1 to level 2, which share a theme). Loading
  a level forgets the boss theme.
- Upgrade window: its black panel, `fillRect(19, 20, 203, 250)`, stuck out 12 pixels above the frame
  and left the frame's bottom 8 pixels open; it now fills the frame (x 20..220, y 32..281).
- Music after a jingle: `playSound` skips a request for the music id last started (`fN`) while the
  one Player is busy, but jingles (death, pickup, level end) play on that Player without changing
  `fN`. Restarting after a death before the death jingle ended (`ag` -> `aF` -> `a(8)` -> `U`, with no
  stop on the way) skipped the level or boss music, which stayed off until a pickup's jingle and
  `U()` restarted it. The skip now also requires the Player to be playing that id.
- Upgrade screen message: `bb` (a message is up) is cleared when it is dismissed or the screen is
  left normally, but leaving the screen that follows a level's end returns before that (`e.ak`:
  `c(Z)` and return at 708..715, `bb` cleared at 734). Leaving it with Back while a message showed
  made the next upgrade screen open with that stale message ("no soul shards left") already up.

### Input, audio, saves, data

- Keyboard and XInput gamepad map to the original phone keys by context
  (`sodapi::inputContext`): the same button jumps in play and confirms in menus and dialogue. The
  upgrade screen pours points into a stat only while FIRE (or keypad 5) is held, so Confirm and
  Attack send FIRE there, as in dialogue, rather than the left softkey of the other menus.
  Bindings are edited on the OPTIONS screen or in `settings.ini`; digits and the numpad remain
  the phone keypad, so the original cheat sequence still works.
- The OPTIONS screen (`tools/port_java/PortMenu.java`, game state 40) is reached from an entry
  the host installs in the main and pause menus and is drawn with the game's font, framed panel
  and softkey buttons. Headless checks never install it.
- Music and jingles (the original MIDI, and WAV) play through a small SMF sequencer on its own
  thread sending to the system MIDI synthesizer (`engine/host_midi.cpp`); WAV plays through
  PlaySound. The earlier MCI path opened every track synchronously and stalled the game whenever
  music started or a jingle interrupted it. Tracks play as the game mixed them (the game sets no
  volume; its level themes are about 20 dB quieter than the boss theme and jingles); only the
  user's volume and mute scale their channel volumes.
- Arcs are filled only inside the clip with half-plane wedge tests: the boss-death effect draws
  eleven 640x640 arcs per frame, which took about 75 ms per frame with per-pixel `atan2` over the
  whole box and now takes about 6 ms, with identical pixels.
- The save is the original RMS record, stored as `%LOCALAPPDATA%\SoulOfDarkness101\dvseed.sav`.
- Game data is read directly from the user's JAR (SHA-256 checked, other 240x320 builds offered
  with a warning) or from an extracted folder.

## Not done

- A recorded, replayable playthrough checked against the original, and a human pass over every
  level, boss and the ending in each display mode. Fullscreen, live resize, gamepad and audio output were exercised only as far as
  an unattended run allows.
- Touch input (pointer events) is not wired to the mouse.
- Sub-pixel or animation-frame interpolation; per-effect layers.
- Non-Windows hosts. The engine and runtime are portable C++17; the host is Win32.
- A first published release; the launcher's self-update has not yet been exercised against one.
