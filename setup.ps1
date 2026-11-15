param([switch]$SkipBuild, [string]$Jar)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    & python (Join-Path 'tools' 'bootstrap.py')
    if ($LASTEXITCODE -ne 0) { throw "bootstrap.py failed ($LASTEXITCODE)" }
    $reference = @((Join-Path 'tools' 'build_reference.py'))
    if ($Jar) { $reference += @('--jar', $Jar) }
    & python @reference
    if ($LASTEXITCODE -ne 0) { throw "build_reference.py failed ($LASTEXITCODE)" }
    & python (Join-Path 'tools' 'gen_port.py')
    if ($LASTEXITCODE -ne 0) { throw "gen_port.py failed ($LASTEXITCODE)" }
    if (!$SkipBuild) {
        & .\soul\port\build.bat Release
        if ($LASTEXITCODE -ne 0) { throw 'Windows build/checks failed' }
        & python (Join-Path 'tools' 'check_engine.py')
        if ($LASTEXITCODE -ne 0) { throw 'Engine equivalence check failed' }
    }
} finally { Pop-Location }
