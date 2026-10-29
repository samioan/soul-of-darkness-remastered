# Native crystal rewards and return to gameplay

The first-level prototype now completes ordinary chest/crystal collection.
Open a chest with E/J/Enter, collect the crystal, wait for the reward prompt and
press a game key to return to movement. R resets the prototype. Fragment counts
and meters are held in memory; they are not saved yet.

## Tick state

Contact selects player animation 25 and stops motion. On the next tick the
crystal moves above the player and enters action 104, as in the original pickup
handler. The original beam timing runs for 39 simulation ticks, then enters the
reward screen and increments the matching health/magic fragment count. Ordinary
world objects pause during this flow; player pose 25 advances during the beam
and holds on its ending frame. Screen ticks pause the world, including animation.

`src/crystal_reward.cpp` separates Game.l / Game.G state from drawing. On screen
tick 5 an ordinary health crystal restores ten health, capped at the maximum.
An ordinary magic crystal restores ten only when magic has been unlocked, while
still counting its fragment when locked. The fourth crystal resets its matching
fragment count, increases its maximum by ten up to 120, and refills that meter.
A fourth magic crystal does this even while magic is locked. These distinctions
are preserved from the original code.

Dismissal requires a press edge after screen tick 10. Early edges are consumed;
a held key alone does not dismiss. On dismissal, the selected crystal leaves
its modal state. On the next gameplay tick, action 104 integrates once, clears
motion and enters collection cleanup. Player pose 25 requests idle when its
animation ends outside the cinematic. Normal movement and chest updates resume.

The native screen uses the existing bitmap font, crystal sprites and four slots,
centered at the current logical width. The rectangle beam follows the original
phase formulas and colors. The panel wording and slot outlines are native UI;
the original circular ornament, HUD slot artwork, localized strings and exact
phone pixels are not reproduced. Drawing only presents tick state and does not
award fragments or advance timers.

## Original-bytecode state oracle

Default regeneration compiles and runs `CrystalCheck.java` with the JDK's
internal ASM package explicitly exported to this test. It makes isolated copies
of original and regenerated classes, replaces exactly seven Game drawing entry
points and three BSprite drawing entry points with no-ops, and supplies a no-op
DirectGraphics proxy. The original Game.G reward calculation, Game.f entry,
Game.l completion and beam-counter bytecode remain unchanged. Existing checkers
continue to load their unchanged classes and ImageIO adapter.

This allows reward state to execute without claiming a MIDP graphics runtime.
The oracle's drawWorld hook omits all world-render side effects; beam ticks are
called explicitly. It does not prove full world/cutscene equivalence or compare
framebuffers. The native panel is inspected separately.

The ignored `crystal-oracle.tsv` has **448 state snapshots** covering ordinary
health, fourth health, maximum cap, locked/unlocked magic, fourth magic, early
press and held-key scenarios. Every row compares original/regenerated state,
then `check_native_crystals.py` compares native state to the original rows.

Thirteen data-free assertions bring the player/input/pickup/container/reward
executable to **67 assertions**. The suite retains **23 CTests**. Existing
movement, placed-pickup and chest/spawned-pickup oracles retain their scope.
The twelve headless chest fixtures now complete one reward, retain one health
fragment, dismiss the screen and walk away at widths 240/569, 30/60/144 FPS and
both interpolation settings. Their controlled starting placement is unchanged;
this is not a recording of travel from normal spawn or a level play-through.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_crystals.py --exe soul/port/build/Debug/soul_assets.exe
python tools/check_native_containers.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build\Debug\soul_port.exe --movement-preview --data soul/extracted --width 569
```

## Remaining work

[Shipped collection effects](NATIVE_COLLECTION_EFFECTS.md) now run after reward
dismissal. First-time tutorials, full Game actor
queues/cinematic interactions, NPC hiding, saves and localized reward UI remain
pending. Magic unlock is an explicit state flag; its ability pickup is not
ported. Two script-locked chests remain dormant. Enemy ticks, applying damage, platforms,
object collision/contact integration, damage/death and level completion remain
unported. [Weapon-0 melee](NATIVE_MELEE.md) now adds attack control/presentation.
Next: type-1 enemies and damage/reactions.
