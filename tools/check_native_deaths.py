"""Compare ordinary enemy death/reset and delayed loot with original bytecode."""
import argparse
from collections import Counter
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/death-oracle.tsv')
    args = parser.parse_args()
    expected = args.oracle.read_text(encoding='utf-8').splitlines()
    require(Counter(row.split('\t')[0] for row in expected) == dict(E=1760, K=557, N=155, L=109, F=58), 'Incomplete death/loot oracle')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--death-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Death/loot row counts differ')
    for index, (java, native) in enumerate(zip(expected, actual), 1):
        require(java == native, f'Death row {index}:\nJava: {java}\nC++: {native}')
    print('PASS: 2639 original-bytecode death/loot rows: selection, record reset, respawn fade, delayed pickups and effects')


if __name__ == '__main__':
    main()
