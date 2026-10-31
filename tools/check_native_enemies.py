"""Compare the controlled native type-1 AI slice with original bytecode."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/enemy-oracle.tsv')
    args = parser.parse_args()
    expected = args.oracle.read_text(encoding='utf-8').splitlines()
    require(len(expected) == 4224, 'Incomplete type-1 AI oracle')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--enemy-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Enemy row counts differ')
    for index, (java, native) in enumerate(zip(expected, actual), 1):
        require(java == native, f'Enemy row {index}:\nJava: {java}\nC++: {native}')
    print('PASS: 4224 original-bytecode enemy snapshots over 2112 ticks in 22 controlled AI arenas')


if __name__ == '__main__':
    main()
