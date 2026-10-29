"""Compare the native container manifest with independent Python pack readers."""
import argparse
import struct
import subprocess
from pathlib import Path
from game_assets import DEFAULT_DATA, Reader, require, resources


def fingerprint(data):
    value = 14695981039346656037
    for byte in data:
        value = ((value ^ byte) * 1099511628211) & 0xffffffffffffffff
    return f'{value:016x}'


def expected_manifest(data_dir):
    rows = []
    for pack in range(1, 20):
        path = data_dir / str(pack)
        data = path.read_bytes()
        count = struct.unpack_from('<H', data)[0]
        payloads = dict(resources(path))
        rows.append(f'P\t{pack}\t{count}\t{len(data)}')
        for index in range(count):
            payload = payloads.get(index)
            rows.append(f'R\t{pack}\t{index}\t{int(payload is not None)}\t'
                        f'{len(payload) if payload is not None else 0}\t'
                        f'{fingerprint(payload) if payload is not None else "0000000000000000"}')
    data = (data_dir / 'dataIGP').read_bytes()
    reader = Reader(data, 'dataIGP')
    count = reader.u16()
    require(count >= 1, 'dataIGP: no sentinel')
    offsets = [reader.u32() for _ in range(count)]
    base = reader.pos
    require(offsets[0] == 0 and offsets[-1] == len(data) - base, 'dataIGP: invalid bounds')
    rows.append(f'P\tdataIGP\t{count-1}\t{len(data)}')
    for index, (start, end) in enumerate(zip(offsets, offsets[1:])):
        require(start <= end <= len(data) - base, 'dataIGP: invalid range')
        payload = data[base+start:base+end]
        rows.append(f'R\tdataIGP\t{index}\t1\t{len(payload)}\t{fingerprint(payload)}')
    return rows


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--exe', required=True, type=Path)
    parser.add_argument('--data', default=DEFAULT_DATA, type=Path)
    args = parser.parse_args()
    expected = expected_manifest(args.data)
    result = subprocess.run([str(args.exe.resolve()), '--data', str(args.data.resolve()), '--manifest'],
                            check=True, capture_output=True, text=True)
    actual = result.stdout.splitlines()
    require(len(actual) == len(expected), f'native manifest length: {len(actual)} != {len(expected)}')
    for index, (left, right) in enumerate(zip(expected, actual), 1):
        require(left == right, f'native manifest row {index}: {right!r} != {left!r}')
    slots = sum(row.startswith('R\t') for row in expected)
    print(f'PASS: {len(expected)} native/Python container rows (20 containers, {slots} slots; FNV-1a fingerprints)')


if __name__ == '__main__':
    main()
