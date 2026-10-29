# Native first-level finishLevel and progression saves

Acknowledging first-level completion now captures the current player stats,
marks level 0 completed, preserves/increases the maximum unlocked level, writes
the desktop slot, destroys the old world and selects chapter 2. Continue from
a completed save selects the next chapter and never reconstructs the obsolete
checkpoint world. Chapter 2 remains unavailable until its construction and
required handlers work; Left selects chapter 1 for replay.

The native game still constructs only level 0. This is a completion/progression
transition, not a claim that the second level or full game is playable. The
selection is a desktop card with bounded keyboard navigation, not the original
animated map. No publishing/updating or original RMS import was introduced.

## Original evidence

Original e.U/finishLevel clears paired mode and byte 644, raises maxUnlockedLevel
to at least levelIndex+1, sets levelCompleted, invokes Actor.p/serializeProgress,
writes RMS, unloads the old level, selects the following map node and enters
state 19. Serialization retains the completed level at byte 14; advancement is
a separate selection/Continue decision. Maximum unlock is never reduced by replay.
Selection wraps from completed index 8 to 0, while completed Continue at index 8
retains level 8. Native pure decision checks include these terminal branches;
native gameplay/codec support for those levels remains disabled.

Actual unload clears actor/player/platform ownership, scripts, collision/maps,
world sprites and level strings. The native owner detaches player support and
input before destroying MovementPreview; its scripts, camera caches, platforms,
projectiles, contacts, containers, actors and immutable scene assets then leave
scope. Frontend artwork has separate ownership and remains available. In-session
checkpoint ownership is cleared. A copied completion record supplies diagnostics,
not borrowed actor pointers.

Menu action 2 reads saved level/unlocks/completion. Completed saves below index 8
advance the target and disable checkpoint-position restoration. A positive saved
checkpoint marker restores serialized player stats; without one, the original
resets meters instead. The native version preserves this cold-start distinction.
A warm no-checkpoint Continue retains runtime weapon/ability ownership while
resetting meters, matching resetPlayerMeters' limited responsibility. Fresh
replay fills HP/MP to their maxima; ordinary checkpoint Continue retains saved
HP/MP, and Restart retains its existing health-refill/MP policy.

The native availability guard stops completed Continue at the next selection
card before its unsupported world could load. It retains the original target
and avoids restoring old coordinates. Starting chapter 1 reconstructs its world
at spawn, keeps supported progress and flag-512 removals, and clears completion
before the next checkpoint write. Higher unlocks survive that write.

## Desktop format version 2

GameSave owns progression metadata alongside checkpoint coordinates and the
latest serialized player stats. A completed record keeps the old checkpoint
coordinates, when present, but replaces its stats with current end-of-level
values. UID -1 represents completion without a previous checkpoint; it is invalid
for an incomplete snapshot. Completion without a checkpoint still produces a
valid Continue record and New Game replacement confirmation.

The existing SODPC header/checksum and atomic replacement protocol now write
version 2. The payload starts with level, maximum unlock and completion flag,
then the previous checkpoint/stat/removal fields. Level 0 remains the only
accepted stored gameplay level; maximum unlock can retain the original 0..9
range. Derived next selection does not authorize loading an unported level.
Flags, counters, equipment, UID uniqueness and field bounds remain validated.

Version-1 files load as incomplete level-0 progress with maximum unlock 0. Reading
does not rewrite them. Their next checkpoint/completion write uses version 2,
retaining meters, equipment, fragments and supported removals. Original 1313-byte
RMS is not this desktop format. Upgrade arrays, other-level snapshots and the
remaining original progress flags remain future work.

A completion write failure leaves the old slot intact, reports the existing
save error and still finishes/unloads in memory. Continue in a later process
uses the previous successful checkpoint. New Game clears both completion and
unlocks only after its existing replacement confirmation succeeds. Automated
runs remain isolated from the user slot unless --save-file is supplied.

## Verification

FinishCheck executes actual original/reference finishLevel, serialization,
in-memory RMS access, world unload and completed menu Continue. Its test ImageIO/
RMS adapters and opaque tile-buffer Graphics sentinel are not a MIDP runtime.
Unlike CompletionCheck's counted UI boundary, no finish/unload/serialization
method is replaced. The full 1313-byte original/reference records are compared,
alongside explicit completion, unlock, paired reset, old-world release and
Continue assertions.

Twelve scenarios cover indices 0/7/8, ordinary/already-higher maximum unlocks and
presence/absence of a checkpoint. They pass 444 original/reference expectations
and provide 24 native logical progress/stat rows. The native rows cover supported
stats and decisions, not every original upgrade/flag or later-level world.

Data-free saves tests cover version-1 migration, version-2 checkpoint/completion
round trips, no-checkpoint defaults, warm runtime flags, higher-unlock retention,
ending decisions, unsupported levels, invalid progress metadata, old-slot
preservation and the existing truncation/single-byte-corruption corpus. Frontend
tests verify that an unlocked unported chapter cannot start and that chapter 1
remains replayable. Three new CLI guards bring CTest to 101; the separate player
suite remains 508 assertions.

The host checker runs 36 new-process finish/Continue/replay cases at 240/569,
30/60/144 FPS and interpolation on/off, alternating legacy and current checkpoint
files. The controlled finish fixture resumes checkpoint 11, changes current
meters/counters, relocates near exit 173 and uses actual movement/contact/ack.
It verifies current end stats with retained checkpoint coordinates, one save/
unload, no obsolete-world restoration, blocked chapter-2 entry, fresh chapter-1
replay and preservation of unlocks on its next checkpoint. Extra cases cover
read-only legacy menu boot, no-checkpoint completion/default Continue, failed-write
recovery and New Game reset. These fixtures are not a first-level route test.

Validation on 2026-10-29: default regeneration exited 0, compiled with zero
errors and passed ReferenceCheck's 4218 comparisons and all registered checks,
including the 444 real finish/Continue expectations. Debug and Release passed
all 101 CTests, the new 24-row oracle and 36 cross-process finish/Continue/replay
cases. Existing save migration/recovery, checkpoint/Restart, completion UI,
startup/frontend and intro playback/skip regressions passed. Chapter-2 selection
captures were inspected at widths 240/569. The rebuilt data-free package passed
validation and its asset tool matched the new oracle.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_finish.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_saves.py --exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

The next task is selected-level construction; entry to level 1 stays disabled
until its required handlers are implemented. Logs, captures, oracles and binaries
remain ignored and no game data is committed or packaged.
