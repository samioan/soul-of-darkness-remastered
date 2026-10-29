# Historical project roadmap notes

These are the former append-only notes, preserved through the native beam pass.
They contain superseded status statements. Use [ROADMAP.md](ROADMAP.md) for current status and remaining work.

---

# Roadmap

## Target and baseline

Input: `roms/Soul-of-Darkness_J2ME_EN_v137.jar`, 347896 bytes.
SHA256: `9d839e59d6cb2f80575736fbf867babe0da852312652490dd9025d0a0654dd6b`.
Manifest: Soul Of Darkness, Gameloft SA, 1.3.7, CLDC-1.1 / MIDP-2.0,
MIDlet `GloftSOFD`, Created-By 1.4.2_15. There are 31 ZIP entries, six class
files, numeric data files 0..19, dataIGP, icon/logo/new PNGs and the manifest.
This is JVM bytecode; no native disassembler is required.

## Phase 0: complete

- Original JAR preserved; canonical roms copy is hash-identical.
- Safe-path, CRC-checked extraction with per-entry inventory.
- Vineflower 1.12.0 decompilation: all six Java source files recovered.
- Pinned local tool provisioning; JDK 21 selected over Java 8 on PATH.
- Native C++17 Win32 scaffold configured with CMake + Ninja + MSVC x64.
- Debug and Release builds and CTest pass without any game data.
- Git ignore rules, local editor tasks and data-free Windows CI configured.

## Phase 1: core, type handlers and scripts mapped; deeper runtime branches pending

Class-level roles and the core Game pass are mapped in CLASS_MAP.md:
236 fields and 354 methods now have evidence-based names (including all 55
fields and 29 methods of BSprite). The generated Java
compiles against MIDP/CLDC/Nokia API stubs with zero errors. Default regeneration
also runs behavioral comparisons against the original bytecode for supported
pure/static methods. Two Vineflower artifacts are corrected with asserting
patches checked against bytecode: camera boolean/int syntax and script counter
post-increment timing. Neither compilation nor the partial
behavior harness proves full gameplay equivalence.

GloftSOFD, Game.paint/changeState/run, screen/menu dispatch, input masks and
outer resource/string/binary accessors have been read. STATE_MACHINE.md and
INPUT.md record the verified flow and timing. Sound/RMS access boundaries,
camera units and the trigonometry table are mapped; deeper save and gameplay
semantics remain open.

BSprite's full loader, pixel/PNG paths, bounds, draw stack, caches and font
methods are mapped in BSPRITE.md. The parser consumes all 36 sprite resources
exactly, and its pixels/bounds match the original-Java oracle. Animated frame
durations were confirmed to count simulation ticks at Actor call sites.

Actor's construction/common lifecycle, fixed-point integration, player actions,
animation, bounds, tile slopes/walls and geometry are now mapped in ACTOR.md.
The default checker compares these paths against the original bytecode, including
700 controlled movement/action ticks. All 27 player animation clocks and the
six slope shapes are covered. This does not complete the large Actor class.

Type-specific handler dispatch, shared damage/targeting helpers, stat access
and selected action-entry branches are now mapped in ENEMIES_AND_OBJECTS.md.
Sixteen original/reference arenas pass 320385 state/world comparisons;
they include explicit pickup/container/trap/bridge activation and boss opening
sequences. Idle cases, alternate variants and deeper boss phases remain open.

Script dispatch/opcodes, lifecycle and tile-map loading are now mapped in
SCRIPTS_AND_LEVELS.md. The new checker passes 852 map/script comparisons;
controlled actor/camera timelines verify interpolation and exact command ticks,
pause and looping/one-shot completion. Full cutscene/dialogue/QTE effects remain open.

Save buffers, selected restoration paths and linked snapshots are now mapped in
SAVES.md, with 2398 in-memory checks against original bytecode and independent
buffer expectations. The original progress-flag swap is preserved. The separate
lifecycle pass below adds controlled checkpoint/restart and test RMS coverage.

