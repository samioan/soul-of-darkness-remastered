"""Verify the second-level first encounter: removed-source script trigger 103/gate 87 and controlled world chain."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require

ORACLE_ROWS = 2112
SCENARIOS = 11


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    args = parser.parse_args()
    expected = (ROOT / 'soul/analysis/encounter-gate-oracle.tsv').read_text().splitlines()
    require(len(expected) == ORACLE_ROWS and len({r.split('\t')[1] for r in expected}) == SCENARIOS, 'Incomplete encounter gate oracle')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--encounter-gate-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Encounter gate row counts differ')
    for index, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Encounter gate row {index}:\nJava: {want}\nC++:  {got}')
    print(f'PASS: {ORACLE_ROWS} original-bytecode rows: condition-4 removed-source trigger, selected script 95 and gate 87 across {SCENARIOS} scenarios')

    names = ('triggers deferred initial enable wave second chest start end alive96 flags96 alive97 flags97 alive98 flags98 alive102 flags102 '
             'chestflags chestaction chestalive gatey gateaction removedactivations enableactivations containerenables scriptalive gateticks '
             'strikes96 strikes97 strikes98 strikes102 rng').split()
    rows = {}
    for line in subprocess.check_output([str(args.exe), '--data', str(args.data), '--encounter-world-manifest'], text=True).splitlines():
        fields = line.split('\t')
        require(fields[0] == 'W' and len(fields) == 2 + len(names), f'Malformed encounter world row: {line}')
        rows[fields[1]] = dict(zip(names, (int(v) for v in fields[2:])))
    require(set(rows) == {'chain', 'reverse', 'paused', 'first-wave-partial', 'second-wave-partial', 'no-overlap'}, 'Missing encounter world scenario')
    for name, row in rows.items():
        require(row['triggers'] == 4 and row['deferred'] == 0 and row['initial'] == 128, f'{name}: shipped triggers 100/104/62/103 or initial disabled flags missing')
    # Full chain, in either kill order: 100 -> two respawning first-wave enemies -> 104 -> flying pair -> 62 + 103.
    for name in ('chain', 'reverse', 'paused'):
        r = rows[name]
        require(r['enableactivations'] == 1 and r['removedactivations'] == 2 and r['containerenables'] == 1 and r['scriptalive'] == 0, f'{name}: trigger chain incomplete')
        require(0 <= r['enable'] < r['wave'] < r['second'] and r['second'] == r['chest'] == r['start'] < r['end'], f'{name}: trigger ordering is wrong')
        require(r['end'] - r['start'] == 11 and r['gateticks'] == -2, f'{name}: script 95 did not run its 11 ticks and complete')
        require((r['gatey'], r['gateaction']) == (300, 103), f'{name}: gate 87 did not finish opened at y=300')
        require(all(r[f'alive{u}'] == 0 for u in (96, 97, 98, 102)), f'{name}: an enemy remained after all strikes')
        require(r['strikes96'] == r['strikes97'] == 2 and r['strikes98'] == r['strikes102'] == 1, f'{name}: first-wave enemies must respawn once behind a fade')
        require(not r['chestflags'] & (32 | 128) and r['chestalive'] == 1 and r['chestaction'] == 106, f'{name}: chest 59 was not enabled')
    require(rows['paused']['enable'] == 30 and rows['paused']['wave'] - rows['chain']['wave'] == 30 and rows['paused']['end'] - rows['paused']['start'] == 11,
            'Paused world advanced a trigger')
    require(rows['chain']['rng'] == rows['paused']['rng'], 'Pausing changed the random stream')
    # Partial removal never advances the chain.
    first = rows['first-wave-partial']
    require(first['enableactivations'] == 1 and first['removedactivations'] == 0 and first['alive97'] == 1 and first['alive98'] == first['alive102'] == 1
            and first['flags98'] & 32 and first['flags102'] & 32 and first['gatey'] == 380 and first['start'] == -1, 'Trigger 104 fired with a surviving first-wave enemy')
    second = rows['second-wave-partial']
    require(second['removedactivations'] == 1 and second['alive102'] == 1 and second['chestflags'] & 32 and second['start'] == -1 and second['containerenables'] == 0 and second['gatey'] == 380,
            'Triggers 62/103 fired with a surviving second-wave enemy')
    idle = rows['no-overlap']
    require(idle['enableactivations'] == idle['removedactivations'] == idle['containerenables'] == 0 and idle['enable'] == -1 and all(idle[f'alive{u}'] == 1 and idle[f'flags{u}'] == 160 for u in (96, 97, 98, 102)),
            'Chain started without player overlap')
    print('PASS: 6 controlled world fixtures: overlap enable, double-strike first wave, wave enable, chest enable, script 95/gate 87 opening, pause, partial removal and no overlap')
    print('Boundary: controlled records with direct lethal strikes, not the actual level route; the remaining level-1/2 trigger chains stay pending')


if __name__ == '__main__':
    main()
