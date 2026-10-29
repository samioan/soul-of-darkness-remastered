# Native player movement prototype

The executable now has a controllable first-level movement prototype:

```powershell
.\soul\port\build\Debug\soul_port.exe --movement-preview --data soul/extracted --width 569
```

Use Left/Right or A/D to walk, Space/Up/W to jump, Down/S to crouch and Up to
stand. E/J/Enter attacks and opens nearby chests. R reloads the prototype, F8 changes width and Esc exits. Window caption
and overlay identify the prototype and its missing combat/NPC systems. Ordinary
launch still opens the calibration scaffold; other asset previews remain available.

## Implemented subset

`src/player.cpp` reads the length-prefixed actor stream with signed short
parameters. It retains each record's source offset/count and the shared 20-short
buffer tail, rather than zero-filling short records. Parsing covers all 930
records across nine levels. Type 0 and a health/magic subset of type 35 are constructed natively;
other records remain data for subsequent actor work. See [pickups](NATIVE_PICKUPS.md). The prototype loads pack
3's actual player record (UID 186, spawn 121/261), maps and sprite 2:0.

Player initialization preserves unsigned coordinate reads, initial facing/flags,
animation and sprite-derived collision/attack/visual bounds. Native player and
map/sprite owners have explicit lifetimes; borrowing temporaries is disabled.
Custom player bounds and unsupported records fail instead of being guessed.

The movement subset follows original Actor's common integration, vertical tile
probe, tile resolver and walking/idle/crouch/jump/fall/landing paths. Original
`b` bytecode was inspected using `tools/bytecode.py b`; generated Java was not
edited. Fixed position keeps eight fractional bits and modulo-32-bit integration
with explicit signed right shifts. Velocity integrates before acceleration.
Ground speed starts at 1280, rises by 320 to 2560; standard jump is -4608,
normal gravity 768, short-jump gravity 1536 and downward speed caps at 4608.
These are original values, not replacement physics tuning.

Collision preserves signed tile bytes, level-mode boundary responses, original
integer division/remainders, slope formulas, head/feet probes, wall exceptions
and correction order. Pack 9's short collision grid is not padded: a lookup
beyond stored cells throws a checked error. The interactive prototype currently
opens level 0 only. Construction-time object collision writes are not applied.

Pending animations, frame durations, pose-dependent bounds and the separate
animation clock follow the controlled reference paths. The clock advances once
per simulation tick after player control. Drawing never updates player state.
The native movement renderer shows current tick positions; player/camera render
interpolation is still pending. FPS selection changes presentation pacing.

MovementInput preserves pending versus latched presses, immediate held/released
state, direction filtering of pending presses, end-of-tick latching, input clear
and the short-jump hold counter. OS repeat does not create repeated press edges;
focus loss clears keys. Desktop aliases share logical held bits. This is the
movement input subset, not the complete menu/fire/weapon input runtime.

The following camera is an explicit prototype camera: it centers horizontally,
keeps the player's feet near Y 220 and clamps against the map using viewport
width. It reveals more world at width 569. The original Game camera's dead zones,
look-ahead, scripted controllers and shake have not been translated.

## Validation

Default Java regeneration now writes ignored
`soul/analysis/player-core-oracle.tsv`. ActorCheck emits snapshots directly from
original bytecode after comparing original/regenerated state. The existing
700-tick actor trace and check suite retain their prior scope. The new oracle
contains 1200 before/after-animation snapshots over 600 controlled ticks:
walking, jump, crouch, wall, pause and ceiling. Each snapshot includes fixed/pixel
positions, velocity/acceleration, flags, pending/current animation clock, action,
contact flags and collision/attack/visual bounds. The melee fixture remains Java
analysis coverage and is excluded from the native movement oracle.

`check_native_player.py` compares those rows directly with the native manifest,
then independently checks 930 record envelopes and retained tails against Python.
Thirty-one data-free native assertions cover parsing, initialization, integration
order, jump/short-jump values, input latching/clear, pause and prototype boundaries.
The pickup pass adds eleven assertions to this executable (42 total).
The native suite retains 21 CTests per configuration. Debug/Release and
the strict data-free Release package pass.

Twelve actual first-level headless runs use a fixed tick-indexed input sequence
(walk, jump, release) at widths 240/569, FPS 30/60/144 and both interpolation
settings. All produce 60 ticks and equal player states; final pixels match within
each width. The script reaches X 423/Y 259 from X 121/Y 261. These runs check
host integration and presentation independence, not original full-level gameplay.
Short-jump input latching and slope branches have native/source/synthetic checks;
the 600 native/original movement ticks do not cover every slope or key sequence.
The Java oracle still uses test adapters, not a MIDP runtime or phone capture.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_player.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --movement-preview --movement-script --data soul/extracted --headless --width 569 --fps 60 --frames 300
```

## Remaining gameplay boundary

Beyond health/magic pickups and ordinary chests, enemy/object construction and updates, live actor queues/platforms, combat,
magic/weapon 1, damage/death, scripts, level completion, saves, audio, HUD and
boot/menu loading remain absent. The prototype stops when it samples a damage
or death tile and asks for R to reset; that stop is a prototype boundary, not
the original damage response. Actor-controlled gates/platforms are absent.
This is not a level play-through or a completed game port.

The [native chest](NATIVE_CONTAINERS.md) and [crystal reward](NATIVE_CRYSTALS.md)
passes now resume gameplay after collection. The [collection effect pass](NATIVE_COLLECTION_EFFECTS.md)
adds effect lifetime and hidden source pickups. [Side contacts](NATIVE_CONTACTS.md)
now have native helpers and controlled player integration. The preview still
needs live enemy bodies. [Weapon-0 melee](NATIVE_MELEE.md) now adds standing/combo/air
attacks and slash presentation. Next: type-1 enemies and damage/reactions against
original-bytecode fixtures. Keep this movement baseline
as a regression while extending the native runtime.
