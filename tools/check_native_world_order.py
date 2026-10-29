"""Check actual original ordered region/enemy world ticks against the native owner."""
import argparse
from collections import Counter
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    expected = (ROOT/'soul/analysis/world-order-oracle.tsv').read_text().splitlines()
    require(len(expected) == 3840 and len({r.split('\t')[1] for r in expected}) == 12, 'Incomplete world-order oracle')
    require(Counter(r.split('\t')[0] for r in expected) == dict(P=960, E=960, E2=960, W=960), 'Incomplete ordered-world actor/queue coverage')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--world-order-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'World-order row counts differ')
    for i, (j, n) in enumerate(zip(expected, actual), 1):
        require(j == n, f'World-order row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 3840 original-world actor/region/queue snapshots over 480 ticks across twelve ordering scenarios')


if __name__ == '__main__':
    main()
