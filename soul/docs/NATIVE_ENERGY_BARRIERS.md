# Native type-80 energy barriers

Type 80 is a vertical energy barrier, previously mislabelled a collapsing
platform. The native world now constructs its extended collision body, runs
its lifecycle, supports its selected linked triggers and draws lightning with
sprites at both endpoints. Level 1 remains disabled until type-3 enemies and
the remaining level-specific handlers/routes are verified.

## Evidence and corrected name

The record constructor selects effect sprite 1, retains its initial sprite
bounds and extends the bottom by parameter 6. It then sets flag 128 to hide
the actor. `Actor.bd()` enables N/O/P (separation, side contact and immovable
push response), waits for animation endings and removes the actor after a
release. It does not provide the type-33/type-50 rider path.

`Actor.paint()` calls `Game.b(int,int,int)` for three lightning strands, draws
the endpoint sprite at `y + height`, then draws it again at `y`. Linked-trigger
operation 7 sets action 101 and requests animation 31. These constructor,
dispatch, paint, linked-operation, contact and reset call sites were inspected
in original b/e bytecode. The semantic name in names.map is now
`updateEnergyBarrier`; CLASS_MAP.md records the evidence. Generated Java was
regenerated, not edited directly.

## Lifecycle, contacts and selected links

Action 0 enables the priority body; at an animation ending it enters action
102 and requests animation 14. Release enters action 101 and queues animation
31. At its ending, flag 8192 retains an inactive owner with flag 1024; clearing
8192 first permits final removal. Pause and flags 1024/16384 suppress handler
updates. Region exit restores the record and marks a respawnable owner inactive;
it clears contact flag O while preserving the original N/P and animation-reset
quirks. A cleared respawn flag permits ordinary removal instead.

The body uses the existing original-bytecode-checked type-37/type-80 priority
contact pass, before ordinary enemy contacts, with a 2560-pixel correction cap.
It is an immovable side blocker and does not rewrite collision tiles or become
a rider platform. Fire/ice sword poses do not replace its linked release path.

The shipped level-1 chain is implemented for UID 32:

- Trigger 354 enables it when source UID 352 overlaps the trigger rectangle.
- Trigger 124 observes source flag 1024 and clears respawn flags on 354 and 32.
- Trigger 356 observes the same inactive source and requests barrier release.

Triggers retain their lazy source/target links, original operation order and
retained-versus-removed lifecycle. They enter the placed update queue rather
than activating a newly enabled barrier in an already frozen snapshot. Unload
clears trigger borrowers before barrier and enemy owners. Gates can also resolve
barrier action states through the existing UID resolver. Checkpoint removal and
region transitions retain the current native ownership boundaries. Full original
persistence/pool checkpoint state for this later encounter is still unfinished.

The source's type-3 AI is still unported. The chain oracle drives a controlled
source position/flag signal; it is not an arena fight or full level play-through.
Native world binding currently resolves owned type-1/type-2 source signals;
the type-3 port must supply its owned signal before this encounter becomes playable.
Other later-level type-40 conditions and targets remain outside this selected
family, even though all ten placed barrier constructors are checked.

## Lightning and presentation policy

The native generator matches the original line commands, colors, clipping
guards and RNG consumption for the checked calls. Canvas draws cached segments
and the two sprite endpoints. Width 569 reveals a barrier beyond the original
240-wide viewport instead of stretching it.

Original painting consumes shared Game.random. Native bolt geometry advances
once per 83 ms tick using a separate cosmetic stream seeded by UID; repeated
presentation consumes no gameplay RNG or animation events. Geometry is generated
in barrier-local coordinates and clipped by Canvas. This is an explicit desktop
presentation policy, not original shared-game-RNG or screen-clipped pixel parity.
The general Game.paint/global RNG ownership boundary remains unfinished. The
draw-command oracle is not a MIDP Graphics implementation or a pixel oracle.

## Checks

- 2410 original/reference/native lifecycle rows: fifteen handler/animation/reset
  timelines and all ten placed constructors across levels 1/2/4/7.
- 640 rows for the actual level-1 trigger records, cached links, flags and barrier
  lifecycle, with a controlled source signal.
- 726 original/reference/native lightning-command and next-RNG rows. Only
  drawLine is intercepted; the original generator bytecode remains unchanged.
- Seventeen data-free assertions for lifecycle, priority contacts, world contact
  ownership, release/reset, teardown, cached renders and widescreen visibility.
- Twelve controlled selected-level world runs at widths 240/569 and 30/60/144
  FPS, with frequent versus sparse presentation. State, geometry and gameplay
  RNG agree; each run completes release in 100 simulation ticks.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_energy_barriers.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
```

Validation on 2026-10-30: default regeneration exited 0 with zero compile
errors, ReferenceCheck's 4218 comparisons and all registered checks passing.
Debug and Release passed all 106 CTests. The new manifest matched in both
configurations and the rebuilt data-free package. Existing contacts, selected
level reconstruction, timed switches, checkpoints/Restart, finish/Continue/replay
intro and first-arena activation host regressions passed, including their
240/569 and FPS matrices.

Oracles, logs and binaries stay ignored; game data is not committed or packaged.
