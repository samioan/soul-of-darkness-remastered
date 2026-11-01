# Class map and verified decompiler corrections

| Original | Reference name | Evidence |
|---|---|---|
| `GloftSOFD` | unchanged | Extends MIDlet; creates `e`, sets display, starts its thread; pause/destroy lifecycle |
| `e` | `Game` | Extends Nokia FullCanvas, implements Runnable; state-dispatched paint, keyboard events, camera, resources, MIDI/WAV, RMS |
| `a` | `InGamePromotion` | Literal IGP-Signature=1.8, `/dataIGP`, URL-WN/URL-BS/URL, MIDlet platform requests, igp16/glive stores |
| `b` | `Actor` | Linked instances, position/velocity-like fields, collision rectangles, player `e.bk`, action-specific updates, `d` sprite reference |
| `c` | `ErrorReporter` | Stores first Exception; `a(Exception,String)` prints ERROR and stack trace |
| `d` | `BSprite` | Loader explicitly checks BSprite version 991; module/frame/animation arrays, palette/image decode, drawRegion/drawRGB |

Actor combines a substantial amount of gameplay and object-specific behavior;
its class name does not imply a completed member map. The Game/BSprite/Actor core passes now
map 236 fields and 354 methods, in addition to the five class names. The exact
mapping lives in `names.map`. Remaining members keep their original names.
Reference source is generated using the bytecode renamer in
`tools/rename_sod.py`, never a global regex replacement.

`e` also has a bytecode field named `do`, which is a Java keyword; it is now
mapped to `menuNewBadge`, the image loaded from `/new.png` and drawn alongside
promotion menu entries. Unmapped keyword symbols still get an automatic suffix.

The sole compile-blocking artifact found during setup is in original
`e.d(int)` (camera update), recovered as `if (!var3)` despite `var3` being an
int set to 0/1. Original instructions at offsets 501–502 are `iload_3` /
`ifne 602`: enter the following block when slot 3 is zero. The reference
patch changes that expression to `if (var3 == 0)`, asserting one match.
The script pass found a second artifact that compiles but changes behavior.
In `e.b(int,boolean,int)`, offsets 21..32 preserve the old script counter with
`dup_x2` while incrementing/storing the short array value, then store the old
value in local 9. Vineflower emitted increment-then-read; an asserted patch
restores `short var9 = scriptTicks[var0]++;`. The original/reference timeline
failed before the patch because commands ran one tick early.
Reproduce the evidence with `python tools/bytecode.py e`. These patches preserve
original timing; no widescreen or presentation FPS changes are made in Java.

## Core Game member pass

Original source line ranges below refer to `soul/decompiled/e.java`.
`python tools/outline.py Game` finds current reference-method locations.
STATE_MACHINE.md and INPUT.md record call-site behavior in more detail.

| Renamed group | Evidence and review boundary |
|---|---|
| MIDlet `instance/game/display`; ErrorReporter `firstException/report` | Complete small classes read. Display startup, thread creation, lifecycle and exception storage are explicit. |
| Game `state/previousState/stateFrame`, suspension and frame timestamps | Constructor 410..417, paint 419..506, transition 730..863, run/pause/resume 865..908. 83 ms fixed tick and repaint completion handshake. |
| Screen handlers, menu definitions, selection and confirmation | Paint switch, handlers 1687..1975 / 2451..2665 / 4303..4444 / 5160..5175 / 5683..5904; menu action values checked against menu text and transition callers. State 30 reachability is unconfirmed. |
| Font/sprite table, Graphics delegates and softkeys | Font initialized from `/1` resource 0, text drawn through its BSprite methods. Graphics delegates 910..948 each forward directly. Softkeys 1639..1668 swap the two arguments before drawing their left/right labels. |
| `player`, camera position/target, visible bounds and shake counters | Player instantiated for object type 0 in `c(boolean)` (3233..3301); camera 1208..1561. Pixel dimensions are map dimensions multiplied by 20 at 3888..3922. No full Actor schema is claimed. |
| Input masks, queries, latching and menu cheat detector | 5177..5382; mapping and callback operations checked against javap. Held events and frame-latched edges are distinct; see INPUT.md. |
| Pack stream, part/offset metadata, seek/read functions | 4142..4301. Sequential stream reads update position; close resets position while retaining pack metadata. Actual tables validated by inspect_assets.py. |
| Resource/sprite/string accessors and binary primitives | 4053..4140. Sprite resource bytes enter BSprite decoder. Strings skip six header bytes, read BE count and Java modified UTF; byte array helpers use signed byte/short and LE integers. |
| Save buffer / loaded flag / RMS accessor | Buffer size `999 + 250 + 64 = 1313`; accessor 5133..5154 uses record 1 in `/dvseed.sav`, boolean true writes and false reads. The loaded flag is set only after reading an existing record. SAVES.md maps buffer fields and selected restoration paths; full save integration remains pending. |
| Sound data, player, remembered ID, resume delay and media helpers | Visibility callbacks 676..702 and audio 5906..6021. `loadSoundData(level)` maps a level index to pack 19 resource; `playSound(id,loops)` uses already-loaded bytes. Only IDs 0..3 update rememberedMusicId, although level mapping can return 4. This quirk is preserved. |
| Trigonometry and arithmetic helpers | 4011..4051 plus `/17` table: amplitude 256, quadrant samples +256,0,-256,0. Therefore original `k` is `cos256`, and `l(angle)=k(90-angle)` is `sin256`. `randomRange` has an exclusive upper bound for ordinary nonnegative random magnitudes; the original MIN_VALUE overflow case is preserved. |
| `replaceToken` | 6023 onward; removes occurrences, records insertion positions and reinserts replacement. Do not replace this algorithm without checking its fixed 15-position storage and edge behavior. |

