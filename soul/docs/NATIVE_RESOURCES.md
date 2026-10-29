# Native binary readers and resource containers

The first native asset layer lives in `soul/port/src/resources.h/.cpp`. It is a
C++17 library with no Windows, Java, image-decoder or third-party dependency.
The existing Win32 build compiles it as `soul_resources`; `soul_assets.exe` uses
it for read-only validation. The calibration executable does not yet draw game
assets or run gameplay.

## Supported input and API

`BinaryReader` provides checked signed/unsigned 8-bit reads and little/big-endian
16/32-bit reads, bounded takes/skips/seeks and exact-consumption checks. Signed
values use explicit widening/subtraction before narrowing into a representable
result. It does not rely on plain-char signedness, unaligned pointer loads or
implementation-defined unsigned-to-signed conversions. Failed reads/seeks leave
the cursor unchanged; subtraction-based range checks avoid size overflow.

`ResourcePack::numeric` implements the single-part format documented from the
original Game loaders in ASSET_FORMATS.md: u16 counts/first index, absolute u32
offsets with a final sentinel, and a zero prefix on each present entry. It
rejects split parts, nonzero first indices, truncated tables, gaps/overlap before
payload, descending/out-of-file offsets, trailing bytes and unknown prefixes.
The zero prefix is stripped exactly once. Equal offsets represent an absent
slot, whereas an entry containing only its prefix is present with an empty
payload. Neither case is silently substituted for the other.

`ResourcePack::promotion` handles `dataIGP` separately. Its offset count includes
the sentinel; offsets are relative to payload start, and no resource prefix is
removed. Empty promotion ranges remain present. Both parsers validate the entire
container before returning an owner. Numeric file 0 is not a container and is
intentionally outside this tool's 1..19 profile.

Containers own their immutable bytes. Resource access returns optional borrowed
`ByteView`s, whose owner must remain alive and unchanged. Access through a
temporary container is disabled. Views/readers must not outlive the owning pack
or vector; constructing a view directly from a temporary byte vector is disabled.
An invalid requested slot throws `std::out_of_range`; malformed bytes throw
`FormatError`. No implicit zero fill, missing-resource fallback or data rewrite
is performed.

File access supports `std::filesystem::path` and reads in binary mode with size,
read and trailing-growth checks. Its default 64 MiB allocation limit is a tooling
bound, not an inferred game-format limit. The in-memory parsers have no such
arbitrary cap. This is explicit directory-based access; JAR import, path lookup
services, split packs and original streaming resource ownership are still pending.

## Native validation tool

```powershell
.\soul\port\build.bat
.\soul\port\build\Debug\soul_assets.exe --data soul/extracted
python tools/check_native_assets.py --exe soul/port/build/Debug/soul_assets.exe
```

`soul_assets --data DIRECTORY` validates the 19 numeric packs and `dataIGP`,
reporting counts and exact consumption. It writes no files. `--manifest` emits
TSV rows for container size, resource presence, payload size and 64-bit FNV-1a
fingerprint. These are noncryptographic regression fingerprints, not archive
identity/authenticity checks. The tool does not verify JAR identity. Container-only
mode does not decode individual payloads; sprite mode is in NATIVE_SPRITES.md.

`check_native_assets.py` independently reads the directory through the existing
Python numeric pack reader and checked promotion-table parsing, then compares
every native manifest row. For the supplied build, all **252 rows** match:
20 containers and 232 slots (222 numeric slots, 170 present, plus 10 promotion
ranges). Payload fingerprints exclude numeric prefixes and include entire
promotion ranges. No decoded assets or manifest files are written by this check.

Setup runs this comparison after its Debug build; `-SkipBuild` retains the Java/
Python-only pipeline. Ordinary build/CI never requires game data or Python.

## Synthetic checks and package

CTest's `resource_readers` runs **113 assertions** without any game assets:

- Signed/unsigned and endian boundaries, known FNV-1a vector, exact consumption,
  atomic failure, null/empty views, invalid seeks and overflow-sized ranges.
- Valid numeric/promotion samples with absent and present-empty entries, all
  truncations, invalid counts/offsets/prefixes/parts and unconsumed suffixes.
- File byte preservation, missing/empty files and the file allocation limit.

The small file fixtures are created under the build's `resource-fixtures/`
directory and removed individually. CLI help and invalid arguments also run in
CTest, alongside the existing timing/options and headless scaffold checks.
Debug and Release now pass ten CTests, including the separate native sprite
and renderer suites described in NATIVE_SPRITES.md/NATIVE_RENDERING.md. The Release package includes
`soul_port.exe`, `soul_assets.exe` and `display.example.ini`; its strict package
check rejects additional files and verifies both executables. It contains no
original assets, extracted data or decoded previews.

NATIVE_SPRITES.md covers native BSprite decoding and its oracle checks;
NATIVE_RENDERING.md adds sprite drawing and an asset preview. Bitmap fonts,
tile/world rendering, full staged loading and gameplay remain pending.
