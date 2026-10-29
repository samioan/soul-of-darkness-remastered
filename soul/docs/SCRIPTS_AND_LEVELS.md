# Scripts and level maps

This pass maps Game's timed script interpreter, script storage/lifecycle and
tile-map loader. Python validates every shipped script and map payload. The
Java checker compares loading and controlled timelines with the original
bytecode. Full level construction, linked-actor sequences, dialogue rendering,
save restoration and playable native gameplay remain incomplete.

## Level resources and loading

`levelPackPaths` (`e.dI`) contains `/3` through `/14`. Packs 3..11 have object
and script resources followed by three cell grids; packs 12..14 have two visual
grids used by presentation screens. `loadTileMaps(index, gameplay)` remaps
indices 11 to path index 9, 12 to 11 and 13 to 9; other indices are direct.
Do not infer the number of playable levels from this path table.

| Payload | Gameplay resource | Presentation resource |
|---|---|---|
| Serialized objects | 0 | absent |
| Timed scripts | 1 | absent |
| Collision cells / dimensions / unused packed companion | 2 / 3 / 4 | absent |
| Visual layer 0 cells / dimensions / transforms | 5 / 6 / 7 | 0 / 1 / 2 |
| Visual layer 1 cells / dimensions / transforms | 8 / 9 / 10 | 3 / 4 / 5 |

Dimensions are two unsigned LE shorts. Cell grids contain width*height bytes.
Visual IDs are unsigned; 255 means no tile. Each transform byte holds four
two-bit values, highest pair first. Bit 0 selects drawRegion transform bit 1,
and bit 1 selects transform bit 0. Tile regions are 20x20 pixels in layer-specific
atlas images; horizontal atlas placement is `(id*20) % imageWidth`.
The loader does not use gameplay resource 4; its packed size also validates.

`loadMapCellsAndDimensions` replaces global map dimensions each time it reads
a grid. `loadTileMaps` finishes with layer 1's dimensions, sets pixel dimensions
to tiles*20 and normalizes collision bytes 255 to 0. It resets the tile rollback
index to -1. It does not retain separate collision dimensions.

| Pack | Visual dimensions | Script count |
|---|---|---|
| 3 | 91x84 | 3 |
| 4 | 118x67 | 5 |
| 5 | 121x75 | 0 |
| 6 | 902x16 | 3 |
| 7 | 106x64 | 1 |
| 8 | 14x338 | 1 |
| 9 | 148x72 | 1 |
| 10 | 140x105 | 1 |
| 11 | 18x20 | 2 |
| 12, 13 | 20x17 | absent |
| 14 | 24x17 | absent |

Pack 9 has only 148x71 collision cells despite its visual grids being 148x72.
The original loader leaves this mismatch in place. Consequently a direct
collision lookup on the last visual row can index beyond the collision array.
No padding or behavior fix is applied to the Java reference. A future native
port must decide how to handle this after checking reachable gameplay paths.

`loadLevelStep` stages strings at step 1, sound/tile-buffer setup at 3,
background sprite preparation at 4..13, tile maps at 14, objects/scripts at 15,
required-sprite collection at 17, sprite loading at 18..70 and cache finalization
at 71. Step 79 resets world state and constructs/restores actors; steps above
79 report completion. The routine catches exceptions and prints them, so a
completed progress counter alone does not prove successful loading.
`collectRequiredSprites` scans object types and adds dependency/level-specific
sprites before the staged sprite loads. LEVEL_ACTORS.md now covers construction
across all nine shipped object streams and selected level 5 snapshot relinking.
The full staged loader remains untested.

## Script binary format

All multi-byte script values use little-endian order. Counts read as Java byte
or short are signed; shipped counts are nonnegative.

1. Signed byte script count.
2. Per script: signed short ID, signed byte track count, two skipped metadata bytes.
3. Per track: signed byte kind, one skipped byte, optional signed short target
   for kinds 2/3, then signed short keyframe count.
4. Per keyframe: signed short tick, signed byte command count; each command is
   a signed byte opcode followed by its fixed-size operands.

Kinds 0/1 have no target operand; kind 1 controls the camera. Kind 2 resolves
an existing actor UID. Kind 3 creates/finds a cinematic actor, with synthetic
UID `(target << 16) + (scriptIndex << 8) + trackIndex`. Metadata bytes are
preserved but their meaning is not inferred.

`decodeScripts` copies each complete track, appends its initial keyframe offset
as two LE bytes, and stores separate current offsets. Initial offsets are 4
for kinds 0/1 and 6 for kinds 2/3. Reset/restart restores them from the trailer.
The original reset expression masks the shifted high byte with 255, discarding
it; bytecode confirms this quirk. Shipped initial offsets do not exercise a
nonzero high byte. All 17 scripts / 47 tracks / 241 keyframes consume exactly.

## Commands and execution

`scriptTicks` is a signed short array. -1 is reset/disabled, -2 is stopped;
`startScript` assigns 0 without rewinding, while `restartScript` assigns 0 and
rewinds track offsets. `stopAndRewindScript` assigns -2 and rewinds.
`startCutscene` also changes input/combat/UI flags; `finishCutscene` performs
additional cleanup. Background and selected-script update loops are separate.

