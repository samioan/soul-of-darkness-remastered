"""Verify flying-removal chest reveals and the full native arena/reward/gate chain."""
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
    expected = (ROOT / 'soul/analysis/chest-unlock-oracle.tsv').read_text(encoding='utf-8').splitlines()
    require(len(expected) == 2247, 'Incomplete chest-unlock oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--chest-unlock-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Chest unlock row count differs')
    for i, (want, got) in enumerate(zip(expected, actual)):
        require(want == got, f'Chest unlock row {i} differs:\nJava: {want}\nC++:  {got}')
    print('PASS: 2247 original-bytecode chest/trigger/reveal-effect/random rows across seven actual world arenas')
    if not a.port_exe:
        return

    def run(width, fps, seconds=30, game=False, interpolate=True, frames=None):
        mode = ['--game', '--game-arena-script'] if game else ['--movement-preview', '--arena-script']
        cmd = [str(a.port_exe), *mode, '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(frames if frames is not None else fps * seconds)]
        if not interpolate:
            cmd += ['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())

    for frames, kills, enables, opened, magic, modal, completed, gate in [
        (1, '0', '0', '0', '0', '-1', '0', '-1'),
        (140, '4', '0', '0', '0', '-1', '0', '-1'),
        (180, '5', '0', '0', '0', '-1', '0', '-1'),
        (200, '6', '2', '1', '1', '3', '0', '-1'),
        (240, '6', '2', '1', '1', '14', '0', '-1'),
        (260, '6', '2', '1', '1', '-1', '1', '0'),
        (320, '6', '2', '1', '1', '-1', '1', '-2'),
    ]:
        v = run(569, 'native', frames=frames)
        require([v[k] for k in ['enemy_kills', 'chest_enables', 'opened_containers', 'magic_unlocked', 'ability_modal', 'ability_completed', 'gate_script_tick']] == [kills, enables, opened, magic, modal, completed, gate], f'Arena milestone {frames} changed')
    for game in (False, True):
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    v = run(width, fps, seconds=50 if game else 40, game=game, interpolate=interpolate)
                    require(v['flying_enemies'] == '7' and v['enemies'] == '20', 'Placed enemy count differs')
                    require(v['wave1_alive'] == '0' and v['wave2_alive'] == '0' and v['wave2_locked'] == '0', 'Both arena waves did not finish')
                    require(v['player_enable_activations'] == '1' and v['removed_enemy_activations'] == '2' and v['chest_enables'] == '2' and v['arena_chests_locked'] == '0', 'Both waves must enable once, followed by both chests')
                    require(v['enemy_kills'] == '6' and int(v['enemy_hits']) >= 6 and v['player_health'] == '60', 'Real melee did not defeat both waves including type-1 respawns')
                    require(v['opened_containers'] == '1' and v['magic_unlocked'] == '1' and v['ability_unlocks'] == '1' and v['ability_completed'] == '1' and v['abilities_active'] == '0', 'Chest interaction did not grant and complete magic reward')
                    require(v['gate_triggers_active'] == '0' and v['gate_script_tick'] == '-2' and v['gate_y'] == '1415' and v['gate_action'] == '103', 'Actual chest removal did not open the gate and finish its controller')
                    if game:
                        require(v['frontend_state'] == '8', 'Startup/pause fixture did not return to gameplay')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc', 'frontend_crc')}
                    if reference is None:
                        reference = state
                    require(state == reference, 'Width/FPS/interpolation changed two-wave combat state')
                    if crc is None:
                        crc = v['movement_crc']
                    require(v['movement_crc'] == crc, 'FPS/interpolation changed arena pixels')
    print('PASS: 24 arena/chest/magic/tutorial/gate host chains at widths 240/569, 30/60/144 FPS and interpolation on/off; normal enemy removal and chest interaction')


if __name__ == '__main__':
    main()
