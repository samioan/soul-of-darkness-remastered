"""Check native type-52 containers against original-bytecode snapshots and spawn events."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA, ROOT, require


def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe',type=Path,required=True)
    parser.add_argument('--port-exe',type=Path)
    parser.add_argument('--data',type=Path,default=DEFAULT_DATA)
    parser.add_argument('--oracle',type=Path,default=ROOT/'soul/analysis/container-oracle.tsv')
    args=parser.parse_args()
    expected=args.oracle.read_text(encoding='utf-8').splitlines()
    require({r.split('\t')[1] for r in expected if r.startswith('C\t')}=={'closed','open','leave','magic','fall','paused','early','locked'},'Incomplete container oracle')
    require(sum(r.startswith('D\t') for r in expected)==2,'Missing health/magic crystal spawn events')
    require({r.split('\t')[1] for r in expected if r.startswith('S\t')}=={'health_drop','magic_drop','health_crystal','magic_crystal','expired'},'Missing spawned pickup scenarios')
    actual=subprocess.check_output([str(args.exe),'--data',str(args.data),'--container-manifest'],text=True).splitlines()
    for i,(a,b) in enumerate(zip(expected,actual),1):
        require(a==b,f'Container mismatch at row {i}:\nJava: {a}\nC++:  {b}')
    require(len(actual)==len(expected),f'Container row count differs: native {len(actual)}, Java {len(expected)}')
    print(f'PASS: {len(actual)} original-Java container snapshots/events across eight chest and five spawned-pickup scenarios')
    if args.port_exe:
        reference=None
        for width in (240,569):
            checksum=None
            for fps in (30,60,144):
                for interpolate in (True,False):
                    command=[str(args.port_exe),'--movement-preview','--container-script','--data',str(args.data),'--headless','--width',str(width),'--fps',str(fps),'--frames',str(fps*10)]
                    if not interpolate: command+=['--no-interpolation']
                    values=dict(item.split('=') for item in subprocess.check_output(command,text=True).split())
                    require(values['opened_containers']=='1' and values['crystal_boundary']=='0' and values['rewards_completed']=='1' and values['health_fragments']=='1' and values['reward_active']=='0' and values['collection_effects']=='1' and values['active_effects']=='0','Host did not complete one chest/crystal reward and resume')
                    require(values['player_hazard']=='0' and values['player_animation']=='0' and int(values['player_x'])!=1302 and values['player_hits']=='3' and values['player_health']=='30','Chest host reached unexpected player state')
                    state={k:v for k,v in values.items() if k.startswith('player_') or k in ('collection_effects','active_effects','opened_containers','crystal_boundary','rewards_completed','health_fragments','magic_fragments','reward_active')}
                    if reference is None: reference=state
                    require(state==reference,'FPS/width altered chest/crystal state')
                    if checksum is None: checksum=values['movement_crc']
                    require(checksum==values['movement_crc'],'FPS/interpolation altered chest/crystal pixels')
        print('PASS: 12 controlled first-level chest runs at widths 240/569, 30/60/144 FPS and both interpolation settings')


if __name__=='__main__':main()
