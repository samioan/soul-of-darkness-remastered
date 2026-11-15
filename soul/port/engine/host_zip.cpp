// Extracting JAR entries (directory from host_util.cpp, DEFLATE from inflate.cpp). Used by the game
// host to load a JAR in place.
#include "host_util.h"
#include "inflate.h"

namespace sodhost {

bool readZip(const std::vector<std::uint8_t>& z, std::vector<ZipEntry>& out, std::string& error) {
    std::vector<ZipDirEntry> directory;
    if (!readZipDirectory(z, directory, error)) return false;
    for (const ZipDirEntry& d : directory) {
        ZipEntry entry;
        entry.name = d.name;
        const std::uint8_t* begin = z.data() + d.dataStart;
        if (d.method == 0) {
            entry.data.assign(begin, begin + d.compressed);
        } else if (d.method == 8) {
            if (d.compressed == 0 || !sod::inflateRaw(begin, d.compressed, entry.data) || entry.data.size() != d.uncompressed) {
                error = "cannot decompress " + d.name;
                return false;
            }
        } else {
            error = "unsupported compression in " + d.name;
            return false;
        }
        out.push_back(std::move(entry));
    }
    return true;
}

} // namespace sodhost
