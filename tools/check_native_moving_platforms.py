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
    level1_cases = {f'level1-{uid}-{mode}' for uid in (219, 241, 288)
                    for mode in ('rider', 'crouch', 'left', 'right', 'jump', 'high', 'low', 'pause')}
    require(len(expected) == 26880 and len({r.split('\t')[1] for r in expected}) == 42, 'Incomplete moving-platform oracle')
    require({r.split('\t')[1] for r in expected if r.startswith('M\tlevel1-')} == level1_cases,
            'Missing shipped level-1 moving-platform case')
    rows = subprocess.check_output([str(a.exe), '--data', str(a.data), '--moving-platform-manifest'], text=True).splitlines()
    actual = [r for r in rows if not r.startswith('R\t')]
    require(len(actual) == len(expected), 'Moving-platform row count differs')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Moving platform row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 26880 original-Java platform/player snapshots, including 24 shipped level-1 platform scenarios')
    renders = [r.split('\t') for r in rows if r.startswith('R\t')]
    require(len(renders) == 384, 'Incomplete level-1 platform render fixtures')
    centers = {}
    keys = set()
    for _, name, frame, width, crc, center in renders:
        key = (name, int(frame), int(width))
        require(name in level1_cases and int(frame) in range(0, 160, 20) and int(width) in (240, 569),
                'Unexpected level-1 platform render fixture')
        require(key not in keys, 'Duplicate level-1 platform render fixture')
        keys.add(key)
        pair = (name, frame)
        require(centers.setdefault(pair, center) == center, 'Widescreen changed central platform pixels')
        if width == '240':
            require(crc == center, 'Portrait crop differs from its full canvas')
    print('PASS: 384 portrait/widescreen platform surfaces, repeated drawing preserves state and pixels')
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
