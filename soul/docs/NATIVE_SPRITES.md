# Native BSprite decoding

`soul_sprites` now loads all **36 shipped BSprite 991 resources** through the
native resource library. It owns module/frame/collision/animation metadata,
palette and encoded module data, reconstructed PNG variants and decoded ARGB
images. Parsed sprites no longer borrow their input pack bytes. This is an
asset decoder and bounds model, not the Game/Actor runtime or a sprite renderer.

## Implemented behavior

`BSprite::parse` in `soul/port/src/bsprite.cpp` consumes exactly the supplied
resource. It checks version, known feature flags, table slices, frame/module/
animation references, collision counts and image-group/atlas bounds. Frame-module
and animation offsets preserve their signed byte/short meanings. Animation
durations remain simulation ticks; decoding does not advance their clocks.

Custom palettes retain the original 5-bit-to-8-bit shift conversion without
replicating low color bits. Alpha and the RGB bits of transparent entries are
preserved. The three shipped codecs are implemented: 0x0400 packed 2-bit,
0x56F2 literal/repeat and 0x64F0 packed-color runs. Packed final-byte padding is
validated but excluded from logical output. Encoded-byte leftovers, bad indices,
underruns and RLE overruns fail. Custom modules retain the original 12288-pixel
scratch limit. Other codecs known from Java/Python are explicitly unsupported
in this native pass; no shipped resource needs them.

The native PNG path reconstructs the original template replacements at their
fixed offsets. It checks chunk CRCs, order, indexed formats and file consumption
before using Microsoft's built-in WIC PNG decoder for packed index scanlines.
Color expansion uses the checked PNG palette/tRNS bytes directly, retaining RGB
under alpha zero. It does not apply premultiplication or color management.
[Microsoft documents WIC indexed formats and alpha layouts](https://learn.microsoft.com/en-us/windows/win32/wic/-wic-codec-native-pixel-formats).
Only noninterlaced indexed PNGs with 1/2/4/8-bit indices are accepted; dimensions
and the decoder's indexed format must agree. No downloaded decoder or extra DLL
is bundled: Windows supplies WIC (`windowscodecs`/COM).

`decodeModule` returns ARGB pixels with the four X/Y flip combinations, including
PNG atlas subrectangles. `frameBounds` returns stored visual or selected collision
bounds, preserving the wide-offset union starting at the origin. Nested bounds
omit the enclosing record's offset, as in the reference. Missing collision
rectangles return zero bounds. Nested cycles and paths beyond 64 frames fail;
shared subgraphs use cached traversal/bounds to avoid exponential work.

The parser enforces an aggregate 16-million-pixel decode budget across PNG
variants and module/palette validation. This and recursion limits are native
allocation/work bounds, not claims about additional game export formats.
Unknown features, combined custom/PNG storage and unsupported transforms fail
explicitly. Drawing, frame composition, clipping, caches, text layout and actor
animation events remain separate work.

## Cross-checking shipped resources

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build\Debug\soul_assets.exe --data soul/extracted --sprites
python tools/check_native_sprites.py --exe soul/port/build/Debug/soul_assets.exe
```

The tool's `--sprite-manifest` mode emits metadata and noncryptographic FNV-1a
fingerprints to stdout. The Python checker compares **18082 rows** across all
652 modules, 639 frames, 271 animations, 869 module/palette combinations and 19
PNG variants. Rows include every stored metadata table, palette colors, exact
reconstructed PNG bytes, PNG pixels, module pixels under all flips and selected
default/collision/missing bounds under all flips.

The checker first requires `sprite-oracle.tsv` from default Java regeneration
and verifies Python's 9653 original-bytecode pixel-hash/bounds rows using the
existing SHA256 oracle path. It then compares the native output to that checked
Python model using FNV fingerprints. This is a transitive regression comparison,
not direct native SHA256 output or an original-device framebuffer capture.
Both Debug and Release pass it against the freshly regenerated oracle.

No Python sprite parser/decoder was changed. Existing Java/Actor/Behavior/
Script/save/lifecycle checks also pass. Setup runs the native sprite comparison
after building; ordinary CMake/CTest and CI need no game data or Python.

## Synthetic tests and remaining work

`sprite_decoding` runs **222 assertions** with small code-generated fixtures:
palette conversion/alpha, all shipped codecs, pixel/bounds flips, signed offsets,
animation slices, wide/nested bounds quirks, cycles/shared/deep graphs, all byte
truncations, invalid tables/indices/runs, PNG corruption/dimensions/palette indices,
template replacement and atlas subrectangles. These fixtures are independent of
the original game assets. The unshipped nested-frame branch is covered by source-
derived synthetic expectations, not the shipped Java oracle's resource coverage.

Debug and Release now pass ten CTests, including the added renderer suite and prior 113 resource assertions
and scaffold timing/options checks. Release packaging still contains only the
two executables and example settings; it includes no assets or preview images.

NATIVE_RENDERING.md now covers native module/frame/animation drawing, clipping,
alpha, masks and the asset viewer, verified at widths 240/569. Bitmap fonts and
world rendering are next. Simulation
stays at 83 ms; sprite decoding adds no simulation or presentation clock. The
Windows game executable remains a calibration scaffold.
