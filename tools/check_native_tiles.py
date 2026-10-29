"""Compare native map tables with original Java and tile surfaces with Python."""
import argparse
import binascii
from functools import lru_cache
from pathlib import Path
import struct
import subprocess
from bsprite import Sprite, decode_png
from game_assets import DEFAULT_DATA, ROOT, require, resources
from parse_levels import validate_maps


def fingerprint(data):
    value=0xcbf29ce484222325
    for byte in data: value=((value^byte)*0x100000001b3)&0xffffffffffffffff
    return f'{value:016x}'


def binding(level):
    first=(3,3,7,7,7,3,11,11,3,3,7,11)[level]
    return first,0,first+1,0


def metrics(data):
    for pack in range(3,15):
        parts=dict(resources(data/str(pack))); gameplay=pack<=11
        validate_maps(parts,gameplay,f'pack {pack}')
        for index in range(0 if gameplay else 1,3):
            resource=2+index*3 if gameplay else (index-1)*3
            width,height=struct.unpack('<HH',parts[resource+1])
            cells=bytes(0 if b==255 else b for b in parts[resource]) if index==0 else parts[resource]
            transforms='-' if index==0 else fingerprint(parts[resource+2])
            yield '\t'.join(map(str,('M',pack,index,width,height,fingerprint(cells),transforms)))
    for level in range(12): yield '\t'.join(map(str,('B',level,*binding(level))))


class Scene:
    def __init__(self,data,level):
        parts=dict(resources(data/str(level+3)))
        self.width,self.height=struct.unpack('<HH',parts[9])
        self.cells=(parts[5],parts[8]); self.transforms=(parts[7],parts[10])
        images=dict(resources(data/'16')); b=binding(level)
        self.atlases=[decode_png(Sprite.parse(images[b[i]]).png_images[0][b[i+1]]) for i in (0,2)]

    @lru_cache(maxsize=1024)
    def tile(self,layer,index,flip):
        width,_,pixels=self.atlases[layer]
        x=index*20%width; y=index*20//width*20
        return [pixels[(y+(19-py if flip&2 else py))*width+x+(19-px if flip&1 else px)] for py in range(20) for px in range(20)]

    def render(self,width,x,y,clipped,mask):
        pixels=[0xff102030]*(width*320)
        clip=(13,17,width-18,290) if clipped else (0,0,width,320)
        # Independent pixel-oriented lookup: each visible screen pixel resolves
        # its world cell and source coordinate, without native tile traversal.
        for py in range(clip[1],clip[3]):
            wy=y+py; ty=wy//20
            if not 0<=ty<self.height: continue
            for px in range(clip[0],clip[2]):
                wx=x+px; tx=wx//20
                if not 0<=tx<self.width: continue
                cell=ty*self.width+tx
                for layer in (0,1):
                    index=self.cells[layer][cell]
                    if not mask&(1<<layer) or index==255: continue
                    flip=(self.transforms[layer][cell//4]>>((3-cell%4)*2))&3
                    color=self.tile(layer,index,flip)[wy%20*20+wx%20]
                    if color>>24:
                        require(color>>24==255,'tile oracle needs explicit partial alpha handling')
                        pixels[py*width+px]=color
        return binascii.crc32(struct.pack(f'>{len(pixels)}I',*pixels))&0xffffffff

    def camera(self,x,y,width):
        return min(max(0,x),max(0,self.width*20-width)),min(max(0,y),max(0,self.height*20-320))


def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe',required=True,type=Path)
    parser.add_argument('--port-exe',type=Path)
    parser.add_argument('--data',type=Path,default=DEFAULT_DATA)
    parser.add_argument('--oracle',type=Path,default=ROOT/'soul/analysis/map-oracle.tsv')
    args=parser.parse_args()
    expected=list(metrics(args.data))
    require(len(expected)==45 and expected==args.oracle.read_text(encoding='utf-8').splitlines(),'Python maps differ from original Java oracle')
    native=subprocess.check_output([str(args.exe),'--data',str(args.data),'--tile-manifest'],text=True).splitlines()
    require(native[:45]==expected,'Native maps differ from original Java oracle')
    rendered=[]; scenes=[Scene(args.data,level) for level in range(9)]
    for level,scene in enumerate(scenes):
        for width in (240,569):
            for position in range(3):
                x,y=((-13,-7),(scene.width*10-11,scene.height*10-9),(scene.width*20-width+17,scene.height*20-320+13))[position]
                for clipped in (0,1):
                    for mask in (1,2,3):
                        rendered.append('\t'.join(map(str,('R',level,width,position,clipped,mask,scene.render(width,x,y,clipped,mask)))))
    require(native[45:]==rendered,'Native tile pixels differ from Python')
    print(f'PASS: 45 original-Java map/binding rows and {len(rendered)} tile surfaces')
    if args.port_exe:
        runs=0
        # All levels and both widths, including tall/narrow and pack 9 mismatch.
        for level,scene in enumerate(scenes):
            for width in (240,569):
                x,y=scene.camera(77,scene.height*20-319,width)
                checksum=scene.render(width,x,y,0,3)
                rates=(30,60,144) if level==0 else (60,)
                for fps in rates:
                    for interpolate in ((True,False) if level==0 else (True,)):
                        command=[str(args.port_exe),'--headless','--level-preview',str(level),'--data',str(args.data),'--width',str(width),'--camera-x','77','--camera-y',str(scene.height*20-319),'--fps',str(fps),'--frames',str(fps*5)]
                        if not interpolate: command+=['--no-interpolation']
                        values=dict(item.split('=') for item in subprocess.check_output(command,text=True).split())
                        require(int(values['ticks'])==60,'Tile preview simulation tick count differs')
                        require((int(values['camera_x']),int(values['camera_y']))==(x,y),'Tile preview camera differs')
                        require(int(values['tile_crc'])==checksum,'Tile preview pixels differ')
                        runs+=1
        print(f'PASS: {runs} actual level previews (all nine levels, widths 240/569; level 0 FPS/interpolation)')


if __name__=='__main__': main()
