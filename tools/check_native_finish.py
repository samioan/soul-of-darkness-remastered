"""Verify finishLevel progress, save migration, world unload and completed Continue."""
import argparse
from pathlib import Path
import subprocess
import tempfile
from game_assets import ROOT, DEFAULT_DATA, require

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe',type=Path,required=True)
    p.add_argument('--port-exe',type=Path)
    p.add_argument('--data',type=Path,default=DEFAULT_DATA)
    a=p.parse_args()
    expected=(ROOT/'soul/analysis/finish-oracle.tsv').read_text().splitlines()
    actual=subprocess.check_output([str(a.exe),'--data',str(a.data),'--finish-manifest'],text=True).splitlines()
    require(len(expected)==len(actual)==24,'Finish oracle row count differs')
    for i,(j,n) in enumerate(zip(expected,actual),1):require(j==n,f'Finish row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 24 original-bytecode finish/selection/Continue progress rows; Java separately checks full RMS and unload')
    if not a.port_exe:return
    def run(*args):
        r=subprocess.run([str(a.port_exe),'--data',str(a.data),'--headless',*map(str,args)],text=True,capture_output=True)
        require(r.returncode==0,r.stderr)
        return dict(x.split('=') for x in r.stdout.split()),r.stderr
    def saved(path):
        header,body=path.read_text().split('\n',1)
        require(header.startswith('SODPC 2 '),'Save was not written in version 2')
        return list(map(int,body.splitlines()[0].split()))
    def legacy():
        body='0 11 148 259 1 1 23 80 17 90 12 2 1 1 1 2\n11 9\n267 8\n'
        hash=2166136261
        for c in body.encode():hash=((hash^c)*16777619)&0xffffffff
        return f'SODPC 1 {hash}\n{body}'.encode()
    with tempfile.TemporaryDirectory(prefix='sod-finish-') as temp:
        path=Path(temp)/'checkpoint.sav'
        run('--movement-preview','--checkpoint-script','--save-file',path,'--fps','native','--frames',5)
        modern=path.read_bytes();old=legacy()
        path.write_bytes(old)
        menu,_=run('--game','--save-file',path,'--fps','native','--frames',48)
        require(menu['continue_available']=='1' and path.read_bytes()==old,'Reading legacy progress rewrote or lost it')
        baselines={}
        for width in (240,569):
            crcs={}
            for fps in (30,60,144):
                for interpolation in (True,False):
                    path.write_bytes(old if interpolation else modern)
                    options=['--save-file',path,'--width',width,'--fps',fps]
                    if not interpolation:options+=['--no-interpolation']
                    done,_=run('--game','--continue','--finish-script',*options,'--frames',fps*8)
                    require(tuple(done[k] for k in ('world_loaded','finish_count','world_unloads','progress_level','progress_completed','max_unlocked','selected_level','frontend_state'))==('0','1','1','0','1','1','1','19'),'Finish failed to save/unload/select next level')
                    fields=saved(path)
                    require(fields[:18]==[0,1,1,11,148,259,1,1,31,80,42,90,19,3,2,1,1,0],'Finish did not preserve checkpoint coordinates with current progression stats')
                    finished=path.read_bytes()
                    continued,_=run('--game','--continue',*options,'--frames',fps*2)
                    require(tuple(continued[k] for k in ('world_loaded','progress_level','progress_completed','max_unlocked','selected_level','frontend_state','progress_health','progress_magic'))==('0','0','1','1','1','19','31','42'),'Completed Continue restored obsolete checkpoint or entered unavailable level')
                    require(path.read_bytes()==finished and 'player_x' not in continued,'Completed Continue mutated save or constructed old world')
                    replay,_=run('--game','--continue','--replay-completed',*options,'--frames',fps*2)
                    require(tuple(replay[k] for k in ('world_loaded','progress_completed','max_unlocked','selected_level','frontend_state','player_health','player_magic','active_weapon','health_fragments','magic_fragments','player_collected','intro_active'))==('1','0','1','0','8','80','90','1','3','2','19','1'),'Replay did not construct a fresh first level with retained progress')
                    require(saved(path)[:6]==[0,1,0,11,121,259],'Replay checkpoint lost unlock, completion reset or fresh spawn position')
                    for name,v in (('finish',done),('continue',continued),('replay',replay)):
                        state={k:val for k,val in v.items() if k not in ('frames','width','movement_crc','frontend_crc')}
                        if name not in baselines:baselines[name]=state
                        require(state==baselines[name],'Display settings changed '+name+' state')
                        crc=v['frontend_crc']
                        if name not in crcs:crcs[name]=crc
                        require(crc==crcs[name],'Presentation settings changed '+name+' pixels')
        path.write_bytes(old)
        failed,error=run('--game','--continue','--finish-script','--fail-finish-save','--save-file',path,'--fps','native','--frames',96)
        require('Save:' in error and path.read_bytes()==old,'Failed completion write did not preserve prior record')
        require(failed['world_loaded']=='0' and failed['progress_completed']=='1' and failed['selected_level']=='1','Write failure prevented in-memory completion/unload')
        resumed,_=run('--game','--continue','--save-file',path,'--fps','native','--frames',12)
        require(resumed['world_loaded']=='1' and resumed['player_health']=='23' and resumed['player_magic']=='17' and resumed['progress_completed']=='0','Write failure destroyed original checkpoint Continue')
        path.unlink()
        noCheckpoint,_=run('--game','--game-exit-script','--save-file',path,'--fps','native','--frames',216)
        require(saved(path)[3]==-1 and noCheckpoint['progress_completed']=='1','Completion without checkpoint was not saved')
        cold,_=run('--game','--continue','--save-file',path,'--fps','native','--frames',3)
        require(cold['world_loaded']=='0' and cold['selected_level']=='1' and cold['progress_health']=='60' and cold['progress_magic']=='60','Cold completed Continue without checkpoint did not select defaults')
        reset,_=run('--game','--game-script','--save-file',path,'--fps','native','--frames',85)
        require(reset['frontend_state']=='20' and reset['continue_available']=='0' and reset['max_unlocked']=='0' and not path.exists(),'New Game did not clear completed progress')
    print('PASS: 36 cross-process finish/Continue/replay runs at 240/569 across FPS/interpolation, legacy migration, no-checkpoint completion, failure preservation and New Game reset')

if __name__=='__main__':main()
