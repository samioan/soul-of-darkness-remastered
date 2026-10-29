# BSprite 991: loader, pixels, frames and text

The full original `d.java` has been read and its 55 fields / 29 methods renamed
through `names.map`. This document refers to original method signatures so
the findings remain easy to check against raw decompilation and `javap` output.
Use `python tools/outline.py BSprite` for the regenerated method locations.

## Shipped resources and validation

Packs 1, 2 and 16 contain 36 BSprite resources: font/title, gameplay sprites,
and backgrounds. Every nonempty resource in these packs has the 991 signature.
They contain 652 modules, 639 frames, 271 animations and 869 module/palette
combinations. Twelve sprites use PNG-backed images (19 reconstructed PNG
variants); the other 24 use custom paletted pixels.

`python tools/parse_sprites.py` validates exact consumption of every sprite,
frame/animation references, collision ranges, PNG chunk CRCs, image dimensions,
palette indices and compressed module byte consumption. It decodes all module
palettes and composes all stored frames. Truncating any sprite by one byte and
corrupting the CRC of every PNG variant were also checked: all were rejected.

Default `python tools/rename_sod.py` additionally runs two checks:

- `SpriteCheck.java` loads the original and renamed BSprite classes in separate
  loaders. It compares loader fields, paletted decoded pixels under four flips,
  PNG images, frame bounds under flips, animation counts/durations, font mapping
  and wrapping, and image-cache registration behavior.
- It emits `soul/analysis/sprite-oracle.tsv`. The Python decoder compares pixel
  hashes and bounds against this oracle (9,653 records for the supplied build).

The Java sprite checker uses a small ImageIO-backed test Image class, so the
original PNG construction can run without a phone. It does not supply Canvas,
Graphics drawing or audio. The core ReferenceCheck still uses the unchanged
API jars. PNG pixels are consequently cross-checked against Java ImageIO plus
the original palette-template replacement; they are not a device framebuffer
capture. The full draw stack, clipping and cache eviction still need native
rendering/integration checks. Parser previews stay in ignored `soul/analysis/`.

## Binary layout

All integers in the sprite container are little-endian unless noted. Start
with u16 version 991 (`0x03DF`) and u32 feature flags.

| Flag | Loader behavior |
|---|---|
| `0x1` | Module count and module dimension records |
| `0x2` | Module atlas X/Y bytes |
| `0x4` | Per-module image-group byte; multiple palette/image groups |
| `0x100` | Frame-module records, collision rectangles, frame headers and visual bounds |
| `0x400` | Signed 16-bit frame-module offsets rather than signed bytes |
| `0x8000` | Present in several exports; no separate loader branch in this build |
| `0x10000` | Animation-frame records and animation headers |
| `0x1000000` | Custom paletted image data |
| `0x20000000` | PNG template/image path |

Module block: u16 count; each module has an image-group byte when `0x4` is set,
X/Y bytes when `0x2` is set, then unsigned width/height bytes. The original
tracks the highest image group to determine the number of palette groups.
Dimension and coordinate bytes are stored in Java byte arrays; reads generally
mask them with 255.

Frame block, when `0x100` is set:

1. u16 frame-module record count. Each record is module ID low byte, signed
   X/Y offsets (i8 or i16), flags byte. Bits 6..7 of flags extend the ID by
   `((flags & 0xC0) << 2)`. Bit `0x10` selects a nested frame instead of a leaf
   module. Flip bits are X=`1`, Y=`2`.
2. u16 collision rectangle count; each rectangle is i8 X/Y, u8 width/height.
3. u16 frame count. Each frame header has u8 module count, one ignored byte,
   u16 frame-module start index, i8 collision rectangle count. Rectangle starts
   are accumulated by the loader.
4. One visual-bounds rectangle (i8 X/Y, u8 width/height) per frame.

Animation block, when `0x10000` is set:

1. u16 animation-frame record count, then five bytes per record: frame low ID,
   duration, signed X/Y offset, flags/high ID bits.
2. u16 animation count, then four bytes per header: frame count, one ignored
   byte, u16 first animation-frame record.

The shipped frame-module and animation flags use only 0..3, so nested/high-ID
branches are understood from code but not exercised by these resources.

## Custom palette and pixel paths

Palette format is u16 `0x5515` (21781). Each image group has u8 palette count,
u8 color count (zero means 256), then u16 color entries per palette. The high
bit is binary alpha; RGB uses 5 bits per component. The Java conversion is
`alpha | ((c&0x7C00)<<9) | ((c&0x3E0)<<6) | ((c&31)<<3)`.
It leaves low RGB bits zero. Expanding 5 bits by replication would change
the original colors. The hasAlpha flag is set if any palette has an entry
without its high bit; all observed alpha values are 0 or 255.

After palettes: u16 pixel codec, then each module's u16 encoded length and
that many bytes. Java consolidates the module byte blobs and keeps offsets;
the Python model retains individual blobs.

| Codec | Shipped use | Decoding |
|---|---|---|
| `0x0400` (1024) | Font, one sprite | Four 2-bit indices per byte, high bits first |
| `0x56F2` (22258) | Three sprites | Control >127: control-128 literal indices; otherwise control copies of the next index |
| `0x64F0` (25840) | Twenty sprites | Low ceil(log2(colors)) bits select a color; remaining bits encode run length minus one |