Each `updateScript` reads the **old** counter value and increments its stored
short. Each pending track examines one keyframe per call. Position commands
interpolate toward a pending target using integer division by
`targetTick-currentTick+1`. Camera command 12 subtracts 120/160 to center a
world point and clamps to the level bounds minus 240/320. These constants are
future widescreen integration points, not safe global replacements.
At/past the keyframe tick, its byte offset advances; completed mode 0 restarts
the script, other modes finish it. Extended command side effects require exact
tick equality. Some basic commands instead use tick<=currentTick.

| Opcode | Operand bytes | Interpreter behavior |
|---|---|---|
| 11 / 12 | 4 | Camera target X/Y; 12 centers a world point |
| 21 / 31 / 41 | 4 | Target position X/Y on non-camera tracks |
| 22 / 32 / 42 | 1 | Set animation immediately |
| 23 / 43 | 4 | Set encoded flags/facing; unusual upper-word extraction is preserved |
| 24 / 44 | 4 | Clear encoded flags and update facing |
| 34..36 | 4 / 6 / 8 | UID-conditional global command with 1 / 2 / 3 short operands |
| 37..39 | 2 / 4 / 6 | Global command with 1 / 2 / 3 short operands |
| 100 | 6 | Apply short property/value to an actor UID |
| 101 | 2 | Start cutscene by script ID and request completion of current script |
| 102 / 103 | 3 / 0 | Play sound (ID, loops) / stop sound |
| 104 | 2 | Vertical shake |
| 105 / 106 | 1 / 1 | Reserved no-op / set action state |
| 107 / 108 | 4 / 4 | Set / clear raw actor flags |
| 109 / 110 | 0 / 0 | Reserved no-op |
| 111 / 116 | 8 / 4 | Dialogue with explicit anchor / actor anchor |
| 112 / 113 | 1 / 0 | Reserved no-op / remove track actor |
| 114 | 4 | Random prompt/QTE setup with two script IDs |
| 115 | 0 | Disable cutscene skip flag |
| 117 | 2 | Enable scripted focus, snapshot actor position; operand is not read |
| 118 / 119 | 0 / 0 | Reserved no-op / start interaction 17 |
| 120 / 121 | 1 / 0 | Set actor scratch field aG / start interaction 18 |

`executeScriptGlobalCommand` currently acts only with two short operands:
command 0 starts the black fade overlay and clears two UI flags, 1 enables a UI flag and timer,
2 clears that timer and 3 changes Game state to 15. Other operand counts
do not trigger a branch. Actor property 0 adjusts paint priority, 1 controls
flags 32/16, and 2 removes the actor. These meanings follow the dispatchers;
not all are present in the shipped scripts or exercised at runtime here.

## Decompiler correction and validation

Vineflower recovered the interpreter's counter as increment-then-read.
Original `e.b(int,boolean,int)` instructions 21..32 instead use `saload`,
`dup_x2`, increment/narrow/store and then `istore 9`, preserving the old value.
The asserted patch in rename_sod.py restores
`short var9 = scriptTicks[var0]++;`. This is a reference-fidelity fix; the
unpatched source executes commands one tick early. The regression timeline
failed against original bytecode before the correction.

`ScriptCheck.java` compares original/recompiled map arrays/dimensions, collision
normalization, script IDs/track bytes/offsets/timers, ID lookup and lifecycle.
Its 852 comparisons check an actor timeline (interpolation, one-time property change, animation,
state and flags), a centered/clamped camera timeline with view bounds, pause,
looping/one-shot completion and off-tick operand skipping for all 22 extended
opcodes. Independent expected values supplement equality checks.
Actual dialogue, sound, QTE and all actor-spawning opcode effects are not run.

The native tile pass adds two original/reference atlas-table comparisons (854
total) and a separate 45-row `map-oracle.tsv` for normalized map grids,
transforms, dimensions and atlas bindings. NATIVE_TILES.md documents the native
loader, tile renderer and nine-level asset preview; this still does not run
native actors or gameplay.

Desktop Java cannot resolve the original `String.class.getResourceAsStream`
lookup through an application asset loader. The checker seeds each validated
single-part pack's input stream/path/table and invokes the unchanged original
map/resource readers. It does not patch Game or emulate MIDP resource lookup.
Script decoding receives extracted bytes directly. No Canvas, drawing, sound
or RMS service is invoked; full staged loading is outside this harness.

The ignored `soul/analysis/script-oracle.tsv` records original script IDs,
track lengths/initial offsets and SHA256 hashes. The independent Python decoder
matches all 64 rows. It rejects unrecognized opcodes rather than guessing their
operand sizes; the Java switch treats some unknown low commands as no-ops.

```powershell
python tools/parse_levels.py
python tools/rename_sod.py
python tools/parse_levels.py --oracle soul/analysis/script-oracle.tsv
python tools/bytecode.py e
```

LEVEL_ACTORS.md and SAVES.md now cover shipped stream construction and selected
restoration paths. CHECKPOINT_LIFECYCLE.md adds controlled checkpoint/restart
and repeated loads. Next: staged loading, new-game/continue integration,
linked scripts/dialogue and remaining deep gameplay branches before
translating the verified resource models and renderer into the native port.
