"""Verify the ice chest/pickup, physical ice weapon, switch gates and live controls."""
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
    for name, count in [('ice-pickup', 1498), ('ice-weapon', 2880), ('ice-world', 1728)]:
        rows = (ROOT / f'soul/analysis/{name}-oracle.tsv').read_text(encoding='utf-8').splitlines()
        require(len(rows) == count, f'Incomplete {name} oracle')
        expected += rows
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--ice-weapon-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Ice weapon row count differs')
    for i, (want, got) in enumerate(zip(expected, actual)):
        require(want == got, f'Ice weapon row {i} differs:\nJava: {want}\nC++:  {got}')
    print('PASS: 6106 original-bytecode ice chest/pickup, melee/combo/switch, reciprocal enemy and mixed-queue rows')
    if not a.port_exe:
        return

    def run(width, fps, seconds=30, game=False, interpolate=True, frames=None):
        mode = ['--game', '--game-ice-script'] if game else ['--movement-preview', '--ice-script']
        cmd = [str(a.port_exe), *mode, '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(frames if frames is not None else fps * seconds)]
        if not interpolate:
            cmd += ['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())

    for frames, opened, owned, weapon, modal, completed, mp, switches, kills in [
        (1, '0', '0', '0', '-1', '0', '30', '0', '0'),
        (8, '1', '1', '0', '3', '0', '30', '0', '0'),
        (55, '1', '1', '0', '-1', '1', '30', '0', '0'),
        (70, '1', '1', '1', '-1', '1', '30', '1', '0'),
        (100, '1', '1', '1', '-1', '1', '30', '1', '1'),
        (150, '1', '1', '0', '-1', '1', '40', '2', '1'),
        (250, '1', '1', '0', '-1', '1', '60', '2', '1'),
    ]:
        v = run(569, 'native', frames=frames)
        require([v[k] for k in ['opened_containers', 'ice_unlocked', 'active_weapon', 'ability_modal', 'ability_completed', 'player_magic', 'weapon_switches', 'enemy_kills']] == [opened, owned, weapon, modal, completed, mp, switches, kills], f'Ice pickup/control milestone {frames} changed')
    for game in (False, True):
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    v = run(width, fps, seconds=50 if game else 40, game=game, interpolate=interpolate)
                    require(v['opened_containers'] == '1' and v['ice_unlocked'] == '1' and v['abilities_active'] == '0' and v['ability_completed'] == '1', 'Ice chest/reward/tutorial did not finish')
                    require(v['weapon_switches'] == '2' and v['active_weapon'] == '0' and v['weapon_switch_cooldown'] == '-48', 'Fixture did not equip ice then return to fire through normal switch input')
                    require(v['enemy_kills'] == '1' and int(v['enemy_hits']) >= 1 and int(v['ice_attacks']) >= 2 and v['ice_poses'] == '3' and v['melee_attacks'] == '0', 'Ice melee did not defeat the ordinary enemy and demonstrate both ground strikes')
                    require(v['player_health'] == '60' and v['player_magic'] == '60' and v['ability_unlocks'] == '0' and v['fire_casts'] == '0', 'Ice pickup changed magic ownership, cast fire or damaged fixture player')
                    require(v['gate_script_tick'] == '-1' and v['chest_enables'] == '0' and v['gate_y'] == '1500', 'Ice fixture bypassed unrelated arena/gate progression')
                    if game:
                        require(v['frontend_state'] == '8', 'Startup/pause fixture did not return to gameplay')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc', 'frontend_crc')}
                    if reference is None:
                        reference = state
                    require(state == reference, 'Width/FPS/interpolation changed ice weapon state')
                    if crc is None:
                        crc = v['movement_crc']
                    require(v['movement_crc'] == crc, 'FPS/interpolation changed ice gameplay pixels')
    print('PASS: 24 relocated second-level ice-chest/pickup/tutorial/switch/melee host chains at widths 240/569 across FPS/interpolation')


if __name__ == '__main__':
    main()
