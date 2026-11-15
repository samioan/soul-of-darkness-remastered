#!/usr/bin/env python3
"""Generate native engine sources from the reference Java.

    python tools/gen_port.py                  # full 1.0.1 build -> soul/port/gen_full   (the shipped game)
    python tools/gen_port.py --game v137      # cut-down 1.3.7 build -> soul/port/gen
    python tools/gen_port.py [--game G] --vanilla   # unpatched translation -> soul/analysis/gen_<G>_vanilla

Steps: copy the game's src/*.java to a scratch directory, apply its asserting port patches (every
entry must match its stated number of times), add its port Java, compile-check against the MIDP
stubs, then translate Game/Actor/BSprite/ErrorReporter (+ port classes) to C++17 with
tools/j2cpp/J2Cpp.java.

The reference trees (soul/src, soul_full/src) are never modified. The gen directories are
generated; do not edit them.
"""
import importlib
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from decompile import find_java  # noqa: E402

ROOT = Path(__file__).resolve().parent.parent
STUBS = ROOT / "tools" / "midp-stubs"
EMIT = "ErrorReporter,BSprite,Actor,Game"
EXPORTS = [f"--add-exports=jdk.compiler/com.sun.tools.javac.{p}=ALL-UNNAMED" for p in ("api", "code", "tree", "util")]
# game key -> (reference source, patch module, port Java directory, generated output)
GAMES = {
    "full": (ROOT / "soul_full" / "src", "port_patches_full", ROOT / "tools" / "port_java_full", ROOT / "soul" / "port" / "gen_full"),
    "v137": (ROOT / "soul" / "src", "port_patches", ROOT / "tools" / "port_java", ROOT / "soul" / "port" / "gen"),
}
# Compile-time stand-ins for classes implemented natively; never translated.
NATIVE_STUBS = {"PortNative"}


def run(cmd, **kw):
    return subprocess.run([str(c) for c in cmd], check=True, **kw)


def main() -> None:
    vanilla = "--vanilla" in sys.argv
    game = sys.argv[sys.argv.index("--game") + 1] if "--game" in sys.argv else "full"
    if game not in GAMES:
        sys.exit(f"unknown game {game!r}; choose from {', '.join(GAMES)}")
    src, patch_module, port_java, out = GAMES[game]
    if vanilla:
        out = ROOT / "soul" / "analysis" / f"gen_{game}_vanilla"
    java = Path(find_java())
    javac = java.with_name("javac.exe" if java.suffix == ".exe" else "javac")
    sep = ";" if sys.platform == "win32" else ":"
    classpath = sep.join(str(j) for j in sorted(STUBS.glob("*.jar")))
    with tempfile.TemporaryDirectory(prefix="sod_port_") as tmp:
        tmp = Path(tmp)
        work, tool = tmp / "src", tmp / "tool"
        work.mkdir()
        tool.mkdir()
        texts = {p.name: p.read_text(encoding="utf-8") for p in sorted(src.glob("*.java"))}
        extra = []
        if not vanilla:
            patches = importlib.import_module(patch_module).PORT_PATCHES
            for fname, old, new, count in patches:
                found = texts[fname].count(old)
                if found != count:
                    sys.exit(f"port patch expected {count} match(es), found {found} in {fname}: {old[:70]!r}")
                texts[fname] = texts[fname].replace(old, new)
            for path in sorted(port_java.glob("*.java")):
                texts[path.name] = path.read_text(encoding="utf-8")
                if path.stem not in NATIVE_STUBS:
                    extra.append(path.stem)
        for name, text in texts.items():
            (work / name).write_text(text, encoding="utf-8", newline="\n")
        run([javac, *EXPORTS, "-nowarn", "-d", tool, ROOT / "tools/j2cpp/J2Cpp.java"])
        emit = ",".join([*extra, EMIT])
        out.mkdir(parents=True, exist_ok=True)
        staging = tmp / "gen"
        run([java, *EXPORTS, "-cp", tool, "J2Cpp", classpath, staging, emit, *sorted(work.glob("*.java"))])
        for old in list(out.glob("*.cpp")) + list(out.glob("*.h")):
            old.unlink()
        for f in sorted(staging.iterdir()):
            shutil.copyfile(f, out / f.name)
        print(f"wrote {len(list(staging.iterdir()))} files -> {out.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