## Behavioral verification

Default regeneration now compiles both the Java reference and a reflection-based
checker, then loads the original classes and reference classes in isolated
class loaders against the same API JARs. It executes supported pure/static paths
and compares results, using independent format/round-trip expectations where
available. It checks key mapping and static input latching, selected screen
transitions with sound disabled, signed binary accessors, music lookup, all
136 strings from pack 15, and sine/cosine over -720..720 degrees including
quadrant expectations. No extracted/decoded resources are written by the check.

This is not a MIDP runtime or a gameplay equivalence proof. Canvas construction,
event callbacks, rendering, audio, save storage and actor simulation are outside
that core checker. ActorCheck separately covers controlled actor scenarios.
The checks catch renamed-reference regressions and
decompiler errors in the core methods before native translation.

## Complete BSprite pass

All 55 original fields and 29 methods in `d` now have names in `names.map`.
The loader (original lines 61..315), image construction (317..399), metadata/
bounds (401..570), drawing (572..756), pixel decoder (758..900), caches
(902..929), and font layout/drawing (931..1124) have been read with their
Game/Actor call sites. BSPRITE.md records the field groups, feature flags,
palette/PNG formats, transform semantics, animation tick units and font quirks.

The original and regenerated loaders, pixel decoders, bounds/accessors, selected
cache operations and font helpers are executed in SpriteCheck.java. That
checker and ActorCheck's asset loader use the minimal ImageIO test adapter for PNG/RGB Image creation;
it does not emulate Graphics/Canvas. Pixel hashes and bounding rectangles are
then independently compared by the Python parser using an ignored oracle file.
Neither the adapter nor the parser is a full game runtime.

## Actor core pass

69 Actor fields and 75 methods are now mapped, with additional Game actor-list,
health/magic and collision accessors. ACTOR.md documents original source ranges,
record prefixes, eight-fractional-bit integration, bounds and collision edge
semantics, tile slopes, player animation/control flow and test boundaries.

ActorCheck loads original/recompiled Actor and Game classes in separate loaders,
using the same test Image adapter for sprite assets. It compares common updates,
geometry, animation clocks/queuing, tile probes, and seven controlled player
scenarios. Its ignored trace is recorded before animation advancement and can
be visualized with actor_shots.py. No native gameplay or full-level validation
is implied. Those counts describe the core pass before the additions below.

## Enemy/object handler pass

Seven fields and 96 methods were added after the core pass. Constructor,
type dispatch and action-entry call sites establish packed variant/stat access,
active boss ownership, shared targeting/damage helpers, interaction bounds,
enemy/projectile/object handlers and linked-actor operations. Scratch storage
keeps its original names where its role changes across types. See
ENEMIES_AND_OBJECTS.md for dispatch roles, stat strides and examples of state
transitions. A complete per-variant state map remains open.

BehaviorCheck loads real serialized parameters and stat/sprite/string/trig
resources into controlled original/reference arenas. It compares full Actor
fields/statics and selected Game side effects for 16 types, including spawned
actors, plus independent expected-state assertions for selected active cases.
It uses the same Image adapter and does not run full levels, Graphics or RMS.

## Script and level map pass