All nine shipped object streams now execute original/reference construction in
fresh, all-removed restart and inclusive removed-UID fixtures. Selected level 5
snapshot relinking also executes. LEVEL_ACTORS.md records 17898
expectations and 6763 world comparisons, including type 69 ownership and the
shared short-record buffer. This does not run staged loading or gameplay ticks.

CHECKPOINT_LIFECYCLE.md covers checkpoint activation and two complete original
restart calls on all nine levels with an isolated in-memory RMS adapter. Its
1737 expectations, 384 level comparisons and 7908 world comparisons also cover
repeated loads across three packs and preserved respawn-pool/UID-table aliasing.
Existing checkers retain their previous runtime boundaries. No native save
backend or general Game loop is implemented by these fixtures.

NEW_GAME_AND_CONTINUE.md covers confirmed save reset, Continue dispatch,
level-selection stat preparation and loading stages 1/79/80. Its 560 expectations
plus scene comparisons preserve the original fresh-construction override on
Continue and checkpoint/next-level meter differences. Buffer references are
pre-seeded; no mutable rendering runtime or full boot sequence is implied.

LEVEL5_CONTROLLER.md covers shipped controller/hazard activation, three live
linked snapshots, Restart/Continue reconstruction and a resumed common actor
update. Its 353 expectations, 132 level comparisons and 2575 world comparisons
preserve signed placement arithmetic, the camera's -2 correction and the offset
global's deferred refresh. This is controlled handler coverage, not full gameplay.

Next target: remaining object contacts/platforms and fuller
world/region ownership. NATIVE_LIVE_COMBAT.md now connects ordinary enemies,
reciprocal damage, bodies, loot/effects and a native defeat/restart boundary. NATIVE_DEATHS.md now covers ordinary type-1
cleanup, owned spawn-record reset, respawn fade and delayed loot.
NATIVE_COMBAT.md now covers weapon-0 damage, enemy hurt/death entries, player
hit/death response and combat effects in controlled native/original fixtures.
NATIVE_ENEMIES.md now covers a verified native type-1 activation/pursuit/attack
controller; it is not yet loaded by the displayed movement preview. Weapon-0 melee poses,
combo control, slash presentation and attack queries now have native/original
comparisons. Player side-contact
helpers now match controlled original-bytecode scenarios; live enemy bodies,
platform ownership and remaining object contacts still need integration.
Ordinary first-level chests now open and emit crystals;
crystal rewards update fragments/meters and return to movement. Native health/magic
pickup construction and collection now provide the first object interaction. The native player movement
prototype now provides a baseline for that integration. Full staged loading
and remaining world/HUD rendering still need integration;
extend deep enemy/object branches with valid linked-actor fixtures.
Rename members only after verifying callers.
Check suspicious narrowed byte counters and ternaries against
`python tools/bytecode.py e` (or b/d/a).

## Phase 2: maps/scripts and save buffers mapped; object details and integration pending

The 19 numeric pack tables and dataIGP parse with exact consumption. See
ASSET_FORMATS.md. BSprite version 991, palette/image decoding and frame/animation
tables now parse and validate across all shipped resources. Remaining work:
type-specific object parameters, script metadata, fuller string metadata, MIDI/WAV selection
and full save integration around the mapped serializers/restorers. Each parser should
assert bounds and exact byte consumption; derived output stays ignored.
File 0 is a single zero byte; its meaning remains unconfirmed.
`parse_objects.py`, included in setup, consumes 930 object records across nine
streams and 20 big-endian actor stat tables (253 integers) exactly. This validates
record envelopes, not every object's parameter semantics or script references.
`parse_levels.py`, also run by default regeneration, consumes all 12 map packs,
17 scripts / 47 tracks / 241 keyframes exactly. Track hashes/initial offsets
match 64 original-Java oracle rows. Pack 9's collision grid is one row shorter
than its visual grids; the original loader's final dimensions preserve that
mismatch. See SCRIPTS_AND_LEVELS.md before designing native collision storage.

