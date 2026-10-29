"""Verify placed tutorial prompt lifecycles against original Java."""
import argparse
import subprocess
from pathlib import Path

p = argparse.ArgumentParser(description=__doc__)
p.add_argument("--exe", required=True, type=Path)
p.add_argument("--port-exe", type=Path)
p.add_argument("--data", default=Path("soul/extracted"), type=Path)
a = p.parse_args()
expected = Path("soul/analysis/prompt-oracle.tsv").read_text().splitlines()
actual = subprocess.check_output([str(a.exe), "--data", str(a.data), "--prompt-manifest"], text=True).splitlines()
assert len(expected) == 704 and len(actual) == len(expected), (len(actual), len(expected))
for i, (want, got) in enumerate(zip(expected, actual)):
    assert want == got, f"Prompt row {i}:\nJava: {want}\nC++:  {got}"
print("PASS: 704 original-bytecode prompt lifecycle/visibility/animation rows")

if a.port_exe:
    def run(width, fps, game=False, interpolate=True, frames=None):
        flags = ["--game", "--game-prompt-script"] if game else ["--movement-preview", "--prompt-script"]
        cmd = [str(a.port_exe), *flags, "--data", str(a.data), "--headless", "--width", str(width), "--fps", str(fps), "--frames", str(frames if frames is not None else fps*(40 if game else 30))]
        if not interpolate:
            cmd.append("--no-interpolation")
        return dict(x.split("=") for x in subprocess.check_output(cmd, text=True).split())

    keys = ("prompts_active", "prompts_visible", "prompts_removed", "fire_prompt_alive", "fire_prompt_status", "fire_casts")
    for game, frames, expected in ((False,1,(9,0,0,1,0,0)), (False,15,(9,1,0,1,0,0)), (False,40,(7,0,2,1,0,0)), (False,150,(7,0,2,1,0,0)), (True,140,(9,2,0,1,2,1)), (True,150,(9,2,0,1,2,1)), (True,160,(7,0,2,0,2,1))):
        state=run(240,"native",game,frames=frames)
        assert tuple(int(state[k]) for k in keys)==expected, (game,frames,state)
    for game in (False,True):
        baseline=None
        for width in (240,569):
            crc=None
            for fps in (30,60,144):
                for interpolate in (True,False):
                    state=run(width,fps,game,interpolate)
                    assert state["placed_prompts"]=="9" and state["prompts_removed"]=="2" and state["prompts_active"]=="7",state
                    assert state["fire_prompt_alive"]==("0" if game else "1") and state["fire_prompt_status"]==("2" if game else "0"),state
                    assert state["fire_casts"]==("1" if game else "0"),state
                    stable={k:v for k,v in state.items() if k not in {"frames","width","movement_crc","frontend_crc"}}
                    if baseline is None:baseline=stable
                    assert stable==baseline,"Display settings changed prompt simulation"
                    if crc is None:crc=state["movement_crc"]
                    assert state["movement_crc"]==crc,"Display timing changed prompt pixels"
    print("PASS: seven milestones and 24 movement/magic prompt host runs at widths 240/569 across FPS/interpolation")
