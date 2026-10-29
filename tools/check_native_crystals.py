"""Compare native crystal reward ticks with an original-bytecode state oracle."""
import argparse
from pathlib import Path
import subprocess
from game_assets import DEFAULT_DATA,ROOT,require

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe',type=Path,required=True)
    p.add_argument('--data',type=Path,default=DEFAULT_DATA)
    p.add_argument('--oracle',type=Path,default=ROOT/'soul/analysis/crystal-oracle.tsv')
    a=p.parse_args();expected=a.oracle.read_text(encoding='utf-8').splitlines()
    require(len(expected)==448,'Incomplete crystal reward oracle')
    actual=subprocess.check_output([str(a.exe),'--data',str(a.data),'--crystal-manifest'],text=True).splitlines()
    require(len(actual)==len(expected),'Crystal reward row count differs')
    for i,(x,y) in enumerate(zip(expected,actual),1): require(x==y,f'Crystal row {i}:\nJava: {x}\nC++: {y}')
    print('PASS: 448 original-bytecode reward/beam state snapshots; drawing hooks suppressed')
if __name__=='__main__':main()
