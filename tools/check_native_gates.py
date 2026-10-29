"""Compare native linked gate state/tiles and boundary-clipped sprite surfaces."""
import argparse
import binascii
from pathlib import Path
import struct
import subprocess
from bsprite import Sprite
from game_assets import ROOT, DEFAULT_DATA, require, resources


def pixels(sprite, row, width):
    n = list(map(int, row[4:22]))
    _, x, y, _, _, _, _, flags, animation, _, step, _, _, direction, variant, _, ox, oy = n
    out = [0xff102030] * (width*320)
    if not flags & (1024 | 128):
        start, _ = sprite.animations[animation]
        frame, _, dx, dy, af = sprite.animation_frames[start+step]
        flip = flags & 3
        left, top, w, h, source = sprite.compose_frame(frame, 0, flip ^ (af & 3))
        px, py = x-80+(-dx if flip & 1 else dx)+left, y+(-dy if flip & 2 else dy)+top
        direction = 4 if direction == 9 else direction
        boundary = (ox if direction <= 2 else oy) + (20*variant if direction in (2, 8) else 0)
        clip = [0, 0, width, 320]
        if direction == 1: clip[0] = max(0, boundary-80)
        if direction == 2: clip[2] = min(width, boundary-80)
        if direction == 4: clip[1] = max(0, boundary)
        if direction == 8: clip[3] = min(320, boundary)
        for sy in range(max(0, clip[1]-py), min(h, clip[3]-py)):
            for sx in range(max(0, clip[0]-px), min(w, clip[2]-px)):
                color = source[sy*w+sx]
                if color >> 24:
                    require(color >> 24 == 255, 'Gate oracle requires binary alpha')
                    out[(py+sy)*width+px+sx] = color
    return binascii.crc32(struct.pack(f'>{len(out)}I', *out)) & 0xffffffff


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--port-exe', type=Path)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    expected = (ROOT/'soul/analysis/gate-oracle.tsv').read_text().splitlines()
    require(len(expected) == 2304 and len({r.split('\t')[1] for r in expected}) == 16, 'Incomplete linked gate oracle')
    require(len({r.split('\t')[-1] for r in expected if r.startswith('G\tup\t')}) > 2, 'Gate collision oracle must exercise changing cells')
    actual = subprocess.check_output([str(a.exe), '--data', str(a.data), '--gate-manifest'], text=True).splitlines()
    states = [r for r in actual if r.startswith('G\t')]
    require(len(states) == len(expected), 'Gate state row count differs')
    for i, (j, n) in enumerate(zip(expected, states), 1):
        require(j == n, f'Gate row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 2304 original-bytecode linked-gate state/collision-grid snapshots across sixteen scenarios')
    sprite = Sprite.parse(dict(resources(a.data/'2'))[28])
    original = {(r[1], r[2]): r for line in expected if (r := line.split('\t'))[3] == '1'}
    render = [r.split('\t') for r in actual if r.startswith('R\t')]
    require(len(render) == 160, 'Incomplete gate render surfaces')
    for _, scenario, frame, width, checksum in render:
        require(int(checksum) == pixels(sprite, original[scenario, frame], int(width)), f'Gate boundary pixels differ: {scenario} {frame} {width}')
    print('PASS: 160 independently composed clipped gate surfaces at widths 240/569')
    if a.port_exe:
        reference = None
        for width in (240, 569):
            crc = None
            for fps in (30, 60, 144):
                for interpolate in (True, False):
                    cmd = [str(a.port_exe), '--movement-preview', '--region-script', '--data', str(a.data), '--headless', '--width', str(width), '--fps', str(fps), '--frames', str(fps*2)]
                    if not interpolate: cmd += ['--no-interpolation']
                    v = dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())
                    require(v['gates'] == '1', 'First-level gate was not loaded')
                    state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc')}
                    if reference is None: reference = state
                    require(reference == state, 'Width/FPS changed gate host state')
                    if crc is None: crc = v['movement_crc']
                    require(crc == v['movement_crc'], 'FPS/interpolation changed gate host pixels')
        print('PASS: 12 first-level gate-load/region runs at widths 240/569 and 30/60/144 FPS, interpolation on/off')


if __name__ == '__main__':
    main()
