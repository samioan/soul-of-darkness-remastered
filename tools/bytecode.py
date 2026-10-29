"""Dump original bytecode for checking decompiler artifacts (output ignored)."""
import argparse
import subprocess
from pathlib import Path
from decompile import find_java

ROOT = Path(__file__).resolve().parent.parent
parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('classname', nargs='?', default='e')
args = parser.parse_args()
java = Path(find_java())
javap = java.with_name('javap.exe' if java.suffix == '.exe' else 'javap')
result = subprocess.check_output([str(javap), '-c', '-p', '-s', '-classpath',
                                 str(ROOT / 'soul/extracted'), args.classname], text=True)
out = ROOT / 'soul/analysis'
out.mkdir(exist_ok=True)
dest = out / (args.classname.replace('/', '_') + '.bytecode.txt')
dest.write_text(result, encoding='utf-8')
print(dest)
