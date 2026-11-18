#!/usr/bin/env python3
"""Measures the loudness of the game's MIDI tracks and prints the per-track gains the Windows host
plays them with (kTrackLevels in soul/port/engine/host_midi.cpp).

The tracks were mixed for a phone: the title and level themes use channel volumes of 25-50 and
velocities of 20-57, the menu/boss theme and the jingles volumes of 75-127 and velocities up to 127.
Through a desktop GS synthesizer that is a 12-26 dB difference between tracks.

Each track is rendered offline with the General MIDI sample set the Windows GS synthesizer uses
(gm.dls: DLS level 1 regions, sample loops, volume envelopes, the concave velocity and volume
curves) and measured as integrated loudness (ITU-R BS.1770: K-weighting, 400 ms blocks, absolute
and relative gates). The gain is applied the way the host applies it, as one factor on both the
channel volume (CC7) and the note velocities, each rounded and clamped like the host does, and is
iterated until the rendered track reaches the target. A formula over the note data alone (the
levelling removed in 8d9cf2f) misjudged the differences by up to 9 dB.

    python tools/music_levels.py [--dls PATH] [--target LUFS] [--keep DIR]

Needs numpy and scipy, a JDK and the extracted 1.0.1 data (setup.ps1). The tracks are decoded by
the original classes (tools/reference_checks/MusicDump.java); nothing is written to the repository.
"""
import argparse
import math
import struct
import subprocess
import sys
import tempfile
from pathlib import Path

import numpy as np
from scipy.signal import lfilter

sys.path.insert(0, str(Path(__file__).resolve().parent))
from decompile import find_java  # noqa: E402

ROOT = Path(__file__).resolve().parent.parent
EXTRACTED = ROOT / "soul" / "extracted"
STUBS = ROOT / "tools" / "midp-stubs"
CHECKS = ROOT / "tools" / "reference_checks"
FS = 48000
TARGET = -20.0   # LUFS; about where the menu/boss theme already plays


# ---------------------------------------------------------------- DLS level 1 (gm.dls)

def _chunks(d, p, end):
    while p + 8 <= end:
        cid = d[p:p + 4]
        n = struct.unpack_from("<I", d, p + 4)[0]
        if cid in (b"LIST", b"RIFF"):
            yield d[p + 8:p + 12], p + 12, p + 8 + n
        else:
            yield cid, p + 8, p + 8 + n
        p += 8 + n + (n & 1)


def _timecents(v):
    return 0.0 if v == -0x80000000 else 2 ** (v / 65536 / 1200)


