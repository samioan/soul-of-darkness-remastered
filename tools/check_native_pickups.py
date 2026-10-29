"""Compare the native health/magic pickup subset with original Java snapshots."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/pickup-oracle.tsv')
    args = parser.parse_args()
    expected = args.oracle.read_text(encoding='utf-8').splitlines()
    require({row.split('\t')[1] for row in expected} ==
            {'health', 'magic', 'cap', 'far', 'moving', 'paused', 'near', 'dropped', 'frozen'},
            'Incomplete pickup oracle scenarios; regenerate Java checks')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--pickup-manifest'], text=True).splitlines()
    require(len(expected) == len(actual), f'Pickup row count: Java {len(expected)}, native {len(actual)}')
    for index, (a, b) in enumerate(zip(expected, actual), 1):
        require(a == b, f'Pickup mismatch at row {index}:\nJava: {a}\nC++:  {b}')
    print(f'PASS: {len(actual)} original-Java pickup snapshots across nine controlled scenarios')


if __name__ == '__main__':
    main()
