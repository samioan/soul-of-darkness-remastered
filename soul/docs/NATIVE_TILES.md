# Native tile maps and level preview

`soul/port/src/tile_world.h/.cpp` loads all twelve map packs and draws the two
visual tile layers. `--level-preview 0..8` displays the nine gameplay maps with
their original layer atlases. Arrows pan on 83 ms ticks, F8 changes the viewport
width and Esc exits. This is a tile viewer: no actors, player collision, scripts,
HUD, parallax/background effects or gameplay run yet.

## Data and rendering contract

The loader follows the resource layout in SCRIPTS_AND_LEVELS.md. It checks
four-byte LE dimensions, exact cell and packed-transform lengths, positive
dimensions and a 16-million-cell native allocation cap. The visual grids must
have equal dimensions. Gameplay collision bytes 255 become zero; visual 255
remains an empty tile. The unused collision companion's size is validated but
it is not interpreted or retained as transforms.

Collision and visual dimensions remain separate. Pack 9 retains its 148x71
collision cells alongside 148x72 visual cells. No padding, last-row lookup fix,
or gameplay collision policy has been introduced. The visual dimensions define
the rendered world extent, as the original loader's final dimension assignment
does. Native collision lookup awaits the player/gameplay pass.

The original per-cell draw method `e.a(Graphics,int,int,int)` and atlas setup
`e.j(int,int)` were checked in `tools/bytecode.py e` output. Tile IDs select 20x20
regions at `(id*20)%atlasWidth`, `(id*20)/atlasWidth*20`. Packed transforms read
the highest two-bit pair first. Original bit 0 maps to MIDP transform 2
(horizontal mirror), bit 1 to MIDP transform 1 (vertical mirror); the native
Canvas flip encoding therefore uses the packed value directly. These are not
90-degree rotations.

Per cell, layer 0 draws before layer 1 with alpha composition. Atlas resources
and palettes follow original `Game.bi`/`bj` in pack 16, verified across twelve
table entries. The nine gameplay previews load packs `level+3`; this does not
implement the presentation-state index remapping or full staged Game loader.
All referenced tile IDs are checked against their atlas at scene construction.
Atlases require complete 20-pixel rows/columns and valid pixel counts.

The renderer subtracts camera coordinates once and culls to the current Canvas
clip using floor division, including negative or non-tile-aligned cameras.
Coordinates use wide intermediate arithmetic. A separate camera clamp handles
maps smaller than the viewport by clamping their origin to zero. At width 569
more world tiles are visible; glyphs/tiles are not stretched. The original
260x340 scrolling ring buffer is replaced here by drawing visible immutable
tiles directly. Mutable tile changes/rollback and Game buffer integration are
still pending. Rendering changes neither the clip nor simulation state.

## Verification

Default Java regeneration now emits ignored `soul/analysis/map-oracle.tsv`
alongside the existing script oracle. ScriptCheck retains its original/reference
comparisons and adds two atlas-table comparisons (854 script/map comparisons
total). The 45 new rows contain 33 map-grid records and twelve atlas bindings:
normalized original collision cells, original visual cells/transforms, dimensions
and FNV-1a fingerprints. Collision dimensions come from each payload's dimension
resource, since original Game retains only the final visual dimensions.

`check_native_tiles.py` compares Python and native records directly against
that oracle. Its independent, screen-pixel-oriented renderer checks 324 native
surfaces over nine maps, widths 240/569, three camera locations, two clips and
three layer masks. Camera locations include negative fractional-tile offsets,
interior regions and the bottom/right edges. Pixels come from the existing
validated Python PNG/sprite path. This is not a phone framebuffer capture;
Java checks still use the test adapters and do not run Graphics drawing.

Twenty-eight actual host previews cover every level at both widths, with
30/60/144 FPS and both interpolation settings additionally checked on level 0.
All five-second runs produce 60 simulation ticks and matching camera/pixels.
Thirty-six synthetic data-free tile assertions check parsing, bounds, flips,
clipping, layer order, transparency, empty tiles and camera arithmetic. The
native suite now has 17 CTests per configuration; Debug/Release builds and the
strict asset-free Release package pass.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_tiles.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --level-preview 0 --data soul/extracted --width 569 --camera-y 1320
```

The native window caption explicitly identifies tiles-only viewing. Asset data
and render dumps remain external/ignored. Next target: a first-level gameplay
slice with actor loading, native player movement/collision and input/camera
integration. Complete boot/menu loading, backgrounds/HUD, combat, enemies,
scripts, saves and audio remain beyond that initial playable milestone.