The source also implements `0x27F1` literal/repeat encoding, `0x1600` 4-bit,
`0x0200` 1-bit and `0x5602` raw 8-bit indices. Python transcribes those branches,
but the supplied resources do not exercise them. Packed codecs can write a
whole final byte's indices beyond logical width*height in the original scratch
buffer; image output uses only the logical pixel count. New input builds need
their own validation rather than an assumption of support from these branches.

Original decoding uses a shared 12,288-int scratch array. It rejects modules
whose width*height exceeds that size. Normal uncached decoding applies horizontal
and vertical flips in software. Once a sprite has registered with an image cache,
software flips are omitted; Graphics.drawRegion applies them when drawing the
cached image. Applying both transforms would undo the flip.

## PNG templates and atlases

Without `0x4`, the `0x20000000` branch reads a u16 template length and PNG
bytes, u8 palette count and u8 color count (zero means 256). Each palette then
contains the PLTE payload+CRC (`3*colors+4` bytes) and tRNS payload+CRC
(`colors+4` bytes). The original replaces fixed template positions 41 and
`49+(3*colors+4)` before creating an Image.

With `0x4` and without custom pixel data, the loader reads u8 image-group count.
Each group contains a length-prefixed PNG template, then a palette-variant count.
Zero means a complete PNG with no replacement chunks. Nonzero is followed by
color count and the same replacement chunks per variant.

The parser reconstructs the PNGs exactly, checks every chunk CRC, inflates the
indexed image, reverses PNG filters and expands palette/alpha values. Module
images are atlas subrectangles. Standalone background/title sprites have no
module table: their decoded PNG is used as a whole image.

## Bounds, drawing and animation timing

Frame drawing visits frame-module records in order. Parent flip changes offset
sign and subtracts leaf width/height on the flipped axis; child flip flags
combine with XOR. Animation-frame offsets adjust the frame origin with the
same sign convention. Dynamic per-frame-module masks can hide selected pieces.
The preview composer renders unmasked, tight module extents; it does not replace
the collision or stored visual-bounds model.

`getFrameBounds` returns a selected collision rectangle, or zero if the selected
index is absent. For default bounds (`rect=-1`), ordinary sprites use the stored
visual rectangle. Wide-offset sprites compute a union starting at the origin.
The nested-frame helper does not add its enclosing module offset during this
bounds computation. Those semantics must not be silently changed. All current
default/collision/out-of-range bounds and four transforms match the Java oracle.

`getFrameWidth` returns a signed byte from the stored visual rectangle, while
module widths and most bounds reads are unsigned. This is preserved in the
Java reference; the font's actual widths fit the signed range.

Actor's original `f()` increments a per-animation-frame counter once per update,
advances when it reaches the stored duration, and wraps at the animation's frame
count. Duration zero holds the frame. The supplied durations range from 1 to 10;
they are simulation ticks, not milliseconds. See original b.java:659..678 and
the renamed getAnimationFrameDuration/getAnimationFrameCount call sites.
Selecting a higher presentation FPS must not advance those counters faster.

## Image caches and font behavior

Four global cache pools reserve slots per sprite by paletteCount*moduleCount.
An independently sized eviction ring rotates through those slots. Cache lifetime,
reservation base and palette selection are separate from the pixel decoder.
The engine assigns font/UI/gameplay sprites to these pools; this is an original
phone-memory optimization rather than a required desktop data format.

The same BSprite class implements bitmap text. `getGlyphFrame` indexes a literal
character string, including the build's accented-glyph substitutions, and maps
unknown characters to frame 31. The text renderer uppercases input. `{` selects
palette 1, `}` restores the base palette, `~` plus a digit draws an animated inline
icon from sprite 33, and `^` is a wrapped-line separator. Normal glyph advance is
frame width minus one, with a special tighter palette-5 layout in wrapping.
Line spacing is module 1 height plus 5, plus another 3 for the story prologue.

Wrapping uses global line ends (capacity 200) and caches by text alone, ignoring
wrap width and palette changes. A native widescreen implementation must explicitly
invalidate layout when its width changes. Preserve the reference algorithm and
verify the desktop layout change separately. Graphics clip behavior, anchors,
inline icon animation and font drawing still need framebuffer-level comparisons.

## Commands

```powershell
python tools/rename_sod.py
python tools/parse_sprites.py --oracle soul/analysis/sprite-oracle.tsv --quiet
python tools/parse_sprites.py --sheet soul/analysis/sprites.png
python tools/parse_sprites.py --preview 2:0 --animation 0 --step 0 --flip 1 --output soul/analysis/pose.png
```

The contact sheet uses row-major resource order from the command's listing and
shows frame 0 (or the whole PNG for standalone images). A blank font cell is its
space glyph, not a failed decode. Data, image previews and oracle files remain
ignored. The native executable remains a calibration scaffold with explicit
asset previews. NATIVE_SPRITES.md documents the C++ metadata/pixel/PNG decoder;
NATIVE_RENDERING.md covers sprite drawing and NATIVE_FONTS.md covers bitmap text,
layout and inline icon previews. Gameplay remains unported.
