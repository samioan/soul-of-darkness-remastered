# Native startup, title and credits

Normal interactive launches now use the native front end when extracted game
data is found. It searches the working directory's `soul/extracted`, `data` beside
the executable, and the workspace-relative extracted directory for Debug/Release
and dist layouts. `--game --data PATH` selects this explicitly. `--calibration`
keeps the old frame test; ordinary headless runs remain data-free calibration.
Original data is never included in the executable or distribution.

The flow is logo (47 simulation ticks), title/any-key, native menu, original
prologue, first-level selection, then a loading boundary into the current
first-level gameplay slice. The menu also opens the
original credits scroll and exits. Escape returns from the prologue, selection or credits to
the menu. During play it opens a native pause menu: Resume keeps the live world,
Restart Level crosses state 9 and reconstructs the level, and Main Menu returns
to state 2. Focus loss pauses play. Gameplay remains frozen while menus are open.

## Assets and reference evidence

The original `Game.updateLogoLoading` draws `/logo.png` on white and changes
state at frame 47. `updateTitle` uses pack 1/resource 1's cached PNG, with common
string 10 blinking when stateFrame % 10 > 5. `updateScrollingText` uses string 28,
starts at Y=266, clips to Y=20..286 and scrolls one pixel per simulation tick.
Entering credits replaces `X.X.X` with the manifest version `1.3.7`.
Original `e.N/as/ax` call sites and bytecode establish this resource/timing policy.

The native front end uses those original images, font and common strings. Its
checked reader consumes Java modified UTF-8 code units from pack 15/resource 0;
the shipped resource is Czech despite the input filename's EN label.
`english_text.cpp` translates the displayed title prompt, prologue, chapter label
and first-level name. Credit role headings are translated in place; all credited
names, ordering and attribution remain from the resource. This is a desktop
translation, not a recovered official English table. Unused common strings and
unported dialogue resources remain untouched. The original JAR is unchanged.

## New Game, prologue and first-level selection

New Game enters state 20, using the desktop English version of story string 40 at Y=240.
`Game.changeState` sets that initial position; `updateScrollingText` (`e.ax`)
and original bytecode confirm the 230-wide wrap, Y=20..286 clip, one pixel per
tick and four extra pixels while either vertical direction is held. Prologue
font lines are three pixels farther apart than credits, as in
`BSprite.drawWrappedText`. Both scrolls loop at Y=266 once the returned text
bottom is negative. This also corrects the prior native credits loop boundary.
Arrow/W/S releases and window focus loss clear acceleration.

Enter/Space/E maps Next to state 19. A separate press confirms selection and
enters state 9; the following tick reconstructs the preview. Escape returns to
the menu at either screen, and another New Game resets the story position.
The selection card uses original chapter label 41 and level-0 name 42, with a
native title-art layout. A fresh game only
offers level 0; navigation cannot select unsupported levels. This ports the
fresh-start screen sequence, not save-backed unlocks, checkpoint/stat preparation
or the original animated selection map (`e.e/av/au/f/aw`). Those map transitions,
path dots, loading wipes and staged resources remain pending. The subsequent
NATIVE_SAVES.md pass adds checkpoint persistence and a direct Continue path.

UI remains a centered 240-wide portrait panel at widths 240/569. Gameplay reveals
the wider world. Drawing is pure; logo/title clocks and credit scrolling run at
83 ms regardless of presentation FPS or interpolation. A reusable GDI backbuffer
continues to compose the complete Windows client before presentation.

## Current boundaries

This is a native startup path into the verified gameplay slice, not the complete
original boot/runtime. Asset reads happen synchronously; it does not port all
47 staged common-loading operations or the 80-stage level loader. The original
sound prompt is omitted while audio is absent. The menu now offers Continue when a valid desktop save exists; settings remain keyboard-driven. The animated selection map, complete original menu behavior, audio and original HUD remain pending. These screens have no phone
framebuffer equivalence claim.

`frontend_transitions` checks logo timing, title-key isolation, menu navigation,
credits entry/scroll/loop/back, prologue entry/scroll/acceleration/release/loop/back,
selection restrictions/back, loading-before-start, gameplay-back and exit
without game data. Three invalid CLI cases bring CTest to **36 tests** per build.
`check_native_frontend.py` independently compares logo/title pixels with Python
at widths 240/569, independently composes four story-text surfaces from resource
40 and the original font, verifies menu/credits/prologue/selection milestones,
and runs twelve timed prologue captures plus twelve complete
logo/title/credits/prologue/selection-to-game scripts across FPS/interpolation
settings. All 36 CTests and the asset-backed startup checker pass in Debug and
Release; the local dist package is rebuilt without game data.
Screenshots were inspected locally and remain in ignored analysis/.

```powershell
.\soul\port\build\Release\soul_port.exe --game --data soul/extracted
python tools/check_native_frontend.py --exe soul/port/build/Release/soul_port.exe
```

Desktop checkpoint saves and Continue are now implemented in NATIVE_SAVES.md.
Next: original animated selection map and broader progression,
alongside remaining gameplay objects, world ownership, HUD and audio.

## Desktop play flow and English text (current)

Normal interactive launch requires game data and always enters the game front end.
If data cannot be found, a visible error explains setup and --data; it no longer
silently opens calibration. Explicit --calibration and data-free headless runs
retain their validation behavior.

Normal play renders health and magic bars/numbers in the logical canvas at widths
240/569, replacing the GDI prototype diagnostics. Locked magic is dimmed. The
native HUD is a desktop design, not an original-phone HUD equivalence claim.
Pause state 21 draws over the frozen world, shows keyboard controls and supports
Resume, Restart Level and Main Menu. Input is cleared at transitions/focus loss;
R/Restart now restore the latest checkpoint; disk saves and Continue are documented
in NATIVE_SAVES.md. Explicit --movement-preview retains
its diagnostic overlay. Full gameplay and first-level progression remain
unfinished; the first arena/reward/gate chain is now implemented.

Frontend transition checks cover pause/resume/restart/menu and English credit
roles with preserved names. The asset-backed checker independently composes the
English prologue at both widths and compares startup state across FPS settings.
It also checks the pause boundary, frozen world state, resume without reload,
and twelve HUD/pause captures across widths 240/569, FPS 30/60/144 and interpolation.
All 51 CTests pass in Debug and Release. No Java/generated reference or simulation
handler changed in this UI pass.


Normal game world drawing now clamps and clips to the active camera region.
Outdoor areas reveal more world at wider widths. A room narrower than the
viewport is centered with plain margins; its packed neighbors remain hidden.
The HUD and pause menu remain full width. Type-59 passages connect those rooms;
see NATIVE_PASSAGES.md. Full original camera control remains pending.
