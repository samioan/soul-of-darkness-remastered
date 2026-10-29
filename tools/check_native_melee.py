"""Compare native weapon-0 melee with original bytecode and native host runs."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', type=Path, required=True)
    parser.add_argument('--port-exe', type=Path)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--oracle', type=Path, default=ROOT/'soul/analysis/melee-oracle.tsv')
    args = parser.parse_args()
    expected = args.oracle.read_text(encoding='utf-8').splitlines()
    require(len(expected) == 1920, 'Incomplete melee oracle')
    actual = subprocess.check_output([str(args.exe), '--data', str(args.data), '--melee-manifest'], text=True).splitlines()
    require(len(actual) == len(expected), 'Melee row counts differ')
    for index, (java, native) in enumerate(zip(expected, actual), 1):
        require(java == native, f'Melee row {index}:\nJava: {java}\nC++: {native}')
    print('PASS: 1920 original-bytecode melee/player/weapon/hit-query snapshots over 960 ticks')
    if args.port_exe:
        reference = None
        for width in (240, 569):
            checksum = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    command = [str(args.port_exe), '--movement-preview', '--melee-script', '--data', str(args.data),
                               '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(fps*5)]
                    if not interpolate:
                        command += ['--no-interpolation']
                    values = dict(item.split('=') for item in subprocess.check_output(command, text=True).split())
                    require(values['melee_attacks'] == '3' and values['melee_poses'] == '7', 'Host did not perform first/combo/aerial attacks')
                    require(values['weapon_visible'] == '0' and values['player_hazard'] == '0' and values['player_animation'] == '0', 'Host failed to finish attacks and land')
                    state = {key: value for key, value in values.items() if key.startswith(('player_', 'melee_', 'weapon_'))}
                    if reference is None:
                        reference = state
                    require(state == reference, 'FPS/width altered melee state')
                    if checksum is None:
                        checksum = values['movement_crc']
                    require(checksum == values['movement_crc'], 'FPS/interpolation altered melee pixels')
        print('PASS: 12 first-level melee runs at widths 240/569, 30/60/144 FPS and both interpolation settings')


if __name__ == '__main__':
    main()
