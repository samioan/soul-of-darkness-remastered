"""Compare type-50 controllers, support and falling impacts with original bytecode."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--port-exe', type=Path)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    expected = (ROOT/'soul/analysis/swinging-platform-oracle.tsv').read_text().splitlines()
    require(len(expected) == 15840 and len({r.split('\t')[1] for r in expected}) == 15, 'Incomplete swinging-platform oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--swinging-platform-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Swinging-platform row count differs')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Swinging platform row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 15840 original-Java snapshots over 3600 ticks across fifteen swinging/support/cut/fall/impact scenarios')
    if a.port_exe:
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    cmd = [str(a.port_exe), '--movement-preview', '--swing-script', '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(fps*10)]
                    if not interpolate: cmd += ['--no-interpolation']
                    v = dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())
                    require(v['platforms'] == '7' and v['support_uid'] == '4' and v['supported'] == '1', 'Real-level swinging ride lost support or platforms')
                    require(v['linked_area_triggers'] == '1', 'First-level linked area trigger was not loaded')
                    require((v['swing_x'], v['swing_y'], v['swing_angle'], v['swing_state']) == ('417', '1039', '131072', '101'), 'Real pendulum motion differs')
                    require(v['player_x'] == '431' and v['player_y'] == '1039' and v['player_health'] == '60', 'Swing rider state differs')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc')}
                    if reference is None: reference = state
                    require(reference == state, 'Width/FPS changed swinging state')
                    if crc is None: crc = v['movement_crc']
                    require(crc == v['movement_crc'], 'FPS/interpolation changed swinging pixels')
        print('PASS: 12 real-level swinging rides at widths 240/569, 30/60/144 FPS and both interpolation settings')


if __name__ == '__main__': main()
