# Soul of Darkness 1.0.1 (HTC P3450 build): the full game

Added 2026-11-15. This is the build `soul_of_darkness.exe` now plays.

- Input: `roms/SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101_S8000.jar`, 632754 bytes,
  SHA-256 `3da9ea7fe8dfaa2c090e8b3714eaed15127f51dc8b67ed9ef9fed51172e4bea4`,
  MIDlet-Version 1.0.1, plain MIDP 2.0 (no Nokia UI), 240x320.
- Pipeline: `python tools/build_full.py` (extract, decompile, rename with
  `soul_full/docs/names.map`, apply decompiler-artifact fixes, compile-check), then
  `python tools/gen_port.py` (port patches + translation to `soul/port/gen_full`).
- How the translated engine, widescreen, interpolation and checks work:
  [soul/docs/NATIVE_ENGINE.md](../../soul/docs/NATIVE_ENGINE.md).

## How it differs from the 1.3.7 JAR

Same engine and six-class layout (a InGamePromotion, b Actor, c ErrorReporter, d BSprite,
e Game), different obfuscation, and a much larger game. Measured from the two JARs:

| | 1.0.1 (this build) | 1.3.7 |
|---|---|---|
| Language of the text in the JAR | English | Czech |
| Decompiled Actor / Game | 22,236 / 8,845 lines | 12,087 / 6,114 lines |
| Gameplay sprites (pack 2) | 52, 339 KB | 28, 87 KB |
| Background tile sets | 18 sprites, 188 KB | 6 sprites, 38 KB |
| Tile layers drawn | far background, parallax middle, main map | one map |
| Music / effects | 10 MIDI, 3 WAV | 5 MIDI |
| Chapters named in the text | 11 (Summon ... Salvation) plus a prelude | 9 used |
| Pack storage | LZMA, decoder inside Game | uncompressed |
| Simulation tick | 50 ms (66 ms during cutscene scripts), frame-time input | 83 ms |
| Input | keys and touch (pointer events, on-screen icon bar) | keys |

Features present only here, as named by its own strings and screens: a different story with
Lydia and Ritter; weapon upgrades bought with soul shards (power, combo, critical, magic
levels, opened with the right softkey); critical hits; three transformations (fish, beetle,
water elemental) used at blue altars; a carousel main menu and gem-button interface.

So the 1.3.7 JAR is a cut-down edition, not a later revision of the same content.

## Decompiler artifacts fixed (tools/build_full.py)

All are asserting patches with their bytecode evidence in the script.

1. `if (!var3)` on an int flag in the camera update, and the script-tick post-increment:
   the same two artifacts 1.3.7 had.
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

`python tools/check_narrowing.py soul_full` recompiles the reference and compares, per method,
narrowing-instruction counts and call descriptors with the original class files; it is what
located every site in 3 and 4. Its remaining findings are understood and harmless: three
redundant `(byte)` casts on small constants in the LZMA decoder, and inherited-method owner
names (`getAppProperty`, `getWidth`, `repaint`) that javac records differently.

## Verification (2026-11-15)

Same method as 1.3.7: original class files on a JVM versus the built engine, identical seed
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

Not established: a complete playthrough; pixel equivalence with a phone; deeper rooms, bosses
and the ending, which fuzzing reaches only by chance. The transformation and upgrade systems
were exercised only as far as random input happened to trigger them.

## Names

`names.map` renames the five classes and the ~45 members the glue, oracle and port patches
refer to; each is the positional counterpart of the 1.3.7 member of the same name. Everything
else keeps its obfuscated name. Port patches therefore quote obfuscated text, and
`tools/gen_port.py` fails loudly if a regenerated reference no longer matches them.

## Desktop additions specific to this build

- **OPTIONS screen** (`tools/port_java_full/PortMenu.java`): game state 40, reached from a
  menu entry the host installs in the main and pause menus. Drawn with the game's font, framed
  panel and softkey buttons. Pages: display, audio, keyboard, gamepad (rebinding by capture).
  The headless checks never install it, so the original menus are what gets compared.
- Three tile layers widen independently; the far layer bypasses its 260x340 scroll buffer when
  the view is wide. The bottom icon bar keeps its 240 layout, centred, over a full-width strip.
- Tutorial text that names "the right soft key" shows the key bound to that action instead.
  Hints that name keypad digits are left alone: the digits work on the number row.
- Touch input (pointer events) is not wired to the mouse.
