"""Validate numeric pack containers and dataIGP from the original loaders.

No decoded game assets are written. This intentionally does not guess the
meaning of individual payloads. See soul/docs/ASSET_FORMATS.md.
"""
import argparse
import struct
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent


def require(condition, message):
    if not condition:
        raise ValueError(message)


def inspect(data_dir):
    slots = nonempty = 0
    for index in range(1, 20):
        data = (data_dir / str(index)).read_bytes()
        count, parts, first = struct.unpack_from('<HHH', data)
        require(parts == 1 and first == 0, f'{index}: unsupported split pack')
        table_end = 6 + 4 * (count + 1)
        require(table_end <= len(data), f'{index}: truncated offset table')
        offsets = struct.unpack_from(f'<{count + 1}I', data, 6)
        require(offsets[0] == table_end, f'{index}: gap before payload')
        require(offsets[-1] == len(data), f'{index}: unconsumed trailing bytes')
        require(all(a <= b <= len(data) for a, b in zip(offsets, offsets[1:])), f'{index}: invalid offsets')
        present = 0
        for a, b in zip(offsets, offsets[1:]):
            if a == b:  # Java o() returns -1 for absent entries.
                continue
            require(data[a] == 0, f'{index}: unknown resource prefix {data[a]}')
            present += 1
        slots += count
        nonempty += present
        print(f'{index:>2}: {count:>2} slots, {present:>2} present; {len(data):>6} bytes consumed exactly')
    data = (data_dir / 'dataIGP').read_bytes()
    count = struct.unpack_from('<H', data)[0]
    require(2 + 4*count <= len(data), 'dataIGP: truncated table')
    offsets = struct.unpack_from(f'<{count}I', data, 2)
    require(offsets[0] == 0 and offsets[-1] + 2 + 4*count == len(data), 'dataIGP: bad bounds')
    require(all(a <= b for a, b in zip(offsets, offsets[1:])), 'dataIGP: unsorted offsets')
    print(f'dataIGP: {count-1} entries, exact consumption')
    print(f'PASS: 19 packs, {slots} slots ({nonempty} present), promotion container')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--data', type=Path, default=ROOT / 'soul/extracted')
    inspect(parser.parse_args().data)
