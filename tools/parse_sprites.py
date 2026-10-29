"""Validate all 36 BSprite resources; optional previews stay in ignored analysis/."""
import argparse
from collections import Counter
import hashlib
from pathlib import Path
import struct
from bsprite import Sprite, decode_png, write_png
from game_assets import DEFAULT_DATA, ROOT, require, sprite_resources


def digest(pixels):
    return hashlib.sha256(b''.join(struct.pack('>I', p) for p in pixels)).hexdigest()


def load_sprites(data_dir):
    return {(pack, index): Sprite.parse(data, f'{pack}:{index}') for pack, index, data in sprite_resources(data_dir)}


def validate(sprites, oracle=None, quiet=False):
    totals, codecs = Counter(), Counter()
    for (pack, resource), sprite in sprites.items():
        totals.update(sprites=1, modules=len(sprite.modules), frames=len(sprite.frames), animations=len(sprite.animations))
        for module in range(len(sprite.modules)):
            for palette in range(sprite.palette_count(module)):
                sprite.decode_module(module, palette)
                totals['module_palettes'] += 1
        for frame in range(len(sprite.frames)):
            # Also visits nested frames, catches recursive references and verifies rendering ranges.
            sprite.compose_frame(frame)
        for group in sprite.png_images:
            for image in group:
                decode_png(image)
                totals['png_variants'] += 1
        codecs[hex(sprite.codec) if sprite.palettes else 'PNG'] += 1
        if not quiet:
            print(f'{pack:>2}:{resource:<2} modules={len(sprite.modules):>3} frames={len(sprite.frames):>3} '
                  f'animations={len(sprite.animations):>2} flags={sprite.flags:#010x}')
    if oracle is not None:
        checked = 0
        for line in oracle.read_text().splitlines():
            kind, pack, resource, *fields = line.split('\t')
            sprite = sprites[int(pack), int(resource)]
            if kind == 'module':
                palette, module, flip = map(int, fields[:3])
                actual = digest(sprite.decode_module(module, palette, flip))
            elif kind == 'image':
                group, palette, width, height = map(int, fields[:4])
                w, h, pixels = decode_png(sprite.png_images[group][palette])
                require((w, h) == (width, height), 'oracle image dimensions differ')
                actual = digest(pixels)
            elif kind == 'bounds':
                frame, rect, flip = map(int, fields[:3])
                actual = ','.join(map(str, sprite.frame_bounds(frame, rect, flip)))
            else:
                raise ValueError(f'Unknown oracle record: {kind}')
            require(actual == fields[-1], f'Original-Java mismatch: {kind} {pack}:{resource} {fields[:-1]}')
            checked += 1
        require(checked > 0, 'empty sprite oracle')
        print(f'PASS: {checked} pixel-hash/bounds comparisons against original Java')
    print(f'PASS: {dict(totals)}; encodings={dict(codecs)}; exact sprite/resource consumption')


def output_path(path):
    path = path.resolve()
    analysis = (ROOT / 'soul/analysis').resolve()
    require(path.is_relative_to(analysis), 'Decoded previews must stay inside ignored soul/analysis/')
    path.parent.mkdir(parents=True, exist_ok=True)
    return path


def preview(sprite, frame, palette, flip, animation=None, step=0):
    require(palette >= 0, 'palette must be nonnegative')
    if not sprite.frames:
        require(palette < len(sprite.png_images[0]), 'palette out of range')
        width, height, pixels = decode_png(sprite.png_images[0][palette])
        return width, height, pixels
    if animation is not None:
        require(0 <= animation < len(sprite.animations), 'animation out of range')
        start, count = sprite.animations[animation]
        require(0 <= step < count, 'animation step out of range')
        frame, _, _, _, flags = sprite.animation_frames[start+step]
        flip ^= flags & 15
    require(0 <= frame < len(sprite.frames), 'frame out of range')
    _, _, width, height, pixels = sprite.compose_frame(frame, palette, flip)
    return width, height, pixels


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--oracle', type=Path)
    parser.add_argument('--quiet', action='store_true', help='summary only')
    parser.add_argument('--preview', help='pack:resource')
    parser.add_argument('--frame', type=int, default=0)
    parser.add_argument('--palette', type=int, default=0)
    parser.add_argument('--flip', type=int, choices=range(4), default=0)
    parser.add_argument('--animation', type=int)
    parser.add_argument('--step', type=int, default=0)
    parser.add_argument('--output', type=Path)
    parser.add_argument('--sheet', type=Path, help='all sprites, first frame/image, scaled to fit')
    args = parser.parse_args()
    sprites = load_sprites(args.data)
    validate(sprites, args.oracle, args.quiet)
    if args.preview:
        require(args.output is not None, '--preview requires --output')
        sprite = sprites[tuple(map(int, args.preview.split(':')))]
        w, h, pixels = preview(sprite, args.frame, args.palette, args.flip, args.animation, args.step)
        dest = output_path(args.output)
        write_png(dest, w, h, pixels)
        print(f'Preview: {dest} ({w}x{h})')
    if args.sheet:
        columns, cell = 6, 160
        width, height = columns*cell, ((len(sprites)+columns-1)//columns)*cell
        pixels = [0xff20252f]*(width*height)
        for index, sprite in enumerate(sprites.values()):
            w, h, image = preview(sprite, 0, 0, 0)
            scale = min(3, (cell-8)/max(w, h))
            dw, dh = max(1, int(w*scale)), max(1, int(h*scale))
            ox, oy = (index%columns)*cell+(cell-dw)//2, (index//columns)*cell+(cell-dh)//2
            for y in range(dh):
                for x in range(dw):
                    color = image[min(h-1, int(y/scale))*w+min(w-1, int(x/scale))]
                    if color >> 24: pixels[(oy+y)*width+ox+x] = color
        dest = output_path(args.sheet)
        write_png(dest, width, height, pixels)
        print(f'Contact sheet: {dest}; row-major order matches the resource listing')


if __name__ == '__main__':
    main()