## Phase 3: Windows infrastructure ready, gameplay not ported

The calibration shell provides a dynamic logical canvas, aspect-preserving
presentation, borderless fullscreen, FPS selection and fixed-tick interpolation.
NATIVE_RESOURCES.md documents the new native binary readers and single-part
numeric/promotion container library. The separate `soul_assets` tool matches
252 Python manifest rows across all shipped containers; 113 synthetic resource
assertions run without game data. Local packages
now include the read-only inspector alongside the calibration executable.
NATIVE_SPRITES.md covers native metadata, palette/pixel and PNG decoding for all
36 sprites. Both configurations match 18082 Python manifest rows after its 9653
original-Java oracle checks. The native suite now adds 222 sprite assertions
and passes seven CTests without game data. No native sprite drawing, fonts or
gameplay are implemented by that decoder pass alone.
NATIVE_RENDERING.md adds module/frame/animation drawing, clipping, alpha and
masks, plus an explicit asset preview in `soul_port`. It matches 15224 Python
render surfaces and 14 actual preview runs at widths 240/569 with independent
FPS/interpolation. Forty synthetic renderer assertions bring CTest to ten
data-free tests per configuration. This still does not run Game/Actor gameplay.
NATIVE_FONTS.md adds bitmap measurement/wrapping, text drawing, palette controls,
anchors and tick-indexed inline icons. It matches 334 original-Java metric rows,
240 Python text surfaces and 12 actual font-preview runs. Twenty-six synthetic
font assertions and two new argument checks bring CTest to 13 per configuration.
Layout recomputes on width/palette changes. Phone framebuffer captures, dialogue
pagination and menu integration remain pending.
NATIVE_TILES.md adds native loading for twelve map packs and both visual tile
layers, with explicit previews for all nine gameplay maps. Forty-five original
map/binding rows, 324 Python tile surfaces and 28 host previews pass. Separate
collision dimensions preserve pack 9's mismatch without choosing a lookup fix.
Thirty-six tile assertions bring the native suite to 17 CTests. Next target is
a first-level player/actor slice; full backgrounds, HUD, mutable tiles, staged
loading and the remaining gameplay systems are still pending.
NATIVE_PLAYER.md adds signed actor-stream records (including reused tails),
type-0 initialization, player movement/tile response and animation clocks. It
matches 1200 original-Java snapshots over 600 controlled ticks and 930 independent
record rows. The first-level movement prototype adds keyboard input and an
explicit following camera; twelve FPS/width/interpolation runs pass. Thirty-one
synthetic assertions bring the suite to 21 CTests. Beyond the health/magic pickup subset below, object/NPC behavior, combat,
damage and full gameplay remain absent; hazards stop this prototype explicitly.
NATIVE_PICKUPS.md adds type-35 health/magic pickup construction, sprite drawing,
attraction and collection, with capped meter updates. The first-level prototype
loads all twelve placed health pickups. 381 snapshots from nine controlled
original-Java scenarios cover near/far and dropped-item attraction, moving player velocity, health/magic,
limits, pause and a frozen animation's 830 ms fallback. Eleven new data-free
assertions bring the player/input/pickup executable to 42 checks; CTest remains
21 tests. Collection effects, tutorials, saves, upgrade pickups and enemy
attraction remain pending. The temporary meter overlay is not the original HUD.
NATIVE_CONTAINERS.md adds four ordinary first-level chests plus two dormant
script-locked records, fire input, prompts, falling, opening/disappearance and
single-crystal spawns. Its 816 original-Java snapshots/events cover eight chest
and five spawned-pickup scenarios, including the crystal-contact boundary and
ordinary-pickup expiry. Twelve controlled host runs pass at widths 240/569,
30/60/144 FPS and both interpolation settings. The suite now has 54 combined
player/input/pickup/container assertions and 23 CTests. Crystal rewards, effects,
burst/ability/coin variants, saves and full object queues remain pending.
NATIVE_CRYSTALS.md adds the 39-tick crystal beam, fragment counting, fifth-screen-
tick meter effects, fourth-crystal maximum increases, input-gated dismissal and
return to gameplay. 448 original-bytecode state snapshots pass with isolated
drawing hooks suppressed; no phone-render equivalence is implied. Thirteen new
assertions bring the combined executable to 67 checks; CTest remains 23 tests.
Twelve chest host runs now complete the reward and walk away at widths 240/569,
30/60/144 FPS and both interpolation settings. The reward panel uses native
wording/layout with the existing font and sprites. Saves, collection effects,
full world queues, enemies and combat remain pending.
NATIVE_COLLECTION_EFFECTS.md adds type-39 collection events, effect ownership,
birth clocks/removal and hidden source pickups. Both shipped effect animations
are single-frame, so the original ring formula has zero diameter. Eighty-eight
original-bytecode snapshots cover 24 scenarios. Twelve new assertions bring
the combined executable to 79; all 23 CTests pass in Debug and Release. Twelve
chest host runs additionally verify one collection effect and its removal at
widths 240/569 across presentation rates. Next: first-level actor contacts/combat.
NATIVE_CONTACTS.md ports the player side-contact helpers and adds their hook to
native player updates. 628 player/enemy bounds, 7,056 controlled arenas and 384
player ticks match original bytecode. The preview still loads no enemy bodies.
Twenty-one contact assertions and three player integration assertions bring the
combined player executable to 82 assertions and CTest to 24 tests. Full moving-
platform/support ownership remains pending. Next: a native type-1 enemy and
weapon-0 melee/damage slice, with live queue ownership and response to contact.
NATIVE_MELEE.md adds weapon-0 standing/combo/aerial control, recovery timing,
the type-28 slash follower and attack-overlap queries to the movement preview.
1,920 original-bytecode snapshots cover 960 ticks across twelve scenarios.
Twelve host runs perform the combo and aerial strike at widths 240/569 across
presentation rates. Twelve new assertions bring the combined executable to 94;
three argument checks bring CTest to 27 tests. Enemy AI and applying damage
remain pending. Next: a native type-1 enemy with live body queues, hit reactions
and damage/death integration.
NATIVE_ENEMIES.md adds the non-combat type-1 controller: fixed-point pursuit,
activation, attack/recovery, gravity, ledges and tile response. 4,032 original-
bytecode snapshots cover 2,016 ticks in 21 controlled arenas. Fourteen new
assertions bring the combined native executable to 108; CTest remains 27 tests.
The fixtures use synthetic player rectangles to avoid damage, with explicit
health/pose guards. The movement preview still loads no enemies. Next: applying
weapon/enemy damage, hit reactions/death/effects, then live enemy/body queues.
NATIVE_COMBAT.md adds weapon-0 damage/RNG, enemy hurt/death entries, player
health/invulnerability/knockback/death poses and blood/death/number effects.
2,591 original-bytecode rows cover damage matrices, hit gates, reaction ticks,
effect spawns/lifetimes and random-stream values. Twenty-five new assertions
bring the combined executable to 133; CTest remains 27 tests. The movement
preview still has no live enemies. Next: enemy death cleanup, loot and record
reset, then live combat/body/effect queues and death-transition handling.
NATIVE_DEATHS.md adds ordinary type-1 death cleanup, loot selection, owned
record reset, finite/repeating fades and signed, hidden enemy-drop pickups.
2,639 original-bytecode rows cover 144 loot combinations, eleven death arenas,
seven pickup scenarios, spawn events and animation-28 effects. Sixteen new
assertions bring the combined executable to 149; CTest remains 27 tests. General
spawn-pool aliasing and world UID/save ownership remain pending. Next: live
first-level enemy/body/combat/effect queues and player-death transitions.
NATIVE_LIVE_COMBAT.md connects seventeen ordinary level-0 enemies to the
movement preview; three flagged records stay dormant. 3,372 snapshots
compare reciprocal combat with original Game.updateWorldActors across seven
scenarios, including two-enemy order and completed player death. Twenty-four
actual kill/loot/defeat runs pass at widths 240/569 across presentation settings.
Five new assertions bring the combined executable to 154; four argument checks
bring CTest to 31 tests. The preview now provides interactive combat but not
full level progression. Next: hazard damage, remaining contacts/platforms and
fuller world/region ownership, then original HUD/menu/save/audio integration.
PORT_ROADMAP.md defines how those integrate with the game. Implement rendering
and the compatibility layer before gameplay, then verify at original 240x320
before expanding the camera and HUD to widescreen.

