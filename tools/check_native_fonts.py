"""Compare native font metrics with original Java and pixels with Python composition."""
import argparse
import binascii
from functools import lru_cache
from pathlib import Path
import struct
import subprocess
from bsprite import Sprite
from game_assets import DEFAULT_DATA, ROOT, require, resources

GLYPHS = ' !"#$%&\'()*+,-./0123456789:;<=>?\u00a9@ABCDEFGHIJKLMNOPQRSTUVWXYZ_\u00c1\u00c9\u00cd\u00d3\u00da\u00dd\u00c7\u00d0\u00ca\u00d1\u00ae\u00dc\u00a6\u00db\u00a3'
COUNTS, OFFSETS, WIDTHS, HEIGHTS = (5,1,16,1,6), (0,5,0,8,0), (3,6,3,6,3), (3,16,3,16,2)


def glyph(c):
    return GLYPHS.index(c) if c in GLYPHS else 31


class Reference:
    def __init__(self, data):
        self.font = Sprite.parse(dict(resources(data/'1'))[0])
        self.icons = Sprite.parse(dict(resources(data/'2'))[33])
        self.height = self.font.modules[1].height

    def measure(self, text):
        total, i = 0, 0
        while i < len(text):
            c = text[i]
            if c == '~': total += self.font.modules[0].width*2; i += 1
            elif c not in '{}': total += self.font.frame_rects[glyph(c)][2]-1
            i += 1
        return total

    def wrap(self, text, width, palette):
        ends, advance, count, i = [], 0, 0, 0
        while i < len(text):
            c = text[i]
            if c == '^' and advance <= width:
                ends.append(i); advance = count = 0
            elif width > 0 and (c in '^ ' or i == len(text)-1) and advance > width:
                previous = ends[-1] if ends else 0
                i -= 1
                while text[i] != ' ' and i > previous: i -= 1
                if i == previous: i = previous+count
                ends.append(i); advance = count = 0
            else:
                count += 1
                if c == '~': advance += self.font.modules[0].width*2; i += 1
                elif c not in '{}':
                    # Original indexes the flat module table, not frame starts.
                    module = self.font.frame_modules[glyph(c)][0] & 255
                    advance += self.font.modules[module].width+self.font.frame_modules[0][1]-(2 if palette == 5 else 1)
            i += 1
        return ends+[len(text)]

    @lru_cache(maxsize=512)
    def image(self, icon, frame, palette, flip=0):
        return (self.icons if icon else self.font).compose_frame(frame,palette,flip)

    def render(self, width, palette, anchor, wrapped, tick, host=False):
        pixels = ([0xff28323f if x%32 == 0 or y%32 == 0 else 0xff101820 for y in range(320) for x in range(width)]
                  if host else [0xff102030]*(width*320))
        clip = (0,0,width,320) if host else (7,11,width-12,299)

        def blit(image, x, y):
            dx,dy,w,h,source = image; x += dx; y += dy
            for py in range(max(0,clip[1]-y),min(h,clip[3]-y)):
                for px in range(max(0,clip[0]-x),min(w,clip[2]-x)):
                    color = source[py*w+px]
                    if color >> 24:
                        require(color >> 24 == 255,'font reference only supports binary alpha')
                        pixels[(y+py)*width+x+px] = color

        def line(text,x,y,anchors,base,current):
            if y < clip[1]-20 or y > clip[3]: return current
            if anchors & 8: x -= self.measure(text)
            elif anchors & 1: x -= self.measure(text)//2
            if anchors & 32: y -= self.height
            elif anchors & 2: y -= self.height//2
            i = 0
            while i < len(text):
                c = text[i]
                if c == '{': current = 1
                elif c == '}': current = base
                elif c == '~':
                    i += 1; icon = int(text[i]); start,_ = self.icons.animations[icon]
                    frame,_,dx,dy,flags = self.icons.animation_frames[start+tick%COUNTS[icon]]
                    blit(self.image(True,frame,0,flags&3),x+OFFSETS[icon]+dx,y+self.height-HEIGHTS[icon]+dy)
                    x += self.font.modules[0].width+WIDTHS[icon]+OFFSETS[icon]//2
                else:
                    frame = glyph(c); blit(self.image(False,frame,current),x,y)
                    x += self.font.frame_rects[frame][2]-1
                i += 1
            return current

        def block(text,x,y,wrap_width,anchors,base):
            text = '}'+text; ends = self.wrap(text,wrap_width,base); text = text.upper(); spacing = self.height+5
            if anchors & 32: y -= spacing*(len(ends)-1)
            elif anchors & 2: y -= spacing*(len(ends)-1)//2
            start, current = 0, base
            for row,end in enumerate(ends):
                current = line(text[start:end],x,y+row*spacing,anchors,base,current); start = end+1

        if host:
            line('SOUL OF DARKNESS',width//2,28,1,palette,palette)
            block('Native bitmap font preview. {Palette controls} and inline icons: ~0 ~1 ~2 ~3^The viewport width determines where these words wrap.',16,65,width-32,0,palette)
            line('ASSET VIEWER - GAMEPLAY NOT PORTED',width//2,280,1,0,0)
            line('F8 WIDTH  F9 FPS  ESC EXIT',width//2,300,1,0,0)
        elif wrapped:
            block('Soul {of} darkness ~0 ~1 ~2 ~3',width//2,80,width-32,anchor,palette)
        else: line('SOUL {OF} DARKNESS ~0 ~1 ~2 ~3',width//2,80,anchor,palette,palette)
        return binascii.crc32(struct.pack(f'>{len(pixels)}I',*pixels)) & 0xffffffff


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe',required=True,type=Path)
    parser.add_argument('--port-exe',type=Path)
    parser.add_argument('--data',type=Path,default=DEFAULT_DATA)
    parser.add_argument('--oracle',type=Path,default=ROOT/'soul/analysis/font-oracle.tsv')
    args = parser.parse_args()
    oracle = args.oracle.read_text(encoding='utf-8').splitlines()
    native = subprocess.check_output([str(args.exe),'--data',str(args.data),'--font-manifest'],text=True).splitlines()
    metrics = [row for row in native if row.startswith(('G\t','F\t'))]
    require(metrics == oracle,'Native font metrics differ from original Java oracle')
    require(len(metrics)==334,'Incomplete font metrics oracle')
    reference = Reference(args.data)
    for row in oracle:
        fields = row.split('\t')
        if fields[0] == 'G': require(glyph(chr(int(fields[1])))==int(fields[2]),'Python glyph differs from Java')
        else:
            text = bytes.fromhex(fields[1]).decode('utf-16-be')
            require(reference.measure(text)==int(fields[4]),'Python measure differs from Java')
            require(reference.wrap(text,int(fields[3]),int(fields[2]))==list(map(int,fields[5:])),'Python wrap differs from Java')
    expected = ['\t'.join(map(str,('R',width,palette,anchor,int(wrapped),tick,reference.render(width,palette,anchor,wrapped,tick))))
                for width in (240,569) for palette in (0,1,2) for anchor in (0,1,8,32,3) for wrapped in (False,True) for tick in (0,1,4,17)]
    require(native[len(metrics):] == expected,'Native text pixels differ from Python composition')
    print(f'PASS: {len(metrics)} original-Java font rows and {len(expected)} Python render surfaces')
    if args.port_exe:
        for width in (240,569):
            checksum = reference.render(width,0,0,True,60,host=True)
            for fps in (30,60,144):
                for interpolate in (True,False):
                    command = [str(args.port_exe),'--font-preview','--data',str(args.data),'--headless','--width',str(width),'--fps',str(fps),'--frames',str(5*fps)]
                    if not interpolate: command += ['--no-interpolation']
                    values = dict(item.split('=') for item in subprocess.check_output(command,text=True).split())
                    require(int(values['ticks'])==60 and int(values['font_tick'])==60,'Font icon clock differs from simulation')
                    require(int(values['font_crc'])==checksum,'Font preview pixels or clock differ')
        print('PASS: 12 font preview FPS/interpolation checks')


if __name__ == '__main__': main()
