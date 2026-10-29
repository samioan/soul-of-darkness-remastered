"""Verify type-33 movement against original Java and the real first-level ride."""
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
    expected = (ROOT/'soul/analysis/moving-platform-oracle.tsv').read_text().splitlines()
    require(len(expected) == 11520 and len({r.split('\t')[1] for r in expected}) == 18, 'Incomplete moving-platform oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--moving-platform-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Moving-platform row count differs')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Moving platform row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 11520 original-Java platform/player snapshots over 2880 ticks across eighteen scenarios')
    if a.port_exe:
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    cmd = [str(a.port_exe), '--movement-preview', '--platform-script', '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(fps*10)]
                    if not interpolate: cmd += ['--no-interpolation']
                    v = dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())
                    require(v['platforms'] == '7' and v['supported'] == '1' and v['platform_y'] == '1270' and v['platform_vy'] == '-1280', 'Real-level ride failed')
                    require(v['player_y'] == '1251' and v['player_health'] == '60' and v['player_hazard'] == '0', 'Rider state differs')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc')}
                    if reference is None: reference = state
                    require(reference == state, 'Width/FPS changed platform state')
                    if crc is None: crc = v['movement_crc']
                    require(crc == v['movement_crc'], 'FPS/interpolation changed platform pixels')
        print('PASS: 12 real-level platform rides at widths 240/569, 30/60/144 FPS and both interpolation settings')


if __name__ == '__main__': main()
