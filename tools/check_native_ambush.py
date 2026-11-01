"""Verify the second-level ambush scripts 23/24/114: original-bytecode handler rows and controlled world fixtures."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require

ORACLE_ROWS = 12800
SCENARIOS = 12


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    args = parser.parse_args()
    expected = (ROOT / 'soul/analysis/ambush-oracle.tsv').read_text().splitlines()
    require(len(expected) == ORACLE_ROWS and len({r.split('\t')[1] for r in expected}) == SCENARIOS, 'Incomplete ambush oracle')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--ambush-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Ambush row counts differ')
    for index, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Ambush row {index}:\nJava: {want}\nC++:  {got}')
    print(f'PASS: {ORACLE_ROWS} original-bytecode rows: scripts 23/24/114, enemies 92/93/94, chandeliers 89/90/91 and triggers 115/118/123 across {SCENARIOS} scenarios')

    names = ('triggers scripts thrown background maxslots finalslots').split()
    chain = 'uid start end starts finishes frozen enemyaction chandelieraction chandeliery alive'.split()
    rows = {}
    for line in subprocess.check_output([str(args.exe), '--data', str(args.data), '--ambush-world-manifest'], text=True).splitlines():
        fields = line.split('\t')
        require(fields[0] == 'W' and len(fields) == 2 + len(names) + 2 * len(chain), f'Malformed ambush world row: {line}')
        values = [int(v) for v in fields[2:]]
        row = dict(zip(names, values))
        row['chains'] = [dict(zip(chain, values[len(names) + i * len(chain):len(names) + (i + 1) * len(chain)])) for i in range(2)]
        rows[fields[1]] = row
    require(set(rows) == {'s23', 's24', 's114', 'pair', 'paused', 'stagger', 'no-overlap'}, 'Missing ambush world scenario')
    for name, uid in (('s23', 23), ('s24', 24), ('s114', 114), ('paused', 23)):
        r, c = rows[name], rows[name]['chains'][0]
        require(r['triggers'] == r['scripts'] == 1 and r['thrown'] == -1 and r['maxslots'] == 1 and r['finalslots'] == 0, f'{name}: slot bookkeeping')
        delay = 30 if name == 'paused' else 0
        require(c['uid'] == uid and c['start'] == delay and c['end'] == delay + 9 and c['starts'] == c['finishes'] == 1, f'{name}: script timing')
        require(c['frozen'] == 0 and c['enemyaction'] == 101 and (c['chandelieraction'], c['chandeliery']) == (104, 20) and c['alive'] == 0, f'{name}: enemy release, chandelier break or trigger removal')
    pair = rows['pair']
    require(pair['triggers'] == pair['scripts'] == 2 and pair['maxslots'] == 2 and pair['finalslots'] == 0 and pair['thrown'] == -1, 'pair: both slots must fill and drain')
    require([c['uid'] for c in pair['chains']] == [24, 114] and all(c['start'] == 0 and c['end'] == 9 and c['alive'] == 0 and c['chandelieraction'] == 104 for c in pair['chains']), 'pair: scripts must run side by side')
    stagger = rows['stagger']
    require(stagger['thrown'] == 10 and stagger['background'] == 1 and stagger['maxslots'] == 2, 'stagger: a later script must be refused as a background script, not approximated')
    idle = rows['no-overlap']
    require(idle['triggers'] == idle['scripts'] == 3 and idle['maxslots'] == 0 and all(c['start'] == -1 and c['starts'] == 0 and c['alive'] == 1 and c['chandeliery'] == 140 for c in idle['chains']), 'no-overlap: scripts must not start')
    print('PASS: 7 controlled world fixtures: single starts, pause delay, paired slots, refused staggered slot order and no overlap')
    print('Boundary: controlled records with a frozen, positioned player; not the level route. Staggered paired starts remain unported (original runs a world-freezing background script).')


if __name__ == '__main__':
    main()