NATIVE_HAZARDS.md connects lethal and one-point tile hazards through live damage
ownership. Six controlled arenas extend LiveCombatCheck to 4,448 snapshots over
1,052 ticks and 99,745 original/reference comparisons, plus twelve birth-effect
rows checked against native output. Level 0 has no type-2/3
cells; later-level play-throughs remain pending. Six data-free assertions bring
the combined player checks to 160. A reusable Windows backbuffer also removes
the visible black clear between frames; 90 GDI paints verify canvas/bars/overlay
composition, resize and empty clients. CTest now has 32 tests. Next: remaining
contacts/platforms and fuller region/world ownership, then original HUD/menu/save/audio.

NATIVE_PLATFORMS.md adds type-33/50 landing/support ownership to the native player,
including supported walking/crouch/melee and jump/edge detachment. 10,944 snapshots
cover 4,320 contact setups and 1,152 controlled ticks across twelve scenarios;
251,727 original/reference comparisons pass. Nine data-free assertions bring
player checks to 169; CTest remains 32 tests. The displayed level does not load
these platforms yet. Next: type-33 movement, including rider-controlled variant 8,
and stable live platform ownership, then type-50 movement and remaining contacts.

NATIVE_MOVING_PLATFORMS.md now implements type-33 rail/rider controllers and
connects level 0's platform with stable support ownership and registry order.
11,520 snapshots cover 2,880 controlled ticks / eighteen scenarios, with 264,963
original/reference comparisons. Twelve actual first-level rides pass across
width/FPS/interpolation settings; combined player assertions reach 175.
NATIVE_STARTUP.md adds default interactive logo/title/menu and original credits
when data is present, plus menu-to-preview loading and back/restart behavior.
Original logo/title pixels match Python at 240/569 and twelve startup-to-game
runs pass. CTest now has 36 tests. The first-level entry remains explicitly a
preview; full menus, prologue/selection, persistence, audio and HUD are unfinished.
The next startup pass now connects New Game to original story string 40, its
Y=240 entry, prologue line spacing and held-key acceleration, then a native
first-level selection card using original strings 41/42. Four independently
composed story surfaces pass at widths 240/569; the complete boot script now
traverses prologue and selection. Selection only offers level 0. The original
animated selection map, save-backed unlocks/start decisions and Continue are
still pending; no save reset or complete original menu equivalence is claimed.
NATIVE_SWINGING_PLATFORMS.md now adds type-50 pendulum/support, weapon-0 chain
cutting, fall/break states and falling player/type-1 impacts. All six first-level
chandeliers are owned live. Fifteen controlled scenarios produce 15840 matching
native snapshots over 3600 ticks and 416171 original/reference comparisons.
Twelve real-level rides pass at widths 240/569 across presentation settings.
Six new data-free assertions bring player checks to 181; CTest has 39 tests.
Projectile chain attacks, type-45 falling activation and other damageable actor
types remain pending. Next: fuller world/region ownership and remaining object
handlers; the original selection map and save-backed startup also remain pending.

