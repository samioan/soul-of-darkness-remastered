"""Check original region-sweep decisions and live transient-owner cleanup."""
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
    expected = (ROOT/'soul/analysis/transient-oracle.tsv').read_text().splitlines()
    require(len(expected) == 576, 'Incomplete transient oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--transient-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Transient row counts differ')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Transient row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 576 original-Java transient-removal decisions across change, same-region and death boundaries')
    if a.port_exe:
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    cmd = [str(a.port_exe), '--movement-preview', '--region-script', '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(fps*2)]
                    if not interpolate:
                        cmd += ['--no-interpolation']
                    v = dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())
                    require(v['region_pickups_removed'] == '2' and v['region_effects_removed'] == '1', 'Region transition failed to sweep existing pickups/effect')
                    require(v['spawned_pickups_active'] == '1', 'A pickup born after the sweep failed to survive')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc')}
                    if reference is None:
                        reference = state
                    require(reference == state, 'Width/FPS changed region-cleanup state')
                    if crc is None:
                        crc = v['movement_crc']
                    require(crc == v['movement_crc'], 'FPS/interpolation changed cleanup pixels')
        print('PASS: 12 first-level transition/cleanup/new-birth runs at widths 240/569 and 30/60/144 FPS, interpolation on/off')


if __name__ == '__main__':
    main()
