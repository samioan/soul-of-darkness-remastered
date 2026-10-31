"""Compare native type-3 decisions with original bytecode; no full actor world."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    args = parser.parse_args()
    expected = (ROOT / 'soul/analysis/type3-decision-oracle.tsv').read_text().splitlines()
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--type3-decision-manifest'], text=True).splitlines()
    require(len(expected) == len(actual) == 10922, 'Incomplete type-3 decision oracle')
    for index, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Type-3 decision row {index}:\nJava: {want}\nC++: {got}')
    print('PASS: 10922 original-bytecode rows: ballistic aiming, action entries and all three AI decision variants')
    print('Boundary: isolated decisions; integrated actors are checked by check_native_type3_actors.py')


if __name__ == '__main__':
    main()