NATIVE_REGIONS.md now adds active/previous region ownership, deferred transition
consumption and ordinary enemy exit/return reconstruction without death loot or
respawn-count decrements. Eight isolated original-world scenarios produce 176
matching native snapshots and 354 original/reference expectations. Seven new
data-free assertions bring player checks to 188; CTest remains 39 tests. Exact
mixed region/actor dispatch, dropped-pickup cleanup, UID/pool/save ownership and
other handlers remain pending. Next: remaining world/object contacts, including
type-45 bridge interactions. Full gameplay remains unfinished.

NATIVE_BRIDGES.md now loads both level-0 type-45 bridges, adds shared mutable
collision rectangles and connects falling chandelier impacts in target-record
order. Eleven controlled scenarios yield 2040 matching native snapshots and
2051 original/reference expectations, including grid CRCs and either update
order. Twelve real-level chandelier/bridge runs pass across width/FPS settings.
Twelve new assertions bring player checks to 200; CTest has 42 tests. Projectile
activation, persistent removed UIDs and other mutable objects remain pending.
Next: transient pickup/effect cleanup on region changes and further shared world
ownership, followed by remaining object handlers. Full gameplay is unfinished.

NATIVE_TRANSIENTS.md now connects the pending region sweep to the separate
pickup/collection-effect owners before their handlers and new births. Placed
slots survive; discarded crystals cannot retain a modal request. The original
world sweep verifies 576 removal decisions and 4032 expectations, including UID,
persistent type/animation, respawnable flag and death/no-change boundaries.
Twelve real-level cleanup/new-birth runs pass across width/FPS settings. Fourteen
new assertions bring player checks to 214; CTest has 45 tests. Next: exact mixed
actor/region dispatch and further shared world/object integration; UID pools,
checkpoint/save reconstruction and full gameplay remain unfinished.

