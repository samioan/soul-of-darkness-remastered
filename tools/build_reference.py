#!/usr/bin/env python3
"""Reference pipeline for Soul of Darkness 1.0.1 (HTC P3450 build, 240x320).

    python tools/build_reference.py              # extract (if needed), decompile, rename, compile-check
    python tools/build_reference.py --jar PATH   # use this JAR instead of roms/

Output:
    soul/extracted/   JAR contents (ignored)
    soul/decompiled/  raw Vineflower output
    soul/src/         renamed reference Java (soul/docs/names.map + PATCHES), compile-checked

PATCHES are decompiler artifacts fixed textually after renaming: (file, old, new), each must match
exactly once and carries its bytecode evidence in the comment. tools/check_engine.py is what
finds them: it compares the translated source with the original class files tick by tick.
"""
import hashlib
import shutil
import subprocess
import sys
import tempfile
import zipfile
from pathlib import Path, PurePosixPath

sys.path.insert(0, str(Path(__file__).resolve().parent))
from decompile import VINEFLOWER, find_java  # noqa: E402

ROOT = Path(__file__).resolve().parent.parent
GAME = ROOT / "soul"
JAR = ROOT / "roms" / "SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101_S8000.jar"
SHA256 = "3da9ea7fe8dfaa2c090e8b3714eaed15127f51dc8b67ed9ef9fed51172e4bea4"
STUBS = ROOT / "tools" / "midp-stubs"
RENAMER_SRC = ROOT / "tools" / "renamer" / "ChRenamer.java"

PATCHES: list[tuple[str, str, str]] = [
    # Game.updateCamera: local 3 is an int flag (iload_3 / ifne).
    ("Game.java", "            if (!var3) {", "            if (var3 == 0) {"),
    # Script tick: dup_x2 keeps the old short for local 9 while the incremented value is stored
    # (tools/check_engine.py diverges without it).
    ("Game.java", "         cX[var0]++;\n         short var9 = cX[var0];", "         short var9 = cX[var0]++;"),
    # Actor.a(Object): checkcast Integer / invokevirtual Integer.intValue, printed as auto-unboxing
    # (which CLDC source cannot contain).
    ("Actor.java", "      return (Integer)var1;", "      return ((Integer)var1).intValue();"),
]

# Locals the decompiler declared narrower than the bytecode treats them. The original methods contain
# fewer i2b/i2s instructions than the decompiled source recompiles to (tools/check_narrowing.py), i.e.
# these variables were ints and their compound assignments never truncated.
# (file, first line of the method, declaration to widen): the method line must be unique in the file
# and the declaration must occur exactly once inside that method.
WIDEN: list[tuple[str, str, str]] = [
    ("Game.java", "   static final int v(int var0) {", "byte var5"),
    ("Game.java", "   private static int a(int var0, int var1, int var2, boolean var3) {", "byte var6"),
    ("Game.java", "   private static void a(Graphics var0, int var1, int var2, int var3) {", "byte var6"),
    ("Game.java", "   private static void a(Graphics var0, byte[] var1, byte[] var2, BSprite var3, int var4, int var5, int var6) {", "byte var9"),
    ("Game.java", "   static final void a(int var0, int var1, int var2, int var3, byte[] var4, int var5) {", "byte var10"),
    ("Game.java", "   public static final void b(int var0, boolean var1, int var2) {", "short var35"),
    ("Game.java", "   private static void aN() {", "byte var0"),
    ("Game.java", "   static final void a(byte[] var0, byte[] var1, int var2, int var3) {", "byte var9"),
    ("Game.java", "   static final byte[] c(byte[] var0) {", "short var4"),
    ("Actor.java", "   private int aA() {", "byte var2"),
    ("Actor.java", "   private void b(int var1, boolean var2) {", "byte var3"),
    ("Actor.java", "   private void aP() {", "byte var21"),
    ("Actor.java", "   private void bz() {", "byte var3"),
    ("Actor.java", "   public final void al() {", "byte var2"),
    ("Actor.java", "   public final void al() {", "byte var1"),
    ("Actor.java", "   private void cI() {", "byte var1"),
    ("Actor.java", "   private boolean cJ() {", "byte var1"),
    ("Actor.java", "   public static final void am() {", "byte var0"),
]

# Save-buffer writes whose third argument is byte-typed. The game has two writers named a:
# a(byte[],int,int) stores one byte, a(byte[],int,short) stores two. The original bytecode calls the
# one-byte writer at these sites, but in Java source a byte argument selects the short overload, so
# the decompiled call silently clobbers the following byte (found by tools/check_engine.py at save
# offset 805; tools/check_narrowing.py lists the affected methods). An (int) cast restores the
# original target. (file, regex, replacement, expected substitutions)
OVERLOADS: list[tuple[str, str, str, int]] = [
    ("Actor.java", r"(Game\.a\(Game\.dt, [^;,]+, )(\(byte\)\()", r"\1(int)\2", 12),
    ("Actor.java", r"(Game\.a\(Game\.dt, [^;,]+, )((?:Game\.cq|bH|ej|dy|dx)\[var\d+\]\);)", r"\1(int)\2", 5),
    ("Game.java", r"(\ba\(dt, [^;,]+, )(\(byte\))", r"\1(int)\2", 4),
]


