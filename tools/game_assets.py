"""Checked readers for the supplied Gameloft packs; no filesystem writes."""
import struct
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
DEFAULT_DATA = ROOT / 'soul/extracted'


class FormatError(ValueError):
    pass


class Reader:
    def __init__(self, data, label='data'):
        self.data, self.pos, self.label = data, 0, label

    def take(self, size):
        if size < 0 or self.pos + size > len(self.data):
            raise FormatError(f'{self.label}: truncated at {self.pos}, need {size} bytes')
        result = self.data[self.pos:self.pos+size]
        self.pos += size
        return result

    def number(self, fmt):
        return struct.unpack(fmt, self.take(struct.calcsize(fmt)))[0]

    def u8(self): return self.number('<B')
    def i8(self): return self.number('<b')
    def u16(self): return self.number('<H')
    def i16(self): return self.number('<h')
    def u32(self): return self.number('<I')

    def done(self):
        if self.pos != len(self.data):
            raise FormatError(f'{self.label}: {len(self.data)-self.pos} trailing bytes at {self.pos}')


def require(value, message):
    if not value:
        raise FormatError(message)


def resources(path):
    reader = Reader(Path(path).read_bytes(), str(path))
    count, parts, first = reader.u16(), reader.u16(), reader.u16()
    require(parts == 1 and first == 0, f'{path}: split packs not supported')
    offsets = [reader.u32() for _ in range(count+1)]
    require(offsets[0] == reader.pos and offsets[-1] == len(reader.data), f'{path}: invalid pack bounds')
    for index, (start, end) in enumerate(zip(offsets, offsets[1:])):
        require(start <= end <= len(reader.data), f'{path}:{index}: invalid resource bounds')
        if start != end:
            require(reader.data[start] == 0, f'{path}:{index}: unsupported prefix')
            yield index, reader.data[start+1:end]


def sprite_resources(data_dir=DEFAULT_DATA):
    # Game font/title, gameplay sprites and background images, respectively.
    for pack in (1, 2, 16):
        for resource, data in resources(data_dir / str(pack)):
            require(data[:2] == b'\xdf\x03', f'{pack}:{resource}: expected BSprite signature')
            yield pack, resource, data
