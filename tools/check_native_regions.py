"""Compare isolated region selection and ordinary enemy reset with original bytecode."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    p.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/region-oracle.tsv')
    a = p.parse_args()
    expected = a.oracle.read_text(encoding='utf-8').splitlines()
    require(len(expected) == 176 and len({r.split('\t')[1] for r in expected}) == 8, 'Incomplete region oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--region-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Region row counts differ')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Region row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 176 original-Java region selection/reset snapshots across eight scenarios')


if __name__ == '__main__':
    main()
