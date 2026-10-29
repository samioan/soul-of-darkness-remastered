"""Compare native platform landing/support transitions with original Java."""
import argparse
from collections import Counter
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/platform-oracle.tsv')
    args = parser.parse_args()
    expected = args.oracle.read_text(encoding='utf-8').splitlines()
    cases = ('stand', 'jump', 'walk', 'crouch', 'melee', 'combo', 'rise', 'sink', 'slide', 'pause', 'swing', 'swing_jump')
    require(Counter(r.split('\t')[1] for r in expected) == dict(matrix=8640, **{s: 192 for s in cases}), 'Incomplete platform oracle')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--platform-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Platform row count differs')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Platform row {i}:\nJava: {j}\nC++: {n}')
    by_case = {s: [r.split('\t') for r in actual if r.split('\t')[1] == s] for s in cases}
    require({'13', '14'} <= {r[13] for r in by_case['combo']}, 'Supported combo failed')
    require(all(r[13] != '15' for r in by_case['melee']), 'Supported melee selected aerial pose')
    require(by_case['stand'][-1][7] == '161' and by_case['swing'][-1][7] == '160', 'Platform type-specific standing height differs')
    for s in ('jump', 'walk', 'swing_jump', 'slide'):
        links = [r[-2] for r in by_case[s]]
        first = links.index('1')
        require('0' in links[first:], f'{s} never detached')
    print('PASS: 10944 original-Java platform snapshots: 4320 contact setups and 1152 player ticks across twelve scenarios')


if __name__ == '__main__':
    main()
