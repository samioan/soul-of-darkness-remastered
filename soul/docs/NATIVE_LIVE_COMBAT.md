# First-level native combat preview

The existing `--movement-preview` now constructs **seventeen ordinary type-1
enemies** from level 0 and runs reciprocal combat. Three flagged records
remain dormant. Two also carry script-pause flag 32; all three carry flag 2048,
whose terminal-removal ownership is not connected yet. Walk, jump, crouch and
attack with the existing controls; enemies
pursue, attack, receive weapon-0 damage and die. Loot reveals, attracts and
collects; blood, death bursts and floating numbers render with original sprites
and the bitmap font. Accepted hits hide the slash and update health. Defeat
finishes the death animation and displays a native restart panel; R reconstructs
the preview from spawn. This is an interactive gameplay slice, not a complete
port or a full original Game runtime. The default executable is still the
calibration host in this historical pass. NATIVE_STARTUP.md now opens a native
logo/title/menu/credits flow by default when extracted data is present.

```powershell
.\soul\port\build\Release\soul_port.exe --movement-preview --data soul/extracted --width 569
```

A/D or arrows move, Space/W/Up jumps, Down/S crouches, E/J/Enter attacks and
opens chests, and R now restores the latest session checkpoint (see
NATIVE_CHECKPOINTS.md). F8/F9/F10/F11 retain width/FPS/interpolation/
fullscreen controls. Health/magic still use the temporary desktop overlay.
Player/world render interpolation and the original HUD remain unfinished.

## Tick and queue ownership

`CombatWorld` borrows the host's immutable sprites/maps and mutable player/meters.
It owns ordinary enemies, the shared deterministic Java random stream, player
invulnerability, body snapshots, combat effects and loot events. Source assets
remain external. Construction keeps enemy registry order and counts unsupported
flagged records separately instead of activating them prematurely.

As in original `Game.updateWorldActors` (`e.W()`), the player updates first using
the previous tick's enemy queue. Side-contact corrections and touched flags copy
back to the owners before their handlers run. Bounds are refreshed at the
original contact boundary; the stale rectangle after a positional correction is
preserved. A player beginning death skips that contact refresh. The new queue
then runs enemies in registry order. Reciprocal damage occurs inside each enemy
handler, before its final facing flags, so damage and reaction can affect later
actors on the same tick. Only base weapon 0 is connected.

Deaths emit loot events into the existing pickup owner. Newly spawned pickups
wait until the next update; clocks advance separately at the tick's paint
boundary. Combat effects follow the same birth/update/advance division. The
player's bounds refresh before animation advance, including a pose changed by
an enemy hit. Player invulnerability decrements on simulation ticks, and drawing
never changes actor state. The tick remains 83 ms independently of display FPS.

The prototype uses the existing following camera, not the original full camera
controller. Enemy update buckets and knockback view bounds use a 240-wide
simulation view; widening the presentation reveals more world without changing
combat timing. Enemy drawing uses the actual full viewport. Type-22 rectangles
select an active region and clamp ordinary enemy velocity at its twelve-pixel
inset; entering a region can reactivate eligible inactive enemies. Full region
exit resets, shared spawn-pool/UID/save aliasing, camera scripts and transitions
remain unfinished. The seven combined oracle fixtures have no region owner;
they isolate reciprocal queues and combat from camera-controller behavior.

Ordinary enemies have N=false, so their queues require player side-contact
handling but no non-player N-blocker separation. Platforms, gates and other
N-bearing actors are not loaded into these queues yet. Their support/link and
separation branches must be implemented before extending that actor set.

## Validation

Default regeneration runs `LiveCombatCheck.java`. It invokes the **unmodified
original `Game.updateWorldActors` method**, including its actual update queue,
with real sprite/stat data and controlled flat arenas. It compares against the
generated Java and exports ignored `analysis/live-combat-oracle.tsv`. Paint's
bounds/clock/invulnerability operations are driven explicitly without Graphics;
there is no phone framebuffer claim or full boot/menu runtime.

The initial combat pass matched **3,372 snapshots across 783 simulation ticks**:
1,566 player states, 1,566 first-enemy states and 240 second-enemy states. Seven
scenarios cover fighting, enemy contact damage, facing left, lethal player damage,
combo inputs, aerial inputs and two-enemy registry order. They include resulting
health, invulnerability, enemy cooldown, death state and complete movement/
animation/collision/attack/visual snapshots. Original/reference checks total
70,815 comparisons. Independent assertions require an enemy kill, enemy-to-player
damage and completed player death.

Two headless scripts start beside UID 19 on the actual first-level floor:
`--combat-script` attacks, kills and collects one drop; `--defeat-script` starts
with ten health and reaches defeat. Twenty-four host runs verify both at widths
240/569, 30/60/144 FPS and both interpolation settings, comparing final state
across all runs and final pixels per width. The chest/reward host fixture now
also encounters live enemies: its ten-second result includes three hits and
health 30, while still completing the same crystal reward. It no longer assumes
the final position must remain right of the chest after knockback.

Five new data-free assertions cover supported/dormant loading, previous-queue
handoff, inactive-enemy persistence and lethal-health synchronization. The
combined executable now has 154 assertions; four new argument checks bring CTest
to **31 tests**. Existing movement, melee, contact, pickup, chest, crystal,
collection-effect, damage and death oracles remain regressions.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_live_combat.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
python tools/check_native_live_combat.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe
```

## Remaining gameplay

Flag 2048 is a death-removal condition in resetOrRemoveInternal, not by itself
a script pause; the three skipped records are an explicit unsupported subset.
Tile hazards now connect through the live owner; six additional controlled
arenas extend the combined check to 4,448 snapshots across thirteen scenarios.
See NATIVE_HAZARDS.md for health timing, test boundaries and the Windows black
flashing fix. Platforms, other enemy/object types, scripts,
checkpoints/saves, general world reconstruction, original menus/HUD, audio and
full level progression remain pending. The defeat panel restarts this preview;
it does not implement original Continue/Restart persistence. Crystal reward
modals freeze the combat owner alongside the existing prototype cinematic.
No complete game play-through or full runtime equivalence is claimed.

Ordinary-enemy region exit/return reconstruction is now connected; see
[native regions](NATIVE_REGIONS.md) for controlled comparisons and the remaining
mixed-dispatch, UID/pool and transient-pickup boundaries.


Current activation update: all 20 level-0 type-1 records now load. Flag 2048's
final-death removal and trigger 129's two waiting enemies are supported; the
`dormant_enemies` omitted-record count is zero. See NATIVE_ENEMY_ACTIVATION.md
for the 2146-row oracle and actual first-wave fight. Type-2 enemies and the
later chest-unlock triggers remain pending.