NATIVE_WORLD_ORDER.md now inserts type-22 regions into a typed, frozen update
queue for supported placed actors. Same-tick enemy clamps observe region changes
at their actual record position; later regions skip after the first change.
Twelve original-world fixtures produce 3840 matching snapshots over 480 ticks
and 1042 original/reference expectations, including full fixture update-UID
queues, overlaps, pause/hidden flags and previous-bucket eligibility. The paused
player contact path also preserves its original enemy-bounds refresh timing.
Twelve new assertions bring player checks to 226; CTest remains 45 tests. Next:
first-level linked object/trigger integration and remaining shared owners, then
checkpoint/save-backed progression. Full gameplay remains unfinished.

NATIVE_LINKED_TRIGGERS.md now connects first-level type-40 UID 182 to chandeliers
25/177 in the ordered world queue. The active-region overlap condition controls
forced updates with original lazy links, flag preservation and next-snapshot
eligibility. Twelve handler scenarios plus four actual original-world fixtures
produce 512 matching native rows and 515 original/reference expectations.
Twelve new assertions bring player checks to 238; CTest remains 45 tests.
Linked gates, other trigger modes/scripts, general UID ownership and checkpoint/
save-backed progression remain pending. Full gameplay remains unfinished.

NATIVE_GATES.md now loads first-level gate UID 154, including its construction
collision and doorway-clipped sprite, with linked opening/return and clear/hold
handlers. Sixteen controlled original/reference scenarios produce 2304 matching
native state/grid rows and 2310 expectations; 160 independent surfaces cover
widths 240/569. Twelve first-level load/region host runs pass across FPS settings.
Fifteen new assertions bring player checks to 253; CTest remains 45 tests.
The shipped gate has no UID links and its script-165 activation is not yet
executed natively. Next: native scripts and additional trigger/actor ownership,
then checkpoint/save-backed progression. Full gameplay remains unfinished.

NATIVE_SCRIPTS.md now adds native decoding for all 17 scripts / 47 tracks and
a selected existing-gate track runner for position/animation/action commands.
The complete script-165 track matches 1536 original-bytecode timer/gate/grid rows
across eight timelines and 1540 original/reference expectations. Twelve actual
host runs lift the gate and clear its doorway at widths 240/569 across FPS and
interpolation settings. Sixteen new assertions bring player checks to 269;
three argument guards bring CTest to 48 tests. Normal activation still waits
for script-locked chest 57 and its linked trigger sequence. Next: those owner/
reward dependencies, broader scripts and checkpoint/save-backed progression.
Full gameplay remains unfinished.

