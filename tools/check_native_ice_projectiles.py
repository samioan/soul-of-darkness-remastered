"""Verify ice casting, enemy hits, wall footholds, replacement and rider cleanup."""
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
    for name, count in [('ice-projectile', 6840), ('ice-magic', 10762)]:
        rows = (ROOT / f'soul/analysis/{name}-oracle.tsv').read_text(encoding='utf-8').splitlines()
        require(len(rows) == count, f'Incomplete {name} oracle')
        expected += rows
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--ice-projectile-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Ice projectile row count differs')
    for i, (want, got) in enumerate(zip(expected, actual)):
        require(want == got, f'Ice projectile row {i} differs:\nJava: {want}\nC++:  {got}')
    print('PASS: 17602 original-bytecode ice projectile/casting/enemy/platform/rider/random rows across forty arenas')
    if not a.port_exe:
        return

    def run(width, fps, game=False, interpolate=True, frames=None):
        flags = ['--game', '--game-ice-projectile-script'] if game else ['--movement-preview', '--ice-projectile-script']
        cmd = [str(a.port_exe), *flags, '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(frames if frames is not None else fps * (60 if game else 50))]
        if not interpolate:
            cmd += ['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())

    keys = ['ice_shots', 'ice_hits', 'ice_platforms', 'ice_landings', 'ice_detachments', 'ice_riding_ticks', 'ice_fixture_stage', 'player_magic', 'player_y']
    for frames, values in [
        (70, [1, 0, 0, 0, 0, 0, 1, 45, 259]),
        (85, [1, 1, 0, 0, 0, 0, 2, 45, 259]),
        (130, [2, 1, 1, 0, 0, 0, 4, 30, 208]),
        (150, [2, 1, 1, 1, 0, 13, 5, 30, 207]),
        (170, [3, 1, 1, 1, 1, 29, 7, 15, 207]),
        (200, [3, 1, 2, 1, 1, 29, 7, 15, 259]),
        (600, [3, 1, 2, 1, 1, 29, 7, 60, 259]),
    ]:
        v = run(569, 'native', frames=frames)
        require([int(v[k]) for k in keys] == values, f'Ice projectile milestone {frames} changed')
    for game in (False, True):
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    v = run(width, fps, game, interpolate)
                    require(v['ice_shots'] == '3' and v['ice_hits'] == '1' and v['enemy_kills'] == '1' and v['enemy_hits'] == '0', 'Ice shot did not defeat the ordinary enemy')
                    require(v['ice_platforms'] == '2' and v['ice_projectiles_active'] == '0' and v['ice_fixture_stage'] == '7', 'Wall freezing/replacement/expiry chain did not finish')
                    require(v['ice_landings'] == '1' and v['ice_detachments'] == '1' and v['ice_riding_ticks'] == '29' and v['ice_ride_y'] == '207', 'Natural jump onto the ice foothold or melt detachment changed')
                    require(v['player_y'] == '259' and v['supported'] == '0' and v['player_animation'] == '0' and v['player_health'] == '60' and v['player_magic'] == '60', 'Player did not return to the floor with recovered MP')
                    require(v['opened_containers'] == '1' and v['ice_unlocked'] == '1' and v['active_weapon'] == '1' and v['weapon_switches'] == '1' and v['ability_completed'] == '1', 'Ordinary chest/reward/tutorial/equip chain changed')
                    require(v['fire_casts'] == '0' and v['ice_attacks'] == '0' and v['melee_attacks'] == '0' and v['gate_script_tick'] == '-1' and v['gate_y'] == '1500', 'Fixture bypassed unrelated combat or gate progression')
                    if game:
                        require(v['frontend_state'] == '8', 'Startup/pause fixture did not return to gameplay')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc', 'frontend_crc')}
                    if reference is None:
                        reference = state
                    require(state == reference, 'Width/FPS/interpolation changed ice projectile state')
                    if crc is None:
                        crc = v['movement_crc']
                    require(v['movement_crc'] == crc, 'FPS/interpolation changed ice gameplay pixels')
    print('PASS: seven milestones and 24 ice-shot/wall/jump/rider/replacement host chains at widths 240/569 across FPS/interpolation')


if __name__ == '__main__':
    main()
