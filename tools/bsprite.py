"""BSprite 991 data model, pixel decoder and frame composition.

Transcribed from original d.java. Supports the feature combinations shipped
in this JAR; rejects unimplemented branches instead of guessing.
"""
from dataclasses import dataclass, field
import binascii
import struct
import zlib
from functools import lru_cache
from game_assets import Reader, require


@dataclass
class Module:
    width: int
    height: int
    x: int = 0
    y: int = 0
    image: int = 0


@dataclass
class Sprite:
    flags: int
    modules: list = field(default_factory=list)
    frame_modules: list = field(default_factory=list)
    collision_rects: list = field(default_factory=list)
    frames: list = field(default_factory=list)
    frame_rects: list = field(default_factory=list)
    animation_frames: list = field(default_factory=list)
    animations: list = field(default_factory=list)
    palettes: list = field(default_factory=list)
    encoded: list = field(default_factory=list)
    png_images: list = field(default_factory=list)
    codec: int = 0

    @classmethod
    def parse(cls, data, label='sprite'):
        r = Reader(data, label)
        require(r.u16() == 991, f'{label}: incorrect BSprite version')
        s = cls(r.u32())
        known = 1 | 2 | 4 | 256 | 1024 | 32768 | 65536 | 16777216 | 536870912
        require(s.flags & ~known == 0, f'{label}: unsupported flags {s.flags:#x}')
        image_count = 0
        if s.flags & 1:
            for _ in range(r.u16()):
                image = r.i8() if s.flags & 4 else 0
                require(image >= 0, f'{label}: negative image group')
                x, y = (r.u8(), r.u8()) if s.flags & 2 else (0, 0)
                s.modules.append(Module(r.u8(), r.u8(), x, y, image))
            image_count = max((m.image for m in s.modules), default=0) + 1
        if s.flags & 256:
            for _ in range(r.u16()):
                module = r.u8()
                x, y = (r.i16(), r.i16()) if s.flags & 1024 else (r.i8(), r.i8())
                flags = r.u8()
                s.frame_modules.append((module | ((flags & 192) << 2), x, y, flags))
            s.collision_rects = [read_rect(r) for _ in range(r.u16())]
            rect_start = 0
            for _ in range(r.u16()):
                count = r.u8()
                r.take(1)  # Byte ignored by this build's loader.
                start = r.u16()
                rect_count = r.i8()
                require(rect_count >= 0, f'{label}: negative collision rectangle count')
                s.frames.append((start, count, rect_start, rect_count))
                rect_start += rect_count
            s.frame_rects = [read_rect(r) for _ in s.frames]
            require(rect_start == len(s.collision_rects), f'{label}: unused collision rectangles')
        if s.flags & 65536:
            for _ in range(r.u16()):
                frame, duration, x, y, flags = r.u8(), r.u8(), r.i8(), r.i8(), r.u8()
                s.animation_frames.append((frame | ((flags & 192) << 2), duration, x, y, flags))
            for _ in range(r.u16()):
                count = r.u8()
                r.take(1)
                s.animations.append((r.u16(), count))
        if s.flags & 16777216:
            require(r.u16() == 21781, f'{label}: unsupported palette encoding')
            for _ in range(image_count):
                palette_count, color_count = r.u8(), r.u8()
                color_count = color_count or 256
                group = []
                for _ in range(palette_count):
                    colors = []
                    for _ in range(color_count):
                        color = r.u16()
                        # The original does not replicate low RGB bits to fill all eight bits.
                        colors.append((0xff000000 if color & 32768 else 0) |
                                      ((color & 31744) << 9) | ((color & 992) << 6) | ((color & 31) << 3))
                    group.append(colors)
                s.palettes.append(group)
            s.codec = r.u16()
            require(s.codec in (25840, 10225, 22258, 5632, 1024, 512, 22018), f'{label}: unsupported codec {s.codec}')
            s.encoded = [r.take(r.u16()) for _ in s.modules]
        if s.flags & 536870912 and not s.flags & 4:
            template = r.take(r.u16())
            palettes, colors = r.u8(), r.u8()
            colors = colors or 256
            group = []
            for _ in range(palettes):
                image = bytearray(template)
                image[41:41+colors*3+4] = r.take(colors*3+4)
                alpha = 49+colors*3+4
                image[alpha:alpha+colors+4] = r.take(colors+4)
                group.append(bytes(image))
            s.png_images = [group]
        if s.flags & 4 and not s.flags & 16777216:
            for _ in range(r.u8()):
                template = r.take(r.u16())
                palettes = r.u8()
                group = []
                if not palettes:
                    group.append(template)
                else:
                    colors = r.u8() or 256
                    for _ in range(palettes):
                        image = bytearray(template)
                        image[41:41+colors*3+4] = r.take(colors*3+4)
                        alpha = 49+colors*3+4
                        image[alpha:alpha+colors+4] = r.take(colors+4)
                        group.append(bytes(image))
                s.png_images.append(group)
        r.done()
        s.validate(label)
        return s

    def validate(self, label):
        for index, (start, count, rect_start, rect_count) in enumerate(self.frames):
            require(start+count <= len(self.frame_modules), f'{label}: frame {index} module range')
            require(rect_start+rect_count <= len(self.collision_rects), f'{label}: frame {index} rect range')
        for index, (_, count) in enumerate(self.animations):
            start = self.animations[index][0]
            require(start+count <= len(self.animation_frames), f'{label}: animation {index} range')
        for frame, _, _, _, _ in self.animation_frames:
            require(frame < len(self.frames), f'{label}: animation references frame {frame}')
        for module, _, _, flags in self.frame_modules:
            limit = len(self.frames) if flags & 16 else len(self.modules)
            require(module < limit, f'{label}: invalid nested frame/module {module}')
        for m in self.modules:
            if self.png_images:
                group = m.image if self.flags & 4 else 0
                require(group < len(self.png_images), f'{label}: invalid image group')
                for png in self.png_images[group]:
                    w, h, _ = decode_png(png)
                    require(m.x+m.width <= w and m.y+m.height <= h, f'{label}: module outside PNG')

    def palette_count(self, module=0):
        group = self.modules[module].image if self.flags & 4 else 0
        return len(self.palettes[group] if self.palettes else self.png_images[group])

    def decode_module(self, module, palette=0, transform=0):
        m = self.modules[module]
        group = m.image if self.flags & 4 else 0
        if self.png_images:
            w, _, image = decode_png(self.png_images[group][palette])
            pixels = [image[(m.y+y)*w+m.x+x] for y in range(m.height) for x in range(m.width)]
        else:
            colors = self.palettes[group][palette]
            data = Reader(self.encoded[module], f'module {module}')
            indices = []
            count = m.width*m.height
            while len(indices) < count:
                value = data.u8()
                if self.codec == 25840:
                    bits = (len(colors)-1).bit_length()
                    indices.extend([value & ((1 << bits)-1)] * ((value >> bits)+1))
                elif self.codec == 10225:
                    indices.extend([data.u8()]*(value-128) if value > 127 else [value])
                elif self.codec == 22258:
                    indices.extend(data.take(value-128) if value > 127 else [data.u8()]*value)
                elif self.codec in (5632, 1024, 512):
                    bits = {5632: 4, 1024: 2, 512: 1}[self.codec]
                    indices.extend((value >> shift) & ((1 << bits)-1) for shift in range(8-bits, -1, -bits))
                else:
                    indices.append(value)
            # Packed 1/2/4-bit branches in the original write a whole final byte;
            # compare logical image pixels while retaining storage-padding semantics.
            padding = len(indices)-count
            require(padding == 0 or self.codec in (5632, 1024, 512), f'module {module}: RLE overrun')
            data.done()
            require(all(i < len(colors) for i in indices), f'module {module}: invalid palette index')
            pixels = [colors[i] for i in indices[:count]]
        if transform & 1:
            pixels = [pixels[y*m.width+x] for y in range(m.height) for x in range(m.width-1, -1, -1)]
        if transform & 2:
            pixels = [pixels[y*m.width+x] for y in range(m.height-1, -1, -1) for x in range(m.width)]
        return pixels

    def frame_bounds(self, frame, rect=-1, transform=0, depth=0):
        require(depth < 64, 'recursive frame bounds')
        start, count, rect_start, rect_count = self.frames[frame]
        if rect != -1:
            if rect >= rect_count:
                return (0, 0, 0, 0)
            x, y, w, h = self.collision_rects[rect_start+rect]
        elif self.flags & 1024:
            left = top = right = bottom = 0
            for module, dx, dy, flags in self.frame_modules[start:start+count]:
                if flags & 16:
                    # Original getFrameModuleBounds does not add the nested record offset.
                    box = self.frame_bounds(module, depth=depth+1)
                else:
                    m = self.modules[module]
                    box = (dx, dy, dx+m.width, dy+m.height)
                left, top = min(left, box[0]), min(top, box[1])
                right, bottom = max(right, box[2]), max(bottom, box[3])
            x, y, w, h = left, top, right-left, bottom-top
        else:
            x, y, w, h = self.frame_rects[frame]
        if transform & 1: x = -x-w
        if transform & 2: y = -y-h
        return (x, y, x+w, y+h)

    def compose_frame(self, frame, palette=0, transform=0):
        """Return (left, top, width, height, ARGB pixels), tight to module extents."""
        placements = []
        def visit(index, x, y, flips, stack):
            require(index not in stack and len(stack) < 64, 'cyclic/deep nested frame')
            start, count, _, _ = self.frames[index]
            for module, dx, dy, flags in self.frame_modules[start:start+count]:
                px, py = x+(-dx if flips & 1 else dx), y+(-dy if flips & 2 else dy)
                if flags & 16:
                    visit(module, px, py, flips ^ (flags & 15), stack+(index,))
                else:
                    m = self.modules[module]
                    if flips & 1: px -= m.width
                    if flips & 2: py -= m.height
                    if m.width and m.height:
                        placements.append((module, px, py, flips ^ (flags & 15)))
        visit(frame, 0, 0, transform, ())
        if not placements: return (0, 0, 1, 1, [0])
        left, top = min(p[1] for p in placements), min(p[2] for p in placements)
        right = max(x+self.modules[m].width for m, x, _, _ in placements)
        bottom = max(y+self.modules[m].height for m, _, y, _ in placements)
        w, h = right-left, bottom-top
        require(w*h <= 16_000_000, 'frame composition too large')
        out = [0]*(w*h)
        for module, x, y, flags in placements:
            m = self.modules[module]
            for index, color in enumerate(self.decode_module(module, palette, flags)):
                if color >> 24:
                    # Shipped sprite palettes use binary alpha.
                    out[(y-top+index//m.width)*w+x-left+index%m.width] = color
        return left, top, w, h, out


def read_rect(reader):
    return (reader.i8(), reader.i8(), reader.u8(), reader.u8())


@lru_cache(maxsize=64)
def decode_png(data):
    """Decode the shipped noninterlaced indexed PNGs; validate every chunk CRC."""
    r = Reader(data, 'PNG')
    require(r.take(8) == b'\x89PNG\r\n\x1a\n', 'invalid PNG signature')
    palette, alpha, compressed = [], [], bytearray()
    width = height = depth = color_type = None
    while True:
        size = r.number('>I')
        kind, payload, crc = r.take(4), r.take(size), r.number('>I')
        require(binascii.crc32(kind+payload) & 0xffffffff == crc, f'PNG {kind!r}: CRC mismatch')
        if kind == b'IHDR':
            width, height, depth, color_type, comp, filtering, interlace = struct.unpack('>IIBBBBB', payload)
            require(width > 0 and height > 0 and width*height <= 16_000_000, 'PNG dimensions')
            require(color_type == 3 and depth in (1, 2, 4, 8) and comp == filtering == interlace == 0,
                    f'unsupported PNG format {color_type}/{depth}/{interlace}')
        elif kind == b'PLTE':
            require(len(payload)%3 == 0, 'PNG palette size')
            palette = [int.from_bytes(payload[i:i+3], 'big') for i in range(0, len(payload), 3)]
        elif kind == b'tRNS': alpha = list(payload)
        elif kind == b'IDAT': compressed.extend(payload)
        elif kind == b'IEND': break
    r.done()
    require(width is not None and palette, 'incomplete PNG')
    raw = zlib.decompress(compressed)
    stride = (width*depth+7)//8
    require(len(raw) == height*(stride+1), 'PNG decompressed length')
    previous, pixels = [0]*stride, []
    for y in range(height):
        offset = y*(stride+1)
        mode, row = raw[offset], list(raw[offset+1:offset+1+stride])
        require(mode <= 4, 'PNG filter type')
        for x in range(stride):
            a = row[x-1] if x else 0
            b, c = previous[x], previous[x-1] if x else 0
            if mode == 1: add = a
            elif mode == 2: add = b
            elif mode == 3: add = (a+b)//2
            elif mode == 4:
                p = a+b-c
                distances = (abs(p-a), abs(p-b), abs(p-c))
                add = (a, b, c)[distances.index(min(distances))]
            else: add = 0
            row[x] = (row[x]+add)&255
        for x in range(width):
            index = (row[x*depth//8] >> (8-depth-(x*depth%8))) & ((1<<depth)-1)
            require(index < len(palette), 'PNG index outside palette')
            pixels.append(((alpha[index] if index < len(alpha) else 255)<<24) | palette[index])
        previous = row
    return width, height, pixels


def write_png(path, width, height, pixels):
    """Lossless RGBA preview; caller must choose an ignored output directory."""
    def chunk(kind, data):
        return struct.pack('>I', len(data))+kind+data+struct.pack('>I', binascii.crc32(kind+data)&0xffffffff)
    rows = bytearray()
    for y in range(height):
        rows.append(0)
        for pixel in pixels[y*width:(y+1)*width]:
            rows.extend(((pixel>>16)&255, (pixel>>8)&255, pixel&255, pixel>>24))
    path.write_bytes(b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', width, height, 8, 6, 0, 0, 0)) +
                     chunk(b'IDAT', zlib.compress(rows)) + chunk(b'IEND', b''))
