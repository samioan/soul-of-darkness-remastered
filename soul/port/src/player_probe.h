#pragma once
#include <filesystem>
void emitPlayerManifest(const std::filesystem::path& root);
void emitPickupManifest(const std::filesystem::path& root);
void emitContainerManifest(const std::filesystem::path& root);
void emitCrystalManifest(const std::filesystem::path& root);
void emitEffectManifest(const std::filesystem::path& root);
void emitContactManifest(const std::filesystem::path& root);
void emitPlatformManifest(const std::filesystem::path& root);
void emitMovingPlatformManifest(const std::filesystem::path& root);
void emitSwingingPlatformManifest(const std::filesystem::path& root);
void emitMeleeManifest(const std::filesystem::path& root);
void emitEnemyManifest(const std::filesystem::path& root);
void emitCombatManifest(const std::filesystem::path& root);
void emitDeathManifest(const std::filesystem::path& root);
void emitLiveCombatManifest(const std::filesystem::path& root);

void emitRegionManifest(const std::filesystem::path& root);

void emitBridgeManifest(const std::filesystem::path& root);

void emitTransientManifest(const std::filesystem::path& root);

void emitWorldOrderManifest(const std::filesystem::path& root);

void emitLinkedTriggerManifest(const std::filesystem::path&);

void emitGateManifest(const std::filesystem::path&);

void emitGateScriptManifest(const std::filesystem::path&);
void emitScriptDataManifest(const std::filesystem::path&);

void emitRemovedGateTriggerManifest(const std::filesystem::path&);

void emitPassageManifest(const std::filesystem::path&);

void emitAbilityManifest(const std::filesystem::path&);

void emitActivationManifest(const std::filesystem::path&);

void emitFlyingEnemyManifest(const std::filesystem::path&);

void emitFlyingWorldManifest(const std::filesystem::path&);
void emitRemovedEnemyManifest(const std::filesystem::path&);

void emitChestUnlockManifest(const std::filesystem::path&);

void emitFireMagicManifest(const std::filesystem::path&);

void emitIcePickupManifest(const std::filesystem::path&);
void emitIceWeaponManifest(const std::filesystem::path&);
void emitIceWorldManifest(const std::filesystem::path&);

void emitIceProjectileManifest(const std::filesystem::path&);

void emitIceMagicManifest(const std::filesystem::path&);

void emitIceAttachmentManifest(const std::filesystem::path&);

void emitCheckpointManifest(const std::filesystem::path&);

void emitBeamManifest(const std::filesystem::path&);

void emitPromptManifest(const std::filesystem::path&);

void emitIntroManifest(const std::filesystem::path&);

void emitChandelierManifest(const std::filesystem::path&);

void emitPromptRemovalManifest(const std::filesystem::path&);

void emitCameraControllerManifest(const std::filesystem::path&);

void emitCompletionManifest(const std::filesystem::path&);

void emitFinishManifest(const std::filesystem::path&);

void emitSelectedLevelManifest(const std::filesystem::path&);

void emitTimedSwitchManifest(const std::filesystem::path&);

void emitEnergyBarrierManifest(const std::filesystem::path&);

void emitThrownObjectManifest(const std::filesystem::path&);
void emitType3DecisionManifest(const std::filesystem::path&);
void emitType3ActorManifest(const std::filesystem::path&);
void emitType3WorldManifest(const std::filesystem::path&);
void emitEncounterGateManifest(const std::filesystem::path&);
void emitEncounterWorldManifest(const std::filesystem::path&);
void emitAmbushManifest(const std::filesystem::path&);
void emitAmbushWorldManifest(const std::filesystem::path&);
void emitLevelAuditManifest(const std::filesystem::path&);
void emitSineFlyerManifest(const std::filesystem::path&);
void emitSineFlyerWorldManifest(const std::filesystem::path&);
