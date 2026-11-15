$ErrorActionPreference = 'Stop'
$dist = Join-Path $PSScriptRoot 'dist'
# Relative paths: the launcher at the top, the game and its checker in bin\ (launcher/updater.cpp's
# package check refuses an update zip without this shape).
$expected = @('SoulOfDarkness.exe', 'README.txt', 'NOTICE.md', 'bin\soul_of_darkness.exe', 'bin\soul_engine_check.exe')
if (!(Test-Path -LiteralPath $dist)) { throw 'Build the package first with build_dist.bat' }
$files = @(Get-ChildItem -LiteralPath $dist -Recurse -File)
foreach ($file in $files) {
    $relative = $file.FullName.Substring($dist.Length + 1)
    if ($relative -notin $expected) { throw "Unexpected package file: $relative" }
}
foreach ($name in $expected) {
    if (!(Test-Path -LiteralPath (Join-Path $dist $name))) { throw "Missing package file: $name" }
}
# The launcher embeds its artwork (banner ~240 KB, icon ~210 KB); a launcher without them is a broken build.
if ((Get-Item (Join-Path $dist 'SoulOfDarkness.exe')).Length -lt 400KB) { throw 'SoulOfDarkness.exe is missing its embedded artwork' }
# The game engine must start and keep ticking without any game data present.
& (Join-Path $dist 'bin\soul_engine_check.exe') --data no-such-directory --ticks 40 --width 569 --quiet
if ($LASTEXITCODE -ne 0) { throw 'Packaged engine failed its data-free run' }
Write-Host 'PASS: package contains only the launcher, the game, its checker and the readmes; no game data'
