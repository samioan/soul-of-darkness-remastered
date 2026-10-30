"""Check selected-level inputs, selection guards and same-level reconstruction."""
import argparse
from pathlib import Path
import subprocess
import tempfile
from game_assets import ROOT, DEFAULT_DATA, require


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--port-exe', type=Path)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    expected = (ROOT / 'soul/analysis/selected-level-oracle.tsv').read_text().splitlines()
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--selected-level-manifest'], text=True).splitlines()
    require(len(expected) == len(actual) == 983, 'Incomplete selected-level oracle')
    for i, (want, got) in enumerate(zip(expected, actual), 1):
        require(want == got, f'Selected-level row {i}:\nJava: {want}\nC++: {got}')
    print('PASS: 983 original-bytecode selected-level input/player rows across all nine levels; reverse-order reloads also construct')
    if not a.port_exe:
        return

    def run(*args, error=None):
        r = subprocess.run([str(a.port_exe), '--data', str(a.data), '--headless', *map(str, args)], text=True, capture_output=True)
        if error:
            require(r.returncode != 0 and error in r.stderr, f'Expected {error}: {r.stdout} {r.stderr}')
            return
        require(r.returncode == 0, r.stderr)
        return dict(x.split('=', 1) for x in r.stdout.split())

    run('--movement-level', 0, error='requires the explicit movement preview')
    run('--game', '--movement-level', 0, error='requires the explicit movement preview')
    for level in (-1, 1, 8, 9):
        run('--movement-preview', '--movement-level', level, error='Selected gameplay level is not playable')
    for width in (240, 569):
        args = ['--movement-preview', '--width', width, '--fps', 'native', '--frames', 80]
        implicit = run(*args)
        explicit = run(*args, '--movement-level', 0)
        require(implicit == explicit, 'Explicit/default level-zero paths differ')
        require(explicit['world_level'] == '0' and explicit['world_pack'] == '3', 'Mixed selected-level ownership')
        restarted = run('--movement-preview', '--movement-level', 0, '--checkpoint-script', '--width', width, '--fps', 'native', '--frames', 500)
        require(restarted['world_level'] == '0' and restarted['world_pack'] == '3' and int(restarted['checkpoint_restarts']) >= 2, 'Restart lost selected level')
    with tempfile.TemporaryDirectory(prefix='sod-selected-') as temp:
        path = Path(temp) / 'checkpoint.sav'
        run('--movement-preview', '--checkpoint-script', '--save-file', path, '--fps', 'native', '--frames', 5)
        _, body = path.read_text().split('\n', 1)
        fields = body.splitlines()[0].split()
        fields[3] = '376'  # Valid UID range, but not a checkpoint in level zero.
        body = ' '.join(fields) + '\n' + '\n'.join(body.splitlines()[1:]) + '\n'
        checksum = 2166136261
        for byte in body.encode():
            checksum = ((checksum ^ byte) * 16777619) & 0xffffffff
        path.write_bytes(f'SODPC 2 {checksum}\n{body}'.encode())
        saved = path.read_bytes()
        run('--game', '--continue', '--save-file', path, '--fps', 'native', '--frames', 5, error='Checkpoint UID is not in the selected level')
        require(path.read_bytes() == saved, 'Rejected selected-level checkpoint rewrote save')
    print('PASS: host checks for selection guards, default/explicit parity, repeated restarts at 240/569 and foreign checkpoint rejection')


if __name__ == '__main__':
    main()
