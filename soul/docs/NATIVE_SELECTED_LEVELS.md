# Selected-level data and world ownership

The native gameplay factory now takes its level from the frontend selection or
the reconstructed checkpoint. `LevelDescriptor` supplies the pack, mode,
string-table index, background binding and common sprite/stat roles.
`LevelData` owns the selected maps, placed records, script bytes and decoded
tracks, checkpoint UIDs, required sprites, font, stat tables and platform trig.
No resource-pack views survive the load. `MovementPreview` owns that bundle
before its borrowers and cannot be copied or moved once constructed.

The data loader accepts all nine selections. Normal gameplay and desktop save
acceptance remain gated to level 0: the second-level type-3 AI, existing-type
variants and route checks are still pending. Type-80/type-89 handler foundations are now implemented; see
NATIVE_ENERGY_BARRIERS.md and NATIVE_TIMED_SWITCHES.md. Loading later data and
initializing its player do not construct or verify all of its native actors.
The opening-script runners are explicitly level-0 implementations; decoding
the other tracks does not execute them.

## Original evidence and selected inputs

Original `Game.J(int)` opens `levelPackPaths[index]`, reading records from
resource 0 and scripts from resource 1. `Game.K(int)` selects string table
`index + 2`, loads maps/objects for the same index, and invokes `Game.ah()` to
collect required sprites. Native selection reproduces the placed-type sprite
table, unconditional resources, boss/variant dependencies and level-dependent
additions. Sprite 35 is also retained for the global HUD preload.
`Game.aF` supplies the nine level modes; `Game.bi` supplies background IDs.
These call sites and the record loop were inspected using
`python tools/bytecode.py e` and `python tools/bytecode.py b`.

`LevelSelectionCheck` runs original bytecode and regenerated reference Java in
isolated worlds with the existing ImageIO test adapter. Its **983 rows** cover:

- Nine descriptor/map-dimension rows and nine 53-slot required-sprite masks.
- All 930 placed records, including offsets, counts and the shared short tail.
- Nine checkpoint-UID lists and all 17 decoded scripts with raw track hashes.
- Nine player initialization rows: UID, position, flags and animation.

The native `--selected-level-manifest` matches every row, then loads the nine
bundles again in reverse order and constructs their selected players. Pack 9
retains its 148-by-71 collision grid and 148-by-72 visual dimensions; there is
no padded collision row. The audit found that later player records retain flag
8. Original `Actor.g()` uses sprite bounds for type 0 despite the custom-bound
flag, so the former native constructor rejection was removed. This does not
establish later-level movement/controller equivalence.

## Restart, checkpoints and teardown

Checkpoint snapshots now carry their level, also used by `GameSave` without
changing the version-2 file layout. Player and actor restoration reject a
different level before mutation. The host additionally requires the saved
checkpoint UID to appear in the selected bundle. UID validation uses the
signed-short range independently of the 250 persistence slots: level 1 has
checkpoint UID 376. Version-1 migration and level-0-only save acceptance remain.

Restart derives the next bundle from the checkpoint's level or the live
descriptor. Fresh replay copies progression with the new world's level and
spawn position. The first-level ice-pickup analysis fixture explicitly loads
a second-level chest; that controlled fixture is the only cross-level record
exception and is not a normal gameplay construction path.

`CombatWorld::detach()` is idempotent. It clears the player's support, host
callbacks/container pointer, fire/ice projectile owners, script owners, cached
trigger links, contact bodies and queues before their actor/platform targets.
Platform destruction also detaches externally retained and overwritten ice
backlinks. The host calls detach in its destructor while host containers and
the shared assets are still alive. Dispatch, player update, contact preparation
and animation advance reject a detached world; a load constructs new owners.

Data-free assertions cover cached platform-flag targets, owned/external and
overwritten projectile backlinks, a mounted rider, repeated detach and rejected
post-unload dispatch. They also cover selected descriptors, later player flags
and cross-level checkpoint rejection. The host checker compares default and
explicit level 0 at widths 240/569, exercises repeated checkpoint reconstruction,
rejects unavailable selections and rejects a foreign checkpoint without rewriting
its save. `--movement-level 0` is an explicit preview option; later entries are
still unavailable. Headless output identifies `world_level` and `world_pack`.

## Verification

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
.\soul\port\build.bat Release
python tools/check_native_selected_levels.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_checkpoints.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
python tools/check_native_finish.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

Validation on 2026-10-29: default regeneration compiled with zero errors and
passed ReferenceCheck's 4218 comparisons, the new 983 rows and every registered
Java/oracle check. Debug and Release passed all 104 CTests and 530 player
assertions. Selected-level checks passed against Debug and the packaged Release.
Existing movement, tile surfaces, startup, checkpoint/Restart, save migration,
finish/Continue/replay, intro, chandelier, camera and ice projectile/attachment
regressions passed, including their width-240/569 and FPS matrices. The rebuilt
package contains no game data.

The data-free baseline for this pass was **104 CTests and 530 player assertions**. Oracles,
verification logs, captures and binaries remain in ignored directories. This
foundation does not enable level 1, implement later controllers or replace the
remaining full first-level route test.
