"""Validate the serialized object-record envelopes and actor stat tables."""
from collections import Counter
import argparse
from pathlib import Path
from game_assets import DEFAULT_DATA, Reader, require, resources


def object_records(data, label='objects'):
    reader = Reader(data, label)
    while reader.pos < len(data):
        count = reader.u8()
        require(6 <= count <= 20, f'{label}: invalid record short count {count}')
        offset = reader.pos
        row = tuple(reader.i16() for _ in range(count))
        require(0 <= row[0] <= 100, f'{label}: unknown actor type {row[0]}')
        yield offset, row
    reader.done()


def stat_tables(data):
    reader = Reader(data, 'actor stats')
    count = reader.number('>i')
    require(0 <= count <= 256, 'invalid stat table count')
    tables = []
    for _ in range(count):
        size = reader.number('>i')
        require(0 <= size <= len(data)//4, 'invalid stat row size')
        tables.append(tuple(reader.number('>i') for _ in range(size)))
    reader.done()
    return tables


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--data', type=Path, default=DEFAULT_DATA)
    args = parser.parse_args()
    histogram = Counter()
    for pack in range(3, 12):
        records = list(object_records(dict(resources(args.data / str(pack)))[0], f'pack {pack}'))
        histogram.update(row[0] for _, row in records)
        require(sum(row[0] == 0 for _, row in records) == 1, f'pack {pack}: expected one player record')
        print(f'level {pack-3}: {len(records)} object records, exact envelope consumption')
    tables = stat_tables(dict(resources(args.data / '18'))[0])
    print(f'PASS: {sum(histogram.values())} records, {len(histogram)} actor types; '
          f'{len(tables)} stat tables / {sum(map(len, tables))} integers')
    print('Type-specific parameter meanings and script payloads require separate validation.')


if __name__ == '__main__':
    main()
