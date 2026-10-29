# Native pickup collection effects

The movement preview now consumes type-35 collection events and owns the
corresponding type-39 actors, using sprite `2:1`, animations 11/12 and paint
priority 308. Health and health crystals select 11; magic and magic crystals
select 12. Events occur on ordinary contact, dropped-item expiry and the first
gameplay update after the crystal reward closes. Expiry still awards no meter
increase. Spawn position is the source's integrated pixel position; facing
comes from its current facing state, including the spawned pickup's cleared
facing state. New effects receive the birth tick's animation advance, then
begin handler updates on the following tick.

Collected pickups now stop drawing in action 100/101, matching the original
early return in `Actor.paint`. Their delayed meter award and removal clocks
continue independently of presentation FPS.

## Original evidence and shipped-asset quirk

`Actor.ad()` (`b.ad()`) changes the source action to 100, then calls
`spawnActor(39, 1, 11 + min(animationId >> 1, 2), 0, 0, 308, false)`.
The supported pickup animations are 0..3, selecting effects 11/12.
`createActor` leaves fixed coordinates at zero until the first common update,
copies source position/facing and sets UID -1. `updateEffect` (`b.bG()`)
subtracts the 83 ms tick from timer `aw`, refreshes bounds and removes these
animations when `isAnimationEnding` is true. These calls were checked against
`python tools/bytecode.py b`.

The original paint path also calculates `animationFrame * 30 / frameCount`
and calls drawArc in green/blue. **Both animations in this JAR have one frame,
duration one**, referencing sprite frame 0. Their diameter is therefore always
zero, and they are removed on their first handler update. The native port
preserves this short lifetime and sprite pose; it does not invent an expanding
ring. Alternate assets with multiple frames are explicitly unsupported.
No phone framebuffer or device-specific zero-size drawArc equivalence is claimed.

## Validation

BehaviorCheck now emits ignored `soul/analysis/effect-oracle.tsv` during default
regeneration. It invokes the original and regenerated collection helper, then
compares actor/world state through effect removal. Twenty-four scenarios cover
all four source animations, both facing directions, normal updates, five paused
ticks in state 32 and frozen animation flags. The shipped one-frame effects
still end when their animation clock is frozen: the ending predicate already
holds. The oracle contains **88 snapshots** including birth. Ring diameter is
evaluated from the original animation count without invoking mutable paint.

`check_native_effects.py` compares every snapshot with `--effect-manifest`.
Twelve new synthetic assertions cover collection events, hidden source drawing,
expired magic drops, deferred crystal cleanup, pause, removal and pure effect
drawing. The combined executable has **79 assertions**; the suite retains
**23 CTests** per configuration. Twelve chest host runs now also require one
collection effect and no surviving effect after reward dismissal and walking
away, at widths 240/569, 30/60/144 FPS and both interpolation settings.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_effects.py --exe soul/port/build/Debug/soul_assets.exe
python tools/check_native_containers.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
```

The preview uses a small owned effect list. This does not implement the original
global activation queues, registry/pool reuse, enemy attraction or other type-39
handlers. Tutorial overlays, saves, damage, enemies and combat remain pending.
[Side-contact helpers](NATIVE_CONTACTS.md) now have controlled native coverage.
Next: the first native type-1 enemy and weapon-0 melee/damage slice.
