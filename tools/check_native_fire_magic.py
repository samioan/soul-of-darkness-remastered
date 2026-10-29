"""Compare original fire casting, radial effects, damage, MP recovery and live controls."""
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
    expected = (ROOT / 'soul/analysis/fire-magic-oracle.tsv').read_text(encoding='utf-8').splitlines()
    require(len(expected) == 4790, 'Incomplete fire magic oracle')
    require(sum(r.startswith('P\t') for r in expected) == 3360 and sum(r.startswith('B\t') for r in expected) == 936 and sum(r.startswith('E\t') for r in expected) == 480, 'Missing player, pulse or reciprocal enemy cases')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--fire-magic-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Fire magic row count differs')
    for i, (want, got) in enumerate(zip(expected, actual)):
        require(want == got, f'Fire magic row {i} differs:\nJava: {want}\nC++:  {got}')
    print('PASS: 4790 original-bytecode player/charge/pulse/enemy/MP/random rows across fourteen arenas')
    if not a.port_exe:
        return

    def run(width, fps, mode='direct', interpolate=True, frames=None):
        flags = ['--movement-preview', '--fire-script'] if mode == 'direct' else ['--game', '--game-fire-script' if mode == 'game' else '--game-arena-fire-script']
        seconds = 30 if mode == 'direct' else 40 if mode == 'game' else 50
        cmd = [str(a.port_exe), *flags, '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(frames if frames is not None else fps * seconds)]
        if not interpolate:
            cmd += ['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())

    for frames, casts, active, mp in [(10, '0', '0', '60'), (11, '1', '6', '45'), (110, '4', '0', '0'), (126, '4', '0', '0'), (162, '4', '0', '10'), (237, '5', '6', '5'), (361, '5', '0', '25')]:
        v = run(569, 'native', frames=frames)
        require([v[k] for k in ('fire_casts', 'fire_active', 'player_magic')] == [casts, active, mp], f'Fire control/MP milestone {frames} changed')
        if frames == 126:
            require(int(v['fire_warning']) > 0, 'Insufficient MP did not notify the player')
    for mode in ('direct', 'game', 'arena'):
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    v = run(width, fps, mode, interpolate)
                    require(v['fire_active'] == '0' and v['fire_charge'] == '-1' and v['player_health'] == '60' and v['player_animation'] == '0', 'Casting did not finish and return to live gameplay')
                    if mode == 'arena':
                        require(v['fire_casts'] == '1' and v['ability_unlocks'] == '1' and v['ability_completed'] == '1' and v['chest_enables'] == '2' and v['opened_containers'] == '1' and v['enemy_kills'] == '6', 'Arena reward did not unlock a normally cast spell')
                        require(v['gate_script_tick'] == '-2' and v['gate_y'] == '1415' and v['player_magic'] == '60', 'Post-arena spell did not preserve the opened gate or recover MP')
                    else:
                        require(v['fire_casts'] == '5' and int(v['fire_hits']) >= 1 and v['enemy_kills'] == '1' and v['enemy_hits'] == '0' and v['melee_attacks'] == '0', 'Fire controls did not defeat the ordinary enemy without physical melee')
                        require(v['opened_containers'] == '0' and v['gate_script_tick'] == '-1' and v['player_magic'] == '25', 'Casting fixture changed unrelated progression or MP recovery')
                    if mode != 'direct':
                        require(v['frontend_state'] == '8', 'Game fixture did not return from startup/pause to gameplay')
                    state = {k: value for k, value in v.items() if k not in ('frames', 'width', 'movement_crc', 'frontend_crc')}
                    if reference is None:
                        reference = state
                    require(state == reference, 'Width/FPS/interpolation changed fire combat or MP state')
                    if crc is None:
                        crc = v['movement_crc']
                    require(v['movement_crc'] == crc, 'FPS/interpolation changed fire gameplay pixels')
    print('PASS: 36 live fire/MP and normal arena-unlock-to-casting runs at widths 240/569, 30/60/144 FPS and interpolation on/off')


if __name__ == '__main__':
    main()
