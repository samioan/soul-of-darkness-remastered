# Soul of Darkness 1.0.1 (HTC P3450 build): the full game

This is the build `soul_of_darkness.exe` plays, and the only one the project supports.

- Input: `roms/SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101_S8000.jar`, 632754 bytes,
  SHA-256 `3da9ea7fe8dfaa2c090e8b3714eaed15127f51dc8b67ed9ef9fed51172e4bea4`,
  MIDlet-Version 1.0.1, plain MIDP 2.0 (no Nokia UI), 240x320.
- Pipeline: `python tools/build_reference.py` (extract, decompile, rename with
  `soul/docs/names.map`, apply decompiler-artifact fixes, compile-check), then
  `python tools/gen_port.py` (port patches + translation to `soul/port/gen`).
- How the translated engine, widescreen, interpolation and checks work:
  [NATIVE_ENGINE.md](NATIVE_ENGINE.md).

## The build

Six classes: the GloftSOFD MIDlet and a InGamePromotion, b Actor, c ErrorReporter, d BSprite,
e Game. English text; three tile layers (far background, parallax middle, main map); ten MIDI
tracks and three WAV effects; LZMA-compressed packs with the decoder inside Game; a 50 ms tick
(66 ms during cutscene scripts) with frame-time input; keys and touch (pointer events, on-screen
icon bar). Eleven chapters (Summon ... Salvation) plus a prelude, with Lydia and Ritter; weapon
upgrades bought with soul shards (power, combo, critical, magic levels, opened with the right
softkey); critical hits; three transformations (fish, beetle, water elemental) used at blue
altars; a carousel main menu and gem-button interface.

## Decompiler artifacts fixed (tools/build_reference.py)

All are asserting patches with their bytecode evidence in the script.

1. `if (!var3)` on an int flag in the camera update, and the script-tick post-increment
   (`dup_x2` keeps the old value for the local while the incremented one is stored).
2. `return (Integer)var1;` printed as auto-unboxing (the bytecode calls `intValue`).
3. **Narrowed locals** (`WIDEN`, 18 declarations): Vineflower typed int locals as `byte`/`short`
   where only small constants were stored. Compound assignments then truncate in Java source
   but never did in the original. One of them sits in the built-in LZMA decoder and stopped
   every pack from loading.
4. **Overload selection** (`OVERLOADS`, 21 call sites): the game has two save-buffer writers
   named `a`: `(byte[],int,int)` stores one byte, `(byte[],int,short)` two. The original calls
   the one-byte writer with byte-typed arguments; in Java source those arguments select the
   short overload, which overwrites the next save byte. `tools/check_engine.py` caught this as a
   single differing cell (save offset 805) at tick 1041 of one scenario.

`python tools/check_narrowing.py` recompiles the reference and compares, per method,
narrowing-instruction counts and call descriptors with the original class files; it is what
located every site in 3 and 4. It also compares constructors and the keyword-renamed `do_()`
(both were silently skipped until 2026-11-15). Its six remaining findings are understood and
harmless: three redundant `(byte)` casts on the constants 7-11 in the LZMA decoder (the local
they feed was widened to `int` by `WIDEN`), and inherited-method owner names (`getAppProperty`,
`getWidth`/`getHeight`, `repaint`, `setFullScreenMode`) that javac records under the subclass.

A wider per-method comparison (2026-11-15, not a checked-in tool) of every compiler-independent
operation (int/long arithmetic, shifts, logic, conversions, typed array loads and stores, field
writes) between the original class files and the recompiled reference found no other
difference than those casts and two compound assignments on array elements
(`a[i] += x` printed as `a[i] = a[i] + x`, side-effect-free index, in `Game.ak`/`Game.ar`).

## Verification (2026-11-15)

Original class files on a JVM versus the built engine, identical seed
and scripted keys, fingerprint of every Game/Actor/BSprite static and every live actor field
after each tick. The oracle feeds the frame time the original loop would (50 or 66 ms) and
holds the wall-clock stall watchdog at zero on both sides.

| Build compared | Scenarios | Ticks | Result |
|---|---|---|---|
| Unpatched translation | 44, all 11 levels | 202,630 | identical |
| Shipped Release engine, width 240, original menus | 44, all 11 levels | 187,499 | identical |

States reached: loading, sound prompt, title, menu, help, credits, prologue, level map,
gameplay, pause, game over, weapon upgrade (31), exit. Widescreen and interpolation: 22 runs
of 6,000 ticks over all 11 levels at 240/427/569/747 raised no engine exception, and replaying
82,400 recorded world ticks at alpha 1 reproduced each tick's frame exactly.

Those runs reported `MIDlet-Version` as "1.3.7" on both sides (a leftover from an earlier
target); the About screen now shows the JAR's own "1.0.1", runtime and oracle alike. Rechecked
after that change with the default `check_engine.py` run only: 6 scenarios, 13,732 ticks,
identical.

Audit (2026-11-17, after the v0.1.3/v0.1.4 desktop changes): the default run (6 scenarios,
13,732 ticks) and 22 further scenarios over all 11 levels (80,611 ticks) identical; regenerating
the reference from the JAR changes nothing in `soul/decompiled` or `soul/src`, which contain no
decompiler failure markers; `check_narrowing.py` reports only the six findings above. A translation
built without the port patches (`gen_port.py --vanilla`) and the shipped engine at width 240 with the
original menus rendered 620 frames from 22 scenarios pixel for pixel identically, with identical
state traces: at 240 the desktop patches change neither state nor pixels.

Not established: a recorded playthrough replayed against the original (a person has played the
desktop build to the credits); pixel equivalence with a phone; deeper rooms, bosses
and the ending, which fuzzing reaches only by chance. The transformation and upgrade systems
were exercised only as far as random input happened to trigger them.

## Names

`names.map` renames the five classes and the ~45 members the glue, oracle and port patches
refer to. Everything else keeps its obfuscated name. Port patches therefore quote obfuscated text, and
`tools/gen_port.py` fails loudly if a regenerated reference no longer matches them.

## Desktop additions

- **OPTIONS screen** (`tools/port_java/PortMenu.java`): game state 40, reached from a
  menu entry the host installs in the main and pause menus. Drawn with the game's font, framed
  panel and softkey buttons. Pages: display, audio, keyboard, gamepad (rebinding by capture).
  The headless checks never install it, so the original menus are what gets compared.
- Three tile layers widen independently; the far layer bypasses its 260x340 scroll buffer when
  the view is wide. The bottom icon bar keeps its 240 layout, centred, over a full-width strip.
- Tutorial text that names "the right soft key" shows the key bound to that action instead.
  Hints that name keypad digits are left alone: the digits work on the number row.
- Touch input (pointer events) is not wired to the mouse.
