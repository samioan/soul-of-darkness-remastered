# Tooling

The decompilation helpers, Java 17 discovery and symbol-table renamer are adapted from
`../gow-betrayal-decomp/tools`, itself following the TES/Ratchet workflow.
`soul/port/vcvars.bat` is the same generic VS discovery used there. All scripts resolve project
paths relative to their own location.

| Command | Result |
|---|---|
| `python tools/bootstrap.py` | Verify/provision pinned tools, locate Java 17+ |
| `python tools/build_reference.py [--jar PATH]` | Extract (SHA-256 checked), decompile to `soul/decompiled`, rename to `soul/src`, apply asserting decompiler fixes, compile-check |
| `python tools/gen_port.py [--vanilla]` | Apply `port_patches.py` + `port_java`, translate to `soul/port/gen` (or the unpatched translation to ignored `soul/analysis/gen_vanilla`) |
| `python tools/check_engine.py` | Original class files vs. the built engine, tick by tick |
| `python tools/check_narrowing.py` | Narrowing-cast counts and call targets of `soul/src` vs. the original class files |
| `python tools/outline.py Game` | Member line ranges; `--big N` filters large methods; `--calls NAME` approximates callers/callees |
| `python tools/bytecode.py e` | Original instructions in ignored `soul/analysis/e.bytecode.txt` |
| `python tools/make_banner.py soul/port/launcher/assets/banner_source.jpg` | The launcher's embedded banner (`banner.jpg`), via System.Drawing; from shadowkey-decomp |
| `python tools/make_icon.py soul/port/launcher/assets/banner_source.jpg` | The launcher's `.ico` (the hero's face, 16-256 px); from shadowkey-decomp |

`bootstrap.py` contains SHA256 pins for all tool JARs. Existing matching copies are used first,
then copies from `gow-betrayal-decomp`, `rac-j2me-decomp` or `tes-travels-decomp` beside this
project. Vineflower alone also has a pinned download fallback:
[Vineflower 1.12.0](https://github.com/Vineflower/vineflower/releases/tag/1.12.0).
Vineflower is third-party software under Apache-2.0; its JAR is ignored.

API stub JARs (`midpapi20.jar`, `cldcapi11.jar`, `nokiaui.jar`) are copied from
`../rac-j2me-decomp/tools/midp-stubs/` here. These are compilation API definitions, not a
desktop runtime/emulator. The reference projects do not record a reliable download source for
these exact binaries; this project does not invent one. On a machine without those siblings,
provide the matching JARs in `tools/midp-stubs/` before running setup; pins enforce the same
environment. The native build and CI need no Java, API stubs, Python or game data.

## Reference Java

Renames go in `soul/docs/names.map`, using original class/member names and Java-style parameter
types. `renamer/ChRenamer.java` operates on bytecode symbols through Vineflower's renamer hook, so
every reference is rewritten consistently. Decompiler artifacts are fixed in
`build_reference.py` as asserting entries (`PATCHES`, `WIDEN`, `OVERLOADS`), each with its
bytecode evidence; never edit `soul/src` or `soul/decompiled` by hand.

`check_narrowing.py` recompiles `soul/src` and compares each method's narrowing-cast counts and
call descriptors with the original class files. It finds two decompiler artifact classes that
compile cleanly but change behavior: int locals declared as byte/short, and calls that Java
overload resolution sends to a different method than the bytecode did. Its remaining findings
are listed as harmless in `soul/docs/BUILD_101.md`.

## Translated engine (the playable game)

`gen_port.py` copies `soul/src`, applies the asserting desktop patches in `port_patches.py`,
adds `port_java/*.java`, compile-checks the result and translates it to C++17 with
`j2cpp/J2Cpp.java` (javac attributed trees; needs the JDK 17+ that the rest of the pipeline
already uses). Output: `soul/port/gen/`, tracked, never edited by hand.

`check_engine.py` runs the original class files (`reference_checks/EngineOracle.java` with the
no-draw API in `reference_checks/engine_adapter/`) against
`soul/port/build/Release/soul_engine_check.exe` with identical seeds and scripted input,
comparing a fingerprint of all Game/Actor/BSprite statics and all live actor fields after every
tick. `--ticks`, `--seeds`, `--level`, `--save`, `--script` and `--exe` select longer or
different runs; traces go to ignored `soul/analysis/engine-*.txt`.

`soul_engine_check.exe` is the headless driver: `--data DIR --ticks N --seed S --script FILE
--width W --shot TICK FILE --mid-shot TICK FILE --verify-replay --describe --save-in FILE
--save-out FILE`. `--trace-throws N` prints a symbolized stack for the first N Java exceptions
the runtime raises (needs a build with debug information). See `soul/docs/NATIVE_ENGINE.md`.
