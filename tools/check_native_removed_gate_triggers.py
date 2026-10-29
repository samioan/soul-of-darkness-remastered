"""Verify removed-source trigger 166 and its selected gate-script lifecycle."""
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
    expected = (ROOT/'soul/analysis/removed-gate-trigger-oracle.tsv').read_text().splitlines()
    require(len(expected) == 1536 and len({r.split('\t')[1] for r in expected}) == 8, 'Incomplete removed-source trigger oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--removed-gate-trigger-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Removed-source trigger row counts differ')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Removed-source row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 1536 original-bytecode removed-source/selected-script/gate snapshots across eight scenarios')
    if a.port_exe:
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolation in (True, False):
                    cmd = [str(a.port_exe), '--movement-preview', '--gate-trigger-script', '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(fps*4)]
                    if not interpolation: cmd += ['--no-interpolation']
                    v = dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())
                    require((v['gate_triggers'], v['gate_triggers_active']) == ('1', '0'), 'Removed-source trigger did not finish its lifecycle')
                    require((v['gate_script_tick'], v['gate_y'], v['gate_action']) == ('-2', '1415', '103'), 'Source removal did not complete the gate timeline')
                    require(v['opened_containers'] == '0', 'Controlled removal fixture must not claim the unported chest reward')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc')}
                    if reference is None: reference = state
                    require(reference == state, 'Width/FPS changed removed-source host state')
                    if crc is None: crc = v['movement_crc']
                    require(crc == v['movement_crc'], 'FPS/interpolation changed removed-source host pixels')
        print('PASS: 12 first-level controlled container-removal/trigger/gate runs at widths 240/569 across FPS/interpolation settings')


if __name__ == '__main__':
    main()
