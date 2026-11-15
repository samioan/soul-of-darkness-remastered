#!/usr/bin/env python3
"""Whole-engine equivalence check: original class files vs. the translated C++ engine.

Both sides run Game.paint tick by tick with the same java.util.Random seed and the same scripted
key events, and print a fingerprint of every static field of Game/Actor/BSprite plus every field of
every live actor after each tick. Any divergence fails with the first differing tick and fields.

    python tools/check_engine.py                                   # Release soul_engine_check, default scenarios
    python tools/check_engine.py --ticks 6000 --seeds 1 2 3        # custom fuzz
    python tools/check_engine.py --exe <other soul_engine_check>   # e.g. a Debug or unpatched (--vanilla) build

soul_engine_check runs at width 240 with the original text unless told otherwise; in that configuration
the desktop patches are required to be no-ops, so the shipped engine itself is what gets compared.
Drawing is not compared (the oracle's Graphics draws nothing); promotion screens
(class a) are replaced by the same disabled stub on both sides. Results go to soul/analysis/.
"""
import argparse
import random
import subprocess
import sys
import tempfile
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from decompile import find_java  # noqa: E402

ROOT = Path(__file__).resolve().parent.parent
EXTRACTED = ROOT / "soul" / "extracted"
DEFAULT_EXE = ROOT / "soul/port/build/Release/soul_engine_check.exe"
LEVELS = 11
STEP = 40  # boot key spacing in ticks
STUBS = ROOT / "tools" / "midp-stubs"
CHECKS = ROOT / "tools" / "reference_checks"
ANALYSIS = ROOT / "soul" / "analysis"

UP, DOWN, LEFT, RIGHT, FIRE, SOFT_L, SOFT_R = -1, -2, -3, -4, -5, -6, -7
NUM = {n: 48 + n for n in range(10)}
STAR, POUND = 42, 35


def tap(events, tick, key, hold=1):
    events.append((tick, "+", key))
    events.append((tick + hold, "-", key))