NATIVE_REMOVED_TRIGGERS.md now connects the real source-container owner to trigger
166 and selected gate script 165. Fresh placement indices, lazy source caching,
the original index-zero shortcut and one-time start/completion cleanup match
1536 original-bytecode rows and 1539 original/reference expectations. Twelve
controlled first-level removal/trigger/gate runs pass across widths/FPS settings.
Fifteen assertions bring player checks to 284; three argument checks bring CTest
to 51 tests. Normal chest 57 remains locked: its reward -2 spawns a type-48
ability-2 actor, whose ownership/reward and preceding unlock sequence are next.
Full gameplay remains unfinished.

## Phase 4: validation and distribution, pending

Compare behavior and screenshots with the original build at native width.
Add deterministic input scripts, save round-trips, resource and gameplay
regressions, and play through every level at each aspect/FPS preset. Build a
launcher that imports the user's own JAR and checks its expected identity.
Only configure release/update hosting after a real destination is supplied.
Current CI artifacts are explicitly scaffolds, not playable releases.


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

NATIVE_FLYING_ENEMIES.md now loads all seven first-level behavior-zero type-2
enemies and connects removed-source trigger 54 to the first wave's final
removals. Original fixed-point pursuit, windup/strike/recovery, physical damage,
body contacts, shared death/loot and mixed enemy queues have 4655 native rows
matching original-bytecode fixtures (5985 original/reference expectations).
Twenty-four two-wave host fights produce six real melee defeats at widths
240/569 across FPS/interpolation settings. Seventeen assertions bring player
checks to 353; CTest is 63 tests. Next: trigger 55's chest enable effects, then
normal chest 57/reward/gate progression. Full gameplay remains unfinished.

NATIVE_CHEST_UNLOCK.md now connects trigger 55 to flying UIDs 113/48 and
chests 56/57, including the original reveal effects and next-tick handler
eligibility. Normal first-level gameplay now connects both waves, chest 57's
magic reward/tutorial, actual chest removal and trigger 166/script 165's gate
opening. Seven original-world arenas provide 2247 native-comparable rows and
568 original/reference expectations; seven timed milestones and 24 host chains
check the complete connection at widths 240/569 across FPS/interpolation.
Eleven assertions bring player checks to 364; CTest is 66 tests. Next: magic
casting/weapon controls and remaining first-level progression, then broader
scripts and checkpoint/save integration. Full gameplay remains unfinished.

NATIVE_FIRE_MAGIC.md now connects K/0 to weapon-0 magic after the ability unlock,
including grounded/crouched/MP/occupied-slot gates, charge pose, 15-MP spending,
six radial fire effects, ordinary enemy damage/removal and timed MP recovery.
The charge preserves the original random draws on simulation ticks; drawing
stays independent of FPS. Fourteen original arenas provide 4790 native rows
and 3380 original/reference expectations. Seven milestones and 36 host runs
check actual fire defeats and normal arena-unlock-to-casting at widths 240/569
across FPS/interpolation. Seventeen assertions bring player checks to 381;
CTest is 69 tests. Next: the separate ice pickup, weapon-1 melee/projectile
owner and switching controls, alongside remaining first-level progression and
checkpoint/save integration. Full gameplay remains unfinished.

NATIVE_ICE_WEAPONS.md now ports the second-level chest's -5 reward and type-48
sprite-18/animation-35 pickup, preserving ice ownership/tutorial without MP
refill or automatic equipping. Q/9 now switches owned weapons with the original
450 ms cooldown and input priority. Ice physical poses/combo/air control and
ordinary enemy damage have 6106 native rows matching original-bytecode fixtures
(6118 original/reference expectations). Seven milestones and 24 relocated-chest
host chains verify reward/tutorial/equip/ice defeat/return-to-fire at widths
240/569 across FPS/interpolation. Sixteen assertions bring player checks to 397;
CTest is 72 tests. Ordinary first-level placements still have no ice reward:
the shipped chest is in level 2. The following pass now covers ice casting and
temporary wall footholds; linked attachments remain open.
Full gameplay remains unfinished.

