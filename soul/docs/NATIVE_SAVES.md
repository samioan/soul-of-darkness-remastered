# Native desktop saves and Continue

The first-level checkpoint snapshot now autosaves to
`%LOCALAPPDATA%/SoulOfDarkness/checkpoint.sav`. Debug, Release and the packaged
executable share this user slot. A valid file adds **Continue** to the title
menu. Incomplete progress resumes the supported first-level checkpoint across
process exits. [Completed progress](NATIVE_FINISH.md) selects the next chapter
without reconstructing the old world. Chapter 2 is selected with entry disabled;
Left selects chapter 1 for replay.
New Game asks Enter to confirm replacing progress; Escape cancels. Confirmation
clears the file before the prologue. Entering the new level may immediately
activate its first checkpoint and create a fresh save.

Continue retains captured HP and MP. Restart Level and R refill HP while
retaining checkpoint MP. Continue follows the original fresh-construction rule:
per-record removals reset, while flag-512 removed UIDs persist. Thus ordinary
pickups and checkpoints can reappear; an overlapping checkpoint may immediately
activate and save again. Restart retains the previous type-based persistence
policy. See NEW_GAME_AND_CONTINUE.md for the original 560-expectation menu/loading
fixtures and NATIVE_CHECKPOINTS.md for checkpoint capture and reconstruction.

## File and failure handling

`save_game.cpp` writes desktop format `SODPC`, version 2, and reads versions 1/2.
This is explicitly separate from the original 1313-byte RMS record. It stores level 0,
completion/maximum unlock, checkpoint UID (or -1 for completion without one),
position/facing, equipped weapon, the ported meters/fragments/counter and
ice/magic ownership, followed by owned UID/runtime-flag removal pairs. Completed
records retain checkpoint coordinates but serialize current end stats.
Version-1 records migrate to incomplete level-0 progress with maximum unlock 0;
reading alone does not rewrite them. No raw pointers, animation objects or
platform links are serialized. The textual
payload has a 32-bit FNV-1a checksum for accidental corruption detection.

Loading caps file size at 16 KiB and removal count at 250, checks header/version,
checksum, field ranges, booleans, duplicate UIDs (0..32767, including placed prompt UID 267), equipment ownership and trailing
data. Unsupported levels fail validation. Corrupt files disable Continue and
remain intact until the player replaces progress. Interactive errors show a
message box; automation writes the error to stderr. Storage failure preserves
the in-session checkpoint and the previous valid file where replacement has
not succeeded. A menu status reports that saving is unavailable.

Writes validate first, create the user directory, flush/close a sibling `.tmp`
file, then replace the slot with Windows `MoveFileExW` using replacement and
write-through flags. This avoids exposing a partially streamed destination;
it is not a guarantee against every hardware/power failure. Saves are ignored
by git and excluded from the package.

`--save-file PATH` selects an isolated slot, including in headless tests.
`--game --continue` invokes the same menu action directly and requires a valid
save. Automated runs without `--save-file` do not access the user's slot. Normal
interactive startup resolves LocalAppData through the Windows known-folder API.

## Validation and boundaries

Data-free CTest covers full field/removal round trips, replacing a previous
save, invalid-write preservation, storage failures, every single-byte mutation
and truncation of a valid file, invalid checksummed payloads, removal on New
Game, Continue dispatch and confirmation/cancel. This initial storage pass had 80 CTests; the current progression pass has
101 CTests (see NATIVE_FINISH.md).

```
python tools/check_native_saves.py --exe soul/port/build/Debug/soul_port.exe
```

The checker captures an actual checkpoint through normal movement, exits, then
starts twelve new processes using Continue at widths 240/569, 30/60/144 FPS and
both interpolation settings. All simulation diagnostics match across display
settings; per-width render CRCs match. It verifies saved HP=23/MP=17 rather than
Restart's HP refill, weapon/unlocks/fragments, position, reconstruction and
checkpoint reactivation. Further runs check menu availability without writing,
confirmed New Game deletion during the prologue, fresh progression at level
entry, corrupt/missing saves, failed-write recovery through two in-session restarts and
automated user-slot isolation. Ignored native captures show the Continue menu
without clipping at both widths.

The original Java menu/lifecycle and checkpoint checks remain the behavioral
reference; this storage pass introduces no Java patches or original-format
compatibility. Native upgrade arrays, other levels' special snapshots, original
RMS importing, multiple slots, later-level construction and general mutable
actor state remain unfinished. First-level completion/unlocks, world unload,
version migration and completed Continue are now implemented in NATIVE_FINISH.md.
The full game remains unfinished; selected-level construction is next.

Validation on 2026-10-29: default Java regeneration passed compilation with zero
errors, ReferenceCheck's 4218 comparisons and all existing fixtures. Debug and
Release passed all 80 CTests. Both passed the existing 6785-row checkpoint oracle,
seven milestones and 24 restart host chains. Debug and the rebuilt package passed
the desktop-save checker; the package matches Release. The existing Release
startup/English story/HUD/pause checker also passed. No game data or saves are
included in the package.


NATIVE_INTRO.md now executes reconstructed type-20 trigger 82. Resuming a
checkpoint inside its volume can replay the intro while retaining the saved
meters/equipment; this follows the existing nonpersistent-trigger reconstruction
policy. The save checker accounts for that active scene instead of assuming
that the restored trigger does nothing.