Eleven fields and 27 methods were added for script storage/lifecycle, the timed
interpreter and opcode/global dispatch, cinematic actor lookup, tile-map loading
and sprite dependency collection. Original methods `e.a(byte[])`,
`e.b(int,boolean,int)` and `e.a(int,byte[],int,int,int,b)` establish parser,
timeline and extended-opcode behavior; `e.b(int,boolean)` loads grids through
`e.L(int)` and `e.M(int)`. Callers are the staged level loader and the gameplay,
trigger and boss cutscene paths. See SCRIPTS_AND_LEVELS.md for the binary layout,
opcode table, timing correction, actual data mismatch and test boundaries.

## Persistence pass

Seven methods now name progress/removed-UID serialization, persistence/progress/
checkpoint restoration and the two linked-actor snapshot helpers. Checkpoint,
completion, continue and restart callers establish their roles; original source
ranges and bytecode evidence are in SAVES.md. SaveCheck runs the unchanged
original methods and generated reference with independent buffer expectations.
It preserves the original swap of Actor `bp`/`bq` on restoration. Full level
construction, checkpoint activation and RMS storage remain outside this harness.

## Level actor construction pass

Four methods now name stream construction (`e.c(boolean)`), reconstruction with
level 5 saved links (`e.d(boolean)`), the exact restart-removal type predicate
(`e.I(int)`) and post-insertion tile collision finalization (`b.a(short[])`).
Callers are load step 79, the reconstruction/restart path and ordinary stream
insertion. LEVEL_ACTORS.md records original source locations and bytecode evidence
for inclusive UID scanning and duplicate type 69 construction. LevelCheck covers
all shipped streams in three modes and selected level 5 snapshot relinking.
It does not run the full staged loader or gameplay.

## Checkpoint/restart lifecycle verification

LifecycleCheck adds controlled original/reference checkpoint collision, RMS
accessor and complete restart execution on all nine levels, plus repeated loads
and respawn-table/pool alias checks. Source evidence is original b.ao() at 2127,
e.al() at 3856 and b.m() at 1280; bytecode confirms restart ordering and the
pool-cursor reset. CHECKPOINT_LIFECYCLE.md records coverage and limits. A separate
test harness supplies in-memory RMS; the other checkers retain their prior API
boundaries. No new member renames or decompiler patches are required by this pass.

## New-game/Continue pass

Three fields and four methods name level completion, checkpoint Continue,
position-restore suppression, default meters, selected-level stat setup, tile
buffer reuse and camera-region selection. NEW_GAME_AND_CONTINUE.md records the
original source locations and callers. Original menu action 2 and load stage 79
confirm that Continue forces fresh construction inside the reconstruction wrapper
before restoring checkpoint position/progression. MenuCheck compares that path,
next-level progression, confirmed New Game and selected-level stat preparation.
Its pre-seeded buffer/opaque Graphics fixture performs no rendering; the full
staged loader remains outside this pass. No additional decompiler patch is needed.

## Level 5 controller/save pass

Five Game fields now identify active controller UID, hazard camera-relative Y
offset, controller mode, active overlap and scrolling axis. Constructor records,
original b.bi()/b.bc() handlers, camera call sites and checkpoint serialization/
reconstruction establish these names. LEVEL5_CONTROLLER.md records source and
bytecode evidence. ControllerCheck covers activation, three constructor-linked
platform snapshots through checkpoint capture, Restart/Continue restoration,
signed camera/offset arithmetic and a resumed common hazard update. It preserves
the saved-offset local without inventing a global assignment. No patch or native
gameplay implementation is introduced by this pass.

## Native collection-effect evidence

No renames or generated-source patches were needed. Original `b.ad()` changes
the pickup action to 100 and spawns type 39/sprite 1/animation 11 or 12 at
priority 308; `b.bG()` removes those effects at animation ending. Call sites
in `updatePickup` cover contact, expiry and resumed crystal action 104.
`Actor.paint` hides pickups in actions 100/101 and computes the effect ring
diameter from its animation step/count. Both shipped effects have one frame,
so that diameter remains zero. Original bytecode and the 88-row BehaviorCheck
oracle confirm this limited lifecycle; see NATIVE_COLLECTION_EFFECTS.md.

## Native player side-contact evidence

No renames or decompiler patches are introduced. Original `b.x()` runs separate
reverse-order priority (types 37/80, cap 2560) and ordinary (cap 9) contact passes.
`b.a(b,int)` uses O/P/Q/R/S and wall/ground flags to select displacement;
`b.ai()` has a separate late player-separation branch selected by N and !O.
Constructor assignments, `updatePlayer`, `updateEnemyType1` and gate/platform
handlers establish the inspected roles. Original bytecode preserves asymmetric
velocity conditions and different pass filters. NATIVE_CONTACTS.md records the
628 bounds, 7,056 arenas and 384 controlled player ticks used for the native
port. Full actor queues, enemy AI and platform/link ownership remain separate.

