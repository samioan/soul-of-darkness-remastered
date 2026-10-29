$ErrorActionPreference = 'Stop'
$dist = Join-Path $PSScriptRoot 'dist'
$expected = @('soul_port.exe', 'soul_assets.exe', 'display.example.ini')
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
Write-Host 'PASS: scaffold package contains only executables and example settings; no game data'