def boot_to_level(events, level, start, step):
    """Sound prompt -> menu -> New Game -> prologue -> level map -> chosen level. Returns the next free tick.

    Later levels are reached by setting Game.maxUnlockedLevel on both sides ("!" script lines) and
    walking the original selection map to the right; nothing else about the session is altered.
    """
    tap(events, start, SOFT_L)            # sound prompt
    tap(events, start + step, FIRE)       # title
    tap(events, start + 2 * step, FIRE)   # New Game
    tap(events, start + 3 * step, SOFT_L) # skip prologue
    t = start + 4 * step
    if level:
        events.append((t, "!", 11))
        t += 30                      # map intro animation
        for _ in range(level):
            tap(events, t, RIGHT)
            t += 2 * step            # path animation between map nodes
    for _ in range(10 * step // 20): # confirm the level, then advance any intro dialogue
        tap(events, t, FIRE)
        t += 12
    return t


def fuzz(events, rng, start, end, keys, cheat=False):
    """Random overlapping key holds, like mashing a keypad."""
    t = start
    if cheat:
        # original cheat: 2,1,1,5 then * for god mode (Game.updateCheatSequence)
        for k in (NUM[2], NUM[1], NUM[1], NUM[5], STAR):
            tap(events, t, k)
            t += 3
    while t < end:
        key = rng.choice(keys)
        hold = rng.choice((1, 1, 2, 3, 5, 8, 13, 21, 34))
        tap(events, t, key, hold)
        t += rng.choice((1, 1, 2, 3, 4, 6, 9))


def scenario(seed, ticks, cheat, level):
    rng = random.Random(seed)
    events = []
    t = boot_to_level(events, level, 100, STEP)
    move = [LEFT, RIGHT, RIGHT, RIGHT, UP, UP, DOWN, FIRE, FIRE, FIRE, NUM[0], NUM[9], NUM[1], NUM[3], NUM[7], NUM[5]]
    fuzz(events, rng, t, ticks, move + ([SOFT_L, SOFT_R] if seed % 3 == 0 else []), cheat)
    return events


def write_script(path, events):
    path.write_text("".join(f"{t} {op} {key}\n" for t, op, key in sorted(events, key=lambda e: e[0])), encoding="ascii")


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--exe", type=Path, default=DEFAULT_EXE, help="headless driver to test (default: the Release soul_engine_check)")
    ap.add_argument("--ticks", type=int, default=2500)
    ap.add_argument("--seeds", type=int, nargs="*", default=[1, 2, 3, 4, 5, 6])
    ap.add_argument("--level", type=int, default=-1, help="level index for every scenario (default: seed modulo the level count)")
    ap.add_argument("--save", type=Path, help="RMS record to preload on both sides")
    ap.add_argument("--script", type=Path, help="use this key script instead of generated fuzz")
    args = ap.parse_args()

    java = Path(find_java())
    javac = java.with_name("javac.exe" if java.suffix == ".exe" else "javac")
    sep = ";" if sys.platform == "win32" else ":"
    jars = sorted(STUBS.glob("*.jar"))
    ANALYSIS.mkdir(exist_ok=True)
    total = 0
    with tempfile.TemporaryDirectory(prefix="sod_oracle_") as tmp:
        tmp = Path(tmp)
        adapter, tool = tmp / "adapter", tmp / "tool"
        adapter.mkdir()
        tool.mkdir()
        sources = [str(p) for p in (CHECKS / "engine_adapter").rglob("*.java")]
        subprocess.run([str(javac), "-nowarn", "-cp", sep.join(map(str, jars)), "-d", str(adapter), *sources], check=True)
        subprocess.run([str(javac), "-nowarn", "-cp", sep.join([str(adapter), *map(str, jars)]), "-d", str(tool),
                        str(CHECKS / "EngineOracle.java")], check=True)
        boot = sep.join([str(adapter), str(tool), str(EXTRACTED), *map(str, jars)])
        # The game opens its packs with "".getClass().getResourceAsStream(...), i.e. as java.base resources.
        data = tmp / "data"
        data.mkdir()
        for f in EXTRACTED.iterdir():
            if f.is_file() and f.suffix != ".class":
                (data / f.name).write_bytes(f.read_bytes())
        for seed in args.seeds:
            cheat = seed % 2 == 0
            script = tmp / f"script{seed}.txt"
            if args.script:
                script = args.script
            else:
                write_script(script, scenario(seed, args.ticks, cheat, args.level if args.level >= 0 else seed % LEVELS))
            java_trace, cpp_trace = ANALYSIS / f"engine-oracle-{seed}.txt", ANALYSIS / f"engine-native-{seed}.txt"

            def run_java(extra=()):
                cmd = [str(java), "-Djava.awt.headless=true", f"-Xbootclasspath/a:{boot}",
                       f"--patch-module=java.base={data}", "EngineOracle",
                       str(script), str(args.ticks), str(seed), str(java_trace), *extra]
                if args.save:
                    cmd += ["-1", "-", str(args.save)] if not extra else [str(args.save)]
                subprocess.run(cmd, check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

            def run_cpp(extra=()):
                cmd = [str(args.exe), "--data", str(EXTRACTED), "--ticks", str(args.ticks), "--seed", str(seed),
                       "--script", str(script), "--trace", str(cpp_trace), "--quiet", *extra]
                if args.save:
                    cmd += ["--save-in", str(args.save)]
                subprocess.run(cmd, check=True)

            run_java()
            run_cpp()
            a, b = java_trace.read_text().splitlines(), cpp_trace.read_text().splitlines()
            states = sorted({int(line.split()[2]) for line in a})
            for i, (x, y) in enumerate(zip(a, b)):
                if x != y:
                    dj, dc = ANALYSIS / "engine-oracle-dump.txt", ANALYSIS / "engine-native-dump.txt"
                    run_java((str(i), str(dj)))
                    run_cpp(("--dump", str(i), str(dc)))
                    jl, cl = dj.read_text().splitlines(), dc.read_text().splitlines()
                    print(f"seed {seed}: first divergence at tick {i}: oracle '{x}' native '{y}'")
                    shown = 0
                    for n, (p, q) in enumerate(zip(jl, cl)):
                        if p.split("=", 1)[-1] != q.split("=", 1)[-1]:
                            pv, qv = p.split(" [", 1), q.split(" [", 1)
                            detail = ""
                            if len(pv) == 2 and len(qv) == 2:
                                pe, qe = pv[1].rstrip("]").split(","), qv[1].rstrip("]").split(",")
                                cells = [f"[{i}] {a} vs {b}" for i, (a, b) in enumerate(zip(pe, qe)) if a != b][:12]
                                detail = "  cells (oracle vs native): " + "; ".join(cells)
                            print(f"  field #{n}: oracle {pv[0]}   native {qv[0]}{detail}")
                            shown += 1
                            if shown >= 25:
                                break
                    if len(jl) != len(cl):
                        print(f"  field counts differ: oracle {len(jl)} native {len(cl)}")
                    sys.exit(1)
            if len(a) != len(b):
                sys.exit(f"seed {seed}: trace lengths differ: oracle {len(a)} native {len(b)}")
            total += len(a)
            print(f"seed {seed}: {len(a)} ticks identical (states visited: {states})", flush=True)
    print(f"engine equivalence: {total} ticks, {len(args.seeds)} scenario(s), all state fingerprints identical")


if __name__ == "__main__":
    main()