## Native weapon-0 melee evidence

No renames or generated-source patches were needed. Original player `b.aw()`
selects poses 13/14/15, `aj()`/aU queues the second standing strike, and `db`
gates attack recovery. `b.a(int,int,int)` activates the type-28 slash at player
frame/tick zero, while `b.ah()` follows player position/facing; original
`d(b)`/`e(b)` reset/enable and hide/inactivate it. The type-28 constructor,
level-0 UID 191 record, dispatch and weapon-0 overlap predicate were inspected
with bytecode before native translation. NATIVE_MELEE.md records 1,920 snapshots
including pose/bounds, combo, recovery, slash state and target queries. Enemy AI,
damage application and complete combat remain outside this pass.

## Native type-1 AI evidence

No renames or generated-source patches were needed. Constructor record[10]
selects cE, record[11] selects stat row 0/stride 3, and record[6..9] supplies
activation bounds. Original `b.aH()`, `c(int,boolean)` type-1 entries, `g(int[])`,
`g(boolean)` and `t(int)` establish activation, pursuit, interaction reach,
attack/rest timing and ledge/fall behavior. Bytecode confirms the low variant
bit and 400/500 ms timers. Flag 32 does not gate this type's update. The native
AI checker covers 4,032 snapshots; NATIVE_ENEMIES.md describes the synthetic
non-damaging player rectangles and empty-queue boundary. Damage, hurt/death
entries and live host integration remain pending.

## Native damage and hit-response evidence

No names or generated-source patches change. Original `b.e(boolean)` gates
weapon hits before rolling damage; `b.b(int,boolean)` spawns the unadjusted
number, applies bM/bN reductions, emits blood and enters hurt/death. Type-1
forced action 2 restarts animation 3 and knocks away from the player. `b.c(int)`
scales player damage and grants invulnerability; `a(int,int,int,b)` provides the
ordinary knockback entry. Player poses 8/9 and effect handler `bG()` establish
recovery/death clocks and the double decrement for number animation 29.
Game `f(int,int)` preserves signed abs/remainder, including MIN_VALUE overflow.
Native CombatCheck covers 2,591 oracle rows; see NATIVE_COMBAT.md for explicit
entry/queue boundaries and pending enemy death cleanup. Bytecode was checked
for Actor and Game before translating these paths.

## Native ordinary type-1 death/reset and loot evidence

No renames or generated-tree changes were needed. `b.aH()` action 3 invokes
`resetOrRemove(true)`, which selects loot, decrements di, restores eligible
records through af/ag or removes the actor, preserves flag 512 and optionally
starts a fade. The initializer and setAnimationImmediate establish retained
fields and same-animation pending-clock behavior. `updateRespawnFade` controls
5/6/7 and animation-28 emission. Original `dropLoot` preserves the reversed
randomRange(-20,-100), while pickup `b()` and common/106 az decrements establish
1162 ms hidden delay and seventh-update reveal. Bytecode and 2,639 native/original
rows cover this owned-record subset; general Game pool/UID/save aliasing is not
implemented by it. See NATIVE_DEATHS.md.

## Connected first-level combat evidence

Original Game `W()` updates the player before rebuilding and iterating its
registry-ordered update queue; Actor player contacts therefore use the preceding
queue. `h(b)` establishes bucket/flag update eligibility, while paint sorts by
priority and refreshes bounds before clocks. Original type-1 dispatch applies
weapon damage or player contact damage before facing synchronization. Call-site
inspection plus 3,372 original-world-update snapshots verify the connected
ordinary-enemy subset, including two-enemy order and the lethal-contact bounds
boundary. No Java renames/patches change. NATIVE_LIVE_COMBAT.md documents the
prototype camera/region policy, flagged records and unfinished world
ownership rather than treating the controlled world checker as a full game.

Flag audit clarification: Actor flag 2048 is consulted by resetOrRemoveInternal
for terminal removal after death, not by shouldUpdateActor as a script pause.
Two of the three flagged level-0 records also have flag 32. CombatWorld currently
leaves all three dormant because their removal ownership is outside this slice;
the seventeen supported records run live. Earlier boundary labels calling every
2048 record script-linked were too broad.

Native tile dispatch follows original Actor.aw bytecode offsets 324–386:
feet/head/above-feet type 2 selects immediate pose 9 before type 3 can invoke
a(4,1,0,this). Pose 9 writes health zero at frame 1/tick 0 and changes to state 12
on its ending frame. Six additional original Game.W arenas verify those paths,
including mixed-tile priority and invulnerability; see NATIVE_HAZARDS.md.
No new renames or generated-source patches were needed.