class Dls:
    def __init__(self, path):
        self.d = d = Path(path).read_bytes()
        if d[:4] != b"RIFF" or d[8:12] != b"DLS ":
            raise SystemExit(f"{path}: not a DLS file")
        self.instruments, self.waves = {}, []
        for cid, q, e in _chunks(d, 12, len(d)):
            if cid == b"lins":
                for c, q2, e2 in _chunks(d, q, e):
                    if c == b"ins ":
                        self._instrument(q2, e2)
            elif cid == b"wvpl":
                for c, q2, e2 in _chunks(d, q, e):
                    if c == b"wave":
                        self.waves.append(self._wave(q2, e2))

    def _articulation(self, q, e):
        a = {}
        for c, q2, _ in _chunks(self.d, q, e):
            if c == b"art1":
                size, count = struct.unpack_from("<II", self.d, q2)
                for i in range(count):
                    src, ctl, dst, _, scale = struct.unpack_from("<HHHHi", self.d, q2 + size + i * 12)
                    if src == 0 and ctl == 0:
                        a[dst] = scale
        return a

    def _wsmp(self, q):
        size, unity, fine, attenuation, _, loops = struct.unpack_from("<IHhiII", self.d, q)
        loop = None
        if loops:
            _, _, start, length = struct.unpack_from("<IIII", self.d, q + size)
            loop = (start, length)
        return unity, fine, attenuation, loop

    def _instrument(self, q, e):
        d = self.d
        regions, art, bank, program = [], {}, 0, 0
        for c, q2, e2 in _chunks(d, q, e):
            if c == b"insh":
                _, bank, program = struct.unpack_from("<III", d, q2)
            elif c == b"lart":
                art = self._articulation(q2, e2)
            elif c == b"lrgn":
                for c3, q3, e3 in _chunks(d, q2, e2):
                    if c3 != b"rgn ":
                        continue
                    r = {"art": None, "wsmp": None}
                    for c4, q4, e4 in _chunks(d, q3, e3):
                        if c4 == b"rgnh":
                            r["keys"] = struct.unpack_from("<HH", d, q4)
                            r["vels"] = struct.unpack_from("<HH", d, q4 + 4)
                        elif c4 == b"wsmp":
                            r["wsmp"] = self._wsmp(q4)
                        elif c4 == b"wlnk":
                            r["wave"] = struct.unpack_from("<HHII", d, q4)[3]
                        elif c4 == b"lart":
                            r["art"] = self._articulation(q4, e4)
                    regions.append(r)
        self.instruments[(bool(bank & 0x80000000), (bank >> 8) & 0x7F, program & 0x7F)] = (art, regions)

    def _wave(self, q, e):
        w = {"wsmp": None}
        for c, q2, e2 in _chunks(self.d, q, e):
            if c == b"fmt ":
                w["fmt"] = struct.unpack_from("<HHIIHH", self.d, q2)
            elif c == b"wsmp":
                w["wsmp"] = self._wsmp(q2)
            elif c == b"data":
                raw = self.d[q2:e2]
                if w["fmt"][5] == 16:
                    w["pcm"] = np.frombuffer(raw, "<i2").astype(np.float32) / 32768
                else:
                    w["pcm"] = (np.frombuffer(raw, np.uint8).astype(np.float32) - 128) / 128
        return w

    def note(self, drum, program, key, velocity, gain, held):
        inst = self.instruments.get((drum, 0, program)) or self.instruments.get((drum, 0, 0))
        if not inst:
            return None
        art, regions = inst
        region = next((r for r in regions if r["keys"][0] <= key <= r["keys"][1]
                       and r["vels"][0] <= velocity <= r["vels"][1]), None)
        if region is None:
            return None
        a = region["art"] if region["art"] is not None else art
        wave = self.waves[region["wave"]]
        unity, fine, attenuation, loop = region["wsmp"] or wave["wsmp"]
        attack = _timecents(a.get(0x0206, -0x80000000))
        decay = _timecents(a.get(0x0207, -0x80000000))
        release = _timecents(a.get(0x0209, -0x80000000))
        sustain = a.get(0x020A, 1000 << 16) / 65536 / 1000
        if drum:
            held = 4.0      # GS drum notes ignore note-off
        sustain_db = -96.0 if sustain <= 0 else max(-96.0, 20 * math.log10(min(1.0, sustain)))
        n = int(min(held + release, 8.0) * FS)
        if n <= 0:
            return None
        t = np.arange(n) / FS
        db = np.where(t < attack, 0.0, -96.0 * (t - attack) / max(decay, 1e-9))
        db = np.maximum(db, sustain_db) if decay > 0 else np.where(t < attack, 0.0, sustain_db)
        env = 10 ** (db / 20) * np.where(t < attack, t / max(attack, 1e-9), 1.0)
        off = int(held * FS)
        if off < n:
            level_db = 20 * math.log10(max(float(env[max(off - 1, 0)]), 1e-6))
            tail = level_db - 96.0 * (t[off:] - held) / max(release, 1e-3)
            env[off:] = 10 ** (np.maximum(tail, -120.0) / 20)
        pcm = wave["pcm"]
        pos = np.arange(n) * (2 ** ((key - unity) / 12 + fine / 1200) * wave["fmt"][2] / FS)
        if loop and loop[1] > 0:
            start, length = loop
            pos = np.where(pos >= start + length, start + np.mod(pos - start, length), pos)
        else:
            keep = pos < len(pcm) - 1
            pos, env = pos[keep], env[keep]
        i = pos.astype(np.int64)
        f = (pos - i).astype(np.float32)
        s = pcm[i] * (1 - f) + pcm[np.minimum(i + 1, len(pcm) - 1)] * f
        return s * env * gain * 10 ** (-attenuation / 655360 / 20)


