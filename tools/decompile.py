#!/usr/bin/env python3
"""Shared Vineflower/JDK helpers for the reference tools.

Decompiling itself is done by tools/build_reference.py, which writes soul/decompiled (raw
Vineflower output) and soul/src (renamed reference Java).

Vineflower 1.12.0 requires Java 17+. find_java() tries $JAVA_HOME, then
`java` on PATH, then any installed Temurin JDK (the usual Adoptium install
location) -- the PATH java on this machine is 1.8, too old to load it.
"""
import os
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
VINEFLOWER = ROOT / "tools" / "vineflower.jar"


def _java_major(exe: str) -> int:
    """Major Java version of `exe` (8 for 1.8.x, 21 for 21.x), 0 if unknown."""
    try:
        out = subprocess.run(
            [exe, "-version"], capture_output=True, text=True, check=True
        )
    except (OSError, subprocess.CalledProcessError):
        return 0
    m = re.search(r'version "([^"]+)"', out.stderr + out.stdout)
    if not m:
        return 0
    parts = m.group(1).split(".")
    if parts[0] == "1":  # legacy scheme: 1.8.0_503 -> 8
        return int(parts[1]) if len(parts) > 1 else 0
    return int(parts[0])


def find_java() -> str:
    """A Java 17+ executable: $JAVA_HOME, then PATH, then Temurin installs."""
    candidates: list[str] = []
    java_home = os.environ.get("JAVA_HOME")
    if java_home:
        candidates.append(str(Path(java_home) / "bin" / "java.exe"))
    candidates.append("java")
    adoptium = Path("C:/Program Files/Eclipse Adoptium")
    if adoptium.exists():
        candidates += sorted(str(p) for p in adoptium.glob("jdk-*/bin/java.exe"))
    for exe in candidates:
        if _java_major(exe) >= 17:
            return exe
    sys.exit(
        "no Java 17+ found (tried $JAVA_HOME, PATH, and installed Temurin JDKs) "
        "-- Vineflower 1.12.0 needs it. Install one from https://adoptium.net/"
    )