Platform evidence: original Actor.d(int,int[]) refreshes the surface, checks
predicted player Y and inclusive X, sets static aK/playerPlatform, requests pose 6,
and clears type-50 moduleMask[11] on first landing. Its retained-support branch
refreshes player bounds and distinguishes type-33 top+1 from type-50 top.
Player aw/ay/az/aA consult this link independently of grounded and clear it on
jump. NATIVE_PLATFORMS.md records 10,944 matching controlled native snapshots;
movement handlers and live platform registry ownership remain separate work.

Type-33 movement now follows original Actor.s carrying before integration and
S/updateMovingPlatform's two support checks, rail direction/delay logic and
rider-controlled mode 8. Bytecode confirms the mode-4/5 extra shift and mode-6/7
unshifted diagonal formula. The controlled original checker adds 264,963
comparisons; NATIVE_MOVING_PLATFORMS.md records scope and live ownership.
Startup evidence: e.N loads /logo.png and changes after frame 47; e.as loads
1:1 title art and blinks common string 10; e.ax scrolls common string 28 in the
portrait clip. The credits entry replaces X.X.X with MIDlet-Version. Native
startup uses these assets with an explicitly partial native menu and synchronous
loading boundary; NATIVE_STARTUP.md distinguishes it from full original boot.
Prologue evidence: e.changeState(20) sets scrollingTextY=240; e.ax selects
common string 40, loops when drawWrappedText returns negative, and subtracts
four extra pixels for either held vertical-key mask. BSprite.drawWrappedText
adds three to line spacing in state 20. Next enters state 19 and sets bP=true.
e.e(boolean) draws chapter string 41 and level name 42+dh; e.av gates horizontal
navigation on maxUnlockedLevel. The native fresh-start flow uses story timing
and those labels, but its single-level card does not port the animated map,
stat/save preparation or unlocked-level traversal. No new semantic names added.

Type-50 evidence: initialization and Actor.n construct signed rope/pivot state
and the wider visibility envelope; br uses one support check before its action
switch. V advances angle by max(at,0)<<8, preserves center/turn latch order and
changes speed after direction decisions. T restores selected mask slots and
placement, sets latch 11, then stops motion. U/j test chain attacks; br falls,
checks damageable queue actors/type 45, resolves vertical tiles and enters
animation 12 then hidden state 104. Original bytecode confirms the position
formula's add-before-shift and next-tick velocities. Game.an/k/l load and index
pack 17's 256 integer trig entries. Game.a(seven ints) draws segmented chains.
NATIVE_SWINGING_PLATFORMS.md records 416171 controlled original/reference
comparisons, live integration and excluded branches. No new renames or patches.

Native region lifecycle evidence (no new renames): `Game.k` selection,
`Actor.aD` point containment, `Game.a(Actor)` active/previous region storage,
`Game.W` new-region-first overlap and `Actor.b(false)` /
`Actor.a(false,false)` spawn-record restoration were inspected with original
`e`/`b` bytecode. RegionCheck runs these original/reference boundaries with
updates paused; see NATIVE_REGIONS.md for coverage and native integration limits.

Type-45 native bridge evidence (no new names): constructor sprite 36/custom
bounds/priority 2/radius 4, `Actor.bf` idle/collapse/removal timing,
`Actor.a(int[],int)` cell-centre fill and `Game.c(int,int,int)` clipped writes,
type-50 `Actor.br` ordered bridge impact before tile resolution, and custom draw
segment/debris arithmetic were inspected against original `b` bytecode.
BridgeCheck compares original/reference handlers and collision-grid CRCs;
NATIVE_BRIDGES.md records the limits of the native port.

Native transient sweep evidence (no additional names): original `Game.W`
bytecode confirms the post-player death guard, respawnable/basic-enemy precedence,
exact UID -1 comparison, retained types 84/85 and type-39 animation-13 exception,
and removal before queue construction. TransientCheck executes original/reference
world sweeps with paused ordinary handlers; NATIVE_TRANSIENTS.md distinguishes
the decision matrix from supported native pickup/effect owner tests.

Native ordered-world evidence (no new names): `Game.W` registry-order snapshot
and post-change region skip, `Game.h(Actor)` flag-16 precedence, type-22
constructor flags, `Actor.aD` / `Game.a(Actor)` bounds/pending state and
`Actor.aS` end-of-update clamp were checked against original `e`/`b` bytecode.
WorldOrderCheck runs actual original/reference world ticks with live enemies and
a controlled individually paused player; NATIVE_WORLD_ORDER.md records supported
queue boundaries and the separate camera/render responsibilities.

