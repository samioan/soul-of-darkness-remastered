"""Verify magic ability, chest reward, beam/tutorial clocks and the live gate chain."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe',required=True,type=Path)
    p.add_argument('--port-exe',type=Path)
    p.add_argument('--data',type=Path,default=DEFAULT_DATA)
    p.add_argument('--oracle',type=Path,default=ROOT/'soul/analysis/ability-oracle.tsv')
    p.add_argument('--modal-oracle',type=Path,default=ROOT/'soul/analysis/ability-modal-oracle.tsv')
    a=p.parse_args()
    expected=a.oracle.read_text(encoding='utf-8').splitlines()+a.modal_oracle.read_text(encoding='utf-8').splitlines()
    require(len(expected)==2002,'Incomplete ability/chest/modal oracle')
    require(len([r for r in expected if r.startswith('A\t')])==1440,'Missing ability handler cases')
    require(len([r for r in expected if r.startswith('M\t')])==504,'Missing beam/tutorial cases')
    drops=[r.split('\t') for r in expected if r.startswith('D\t')]
    require(len(drops)==2 and drops[0][3:6]==['48','27','2'] and drops[1][3:6]==['35','33','0'],'Missing magic drop and repeat-unlock health fallback')
    actual=subprocess.check_output([str(a.exe),'--data',str(a.data),'--ability-manifest'],text=True).splitlines()
    require(len(actual)==len(expected),'Ability row count differs')
    for i,(want,got) in enumerate(zip(expected,actual)):
        require(want==got,f'Ability row {i} differs:\nJava: {want}\nC++:  {got}')
    print('PASS: 2002 original-bytecode ability/chest/beam/tutorial snapshots, including repeated-unlock health fallback')
    if not a.port_exe:return

    def run(width,fps,frames,game=False,interpolate=True):
        mode=['--game','--game-ability-script'] if game else ['--movement-preview','--ability-script']
        cmd=[str(a.port_exe),*mode,'--headless','--data',str(a.data),'--width',str(width),'--fps',str(fps),'--frames',str(frames)]
        if not interpolate:cmd+=['--no-interpolation']
        return dict(x.split('=') for x in subprocess.check_output(cmd,text=True).split())

    for game in (False,True):
        reference=None
        for width in (240,569):
            crc=None
            for fps in (30,60,144):
                for interpolate in (True,False):
                    v=run(width,fps,fps*(20 if game else 15),game,interpolate)
                    require(v['opened_containers']=='1' and v['abilities']=='1' and v['abilities_active']=='0' and v['magic_unlocked']=='1' and v['ability_unlocks']=='1' and v['ability_completed']=='1','Chest ability chain failed')
                    require(v['ability_modal']=='-1' and v['ability_beam']=='39' and v['gate_triggers_active']=='0' and v['gate_script_tick']=='-2' and v['gate_y']=='1415' and v['gate_action']=='103','Ability completion did not release chest/trigger/gate chain')
                    require(v['player_health']=='60' and v['player_magic']=='60' and v['player_animation']=='0' and v['health_fragments']=='0' and v['magic_fragments']=='0' and v['rewards_completed']=='0','Ability reward incorrectly changed crystal/player state')
                    if game:require(v['frontend_state']=='8','Startup fixture did not return to gameplay')
                    state={k:val for k,val in v.items() if k not in ('frames','width','movement_crc','frontend_crc')}
                    if reference is None:reference=state
                    require(reference==state,'Width/FPS/interpolation changed magic reward or world state')
                    if crc is None:crc=v['movement_crc']
                    require(crc==v['movement_crc'],'FPS/interpolation changed ability/gate pixels')
    for frames,mode,completed in ((10,'3','0'),(50,'14','0'),(60,'-1','1')):
        v=run(569,'native',frames)
        require(v['ability_modal']==mode and v['ability_completed']==completed,'Missing chase/beam/tutorial/resume milestone')
    for width in (240,569):
        crc=None
        for fps in (30,60,144):
            for interpolate in (True,False):
                v=run(width,fps,fps*14,True,interpolate)
                require(v['ability_modal']=='14' and v['ability_completed']=='0' and v['frontend_state']=='8','Game tutorial timing changed')
                if crc is None:crc=v['frontend_crc']
                require(crc==v['frontend_crc'],'FPS/interpolation changed translated tutorial/HUD pixels')
    print('PASS: 24 live chest/magic/tutorial/gate runs and 12 translated tutorial/HUD captures at widths 240/569 across FPS/interpolation settings')


if __name__=='__main__':main()
