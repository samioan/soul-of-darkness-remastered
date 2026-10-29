"""Compare controlled native combat with unmodified original bytecode."""
import argparse
from collections import Counter
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/combat-oracle.tsv')
    args = parser.parse_args()
    expected = args.oracle.read_text(encoding='utf-8').splitlines()
    counts = Counter(row.split('\t')[0] for row in expected)
    require(counts == dict(P=622, E=504, D=324, F=286, W=256, S=227, R=180, N=144, G=48), 'Incomplete combat oracle')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--combat-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Combat row counts differ')
    for index, (java, native) in enumerate(zip(expected, actual), 1):
        require(java == native, f'Combat row {index}:\nJava: {java}\nC++: {native}')
    print('PASS: 2591 original-bytecode combat rows: damage/RNG, hit guards, enemy/player reactions, death entries and effects')


if __name__ == '__main__':
    main()
