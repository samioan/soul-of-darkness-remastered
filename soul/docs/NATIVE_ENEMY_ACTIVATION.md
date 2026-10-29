# First-wave enemy activation

Normal first-level play now runs type-40 trigger 129. When Kale overlaps its
rectangle, it enables type-1 enemies 114 and 117 and removes itself. Each enemy
has one allowed respawn; its final defeat removes the placed actor. This is the
first stage of the chest-56/57 unlock chain. The following trigger 54, type-2
enemies 113/48 and trigger 55 remain pending, so this pass does not unlock those
chests or the gate in normal play.

## Original evidence

`Actor.updateLinkedTrigger` (`bh`) resolves source and target references lazily.
Condition 1 calls `Actor.g`, which refreshes the source actor's bounds before
checking inclusive overlap. Actor mode 0 / operation 0 calls enable on each
resolved target and then removes the trigger. An absent target does not delay
deletion or prevent the other targets from enabling. Original `removeLinkedTrigger`
clears all six links before `Game.removeActor` releases bounds.

`Actor.enableLinkedActor` clears flag 32, and clears flag 128 unless target
action is 5 (respawn fade). It preserves other flags. Enemy targets do not gain
flag 16, so newly enabled enemies enter the next frozen update queue. An enemy
already forced into the current queue retains its slot and runs at its original
record position. The native `PlayerEnableTrigger` supports one player source
and type-1 targets only. Loading rejects non-player sources and unsupported
target types; conditions that enable type-2 enemies or chests remain separate
work. Hidden/frozen/paused triggers skip lookup and mutation.

Flag 2048 is not an update pause. `Actor.resetOrRemoveInternal(true,...)` decrements
the respawn count, restores the spawn record and removes the actor when flag 2048
is present and neither flag 4 nor a remaining respawn requires fading. The
native cleanup now retains permitted fades and removes the final lifetime,
clearing collision/attack/visual bounds and excluding future contacts. Region
exit uses the existing non-death reset and does not apply terminal removal.
The native placed-removal ledger records these removals for subsequent owners;
general UID-slot reuse and save-backed reconstruction remain unported.

All three previously omitted flag-2048 type-1 records now load. UID 205 is an
ordinary active enemy at (78,1040); UIDs 114/117 initially retain flag 32 and wait
for trigger 129. The first level contains 20 supported type-1 actors. The old
`dormant_enemies` diagnostic counts omitted unsupported records (now zero);
`wave1_locked` reports the two loaded enemies' actual waiting flags.

The original `Game.drawActors` / `Actor.isVisible` path refreshes nearby bounds
even when an actor is outside the frozen update queue and ultimately outside
the viewport. The new waiting-enemy arena exposed that distinction. The native
world now performs that nearby refresh before deciding whether to advance the
animation. This preserves the post-enable next-queue boundary without stale
animation bounds. Camera bucket eligibility remains based on 240-wide reference
coordinates; presentation width does not affect the wave.

The relevant `b/e` bytecode and call sites were inspected. No generated Java,
names or decompiler patches were edited.

## Verification

Default regeneration runs `ActivationCheck.java`, emitting ignored
`analysis/activation-oracle.tsv`: 2146 rows and 1382 original/reference
expectations. Thirteen handler cases cover overlap, inclusive edges, misses,
pause, hidden/frozen triggers, missing/late sources and targets, fade targets
and preserved flags. Four original `Game.updateWorldActors` arenas cover
trigger-first, enemy-first, forced-current-queue and delayed entry. Their queue
rows preserve the removed trigger's cleared slot as sentinel -999. Six lifetime
arenas cover final death, one permitted respawn followed by final death, forced
respawn, preserved flags, pause and ordinary active flag-2048 AI. Final random
stream values are compared too. These are controlled test-adapter arenas, not
a complete MIDP runtime or full game equivalence claim.

`check_native_activation.py` compares every row and runs 24 actual first-level
arena fixtures at widths 240/569, FPS 30/60/144 and both interpolation settings.
Fixtures place Kale at the arena and grant invulnerability, then use ordinary
movement/melee input. They do not enable targets, alter enemy health, remove
enemies, unlock chests or start the gate script directly. Both enemies and their
allowed respawns are defeated, producing four deaths and two final removals.
Half the fixtures also traverse native startup and the existing pause/resume
boundary. Separate captures assert the initial waiting/enable boundary.

Sixteen data-free assertions bring player checks to 336. Three new CLI guards
bring CTest to 60 tests. Existing startup/live-combat placement assertions now
expect 20 loaded type-1 actors; the earlier unsupported subset counted three.
Other combat, loot, platforms, passages, ability and gate checks remain regressions.

```powershell
python tools/check_native_activation.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

Next: type-2 enemy ownership/AI and trigger 54's removed-source enable condition,
then trigger 55's chest enable effects and the normal ability/gate sequence.
Complete first-level progression and the full game remain unfinished.