Native linked area evidence (no new names): original `Actor.bh` condition zero
reads `Game.au.collisionBounds`, not the player bounds; operation two calls
`Actor.k(Actor)` on overlap and `Actor.m(Actor)` outside it. These preserve
unrelated flags while toggling 16/32. Constructor record slots and lazy UID
lookup, plus `Game.h(Actor)` type-40 radius seven and `Game.W` frozen dispatch,
were inspected against original bytecode. LinkedTriggerCheck covers original/
reference handler and world snapshots; NATIVE_LINKED_TRIGGERS.md records the
platform-only target scope and unported script/gate boundaries.

Native gate evidence (no new names): type-31 construction, `Actor.be` link
resolution/action transitions, `Actor.h(int)` anchored collision fill, common
pre-handler fixed integration and `Actor.l(int,int)` / BSprite boundary masks
were inspected against original `b` bytecode. GateCheck compares clocks, bounds,
fixed synchronization and collision CRCs across sixteen scenarios. Script 165's
actor-154 track explains the unlinked first-level gate's clear/hold activation;
NATIVE_GATES.md records the pending native script ownership.

Native gate-script evidence (no new names): original `Game.b(int,boolean,int)`
post-increment counter, one-key-per-track dispatch, inclusive interpolation,
animation-at/past-key and exact-tick opcode-106 behavior were checked against
`e` bytecode. `Game.t/u/v` start/stop/restart distinguish retained/rewound offsets.
GateScriptCheck executes the real script-165 actor track plus gate handlers and
grid writes. `Actor.b(int)` changes animation without refreshing bounds; the
subsequent script visibility/gate/paint path supplies the refresh. NATIVE_SCRIPTS.md
records selected-gate execution and excluded full-cutscene/trigger ownership.

Native removed-source evidence (no new names): `Actor.bh` caches source actor
persistence indices; `Actor.a(int,int,boolean)` checks bD == -99 plus the original
index-zero shortcut. `Actor.a(byte)` / H / c(byte) register, poll, rewind and
release selected scripts before trigger removal. `Game.c(boolean)` assigns
nonplayer record indices and `Game.c(Actor)` marks removed slots. Selected script
updates precede the world tick. Original `Actor.ag` shows chest 57's reward -2
creates a type-48, sprite-27 ability actor. RemovedGateTriggerCheck exercises
controlled removal, actual selected-script lifecycle and gate/grid state;
NATIVE_REMOVED_TRIGGERS.md records the separate container-owner boundary.


Native passage evidence: original Actor.bt (type-59 dispatch) and Actor.bs
(relative-height mapping), plus initializeFromRecord's type-59 target/mode fields,
are checked against original bytecode and 464 player/handler snapshots. First-level
UID 81 links (1228,37) to UID 80 (1542,100), explaining the apparent tower cutoff.
All 16 level-0 records use horizontal modes 1/2. No new Java names were introduced.
See NATIVE_PASSAGES.md for native queue, region, fixed-coordinate and view limits.


Native magic reward evidence: Actor.ag emits (48,27,2) for reward -2; Actor.bg
sets bu, refills magic, enters pose 25 and transitions through attraction/float/
removal. Actor.createActor establishes birth fixed coordinates and inherited
facing. Game.e(int,int), Game.f/a/c/d/l establish attraction and beam/tutorial
clocks. Original bytecode and 2002 native-comparable snapshots cover these
selected branches. No new Java names were introduced. See NATIVE_ABILITIES.md.


Native enemy activation evidence: Actor.bh's condition 1 / actor mode 0 / operation
0 uses Actor.g to refresh source bounds, enables referenced enemies and removes
the one-shot trigger. Actor.l clears flags 32 and (outside action 5) 128.
Actor.resetOrRemoveInternal applies flag 2048 only to final death removal, with
flag 4 and respawn count taking precedence. Game.W freezes eligibility before
dispatch, and Game.drawActors / Actor.isVisible refresh nearby waiting bounds.
2146 native-comparable rows cover these selected branches; no new Java names
were introduced. See NATIVE_ENEMY_ACTIVATION.md.

Native flying-wave evidence: Actor.updateEnemyType2's behavior 0 (cE[4]),
changeActionState's type-2 entries, Game.e(int,int) distance and getStat's table-2
selection establish flight/attack/damage behavior. Actor.createActor uses a
facing toggle, including the death effect. Actor.bh condition 4 caches source
persistence indices and treats missing first/second sources differently;
Actor.a(int,int,boolean) preserves the index-zero shortcut. Game.W retains
placed dispatch order and frozen eligibility. The original b.bytecode.txt / e.bytecode.txt dumps
and 4671 native-comparable rows cover selected branches; no new Java names
were introduced. See NATIVE_FLYING_ENEMIES.md.

