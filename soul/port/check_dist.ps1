$ErrorActionPreference = 'Stop'
$dist = Join-Path $PSScriptRoot 'dist'
$expected = @('soul_of_darkness.exe', 'soul_engine_check.exe', 'soul_of_darkness_v137.exe', 'soul_engine_check_v137.exe', 'README.txt',
    'soul_port.exe', 'soul_assets.exe', 'display.example.ini')
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
& (Join-Path $dist 'soul_port.exe') --self-test
if ($LASTEXITCODE -ne 0) { throw 'Packaged scaffold self-test failed' }
& (Join-Path $dist 'soul_assets.exe') --help
if ($LASTEXITCODE -ne 0) { throw 'Packaged asset inspector failed' }
# The game engine must start and keep ticking without any game data present.
& (Join-Path $dist 'soul_engine_check.exe') --data no-such-directory --ticks 40 --width 569 --quiet
if ($LASTEXITCODE -ne 0) { throw 'Packaged engine failed its data-free run' }
& (Join-Path $dist 'soul_engine_check_v137.exe') --data no-such-directory --ticks 40 --width 569 --quiet
if ($LASTEXITCODE -ne 0) { throw 'Packaged 1.3.7 engine failed its data-free run' }
Write-Host 'PASS: package contains only executables, the readme and example settings; no game data'
