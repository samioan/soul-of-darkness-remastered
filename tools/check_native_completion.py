"""Verify exit 173 and state-15 bars/input, excluding finishLevel progression."""
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
    expected=(ROOT/'soul/analysis/completion-oracle.tsv').read_text().splitlines()
    actual=subprocess.check_output([str(a.exe),'--data',str(a.data),'--completion-manifest'],text=True).splitlines()
    require(len(expected)==1008 and len(actual)==len(expected),'Completion oracle row count differs')
    for i,(j,n) in enumerate(zip(expected,actual),1):require(j==n,f'Completion row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 1008 original-bytecode exit/bar/input snapshots across 21 scenarios')
    if not a.port_exe:return
    def run(width,fps,frames,game=False,wait=False,interpolation=True):
        cmd=[str(a.port_exe),'--data',str(a.data),'--headless','--width',str(width),'--fps',str(fps),'--frames',str(frames)]
        flag='--game-exit-' if game else '--exit-'
        flag+='wait-script' if wait else 'script'
        cmd+=['--game',flag] if game else ['--movement-preview',flag]
        if not interpolation:cmd+=['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd,text=True).split())
    for width in (240,569):
        frozen=None
        for frames,state,bars in ((5,'8','0'),(12,'15','81'),(18,'15','160'),(24,'19','0'),(40,'19','0')):
            v=run(width,'native',frames)
            require((v['completion_state'],v['completion_bars'])==(state,bars),'Exit/bar/ack milestone differs')
            if frames>=12:
                motion=tuple(v[k] for k in ('player_x','player_y','player_vx','player_vy','player_animation','player_step','player_tick','player_health','player_magic'))
                if frozen is None:frozen=motion
                require(motion==frozen,'Gameplay continued during completion')
            if frames>=24:require((v['completion_starts'],v['completion_acks'])==('1','1'),'Completion repeated or did not acknowledge')
    print('PASS: ten exit/closing/message/ack milestones and frozen player state at widths 240/569')
    for game in (False,True):
        for wait in (False,True):
            baseline=None
            for width in (240,569):
                crc=None
                for fps in (30,60,144):
                    for interpolation in (True,False):
                        v=run(width,fps,fps*(18 if game else 8),game,wait,interpolation)
                        require((v['completion_state'],v['completion_starts'],v['completion_acks'],v['exit_triggers'])==('15' if wait else '19','1','0' if wait else '1','1'),'Exit flow did not reach its one-time endpoint')
                        require(v['completion_bars']==('160' if wait else '0') and v['defeated']=='0','Completion bars/death state differs')
                        require(v['player_x']=='1777' and v['player_y']=='799','Exit failed to freeze Kale after contact')
                        if game:require(v['frontend_state']==('15' if wait else '19'),'Frontend did not own the completion/selection state')
                        state={k:val for k,val in v.items() if k not in ('frames','width','movement_crc','frontend_crc')}
                        if baseline is None:baseline=state
                        require(state==baseline,'Width/FPS/interpolation changed completion simulation')
                        key='frontend_crc' if game else 'movement_crc'
                        if crc is None:crc=v[key]
                        require(crc==v[key],'FPS/interpolation changed completion pixels')
    print('PASS: 48 raw/game wait/ack host runs at widths 240/569 across FPS/interpolation')

if __name__=='__main__':main()
