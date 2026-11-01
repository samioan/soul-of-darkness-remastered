"""Verify type-2 behavior 6 (the second level's sine-wave flyers): original-bytecode rows and a controlled real-record world fixture."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require

ORACLE_ROWS = 2663
SCENARIOS = 11


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    args = parser.parse_args()
    expected = (ROOT / 'soul/analysis/sine-flyer-oracle.tsv').read_text().splitlines()
    require(len(expected) == ORACLE_ROWS and len({r.split('\t')[1] for r in expected}) == SCENARIOS, 'Incomplete sine flyer oracle')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--sine-flyer-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Sine flyer row counts differ')
    for index, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Sine flyer row {index}:\nJava: {want}\nC++:  {got}')
    print(f'PASS: {ORACLE_ROWS} original-bytecode rows: behavior-6 wave, attack, hurt, death, region reset, pause, hidden and frozen across {SCENARIOS} scenarios')

    rows = {}
    names = 'regions wave reset miny maxy finalx behavior originx'.split()
    for line in subprocess.check_output([str(args.exe), '--data', str(args.data), '--sine-flyer-world-manifest'], text=True).splitlines():
        fields = line.split('\t')
        require(fields[0] == 'W' and len(fields) == 2 + len(names), f'Malformed sine flyer world row: {line}')
        rows[fields[1]] = dict(zip(names, (int(v) for v in fields[2:])))
    require(set(rows) == {'wave', 'dormant'}, 'Missing sine flyer world scenario')
    wave, dormant = rows['wave'], rows['dormant']
    require(wave['regions'] == 1 and wave['behavior'] == 6 and wave['wave'] == 0, 'wave: the flyer must enter its wave state in the first tick inside its region')
    require(wave['miny'] == -30 and wave['maxy'] == 30, 'wave: the sine amplitude must be exactly 30 pixels around the origin')
    require(wave['reset'] > wave['wave'] and wave['finalx'] == 0, 'wave: leaving the region must reset the flyer to its spawn')
    require(dormant['wave'] == -1 and dormant['reset'] == -1 and dormant['miny'] == dormant['maxy'] == 0, 'dormant: the flyer must stay idle without player overlap')
    print('PASS: 2 controlled real-record CombatWorld fixtures (UID 181 and its camera region): wave amplitude, region-exit reset and dormancy')
    print('Boundary: the controlled player is frozen and positioned; UIDs 136/137 share this handler but are not world-fixtured, and no level route was run.')


if __name__ == '__main__':
    main()
