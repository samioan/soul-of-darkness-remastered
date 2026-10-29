param([switch]$SkipBuild)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    foreach ($script in @('bootstrap.py', 'extract_jar.py', 'decompile.py', 'rename_sod.py', 'inspect_assets.py', 'parse_objects.py')) {
        & python (Join-Path 'tools' $script)
        if ($LASTEXITCODE -ne 0) { throw "$script failed ($LASTEXITCODE)" }
    }
    if (!$SkipBuild) {
        & .\soul\port\build.bat
        if ($LASTEXITCODE -ne 0) { throw 'Windows build/checks failed' }
        & python tools/check_native_assets.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native/Python asset comparison failed' }
        & python tools/check_native_sprites.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native sprite comparison failed' }
        & python tools/check_native_render.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native sprite render/preview comparison failed' }
        & python tools/check_native_fonts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native font comparison failed' }
        & python tools/check_native_tiles.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native tile comparison failed' }
        & python tools/check_native_player.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native player comparison failed' }
        & python tools/check_native_pickups.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native pickup comparison failed' }
        & python tools/check_native_containers.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native container comparison failed' }
        & python tools/check_native_crystals.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native crystal reward comparison failed' }
        & python tools/check_native_effects.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native collection effect comparison failed' }
        & python tools/check_native_contacts.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native side-contact comparison failed' }
        & python tools/check_native_platforms.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native platform comparison failed' }
        & python tools/check_native_moving_platforms.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native moving-platform comparison failed' }
        & python tools/check_native_world_order.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw "Native world-order checks failed" }
        & python tools/check_native_linked_triggers.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw "Native linked-trigger checks failed" }
        & python tools/check_native_gates.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native linked-gate checks failed" }
        & python tools/check_native_gate_scripts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native gate-script checks failed" }
        & python tools/check_native_activation.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native first-wave activation checks failed" }
        & python tools/check_native_flying_enemies.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native flying enemy and second-wave checks failed" }
        & python tools/check_native_chest_unlock.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native chest unlock and arena progression checks failed" }
        & python tools/check_native_fire_magic.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native fire casting and MP recovery checks failed" }
        & python tools/check_native_ice_weapons.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native ice pickup and weapon checks failed" }
        & python tools/check_native_ice_projectiles.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native ice projectile checks failed" }
        & python tools/check_native_ice_attachments.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw "Native ice attachment checks failed" }
        & python tools/check_native_checkpoints.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native checkpoint check failed" }
        & python tools/check_native_intro.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native intro cutscene checks failed" }
        & python tools/check_native_prompts.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native placed tutorial prompt checks failed" }
        & python tools/check_native_beams.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native beam hazard checks failed" }
        & python tools/check_native_saves.py --exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native desktop save and Continue checks failed" }
        & python tools/check_native_abilities.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native magic ability checks failed" }
        & python tools/check_native_passages.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native passage and room-view checks failed" }
        & python tools/check_native_removed_gate_triggers.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native removed-source gate trigger checks failed" }
        & python tools/check_native_transients.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native transient checks failed" }
        & python tools/check_native_bridges.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw "Native bridge checks failed" }
        & python tools/check_native_regions.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw "Native region checks failed" }
        & python tools/check_native_swinging_platforms.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native swinging-platform comparison failed' }
        & python tools/check_native_frontend.py --exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native frontend comparison failed' }
        & python tools/check_native_melee.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native melee comparison failed' }
        & python tools/check_native_enemies.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native enemy AI comparison failed' }
        & python tools/check_native_combat.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native combat comparison failed' }
        & python tools/check_native_deaths.py --exe soul/port/build/Debug/soul_assets.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native enemy death/loot comparison failed' }
        & python tools/check_native_live_combat.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
        if ($LASTEXITCODE -ne 0) { throw 'Native live combat comparison failed' }
    }
} finally { Pop-Location }
