#pragma once
#include "checkpoint.h"
#include <optional>
namespace soul {
// Versioned desktop format for the supported first-level snapshot, not J2ME RMS.
std::optional<CheckpointSnapshot> loadGameSave(const std::filesystem::path&);
void writeGameSave(const std::filesystem::path&,const CheckpointSnapshot&);
void clearGameSave(const std::filesystem::path&);
}
