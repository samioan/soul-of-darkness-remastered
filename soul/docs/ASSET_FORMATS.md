# Asset formats: initial verified findings

This is a Gameloft engine, not the Metaflow RP container used by Betrayal and
Clone Home. The standalone game assets are numeric packs, not loose PNG
atlases. Tools validate the format below from the actual Java loaders.

## Numeric packs 1..19

`e.a(String)` opens a pack. `t()` reads a little-endian u16; `u()` reads a
little-endian 32-bit word. Header:

| Field | Encoding | Meaning from loader |
|---|---|---|
| dU | u16 LE | Total resource slots |
| dW | u16 LE | Number of pack parts (all supplied packs: 1) |
| dX[dW] | u16 LE each | First slot index of each part (all supplied packs: 0) |
| dV[n+1] | u32 LE each | Absolute offsets within the current part; final entry is end offset |

The offset table has one entry per slot plus a sentinel. Its first entry
equals the end of the header/table. Adjacent equal offsets denote absent
entries. For nonempty entries, `e.o(int)` seeks to the offset, consumes one
prefix byte using `s()`, and returns `nextOffset - offset - 1` payload bytes.
All observed prefixes are zero; their wider semantics are not yet known.
The Java loader names subsequent parts `packName.partIndex`, but this input
has no split packs. The initial validator intentionally rejects split input.

`python tools/inspect_assets.py` validates all 19 tables, monotonic offsets,
file bounds, prefix values and exact consumption: 222 slots, 170 present.
Duplicate tables/bytes in packs 12 and 13 are preserved. File `0` is one zero
byte and is not parsed as a container; its role still needs call-site evidence.
The native `soul_resources` library now implements the checked single-part
numeric and promotion containers too. NATIVE_RESOURCES.md records its API,
data-free tests and 252-row comparison with the independent Python readers.

## dataIGP

From `a.e()` and `a.b(int)`: u16 LE offset count, that many u32 LE offsets,
then payload. Offsets are relative to payload start (`2 + 4*count`). Entries
are adjacent offset ranges, with the final offset a sentinel. The supplied
file has 11 offsets, 10 ranges, and consumes all 50896 bytes. This data serves
the in-game promotion system and should not be confused with level content.

## Text, trigonometry and save boundary (core read-through)

Pack 15's resource IDs are string-table indices, not language variants. Resource
0 is base UI text; resources 2..10 supply level-associated text and resource 1
is absent. The Java string loader skips six bytes, reads a big-endian short
count, then that many DataInputStream.readUTF strings. Values are uppercased
and trimmed. There are 136 strings across 10 nonempty tables, all consumed
exactly and compared against the original bytecode by the reference checker.
The text appears Czech despite the input filename's EN label. Some accented
characters use glyph substitutions; preserve the stored codepoints until the
BSprite font mapping is decoded. Do not use a replacement-error UTF-8 decoder.

Pack 17 resource 0 contains 256 little-endian signed 32-bit trigonometric
samples (1024 bytes). Quadrant values are +256,0,-256,0. Original `e.k(angle)`
normalizes negative degrees, indexes `(angle % 360) * 256 / 360`, and returns
cosine scaled by 256. `e.l(angle)` is `k(90-angle)`, hence sine. These are
renamed `cos256` / `sin256`; quadrant and original/reference comparisons are
part of regeneration.

The RMS buffer has 1313 bytes. `accessSaveRecord(true)` creates or replaces
record 1 of `/dvseed.sav`; false reads an existing record into that buffer.
Only an existing-record read sets `saveLoaded`. Offsets 14/15 provide saved
level/unlock indices and 16 is a signed LE progress marker used by New Game
and Continue. SAVES.md, CHECKPOINT_LIFECYCLE.md and NEW_GAME_AND_CONTINUE.md
now document the save layout and selected integration checks; no production
native save backend is implemented yet.

## BSprite: validated

`d.a(byte[],int)` checks a u16 LE version of 991 (`0x03DF`), followed by
32-bit feature flags and flag-dependent module/frame/animation/palette data.
The decoder renders through Image.createRGBImage, Graphics.drawRGB and
Graphics.drawRegion. `tools/bsprite.py` and `tools/parse_sprites.py` now validate
all 36 sprites: 652 modules, 639 frames, 271 animations, 869 module/palette
combinations and 19 PNG variants. Every sprite consumes its resource exactly.
Paletted module pixels, reconstructed PNGs and frame bounds are compared with
the original-Java oracle during regeneration. See BSPRITE.md for the full
format, cache/font semantics, validation boundaries and preview commands.
A native C++ renderer is still required.

## Remaining payloads

Tile grids, packed transforms and timed scripts now validate with
`parse_levels.py`: 12 packs, 17 scripts, 47 tracks, 241 keyframes, exact consumption.
The independent decoder matches 64 original-Java script/track oracle rows.
SCRIPTS_AND_LEVELS.md documents the resource table and opcode sizes, including
pack 9's unequal collision/visual grid heights. Script metadata and all linked
UID semantics remain open; format validation is not a cutscene play-through.

Object and stat envelopes now have separate validation in `parse_objects.py`.
Pack resources 3..11 / 0 contain 930 records across 41 types: a u8 count then
that many signed LE shorts, with common type/UID/X/Y prefix. Records consume
exactly, with one player per stream; type-specific parameters and references
still require validation. Pack 18 / 0 is a BE int row count followed by BE int
row lengths and values: 20 rows, 253 values, 1096 bytes consumed exactly.
See ENEMIES_AND_OBJECTS.md for stat strides and selected constructor checks.

`e` also reads an integer table through DataInputStream (big-endian Java
readInt), so not all payloads use the outer container's little-endian order.
Sound creation selects audio/midi for IDs below 20 or audio/x-wav otherwise,
using the currently loaded shared sound byte array. Full media/resource
semantics remain pending. Do not infer record fields or level counts from
pack names alone.
