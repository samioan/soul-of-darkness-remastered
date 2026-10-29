# Native bitmap fonts

`soul/port/src/bitmap_font.h/.cpp` implements bitmap text over the native sprite
renderer. It borrows the immutable font (pack 1, resource 0). Inline icons borrow
a separate renderer for pack 2, resource 33, as loaded by original `Game.ag()`.
The sprites must outlive their renderers. Native gameplay and menus remain unported.

## Reference contract

The implementation follows original `d.a(String)`, `d.a(char)`, `d.a(String,int)`
and the two Graphics text overloads. Bytecode was inspected with
`python tools/bytecode.py d`. No rename map, decompiler patch or generated Java
was edited in this pass.

- The literal glyph mapping includes accented Latin characters and symbols.
  Unknown UTF-16 units map to frame 31. Measurement uses signed stored frame
  widths minus one; drawing uses unsigned widths minus one.
- Measurement and wrapping consume raw text. Drawing uppercases the string.
  Wrapped layout runs first and retains its raw UTF-16 indices, even if uppercase
  conversion expands a character. Lowercase raw text uses fallback glyph widths
  during wrapping; this original quirk is preserved.
- Wrapping reads the flat `frameModules[glyph]` table rather than following frame
  starts, including after multi-module glyphs. It adds the first frame-module's
  X offset to each width. Palette 5 subtracts two instead of one. The shipped
  font has three palettes; palette 5 is tested as a layout rule only.
- `{` selects palette 1; `}` restores the caller's base palette. Wrapped lines
  carry the current palette. State is local to each call instead of using the
  original static text ranges and global palette state.
- `~0` through `~3` draw shipped icon animations using the caller's simulation
  `stateFrame` modulo the original fixed frame-count table, independently of
  animation durations. Measurement uses twice module 0's signed width; drawing
  uses the different original icon width/offset tables. The table also contains
  `~4` metrics, but the shipped sprite has no animation 4; drawing it raises a
  checked range error. No fifth animation is invented.
- `^` separates wrapped lines; plain text treats it as an unknown glyph. Line
  height is module 1 height plus 5, with another 3 for the prologue option.
- Horizontal center/right and vertical center/bottom anchors preserve reference
  order and rounding, including the wrapped block adjustment followed by each
  line's anchor. The coarse vertical cull skips controls in culled lines too.
  Canvas supplies pixel clipping.

Each call recomputes layout, deliberately replacing the phone's text-only cache
which ignores width/palette changes. Width 569 therefore fits more text without
scaling glyphs. Uppercasing supports deterministic ASCII/Latin-1, including
sharp-s expansion, with UTF-16 fallback elsewhere. This does not implement
Java's general locale-dependent Unicode conversion. Partial line windows,
dialogue pagination, phone image-cache palette fallback and Game text globals
remain integration work. Draw errors can leave a partially drawn surface.

Native guards bound input to 32768 UTF-16 units and 200 lines and reject malformed
icon escapes, incomplete font tables and coordinate overflow. These safeguards
do not describe original malformed-input behavior. Constructor borrowing from
temporary sprites is disabled.

## Validation and preview

Default `python tools/rename_sod.py` writes ignored
`soul/analysis/font-oracle.tsv`. SpriteCheck compares original and regenerated
Java and explicitly clears the width-blind cache for each width/palette. Its
334 rows cover 224 glyph mappings and 110 measurement/layout cases, with widths
0/40/120/240/569 and palettes 0/5. Its existing sprite pixel/bounds oracle keeps
the same scope. Java uses the test ImageIO adapter, not a MIDP runtime.

`check_native_fonts.py` compares native metrics directly with that oracle and
validates independent Python metrics against it. Python frame composition then
supplies 240 expected text surfaces: two widths, three actual palettes, five
anchors, plain/wrapped text and four icon ticks. These are software render
comparisons, not phone framebuffer captures.

Twenty-six synthetic font assertions and two new argument checks bring the
native suite to 13 data-free CTests per configuration. Twelve actual font-preview
runs verify ticks and final pixels at widths 240/569, 30/60/144 FPS and both
interpolation settings. Five seconds yields 60 ticks; drawing never advances
icons. Debug/Release builds and the strict data-free Release package pass.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_fonts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --font-preview --data soul/extracted --width 240
.\soul\port\build\Debug\soul_port.exe --font-preview --data soul/extracted --width 569 --palette 2
```

F8 changes width and recomputes layout. The viewer uses actual bitmap glyphs,
palette controls and inline icons. Assets stay external and excluded from git
and packages. Next: tile/world rendering, preserving pack 9's different visual
and collision dimensions, before staged loading and gameplay.
