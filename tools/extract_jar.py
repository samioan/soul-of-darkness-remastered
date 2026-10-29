"""Extract the supplied JAR safely; preserves the original bytes."""
import argparse
import hashlib
import json
import zipfile
from pathlib import Path, PurePosixPath

ROOT = Path(__file__).resolve().parent.parent
JAR = ROOT / 'roms/Soul-of-Darkness_J2ME_EN_v137.jar'
OUT = ROOT / 'soul/extracted'


def extract(jar=JAR):
    with zipfile.ZipFile(jar) as archive:
        entries = archive.infolist()
        for entry in entries:
            path = PurePosixPath(entry.filename)
            if path.is_absolute() or '..' in path.parts or '\\' in entry.filename or ':' in entry.filename:
                raise ValueError(f'Unsafe archive entry: {entry.filename}')
        if archive.testzip() is not None:
            raise ValueError('Archive CRC check failed')
        OUT.mkdir(parents=True, exist_ok=True)
        # Remove only previous extracted files, after validating the input archive.
        for old in OUT.rglob('*'):
            if old.is_file():
                old.unlink()
        archive.extractall(OUT)
        report = {'jar': Path(jar).name, 'sha256': hashlib.sha256(Path(jar).read_bytes()).hexdigest(),
                  'entries': [{'name': e.filename, 'size': e.file_size, 'crc32': f'{e.CRC:08x}'} for e in entries]}
        (ROOT / 'soul/inventory.json').write_text(json.dumps(report, indent=2)+'\n')
    print(f'Extracted {len(entries)} entries -> soul/extracted; wrote soul/inventory.json')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--jar', type=Path, default=JAR)
    extract(parser.parse_args().jar)
