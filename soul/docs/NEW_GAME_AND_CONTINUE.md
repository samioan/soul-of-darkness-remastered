# New Game, Continue and selected loading stages

`MenuCheck.java` executes original and regenerated menu-action dispatch, confirmed
New Game, level-selection stat preparation and loading stages 1/79/80. It uses
the isolated lifecycle harness and its in-memory RMS adapter. It does not paint
menus, run the complete staged loader or implement a native game runtime.

## New Game decision and save reset

Menu action 1 checks the checkpoint short at save offset 16. When positive, it
opens confirmation. Right softkey cancels and preserves progress; left softkey
confirms and calls `startNewGame` (original e.P(), e.java:1794). With no checkpoint,
action 1 directly enters prologue state 20 without calling the reset handler.
That branch relies on the surrounding initialization state; it is not another
full-buffer reset. Both branches clear the input edge.

Confirmed reset selects level/unlocks 0, enters state 20, clears all 1313 save
bytes, initializes health/magic and both maxima to 60, writes the initial upgrade
short 1 at offset 32, writes the four meter values, and requests an RMS write.
The remaining meter-adjacent counters are zero. It then restores progression
from this buffer and clears three Actor tutorial/progress flags. The fixture
checks the complete expected record, reset stats/unlocks, confirmation/cancel,
and the separate no-checkpoint branch. The original write does not set
`saveLoaded` (as established separately in CHECKPOINT_LIFECYCLE.md).

## Continue is distinct from Restart

Menu action 2 reads saved level/unlocks and the `levelCompleted` short at 24.
It initially enables `continueFromCheckpoint`. If completion is true and the
saved level is below 8, it advances one level and disables that flag. Level 8
does not advance through this branch. A positive checkpoint UID+1 restores
saved progression/meters before loading; otherwise `resetPlayerMeters` assigns
the default meters. Both paths enter state 9 and clear input.

Loading stage 1 clears `levelCompleted` and loads the level string table.
Stage 79 restores removed-UID tables and persistence bytes. With the Continue
flag, it calls `reconstructLevelActors(true)`, restores the checkpoint, refreshes
camera-region selection, then consumes the flag. Without it, it constructs
actors in fresh mode. Stage 79 returns false; stage 80 reports completion.
The staged loader catches exceptions, so the checker asserts the resulting
world/state rather than accepting the completion boolean as evidence of success.

The reconstruction wrapper sees `continueFromCheckpoint` still set and forces
**fresh actor construction**, clearing the per-record persistence bytes. This
recreates the tested checkpoint even when its saved byte is -99. Restart instead
calls the wrapper with the Continue flag clear and preserves type 23's removed
marker. Removed-UID tables remain a separate filter in either case. No Java
behavior fix is applied to this original difference.

On the tested checkpoint Continue path, saved health/magic are restored without
Restart's health refill. On next-level advancement, the fresh player constructor
fills the meters to their current maxima. At level 8 with saved completion set,
stage 1 clears completion but checkpoint restoration reads it back from the save.
These are tested branch outcomes, not a claim of a full ending play-through.

The fixture tests saved levels 0/5/7/8 with both completion values. It compares
original/reference worlds and independently checks target level, checkpoint vs
spawn coordinates, level 5's Y correction, meters, recreated checkpoint, cleared
persistence bytes, flag consumption and unchanged serialized save bytes. It
also tests Continue without a checkpoint at menu dispatch only.

## Level selection and semantic names

The selected-level start helper `prepareLevelSelectionStats` restores progress
when a checkpoint exists, otherwise resets default meters, then always enables
`skipCheckpointPosition` and consumes selection flag `dw`. This lets a selected
level start at its spawn instead of borrowing another level's saved coordinates.
The checker invokes this helper directly; selection-map painting/input is outside
the fixture.

| Original member | Name | Evidence |
|---|---|---|
| e.aI | levelCompleted | Set on level/final completion, serialized at 24, read by Continue, cleared at load stage 1 |
| e.dK | continueFromCheckpoint | Continue assigns it; stage 79 and reconstruction choose fresh actors plus checkpoint restoration |
| e.bP | skipCheckpointPosition | Selection preparation sets it; checkpoint activation clears it; restoration gates coordinates on it |
| e.aj() | resetPlayerMeters | e.java:3734; default health/magic/maxima and associated counters |
| e.aa() | prepareLevelSelectionStats | e.java:2469; called when entering a selected level |
| e.ae() | ensureTileBuffer | e.java:2889; create/reuse 260x340 image/Graphics, mark dirty |
| e.k() | refreshCameraRegionSelection | e.java:2096; update type 22 actors until a region is selected |

Call sites, save writers/readers and original bytecode establish these names.
The pass adds no decompiler patches.

## Test setup and remaining work

The checks pass 560 expectations plus selected-world comparisons. String data
uses the same seeded pack-stream approach as ScriptCheck. Real gameplay maps,
scripts, sprites and stats are supplied before stage 79; intervening stages are
not executed.

Entering state 20 calls `ensureTileBuffer`. The fixture pre-seeds a 260x340
ImageIO-backed image and an opaque API-stub Graphics instance allocated through
Unsafe without calling its throwing constructor. The unchanged original helper
only checks these references and sets its dirty flag. No Graphics method is
called and no mutable MIDP image/Graphics implementation is supplied. This tests
buffer reuse only, not allocation or rendering. The Image/Graphics runtime
boundaries of the other checkers remain unchanged.

Pending: complete boot/loading, prologue/selection/menu drawing, real keypad
callbacks, allocation of mutable rendering buffers, malformed records/storage failures, and
native rendering/gameplay. Keep the Windows executable described as a scaffold.
LEVEL5_CONTROLLER.md now covers controller/hazard restoration with three live
linked snapshots through Restart and Continue.
