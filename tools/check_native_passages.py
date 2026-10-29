"""Check type-59 links, the first blocked route, and widescreen room isolation."""
import argparse
from pathlib import Path
import subprocess
import tempfile
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe',required=True,type=Path)
    p.add_argument('--port-exe',type=Path)
    p.add_argument('--data',type=Path,default=DEFAULT_DATA)
    p.add_argument('--oracle',type=Path,default=ROOT/'soul/analysis/passage-oracle.tsv')
    a=p.parse_args()
    expected=a.oracle.read_text(encoding='utf-8').splitlines()
    names={r.split('\t')[1] for r in expected}
    require(len(names)==29 and len(expected)==464,'Incomplete passage oracle')
    require(len([n for n in names if n.startswith('level-')])==16,'Missing shipped passage links')
    actual=subprocess.check_output([str(a.exe),'--data',str(a.data),'--passage-manifest'],text=True).splitlines()
    require(len(actual)==len(expected),'Passage row count differs')
    for i,(want,got) in enumerate(zip(expected,actual)):
        require(want==got,f'Passage row {i} differs:\nJava: {want}\nC++:  {got}')
    print('PASS: 464 original-bytecode passage/player snapshots; 13 boundary cases and all 16 first-level links')
    if not a.port_exe:return
    reference=None
    images={}
    with tempfile.TemporaryDirectory(prefix='sod-passages-') as tmp:
        for game in (False,True):
            reference=None
            for width in (240,569):
                crc=None
                for fps in (30,60,144):
                    for interpolate in (True,False):
                        mode=['--game','--game-passage-script'] if game else ['--movement-preview','--passage-script']
                        seconds=10 if game else 1
                        cmd=[str(a.port_exe),*mode,'--data',str(a.data),'--headless','--width',str(width),'--fps',str(fps),'--frames',str(fps*seconds)]
                        dump=Path(tmp)/f'{width}.ppm'
                        capture=game and fps==60 and interpolate
                        if capture:cmd+=['--dump',str(dump)]
                        if not interpolate:cmd+=['--no-interpolation']
                        v=dict(x.split('=') for x in subprocess.check_output(cmd,text=True).split())
                        require(v['passages']=='16' and v['passage_transfers']=='1' and v['last_passage']=='81' and v['region']=='1','First route did not reach its linked room')
                        require(int(v['player_x'])>1542 and v['player_health']=='60' and v['player_hazard']=='0','First route left invalid player state')
                        if game:require(v['frontend_state']=='8','Game startup did not reach live passage fixture')
                        state={k:val for k,val in v.items() if k not in ('width','frames','movement_crc','frontend_crc')}
                        if reference is None:reference=state
                        require(reference==state,'Width/FPS/interpolation changed passage state')
                        if crc is None:crc=v['movement_crc']
                        require(crc==v['movement_crc'],'FPS/interpolation changed passage pixels')
                        if capture:
                            data=dump.read_bytes().split(b'\n',3)[3]
                            require(len(data)==width*320*3,'Invalid gameplay capture')
                            images[width]=data
        # Portrait and widescreen share world coordinates at this destination.
        # Compare the shared room below the HUD and require plain margins.
        for y in range(40,320):
            portrait=images[240][y*240*3:(y+1)*240*3]
            wide=images[569][(y*569+144)*3:(y*569+384)*3]
            require(portrait==wide,'Shared room pixels stretch or change with viewport width')
            for left,right in ((0,144),(424,569)):
                require(images[569][(y*569+left)*3:(y*569+right)*3]==bytes((16,32,48))*(right-left),'Packed neighboring room leaked into widescreen margins')
    print('PASS: 24 first-passage host runs, 240/569 room pixels and hidden packed neighbors across FPS/interpolation settings')


if __name__=='__main__':main()
