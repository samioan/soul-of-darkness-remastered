"""Visualize validated ActorCheck traces on their synthetic collision test map.

These are reference-analysis poses, not screenshots of the Windows port.
Run tools/rename_sod.py to regenerate the trace before using this tool.
"""
import argparse
import csv
import json
import re
from pathlib import Path
from bsprite import Sprite, write_png
from game_assets import ROOT, DEFAULT_DATA, require, resources
from parse_sprites import output_path


def paste(canvas, width, height, pixels, image_width, image_height, x, y):
    for row in range(image_height):
        if not 0 <= y+row < height:
            continue
        for col in range(image_width):
            if 0 <= x+col < width:
                color = pixels[row*image_width+col]
                if color >> 24:
                    canvas[(y+row)*width+x+col] = color


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--trace', type=Path, default=ROOT / 'soul/analysis/actor-trace.tsv')
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    parser.add_argument('--scenario', choices=['walk', 'jump', 'crouch', 'melee', 'wall', 'pause', 'ceiling'], default='jump')
    parser.add_argument('--frames', default='0,4,8,12,16,20,24,28')
    parser.add_argument('--output', type=Path, default=ROOT / 'soul/analysis/jump.png')
    args = parser.parse_args()
    frames = list(map(int, args.frames.split(',')))
    require(0 < len(frames) <= 100, 'select between 1 and 100 frames')
    with args.trace.open(encoding='utf-8') as file:
        samples = {int(r['frame']): r for r in csv.DictReader(file, delimiter='\t') if r['scenario'] == args.scenario}
    require(all(frame in samples for frame in frames), 'requested frame absent; regenerate the Actor trace')
    hero = Sprite.parse(dict(resources(args.data / '2'))[0])
    font = Sprite.parse(dict(resources(args.data / '1'))[0])
    source = (ROOT / 'soul/decompiled/d.java').read_text(encoding='utf-8')
    match = re.search(r'("(?:\\.|[^"\\])*")\.indexOf\(var1\)', source)
    require(match is not None, 'cannot find original font glyph map')
    alphabet = json.loads(match.group(1))
    columns, cell = min(4, len(frames)), 240
    width, height = columns*cell, ((len(frames)+columns-1)//columns)*cell
    canvas = [0xff20252f]*(width*height)
    for index, frame in enumerate(frames):
        sample = {key: int(value) for key, value in samples[frame].items() if key != 'scenario'}
        cx, cy = (index%columns)*cell, (index//columns)*cell
        camera_x = max(0, sample['x']-120)
        scene = [0xff101923]*(cell*cell)
        for y in range(cell):
            for x in range(cell):
                solid = y >= 200
                if args.scenario == 'wall': solid |= 180 <= x+camera_x < 200
                if args.scenario == 'ceiling': solid |= 60 <= y < 80
                if solid:
                    scene[y*cell+x] = 0xff43505c if (x+camera_x)%20 and y%20 else 0xff75808a
        start, count = hero.animations[sample['animation']]
        require(0 <= sample['animationFrame'] < count, 'invalid traced animation frame')
        pose, _, dx, dy, flags = hero.animation_frames[start+sample['animationFrame']]
        flip = sample['flags']&3
        left, top, w, h, pixels = hero.compose_frame(pose, transform=flip^(flags&3))
        x = sample['x']+(-dx if flip&1 else dx)-camera_x+left
        y = sample['y']+(-dy if flip&2 else dy)+top
        paste(scene, cell, cell, pixels, w, h, x, y)
        x = 8
        for char in f'{args.scenario.upper()} {frame:03d}':
            glyph = alphabet.index(char)
            left, top, w, h, pixels = font.compose_frame(glyph)
            paste(scene, cell, cell, pixels, w, h, x+left, 12+top)
            x += font.frame_rects[glyph][2]-1
        paste(canvas, width, height, scene, cell, cell, cx, cy)
    dest = output_path(args.output)
    write_png(dest, width, height, canvas)
    print(f'Actor reference pose sheet: {dest} ({args.scenario}, before animation advancement; synthetic test map)')


if __name__ == '__main__':
    main()
