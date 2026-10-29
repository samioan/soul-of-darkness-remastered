# Arena chest unlock and reward/gate connection

Trigger 55 now enables chests 56/57 after both flying arena enemies (113/48)
finish their final lifetimes. Normal first-level gameplay therefore connects
trigger 129, the first wave and its respawns, trigger 54, the flying wave,
trigger 55, chest 57's magic pickup/tutorial, chest removal, trigger 166 and
gate script 165. The complete first level and game remain unfinished.

## Original evidence and native ownership

`Actor.updateLinkedTrigger` (`bh`) uses condition 4, actor mode 0, operation 0
for both wave triggers. Its lazy source/target caches, persistence indices,
missing-first-source quirk and index-zero shortcut are shared with the previous
pass. `Actor.enableLinkedActor` (`l(b)`) clears flag 32 and clears flag 128
outside action 5. For type 52 it also spawns type 39 / sprite 1 / animation 28
at the chest with priority 302 and inherited facing. Original `b` bytecode
confirms these operations, including the separate type-20 forced-update branch
which remains outside the native target subset. No Java names or decompiler
patches changed.

`RemovedEnemyTrigger` now supports the shipped flying-to-container link in
addition to the ground-to-flying link. Its cached target has an optional enable
callback; the container callback adds the reveal effect to the existing native
combat-effect owner. Its first handler occurs next tick, while its first
animation advance occurs on the birth tick. The callback runs only for resolved
targets, and one-shot deletion prevents repeat reveals.

`CombatWorld.bindContainers` borrows the completed host container vector once;
the vector's elements must remain stable while links are cached. The host binds
after construction and never adds further placed containers. Before nonplayer
dispatch, the world freezes the host containers' flag eligibility. Consequently
chests become visible on the enable tick, but their first handler runs on the
following tick. Shipped chests 56/57 precede trigger 55 in record order. Chest
56 preserves its crystal-reward flag 512 and facing; chest 57 preserves its
negative magic reward. Existing chest interaction, ability ownership, removal
trigger and gate controller handle the rest of the chain.

This is a bridge between existing native owners. General mixed actor dispatch,
container distance culling, arbitrary trigger operations, alternate target types,
script/cutscene ownership and saved removed-UID restoration remain separate
boundaries. The normal path does not clear chest flags from a test fixture.

## Checks

Default Java regeneration runs `ChestUnlockCheck.java`: 568 original/reference
expectations and 2247 native-comparable rows. Seven actual Game world arenas
cover normal and reverse final-removal order, one surviving source, pause,
hidden/frozen triggers and reversed chest facing. The fixtures construct two
chests before trigger 55 and two flying sources after it, explicitly seed fresh
persistence indices and initial camera buckets, and apply lethal damage on
selected ticks. Original handlers perform cleanup/removal and enable the chests.
Snapshots compare both chest states/bounds, trigger caches/lifetime, all reveal
effects through removal and the final random stream. The analysis ImageIO
adapter is not a MIDP runtime or a full original-level play-through.

```powershell
python tools/check_native_chest_unlock.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

The checker compares every oracle row, seven timed milestones and 24 live host
chains at widths 240/569, 30/60/144 FPS and interpolation on/off. The headless
`--arena-script` and `--game-arena-script` fixtures place an invulnerable player
at the arena. Normal movement/melee defeats six enemy lifetimes, then normal
movement and fire interact with chest 57. The magic reward, beam, tutorial
dismissal, chest removal and gate opening use their existing handlers. The game
fixture also traverses startup and pause/resume. No enemy health/removal,
chest lock, reward completion or gate start is forced by this host fixture.

The direct fixture retains the closed gate at tick 180 (one flying source still
alive), enters the beam by tick 200, reaches the tutorial by tick 240, starts
the gate controller at tick 260 and completes it by tick 320. The final gate
is at Y=1415/action 103 with selected script rewound and its trigger removed.
The older combat-only second-wave fixture now checks both revealed chests while
leaving them unopened; it still verifies the six real melee defeats.

Eleven new data-free assertions bring player checks to 364. Three CLI guards
bring CTest to 66 tests. Next: magic casting/weapon controls and remaining
first-level progression, followed by broader scripts and checkpoint/save
integration. Full gameplay remains unfinished.

Validation on 2026-10-28: full default Java regeneration passed; Debug and
Release passed all 66 CTests and the new checker. Existing linked/world-order,
region, combat, transient, bridge, platform, gate/script, passage, container,
ability, wave and frontend regressions also passed in both builds. Native game
captures confirm the revealed chests and opened doorway with the English HUD
at widths 240/569. `build_dist.bat` rebuilt the data-free package; its binaries
match Release and pass all 2247 comparison rows and 24 full arena chains.

The following fire pass now makes the unlocked ability usable with K/0 and
verifies the normal arena-to-casting chain. See NATIVE_FIRE_MAGIC.md.
