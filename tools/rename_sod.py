#!/usr/bin/env python3
"""Soul of Darkness phase 1: rename the decompilation into soul/src/.

Unlike a regex pass over single-letter identifiers, this renames
at the class-file level: Vineflower's --user-renamer-class hook is given
tools/renamer/ChRenamer.java, which reads soul/docs/names.map, so every
reference -- including inherited members reached through a subclass, and the
same single letter reused in different classes -- is rewritten correctly.

    python tools/rename_sod.py            # regenerate soul/src/ and compile-check it
    python tools/rename_sod.py --no-check # skip compile and behavior checks

Steps: compile ChRenamer, run Vineflower over soul/extracted/ with the
map, apply PATCHES (bytecode-verified decompiler artifacts), write
soul/src/*.java, then compile them against the MIDP/CLDC/Nokia-UI stub
jars in tools/midp-stubs/. The compiler is the final arbiter: src/ must build
with zero errors.

names.map format (one entry per line, '#' comments):
    class  <old> <New>
    field  <oldClass>.<oldField> <newName>
    method <oldClass>.<oldMethod>(<paramTypes>) <newName>
paramTypes are Java-style, comma separated, package-free, with obfuscated
class names left as they are: (int,byte[]) (Graphics,f[],int) ().
"""
import subprocess
import sys
import tempfile
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from decompile import VINEFLOWER, find_java  # noqa: E402

ROOT = Path(__file__).resolve().parent.parent
GAME = ROOT / "soul"
EXTRACTED = GAME / "extracted"
MAP = GAME / "docs" / "names.map"
OUT = GAME / "src"
RENAMER_SRC = ROOT / "tools" / "renamer" / "ChRenamer.java"
STUBS = ROOT / "tools" / "midp-stubs"

# Bytecode-verified decompiler artifacts, fixed textually after the
# rename. (file, old, new); every entry must match exactly once.
# e.d(int), bytecode offsets 501..502: iload_3 / ifne 602 (local is 0/1).
PATCHES: list[tuple[str, str, str]] = [
    ("Game.java", "            if (!var3) {", "            if (var3 == 0) {"),
    # e.b(int,boolean,int), offsets 21..32: dup_x2 preserves the old short
    # for local 9 while the incremented/narrowed short is stored in cd[index].
    ("Game.java", "         scriptTicks[var0]++;\n         short var9 = scriptTicks[var0];",
     "         short var9 = scriptTicks[var0]++;"),
]


def run(cmd, **kw):
    return subprocess.run([str(c) for c in cmd], check=True, **kw)


