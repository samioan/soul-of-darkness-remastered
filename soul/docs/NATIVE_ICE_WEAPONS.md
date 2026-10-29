# Ice pickup, physical attacks and weapon switching

The native reward owner now supports the ice blade, and Q, keyboard 9 or numpad
9 switches between owned fire/ice weapons. Ice has separate standing, combo
and aerial strikes, with normal ordinary-enemy damage and death/loot handling.
The original ice chest is in level 2 (pack 4), outside the current normal
first-level slice. No ice pickup was added to ordinary first-level placements.
The relocated chest fixture below makes this owner testable before the later
level/loading progression is implemented.

The following pass in NATIVE_ICE_PROJECTILES.md now adds ice magic and temporary
wall footholds. K/0 casts the equipped weapon's spell after magic is unlocked.
The complete game remains unfinished.

## Original evidence and native changes

Pack 4's type-52 UID 59 at (721,460), flags 2217, reward -5 emits type 48 with
sprite **18**, animation **35**. This differs from magic reward -2's sprite 27 /
animation 2. `Actor.updateContainer` substitutes one randomized health orb and
changes the reward to zero when ice is already owned. The same branch handles
the previously ported repeated magic reward.

`Actor.updateAbilityPickup` animation 35 sets `bs` (ice ownership), schedules
tutorial 53 and enters the existing pickup pose/attraction/beam/removal path.
It does not change the active weapon, unlock magic or refill MP. Repeated ice
collection skips its tutorial. `AbilityPickup` now accepts exactly animations
2/35, borrows the correct sprite, and retains the original common clocks/bounds.
The host renders the ice tutorial in English with the Q/9 binding.

`Actor.updatePlayer` gives physical attack first priority, then magic, then
keypad 9 / mask 512 for switching. `Actor.switchWeapon` rejects unowned ice,
allows returning to fire, starts a 450 ms cooldown, and clears queued combos.
Cooldown decrements by 83 ms and may end at -48; holding the switch key does
not repeat. Switching is allowed in the original attack-control window, including
airborne movement, and is blocked during the verified melee/recovery windows.
The desktop HUD displays the equipped blade and switch binding after ownership.
Original type-21 switch icon births are represented by that desktop UI rather
than a general native type-21 actor owner.

Ice physical attack poses are 19/20/21. The first ground strike can queue 20;
air strikes stop movement and suppress steering until their ending. Crouched
attacks require standing headroom, and physical input retains priority over a
simultaneous switch edge. `Player` now handles both weapon pose families and
queries the equipped weapon's attack rectangles. The type-28 attached slash
continues to activate only for the fire weapon, as in the original. Existing
ordinary-enemy damage helpers match the initial ice/fire damage tables for
supported types 1/2; upgraded weapon stats and other targets remain separate.

Original `b`/`e` bytecode, constructor/reward call sites and pack 4 records confirm
these branches. No Java names, generated-source edits or decompiler patches
changed. The ice unlock/equipped state currently belongs to the live session;
native save/Continue restoration remains unfinished.

## Verification

Default regeneration runs three original/reference checks with the analysis
ImageIO adapter:

- `IcePickupCheck`: 1502 expectations and 1498 native rows from two chest/reward
  arenas and ten pickup arenas. They cover first/repeated ice rewards, inherited
  facing, attraction, contact, MP preservation, level-independent ownership,
  pause, hidden/frozen state and explicit interaction completion.
- `IceWeaponCheck`: 2887 expectations and 2880 native rows over eighteen 80-tick
  cases: standing/combo/late/held attacks, movement, crouch/ceiling, air/fall,
  pause, facing, switch/cooldown/held/locked/return-to-fire, air switching and
  physical-input priority. Rows include player/weapon bounds, combo/recovery,
  switch cooldown and independent weapon-0/1 hit-query masks.
- `IceWorldCheck`: 1729 expectations and 1728 native rows from actual world
  dispatch, reciprocal ice damage and mixed flying/ground contact queues.

Totals: 6118 original/reference expectations and 6106 native comparisons. These
selected arenas do not establish complete world, level-2 or weapon equivalence.

```powershell
python tools/check_native_ice_weapons.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

The checker also verifies seven milestones and 24 actual host chains across
widths 240/569, 30/60/144 FPS and interpolation on/off. The headless
`--ice-script` / `--game-ice-script` fixtures clone the original second-level
ice chest into the first-level test arena at (340,262), give it isolated test
UID 1000, explicitly unlock the
fixture chest, and place an invulnerable player there with magic already owned
and MP=30. Normal input opens it; original native reward/collection handlers
grant ice without refilling MP. The fixture dismisses the tutorial, equips ice,
approaches UID 19, defeats it with ice strikes and returns to fire. It never
forces ice ownership, enemy health/removal or the normal arena/gate chain.
The game fixture traverses startup and pause/resume. Full level-2 play is not
claimed by this relocated setup.

Sixteen new data-free assertions bring player checks to 397; three CLI guards
bring CTest to 72 tests.

## Following projectile pass

NATIVE_ICE_PROJECTILES.md now ports ice magic's charge/spending/birth and
type-39 flight, ordinary enemy impact, wall/slope collision, temporary footholds,
both `aR` slots and replacement/expiry/rider detachment. Type-39 animation-9
support is verified against original bytecode. NATIVE_ICE_ATTACHMENTS.md now
covers the gated type-33 link branch and the shipped shot's point-rectangle
rejection. Other gate/trap targets need their own native controllers. Level
completion/loading and checkpoint/save integration remain open. Full gameplay
is unfinished.

Validation on 2026-10-28: full default Java regeneration passed. Debug and
Release passed all 72 CTests, the ice checker and existing physical/fire combat,
movement, arena, chest/ability, world-order, region, platform, gate/script,
passage and frontend regressions. Native game captures at widths 240/569 show
the English ice tutorial, equipped-blade hint and ice strike artwork. The
rebuilt data-free package matches Release and passes all 6106 comparison rows
and 24 relocated-chest gameplay fixtures.
