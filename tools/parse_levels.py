"""Validate shipped tile grids and script structure; optionally compare Java oracle."""
import argparse
from collections import Counter
import hashlib
from pathlib import Path
from game_assets import DEFAULT_DATA, Reader, require, resources

# Game.cn / getScriptOpcodeSize: includes reserved commands with no side effect.
EXTENDED_SIZES = (6, 2, 3, 0, 2, 1, 1, 4, 4, 0, 0, 8, 1, 0, 4, 0, 4, 2, 0, 0, 1, 0)
SIZES = {**dict.fromkeys((11, 12, 21, 31, 41), 4),
         **dict.fromkeys((22, 32, 42), 1),
         **dict.fromkeys((23, 24, 43, 44), 4),
         **{op: ((op-34) % 3+1)*2+(2 if op < 37 else 0) for op in range(34, 40)},
         **{100+i: size for i, size in enumerate(EXTENDED_SIZES)}}


def scripts(data, label='scripts'):
    r = Reader(data, label)
    count = r.i8()
    require(count >= 0, f'{label}: negative script count')
    result = []
    for _ in range(count):
        uid, track_count = r.i16(), r.i8()
        require(track_count >= 0, f'{label}: negative track count')
        metadata = r.take(2)  # Loader skips these bytes; semantics unconfirmed.
        tracks = []
        for _ in range(track_count):
            start = r.pos
            kind, flags = r.i8(), r.u8()
            require(kind in range(4), f'{label}: unrecognized track kind {kind}')
            target = r.i16() if kind in (2, 3) else None
            key_count = r.i16()
            require(key_count >= 0, f'{label}: negative key count')
            initial = r.pos-start
            keys = []
            for _ in range(key_count):
                tick, command_count = r.i16(), r.i8()
                require(command_count >= 0, f'{label}: negative command count')
                commands = []
                for _ in range(command_count):
                    op = r.i8()
                    # Unknown low opcodes consume zero bytes in Java. Reject here
                    # so a future resource variant cannot silently desynchronize.
                    require(op in SIZES, f'{label}: unsupported opcode {op} at {r.pos-1}')
                    commands.append((op, r.take(SIZES[op])))
                keys.append((tick, commands))
            raw = data[start:r.pos]+initial.to_bytes(2, 'little')
            tracks.append(dict(kind=kind, flags=flags, target=target, keys=keys,
                               raw=raw, initial=initial))
        result.append(dict(uid=uid, metadata=metadata, tracks=tracks))
    r.done()
    return result


def validate_maps(parts, gameplay, label):
    grids = (2, 5, 8) if gameplay else (0, 3)
    dimensions = []
    for cells in grids:
        r = Reader(parts[cells+1], f'{label}:{cells+1}')
        width, height = r.u16(), r.u16()
        r.done()
        require(width > 0 and height > 0, f'{label}: empty map')
        require(len(parts[cells]) == width*height, f'{label}:{cells}: grid size mismatch')
        dimensions.append((width, height))
        require(len(parts[cells+2]) == (width*height+3)//4,
                f'{label}:{cells+2}: packed transform size mismatch')
    require(dimensions[-2] == dimensions[-1], f'{label}: unequal visual grid dimensions')
    if gameplay and dimensions[0] != dimensions[-1]:
        print(f'{label}: collision {dimensions[0]} differs from visual {dimensions[-1]} (preserved)')
    return dimensions[-1]


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    p.add_argument('--oracle', type=Path)
    args = p.parse_args()
    rows, histogram = [], Counter()
    script_count = track_count = key_count = 0
    for pack in range(3, 15):
        parts = dict(resources(args.data/str(pack)))
        width, height = validate_maps(parts, pack <= 11, f'pack {pack}')
        if pack <= 11:
            decoded = scripts(parts[1], f'pack {pack}:scripts')
            script_count += len(decoded)
            for si, script in enumerate(decoded):
                rows.append(f'S\t{pack}\t{si}\t{script["uid"]}\t{len(script["tracks"])}')
                for ti, track in enumerate(script['tracks']):
                    track_count += 1
                    key_count += len(track['keys'])
                    for _, commands in track['keys']:
                        histogram.update(op for op, _ in commands)
                    rows.append(f'T\t{pack}\t{si}\t{ti}\t{track["initial"]}\t'
                                f'{len(track["raw"])}\t{hashlib.sha256(track["raw"]).hexdigest()}')
            print(f'pack {pack}: {width}x{height} cells, {len(decoded)} scripts; exact consumption')
        else:
            print(f'pack {pack}: {width}x{height} cells, presentation maps; exact consumption')
    if args.oracle:
        require(rows == args.oracle.read_text(encoding='utf-8').splitlines(),
                'Python script decode differs from original-Java oracle')
        print(f'PASS: {len(rows)} original-Java script/track oracle rows')
    print(f'PASS: 12 map packs; {script_count} scripts / {track_count} tracks / {key_count} keyframes')
    print(f'Opcode occurrences: {dict(sorted(histogram.items()))}')


if __name__ == '__main__':
    main()
