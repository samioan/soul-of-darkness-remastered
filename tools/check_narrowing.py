#!/usr/bin/env python3
"""Find decompiler type-narrowing artifacts in a reference tree.

Vineflower sometimes declares an int local as byte/short/char/boolean when the bytecode only ever
stores small constants into it. Java source then narrows silently in compound assignments
(`b <<= 1`, `b += x`), which the original code never did. Such methods recompile to bytecode with
MORE i2b/i2s/i2c instructions than the original.

This recompiles <game>/src and compares, per method, the number of each narrowing instruction with
the original class files (methods are matched through docs/names.map).

    python tools/check_narrowing.py [soul_full|soul]

Exit status 1 if any method differs. A reported method needs an asserting PATCH (tools/build_full.py
or tools/rename_sod.py) that restores the int local, with this tool's output as the evidence.
"""
import re
import subprocess
import sys
import tempfile
from collections import Counter
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from decompile import find_java  # noqa: E402

ROOT = Path(__file__).resolve().parent.parent
STUBS = ROOT / "tools" / "midp-stubs"
OPS = ("i2b", "i2s", "i2c")


def methods(javap: Path, classpath: Path, cls: str):
    out = subprocess.run([str(javap), "-p", "-c", "-s", "-cp", str(classpath), cls], capture_output=True, text=True, check=True).stdout
    result, name, desc = {}, None, None
    pending = None
    for line in out.splitlines():
        m = re.match(r"^  (?:[\w.$\[\]<>, ]+ )?([\w$<>]+)\((.*)\)(?: throws .*)?;$", line)
        if m:
            pending = m.group(1)
            continue
        m = re.match(r"^    descriptor: (\S+)$", line)
        if m and pending:
            name, desc = pending, m.group(1)
            result[(name, desc)] = Counter()
            pending = None
            continue
        m = re.match(r"^\s+\d+: (\w+)", line)
        if m and name and m.group(1) in OPS:
            result[(name, desc)][m.group(1)] += 1
        # calls into the game's own classes: an overload resolved differently shows up as a changed descriptor
        m = re.match(r"^\s+\d+: invoke\w+\s+#\d+(?:,\s*\d+)?\s+// (?:Interface)?Method ([\w$]+\.)?\"?([\w$<>]+)\"?:(\S+)$", line)
        if m and name:
            owner = (m.group(1) or cls + ".").rstrip(".")
            if "/" not in owner:
                result[(name, desc)]["call " + owner + m.group(3)] += 1
    return result


def main() -> None:
    game = ROOT / (sys.argv[1] if len(sys.argv) > 1 else "soul_full")
    java = Path(find_java())
    javac = java.with_name("javac.exe" if java.suffix == ".exe" else "javac")
    javap = java.with_name("javap.exe" if java.suffix == ".exe" else "javap")
    sep = ";" if sys.platform == "win32" else ":"
    classes, method_names = {}, {}
    for line in (game / "docs" / "names.map").read_text(encoding="utf-8").splitlines():
        parts = line.split()
        if len(parts) == 3 and parts[0] == "class":
            classes[parts[1]] = parts[2]
        elif len(parts) == 3 and parts[0] == "method":
            owner, rest = parts[1].split(".", 1)
            method_names[(owner, rest.split("(")[0], rest[rest.index("("):])] = parts[2]
    with tempfile.TemporaryDirectory(prefix="sod_narrow_") as tmp:
        build = Path(tmp)
        cp = sep.join(str(j) for j in sorted(STUBS.glob("*.jar")))
        subprocess.run([str(javac), "-nowarn", "-proc:none", "-encoding", "UTF-8", "--release", "8", "-cp", cp, "-d", str(build),
                        *map(str, sorted((game / "src").glob("*.java")))], check=True, capture_output=True)
        bad = 0
        for old, new in sorted(classes.items()):
            original = methods(javap, game / "extracted", old)
            rebuilt = methods(javap, build, new)

            def rename_desc(d):
                for o, n in classes.items():
                    d = d.replace(f"L{o};", f"L{n};")
                return d

            # names.map keys use Java-style parameter lists; match renamed methods by descriptor + new name
            renamed = {}
            for (owner, name, params), target in method_names.items():
                if owner == old:
                    renamed.setdefault(name, []).append(target)
            for (name, desc), counts in sorted(original.items()):
                candidates = [name] + renamed.get(name, [])
                found = None
                for c in candidates:
                    if (c, rename_desc(desc)) in rebuilt:
                        found = rebuilt[(c, rename_desc(desc))]
                        if c != name:
                            break
                if found is None:
                    print(f"{new}.{name}{desc}: not found in rebuilt classes")
                    bad += 1
                elif any(found[op] != counts[op] for op in OPS):
                    print(f"{new}.{name}{desc}: original {dict((k, counts[k]) for k in OPS if counts[k])} rebuilt {dict((k, found[k]) for k in OPS if found[k])}")
                    bad += 1
                if found is None:
                    continue
                # Calls into the game's own classes, as owner + descriptor (member names differ after
                # renaming). A decompiled call that javac resolves to another overload changes this set.
                original_calls, rebuilt_calls = Counter(), Counter()
                for key, n in counts.items():
                    if key.startswith("call "):
                        owner, d = key[5:].split("(", 1)
                        original_calls[classes.get(owner, owner) + "(" + rename_desc(d)] += n
                for key, n in found.items():
                    if key.startswith("call "):
                        rebuilt_calls[key[5:]] += n
                if original_calls != rebuilt_calls:
                    print(f"{new}.{name}{desc}: call targets differ: original-only {dict(original_calls - rebuilt_calls)} "
                          f"rebuilt-only {dict(rebuilt_calls - original_calls)}")
                    bad += 1
        print(f"{game.name}: {bad} finding(s): narrowing-cast counts or call targets that differ from the original bytecode")
        sys.exit(1 if bad else 0)


if __name__ == "__main__":
    main()
