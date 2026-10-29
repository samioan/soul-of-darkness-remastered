# Historical native port roadmap notes

These are the former append-only notes, preserved through the native beam pass.
They contain superseded status statements. Use [PORT_ROADMAP.md](PORT_ROADMAP.md) for current status and remaining work.

---

# Native port plan

## What works now

`soul/port` is a dependency-free Win32 C++17 calibration host. It builds with
the same CMake/Ninja/MSVC approach as the reference projects, using vswhere
to discover the installed x64 toolchain. It does not execute Java or contain
the game's runtime. Its ordinary moving rectangle is a timing calibration marker;
an explicit `--sprite ... --data ...` mode now previews decoded game assets.

The asset reference is now substantially further along: tools/bsprite.py decodes
all shipped sprite resources and composes frame previews, while the Java sprite
checker exports original pixel/bounds hashes for independent comparison. Use
these validated formats when implementing the C++ asset renderer. They do not
yet supply native gameplay. The native loader addition is described below.

The native resource foundation now exists in `src/resources.h/.cpp`: checked
binary readers and validated single-part numeric/promotion containers. The
`soul_assets` inspector matches all 252 Python manifest rows from the supplied
assets; synthetic checks remain data-free. NATIVE_RESOURCES.md records lifetime,
format and validation boundaries. `src/bsprite.cpp` and `src/sprite_png.cpp` now
decode all shipped sprite metadata, palette pixels and PNG variants too, using
Windows WIC for indexed PNG scanlines. NATIVE_SPRITES.md records the 18082-row
Python comparison and original-Java oracle boundary. `src/sprite_renderer.cpp`
now adds sprite/animation-step drawing, clipping/alpha and masks, with an asset
preview in the host. NATIVE_RENDERING.md records 15224 reference surfaces and
14 FPS/interpolation preview checks. `src/bitmap_font.cpp` adds bitmap text,
wrapping, anchors, palette controls and tick-indexed inline icons. NATIVE_FONTS.md
records 334 Java metric rows, 240 text surfaces and 12 font-preview timing checks.
`src/tile_world.cpp` now adds checked map loading, visible tile drawing and
camera clamping/panning. NATIVE_TILES.md records 45 original-Java map/binding
rows, 324 tile surfaces and 28 actual level previews. `src/player.cpp` adds a
movement subset and actor-record parsing. NATIVE_PLAYER.md documents 1200 Java
snapshots, 930 record rows and twelve first-level movement runs. NPC/object
construction, contacts, combat and full Game behavior remain pending.

ActorCheck now runs common actor integration, player movement/actions and tile
collision against the original class files on controlled fixtures. Its trace
and ACTOR.md provide concrete fixed-point/timing/bounds expectations for the
native port. Actor.paint still mutates animation/timers; separating update from
render must cover that path too. Enemy/object AI and full-level behavior remain
unverified by these fixtures.

The host supports 240..2048 logical columns at height 320, aspect-preserving
DIB presentation, window resize, fullscreen, independent display pacing,
optional interpolation, settings files, CLI overrides and headless PPM output.
CTest checks simulation-speed invariance over 83 seconds at 30/60/120/144/240
FPS, bounded catch-up after stalls, interpolation range and option validation.
Headless runs are deterministic and ignore personal settings by default.

## Implementation order

1. Checked readers, single-part pack access and shipped BSprite decoding are
   implemented, along with sprite drawing/clipping/alpha, bitmap fonts and the
   two visual tile layers. Full backgrounds/HUD and mutable world rendering
   remain pending. Prioritize the first-level gameplay slice below.
2. Add the needed Java semantics: signed bytes, unsigned shifts, overflow,
   arrays and bounds exceptions, fixed-point arithmetic, random/time services,
   RMS and MIDI/WAV. Port small classes first. Keep import/runtime state local
   to this project; do not reuse unrelated game assets or gameplay patches.
