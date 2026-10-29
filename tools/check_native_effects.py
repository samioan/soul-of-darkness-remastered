"""Compare native collection-effect lifetime/geometry with original Java bytecode."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/effect-oracle.tsv')
    args = parser.parse_args()
    expected = args.oracle.read_text(encoding='utf-8').splitlines()
    require(len(expected) == 88, 'Incomplete effect oracle: expected 88 snapshots')
    cases = {tuple(row.split('\t')[1:4]) for row in expected}
    require(len(cases) == 24, 'Incomplete effect oracle: expected 24 scenarios')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--effect-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), f'Effect row counts differ: {len(actual)} != {len(expected)}')
    for index, (java, native) in enumerate(zip(expected, actual), 1):
        require(java == native, f'Effect row {index}:\nJava: {java}\nC++: {native}')
    print(f'PASS: {len(expected)} original-bytecode effect snapshots across 24 scenarios; no phone pixel claim')


if __name__ == '__main__':
    main()
