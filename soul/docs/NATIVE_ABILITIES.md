# Magic pickup and chest reward

The native port now owns the type-48 / sprite-27 / animation-2 actor emitted by
first-level chest 57's reward -2. It grants the magic unlock on player contact,
restores the magic meter, pulls into the player, runs the beam/tutorial, floats
above the player and removes itself after interaction completion. This connects
the reward to the existing chest-removal trigger and script-165 gate controller.
Normal chest 57 now unlocks through the completed arena trigger chain; see
NATIVE_CHEST_UNLOCK.md. The earlier controlled ability fixture still explicitly
unlocks that chest to test this reward owner in isolation.

## Original evidence and native ownership

`Actor.updateContainer` (`ag`) spawns type 48 with sprite 27 and animation 2 for
reward -2. When `Actor.bu` is already true, it changes the reward to 0 and emits
one ordinary health orb with random X/Y offsets, rather than another ability.
The native container uses the shared Java random stream for those two draws.
General burst loot and coins remain unsupported. The later ice pass adds its
separate weapon reward; see NATIVE_ICE_WEAPONS.md.

`Actor.updateAbilityPickup` (`bg`) only grants this unlock/refill/tutorial in
level 0 when `bu` is false. It sets player pose 25 immediately for player action
0 or 2, stops motion and hides the weapon. Action 102 uses `Game.e(int,int)`'s
integer approximate distance, retains velocity outside radius 40960, attracts
at speed 2048 inside that radius, and snaps when distance is less than 2048.
That snap starts interaction 3, sets beam anchors and enters action 100.
Action 100 floats upward at -2048, clamps above the player's bounds and removes
the pickup once the interaction is inactive. Facing flags, previous-pose bounds,
fixed-point resynchronization and animation clock timing are retained.

`Game.a(int,boolean,int)` advances the beam through 38 visible ticks. Tick 39
starts interaction 14 if tutorial text is pending, otherwise ends immediately.
The tutorial's first clock tick occurs on that same frame. `Game.c(int)` ignores
input through clock 8; accepted fire/down presses clear text, and `Game.d/l`
release the interaction on the following tick. `AbilityInteraction` retains
these clocks and input boundaries. The native popup uses short English text
and desktop layout. The following fire pass now adds the verified K/0 casting
binding; see NATIVE_FIRE_MAGIC.md.

These branches and call sites were inspected against original `b/e` bytecode
in ignored analysis/. No Java names, decompiler patches or sprite parsing changed.

## Host integration and boundaries

The host loads original sprite 2:27 once, owns spawned ability slots and processes
only preexisting slots in each handler pass. Births advance their animation on
the birth frame and begin handlers on the next tick, like the existing pickup
owner. Magic contact clears input and hides the native weapon. During the beam,
the player pose, ability and supported effects continue; other world owners are
held. The tutorial freezes those actors until dismissal. Ordinary gameplay then
resumes, the ability removes itself, and the chest's remaining lifetime resumes.
Actual chest removal starts trigger 166 / script 165, opening the doorway.
No fixture directly removes the chest or directly starts the gate script.

Region cleanup discards existing ability transients before new births and clears
their pending modal/input state. Unlock state remains in the session's meters.
Restart reconstructs the world; save/Continue persistence is still unported.
The full mixed actor registry and original selective pause/cutscene machinery
remain unfinished. These owner bridges are not a full original-world trace
equivalence claim. Later fire/ice passes now add casting, weapon switching and the ice pickup.
Ice projectiles remain a separate dependency.

## Verification

Default Java regeneration runs `AbilityCheck.java`, which emits ignored
`analysis/ability-oracle.tsv`: 1498 native-comparable rows and 1502 original/
reference expectations. Two chest arenas check the exact magic and repeated-
unlock health reward events, chest animation/lifetime and removal. Ten pickup
arenas cover normal/left/offset contact, repeated unlock, nonzero level, no
overlap, pause, hidden/frozen actors and attraction across the radius boundary.
They compare fixed/integer coordinates, velocity, flags, animation, bounds,
player pose, unlock/refill, weapon hiding and interaction anchors. Interaction
completion in these handler arenas is an explicit fixture input.

`AbilityModalCheck.java` independently executes original beam/tutorial/input
bytecode. It uses CrystalCheck's isolated drawing-only hooks plus a softkey
drawing no-op, preserving the original timers, state entry and input code.
Seven cases (normal, early, held, no text, pause, down, cancellation) emit
504 rows and 2016 original/reference expectations to ignored
`analysis/ability-modal-oracle.tsv`. Text drawing/pagination and phone pixels
are outside this adapter's claim.

`check_native_abilities.py` compares all 2002 rows. Twenty-four real-level host
fixtures cover chest opening, magic collection, beam/tutorial dismissal, return
to gameplay and the actual chest/trigger/gate chain at widths 240/569, FPS
30/60/144 and both interpolation settings. Twelve additional timed game captures
check English tutorial/HUD pixels across those settings. The game fixture also
passes through the existing scripted pause/resume boundary during collection.
Fixtures place the player at chest 57 and clear its lock flags explicitly;
there is no automated full-level play-through claim.

Eighteen new data-free assertions bring player checks to 320. Three CLI guards
bring CTest to 57 tests. Debug and Release checks and the local asset-free package
are rebuilt; images/oracles/logs remain ignored.

```powershell
python tools/check_native_abilities.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

Next: dormant enemy activation and the type-40 trigger chain that unlocks chests
56/57 in normal play, including the still-unported type-2 enemies. Full first-level
progression and the complete game remain unfinished.


The preceding first wave now activates normally through trigger 129; see
NATIVE_ENEMY_ACTIVATION.md. The following type-2 wave and chest-enable trigger
remain pending, so the ability fixture's explicit chest unlock remains isolated
validation setup and normal chest 57 stays locked.

The type-2 wave and trigger 55 now complete the normal chest unlock; the live
arena fixture validates both waves through magic collection and the gate. See
NATIVE_CHEST_UNLOCK.md for current progression and remaining boundaries.

Fire casting and MP recovery now follow this reward; K/0 controls and the
normal arena-unlock-to-casting check are documented in NATIVE_FIRE_MAGIC.md.