3. Port Game/Actor state machines with line-by-line reference comparisons.
   The movement prototype now supplies level 0 player loading, native movement,
   tile collision, keyboard input and a following prototype camera. Health/magic
   pickups now construct, render, attract and collect. Ordinary chests open and
   spawn crystals; crystal rewards now count fragments, update meters and return
   to movement. Collection effects and source-pickup disappearance now match
   the shipped lifecycle; see NATIVE_COLLECTION_EFFECTS.md. Player side-contact
   helpers now have controlled native/original comparisons in NATIVE_CONTACTS.md;
   the preview still needs live enemy/body queues. Weapon-0 melee control,
   combos, aerial poses and attached slash presentation now work; see
   NATIVE_MELEE.md. A controlled type-1 AI slice now matches 4,032 original
   snapshots; see NATIVE_ENEMIES.md. NATIVE_COMBAT.md adds controlled damage,
   hurt/death entries, invulnerability and combat effects. NATIVE_DEATHS.md
   adds ordinary death cleanup, owned record reset, fades and delayed loot.
   NATIVE_LIVE_COMBAT.md now connects seventeen ordinary enemies, reciprocal
   combat/body/effect queues, loot and a native defeat/restart panel. Next:
   remaining contacts/platforms and fuller region/world ownership. NATIVE_HAZARDS.md
   connects tile damage/death and documents buffered Windows presentation.
   NATIVE_PLATFORMS.md now provides controlled type-33/50 landing/support and
   supported player actions. Next port type-33 movement (including rider-controlled
   variant 8) and connect stable live platform ownership; type-50 movement follows.
   This first-level slice precedes completing every
   menu/loader/AI branch.
   Decide whether to adapt the sibling javalang translator after inventorying
   language constructs and required runtime semantics. A translator must fail
   on unsupported constructs and preserve Java evaluation order.
4. Reach boot/menu/first level in headless mode at 240x320. Add scripted keypad
   input, state traces, screenshots and exception logging. Verify a full native
   baseline before changing game behavior.
5. Integrate dynamic camera width and render interpolation as described below.
6. Save compatibility, audio, controller bindings, complete play-throughs and
   user-JAR launcher/package validation follow a proven gameplay baseline.

## Widescreen integration

Current startup: NATIVE_STARTUP.md now opens logo/title/menu/credits; New Game
opens the original prologue and a native first-level selection card before the
gameplay preview. The animated selection map, save-backed startup decisions and
complete original menus remain pending. NATIVE_MOVING_PLATFORMS.md connects type
33 in level 0; NATIVE_SWINGING_PLATFORMS.md connects all six type-50 chandeliers
with support, chain cutting, falling and selected impacts. Fuller world/region
ownership and remaining object contacts are next.

The original hardcodes 240, 320, 120 and 160 in many different roles. Audit
each use rather than applying text substitutions. In particular, camera
`e.d(int)`, visible bounds `e.f()/g()/h()/L()`, tile/sprite culling, backgrounds,
offscreen buffers, shake, scripted camera limits and menu/HUD coordinates
need distinct treatment. Clamp small levels safely when viewport width exceeds
level width. Preserve world/collision units and original portrait UI where
appropriate. Widescreen should expose additional world horizontally.

Test native width 240 and 16:9 width 569, then 427/747 and live width changes,
through boot, menus, gameplay, bosses, cutscenes, pause and transitions.

## Timing and interpolation

Original `e.run()` records wall time, requests repaint, waits for paint to
finish and sleeps `max(1, 83-elapsed)`. This is a minimum-period target, not
proof that every phone sustained 12.05 FPS. `Game.paint` invokes state logic,
so the native port must split tick-dependent work from pure drawing before
calling render at a higher rate. Inspect all wall-clock and tick counters.

`src/timing.h` supplies the 83 ms accumulator and caps stall catch-up at 250 ms.
At each tick, snapshot previous/current positions and camera state; interpolate
only render transforms by the remaining fraction. Do not interpolate collision,
AI, damage, animation event triggers, input edges or saves. Snap render snapshots
on teleports/scene changes. Choose sprite frames from simulation state.

Native, 30/60/120/144 display rates must all produce matching gameplay state for
the same tick-indexed input script. Compare both with and without interpolation;
the current timing self-test is infrastructure coverage, not gameplay coverage.

The ordinary-enemy region lifecycle is now connected to the preview; see
[NATIVE_REGIONS.md](NATIVE_REGIONS.md). Exit restores spawn records without death
rewards, and return clears the hidden flag. This is a controlled subset of world
ownership: mixed actor/region dispatch, transient pickups, UID pools, scripts and
save reconstruction remain pending.

Both level-0 type-45 bridges now render and collapse from falling chandeliers,
with collision edits shared by player/enemy/platform readers. See
[NATIVE_BRIDGES.md](NATIVE_BRIDGES.md) for the 2040-row oracle, live fixture and
remaining projectile/persistence boundaries. Transient world cleanup remains next.

[Native transient cleanup](NATIVE_TRANSIENTS.md) now sweeps dropped pickups and
collection effects at the pending region boundary, before handlers/new births.
Placed slots and later births survive. Exact mixed actor/region dispatch and
UID/pool/save reconstruction remain pending.

[Ordered world dispatch](NATIVE_WORLD_ORDER.md) now runs type-22 regions among
supported placed enemies/platforms/bridges, with frozen eligibility, same-tick
enemy clamps and later-region suppression. The full effects/pickups registry,
scripted objects, camera controllers and UID/save-backed progression are pending.

