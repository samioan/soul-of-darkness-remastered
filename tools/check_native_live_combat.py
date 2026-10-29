"""Check connected player/enemy combat against original world ticks and native previews."""
import argparse
from collections import Counter
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--port-exe', type=Path)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    p.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/live-combat-oracle.tsv')
    a = p.parse_args()
    expected = a.oracle.read_text(encoding='utf-8').splitlines()
    require(Counter(r.split('\t')[0] for r in expected) == dict(P=2104, E=2104, E2=240, H=12), 'Incomplete live-combat oracle')
    require(len({r.split('\t')[1] for r in expected}) == 13, 'Incomplete combat/hazard scenarios')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--live-combat-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Live combat row counts differ')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Live combat row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 4448 original-world-update combat/hazard snapshots and 12 birth-effect rows across thirteen scenarios')
    if a.port_exe:
        for script in ('combat', 'defeat'):
            reference = None
            for width in (240, 569):
                checksum = None
                for fps in (30, 60, 144):
                    for interpolation in (True, False):
                        cmd = [str(a.port_exe), '--movement-preview', f'--{script}-script', '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(fps*10)]
                        if not interpolation: cmd += ['--no-interpolation']
                        v = dict(item.split('=') for item in subprocess.check_output(cmd, text=True).split())
                        require(v['enemies'] == '20' and v['dormant_enemies'] == '0' and v['player_hazard'] == '0', 'Enemy loading/hazard boundary differs')
                        if script == 'combat':
                            require(v['enemy_kills'] == '1' and v['enemy_hits'] == '1' and v['loot_spawned'] == '1' and v['player_collected'] == '1', 'Host failed to kill enemy and collect loot')
                            require(v['player_health'] == '60' and v['player_hits'] == '0' and v['defeated'] == '0', 'Successful fight ended incorrectly')
                        else:
                            require(v['defeated'] == '1' and v['player_health'] == '0' and v['player_animation'] == '9' and v['player_hits'] == '1', 'Host failed to reach defeat')
                        require(v['weapon_visible'] == '0', 'Slash remains visible after combat')
                        state = {k: val for k, val in v.items() if k != 'movement_crc' and k not in ('frames', 'width')}
                        if reference is None: reference = state
                        require(state == reference, 'FPS/width changed live combat state')
                        if checksum is None: checksum = v['movement_crc']
                        require(checksum == v['movement_crc'], 'FPS/interpolation changed combat pixels')
        print('PASS: 24 first-level kill/loot/defeat runs at widths 240/569, 30/60/144 FPS and both interpolation settings')


if __name__ == '__main__': main()
