"""Compare script 160, trigger 161 and actor handlers with original Java."""
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
    expected=(ROOT/'soul/analysis/chandelier-oracle.tsv').read_text().splitlines()
    actual=subprocess.check_output([str(a.exe),'--data',str(a.data),'--chandelier-manifest'],text=True).splitlines()
    require(len(expected)==3000 and len(actual)==len(expected),'Chandelier oracle row count differs')
    for i,(j,n) in enumerate(zip(expected,actual),1):require(j==n,f'Chandelier row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 3000 original-bytecode script/trigger/actor snapshots across ten timelines')

    if a.port_exe:
        for width in (240,569):
            for frames,x,tick,frozen,action in ((25,'117','4',True,'101'),(50,'141','-2',False,'102')):
                cmd=[str(a.port_exe),'--data',str(a.data),'--headless','--movement-preview','--chandelier-script','--width',str(width),'--fps','60','--frames',str(frames)]
                v=dict(x.split('=') for x in subprocess.check_output(cmd,text=True).split())
                require((v['player_x'],v['chandelier_tick'],v['chandelier34_action'],v['player_health'])==(x,tick,action,'60'),'Selected-script activation/movement milestone differs')
                require(bool(int(v['chandelier205_flags'])&16384)==frozen,'Enemy freeze/release tick differs')
        print('PASS: four mid-sequence player-movement, enemy-freeze/release and chandelier-fall milestones')
        for game in (False,True):
            reference=None
            for width in (240,569):
                crc=None
                for fps in (30,60,144):
                    for interpolation in (True,False):
                        cmd=[str(a.port_exe),'--data',str(a.data),'--headless','--width',str(width),'--fps',str(fps),'--frames',str(fps*(14 if game else 4))]
                        cmd+=['--game','--game-chandelier-script'] if game else ['--movement-preview','--chandelier-script']
                        if not interpolation:cmd+=['--no-interpolation']
                        v=dict(x.split('=') for x in subprocess.check_output(cmd,text=True).split())
                        require((v['chandelier_tick'],v['chandelier_starts'],v['chandelier_finishes'],v['chandelier_trigger_alive'])==('-2','1','1','0'),'Host did not release completed script 160/trigger 161')
                        require((v['prompt_removal_triggers'],v['prompt59_alive'],v['prompt187_alive'])==('1','0','1'),'Linked chandelier trigger did not remove only prompt 59')
                        require((v['chandelier34_action'],v['chandelier34_y'])==('104','901'),'Chandelier did not fall, land and finish its break animation')
                        require(int(v['chandelier205_flags'])&16384==0,'Enemy remained frozen after script completion')
                        require(v['defeated']=='0' and v['intro_active']=='0' and v['melee_attacks']=='0' and v['fire_casts']=='0','Unexpected modal, attack or death in chandelier fixture')
                        require(int(v['player_x'])>141,'Normal movement/damage handlers did not continue during selected script')
                        state={k:val for k,val in v.items() if k not in ('frames','width','movement_crc','frontend_crc')}
                        if reference is None:reference=state
                        require(reference==state,'Width/FPS/interpolation changed chandelier host state')
                        if crc is None:crc=v['movement_crc']
                        require(crc==v['movement_crc'],'FPS/interpolation changed chandelier host pixels')
        print('PASS: 24 raw/game host runs at widths 240/569 and 30/60/144 FPS, with interpolation on/off')

if __name__=='__main__':main()
