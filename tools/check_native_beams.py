"""Compare native first-level beam controllers with the original bytecode oracle."""
import argparse
import subprocess
from pathlib import Path

p = argparse.ArgumentParser(description=__doc__)
p.add_argument("--exe", required=True, type=Path)
p.add_argument("--port-exe", type=Path)
p.add_argument("--data", default=Path("soul/extracted"), type=Path)
a = p.parse_args()
expected = (Path("soul/analysis") / "beam-oracle.tsv").read_text().splitlines()
actual = subprocess.check_output([str(a.exe), "--data", str(a.data), "--beam-manifest"], text=True).splitlines()
assert len(expected) == 13152
assert len(actual) == len(expected), (len(actual), len(expected))
for i, (want, got) in enumerate(zip(expected, actual)):
    assert want == got, f"Beam row {i}:\nJava: {want}\nC++:  {got}"
print(f"PASS: {len(actual)} original-bytecode beam state/animation/endpoint/damage rows")

if a.port_exe:
    def run(width, fps, game=False, interpolate=True, frames=None):
        flags = ["--game", "--game-beam-script"] if game else ["--movement-preview", "--beam-script"]
        cmd = [str(a.port_exe), *flags, "--data", str(a.data), "--headless", "--width", str(width), "--fps", str(fps), "--frames", str(frames if frames is not None else fps * (60 if game else 50))]
        if not interpolate:
            cmd.append("--no-interpolation")
        return dict(x.split("=") for x in subprocess.check_output(cmd, text=True).split())

    keys = ("player_x", "player_y", "beam_hits", "beams_active", "player_health")
    for frames, values in ((1, (167,1559,0,0,60)), (20,(147,1559,1,1,50)), (30,(147,1559,1,2,50)), (50,(264,1589,1,0,50)), (100,(269,1599,1,1,50)), (250,(269,1599,1,2,50)), (600,(269,1599,1,1,50))):
        state = run(240, "native", frames=frames)
        assert tuple(int(state[k]) for k in keys) == values, (frames, state)
    for game in (False, True):
        baseline = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    state = run(width, fps, game, interpolate)
                    assert state["beam_emitters"] == "6"
                    assert state["beam_hits"] == ("6" if game else "1") and state["player_health"] == ("0" if game else "50"), state
                    assert state["defeated"] == ("1" if game else "0"), state
                    stable = {k:v for k,v in state.items() if k not in {"frames", "width", "movement_crc", "frontend_crc"}}
                    if baseline is None:
                        baseline = stable
                    assert stable == baseline, "Display settings changed beam simulation"
                    if crc is None:
                        crc = state["movement_crc"]
                    assert state["movement_crc"] == crc, "FPS/interpolation changed beam pixels"
    print("PASS: seven milestones and 24 stationary/sweeping beam host runs at widths 240/569 across FPS/interpolation, including damage and defeat")