Native chest-unlock evidence: original Actor.l(b) clears target flags 32/128
and additionally calls spawnActor(39,1,28,0,0,302,false) for type 52. Actor.bh's
condition 4 / actor mode 0 connects flying UIDs 113/48 to chests 56/57 in shipped
trigger 55. Game.W freezes eligibility before placed dispatch; chests precede
that trigger and become handler-eligible on the next tick. 2247 native-comparable
world/chest/reveal-effect/RNG rows cover the selected branch (568 original/
reference expectations); no new Java names were introduced. See NATIVE_CHEST_UNLOCK.md.

Native fire evidence: Actor.updatePlayer's mask-1/pose-18 branch and Game.f's
charge/spend/birth branch establish the first spell. Actor.bG/updateEffect
animations 16/17, applyWeaponEffectHits(0), spendMagic and regenerateMagic
establish radial bounds/timing, damage, slot release and MP recovery. The
original b/e bytecode also confirms charge drawing's randomRange calls and
the 15-MP / six-or-nine-pulse tables. 4790 native-comparable rows cover selected
player/world/effect/enemy/rank cases (3380 original/reference expectations).
No new Java names were introduced. See NATIVE_FIRE_MAGIC.md.

Native ice evidence: pack 4 UID 59 reward -5 and Actor.ag/updateContainer emit
(48,18,35), while Actor.bg/updateAbilityPickup grants bs/tutorial 53 without MP
refill or auto-equipping. Actor.f(int)/switchWeapon and updatePlayer's mask-512
branch establish ownership/cooldown/combo behavior; poses 19/20/21 and aW establish
ice physical control. Original b/e bytecode and 6106 native-comparable pickup,
melee/switch/query and actual-world combat rows cover the selected branches
(6118 original/reference expectations). No new Java names were introduced.
See NATIVE_ICE_WEAPONS.md for that physical weapon boundary.

Native ice projectile evidence: Game.f(4,...) and Actor.updatePlayer poses
22/23 establish charge/spend/birth; Actor.bG/updateEffect cases 7/8/9/10
establish flight/freezing/standable/melt, aR slot replacement and aK rider
detachment. Original b/e bytecode and 17602 native-comparable rows cover selected
cast gates, MP/RNG, ordinary enemy hits, walls/slopes and player support
(14429 original/reference expectations). No new Java names were introduced.
See NATIVE_ICE_PROJECTILES.md for this wall-foothold boundary and
NATIVE_ICE_ATTACHMENTS.md for the following type-33 attachment analysis.

The following type-33 ice-link pass checks Actor.A/applyWeaponEffectHits,
setAttachedActor, updateMovingPlatform and Game.W's immediate child update.
Original b bytecode reads shot attackBounds (k) at this gate; supplied sprite-1
flight bounds are a point and cannot trigger it. Six natural and eleven
explicitly controlled-link arenas add 18411 native rows and 6157 original/
reference expectations. Parent/child ordering, asymmetric offset storage,
melt and rider release are preserved. No Java names or assets changed.
See NATIVE_ICE_ATTACHMENTS.md for remaining gate/trap target boundaries.

Native checkpoint evidence: Actor.ao/updateCheckpoint serializes position and
progress before removal and copies persistence afterward. Game.I's restart
filter, constructLevelActors' saved-UID omission and Game.al's HP refill/MP
restore order are confirmed in original b/e bytecode. Twenty-three selected
first-level fixtures add 6785 native-comparable semantic/presence rows and 82
original/reference comparison expectations, with two complete Java restarts.
No Java names changed. The native session snapshot is not an RMS file/backend;
see NATIVE_CHECKPOINTS.md for supported fields and runtime boundaries.


Native beam pass: existing Actor.aL/updateBeamEmitter and type-10 action entry
were checked against original b bytecode, including the ray helper
Actor.a(int,int,int,int,int[]) and kind-4 player damage. No names or generated
Java patches changed. NATIVE_BEAMS.md records the bounded native implementation.


Placed-prompt pass: existing Actor.bN/updatePrompt, its constructor/visibility
and Game.removeActor paths were checked against original b bytecode. Placed
UIDs, custom rectangles, magic/switch acknowledgement and registry-slot removal
supply NATIVE_PROMPTS.md's evidence. No names or generated Java patches changed.