[Linked area triggers](NATIVE_LINKED_TRIGGERS.md) now load first-level UID 182
and control the forced-update flags of chandeliers 25/177 using active-region
overlap, lazy links and frozen queue eligibility. The 512-row original oracle
covers selected handlers and actual ordered world ticks. Linked gates, other
trigger/script branches and checkpoint/save-backed progression are next.

[Linked gates](NATIVE_GATES.md) now construct the first-level doorway's shared
collision and clipped artwork, with original opening/return/clear/hold behavior
checked across 2304 snapshots and 160 pixel surfaces. The shipped gate has no
UID links; its script-165 activation remains pending. Native scripts and their
remaining trigger/actor dependencies are next, before save-backed progression.

[Native scripts](NATIVE_SCRIPTS.md) now decode every shipped script and execute
the complete script-165 gate track through selected native ownership. Controlled
host runs open the actual gate; normal activation still requires script-locked
chest 57 and its linked trigger. Broader cutscene/UI/camera effects and the
remaining trigger/reward owners are pending before checkpoint/save progression.

[Container-removal triggers](NATIVE_REMOVED_TRIGGERS.md) now start the gate script
from source 57's actual removal state and clean up the selected slot afterward.
Controlled host runs exercise the complete source/trigger/gate connection.
Normal opening still waits on the type-48 ability-2 reward and locked chest /
linked-enemy activation sequence, before broader script and save progression.


Desktop play-flow pass: normal startup requires data and opens the game front end;
missing data produces a visible setup error instead of the calibration screen.
Native health/magic HUD, pause/resume, restart level and focus-loss pause replace
the normal play diagnostics. Displayed Czech startup text now has a desktop
English translation, with credited people preserved. See NATIVE_STARTUP.md.
This improves the playable first-level slice; it does not complete progression.
Next gameplay dependency remains type-48 ability-2 ownership/reward, chest 57's
unlock sequence, then broader scripts and checkpoint/save progression.


First-level cutoff repair: NATIVE_PASSAGES.md connects all 16 horizontal type-59
links, including source 81 at the first blocked tower wall, in placed world order.
464 native snapshots match original-bytecode handlers across 13 boundary cases
and all shipped links; 24 live host fixtures check the first transfer at both
widths across FPS/interpolation settings. Normal game cameras now clamp/clip to
the active region; narrow rooms center without exposing unrelated packed rooms.
Eighteen new assertions bring player checks to 302 and CTest to 54 tests.
This fixes an earlier route dependency reported during play. Type-48 ability-2
ownership/reward and chest 57's enemy/unlock chain remain next; full progression
and the complete game remain unfinished.


NATIVE_ABILITIES.md now owns chest 57's type-48 magic reward, including original
contact unlock/refill, attraction, pickup pose, floating/removal and the beam/
tutorial clocks. Already-unlocked chests emit the original single health orb
with the shared random stream. 2002 native rows match original-bytecode chest,
ability and modal arenas (3518 original/reference expectations); 24 controlled
host chains open the real gate after chest/reward completion at both widths and
all tested FPS/interpolation settings. Twelve timed game captures check the
English tutorial/HUD. Eighteen assertions bring player checks to 320; CTest is
57 tests. Normal chest 57 remains locked. Next: dormant enemy activation and
type-40 condition/enable chains, including type-2 enemies, before broader scripts
and checkpoint/save-backed progression. Full gameplay remains unfinished.


NATIVE_ENEMY_ACTIVATION.md now connects normal player-overlap trigger 129 to
waiting type-1 enemies 114/117, preserving lazy links, source bounds refresh,
flag clearing and frozen queue timing. Flag 2048 now removes a placed enemy after
its final permitted lifetime; all 20 first-level type-1 records load, including
UID 205 and the two waiting arena enemies. Nearby offscreen animation bounds
refresh correctly while an actor waits outside the update queue. 2146 rows match
original handler/world/terminal-lifetime arenas (1382 original/reference
expectations), and 24 actual host fixtures defeat both enemies and their allowed
respawns using melee across widths/FPS/interpolation settings. Sixteen assertions
bring player checks to 336; CTest is 60 tests. Next: type-2 enemies 113/48 and
removed-source trigger 54, then trigger 55's chest enable effects. Chests 56/57
and the gate remain locked in normal play until that later wave is supported.
Full gameplay remains unfinished.


NATIVE_SAVES.md now documents first-level desktop checkpoint autosaves and
Continue, including their original Restart/Continue distinction and remaining
original-format, upgrade-array and level-completion boundaries.


NATIVE_BEAMS.md now connects all six first-level beam emitters, their tile
raycasts and player damage. Remaining tutorial/camera/script dependencies and
level completion/loading are next; the full game remains unfinished.
