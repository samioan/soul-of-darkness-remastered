# Soul of Darkness decompilation and Windows port

Read README.md and soul/docs/ROADMAP.md first. STATE_MACHINE.md and INPUT.md
in soul/docs document the completed core Game pass. BSPRITE.md documents the
completed sprite pass and the original-Java pixel/bounds oracle.
ACTOR.md documents the controlled movement/collision/player checks and remaining
core boundaries. ENEMIES_AND_OBJECTS.md documents handler roles, stat tables,
object envelopes and selected AI arenas.
SCRIPTS_AND_LEVELS.md documents the timed script interpreter, tile maps,
post-increment correction and pack 9 collision/visual dimension mismatch.
Reference approaches:
../tes-travels-decomp, ../gow-betrayal-decomp, ../rac-j2me-decomp.

- After completing and verifying a roadmap task, check it off in both
  soul/docs/ROADMAP.md and soul/docs/PORT_ROADMAP.md and keep their current
  status/remaining lists accurate. Do not append contradictory pending summaries.
- Keep JARs, extracted/decoded assets, screenshots and tool binaries out of git.
- soul/decompiled is raw Vineflower output. soul/src is generated reference Java.
  Change soul/docs/names.map or asserting PATCHES in tools/rename_sod.py, then
  regenerate. Do not edit generated trees directly.
- Default regeneration must pass compilation and ReferenceCheck's comparisons
  with original bytecode. Those checks cover selected static/pure paths only;
  they do not establish full runtime equivalence.
- SpriteCheck, ActorCheck, BehaviorCheck and ScriptCheck use a test ImageIO adapter; do not confuse that with a
  MIDP runtime. Recheck Python pixels/bounds against its regenerated oracle
  after sprite parser/decoder changes. Keep previews/oracles in ignored analysis/.
- Rename only after inspecting call sites; document evidence in CLASS_MAP.md.
- Check decompiler artifacts against original bytecode with tools/bytecode.py.
  A compile pass alone does not prove runtime equivalence (watch byte counters).
- The original is Gameloft, not the Metaflow RP engine of the sibling projects.
  Reuse infrastructure, verify all game-specific loaders independently.
- Port simulation uses the original 83 ms tick. Presentation FPS is independent.
  Interpolate render state only; collision, animation events and saves stay on ticks.
- Widescreen must reveal more world; update camera/culling/buffers/HUD explicitly.
  Verify both 240 and 569 logical widths. Do not globally replace 240 constants.
- The executable has native startup and a first-level gameplay preview; the full game is unfinished.
  Keep status statements accurate. Do not add publishing/updating to a guessed repo.

Commands: .\setup.ps1; python tools/rename_sod.py;
python tools/inspect_assets.py; .\soul\port\build.bat;
.\soul\port\build_dist.bat. CI builds without any game data.