def widen(text: str, fname: str, method: str, decl: str) -> str:
    if text.count(method + "\n") != 1:
        sys.exit(f"widen: method line not unique in {fname}: {method.strip()!r}")
    start = text.index(method + "\n")
    end = text.find("\n   }\n", start)
    body = text[start:end]
    import re
    hits = re.findall(r"\b" + re.escape(decl) + r"\b", body)
    if len(hits) != 1:
        sys.exit(f"widen: expected one {decl!r} in {fname} {method.strip()!r}, found {len(hits)}")
    body = re.sub(r"\b" + re.escape(decl) + r"\b", "int " + decl.split()[1], body)
    return text[:start] + body + text[end:]


def run(cmd, **kw):
    return subprocess.run([str(c) for c in cmd], check=True, **kw)


def extract(jar: Path) -> None:
    data = jar.read_bytes()
    digest = hashlib.sha256(data).hexdigest()
    if digest != SHA256:
        sys.exit(f"{jar.name}: SHA-256 {digest} is not the supported 1.0.1 build ({SHA256})")
    out = GAME / "extracted"
    with zipfile.ZipFile(jar) as archive:
        for entry in archive.infolist():
            path = PurePosixPath(entry.filename)
            if path.is_absolute() or ".." in path.parts or "\\" in entry.filename or ":" in entry.filename:
                sys.exit(f"unsafe archive entry: {entry.filename}")
        if archive.testzip() is not None:
            sys.exit("archive CRC check failed")
        out.mkdir(parents=True, exist_ok=True)
        for old in out.rglob("*"):
            if old.is_file():
                old.unlink()
        archive.extractall(out)
    print(f"extracted {jar.name} -> {out.relative_to(ROOT)}", flush=True)


def main() -> None:
    jar = JAR
    if "--jar" in sys.argv:
        jar = Path(sys.argv[sys.argv.index("--jar") + 1])
        JAR.parent.mkdir(exist_ok=True)
        if jar.resolve() != JAR.resolve():
            shutil.copyfile(jar, JAR)
    extracted = GAME / "extracted"
    if "--jar" in sys.argv or not (extracted / "e.class").exists():
        if not JAR.exists():
            sys.exit(f"missing {JAR}; pass --jar PATH")
        extract(JAR)

    java = Path(find_java())
    javac = java.with_name("javac.exe" if java.suffix == ".exe" else "javac")
    sep = ";" if sys.platform == "win32" else ":"
    names = GAME / "docs" / "names.map"
    with tempfile.TemporaryDirectory(prefix="sod_ref_") as tmp:
        tmp = Path(tmp)
        classes, raw, renamed, tool = tmp / "classes", tmp / "raw", tmp / "renamed", tmp / "tool"
        for d in (classes, raw, renamed, tool):
            d.mkdir()
        for c in extracted.glob("*.class"):
            shutil.copyfile(c, classes / c.name)
        run([java, "-jar", VINEFLOWER, "-log=WARN", classes, raw])
        run([javac, "-d", tool, "-cp", VINEFLOWER, RENAMER_SRC])
        run([java, f"-Dch.map={names}", "-cp", f"{VINEFLOWER}{sep}{tool}",
             "org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler",
             "-log=WARN", "-ren=1", "-urc=ChRenamer", classes, renamed])
        for sub, source in (("decompiled", raw), ("src", renamed)):
            out = GAME / sub
            out.mkdir(exist_ok=True)
            texts = {p.name: p.read_text(encoding="utf-8") for p in sorted(source.glob("*.java"))}
            if not texts:
                sys.exit("Vineflower produced no sources")
            if sub == "src":
                for fname, old, new in PATCHES:
                    if texts[fname].count(old) != 1:
                        sys.exit(f"patch does not match exactly once in {fname}: {old[:60]!r}")
                    texts[fname] = texts[fname].replace(old, new)
                for fname, method, decl in WIDEN:
                    texts[fname] = widen(texts[fname], fname, method, decl)
                import re
                for fname, pattern, replacement, count in OVERLOADS:
                    texts[fname], done = re.subn(pattern, replacement, texts[fname])
                    if done != count:
                        sys.exit(f"overload fix expected {count} substitution(s) in {fname}, made {done}: {pattern}")
            for old in out.glob("*.java"):
                old.unlink()
            for name, text in texts.items():
                (out / name).write_text(text, encoding="utf-8", newline="\n")
            print(f"wrote {len(texts)} files -> {out.relative_to(ROOT)}", flush=True)
        classpath = sep.join(str(j) for j in sorted(STUBS.glob("*.jar")))
        build = tmp / "build"
        build.mkdir()
        run([javac, "-nowarn", "-proc:none", "-encoding", "UTF-8", "--release", "8", "-cp", classpath, "-d", build,
             *sorted((GAME / "src").glob("*.java"))])
        print("compile check: OK (0 errors)", flush=True)


if __name__ == "__main__":
    main()
