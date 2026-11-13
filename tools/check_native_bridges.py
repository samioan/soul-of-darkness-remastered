"""Compare native bridges, collision edits and chandelier impacts with original bytecode."""
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
    expected = (ROOT/'soul/analysis/bridge-oracle.tsv').read_text().splitlines()
    level1_cases = {f'level1-{bridge}-{mode}' for bridge in (107, 108, 109, 197)
                    for mode in ('idle', 'activate', 'fall', 'pause', 'hidden', 'miss')}
    require(len(expected) == 6360 and len({r.split('\t')[1] for r in expected}) == 35, 'Incomplete bridge oracle')
    require({r.split('\t')[1] for r in expected if r.startswith('B\tlevel1-')} == level1_cases,
            'Missing shipped level-1 bridge case')
    rows = subprocess.check_output([str(a.exe), '--data', str(a.data), '--bridge-manifest'], text=True).splitlines()
    actual = [r for r in rows if not r.startswith('R\t')]
    require(len(expected) == len(actual), 'Bridge row counts differ')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Bridge row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 6360 original-Java bridge/collision/chandelier snapshots, including 24 shipped level-1 bridge scenarios on its collision map')
    renders = [r.split('\t') for r in rows if r.startswith('R\t')]
    require(len(renders) == 288, 'Incomplete level-1 bridge render fixtures')
    centers, keys = {}, set()
    for _, name, frame, width, crc, center in renders:
        key = (name, int(frame), int(width))
        require(name in level1_cases and int(frame) in range(0, 60, 10) and int(width) in (240, 569),
                'Unexpected level-1 bridge render fixture')
        require(key not in keys, 'Duplicate level-1 bridge render fixture')
        keys.add(key)
        require(centers.setdefault((name, frame), center) == center, 'Widescreen changed central bridge pixels')
        if width == '240':
            require(crc == center, 'Portrait crop differs from its full canvas')
    print('PASS: 288 portrait/widescreen bridge surfaces, repeated drawing preserves state and pixels')
    if a.port_exe:
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    cmd = [str(a.port_exe), '--movement-preview', '--bridge-script', '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(fps*10)]
                    if not interpolate:
                        cmd += ['--no-interpolation']
                    v = dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())
                    require(v['bridges'] == '2' and v['bridges_active'] == '1', 'Released real-level chandelier failed to remove exactly one bridge')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc')}
                    if reference is None:
                        reference = state
                    require(reference == state, 'Width/FPS changed bridge simulation')
                    if crc is None:
                        crc = v['movement_crc']
                    require(crc == v['movement_crc'], 'FPS/interpolation changed bridge pixels')
        print('PASS: 12 real-level chandelier/bridge runs at widths 240/569, 30/60/144 FPS and both interpolation settings')


if __name__ == '__main__':
    main()
