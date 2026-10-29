"""Verify first-level checkpoint gates, restart filtering and native session restarts."""
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
    expected = (ROOT / 'soul/analysis/checkpoint-oracle.tsv').read_text(encoding='utf-8').splitlines()
    require(len(expected) == 6785 and sum(r.startswith('C\t') for r in expected) == 59, 'Incomplete checkpoint oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--checkpoint-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Checkpoint row count differs')
    for i, (want, got) in enumerate(zip(expected, actual)):
        require(want == got, f'Checkpoint row {i} differs:\nJava: {want}\nC++:  {got}')
    for row in actual:
        r = row.split('\t')
        if r[0] == 'C' and r[2] != '0':
            require(r[8:12] == ['80', '80', '17', '90'], 'Restart did not refill saved HP and retain saved MP')
    print('PASS: 6785 original-bytecode checkpoint/progression/actor-presence rows across 23 activation and repeated-restart fixtures')
    if not a.port_exe:
        return

    def run(width, fps, game=False, interpolate=True, frames=None):
        flags = ['--game', '--game-checkpoint-script'] if game else ['--movement-preview', '--checkpoint-script']
        cmd = [str(a.port_exe), *flags, '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(frames if frames is not None else fps * (60 if game else 50))]
        if not interpolate:
            cmd += ['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())

    keys = ['checkpoint_uid', 'checkpoint_activations', 'checkpoint_restarts', 'checkpoint_fixture_stage', 'player_health', 'player_magic', 'defeated']
    for frames, values in [
        (1, [-1, 0, 0, 0, 23, 17, 0]),
        (5, [11, 1, 0, 1, 23, 17, 0]),
        (20, [11, 1, 0, 2, 0, 17, 1]),
        (30, [11, 1, 2, 4, 80, 17, 0]),
        (100, [11, 1, 2, 4, 80, 27, 0]),
        (250, [11, 1, 2, 4, 80, 47, 0]),
        (600, [11, 1, 2, 4, 80, 90, 0]),
    ]:
        v = run(569, 'native', frames=frames)
        require([int(v[k]) for k in keys] == values, f'Checkpoint host milestone {frames} changed')
    for game in (False, True):
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    v = run(width, fps, game, interpolate)
                    require(v['checkpoint_uid'] == '11' and v['checkpoint_activations'] == '1' and v['checkpoint_restarts'] == '2' and v['checkpoints_active'] == '5' and v['checkpoint_fixture_stage'] == '4', 'Session did not activate and restart the same consumed checkpoint twice')
                    require(v['player_x'] == '148' and v['player_y'] == '259' and v['player_animation'] == '0' and v['player_health'] == '80' and v['player_magic'] == '90' and v['checkpoint_magic'] == '17', 'Restart position, HP refill or saved-MP recovery changed')
                    require(v['active_weapon'] == '1' and v['ice_unlocked'] == '1' and v['magic_unlocked'] == '1' and v['health_fragments'] == '2' and v['magic_fragments'] == '1', 'Session lost saved weapon/ability/fragment progression')
                    require(v['defeated'] == '0' and v['enemy_kills'] == '0' and v['gate_script_tick'] == '-1' and v['gate_y'] == '1500', 'Restart left the player defeated or bypassed unrelated progression')
                    require(v['checkpoint_menu_restarts'] == ('1' if game else '0'), 'Pause Restart Level did not use the checkpoint path')
                    if game:
                        require(v['frontend_state'] == '8', 'Game checkpoint fixture did not return to gameplay')
                    state = {k: value for k, value in v.items() if k not in ('frames', 'width', 'movement_crc', 'frontend_crc')}
                    if reference is None:
                        reference = state
                    require(state == reference, 'Width/FPS/interpolation changed checkpoint/restart state')
                    if crc is None:
                        crc = v['movement_crc']
                    require(v['movement_crc'] == crc, 'FPS/interpolation changed resumed gameplay pixels')
    print('PASS: seven milestones and 24 checkpoint/defeat/two-restart host chains at widths 240/569 across FPS/interpolation')


if __name__ == '__main__':
    main()
