"""Compare ordinary ice pass-through and controlled moving-object link fixtures."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    expected = (ROOT / 'soul/analysis/ice-attachment-oracle.tsv').read_text(encoding='utf-8').splitlines()
    require(len(expected) == 18411, 'Incomplete ice attachment oracle')
    cases = {r.split('\t')[1] for r in expected}
    require(len(cases) == 17 and sum(r.split('\t')[1].startswith('natural') for r in expected) == 6498, 'Missing natural pass-through or controlled-link cases')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--ice-attachment-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Ice attachment row count differs')
    for i, (want, got) in enumerate(zip(expected, actual)):
        require(want == got, f'Ice attachment row {i} differs:\nJava: {want}\nC++:  {got}')
    natural = [r.split('\t') for r in actual if r.split('\t')[1].startswith('natural')]
    require(all(r[-4:-2] == ['0', '0'] for r in natural), 'A shipped point attack incorrectly created an attachment')
    flight = [r for r in natural if r[0] == 'I' and r[13] == '7' and r[-1] == '1']
    require(flight and all(r[26] == r[28] and r[27] == r[29] for r in flight), 'Shipped ice flight attack bounds changed')
    linked = [r.split('\t') for r in actual if r.startswith('I\tlinked\t')]
    require(any(r[-4:-2] == ['1', '1'] for r in linked) and linked[-1][-4:-2] == ['0', '0'], 'Controlled attachment did not link and detach')
    rider = [r.split('\t') for r in actual if r.startswith('I\tlinked-rider\t')]
    require(any(r[-2] == '1' for r in rider) and rider[-1][-2] == '0', 'Controlled ice rider did not land and detach')
    print('PASS: 18411 original-bytecode rows across six natural pass-through and eleven controlled attachment/order/facing/offset/rider/melt/pause arenas')


if __name__ == '__main__':
    main()
