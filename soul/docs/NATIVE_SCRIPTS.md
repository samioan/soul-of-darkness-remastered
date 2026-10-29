# Native script data and the gate timeline

The native preview now loads first-level script 165 and can execute its existing
gate-154 track before ordinary world updates. A controlled host fixture runs the
actual track, lifts the gate to Y=1415 and clears the doorway on script tick 25.
Normal gameplay still waits for the unported chest/trigger sequence; the fixture
starts the script explicitly and does not establish first-level completion.

## Data and interpreter

`decodeScripts` owns parsed script metadata, tracks, keyframes and command
operands. It preserves each complete track and the loader's two-byte initial
offset trailer. All 17 scripts / 47 tracks match the existing Python parser in
64 rows, including every raw track byte. The parser checks signed counts,
track kinds, known opcode operand lengths, resource bounds and exact consumption.
Decoding an opcode does not imply its side effects are ported.

`GateScript` accepts existing-actor tracks (kind 2) containing position opcode
21, immediate animation 22 and action 106. It rejects other track kinds/commands
before running. This covers the complete shipped script-165 track, not scripts
76/160 or general cutscene/dialogue/camera/audio commands. Targets resolve only
the native gate owner; missing live targets reject before the clock or actors
change. Native loading selects UID 165 explicitly from the decoded first-level
resource; other scripts remain decoded data.

Original `Game.b(int,boolean,int)` / `updateScript` uses the old signed-short
counter and then increments it. Each track examines one pending keyframe per
call. Its position interpolates toward a future key using truncating integer
division by `keyTick-currentTick+1`; animation runs at/past its key, whereas
extended action 106 requires exact tick equality. Byte offsets advance at/past
the key. Completion sets the native timer to -2; loop mode rewinds to zero.
`start` retains current offsets, `restart` rewinds and starts, and `stop` rewinds
and sets -2. Signed-short clock wrap is retained. These branches were inspected
in original `e` bytecode; no generated Java changes or new renames were needed.

Script interpolation changes integer coordinates. The following gate handler
performs the original fixed-point resynchronization and collision writes.
Script-side visibility refresh uses the reference 240-pixel camera and preserves
the gate's distance-bucket/bounds behavior. Immediate animation changes reset
clocks/pending animation and clear flag 64 only when the animation changes.
Gate drawing remains separate from simulation and restores caller clipping.

`CombatWorld` owns the loaded runner and advances selected gate scripts before
player/body preparation and actor dispatch. Its `startGateScript(165)` boundary
is ready for trigger integration. World pause skips script execution; the native
host's existing modal paths also freeze simulation. Full `startCutscene` /
`finishCutscene` UI, camera/input/effect cleanup and selected-script list ownership
are not implemented by this controller.

## Activation dependency and fixture

Shipped trigger UID 166 uses condition 3 with source UID 57 and selected-script
mode to start script 165. Source 57 is a script-locked type-52 container with
reward -2. That container's activation/reward and the preceding linked-object
sequence remain unported. The native preview does not substitute an automatic
start or mark that source removed.

`--gate-script` is a headless movement-preview fixture. It places and individually
freezes the player near the real gate, supplies invulnerability, and explicitly
starts script 165 before the first tick. Other world actors continue running.
This is controlled setup, not a recording of the missing chest interaction.
It cannot be combined with another validation script or interactive `--game`.

## Verification

`GateScriptCheck.java`, included in default regeneration, runs original and
generated `updateScript` and gate handlers on the actual first-level collision
grid. Eight 48-tick timelines cover shipped one-shot execution, pause, restart,
stop, start without rewind, looping, omitted gate handlers and a controlled
animation change. Explicit bounds/animation paint steps use the analysis ImageIO
adapter, not a MIDP runtime. The script is selected and its finish-time interaction
mode is seeded to avoid unrelated camera/interaction cleanup.

All 1540 original/reference expectations pass. The 1536 emitted rows record
script counters/byte offsets and full gate state/grid CRC before/after paint.
Independent assertions require Y=1497 at tick 1, completion and Y=1415 on tick
25, and all four doorway cells cleared by action 103 on that same tick.
`check_native_gate_scripts.py` matches these rows and all script envelopes.
Twelve actual host runs finish script 165 at widths 240/569, 30/60/144 FPS and
both interpolation settings, with matching simulation and per-width pixels.

Sixteen new data-free assertions cover decoding, truncated operands, unsupported
execution, missing-target atomicity, timing/interpolation, pause, completion,
start/restart/stop/loop and the world integration boundary. Player checks total
269. Three new argument checks bring CTest to 48 data-free tests.

Completed validation: default Java regeneration compiles and passes all
registered original/reference checks. All 48 CTests pass in Debug and Release.
Both configurations pass script data/timeline/host checks plus linked-trigger,
ordered-world, region, live combat, transient, bridge, platform and gate
regressions. Live fixtures cover widths 240/569, 30/60/144 FPS and interpolation
on/off. The local distribution is rebuilt and passes its package checks without
bundling game data.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_gate_scripts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --movement-preview --gate-script --data soul/extracted --headless --width 569 --fps 60 --frames 240
```

Next: the script-locked container/reward and linked trigger owners needed to
activate the gate through gameplay, then broader scripts and checkpoint/save
progression. Full gameplay remains unfinished.

The subsequent [container-removal trigger pass](NATIVE_REMOVED_TRIGGERS.md) now
connects source 57's removal state to script 165 and its completion cleanup.
Normal opening still needs chest 57's unlock and separate type-48 ability reward.
