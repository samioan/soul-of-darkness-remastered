"""Compare the first-level chandelier/prompt-removal links with original bytecode."""
import argparse
from pathlib import Path
import subprocess
from game_assets import ROOT, DEFAULT_DATA, require

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe',type=Path,required=True)
    p.add_argument('--data',type=Path,default=DEFAULT_DATA)
    a=p.parse_args()
    expected=(ROOT/'soul/analysis/prompt-removal-oracle.tsv').read_text().splitlines()
    actual=subprocess.check_output([str(a.exe),'--data',str(a.data),'--prompt-removal-manifest'],text=True).splitlines()
    require(len(expected)==352 and len(actual)==len(expected),'Prompt-removal oracle row count differs')
    for i,(j,n) in enumerate(zip(expected,actual),1):require(j==n,f'Prompt-removal row {i}:\nJava: {j}\nC++: {n}')
    print('PASS: 312 handler snapshots plus 40 original world/queue snapshots')

if __name__=='__main__':main()
