# Save buffer and checkpoint restoration

The record is 1313 bytes (`999 + 250 + 64`), stored as record 1 in RMS
`/dvseed.sav`. Multi-byte values below are little endian. Short reads sign
extend; byte reads are signed. No checksum or version validation is performed
by `accessSaveRecord`. Reading an existing record sets `saveLoaded`; writing
does not. An absent record on a read returns without creating record 1.
This describes the original game; no desktop disk save service is implemented yet.
NATIVE_CHECKPOINTS.md now covers a separate first-level in-session semantic
snapshot and restart path, without claiming original save-file compatibility.

## Observed layout

| Byte offset | Stored value / consumer |
|---|---|
| 10 | Menu sound setting (`cy` bit flags); menu toggle writes this byte |
| 14 / 15 | Level index / highest unlocked level (bytes) |
| 16 | Checkpoint UID + 1 (short); zero means no saved checkpoint |
| 18 / 20 / 22 | Player X / Y / facing-left (shorts) |
| 24 | `levelCompleted` (original Game `aI`, short), used by Continue to advance levels |
| 26 | Active weapon (short) |
| 28..43 | Four two-element short arrays from Actor `bM`, `bN`, `bO`, `bP` |
| 60 / 62 / 64 | Maximum health / health / Game `r` (shorts) |
| 66 / 68 / 70 | Maximum magic / magic / Game `u` (shorts) |
| 72..75 | Game `v` (int) |
| 86..103 | Nine per-level removed-UID counts (shorts) |
| 104..643 | Nine rows of 30 removed actor UIDs (shorts) |
| 644 | Level 3 paired-actor state enabled (`Actor.bC`, byte) |
| 645 / 647 / 649 | Level 3 paired actors X / Y / action state (short, short, int) |
| 653..663 | Eleven Actor progress flags (bytes, true only when read as exactly 1) |
| 664..669 | Level 5 linked-actor UIDs, three shorts, -1 for absent |
| 677..730 | Level 5 linked-actor snapshots, three 18-byte slots |
| 731 / 733 / 735 | Level 5 active controller UID / camera Y / hazard camera-relative Y offset (shorts) |
| 999..1248 | 250 per-record actor persistence bytes; -99 marks removed |

Ranges omitted from this table have no assigned meaning from this pass. Do not
repack the record or assume these bytes are available for new desktop settings.

## Writers, readers and original quirks

Original `b.p()` (b.java:2076) becomes `serializeProgress`. Its callers include
checkpoint activation, level completion and gameplay progression. It writes
level, unlocks, stats, upgrade arrays and flags, then calls `b.q()` /
`serializeRemovedActorUids`. The latter updates the current level's count before
writing all nine counts and UID rows. These functions only mutate the buffer.
Serializing progress preserves the higher *signed byte* unlock value already
in the buffer. It does not snapshot player coordinates or persistence bytes.

The checkpoint handler writes UID+1, player position/facing, serializes progress,
removes itself, then copies all 250 persistence bytes. Thus the checkpoint's own
removal is included in that copy. Level 3 adds paired-actor state; level 5 adds
three linked snapshots and camera/controller fields. It then requests an RMS write.

Original `e.p()` (e.java:3714), now `restoreActorPersistence`, reads all UID
rows/counts and persistence bytes. `bH` aliases the selected row of `bG`;
`bI` copies its count. Construction and removal use these tables; the complete
construction/restart lifecycle has not been exercised by this pass.

Original `e.q()` (3767), now `restoreProgress`, reads stats and flags.
**The original swaps two flags on restore:** serialization writes Actor `bp`
at 657 and `bq` at 658; restoration assigns 657 to `bq` and 658 to `bp`.
Bytecode confirms `b.p()` offsets 342..379 and `e.q()` offsets 256..292.
This is preserved behavior, not a decompiler patch or a proposed gameplay fix.

Original `e.ak()` (3744), now `restoreCheckpoint`, restores paired actors on
level 3 when enabled, restores player coordinates/facing unless `skipCheckpointPosition` (original Game `bP`) skips
them, and always restores progress. On level 5 it subtracts 20 from restored
player Y. It changes pixel coordinates directly; fixed-point state is left for
the normal actor synchronization path. It does not restore the level 5 linked
actors itself; that happens around construction in `e.d(boolean)`.

Original `e.a(b,int)` / `e.b(b,int)` (3209 / 3219), now
`storeLinkedActorSnapshot` / `restoreLinkedActorSnapshot`, use this slot layout:
animation byte, frame byte, X/Y shorts, action int, flags int, Actor `aw` int.
Restoring bytes and shorts sign extends them, including animation/frame values.
Other actor fields and animation counters are untouched.

## Validation and boundaries

`SaveCheck.java` runs with default `python tools/rename_sod.py`. Its 2398 checks
execute original and recompiled methods in isolated loaders, with independent
expected buffer bytes and restored values. Thirty-two seeded cases check full
buffers including untouched ranges, signed narrowing, every progress flag,
unlock preservation, upgrade arrays, removed UID tables and row aliasing.
Controlled checkpoint cases cover levels 0/3/5 and position-skip behavior.
All three linked snapshot slots test signed fields and neighboring-slot integrity.

This is in-memory buffer coverage. No RMS adapter, checkpoint collision handler,
full level construction, real saved-game sample or restart play-through is
executed by SaveCheck. New-game initialization and malformed/truncated record
behavior remain integration work. It does not establish playable gameplay
or complete save compatibility for the native calibration scaffold.

LevelCheck now separately covers all shipped actor streams and selected level 5
snapshot relinking during reconstruction; see LEVEL_ACTORS.md.
CHECKPOINT_LIFECYCLE.md adds the original checkpoint handler, in-memory test RMS,
complete restart calls and repeated-load checks. Real save files, storage failures
and the full boot/menu path remain outside these fixtures.
NEW_GAME_AND_CONTINUE.md adds confirmed reset, Continue decisions and selected
loading stages; rendering and complete boot/loading remain pending.
LEVEL5_CONTROLLER.md adds controller/hazard restoration and three live linked
snapshots through checkpoint capture, including a resumed common actor update.


NATIVE_SAVES.md now documents first-level desktop checkpoint autosaves and
Continue, including their original Restart/Continue distinction and remaining
original-format, upgrade-array and level-completion boundaries.


NATIVE_PROMPTS.md adds placed tutorial removal capture. Original record slots
and UIDs are distinct: first-level prompt UIDs reach 271, above the 250 slot
count. The version-1 desktop codec accepts removed UIDs through 32767; the
native snapshot still reconstructs ordinary type-21 prompts unless flag 512 applies.
