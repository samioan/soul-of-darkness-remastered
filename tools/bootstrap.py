"""Provision pinned tools from sibling projects, or download Vineflower."""
import hashlib
import shutil
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
TOOLS = ROOT / 'tools'
PINS = {
    'vineflower.jar': '1dfcfe974395734fa467ce620661c7623d05ba83670de0529b1fbd63ff548b9d',
    'midp-stubs/cldcapi11.jar': 'f86f41bfb4097e4bd9cdfeca528960f6977cbda0f5785342adef9909a6a3baef',
    'midp-stubs/midpapi20.jar': 'b7f6ed5e0344c7000643ecc114e6edff3587e8e77face546af0e12ffd6dbe826',
    'midp-stubs/nokiaui.jar': 'f2ed0f8526a5788091d1fd97d4e474c2ff777a1d94fade8f8396c9e5931ea9a8',
}
URL = 'https://github.com/Vineflower/vineflower/releases/download/1.12.0/vineflower-1.12.0.jar'


def valid(path, digest):
    return path.is_file() and hashlib.sha256(path.read_bytes()).hexdigest() == digest


def main():
    for name, digest in PINS.items():
        dest = TOOLS / name
        if not valid(dest, digest):
            dest.parent.mkdir(parents=True, exist_ok=True)
            for sibling in ('gow-betrayal-decomp', 'rac-j2me-decomp', 'tes-travels-decomp'):
                source = ROOT.parent / sibling / 'tools' / name
                if valid(source, digest):
                    shutil.copy2(source, dest)
                    break
            else:
                if name != 'vineflower.jar':
                    raise SystemExit(f'Provide {dest}: matching MIDP/CLDC/Nokia API stubs from a reference project are required. See tools/README.md.')
                with urllib.request.urlopen(URL, timeout=120) as response:
                    data = response.read()
                if hashlib.sha256(data).hexdigest() != digest:
                    raise SystemExit('Vineflower download checksum mismatch')
                dest.write_bytes(data)
        print(f'OK {name} (SHA256 verified)')
    jar = ROOT / 'roms/Soul-of-Darkness_J2ME_EN_v137.jar'
    supplied = ROOT / jar.name
    if not jar.exists() and supplied.exists():
        jar.parent.mkdir(exist_ok=True)
        shutil.copy2(supplied, jar)
    from decompile import find_java
    print(f'Java 17+: {find_java()}')


if __name__ == '__main__':
    main()
