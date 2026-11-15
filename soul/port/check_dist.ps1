$ErrorActionPreference = 'Stop'
$dist = Join-Path $PSScriptRoot 'dist'
$expected = @('soul_of_darkness.exe', 'soul_engine_check.exe', 'README.txt')
if (!(Test-Path -LiteralPath $dist)) { throw 'Build the package first with build_dist.bat' }
$files = @(Get-ChildItem -LiteralPath $dist -Recurse -File)
foreach ($file in $files) {
    if ($file.DirectoryName -ne $dist -or $file.Name -notin $expected) {
        throw "Unexpected package file: $($file.FullName)"
    }
}
foreach ($name in $expected) {
    if (!(Test-Path -LiteralPath (Join-Path $dist $name))) { throw "Missing package file: $name" }
}
# The game engine must start and keep ticking without any game data present.
& (Join-Path $dist 'soul_engine_check.exe') --data no-such-directory --ticks 40 --width 569 --quiet
if ($LASTEXITCODE -ne 0) { throw 'Packaged engine failed its data-free run' }
Write-Host 'PASS: package contains only the executables and the readme; no game data'
