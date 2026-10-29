"""Compare native movement snapshots with original Java and record streams with Python."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA,ROOT,require,resources
from parse_objects import object_records


def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe',type=Path,required=True)
    parser.add_argument('--port-exe',type=Path)
    parser.add_argument('--data',type=Path,default=DEFAULT_DATA)
    parser.add_argument('--oracle',type=Path,default=ROOT/'soul/analysis/player-core-oracle.tsv')
    args=parser.parse_args()
    expected=args.oracle.read_text(encoding='utf-8').splitlines()
    require(len(expected)==1200,'Incomplete player core oracle')
    actual=subprocess.check_output([str(args.exe),'--data',str(args.data),'--player-manifest'],text=True).splitlines()
    for i,(first,second) in enumerate(zip(expected,actual)):
        require(first==second,f'Native player mismatch at row {i+1}:\nJava: {first}\nC++:  {second}')
    require(len(actual)>=len(expected),'Native player manifest incomplete')
    rows=[]
    for level in range(9):
        shared=[0]*20
        for offset,record in object_records(dict(resources(args.data/str(level+3)))[0]):
            shared[:len(record)]=record
            rows.append('\t'.join(map(str,('R',level,offset,len(record),*shared))))
    require(len(rows)==930 and actual[len(expected):]==rows,'Native actor record stream differs from Python')
    print('PASS: 1200 original-Java movement snapshots (600 ticks) and 930 actor records with reused parameter tails')
    if args.port_exe:
        count=0; state=None
        for width in (240,569):
            checksum=None
            for fps in (30,60,144):
                for interpolate in (True,False):
                    command=[str(args.port_exe),'--movement-preview','--movement-script','--data',str(args.data),'--headless','--width',str(width),'--fps',str(fps),'--frames',str(fps*5)]
                    if not interpolate:command+=['--no-interpolation']
                    result=dict(item.split('=') for item in subprocess.check_output(command,text=True).split())
                    require(int(result['ticks'])==60,'Movement prototype tick count differs')
                    require(result['player_hazard']=='0','Movement validation script hit an unported hazard')
                    current={k:v for k,v in result.items() if k.startswith('player_')}
                    if state is None:state=current
                    require(current==state,'Presentation rate/width altered movement simulation')
                    if checksum is None:checksum=result['movement_crc']
                    require(result['movement_crc']==checksum,'Presentation rate/interpolation altered movement pixels')
                    require(int(result['player_x'])>121,'Scripted movement failed to leave spawn')
                    count+=1
        print(f'PASS: {count} real-level movement runs at widths 240/569, 30/60/144 FPS, both interpolation settings')


if __name__=='__main__':main()
