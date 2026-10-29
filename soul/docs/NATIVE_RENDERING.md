# Native sprite rendering and asset preview

`soul_renderer` implements software ARGB surfaces plus module, frame, animation-
step and standalone-image drawing. `soul_port` now has an explicit asset viewer
using this renderer. It still does not run Game/Actor gameplay, level rendering,
menus or bitmap-font layout.

## Drawing contract

`Canvas` in `src/sprite_renderer.h/.cpp` owns a bounded pixel surface. It supports
half-open set/intersect clips, negative/offscreen positions, horizontal/vertical
flips and straight-ARGB source-over blending. Intermediate-alpha channels round
to nearest; alpha zero leaves the destination unchanged. Shipped assets have
binary alpha, while synthetic tests cover intermediate-alpha behavior. Surface
allocation is capped at 16 million pixels. Resize resets the clip; clear fills
the whole surface without changing it. Self-overlapping blits are explicitly
unsupported. Mutable pixel access must preserve the surface's allocation size.

`SpriteRenderer` borrows a sprite that must outlive it; temporary-sprite borrowing
is disabled. It caches unflipped decoded modules by module/palette. Sampling
applies flips once, avoiding double application when a decoded module is reused.
Every draw preserves sprite/animation data and the caller's clip. Rendering does
not advance timers, events, scripts or gameplay.

Frame drawing visits records in order. Parent flips reverse record offsets and
subtract leaf dimensions; child flips combine with XOR. Nested drawing includes
the enclosing record's offset, unlike the reference's nested *bounds* quirk.
Animation drawing applies signed animation offsets before combining its flags
with the frame transform. Coordinates passed to this API are already screen
coordinates; callers handle camera subtraction once.

An optional module mask hides local slots whose value is exactly 1; the same
mask applies at each nested level, as in the source draw loop. Recursive drawing
is bounded at 64 frames and one million visits per draw. Palette selection is
explicit and checked per unmasked module, even when clipped. This renderer does not emulate the
phone image-cache palette fallback, eviction behavior, boundary-split clipping
quirks or Graphics anchor/rotation API. General surface clipping has its own
deterministic contract. Invalid transforms/selectors fail rather than being
treated as a different frame or palette.

## Render comparison and limits

`soul_assets --data DIRECTORY --render-manifest` draws deterministic surfaces
and emits canonical ARGB CRC32 values. `check_native_render.py` compares **15224
surfaces** at widths 240 and 569: every frame under common valid palettes and
four flips, every animation step under four flips, plus first/last-frame clip/
edge/mask cases and standalone PNG variants. Comparison surfaces are 320 pixels
high, with an opaque background; all clip/mask checks operate on that surface.

The reference uses the existing Python frame composer and verified decoded
pixels. Masked cases use a filtered copy of its frame tables. The checker first
verifies Python's 9653 pixel-hash/bounds records against the original-Java oracle.
This is a Python-composition render regression, not execution of original
Graphics drawing, device screenshots or full runtime equivalence. CRC32 is a
noncryptographic regression checksum. The unshipped nested-drawing branch has
source-derived synthetic expectations, separate from shipped-asset coverage.

CTest adds **40 renderer assertions** with synthetic surfaces/sprites: source-
over rounding, alpha-zero behavior, flips after clipping, nested placement,
animation offsets, record order, masks, repeated cached renders, clip preservation,
invalid selectors, oversized/extreme coordinates and widths 240/569. Builds and
all **ten CTests** pass in Debug/Release without original assets. The prior native
container/sprite comparisons continue to pass.

## Asset viewer

```powershell
.\soul\port\build.bat
.\soul\port\build\Debug\soul_port.exe --sprite 2:0 --data soul/extracted --animation 1
.\soul\port\build\Debug\soul_port.exe --sprite 2:0 --data soul/extracted --frame 1 --flip 1 --width 240
.\soul\port\build\Debug\soul_port.exe --sprite 1:1 --data soul/extracted
```

`--sprite PACK:RESOURCE` requires an explicit extracted-data directory. It shows
the selected animation (default 0), or frame 0 when no animations exist, or the
first whole PNG when no frames exist. `--frame` selects a static frame and is
mutually exclusive with `--animation`. `--palette` must be valid for every module
of the previewed sprite; `--flip` accepts 0..3. Unsupported/missing resources fail
without guessing another asset. Existing aspect/FPS/fullscreen settings work.
The ordinary launch still shows the calibration marker without requiring data.

Only the asset viewer owns its preview animation clock. It advances at 83 ms,
holds zero-duration frames and uses caller-selected frames for drawing. Display
FPS and render interpolation do not advance it. This is not an Actor animation
state machine or completed gameplay interpolation.

```powershell
python tools/check_native_render.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --headless --sprite 2:0 --data soul/extracted --width 569 --frames 60 --dump soul/analysis/native-player-569.ppm
```

The optional `--port-exe` checks **14 actual preview runs** against Python at
30/60/144 FPS, interpolation on/off and widths 240/569, using a multi-step player
animation with unequal durations, plus standalone PNG
presentation. Headless output includes tick, preview pose and final pixel checksum.
Setup runs render/preview comparisons after building. CI stays data-free.
Explicit dumps/previews stay in ignored `soul/analysis/`; no assets enter packages.

The wider viewer canvas is a rendering test, not a completed widescreen gameplay
camera. NATIVE_FONTS.md now covers bitmap text and inline icon previews. Next:
tile layers/backgrounds/HUD, mutable rendering buffers and full staged loading
before translating gameplay.

The Windows black flashing fix now composes canvas, bars and text in a reusable
offscreen GDI buffer before a single visible copy. See NATIVE_HAZARDS.md for
presentation coverage and the remaining limits.
