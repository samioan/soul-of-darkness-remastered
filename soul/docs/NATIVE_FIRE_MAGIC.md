# Native fire casting and MP recovery

After chest 57 unlocks magic, K, keyboard 0 or numpad 0 casts the fire ability.
The original phone input is keypad 0 (mask 1). Casting costs 15 MP, requires
ground or a supported platform, excludes crouching and waits for the first fire
slot to clear before another cast. Physical fire/attack input retains priority
if both edges occur together. E/J/Enter continues to attack/open chests.
The later ice pass now supplies weapon switching and the separate ice pickup/
physical weapon; ice magic/projectiles remain pending. See NATIVE_ICE_WEAPONS.md.

## Original behavior and native ownership

`Actor.updatePlayer` requests pose 18 and resets `Game.H` to zero on an accepted
magic edge. `Game.drawWorld` calls `Game.f(3,x,y,80)` while pose 18 is current.
Four calls advance the charge to 80/160/240/320; the next spends 15 MP, resets
the recovery timer, enters pose 16 immediately and spawns six fire effects at
rank zero (nine at ranks one/two). Their type/sprite/animation are 39/1/16;
flags include 4096 and forced update 16, and they inherit the player's facing.
They are born at the player's X/Y-22 with a 600 ms timer, zero fixed coordinates
and separate slot indices. The native game currently uses the initial rank.

The charge drawing code consumes 20 calls to `Game.randomRange` per charge
step, including the original zero/negative upper bounds at its final steps.
The native controller preserves these random draws on simulation ticks. Its
charge ring is a desktop presentation, not an original framebuffer claim.
Drawing the ring or sprites never advances the charge or random stream.

`Actor.updateEffect` integrates and refreshes bounds before its radial handler.
The normal fire pulse places itself at radius 30/angle 10 around the player
using the original integer trig table, queues animation 17 and tests hits with
the pre-movement bounds. Its pending animation applies during the tick's
animation advance. Missing collision rectangles fall back to visual bounds.
Explosion hits continue until animation ending; removal releases the fire slot.
Pause freezes both handlers and animation clocks. Region transitions clear
fire transients and release their slots.

`Actor.applyWeaponEffectHits(0)` damages supported ordinary ground/flying
enemies with 15/20/25 raw damage by rank, using their existing reaction,
cooldown, blood/damage-number/death, loot and removal ownership. It rejects
point rectangles, hidden/frozen actors and dead/fading actions. Physical
melee damage remains separate. The first-level bridge overlap branch also
feeds the existing bridge activation/grid owner; its fire-specific path is
source-based integration, outside the new oracle's enemy arenas.

`Actor.regenerateMagic` sets locked MP to zero. After unlock, it accumulates
83 ms per gameplay tick; every 5000 ms it restores 10 MP, capped to the maximum,
for player actions 0/2. The clock continues while MP is full and is reset when
spending succeeds or a lower meter receives a refill. Normal startup now shows
zero usable MP before the ability is acquired. The desktop HUD shows the fire
binding after unlock and a brief LOW MP message after a rejected cast.

`FireMagic`/`FirePulse` borrow the effect sprite and copy the original trig table.
`CombatWorld` owns them, updates existing pulses after placed actors and handles
their damage in frozen target order. The host charges before the tick's player
and effect animation advance, preserving birth timing. Fire effects draw with
foreground effects after the player. Original `b`/`e` bytecode confirms the
selected branches; no Java renames or generated-source edits were needed.

## Verification

Default regeneration runs `FireMagicCheck.java` after the existing ASM drawing
adapters. Only named drawing methods are replaced with no-ops: the charge,
random, player, world, damage and MP bytecode remains original. A DirectGraphics
proxy accepts color changes. This is an analysis adapter, not a MIDP runtime.

Fourteen 120-tick arenas cover standing/left casting, held input, blocked and
later recasts, insufficient/locked MP, crouching, airborne input, pause, recovery,
ground/flying enemy hits and all three fire ranks. They provide 3380 original/
reference expectations and 4790 native-comparable rows: 3360 player/MP/charge
snapshots, 936 pulse/bounds snapshots, 480 reciprocal enemy snapshots and 14
final random-stream observations. The checker does not claim all magic targets,
ice projectiles, thrown effects, boss interactions or full-world equivalence.

```powershell
python tools/check_native_fire_magic.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

The checker also verifies seven control/MP milestones and 36 real host runs at
widths 240/569, 30/60/144 FPS and both interpolation settings. The headless
`--fire-script` / `--game-fire-script` fixtures place an invulnerable player by
UID 19 with magic unlocked. Normal magic key input produces five successful
casts, a real fire defeat without melee, an insufficient-MP rejection and MP
recovery. `--game-arena-fire-script` instead obtains magic through both actual
arena waves, chest 57, its tutorial and gate, then casts once and recovers MP.
It does not force enemy/chest removal, the unlock, gate activation or casting.
Both game fixtures pass through startup and the normal pause/resume boundary.

Seventeen data-free assertions bring player checks to 381; three CLI guards
bring CTest to 69 tests. Next: the separate ice-weapon pickup, melee/projectile
controller and switching controls, plus remaining first-level progression and
checkpoint/save integration. The complete game remains unfinished.

Validation on 2026-10-28: full default Java regeneration passed. Debug and
Release passed all 69 CTests, the fire checker and existing arena, chest,
ability, world-order, combat, region, platform, gate/script, passage and frontend
regressions. Native game captures at widths 240/569 show the charge ring,
foreground radial explosions, MP cost and English binding hint. The rebuilt
data-free package matches Release and passes all 4790 rows and 36 host runs.

The following ice-owner pass now supplies the second-level reward, ice melee
and Q/9 switching. NATIVE_ICE_PROJECTILES.md now covers ice casting, enemy hits,
temporary wall footholds and rider detachment; linked object attachments remain open.
