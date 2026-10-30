"""Compare type-89 handler/animation states against original bytecode."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    expected = (ROOT / 'soul/analysis/timed-switch-oracle.tsv').read_text().splitlines()
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--timed-switch-manifest'], text=True).splitlines()
    require(len(expected) == len(actual) == 4153, 'Incomplete timed-switch oracle')
    for i, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Timed switch row {i}:\nJava: {want}\nC++: {got}')
    rows = [r.split('\t') for r in actual]
    cases = {r[1] for r in rows if not r[1].startswith('placed-')}
    require(len(cases) == 23, 'Missing timed-switch scenarios')
    for case in ('idle', 'ice', 'point', 'miss', 'hidden', 'frozen'):
        require(all(r[12] == '0' for r in rows if r[1] == case), f'{case} incorrectly activated')
    for case in ('hit', 'fire', 'fire-hidden', 'edge', 'mode1'):
        require(any(r[12] == '101' for r in rows if r[1] == case), f'{case} never activated')
    require(sum(r[1] == 'placed-1' for r in rows) == 3 and sum(r[1] == 'placed-4' for r in rows) == 10, 'Missing shipped switches')
    print('PASS: 4153 original-bytecode timed-switch rows: 23 handler/animation timelines and all 13 placed constructors')


if __name__ == '__main__':
    main()