Opening cutscene pass: existing Actor.updateTrigger/Actor.o and Game.startCutscene,
updateBackgroundScripts/updateScript, executeScriptOpcode 116/113 and
finishCutscene were checked against original b/e bytecode. IntroCheck compares
the shipped trigger 82/script 76, including high-word flags and skip cleanup.
No semantic names or generated Java patches changed; see NATIVE_INTRO.md.


Type-80 correction: Actor.bd is now updateEnergyBarrier (previously labelled
updateCollapsingPlatform). Actor.initializeFromRecord extends its sprite bounds
vertically by record[6], marks it hidden and maps it to effect sprite 1. Actor.bd
enables its priority solid body and waits for animation endings; linked-trigger
operation 7 requests animation 31 and removal. Actor.paint calls Game.b(III),
which draws three random red/white lightning strands, and draws the sprite at
both vertical endpoints. Level-1 enemy 352 and triggers 354/124/356 enable, clear
the respawn flag and release barrier UID 32. Original b/e bytecode confirms these
call sites; this is an energy barrier rather than a collapsing rider platform.


Type-16 foundation: existing Actor.bH/updateThrownObject, bJ bounce physics, bK
reflected/exploding target contacts, action entries 101-105, m(II)/n(II) held
birth/release and B(I)/Game.d(Actor) holder notification were inspected against
original b/e bytecode. Flag 256 is removed by release/replacement, while B(I)
clears the holder directly and preserves the removed object's flag until it is
discarded. ThrownObjectCheck verifies these paths without executing type-3 AI.
No names or generated-Java patches changed; see NATIVE_THROWN_OBJECTS.md.

Type-3 actor integration: Actor.initializeFromRecord (type 3: cE[slot 10], stat table 3,
link slot 12, snapDownToGround before the custom bounds, respawn-fade re-entry when the
record has flag 128), updateEnemyType3's link/tail, m/n/bI held-shot operations, positionChild,
Game.W's parent-attached dispatch, updateContainer's carried branch and Game.removeActor's
setAttachedActor(null) were inspected against original b/e bytecode. Actor.refreshBounds
reads the flags field (not facingLeft), which explains the one-update chest flip lag.
No names or generated-Java patches changed; see NATIVE_TYPE3_ACTORS.md.

Level-1 encounter evidence: Actor.updateLinkedTrigger's field roles (aw condition, ax
target/script flag, au action, y/z/A sources, B/C/D targets or script), Actor.a(int,int,boolean)'s
unused-slot value (the third argument), the condition-4 first-source clearing, H()'s poll on every
tick, enableLinkedActor's flag/effect rules and Game.updateSelectedScripts' ordering were inspected
against the regenerated Java. Shipped level-1 records show 104 targets in slots B/C, 103 starting
script 95 and 62 enabling chest 59. No names or generated-Java patches changed; see
NATIVE_LEVEL1_ENCOUNTER.md.

Level-1 ambush evidence: Game.executeScriptOpcode case 106 calls changeActionState for basic enemies
and assigns actionState otherwise; cases 23/24 extend the operand with |=65536 when bit 0 is set and use
flags |= / &= ~(v>>16), with command 24 recomputing facingLeft outside its tick condition. Actor.a(byte) appends to
Game.ck[cj] and Actor.c does cj-- without compacting, so an earlier script finishing first leaves a later one
outside the selected slots; Game.updateBackgroundScripts/hasActiveBackgroundScript then run it and skip
updateWorldActors. Actor.V with zero excursion and speed flips direction each tick without moving the angle.
Shipped scripts 23/24/114 and records 89-94/115/118/123 were decoded from pack 4. No names or generated-Java
patches changed; see NATIVE_LEVEL1_AMBUSH.md.

Level-1 envelope evidence: Actor.cE[49]==12 gives type-2 behavior 6; Actor.aE (updateEnemyType2) state 0
enters 138 and sets ac/ad/aF/flags|=16, states 138/139 drive the sine path, and Game.au (set by Game.a(Actor) from
the type-22 handler) is the active camera region, not the player. Actor.aS (clampActorToActiveRegion) resets types
2 with behavior 3/5/6 outside it (3 removes its spawner). Actor.initializeFromRecord type 10 assigns aF only for
animations 0/8/4, so animation 6 leaves it 0, and Actor.a(int,int,int,Actor) tests `aF<8`/`aF==1`. All were read in
the regenerated Java and b.bytecode.txt (lookupswitch at b.aE offsets 63/435/1783). No names or generated-Java
patches changed; see NATIVE_LEVEL1_ENVELOPE.md.
