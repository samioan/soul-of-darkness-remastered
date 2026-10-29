# Native chests and spawned crystals

The first-level prototype now constructs all six type-52 records. Four are
ordinary chests and two retain their script-locked/invisible flags. Approach an
ordinary chest while standing or walking; when the original fire prompt appears,
press E, J or Enter to open it. R resets the prototype.

An opening chest emits a health crystal and changes to its open pose, waits,
then disappears. The [crystal reward pass](NATIVE_CRYSTALS.md) now continues
through the beam and reward panel, counts fragments, updates meters and resumes
movement. This is still an in-memory prototype without a completed save flow or
level play-through.

## Implemented subset

`src/container.cpp` follows type-52 initialization, the common fixed-point
integrator, `updateContainer` (original `b.ag`), the false/false tile probe path,
falling/landing and animation clocks. Sprite 2:34 supplies chest poses and bounds;
sprite 2:18 animation 25 supplies the fire prompt. Flag-8 custom rectangles do
not override type-52 sprite collision bounds in the original. The parameter
buffer retains tails, as before. Sprite/map owners outlive the containers and
temporary borrowing is disabled.

Contact enters action 106. A fire edge in that state requests animation 1 and
enters 101; a press on the very first contact tick does not open the chest.
Leaving contact removes the prompt, while fire on the departure tick can still
open an existing prompt because the fire check precedes prompt cleanup. Opening
animation completion creates one crystal (animation 1 for health, 3 for magic),
requests animation 2 and enters 102. Its 1000 ms timer requests animation 3 only
when negative; final animation completion removes the chest. These details are
verified against bytecode snapshots rather than inferred from appearance.

The native single-crystal spawn preserves UID -1, initial zero fixed positions,
position/facing flags, 6000 ms lifetime and the original deferred facing-bit
refresh. Spawned objects begin updating on the next tick; their animation clock
can advance on the creation tick. Ordinary health/magic spawned pickups also
have checked attraction, collection and expiry paths. The chest-pass oracle
stops at action 103; the native runtime now continues through the separate
[NATIVE_CRYSTALS.md](NATIVE_CRYSTALS.md) reward flow. First-level
ordinary chests all select the health-crystal branch.

E/J/Enter maps to original fire bit 65536. Input remains latched at tick end;
fire clears pending direction groups while retaining previously held directions.
Player input-clear requests take effect before object updates. Fire currently
interacts with chests and does not perform combat.

Chest/placed-pickup drawing precedes prompt/player drawing. Spawned pickups draw
after the player (original priority 308). All simulation and animation stay on
83 ms ticks; drawing does not advance them. The prototype still uses its simple
following camera and direct object lists, not the original activation queues,
registry reuse, paint sorting or save-removal bookkeeping.

## Validation

Default Java regeneration writes ignored `soul/analysis/container-oracle.tsv`.
Original and regenerated Java are compared for every actor field and the existing
selected world state. The native checker matches **816 snapshots/spawn events**:
eight chest scenarios (idle, opening, departure, magic reward, falling, pause,
first-contact fire and script-locked) and five spawned-pickup scenarios (health,
magic, both crystals through contact and ordinary-pickup expiry).

Chest fixtures update the root only; child prompt/pickup behavior is checked
separately. Spawn events identify newly allocated actors by identity, including
reused registry slots. The fixture supplies the removed-UID storage needed when
a persistent chest disappears. Upgrade traces stop at action 103; neither the
Java test adapter nor these traces run a real MIDP screen or original gameplay.

Twelve data-free assertions extend player/input/pickup coverage to 54 assertions.
Two CLI rejection checks bring CTest to 23 tests. Debug and Release builds and
the data-free Release package are checked, alongside the existing 1200 movement
snapshots, 381 placed-pickup snapshots and 930 record rows.

The explicit headless `--container-script` fixture starts the player at the
first chest and supplies a latched fire press when its prompt appears. Twelve
runs at widths 240/569, 30/60/144 FPS and both interpolation settings must open
exactly one chest and complete its reward, return to idle and walk away, with equal state
and equal pixels within each width. This controlled initial placement is not
an input recording from the normal player spawn. The ordinary movement-script
regression still starts at the shipped spawn.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_containers.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --movement-preview --data soul/extracted --width 569
```

## Remaining boundaries

Crystal reward timing, fragment counts, maximum-meter changes and returning
to gameplay are covered by NATIVE_CRYSTALS.md. The shipped collection effects
and source-pickup disappearance are covered by NATIVE_COLLECTION_EFFECTS.md.
Persistence remains absent. Burst loot,
coin/ability rewards, scripted container variants and attack activation are not
ported and fail explicitly if reached. The two locked first-level chests remain
dormant without scripts. Original Game activation/camera queues and full actor
contacts are still needed before claiming general object behavior. Enemies,
combat, damage/death, platforms and level completion remain pending.