def main() -> None:
    check = "--no-check" not in sys.argv
    java = Path(find_java())
    javac = java.with_name("javac.exe" if java.suffix == ".exe" else "javac")

    with tempfile.TemporaryDirectory(prefix="sod_rename_") as tmp:
        tmp = Path(tmp)
        cls_dir, out_dir = tmp / "cls", tmp / "out"
        cls_dir.mkdir()
        out_dir.mkdir()

        run([javac, "-d", cls_dir, "-cp", VINEFLOWER, RENAMER_SRC])
        sep = ";" if sys.platform == "win32" else ":"
        run([
            java, f"-Dch.map={MAP}", "-cp", f"{VINEFLOWER}{sep}{cls_dir}",
            "org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler",
            "-log=WARN", "-ren=1", "-urc=ChRenamer",
            EXTRACTED, out_dir,
        ])

        sources = sorted(out_dir.glob("*.java"))
        if not sources:
            sys.exit("Vineflower produced no sources")

        # Clear the contents, not the directory itself: on Windows the directory cannot be removed
        # while any shell has it as its working directory.
        OUT.mkdir(exist_ok=True)
        texts = {p.name: p.read_text(encoding="utf-8") for p in sources}
        for fname, old, new in PATCHES:
            if texts[fname].count(old) != 1:
                sys.exit(f"patch does not match exactly once in {fname}: {old[:60]!r}")
            texts[fname] = texts[fname].replace(old, new)
        for old in OUT.glob("*.java"):
            old.unlink()
        for name, text in texts.items():
            (OUT / name).write_text(text, encoding="utf-8", newline="\n")
        print(f"wrote {len(texts)} files -> {OUT.relative_to(ROOT)}", flush=True)

        if check:
            classpath = sep.join(str(j) for j in sorted(STUBS.glob("*.jar")))
            build = tmp / "build"
            build.mkdir()
            run([javac, "-nowarn", "-proc:none", "-encoding", "UTF-8",
                 "--release", "8", "-cp", classpath, "-d", build,
                 *sorted(OUT.glob("*.java"))])
            print("compile check: OK (0 errors)", flush=True)
            # Compare pure recovered methods with the unmodified class files.
            # No Canvas construction, rendering, audio or RMS calls occur here.
            harness = tmp / "checks"
            harness.mkdir()
            run([javac, "--release", "17", "-encoding", "UTF-8", "-d", harness,
                 ROOT / "tools/reference_checks/ReferenceCheck.java"])
            run([java, "-cp", harness, "ReferenceCheck", EXTRACTED, build, STUBS])
            run([javac, "--release", "17", "-encoding", "UTF-8", "-cp", classpath, "-d", harness,
                 ROOT / "tools/reference_checks/SpriteCheck.java",
                 ROOT / "tools/reference_checks/ActorCheck.java",
                 ROOT / "tools/reference_checks/ContactCheck.java",
                 ROOT / "tools/reference_checks/PlatformCheck.java",
                 ROOT / "tools/reference_checks/MovingPlatformCheck.java",
                 ROOT / "tools/reference_checks/SwingingPlatformCheck.java",
                 ROOT / "tools/reference_checks/RegionCheck.java",
                 ROOT / "tools/reference_checks/BridgeCheck.java",
                 ROOT / "tools/reference_checks/TransientCheck.java",
                 ROOT / "tools/reference_checks/WorldOrderCheck.java",
                 ROOT / "tools/reference_checks/LinkedTriggerCheck.java",
                 ROOT / "tools/reference_checks/GateCheck.java",
                 ROOT / "tools/reference_checks/GateScriptCheck.java",
                 ROOT / "tools/reference_checks/RemovedGateTriggerCheck.java",
                 ROOT / "tools/reference_checks/PassageCheck.java",
                 ROOT / "tools/reference_checks/AbilityCheck.java",
                 ROOT / "tools/reference_checks/IcePickupCheck.java",
                 ROOT / "tools/reference_checks/IceWeaponCheck.java",
                 ROOT / "tools/reference_checks/IceWorldCheck.java",
                 ROOT / "tools/reference_checks/IceProjectileCheck.java",
                 ROOT / "tools/reference_checks/IceAttachmentCheck.java",
                 ROOT / "tools/reference_checks/ActivationCheck.java",
                 ROOT / "tools/reference_checks/FlyingEnemyCheck.java",
                 ROOT / "tools/reference_checks/FlyingWorldCheck.java",
                 ROOT / "tools/reference_checks/RemovedEnemyCheck.java",
                 ROOT / "tools/reference_checks/ChestUnlockCheck.java",
                 ROOT / "tools/reference_checks/MeleeCheck.java",
                 ROOT / "tools/reference_checks/EnemyCheck.java",
                 ROOT / "tools/reference_checks/CombatCheck.java",
                 ROOT / "tools/reference_checks/DeathCheck.java",
                 ROOT / "tools/reference_checks/LiveCombatCheck.java",
                 ROOT / "tools/reference_checks/BehaviorCheck.java",
                 ROOT / "tools/reference_checks/ScriptCheck.java",
                 ROOT / "tools/reference_checks/SaveCheck.java",
                 ROOT / "tools/reference_checks/LevelCheck.java",
                 ROOT / "tools/reference_checks/image_adapter/javax/microedition/lcdui/Image.java"])
            oracle = GAME / "analysis/sprite-oracle.tsv"
            script_oracle = GAME / "analysis/script-oracle.tsv"
            # Keep the RMS adapter out of every existing oracle's runtime.
            lifecycle = tmp / "lifecycle"
            lifecycle.mkdir()
            run([javac, "--release", "17", "-encoding", "UTF-8", "-cp", f"{harness}{sep}{classpath}", "-d", lifecycle,
                 ROOT / "tools/reference_checks/SpriteCheck.java",
                 ROOT / "tools/reference_checks/LifecycleCheck.java",
                 ROOT / "tools/reference_checks/CheckpointCheck.java",
                 ROOT / "tools/reference_checks/BeamCheck.java",
                 ROOT / "tools/reference_checks/PromptCheck.java",
                 ROOT / "tools/reference_checks/IntroCheck.java",
                 ROOT / "tools/reference_checks/ChandelierCheck.java",
                 ROOT / "tools/reference_checks/PromptRemovalCheck.java",
                 ROOT / "tools/reference_checks/CameraControllerCheck.java",
                 ROOT / "tools/reference_checks/MenuCheck.java",
                 ROOT / "tools/reference_checks/FinishCheck.java",
                 ROOT / "tools/reference_checks/LevelSelectionCheck.java",
                 ROOT / "tools/reference_checks/ControllerCheck.java",
                 ROOT / "tools/reference_checks/image_adapter/javax/microedition/lcdui/Image.java",
                 ROOT / "tools/reference_checks/rms_adapter/javax/microedition/rms/RecordStore.java"])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "LifecycleCheck", EXTRACTED, build, STUBS, MAP])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "CheckpointCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "BeamCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "PromptCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "IntroCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "ChandelierCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "PromptRemovalCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "CameraControllerCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "MenuCheck", EXTRACTED, build, STUBS, MAP])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "FinishCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "LevelSelectionCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", f"{lifecycle}{sep}{harness}", "ControllerCheck", EXTRACTED, build, STUBS, MAP])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "LevelCheck", EXTRACTED, build, STUBS, MAP])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "SaveCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "ScriptCheck", EXTRACTED, build, STUBS, script_oracle])
            run([sys.executable, ROOT / "tools/parse_levels.py", "--oracle", script_oracle])
            # Sprite/Actor/Behavior checks load the ImageIO-backed test Image; the core
            # checker still uses the unchanged API jars in isolated loaders.
            run([java, "-Djava.awt.headless=true", "-cp", harness, "SpriteCheck", EXTRACTED, build, STUBS, oracle])
            run([sys.executable, ROOT / "tools/parse_sprites.py", "--oracle", oracle, "--quiet"])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "ActorCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "ContactCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "PlatformCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "MovingPlatformCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "SwingingPlatformCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "RegionCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "BridgeCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "TransientCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "WorldOrderCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "LinkedTriggerCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "GateCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "GateScriptCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "RemovedGateTriggerCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "PassageCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "AbilityCheck", EXTRACTED, build, STUBS])
            for name in ("IcePickupCheck", "IceWeaponCheck", "IceWorldCheck", "IceProjectileCheck", "IceAttachmentCheck"):
                run([java, "-Djava.awt.headless=true", "-cp", harness, name, EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "ActivationCheck", EXTRACTED, build, STUBS])
            for name in ("FlyingEnemyCheck", "FlyingWorldCheck", "RemovedEnemyCheck", "ChestUnlockCheck"):
                run([java, "-Djava.awt.headless=true", "-cp", harness, name, EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "MeleeCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "EnemyCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "CombatCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "DeathCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "LiveCombatCheck", EXTRACTED, build, STUBS])
            run([java, "-Djava.awt.headless=true", "-cp", harness, "BehaviorCheck", EXTRACTED, build, STUBS, MAP])
            # Isolated no-op drawing hooks preserve original reward/beam/input bytecode.
            run([javac, "--add-exports", "java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED",
                 "-cp", f"{harness}{sep}{classpath}", "-d", harness,
                 ROOT / "tools/reference_checks/CrystalCheck.java",
                 ROOT / "tools/reference_checks/CompletionCheck.java",
                 ROOT / "tools/reference_checks/AbilityModalCheck.java",
                 ROOT / "tools/reference_checks/FireMagicCheck.java",
                 ROOT / "tools/reference_checks/IceMagicCheck.java"])
            run([java, "--add-exports", "java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED",
                 "-Djava.awt.headless=true", "-cp", harness, "CrystalCheck", EXTRACTED, build, STUBS, tmp / "crystal"])
            run([java, "--add-exports", "java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED",
                 "-Djava.awt.headless=true", "-cp", harness, "CompletionCheck", EXTRACTED, build, STUBS, tmp / "completion"])
            run([java, "--add-exports", "java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED",
                 "-Djava.awt.headless=true", "-cp", harness, "AbilityModalCheck", EXTRACTED, build, STUBS, tmp / "ability-modal"])
            run([java, "--add-exports", "java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED",
                 "-Djava.awt.headless=true", "-cp", harness, "FireMagicCheck", EXTRACTED, build, STUBS, tmp / "fire-magic"])
            run([java, "--add-exports", "java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED",
                 "-Djava.awt.headless=true", "-cp", harness, "IceMagicCheck", EXTRACTED, build, STUBS, tmp / "ice-magic"])



if __name__ == "__main__":
    main()
