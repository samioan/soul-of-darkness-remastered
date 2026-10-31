# Native container-removal gate trigger

The first-level preview now loads type-40 trigger UID 166 and connects its real
container source UID 57 to selected gate script 165. Once a source resolved by
the trigger is removed, the trigger starts the script exactly once. The first
script key runs on the following tick; completion rewinds the script and removes
the trigger. The controlled host fixture exercises this complete connection.

The later ability and chest-unlock passes now connect this controller to normal
arena progression: see NATIVE_ABILITIES.md and NATIVE_CHEST_UNLOCK.md. The
controlled removal fixture below retains its original narrow setup and does
not claim a playable level ending.

## Original evidence

`Actor.bh` resolves its source actor lazily and caches the actor's persistence
index. Condition 3 consults `Game.bD[index] == -99`, with the original extra
`index == 0` shortcut. Thus index zero can trigger even while its source is
still live. Missing UID links resolve to -1 and retry; a source removed before
the first successful lookup cannot start this trigger from its UID alone.
Source hidden/pause flags do not prevent the UID lookup or condition test.

Selected-script mode calls `Actor.a(byte)` once while scratch field `as == -1`:
it adds the script index/mode to `Game.ck/cl`, increments `cj`, starts its counter
at zero and sets `as = 1`. `Actor.H` waits for timer -2, calls stop/rewind, releases
the selected slot, resets the latch and removes the trigger. This completion
poll also runs when the condition is false. Handler pause/flags 1024/16384 skip
these operations.

`Game.constructLevelActors` assigns persistence indices in nonplayer record
order, including currently unported records. `Game.removeActor` marks the placed
slot -99. `Game.updateSelectedScripts` precedes ordinary world updates, so a
trigger started during the world tick leaves key zero for the next tick.
Original `b`/`e` bytecode and call sites confirm these branches. No generated Java
edits, renames or decompiler patches were needed.

## Native ownership and ordering

`RemovedScriptTrigger` supports a single placed source, condition 3, selected
one-shot mode and script UID 165 in this pass. Later work adds condition 4, up to three
enemy sources and script 95; see NATIVE_LEVEL1_ENCOUNTER.md.
`CombatWorld` records fresh nonplayer placement identities, preserving indices
even for unported records, and receives actual alive/removed state from the
separate container owner. Cached indices survive source removal. This is an
in-memory source-state bridge, not a full actor registry, save table or UID pool.

Eligible source triggers join the typed frozen queue with type-40 radius seven
and normal flag precedence. Their handler finishes through
`finishContainerSources` after the separately owned container handlers. The
supported source must precede its trigger in record order; native construction
rejects the opposite arrangement. The shipped source, gate and trigger follow
that order. Later actor handlers do not observe a new script command until the
next tick, preserving this selected-script controller boundary. A fresh world
snapshot authorizes only one source-finalization call, preventing stale queue
dispatch or duplicate handler calls. Death/hazard guards cannot reuse it.

When the selected gate script completes, the trigger stops/rewinds it, clears
its cached source/bounds and becomes inactive. Its placed slot is marked removed
in the local source-state table. Full selected-script list reuse, registry null
slots, persistent removed UID buffers and cutscene UI/camera cleanup remain
unimplemented, alongside the previously documented script runner boundaries.

## Checks

`RemovedGateTriggerCheck.java`, run during default regeneration, invokes actual
original/reference selected-script, trigger and gate methods on the real first-
level collision grid. Eight 48-tick cases cover normal removal, missing source,
removal before lookup, persistence index zero, pause, hidden/frozen trigger and
a hidden source. Source removal is controlled; the ability/chest reward handler
is not executed. Paint uses explicit bounds/animation steps and the analysis
ImageIO adapter. The selected-script completion interaction mode is isolated
from unrelated camera/interaction cleanup.

All 1539 original/reference expectations pass. The 1536 native-matching rows
include source cache/index, latch, trigger liveness/bounds, selected slot count,
script clock/offsets, source removal marker and full gate state/grid CRC.
Independent assertions require counter zero on the source-removal tick, release
of the selected slot on completion and the live index-zero shortcut.

`--gate-trigger-script` is a headless movement fixture. It places/freezes the
player near the real gate, supplies invulnerability and explicitly marks chest
57 removed on tick 4. It does not call `startGateScript`, open the chest, spawn
its ability or bypass its reward handler in normal play. The real trigger starts
script 165 from that controlled removal. Twelve runs verify trigger cleanup and
the opened gate at widths 240/569, 30/60/144 FPS and both interpolation settings,
with matching simulation and per-width pixels. Other validation scripts and
interactive `--game` cannot be combined with this fixture.

Fifteen new data-free assertions cover source binding, missing/removed sources,
one-time activation/completion, index zero, pause/hidden flags, rejected modes,
world identity/queue ownership and the next-tick script boundary. Combined
player checks total 284. Three new argument checks bring CTest to 51 tests.

Completed validation: default Java regeneration compiles and passes all registered
original/reference checks. All 51 CTests pass in Debug and Release. Both native
configurations pass the new trigger oracle/host checks plus script, linked-area,
ordered-world, region, combat, transient, bridge, moving/swinging platform and
ordinary chest regressions. Host fixtures cover widths 240/569, 30/60/144 FPS and
interpolation on/off. The local Windows package is rebuilt without game assets.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_removed_gate_triggers.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --movement-preview --gate-trigger-script --data soul/extracted --headless --width 569 --fps 60 --frames 240
```

Next: type-48 ability-2 ownership/reward and chest 57's unlock/opening sequence,
then remaining linked actors, broader scripts and checkpoint/save progression.
Full gameplay remains unfinished.
