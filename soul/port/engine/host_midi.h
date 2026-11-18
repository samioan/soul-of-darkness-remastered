// Music for the Windows host: a small Standard MIDI File sequencer on its own thread, sending to the
// system MIDI synthesizer (midiOut). It replaces MCI, whose synchronous open of every track stalled
// the game each time music started, looped or a jingle interrupted it.
//
// The tracks were mixed for a phone and differ by 12-26 dB through a desktop GS synthesizer (the
// title and level themes quiet, the menu/boss theme and the jingles loud). Each known track has a
// measured gain (tools/music_levels.py) that levels it to a common loudness, applied to its channel
// volumes (CC7) and note velocities; the user's volume scales CC7 too.
#pragma once
#include <cstdint>
#include <memory>
#include <string>
#include <vector>

namespace sodhost {

struct MidiSong {
    struct Event {
        double ms;            // from the start of the song
        std::uint32_t msg;    // packed short message: status | data1 << 8 | data2 << 16
    };
    std::vector<Event> events;
    double lengthMs = 0;      // end of the longest track
    double levelDb = 0;       // levelling gain for this track, 0 when it is not a known track
};

// Parses a format 0/1 SMF and looks up its levelling gain. Returns false for anything that is not one.
bool parseMidi(const std::vector<std::uint8_t>& data, MidiSong& out, std::string& error);

class MidiPlayer {
public:
    MidiPlayer();
    ~MidiPlayer();
    MidiPlayer(const MidiPlayer&) = delete;
    MidiPlayer& operator=(const MidiPlayer&) = delete;
    // loops: -1 forever, otherwise the number of plays. Never blocks on the device.
    void play(std::shared_ptr<const MidiSong> song, int loops);
    void stop();
    bool playing() const;
    // volume 0..100 (amplitude), applied to the song currently playing as well.
    void setVolume(int volume);
    // Messages for the host log, collected from the player thread.
    std::vector<std::string> takeLog();
private:
    struct Impl;
    std::unique_ptr<Impl> impl_;
};

} // namespace sodhost
