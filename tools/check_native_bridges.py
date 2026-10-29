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
    require(len(expected) == 2040 and len({r.split('\t')[1] for r in expected}) == 11, 'Incomplete bridge oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--bridge-manifest'], text=True).splitlines()
    require(len(expected) == len(actual), 'Bridge row counts differ')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Bridge row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 2040 original-Java bridge/collision/chandelier snapshots across eleven scenarios')
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
