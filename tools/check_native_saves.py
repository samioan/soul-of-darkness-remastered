"""Cross-process native checkpoint autosave/Continue checks; no original RMS compatibility claim."""
import argparse
import subprocess
import tempfile
from pathlib import Path


def run(exe, data, *args, ok=True):
    result = subprocess.run([str(exe), "--data", str(data), "--headless", *map(str, args)], capture_output=True, text=True)
    if not ok:
        assert result.returncode != 0, result.stdout
        return result
    assert result.returncode == 0, result.stderr
    return dict(item.split("=", 1) for item in result.stdout.split() if "=" in item)


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--exe", required=True, type=Path)
    ap.add_argument("--data", type=Path, default=Path("soul/extracted"))
    opt = ap.parse_args()
    with tempfile.TemporaryDirectory(prefix="sod-save-") as temp:
        save = Path(temp) / "checkpoint.sav"
        run(opt.exe, opt.data, "--movement-preview", "--checkpoint-script", "--save-file", save, "--fps", "native", "--frames", 5)
        assert save.exists()
        original = save.read_bytes()
        # Each invocation starts a fresh process and enters the actual Continue menu action.
        checks = 0
        baseline = None
        for width in (240, 569):
            expected = None
            for fps in (30, 60, 144):
                for interpolation in (True, False):
                    save.write_bytes(original)
                    args = ["--game", "--continue", "--save-file", save, "--width", width, "--fps", fps, "--frames", fps * 2]
                    if not interpolation:
                        args.append("--no-interpolation")
                    state = run(opt.exe, opt.data, *args)
                    for key, value in {"frontend_state": 8, "player_x": 148, "player_y": 259, "player_health": 23, "player_magic": 17, "active_weapon": 1, "ice_unlocked": 1, "magic_unlocked": 1, "health_fragments": 2, "magic_fragments": 1, "checkpoint_uid": 11, "checkpoint_restarts": 0, "checkpoint_activations": 1, "checkpoints_active": 5, "continue_available": 1}.items():
                        assert int(state[key]) == value, (key, state[key], value)
                    if expected is None:
                        expected = state["frontend_crc"]
                    assert expected == state["frontend_crc"], "Presentation changed resumed state"
                    deterministic = {k: v for k, v in state.items() if k not in {"frames", "width", "movement_crc", "frontend_crc"}}
                    if baseline is None:
                        baseline = deterministic
                    assert baseline == deterministic, "Width/FPS/interpolation changed simulation"
                    checks += 1
        # Normal boot exposes Continue; scripted New Game confirms and erases the prior file.
        save.write_bytes(original)
        menu = run(opt.exe, opt.data, "--game", "--game-script", "--save-file", save, "--fps", "native", "--frames", 48)
        assert menu["frontend_state"] == "2" and menu["continue_available"] == "1" and save.read_bytes() == original
        new_game = run(opt.exe, opt.data, "--game", "--game-script", "--save-file", save, "--fps", "native", "--frames", 85)
        assert new_game["frontend_state"] == "20" and new_game["continue_available"] == "0" and not save.exists(), new_game
        save.write_bytes(original)
        fresh = run(opt.exe, opt.data, "--game", "--game-script", "--save-file", save, "--fps", "native", "--frames", 150)
        assert fresh["frontend_state"] == "8" and fresh["player_health"] == "60" and fresh["ice_unlocked"] == "0" and fresh["health_fragments"] == "0", fresh
        # Corrupt files remain intact, disable Continue, and produce a recoverable startup diagnostic.
        save.write_bytes(b"corrupt checkpoint")
        corrupt = run(opt.exe, opt.data, "--game", "--save-file", save, "--fps", "native", "--frames", 48)
        assert corrupt["continue_available"] == "0" and save.read_bytes() == b"corrupt checkpoint"
        run(opt.exe, opt.data, "--game", "--continue", "--save-file", save, "--frames", 1, ok=False)
        save.unlink()
        run(opt.exe, opt.data, "--game", "--continue", "--save-file", save, "--frames", 1, ok=False)
        blocker = Path(temp) / "not-a-directory"
        blocker.write_text("file")
        failed_write = run(opt.exe, opt.data, "--movement-preview", "--checkpoint-script", "--save-file", blocker / "checkpoint.sav", "--fps", "native", "--frames", 30)
        assert failed_write["disk_checkpoint"] == "-1" and failed_write["checkpoint_restarts"] == "2" and failed_write["player_health"] == "80", failed_write
        # Automated runs without --save-file never read/write the user's desktop slot.
        isolated = run(opt.exe, opt.data, "--game", "--fps", "native", "--frames", 48)
        assert isolated["continue_available"] == "0"
    print(f"PASS: {checks} cross-process Continue runs at widths 240/569, New Game reset, corruption/missing-save handling, write-failure recovery and headless isolation")


if __name__ == "__main__":
    main()
