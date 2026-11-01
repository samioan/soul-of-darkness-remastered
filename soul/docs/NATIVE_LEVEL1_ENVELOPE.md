# Second-level loader envelope: sine-wave flyers and beam variants

Constructing the whole second level through the native owners (the `--level-audit-manifest`
audit below) stopped at exactly two loaders: flying enemies and beam emitters. This pass ports
those variants. After it every native loader accepts every shipped level-1 record. That is an
envelope result, not a behavior result: the unverified variants are listed under Boundaries and
normal gameplay still constructs level 0 only.

## Level-1 loader audit

`soul_assets --data DIR --level-audit-manifest` runs the preview's loaders in order for each of
the nine levels and prints the first rejection per loader (`level<TAB>step<TAB>OK|message`), then a
smoke run for levels whose world constructs: the frozen, invulnerable player is parked on every
placed non-trigger record for 40 ticks. A step that throws means a handler left its verified
subset; success only means this sweep stayed inside it. It is a progress map, not an oracle.
Levels 0 and 1 now report OK for every step, and the level-1 sweep drives two of the three
sine flyers into their wave state. Other levels still reject (sprites, gates, prompts, type-13
targets, paired mode and so on) and are the later roadmap items.

## Type 2, behavior 6 (variant index 49): UIDs 181, 136, 137

`Actor.cE[49]` is 12, so `getBehaviorVariant()` is 6 (the roadmap's earlier "behavior 5" label was
wrong). The handler, from `Actor.updateEnemyType2` and its bytecode (`b.aE`):

- State 0: on player/custom-bounds overlap, `changeActionState(138)` (immediate animation 2), then
  `ac=x`, `ad=y`, `aF=facingLeft?2:1` and `flags|=16`. There is no fall-through into 138 that tick.
- States 138/139: timer A decrements; in 138 the interaction rectangle (the same constants as behavior
  0's pursuit, from `bZ={15,10,-15,0}`) enters 139 (immediate animation 4) on player overlap, and 139
  returns to 138 when its animation ends. Velocity X is +/-768 by `aF`, and `y = ad + (30*sin256(2*(ac-x))>>8)`
  every tick. If the actor's collision bounds miss `Game.au`, it clears flag 16 and `resetOrRemove(false)`.
- **`Game.au` is the active camera region, not the player** (set by `Game.a(Actor)` from the type-22 handler;
  the type-40 area trigger already uses the same rectangle). `Actor.aS` (`clampActorToActiveRegion`) runs at the
  end of every eligible basic-enemy update; for behavior 3 it removes its spawner and for 5/6 it resets the actor when its bounds leave the
  region, otherwise it applies the 12-pixel velocity clamp.
- Hurt (state 2) uses animation 8 for every behavior except 0/1, and its end re-captures `ac`/`ad` and
  re-enters 138 for behavior 6. Contact damage and weapon damage follow behavior 0's verified rules.
- `Actor.initializeFromRecord` reloads `ad` (the vertical origin) on every reset, but not `ac`.

Native changes: `EnemyType2` takes the trigonometry table (`loadFlyingEnemies(..., &trig)`), keeps
`originX/originY/direction`, uses hurt animation 8 and implements states 0/2/138/139. `clampToRegion`
holds both clamp forms; the world refreshes `activeRegion` before each flyer update and calls it after.
The type-1/2/3 constructor accepts variant index 49 only for type 2 (`p[12]==-1` still required).

## Beam emitters starting in animation 6: UIDs 3 and 5

Type-10 records 3/5 start in animation 6. `Actor.initializeFromRecord` assigns the direction field `aF`
only for animations 0, 8 and 4, so it stays 0; every use (`aF==8`, `aF==4`, `aF==1`, `aF<8`) then behaves as a
sideways beam whose player-push source is `x-15`. The native constructor accepts animation 6 with direction 0,
which reproduces that. The other four level-1 emitters use animations 0/4/8 and stat rows the loader already
supported; the record count (14) is unchanged.

## Verification

- `SineFlyerCheck.java` runs the unchanged original and regenerated handlers, a real type-22 region actor and
  `Game.au` for eleven scenarios of 120 ticks: right/left/wide-bounds waves, attack, no overlap, a wide region,
  hurt at 3 then lethal damage, pause, hidden and frozen. Effect and loot births, the random stream and every
  actor field (including `ac`, `ad`, `aF`) are compared; independent assertions require the wave, the region
  reset and the attack state. **2663 rows** match the native probe (3979 original/reference expectations).
- `BeamCheck.java` gains six scenarios on the real level-1 emitters and collision grid (setup pack 4); the
  oracle grows from 8832 to **13152 rows**, all matching natively.
- Ten data-free assertions (player suite now 567): trig-table requirement, variant selection, wave entry and
  origin capture, the missing-region guard, the sine expression, region-exit reset by handler and by clamp,
  behavior 0's unchanged clamp and the animation-8 hurt.
- `--sine-flyer-world-manifest` drives a real `CombatWorld` with the shipped player, UID 181 and its camera
  region: wave entry in the first tick, an exactly +/-30 pixel amplitude, reset to the spawn after leaving the region
  (tick 80) and dormancy without overlap.

```
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_sine_flyers.py --exe soul/port/build/Debug/soul_assets.exe
python tools/check_native_beams.py --exe soul/port/build/Debug/soul_assets.exe
.\soul\port\build_dist.bat
```

## Boundaries

- Constructed-but-unverified level-1 variants remain: the type-50 records other than chandeliers 89-91 (UIDs
  25/33/145/190/169/510), the type-33 platforms (UIDs 219/241/288), prompts with other modes, gates, bridges,
  the camera controller (UID 125), the intro camera trigger 134/script 133 (not loaded at all outside level 0),
  and the level-1 passage links. Each needs its own original comparison before being called supported.
- Flyers 136/137 share the verified handler but are not world-fixtured; the sweep reaches two of three flyers.
- Behavior-6 interplay with a camera controller or multiple overlapping regions is not exercised.
- Controlled fixtures only; no level route and no change to the frontend gating.
