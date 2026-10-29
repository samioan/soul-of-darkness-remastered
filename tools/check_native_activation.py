"""Verify first-wave enable timing, terminal enemy lifetime and real arena combat."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe',type=Path,required=True)
    p.add_argument('--port-exe',type=Path)
    p.add_argument('--data',type=Path,default=DEFAULT_DATA)
    p.add_argument('--oracle',type=Path,default=ROOT/'soul/analysis/activation-oracle.tsv')
    a=p.parse_args();expected=a.oracle.read_text(encoding='utf-8').splitlines()
    require(len(expected)==2146,'Incomplete activation oracle')
    require(sum(r.startswith('T\t') for r in expected)==156 and sum(r.startswith('D\t') for r in expected)==960 and sum(r.startswith('Q\t') for r in expected)==256,'Missing handler, terminal lifetime or actual world queue cases')
    actual=subprocess.check_output([str(a.exe),'--data',str(a.data),'--activation-manifest'],text=True).splitlines()
    require(len(actual)==len(expected),'Activation row count differs')
    for i,(want,got) in enumerate(zip(expected,actual)):
        require(want==got,f'Activation row {i} differs:\nJava: {want}\nC++:  {got}')
    print('PASS: 2146 original-bytecode trigger, world queue/enemy, terminal lifetime and random-stream rows')
    if not a.port_exe:return

    def run(width,fps,frames,game=False,interpolate=True):
        mode=['--game','--game-activation-script'] if game else ['--movement-preview','--activation-script']
        cmd=[str(a.port_exe),*mode,'--data',str(a.data),'--headless','--width',str(width),'--fps',str(fps),'--frames',str(frames)]
        if not interpolate:cmd+=['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd,text=True).split())

    for frames,activations,locked in ((1,'0','2'),(2,'1','0')):
        v=run(569,'native',frames)
        require(v['player_enable_activations']==activations and v['wave1_locked']==locked,'Arena entry did not preserve frozen eligibility/next-snapshot timing')
    for game in (False,True):
        reference=None
        for width in (240,569):
            crc=None
            for fps in (30,60,144):
                for interpolate in (True,False):
                    v=run(width,fps,fps*(30 if game else 20),game,interpolate)
                    require(v['enemies']=='20' and v['dormant_enemies']=='0','Supported type-1 placement count differs')
                    require(v['player_enable_triggers']=='1' and v['player_enable_active']=='0' and v['player_enable_activations']=='1' and v['wave1_alive']=='0' and v['wave1_locked']=='0','First wave did not activate once and remove after final defeats')
                    require(v['enemy_kills']=='4' and int(v['enemy_hits'])>=4 and v['player_health']=='60','Fixture did not defeat both enemies and their allowed respawns through melee')
                    require(v['opened_containers']=='0' and v['magic_unlocked']=='0' and v['gate_script_tick']=='-1' and v['gate_y']=='1500','First-wave slice bypassed the missing type-2/chest unlock chain')
                    if game:require(v['frontend_state']=='8','Game fixture did not return from startup/pause to live arena')
                    state={k:val for k,val in v.items() if k not in ('frames','width','movement_crc','frontend_crc')}
                    if reference is None:reference=state
                    require(state==reference,'Width/FPS/interpolation changed arena state')
                    if crc is None:crc=v['movement_crc']
                    require(v['movement_crc']==crc,'FPS/interpolation changed arena pixels')
    print('PASS: 24 first-level arena runs at widths 240/569, 30/60/144 FPS and both interpolation settings; real melee/respawns/final removal, no chest bypass')


if __name__=='__main__':main()
