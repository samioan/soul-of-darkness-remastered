"""Compare native type-3 actors with original bytecode and run controlled selected-level world fixtures."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require

ORACLE_ROWS = 11487
SIMPLE_THROWERS = {(1, 48), (1, 7), (1, 88), (1, 69), (1, 70)}
RUNNERS = {(1, 352), (2, 99)}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    args = parser.parse_args()
    expected = (ROOT / 'soul/analysis/type3-actor-oracle.tsv').read_text().splitlines()
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--type3-actor-manifest'], text=True).splitlines()
    require(len(expected) == len(actual) == ORACLE_ROWS, 'Incomplete type-3 actor oracle')
    for index, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Type-3 actor row {index}:\nJava: {want}\nC++: {got}')
    print(f'PASS: {ORACLE_ROWS} original-bytecode rows over 24 integrated type-3 scenarios: movement, held-shot birth/release, '
          'parent/child dispatch, linked chest carry/drop, damage, death/loot, respawn, pause/freeze and reflection')
    rows = [line.split('\t') for line in subprocess.check_output([str(args.exe), '--data', str(args.data), '--type3-world-manifest'], text=True).splitlines()]
    worlds = {(int(r[1]), int(r[2]), int(r[3])): [int(v) for v in r[4:]] for r in rows if r[0] == 'W'}
    require(len(worlds) == 48, 'Expected 16 shipped type-3 records in three world modes')
    names = 'behavior mask born maxshots finalshots kills death alive health respawns attached chestflags chestaction chestgrounded carried x y effects loot rng'.split()
    for key, values in worlds.items():
        row = dict(zip(names, values))
        level, uid, mode = key
        if mode == 2:
            require(row['mask'] >> 8 == 0 and row['born'] == 0 and row['kills'] == 0, f'Remote-player fixture {key} acted')
        if mode == 1:
            require(row['kills'] >= 1, f'Lethal damage did not register for {key}')
        if mode == 0 and (level, uid) in SIMPLE_THROWERS:
            require((row['mask'] >> 13) & 7 == 7 and row['born'] >= 3 and row['maxshots'] >= 1 and row['kills'] == 0, f'Thrower {key} did not cycle aim/release/recovery')
        if (level, uid) in RUNNERS and mode == 0:
            require(row['attached'] == 52 and row['chestflags'] & 32 and row['carried'] >= 150 and (row['mask'] >> 16) & 1, f'Runner {key} did not carry its chest')
        if (level, uid) in RUNNERS and mode == 1:
            require(row['attached'] == 0 and not row['chestflags'] & (32 | 256) and row['chestaction'] == 0 and row['chestgrounded'] == 1 and row['alive'] == 0,
                    f'Runner {key} did not drop its chest and leave the world')
    enable = [[int(v) for v in r[1:]] for r in rows if r[0] == 'E']
    require(len(enable) == 1, 'Missing shipped enable-trigger fixture')
    triggers, before, activations, tick, flags62, flags74, action62, action74, alive = enable[0]
    require(triggers == 1 and before == 32 and activations == 1 and tick == 0 and alive == 0, 'Trigger 308 did not enable once on first overlap')
    require(not (flags62 | flags74) & (32 | 128) and action62 >= 101 and action74 >= 101, 'Trigger 308 did not enable and release both type-3 actors')
    presentation = [[int(v) for v in r[1:]] for r in rows if r[0] == 'H']
    require(len(presentation) == 4, 'Missing presentation fixtures')
    simulation = {tuple(r[2:7] + r[9:]) for r in presentation}
    require(len(simulation) == 1, 'Width or repeated rendering changed type-3 simulation or RNG')
    for width in (240, 569):
        group = [r for r in presentation if r[0] == width]
        require(len(group) == 2 and group[0][7:9] == group[1][7:9], 'Repeated presentation changed pixels')
    require(presentation[0][7:9] != presentation[2][7:9], 'Widescreen fixture did not render distinct pixels')
    print('PASS: 48 controlled shipped-record fixtures (levels 1/2, three modes), level-2 enable trigger 308 -> actors 62/74, and 4 presentation fixtures at widths 240/569')
    print('Boundary: controlled records, not a level route; level 3 paired mode, remaining level-1/2 triggers, non-player separation and exact actor-slot reuse remain pending')


if __name__ == '__main__':
    main()
