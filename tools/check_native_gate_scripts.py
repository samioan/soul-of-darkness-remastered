"""Compare native script envelopes and selected script-165 gate execution."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require, resources
from parse_levels import scripts


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--port-exe', type=Path)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    rows = []
    for pack in range(3, 12):
        for i, script in enumerate(scripts(dict(resources(a.data/str(pack)))[1])):
            rows.append('\t'.join(map(str, ('S', pack, i, script['uid'], *script['metadata'], len(script['tracks'])))))
            for j, t in enumerate(script['tracks']):
                rows.append('\t'.join(map(str, ('T', pack, i, j, t['kind'], t['flags'], t['target'] if t['target'] is not None else -1, t['initial'], t['raw'].hex()))))
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--script-data-manifest'], text=True).splitlines()
    require(rows == actual and len(rows) == 64, 'Native script envelopes differ from Python')
    print('PASS: all 17 scripts / 47 tracks, including metadata and every track byte')
    expected = (ROOT/'soul/analysis/gate-script-oracle.tsv').read_text().splitlines()
    require(len(expected) == 1536 and len({r.split('\t')[1] for r in expected}) == 8, 'Incomplete gate script oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--gate-script-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Gate script row counts differ')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Gate script row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 1536 original-bytecode script/gate snapshots across eight timelines')
    if a.port_exe:
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolation in (True, False):
                    cmd = [str(a.port_exe), '--movement-preview', '--gate-script', '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(fps*4)]
                    if not interpolation: cmd += ['--no-interpolation']
                    v = dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())
                    require(v['gates'] == '1' and v['gate_script_loaded'] == '1', 'Gate script is not loaded in the host')
                    require((v['gate_script_tick'], v['gate_y'], v['gate_action']) == ('-2', '1415', '103'), 'Host did not finish the shipped gate timeline')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc')}
                    if reference is None: reference = state
                    require(reference == state, 'Width/FPS changed gate script host state')
                    if crc is None: crc = v['movement_crc']
                    require(crc == v['movement_crc'], 'FPS/interpolation changed gate script host pixels')
        print('PASS: 12 actual script-165/gate host runs at widths 240/569 across FPS/interpolation settings')


if __name__ == '__main__':
    main()