# ---------------------------------------------------------------- SMF and rendering

def parse_smf(d):
    def be(p, n):
        return int.from_bytes(d[p:p + n], "big")
    tracks, division = be(10, 2), be(12, 2)
    p, events, order = 8 + be(4, 4), [], 0
    for _ in range(tracks):
        q, end = p + 8, p + 8 + be(p + 4, 4)
        tick = status = 0

        def vlq():
            nonlocal q
            v = 0
            while True:
                b = d[q]
                q += 1
                v = (v << 7) | (b & 0x7F)
                if not b & 0x80:
                    return v
        while q < end:
            tick += vlq()
            b = d[q]
            q += 1
            if b == 0xFF:
                kind = d[q]
                q += 1
                n = vlq()
                if kind == 0x51:
                    events.append((tick, order, None, int.from_bytes(d[q:q + 3], "big"), 0))
                q += n
                order += 1
                if kind == 0x2F:
                    break
                continue
            if b in (0xF0, 0xF7):
                q += vlq()
                continue
            if b & 0x80:
                status, first = b, d[q]
                q += 1
            else:
                first = b
            second = 0
            if status >> 4 not in (0xC, 0xD):
                second = d[q]
                q += 1
            events.append((tick, order, status, first, second))
            order += 1
        p = end
    events.sort(key=lambda e: (e[0], e[1]))
    tempo, last, ms, out = 500000, 0, 0.0, []
    for tick, _, status, a, b in events:
        ms += (tick - last) * tempo / division / 1000
        last = tick
        if status is None:
            tempo = a or 500000
        else:
            out.append((ms / 1000, status, a, b))
    return out, ms / 1000


def scaled(value, factor, low):
    """The host's rounding: std::clamp(std::lround(value * factor), low, 127)."""
    return min(127, max(low, int(math.floor(value * factor + 0.5))))


def render(dls, events, length, gain_db):
    f = 10 ** (gain_db / 80)    # amplitude follows (velocity * volume)^2
    program, volume, sounding, notes = [0] * 16, [scaled(100, f, 0)] * 16, {}, []
    for t, status, a, b in events:
        kind, ch = status >> 4, status & 0xF
        if kind == 0xC:
            program[ch] = a
        elif kind == 0xB and a == 7:
            volume[ch] = scaled(b, f, 0)
        elif kind == 0x9 and b > 0:
            if (ch, a) in sounding:
                notes.append(sounding.pop((ch, a)) + (t,))
            sounding[(ch, a)] = (t, ch, program[ch], a, scaled(b, f, 1), volume[ch])
        elif kind in (0x8, 0x9) and (ch, a) in sounding:
            notes.append(sounding.pop((ch, a)) + (t,))
    notes += [v + (length,) for v in sounding.values()]
    out = np.zeros(int((length + 9) * FS), np.float32)
    for t0, ch, prog, key, vel, vol, t1 in notes:
        s = dls.note(ch == 9, 0 if ch == 9 else prog, key, vel, (vel / 127) ** 2 * (vol / 127) ** 2, t1 - t0)
        if s is not None:
            i = int(t0 * FS)
            out[i:i + len(s)] += s
    return out[:int(length * FS) + FS]


