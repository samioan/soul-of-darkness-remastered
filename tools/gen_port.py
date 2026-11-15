#!/usr/bin/env python3
"""Generate native engine sources from the reference Java.

    python tools/gen_port.py              # soul/src -> soul/port/gen (the shipped game)
    python tools/gen_port.py --vanilla    # unpatched translation -> soul/analysis/gen_vanilla

Steps: copy soul/src/*.java to a scratch directory, apply the asserting port patches in
tools/port_patches.py (every entry must match its stated number of times), add tools/port_java,
compile-check against the MIDP stubs, then translate Game/Actor/BSprite/ErrorReporter (+ port
classes) to C++17 with tools/j2cpp/J2Cpp.java.

The reference tree soul/src is never modified. soul/port/gen is generated; do not edit it.
"""
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from decompile import find_java  # noqa: E402
from port_patches import PORT_PATCHES  # noqa: E402

ROOT = Path(__file__).resolve().parent.parent
STUBS = ROOT / "tools" / "midp-stubs"
EMIT = "ErrorReporter,BSprite,Actor,Game"
EXPORTS = [f"--add-exports=jdk.compiler/com.sun.tools.javac.{p}=ALL-UNNAMED" for p in ("api", "code", "tree", "util")]
SRC = ROOT / "soul" / "src"
PORT_JAVA = ROOT / "tools" / "port_java"
OUT = ROOT / "soul" / "port" / "gen"
# Compile-time stand-ins for classes implemented natively; never translated.
NATIVE_STUBS = {"PortNative"}


def run(cmd, **kw):
    return subprocess.run([str(c) for c in cmd], check=True, **kw)


def main() -> None:
    vanilla = "--vanilla" in sys.argv
    src, port_java, out = SRC, PORT_JAVA, OUT
    if vanilla:
        out = ROOT / "soul" / "analysis" / "gen_vanilla"
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
            for fname, old, new, count in PORT_PATCHES:
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
