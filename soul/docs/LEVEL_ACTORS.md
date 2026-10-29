# Level actor construction and reconstruction

`LevelCheck.java` executes construction from all nine shipped object streams,
using their real collision maps, sprites, fonts, strings and actor stat tables.
It compares original bytecode with regenerated Java before gameplay updates.
This completes a construction pass, not the staged loader, runtime or native port.

## Record stream and ownership

Original `e.c(boolean)` (e.java:3233), now `constructLevelActors`, reads the
object bytes in `bF`. A signed byte gives the record's short count, followed by
little-endian signed shorts. All shipped counts fit 6..20. Each record overwrites
only its prefix of the shared 20-short `bE` buffer. Its unused tail retains the
preceding record's values; there is no per-record zero fill.

The constructor receives the byte offset of the first short and the short count.
Respawn registration retains this slice in Actor's pool and UID table. The
player (type 0) goes into `player`, type 69 uses dedicated `bm`/`bn` slots, and
other actors enter the ordinary array. The first type 28 also becomes `bo`.
Do not flatten these dedicated actors into the ordinary array during translation.

Non-player records advance a persistence index. Records skipped through the
removed-UID table also advance it. Ordinary actors receive the index after
`addActor`, which otherwise assigns the transient marker -98.

**Type 69 is constructed twice** when filling an empty dedicated slot: first
through the common non-player branch, then again for `bm` or `bn`. The retained
actor has its default persistence index 0; the temporary instance received the
stream index. Original `e.c(boolean)` bytecode offsets 159..247 and 280..334
confirm this. The reference preserves the construction calls and their effects.

After ordinary insertion, `Actor.finalizeRecordCollision` (original
`b.a(short[])`, b.java:1875) fills collision cells with tile value 24 for types
61/79 using their bounds. Its record parameter is unused in this build. This is a construction
side effect, not an actor-link resolution pass. World comparisons include the
collision grid and tile rollback storage, alongside Actor fields and statics.

## Removal and restart rules

Fresh construction (`false`) clears all 250 persistence bytes. Both modes still
consult the selected removed-UID row `bH`. The lookup scans indices **0 through
`bI`, inclusively**; bytecode offsets 101..132 confirm `if_icmpgt`, not `>=`.
Even a stored count of zero examines slot zero. Keep this behavior in the
reference rather than substituting a conventional count-based search.

Restart construction (`true`) skips a record marked -99 only for types 35, 23,
48 and 82. `staysRemovedOnRestart` (original `e.I(int)`, e.java:3229) implements
this exact set. Other reconstructed records clear their persistence byte. UID
table skips happen first, leave their persistence byte untouched, and apply to
either mode. The player does not consume an ordinary persistence slot.

The separate `reconstructLevelActors` wrapper (original `e.d(boolean)`,
e.java:3797) also handles saved level 5 links and camera/controller state:

- With a true argument on level 5, read three linked UIDs and 54 snapshot bytes.
- Call construction; `continueFromCheckpoint` (original `dK`) forces fresh construction
  even when the wrapper received true.
- Match constructed ordinary actors to the three UIDs and restore their slots.
- Restore controller UID `am` and associated camera/actor offsets when present.
- Clear each linked pointer whose saved UID is -1.

`restoreCheckpoint` subsequently restores player/progress state; see SAVES.md.
The reconstruction wrapper does not itself advance gameplay or render actors.

## Validation and remaining work

Default regeneration runs 27 construction scenarios: three for each shipped
level (fresh construction from nonzero persistence, restart with all persistence
bytes marked removed, and a UID placed in slot zero with count zero). Independent
expectations check record identity/order, persistence indices and bytes, respawn
offsets/counts, ordinary actor totals and the final shared parameter buffer.
Original/reference comparisons cover ordinary/player/dedicated actors, Actor
statics and selected world fields.

A further level 5 scenario runs the reconstruction wrapper with two saved linked
actors and an absent third UID. It checks actual world-member pointer identity,
all seven restored snapshot fields and clearing a stale pointer. These fixtures
pass **17898 expectations and 6763 world comparisons**. The save
checker separately covers checkpoint coordinate restoration.

Fixtures load unchanged original map readers using seeded pack streams, as in
ScriptCheck. They load all available gameplay sprites rather than exercising
the staged loader's dependency/cache scheduling. They construct no Canvas and
perform no drawing, audio, RMS access or gameplay ticks. The all-removed scenario
is a controlled stress fixture, not a claim of a reachable save state.

CHECKPOINT_LIFECYCLE.md now covers controlled checkpoint activation, test RMS,
two complete restart calls per level and repeated loads. It confirms the retained
UID table and overwritten pool-array aliases without changing the reference.
NEW_GAME_AND_CONTINUE.md now tests the Continue override and loading stages 1/79/80.
LEVEL5_CONTROLLER.md now covers controller/hazard restoration and live linked
snapshot capture. Still pending: deeper linked script/controller/platform
interactions and actual staged boot/loading. The native
port still needs an explicit pool/registry ownership policy.
Pack 9's map-dimension mismatch remains preserved (SCRIPTS_AND_LEVELS.md).
