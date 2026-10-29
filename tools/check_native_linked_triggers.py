"""Compare the supported type-40 area trigger with original-bytecode snapshots."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    expected = (ROOT/'soul/analysis/linked-trigger-oracle.tsv').read_text().splitlines()
    require(len(expected) == 512 and sum(r.startswith('T\t') for r in expected) == 384 and sum(r.startswith('Q\t') for r in expected) == 128, 'Incomplete linked trigger oracle')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--linked-trigger-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Linked trigger row count differs')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'Linked trigger row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 384 original-bytecode area-trigger snapshots and 128 original-world queue snapshots')


if __name__ == '__main__':
    main()
