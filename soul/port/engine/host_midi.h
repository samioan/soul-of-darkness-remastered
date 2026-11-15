// Music for the Windows host: a small Standard MIDI File sequencer on its own thread, sending to the
// system MIDI synthesizer (midiOut). It replaces MCI, whose synchronous open of every track stalled
// the game each time music started, looped or a jingle interrupted it.
//
// The game's tracks were mixed for a phone and differ by about 20 dB (the level themes are quiet, the
// boss theme and the jingles loud). Each track is measured when it is loaded and its channel volumes
// (CC7) are scaled so all of them play at a similar loudness; the user's volume scales them too.
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
    double loudnessDb = 0;    // mean power of the sounding notes under the GS/DLS volume curves
    double gain = 1;          // multiplier for every channel volume (CC7) that levels this song
};

// Parses a format 0/1 SMF and measures it. Returns false for anything that is not one.
bool parseMidi(const std::vector<std::uint8_t>& data, MidiSong& out, std::string& error);

// Loudness every song is levelled to (dB, same scale as MidiSong::loudnessDb) and the gain limits.
constexpr double kMidiTargetDb = -18.0;
constexpr double kMidiMinGain = 0.3;
constexpr double kMidiMaxGain = 2.5;

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
