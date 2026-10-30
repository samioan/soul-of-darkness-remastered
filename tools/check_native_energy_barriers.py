"""Check type-80 state, the selected trigger chain and lightning commands."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    args = parser.parse_args()
    expected = []
    for name, count in (('energy-barrier', 2410), ('barrier-chain', 640), ('lightning', 726)):
        rows = (ROOT / f'soul/analysis/{name}-oracle.tsv').read_text().splitlines()
        require(len(rows) == count, f'Incomplete {name} oracle')
        expected += rows
    rows = subprocess.check_output([str(args.exe), '--data', str(args.data), '--energy-barrier-manifest'], text=True).splitlines()
    actual = [r for r in rows if not r.startswith('H\t')]
    require(len(actual) == len(expected), 'Barrier row count differs')
    for i, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Barrier row {i}:\nJava: {want}\nC++: {got}')
    hosts = [r.split('\t') for r in rows if r.startswith('H\t')]
    require(len(hosts) == 12, 'Missing FPS/presentation fixtures')
    for width in ('240', '569'):
        group = [r for r in hosts if r[1] == width]
        require(len(group) == 6 and all(r[4:] == group[0][4:] for r in group), 'FPS/presentation changed barrier simulation or rendering')
    require(all(r[7:] == hosts[0][7:] for r in hosts), 'Logical width changed gameplay state/RNG')
    require(all(r[4] == '100' and r[18] == '0' and r[5] != r[6] for r in hosts), 'Barrier did not render and complete release in 100 ticks')
    print('PASS: 3776 original-bytecode rows: barrier lifecycle/all 10 constructors, shipped trigger chain and lightning drawing/RNG commands')
    print('PASS: 12 controlled selected-level world runs at 240/569 and 30/60/144 FPS; repeated presentation preserves state, geometry and gameplay RNG')


if __name__ == '__main__':
    main()
