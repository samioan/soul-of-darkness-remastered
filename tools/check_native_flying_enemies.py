"""Compare flying AI, reciprocal world combat and the second-wave enable chain."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--port-exe', type=Path)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    expected = []
    for name, count in [('flying-enemy', 2719), ('flying-world', 1728), ('removed-enemy', 208)]:
        rows = (ROOT / f'soul/analysis/{name}-oracle.tsv').read_text(encoding='utf-8').splitlines()
        require(len(rows) == count, f'Incomplete {name} oracle')
        expected += rows
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--flying-enemy-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Flying enemy row count differs')
    for i, (want, got) in enumerate(zip(expected, actual)):
        require(want == got, f'Flying enemy row {i} differs:\nJava: {want}\nC++:  {got}')
    print('PASS: 4655 original-bytecode flight, attack, contact, mixed body queue, damage, lifetime, loot and removed-source rows')
    if not a.port_exe:
        return

    def run(width, fps, seconds=30, game=False, interpolate=True, frames=None):
        mode = ['--game', '--game-second-wave-script'] if game else ['--movement-preview', '--second-wave-script']
        cmd = [str(a.port_exe), *mode, '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(frames if frames is not None else fps * seconds)]
        if not interpolate:
            cmd += ['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())

    for frames in (1, 2):
        v = run(569, 'native', frames=frames)
        require(v['wave2_locked'] == '2' and v['removed_enemy_activations'] == '0', 'Second wave enabled before final first-wave removal')
    for game in (False, True):
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    v = run(width, fps, seconds=40 if game else 30, game=game, interpolate=interpolate)
                    require(v['flying_enemies'] == '7' and v['enemies'] == '20', 'Placed enemy count differs')
                    require(v['wave1_alive'] == '0' and v['wave2_alive'] == '0' and v['wave2_locked'] == '0', 'Both arena waves did not finish')
                    require(v['player_enable_activations'] == '1' and v['removed_enemy_activations'] == '2' and v['chest_enables'] == '2' and v['arena_chests_locked'] == '0', 'Both waves must enable once, followed by both chests')
                    require(v['enemy_kills'] == '6' and int(v['enemy_hits']) >= 6 and v['player_health'] == '60', 'Real melee did not defeat both waves including type-1 respawns')
                    require(v['opened_containers'] == '0' and v['magic_unlocked'] == '0' and v['gate_script_tick'] == '-1' and v['gate_y'] == '1500', 'Combat-only fixture opened a chest without interaction')
                    if game:
                        require(v['frontend_state'] == '8', 'Startup/pause fixture did not return to gameplay')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc', 'frontend_crc')}
                    if reference is None:
                        reference = state
                    require(state == reference, 'Width/FPS/interpolation changed two-wave combat state')
                    if crc is None:
                        crc = v['movement_crc']
                    require(v['movement_crc'] == crc, 'FPS/interpolation changed arena pixels')
    print('PASS: 24 two-wave host fights at widths 240/569 and 30/60/144 FPS with interpolation on/off; six melee kills, no chest bypass')


if __name__ == '__main__':
    main()
