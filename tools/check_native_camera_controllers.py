"""Verify first-level camera controllers against original bytecode and the live host."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe',type=Path,required=True)
    p.add_argument('--port-exe',type=Path)
    p.add_argument('--data',type=Path,default=DEFAULT_DATA)
    a=p.parse_args()
    expected=(ROOT/'soul/analysis/camera-controller-oracle.tsv').read_text().splitlines()
    actual=subprocess.check_output([str(a.exe),'--data',str(a.data),'--camera-controller-manifest'],text=True).splitlines()
    require(len(expected)==2008 and len(actual)==len(expected),'Camera-controller oracle row count differs')
    require({r.split('\t')[1] for r in expected if r.startswith('C\t')}=={'63','167','125'},'Missing shipped camera controller')
    for i,(j,n) in enumerate(zip(expected,actual),1):require(j==n,f'Camera-controller row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 1920 handler/camera rows (level-0 controllers 63/167 and level-1 controller 125) plus 80 original world/order rows and eight eligibility rows')
    if not a.port_exe:return
    def run(width,fps,frames,game=False,interpolation=True):
        cmd=[str(a.port_exe),'--data',str(a.data),'--headless','--width',str(width),'--fps',str(fps),'--frames',str(frames)]
        cmd+=['--game','--game-camera-script'] if game else ['--movement-preview','--camera-script']
        if not interpolation:cmd+=['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd,text=True).split())
    for width in (240,569):
        for frame,uid,stage in ((8,'63','0'),(18,'-1','1'),(32,'167','2'),(47,'-1','3'),(65,'-1','4')):
            v=run(width,'native',frame)
            require((v['camera_uid'],v['camera_fixture_stage'])==(uid,stage),'Camera activation/release milestone differs')
            require(v['camera63_alive']==('1' if frame<18 else '0'),'Linked-source removal did not remove camera 63')
            if uid!='-1':require(v['camera_active']=='1','Selected camera is inactive')
    print('PASS: ten linked/falling camera activation and release host milestones at 240/569')
    for width in (240,569):
        for tick,uid in ((8,'63'),(32,'167')):
            baseline=None
            crc=None
            for fps in (30,60,144):
                for interpolation in (True,False):
                    v=run(width,fps,fps*(2 if tick==8 else 8)//3,False,interpolation)
                    require(v['camera_uid']==uid,'Active camera render milestone differs')
                    state={k:val for k,val in v.items() if k not in ('frames','width','movement_crc','frontend_crc')}
                    if baseline is None:baseline=state
                    require(state==baseline,'FPS/interpolation changed an active camera frame')
                    if crc is None:crc=v['movement_crc']
                    require(v['movement_crc']==crc,'FPS/interpolation changed active camera pixels')
    print('PASS: 24 active-controller render runs at both widths across FPS/interpolation')

    for game in (False,True):
        baseline=None
        for width in (240,569):
            crc=None
            for fps in (30,60,144):
                for interpolation in (True,False):
                    v=run(width,fps,fps*(18 if game else 8),game,interpolation)
                    require((v['camera_uid'],v['camera_active'],v['camera63_alive'],v['camera_fixture_stage'])==('-1','0','0','4'),'Host did not finish both camera branches')
                    require((v['camera63_ticks'],v['camera167_ticks'],v['camera_controllers'])==('9','19','2'),'World camera dispatch tick counts differ')
                    require(v['defeated']=='0','Controlled camera fixture was defeated')
                    state={k:val for k,val in v.items() if k not in ('frames','width','movement_crc','frontend_crc')}
                    if baseline is None:baseline=state
                    require(state==baseline,'Width/FPS/interpolation changed controller simulation')
                    if crc is None:crc=v['movement_crc']
                    require(crc==v['movement_crc'],'FPS/interpolation changed camera pixels')
        print('PASS: twelve '+('game' if game else 'raw')+' host runs across widths/FPS/interpolation')

if __name__=='__main__':main()
