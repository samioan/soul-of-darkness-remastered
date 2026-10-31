#include "resources.h"
#include "bsprite.h"
#include "render_probe.h"
#include "font_probe.h"
#include "tile_probe.h"
#include "player_probe.h"
#include <iomanip>
#include <iostream>
#include <sstream>

namespace {
std::string hex(std::uint64_t value) {
    std::ostringstream out; out << std::hex << std::setfill('0') << std::setw(16) << value; return out.str();
}
void inspectSprites(const std::filesystem::path& root, bool manifest) {
    std::size_t sprites = 0, modules = 0, frames = 0, animations = 0;
    for (int id : {1,2,16}) {
        const auto pack = soul::ResourcePack::numeric(soul::readAssetFile(root / std::to_string(id)));
        for (std::size_t resource = 0; resource < pack.size(); ++resource) {
            const auto bytes = pack.resource(resource); if (!bytes) continue;
            const auto s = soul::BSprite::parse(*bytes, std::to_string(id) + ":" + std::to_string(resource));
            ++sprites; modules += s.modules().size(); frames += s.frames().size(); animations += s.animations().size();
            const std::string prefix = "\t" + std::to_string(id) + "\t" + std::to_string(resource);
            if (!manifest) {
                std::cout << id << ':' << resource << " modules=" << s.modules().size() << " frames=" << s.frames().size()
                    << " animations=" << s.animations().size() << " codec=" << s.codec() << '\n'; continue;
            }
            std::cout << "S" << prefix << '\t' << s.flags() << '\t' << s.codec() << '\t' << s.modules().size() << '\t'
                << s.frameModules().size() << '\t' << s.collisionRects().size() << '\t' << s.frames().size() << '\t'
                << s.animationFrames().size() << '\t' << s.animations().size() << '\t' << s.palettes().size() << '\t' << s.images().size() << '\n';
            for (std::size_t i = 0; i < s.modules().size(); ++i) {
                const auto& m = s.modules()[i];
                std::cout << "M" << prefix << '\t' << i << '\t' << m.width << '\t' << m.height << '\t' << m.x << '\t' << m.y << '\t' << m.image << '\n';
                for (std::size_t p = 0; p < s.paletteCount(i); ++p) for (unsigned flip = 0; flip < 4; ++flip) {
                    const auto pixels = s.decodeModule(i,p,flip);
                    std::cout << "P" << prefix << '\t' << i << '\t' << p << '\t' << flip << '\t' << pixels.size() << '\t' << hex(soul::pixelFingerprint(pixels)) << '\n';
                }
            }
            for (std::size_t i = 0; i < s.frameModules().size(); ++i) {
                const auto& fm = s.frameModules()[i];
                std::cout << "FM" << prefix << '\t' << i << '\t' << fm.module << '\t' << fm.x << '\t' << fm.y << '\t' << fm.flags << '\n';
            }
            for (std::size_t i = 0; i < s.collisionRects().size(); ++i) {
                const auto& r = s.collisionRects()[i];
                std::cout << "CR" << prefix << '\t' << i << '\t' << r.x << '\t' << r.y << '\t' << r.width << '\t' << r.height << '\n';
            }
            for (std::size_t i = 0; i < s.frames().size(); ++i) {
                const auto& f = s.frames()[i];
                std::cout << "F" << prefix << '\t' << i << '\t' << f.start << '\t' << f.count << '\t' << f.rectStart << '\t' << f.rectCount
                    << '\t' << f.visual.x << '\t' << f.visual.y << '\t' << f.visual.width << '\t' << f.visual.height << '\n';
                for (int rect = -1; rect <= f.rectCount; ++rect) for (unsigned flip = 0; flip < 4; ++flip) {
                    std::cout << "B" << prefix << '\t' << i << '\t' << rect << '\t' << flip;
                    for (int value : s.frameBounds(i,rect,flip)) std::cout << '\t' << value;
                    std::cout << '\n';
                }
            }
            for (std::size_t i = 0; i < s.animationFrames().size(); ++i) {
                const auto& af = s.animationFrames()[i];
                std::cout << "AF" << prefix << '\t' << i << '\t' << af.frame << '\t' << af.duration << '\t' << af.x << '\t' << af.y << '\t' << af.flags << '\n';
            }
            for (std::size_t i = 0; i < s.animations().size(); ++i) {
                const auto& a = s.animations()[i]; std::cout << "A" << prefix << '\t' << i << '\t' << a.start << '\t' << a.count << '\n';
            }
            for (std::size_t g = 0; g < s.palettes().size(); ++g) for (std::size_t p = 0; p < s.palettes()[g].size(); ++p) {
                const auto& palette = s.palettes()[g][p];
                std::cout << "PAL" << prefix << '\t' << g << '\t' << p << '\t' << palette.size() << '\t' << hex(soul::pixelFingerprint(palette)) << '\n';
            }
            for (std::size_t g = 0; g < s.images().size(); ++g) for (std::size_t p = 0; p < s.images()[g].size(); ++p) {
                const auto& image = s.images()[g][p];
                std::cout << "I" << prefix << '\t' << g << '\t' << p << '\t' << image.width << '\t' << image.height << '\t'
                    << hex(soul::fingerprint(soul::ByteView(image.png))) << '\t' << hex(soul::pixelFingerprint(image.pixels)) << '\n';
            }
        }
    }
    if (!manifest) std::cout << "PASS: " << sprites << " native sprites, " << modules << " modules, " << frames << " frames, " << animations << " animations\n";
}
std::size_t inspect(const soul::ResourcePack& pack, const std::string& name, bool manifest) {
    std::size_t present = 0;
    if (manifest) std::cout << "P\t" << name << '\t' << pack.size() << '\t' << pack.fileSize() << '\n';
    for (std::size_t i = 0; i < pack.size(); ++i) {
        const auto payload = pack.resource(i);
        if (payload) ++present;
        if (manifest) {
            std::cout << "R\t" << name << '\t' << i << '\t' << (payload ? 1 : 0) << '\t'
                << (payload ? payload->size() : 0) << '\t' << std::hex << std::setfill('0') << std::setw(16)
                << (payload ? soul::fingerprint(*payload) : 0) << std::dec << '\n';
        }
    }
    if (!manifest) std::cout << name << ": " << pack.size() << " slots, " << present << " present; "
        << pack.fileSize() << " bytes consumed exactly\n";
    return present;
}
}
int main(int argc, char** argv) {
    try {
        if (argc == 2 && std::string(argv[1]) == "--help") {
            std::cout << "soul_assets --data EXTRACTED_DIRECTORY [--manifest|--sprites|--sprite-manifest|--render-manifest|--font-manifest|--tile-manifest|--player-manifest|--pickup-manifest|--container-manifest|--crystal-manifest|--effect-manifest|--contact-manifest|--platform-manifest|--moving-platform-manifest|--swinging-platform-manifest|--melee-manifest|--enemy-manifest|--combat-manifest|--death-manifest|--live-combat-manifest|--region-manifest|--bridge-manifest|--transient-manifest|--world-order-manifest|--linked-trigger-manifest|--gate-manifest|--gate-script-manifest|--script-data-manifest|--removed-gate-trigger-manifest|--passage-manifest|--ability-manifest|--activation-manifest|--flying-enemy-manifest|--chest-unlock-manifest|--fire-magic-manifest|--ice-weapon-manifest|--ice-projectile-manifest|--ice-attachment-manifest|--checkpoint-manifest|--beam-manifest|--prompt-manifest|--intro-manifest|--chandelier-manifest|--prompt-removal-manifest|--camera-controller-manifest|--completion-manifest|--finish-manifest|--selected-level-manifest|--timed-switch-manifest|--energy-barrier-manifest|--thrown-object-manifest|--type3-decision-manifest|--type3-actor-manifest|--type3-world-manifest|--encounter-gate-manifest|--encounter-world-manifest]\n"
                "Read-only container/sprite validation; no JAR import or gameplay.\n";
            return 0;
        }
        if ((argc != 3 && argc != 4) || std::string(argv[1]) != "--data"
            || (argc == 4 && std::string(argv[3]) != "--encounter-gate-manifest" && std::string(argv[3]) != "--encounter-world-manifest" && std::string(argv[3]) != "--beam-manifest" && std::string(argv[3]) != "--prompt-manifest" && std::string(argv[3]) != "--thrown-object-manifest" && std::string(argv[3]) != "--type3-decision-manifest" && std::string(argv[3]) != "--type3-actor-manifest" && std::string(argv[3]) != "--type3-world-manifest" && std::string(argv[3]) != "--energy-barrier-manifest" && std::string(argv[3]) != "--timed-switch-manifest" && std::string(argv[3]) != "--selected-level-manifest" && std::string(argv[3]) != "--finish-manifest" && std::string(argv[3]) != "--completion-manifest" && std::string(argv[3]) != "--camera-controller-manifest" && std::string(argv[3]) != "--prompt-removal-manifest" && std::string(argv[3]) != "--chandelier-manifest" && std::string(argv[3]) != "--intro-manifest" && std::string(argv[3]) != "--manifest" && std::string(argv[3]) != "--sprites" && std::string(argv[3]) != "--sprite-manifest" && std::string(argv[3]) != "--render-manifest" && std::string(argv[3]) != "--font-manifest" && std::string(argv[3]) != "--tile-manifest" && std::string(argv[3]) != "--player-manifest" && std::string(argv[3]) != "--pickup-manifest" && std::string(argv[3]) != "--container-manifest" && std::string(argv[3]) != "--crystal-manifest" && std::string(argv[3]) != "--effect-manifest" && std::string(argv[3]) != "--contact-manifest" && std::string(argv[3]) != "--platform-manifest" && std::string(argv[3]) != "--swinging-platform-manifest" && std::string(argv[3]) != "--moving-platform-manifest" && std::string(argv[3]) != "--checkpoint-manifest" && std::string(argv[3]) != "--ice-attachment-manifest" && std::string(argv[3]) != "--ice-projectile-manifest" && std::string(argv[3]) != "--ice-weapon-manifest" && std::string(argv[3]) != "--fire-magic-manifest" && std::string(argv[3]) != "--chest-unlock-manifest" && std::string(argv[3]) != "--flying-enemy-manifest" && std::string(argv[3]) != "--activation-manifest" && std::string(argv[3]) != "--ability-manifest" && std::string(argv[3]) != "--passage-manifest" && std::string(argv[3]) != "--removed-gate-trigger-manifest" && std::string(argv[3]) != "--script-data-manifest" && std::string(argv[3]) != "--gate-script-manifest" && std::string(argv[3]) != "--gate-manifest" && std::string(argv[3]) != "--linked-trigger-manifest" && std::string(argv[3]) != "--world-order-manifest" && std::string(argv[3]) != "--transient-manifest" && std::string(argv[3]) != "--bridge-manifest" && std::string(argv[3]) != "--region-manifest" && std::string(argv[3]) != "--live-combat-manifest" && std::string(argv[3]) != "--death-manifest" && std::string(argv[3]) != "--combat-manifest" && std::string(argv[3]) != "--enemy-manifest" && std::string(argv[3]) != "--melee-manifest"))
            throw std::invalid_argument("Usage: soul_assets --data EXTRACTED_DIRECTORY [--manifest|--sprites|--sprite-manifest|--render-manifest|--font-manifest|--tile-manifest|--player-manifest|--pickup-manifest|--container-manifest|--crystal-manifest|--effect-manifest|--contact-manifest|--platform-manifest|--moving-platform-manifest|--swinging-platform-manifest|--melee-manifest|--enemy-manifest|--combat-manifest|--death-manifest|--live-combat-manifest|--region-manifest|--bridge-manifest|--transient-manifest|--world-order-manifest|--linked-trigger-manifest|--gate-manifest|--gate-script-manifest|--script-data-manifest|--removed-gate-trigger-manifest|--passage-manifest|--ability-manifest|--activation-manifest|--flying-enemy-manifest|--chest-unlock-manifest|--fire-magic-manifest|--ice-weapon-manifest|--ice-projectile-manifest|--ice-attachment-manifest|--checkpoint-manifest|--beam-manifest|--prompt-manifest|--intro-manifest|--chandelier-manifest|--prompt-removal-manifest|--camera-controller-manifest|--completion-manifest|--finish-manifest|--selected-level-manifest|--timed-switch-manifest|--energy-barrier-manifest|--thrown-object-manifest|--type3-decision-manifest|--type3-actor-manifest|--type3-world-manifest|--encounter-gate-manifest|--encounter-world-manifest]");
        const std::filesystem::path root(argv[2]);
        if (argc==4 && std::string(argv[3])=="--type3-decision-manifest") { emitType3DecisionManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--type3-actor-manifest") { emitType3ActorManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--type3-world-manifest") { emitType3WorldManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--encounter-gate-manifest") { emitEncounterGateManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--encounter-world-manifest") { emitEncounterWorldManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--thrown-object-manifest") { emitThrownObjectManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--energy-barrier-manifest") { emitEnergyBarrierManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--timed-switch-manifest") { emitTimedSwitchManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--selected-level-manifest") { emitSelectedLevelManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--finish-manifest") {emitFinishManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--completion-manifest") {emitCompletionManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--camera-controller-manifest") {emitCameraControllerManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--prompt-removal-manifest") {emitPromptRemovalManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--chandelier-manifest") {emitChandelierManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--intro-manifest") {emitIntroManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--prompt-manifest") {emitPromptManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--beam-manifest") {emitBeamManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--checkpoint-manifest") {emitCheckpointManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--ice-attachment-manifest") {emitIceAttachmentManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--ice-projectile-manifest") {emitIceProjectileManifest(root);emitIceMagicManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--ice-weapon-manifest") {emitIcePickupManifest(root);emitIceWeaponManifest(root);emitIceWorldManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--fire-magic-manifest") {emitFireMagicManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--chest-unlock-manifest") {emitChestUnlockManifest(root);return 0;}
        if (argc==4 && std::string(argv[3])=="--flying-enemy-manifest") { emitFlyingEnemyManifest(root);emitFlyingWorldManifest(root);emitRemovedEnemyManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--activation-manifest") { emitActivationManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--ability-manifest") { emitAbilityManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--passage-manifest") { emitPassageManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--removed-gate-trigger-manifest") { emitRemovedGateTriggerManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--gate-script-manifest") { emitGateScriptManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--script-data-manifest") { emitScriptDataManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--gate-manifest") { emitGateManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--linked-trigger-manifest") { emitLinkedTriggerManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--world-order-manifest") { emitWorldOrderManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--transient-manifest") { emitTransientManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--bridge-manifest") { emitBridgeManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--region-manifest") { emitRegionManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--swinging-platform-manifest") { emitSwingingPlatformManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--moving-platform-manifest") { emitMovingPlatformManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--platform-manifest") { emitPlatformManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--live-combat-manifest") { emitLiveCombatManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--death-manifest") { emitDeathManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--combat-manifest") { emitCombatManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--enemy-manifest") { emitEnemyManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--melee-manifest") { emitMeleeManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--contact-manifest") { emitContactManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--effect-manifest") { emitEffectManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--crystal-manifest") { emitCrystalManifest(root); return 0; }
        if (argc==4 && std::string(argv[3])=="--container-manifest") { emitContainerManifest(root); return 0; }
        if (argc == 4 && std::string(argv[3]) == "--pickup-manifest") { emitPickupManifest(root); return 0; }
        if (argc == 4 && std::string(argv[3]) == "--player-manifest") { emitPlayerManifest(root); return 0; }
        if (argc == 4 && std::string(argv[3]) == "--tile-manifest") { emitTileManifest(root); return 0; }
        if (argc == 4 && std::string(argv[3]) == "--font-manifest") { emitFontManifest(root); return 0; }
        if (argc == 4 && std::string(argv[3]) == "--render-manifest") { emitRenderManifest(root); return 0; }
        if (argc == 4 && std::string(argv[3]) != "--manifest") {
            inspectSprites(root,std::string(argv[3]) == "--sprite-manifest"); return 0;
        }
        const bool manifest = argc == 4;
        std::size_t slots = 0, present = 0;
        for (int id = 1; id <= 19; ++id) {
            const auto name = std::to_string(id);
            const auto pack = soul::ResourcePack::numeric(soul::readAssetFile(root / name), name);
            slots += pack.size(); present += inspect(pack, name, manifest);
        }
        const auto promotion = soul::ResourcePack::promotion(soul::readAssetFile(root / "dataIGP"));
        inspect(promotion, "dataIGP", manifest);
        if (!manifest) std::cout << "PASS: 19 numeric packs, " << slots << " slots (" << present << " present), promotion container\n";
        return 0;
    } catch (const std::exception& error) { std::cerr << error.what() << '\n'; return 1; }
}