def loudness(x):
    """ITU-R BS.1770 integrated loudness (48 kHz K-weighting coefficients)."""
    y = lfilter([1.53512485958697, -2.69169618940638, 1.19839281085285], [1, -1.69065929318241, 0.73248077421585], x)
    y = lfilter([1.0, -2.0, 1.0], [1, -1.99004745483398, 0.99007225036621], y)
    block, hop = int(0.4 * FS), int(0.1 * FS)
    if len(y) < block:
        y = np.pad(y, (0, block - len(y)))
    power = np.array([np.mean(y[i:i + block] ** 2) for i in range(0, len(y) - block + 1, hop)])
    lk = -0.691 + 10 * np.log10(power + 1e-20)
    gated = power[lk > -70]
    if not len(gated):
        return -99.0
    relative = -0.691 + 10 * np.log10(gated.mean()) - 10
    return -0.691 + 10 * np.log10(power[(lk > -70) & (lk > relative)].mean())


def fnv1a(data):
    h = 0xCBF29CE484222325
    for b in data:
        h = ((h ^ b) * 0x100000001B3) & 0xFFFFFFFFFFFFFFFF
    return h


# ---------------------------------------------------------------- tracks

def dump_tracks(out):
    java = Path(find_java())
    javac = java.with_name("javac.exe" if java.suffix == ".exe" else "javac")
    sep = ";" if sys.platform == "win32" else ":"
    jars = [str(p) for p in sorted(STUBS.glob("*.jar"))]
    if not (EXTRACTED / "e.class").exists():
        raise SystemExit(f"{EXTRACTED} has no game classes; run setup.ps1 first")
    with tempfile.TemporaryDirectory(prefix="sod_music_") as tmp:
        tmp = Path(tmp)
        classes, data = tmp / "classes", tmp / "data"
        classes.mkdir()
        data.mkdir()
        subprocess.run([str(javac), "-nowarn", "-d", str(classes), str(CHECKS / "MusicDump.java")], check=True)
        # The game opens its packs with "".getClass().getResourceAsStream(...), i.e. as java.base resources.
        for f in EXTRACTED.iterdir():
            if f.is_file() and f.suffix != ".class":
                (data / f.name).write_bytes(f.read_bytes())
        subprocess.run([str(java), "-Djava.awt.headless=true", f"-Xbootclasspath/a:{sep.join([str(EXTRACTED), *jars])}",
                        f"--patch-module=java.base={data}", "-cp", str(classes), "MusicDump", str(out)], check=True)


def main():
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--dls", default=r"C:\Windows\System32\drivers\gm.dls")
    ap.add_argument("--target", type=float, default=TARGET, help="loudness every track is levelled to (LUFS)")
    ap.add_argument("--keep", type=Path, help="write the decoded tracks here (outside the repository)")
    args = ap.parse_args()
    dls = Dls(args.dls)
    with tempfile.TemporaryDirectory(prefix="sod_tracks_") as tmp:
        folder = args.keep or Path(tmp)
        folder.mkdir(parents=True, exist_ok=True)
        dump_tracks(folder)
        rows = []
        for path in sorted(folder.glob("t*.mid"), key=lambda p: int(p.stem[1:])):
            data = path.read_bytes()
            events, length = parse_smf(data)
            before = loudness(render(dls, events, length, 0.0))
            gain, after = args.target - before, before
            for _ in range(4):
                after = loudness(render(dls, events, length, gain))
                if abs(after - args.target) < 0.05:
                    break
                gain += args.target - after
            rows.append((path.stem, fnv1a(data), len(data), length, before, gain, after))
    print(f"{'track':6} {'bytes':>6} {'length':>7} {'before':>8} {'gain':>7} {'after':>8}")
    for name, _, size, length, before, gain, after in rows:
        print(f"{name:6} {size:6} {length:6.1f}s {before:7.1f}  {gain:+6.1f}  {after:7.1f}")
    print(f"\n// generated by tools/music_levels.py (target {args.target:.1f} LUFS, {Path(args.dls).name})")
    print("const TrackLevel kTrackLevels[] = {")
    for name, h, size, _, before, gain, _ in rows:
        print(f"    {{0x{h:016x}ull, {size}, {gain:+.1f}}},   // {name}, {before:.1f} LUFS as mixed")
    print("};")


if __name__ == "__main__":
    main()
