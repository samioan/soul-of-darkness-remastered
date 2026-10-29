"""Compare native rendered surfaces with verified Python frame composition."""
import argparse
import binascii
from dataclasses import replace
from functools import lru_cache
from pathlib import Path
import struct
import subprocess
from bsprite import decode_png
from game_assets import DEFAULT_DATA, ROOT, require
from parse_sprites import load_sprites, validate


def masked(sprite):
    modules, frames = [], []
    for start, count, rect_start, rect_count in sprite.frames:
        new_start = len(modules)
        modules.extend(sprite.frame_modules[start+i] for i in range(count) if i%2)
        frames.append((new_start, len(modules)-new_start, rect_start, rect_count))
    return replace(sprite, frame_modules=modules, frames=frames)


def surface(width, image, origin, variant, grid=False):
    pixels = ([0xff28323f if x%32 == 0 or y%32 == 0 else 0xff101820 for y in range(320) for x in range(width)]
              if grid else [0xff102030] * (width*320))
    left, top, w, h, source = image
    x, y = origin[0]+left, origin[1]+top
    clip = (13,17,width-18,290) if variant == 1 else (0,0,width,320)
    for py in range(max(0,clip[1]-y), min(h,clip[3]-y)):
        for px in range(max(0,clip[0]-x), min(w,clip[2]-x)):
            color = source[py*w+px]
            if color >> 24:
                # Shipped palettes use binary alpha; synthetic C++ tests cover
                # source-over rounding for intermediate alpha separately.
                require(color >> 24 == 255, 'partial-alpha reference needs explicit blend oracle')
                pixels[(y+py)*width+x+px] = color
    return f'{binascii.crc32(struct.pack(f">{len(pixels)}I", *pixels)) & 0xffffffff:08x}'


def expected(sprites):
    for (pack, resource), sprite in sprites.items():
        hidden = masked(sprite)
        @lru_cache(maxsize=4096)
        def frame_image(frame, palette, flip, hide):
            return (hidden if hide else sprite).compose_frame(frame, palette, flip)
        palettes = min((sprite.palette_count(m) for m in range(len(sprite.modules))), default=1)
        for width in (240,569):
            def position(variant):
                return (2,2) if variant == 1 else (width-3,319) if variant == 2 else (width//2,160)
            for frame in range(len(sprite.frames)):
                for palette in range(palettes):
                    for flip in range(4):
                        for variant in range(4):
                            if variant and frame not in (0,len(sprite.frames)-1): continue
                            checksum = surface(width,frame_image(frame,palette,flip,variant==3),position(variant),variant)
                            yield '\t'.join(map(str,('F',pack,resource,width,frame,palette,flip,variant,checksum)))
            for animation, (start,count) in enumerate(sprite.animations):
                for step in range(count):
                    frame, _, dx, dy, flags = sprite.animation_frames[start+step]
                    for flip in range(4):
                        x,y = position(0)
                        origin = (x+(-dx if flip&1 else dx), y+(-dy if flip&2 else dy))
                        checksum = surface(width,frame_image(frame,0,flip^(flags&3),False),origin,0)
                        yield '\t'.join(map(str,('A',pack,resource,width,animation,step,flip,checksum)))
            if not sprite.frames:
                for group, images in enumerate(sprite.png_images):
                    for palette, png in enumerate(images):
                        w,h,pixels = decode_png(png)
                        for flip in range(4):
                            transformed = [pixels[(h-1-y if flip&2 else y)*w+(w-1-x if flip&1 else x)] for y in range(h) for x in range(w)]
                            for variant in range(3):
                                checksum = surface(width,(0,0,w,h,transformed),position(variant),variant)
                                yield '\t'.join(map(str,('I',pack,resource,width,group,palette,flip,variant,checksum)))
        frame_image.cache_clear()


def preview_checks(exe, data, sprites):
    sprite = sprites[2,0]
    animation = 4 # Four steps with unequal durations, so a frozen/no-op clock cannot pass.
    start, count = sprite.animations[animation]
    require(count > 1 and len({row[1] for row in sprite.animation_frames[start:start+count]}) > 1,
            'preview timing fixture must have multiple steps and unequal durations')
    # Five seconds = 60 complete 83ms updates. Determine the final pose by
    # consuming duration intervals, independently of presentation frame count.
    remaining, step = 60, 0
    while True:
        duration = sprite.animation_frames[start+step][1]
        if duration == 0 or remaining < duration: break
        remaining -= duration
        step = (step+1)%count
    frame, _, dx, dy, flags = sprite.animation_frames[start+step]
    checks = 0
    for width in (240,569):
        flip = 1
        origin = (width//2-dx,220+dy)
        checksum = int(surface(width,sprite.compose_frame(frame,0,flip^(flags&3)),origin,0,grid=True),16)
        for rate in (30,60,144):
            for interpolate in (False,True):
                command = [str(exe.resolve()),'--sprite','2:0','--data',str(data.resolve()),'--headless',
                           '--animation',str(animation),'--flip','1','--width',str(width),'--fps',str(rate),'--frames',str(rate*5)]
                if not interpolate: command.append('--no-interpolation')
                result = subprocess.run(command,check=True,capture_output=True,text=True)
                values = dict(token.split('=',1) for token in result.stdout.split())
                require(int(values['ticks']) == 60,'preview simulation speed depends on FPS')
                require(int(values['preview_step']) == step and int(values['preview_tick']) == remaining,'preview pose clock differs')
                require(int(values['preview_crc']) == checksum,'preview pixels differ from Python or depend on display FPS/interpolation')
                checks += 1
        command = [str(exe.resolve()),'--sprite','1:1','--data',str(data.resolve()),'--headless',
                   '--width',str(width),'--frames','1']
        result = subprocess.run(command,check=True,capture_output=True,text=True)
        values = dict(token.split('=',1) for token in result.stdout.split())
        w,h,pixels = decode_png(sprites[1,1].png_images[0][0])
        checksum = int(surface(width,(0,0,w,h,pixels),(0,0),0,grid=True),16)
        require(int(values['preview_crc']) == checksum,'standalone preview image differs from Python')
        checks += 1
    print(f'PASS: {checks} asset preview runs (30/60/144 FPS, interpolation on/off, widths 240/569, standalone PNG)')


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', required=True, type=Path)
    parser.add_argument('--data', default=DEFAULT_DATA, type=Path)
    parser.add_argument('--oracle', default=ROOT/'soul/analysis/sprite-oracle.tsv', type=Path)
    parser.add_argument('--port-exe', type=Path, help='also check the native asset preview and its animation timing')
    args = parser.parse_args()
    sprites = load_sprites(args.data)
    validate(sprites,args.oracle,quiet=True)
    result = subprocess.run([str(args.exe.resolve()),'--data',str(args.data.resolve()),'--render-manifest'],
                            check=True,capture_output=True,text=True)
    actual = result.stdout.splitlines()
    count = 0
    for count, row in enumerate(expected(sprites),1):
        require(count <= len(actual),f'native render manifest ended at {count}')
        require(row == actual[count-1],f'native render mismatch at {count}: {actual[count-1]!r} != {row!r}')
    require(count == len(actual),f'native render manifest has extra rows: {len(actual)} != {count}')
    print(f'PASS: {count} native/Python render surfaces at widths 240/569 (frames, animation steps, flips, clips and masks)')
    if args.port_exe: preview_checks(args.port_exe,args.data,sprites)


if __name__ == '__main__':
    main()
