"""Compare native BSprite metadata/pixels/bounds with Python and the Java oracle."""
import argparse
from pathlib import Path
import struct
import subprocess
from bsprite import decode_png
from check_native_assets import fingerprint
from game_assets import DEFAULT_DATA, ROOT, require
from parse_sprites import load_sprites, validate


def pixel_hash(pixels):
    return fingerprint(b''.join(struct.pack('>I', p) for p in pixels))


def manifest(sprites):
    rows = []
    for (pack, resource), s in sprites.items():
        def row(kind, *values):
            rows.append('\t'.join(map(str, (kind, pack, resource, *values))))
        row('S', s.flags, s.codec, len(s.modules), len(s.frame_modules), len(s.collision_rects), len(s.frames),
            len(s.animation_frames), len(s.animations), len(s.palettes), len(s.png_images))
        for i, m in enumerate(s.modules):
            row('M', i, m.width, m.height, m.x, m.y, m.image)
            for p in range(s.palette_count(i)):
                for flip in range(4):
                    pixels = s.decode_module(i, p, flip)
                    row('P', i, p, flip, len(pixels), pixel_hash(pixels))
        for i, values in enumerate(s.frame_modules): row('FM', i, *values)
        for i, values in enumerate(s.collision_rects): row('CR', i, *values)
        for i, values in enumerate(s.frames):
            row('F', i, *values, *s.frame_rects[i])
            for rect in range(-1, values[3]+1):
                for flip in range(4): row('B', i, rect, flip, *s.frame_bounds(i, rect, flip))
        for i, values in enumerate(s.animation_frames): row('AF', i, *values)
        for i, values in enumerate(s.animations): row('A', i, *values)
        for g, group in enumerate(s.palettes):
            for p, palette in enumerate(group): row('PAL', g, p, len(palette), pixel_hash(palette))
        for g, group in enumerate(s.png_images):
            for p, png in enumerate(group):
                w, h, pixels = decode_png(png)
                row('I', g, p, w, h, fingerprint(png), pixel_hash(pixels))
    return rows


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', required=True, type=Path)
    parser.add_argument('--data', default=DEFAULT_DATA, type=Path)
    parser.add_argument('--oracle', default=ROOT/'soul/analysis/sprite-oracle.tsv', type=Path)
    args = parser.parse_args()
    sprites = load_sprites(args.data)
    # Require the independently regenerated original-bytecode oracle; do not silently skip it.
    validate(sprites, args.oracle, quiet=True)
    expected = manifest(sprites)
    result = subprocess.run([str(args.exe.resolve()), '--data', str(args.data.resolve()), '--sprite-manifest'],
                            check=True, capture_output=True, text=True)
    actual = result.stdout.splitlines()
    require(len(actual) == len(expected), f'native sprite manifest length: {len(actual)} != {len(expected)}')
    for index, (left, right) in enumerate(zip(expected, actual), 1):
        require(left == right, f'native sprite row {index}: {right!r} != {left!r}')
    print(f'PASS: {len(expected)} native/Python sprite rows across {len(sprites)} sprites; Python pixels/bounds also match original Java')


if __name__ == '__main__':
    main()
