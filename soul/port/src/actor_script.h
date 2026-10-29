#pragma once
#include "linked_gate.h"
namespace soul {
struct ScriptCommand { int opcode=0;Bytes operands; };
struct ScriptKey { int tick=0,end=0;std::vector<ScriptCommand> commands; };
struct ScriptTrack { int kind=0,flags=0,target=-1,initial=0;Bytes raw;std::vector<ScriptKey> keys; };
struct ScriptData { int uid=0;std::array<std::uint8_t,2> metadata{};std::vector<ScriptTrack> tracks; };
std::vector<ScriptData> decodeScripts(ByteView);
// Selected existing-gate tracks: positions, immediate animation and action.
class GateScript {
public:
    explicit GateScript(ScriptData);
    int uid() const {return data_.uid;}
    int ticks() const {return ticks_;}
    const std::vector<int>& offsets() const {return offsets_;}
    void start();void restart();void stop();
    void update(const std::function<LinkedGate*(int)>& resolve,int cameraX,int cameraY,bool paused=false,bool loop=false);
private:
    ScriptData data_;int ticks_=-1;std::vector<int> offsets_;
    void rewind();
};
}
