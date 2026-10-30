"""Verify type-16 projectile state and controlled native-world ownership."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    expected = (ROOT / 'soul/analysis/thrown-object-oracle.tsv').read_text().splitlines()
    rows = subprocess.check_output([str(a.exe), '--data', str(a.data), '--thrown-object-manifest'], text=True).splitlines()
    actual = [r for r in rows if not r.startswith('H\t')]
    require(len(expected) == len(actual) == 6507, 'Incomplete thrown-object oracle')
    for i, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Thrown-object row {i}:\nJava: {want}\nC++: {got}')
    hosts = [r.split('\t') for r in rows if r.startswith('H\t')]
    require(len(hosts) == 12, 'Missing world/FPS fixtures')
    for width in ('240', '569'):
        group = [r for r in hosts if r[1] == width]
        require(len(group) == 6 and all(r[4:] == group[0][4:] for r in group), 'FPS/presentation changed projectile state or rendering')
    require(all(r[4:10] + r[12:] == hosts[0][4:10] + hosts[0][12:] for r in hosts), 'Width changed projectile simulation/RNG')
    require(all(r[4:8] == ['100', '0', '0', '0'] and r[10] != r[11] for r in hosts), 'Projectile did not render, release and leave the world cleanly')
    print('PASS: 6507 original-bytecode rows over 27 type-16 flight/slope/reflection/explosion/target/holder scenarios')
    print('PASS: 12 selected-level world fixtures at 240/569 and 30/60/144 FPS; repeated presentation preserves state and RNG')


if __name__ == '__main__':
    main()
