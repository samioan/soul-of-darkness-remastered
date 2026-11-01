"""Verify the second level's intro (trigger 134, script 133): original-bytecode rows and controlled real-record world fixtures."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require

ORACLE_ROWS = 4760
SCENARIOS = 7


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    args = parser.parse_args()
    expected = (ROOT / 'soul/analysis/level1-intro-oracle.tsv').read_text().splitlines()
    require(len(expected) == ORACLE_ROWS and len({r.split('\t')[1] for r in expected}) == SCENARIOS, 'Incomplete level-1 intro oracle')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--level1-intro-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Level-1 intro row counts differ')
    for index, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Level-1 intro row {index}:\nJava: {want}\nC++:  {got}')
    print(f'PASS: {ORACLE_ROWS} original-bytecode rows: script 133 timeline, camera, flags, six dialogue anchors and actor presence across {SCENARIOS} scenarios')

    names = 'start end starts finishes skips dialogues dialogueframes triggeralive startx starty px py zx zy zflags fx fy fflags'.split()
    rows = {}
    for line in subprocess.check_output([str(args.exe), '--data', str(args.data), '--level1-intro-world-manifest'], text=True).splitlines():
        fields = line.split('\t')
        require(fields[0] == 'W' and len(fields) == 2 + len(names), f'Malformed level-1 intro world row: {line}')
        rows[fields[1]] = dict(zip(names, (int(v) for v in fields[2:])))
    require(set(rows) == {'normal', 'skip', 'paused', 'blocked-skip'}, 'Missing level-1 intro world scenario')
    normal = rows['normal']
    require(normal['start'] == 1 and normal['starts'] == normal['finishes'] == 1 and normal['skips'] == 0 and normal['dialogues'] == 6 and normal['triggeralive'] == 0,
            'normal: the spawn-overlapped trigger must start the scene once, show six dialogues and be consumed')
    require((normal['startx'], normal['starty']) == (1391, 816) and (normal['px'], normal['py']) == (1445, 819), 'normal: Kale must walk to the scripted end position')
    require((normal['zx'], normal['zy'], normal['zflags']) == (1528, 819, 9 | 8192) and (normal['fx'], normal['fy'], normal['fflags']) == (1406, 736, 8 | 8192), 'normal: zombie/flyer final placement and flag 8192')
    skip = rows['skip']
    require(skip['skips'] == 1 and skip['dialogues'] == 1 and skip['finishes'] == 1 and skip['end'] < normal['end'], 'skip: fast-forward must finish early after one dialogue')
    for key in ('px', 'py', 'zx', 'zy', 'zflags', 'fx', 'fy', 'fflags'):
        require(skip[key] == normal[key], f'skip: {key} must equal the unskipped final state')
    paused = rows['paused']
    require(paused['start'] == normal['start'] + 10 and paused['end'] == normal['end'] + 10 and paused['dialogues'] == 6, 'paused: ten paused ticks must delay the whole scene')
    blocked = rows['blocked-skip']
    require(blocked['skips'] == 0 and blocked['end'] == normal['end'] and blocked['dialogues'] == 6, 'blocked-skip: a disallowed skip must not fast-forward')
    print('PASS: 4 controlled real-record CombatWorld fixtures: spawn-triggered start, skip fast-forward equals normal end state, pause delay and blocked skip')
    print('Boundary: only the intro actors are constructed; the frontend still enters level 0 only and the English text is a desktop translation.')


if __name__ == '__main__':
    main()