NATIVE_ICE_PROJECTILES.md now connects equipped ice magic to K/0, preserving
charge gates/RNG, 15-MP spending, ordinary ground/flying enemy damage, wall and
six-slope probes, both projectile slots and temporary foothold ownership.
Landing, jumping, walking off, wall loss, expiry, replacement while riding and
region cleanup release support on simulation ticks. Forty original arenas
provide 17602 native rows and 14429 original/reference expectations. Seven
milestones and 24 host chains check natural ice defeats, wall freezing,
jumping onto the foothold and replacement/detachment at widths 240/569 across
FPS/interpolation. Eleven assertions bring player checks to 408; CTest is 75
tests. Next: linked ice/object attachments and cleanup, then remaining level
completion/loading and checkpoint/save integration. Full gameplay remains unfinished.

NATIVE_ICE_ATTACHMENTS.md now verifies type-33 links, immediate parent-owned
child updates, ordinary queued updates and melt/rider/teardown cleanup. The
original branch requires the shot's attack rectangle, which is a point in the
shipped data: six natural arenas preserve pass-through, while eleven explicitly
controlled-link arenas cover the gated branch. Seventeen original-world arenas
provide 18411 native rows and 6157 original/reference expectations. Seven
assertions bring player checks to 415; CTest remains 75 tests. No asset/hitbox
change invents a moving-object attachment in normal play. Next: remaining
first-level progression, checkpoint/restart ownership and level completion/
loading. Other gate/trap weapon targets need their own controllers. Full
gameplay remains unfinished.

NATIVE_CHECKPOINTS.md now loads all six first-level type-23 checkpoint volumes
and connects R and pause Restart Level to the latest in-session snapshot.
Position/facing, ported meters/fragments, equipped blade and ice/magic ownership
restore; health fills to its saved maximum and saved MP is retained. Removal
filtering preserves collected checkpoints/pickups and flag-512 UIDs while
ordinary enemies reconstruct. Twenty-three original fixtures provide 6785
native-comparable rows and 82 original/reference comparison expectations,
including two restarts and every first-level actor's presence decision.
Seven milestones and 24 host chains cover defeat, R/menu restart and MP
recovery at widths 240/569 across FPS/interpolation. Fifteen assertions bring
player checks to 430; CTest is 78 tests. This snapshot is in memory only and
does not establish original save-file compatibility. Next: disk-backed saves
and Continue, then remaining first-level progression and level completion/
loading. Full gameplay remains unfinished.


NATIVE_SAVES.md now persists first-level checkpoint snapshots in a versioned,
checksummed desktop slot under LocalAppData. Continue appears for a valid file,
retains saved HP/MP and uses the original fresh-persistence/removed-UID distinction;
R/menu Restart still refill HP. New Game confirms before clearing progress.
Storage uses checked temporary-file replacement; malformed/version/size/range/
checksum failures disable Continue with a visible error. Automation isolates the
user slot unless --save-file is explicit. Data-free roundtrip/corruption/reset
checks and a CLI guard bring CTest to 80 tests. Twelve new-process Continue runs
verify widths 240/569, 30/60/144 FPS and interpolation, with additional boot/reset/
corrupt/missing-save checks. The file is not original RMS compatible and contains
only the supported native subset. Next: remaining first-level actors/progression
and level completion/loading. Full gameplay remains unfinished.


NATIVE_BEAMS.md now loads all six first-level type-10 hazards in record order,
including activation, warning/charge/recovery, rotating sweeps, tile raycasts,
player damage/knockback and endpoint glows. 8832 native rows match original
controller and damage fixtures (8844 original/reference expectations). Seven
milestones and 24 actual-map host runs cover stationary hits and sweeping-beam
defeat at widths 240/569 across FPS/interpolation. Nine assertions bring player
checks to 439; CTest now has 83 tests. The cap has bounded native ownership,
not a general actor-registry/painter equivalence claim. Next: remaining first-
level tutorial/camera/script dependencies and level completion/loading. The
complete game remains unfinished.
