"""Compare native side contacts and player integration with original bytecode."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/contact-oracle.tsv')
    args = parser.parse_args()
    expected = args.oracle.read_text(encoding='utf-8').splitlines()
    require(sum(row.startswith('C\t') for row in expected) == 42336, 'Incomplete contact arenas')
    require(sum(row.startswith('U\t') for row in expected) == 768, 'Incomplete player contact ticks')
    require({row.split('\t')[1] for row in expected if row.startswith('B\t')} == {'0', '2', '3'}, 'Missing player/enemy contact bounds')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--contact-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), f'Contact row counts differ: {len(actual)} != {len(expected)}')
    for index, (java, native) in enumerate(zip(expected, actual), 1):
        require(java == native, f'Contact row {index}:\nJava: {java}\nC++: {native}')
    bounds = sum(row.startswith('B\t') for row in actual)
    print(f'PASS: {bounds} contact bounds, 7056 original-bytecode contact arenas and 768 snapshots over 384 player ticks')


if __name__ == '__main__':
    main()
