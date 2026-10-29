# Native weapon-0 damage and hit response

The native library now has verified weapon-0 damage rolls, player health scaling
and invulnerability, type-1 enemy damage reception and hurt recovery, player
knockback/death poses, and blood/death/damage-number effects. These are controlled
combat entries used by `soul_assets --combat-manifest`. The following
[live combat pass](NATIVE_LIVE_COMBAT.md) now connects ordinary
enemies, reciprocal hits, loot/effects and native defeat handling to the preview.

## Damage rules

`JavaRandom` preserves java.util.Random's 48-bit stream. Game.randomRange takes
the signed absolute value of nextInt and uses a signed remainder with an
exclusive upper bound. Reversed bounds are retained. Equal bounds consume no
random state. The MIN_VALUE absolute-value overflow is retained too: external
seed 247775869193412 produces MIN_VALUE first, so randomRange(9,12) returns 7.
The port handles the MIN_VALUE/-1 remainder explicitly to avoid C++ undefined
behavior. Original bytecode for both Actor and Game was inspected.

`Weapon0::roll` multiplies damage rank values 10/13/15/20 by randomRange(9,12),
then divides by ten. Type-1's weapon damage percentage is 100. `applyDamage`
emits a floating number containing that amount, then reduces actual damage by
10 percentage points when damage rank is 3 and another 15 when secondary rank
is 4. Integer truncation occurs after multiplication. For example, the number
can show 10 while health decreases by 7. Negative damage/health arithmetic uses
Java's signed wrapping behavior; lethal enemy health is not clamped to zero.
Weapon 1, type-14 resistance and spell-hit lists remain outside this slice.

The enemy entry queues effects in original order: sprite 1 animation 29 at
Y-50, blood animation 2 at Y-randomRange(10,50), then death animation 3 at Y-20
when lethal. Priorities are 302. Number/blood orientation derives from facing
before hurt entry; the ordinary type-1 death burst faces right. A surviving hit
with reaction force-restarts animation 3, faces the player, stops motion and
knocks away by 256 fixed units outside the one-pixel dead zone. Action 2 counts
down its timer and returns to pursuit at animation ending. Surviving hits set
200 ms cooldown. `receiveWeapon` rejects death state, positive cooldown and
flags 1024/16384 before querying the player's attack rectangle. Rejected hits
consume no random state. As in original `ab()`, this receiver checks bounds
without adding a new player-animation predicate.

`PlayerDamage::apply` rejects ordinary damage during invulnerability; damage
500+ bypasses that scalar gate. Accepted damage sets 600 ms invulnerability,
or 1200 with the shield flag. Damage scales by
`(((maximumHealth-60)<<7)/60 + 256)` before shifting the product right by eight.
Health clamps to zero. `tick()` decrements positive invulnerability by 83 once
per unpaused simulation tick, preserving the original negative remainder. The
original decremented it during paint; native drawing never changes that timer.

`Player::receiveHit` covers ordinary type-1 hits in player modes 0/2, with no
cinematic, beam exception or shield attachment. Existing hurt pose 8 and death
pose 9 reject hits before scalar damage, even a 500-damage hit. Accepted damage
enters pose 8 immediately, emits blood, faces the source and sets knockback
3072 away from it; a wall behind stops velocity. Delayed horizontal deceleration
is 1024. Pose 8 preserves falling, recovery and view-bound clamping.
`damageView` supplies the logical view bounds; the world must update them with
its camera/viewport. The fixture uses the original 240-wide view.

Lethal player hits request pose 9 on the next update. It quarters horizontal
velocity, stops vertical movement, freezes at animation ending and raises
`deathComplete`. The native comparison maps that event to original state 12;
it does not execute a native death menu or restart. The world must synchronize
health changes, hide the slash after accepted hits, consume effects and handle
that transition. Hazard-triggered damage/death remains at the movement preview's
existing explicit freeze boundary.

## Combat effects

`CombatEffect` covers type-39 sprite-1 animations 2, 3 and 29 in level mode 0.
Blood and death bursts disappear at animation ending. Damage numbers start
with timer 1000, decrement it twice by 83 per update, and set velocity -1024
after integration. They remain through timer 4 on the sixth update and disappear
at -162 on the seventh. Their birth fixed coordinates are zero, as in the
original spawn helper. Pause and animation-freeze behavior are checked.

Rendering is pure. Blood/death use the uncensored palette; damage numbers use
the existing BitmapFont at palette 0, including the original five-pixel digit
spacing. Amounts 100+ skip number drawing as the original does. A live number
requires the caller's font; the effect borrows immutable sprite data. The
checker verifies state/lifetime, not original phone Graphics pixels. Censorship
settings, sounds and full world effect ownership remain pending.

## Verification

Default regeneration runs CombatCheck against unmodified original bytecode
and generated Java with the existing ImageIO test adapter. No combat methods
or drawing hooks are rewritten. It exports ignored `analysis/combat-oracle.tsv`.
The native checker compares every one of its **2,591 rows**:

- 256 weapon rolls and 180 random ranges, including the MIN_VALUE seed;
- 324 player-health/invulnerability combinations;
- 96 direct enemy damage fixtures and 48 weapon-hit guard fixtures;
- 360 enemy hurt/recovery snapshots over 180 ticks;
- 622 player hit/death snapshots over 311 ticks, covering both directions,
  airborne hits, a wall and repeat-hit rejection;
- 286 combat-effect states across 18 normal/paused/frozen fixtures;
- 227 effect spawn events and 144 subsequent random-stream values.

Original/reference checks total 35,318 comparisons. The stationary player in
enemy hurt fixtures uses a synthetic rectangle above the damaging area; those
fixtures do not run reciprocal live combat. Player hit fixtures invoke the
ordinary hit entry explicitly, with an empty body queue. Independent assertions
require enemy hurt and recovery states. Existing movement, melee and contact
oracles remain separate regressions.

Twenty-five new data-free assertions bring the combined native test executable
to 133 checks. CTest remains 27 tests in Debug and Release.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_combat.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
python tools/check_native_combat.py --exe soul/port/build/Release/soul_assets.exe
```

## Next integration

The following [death/loot pass](NATIVE_DEATHS.md) now handles ordinary action 3,
loot selection, delayed pickups, owned-record reset and inactive/respawn fades.
General shared spawn-pool ownership remains pending. Next, wire original queue
order, player hit dispatch, slash hiding, invulnerability ticking, effects and
player-death transition handling into the preview. Verify live combat at widths
240/569 and independent presentation rates. Complete gameplay, platforms,
scripts, saves, menus/HUD and audio are still unfinished.
