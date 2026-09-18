$jarExe = "C:\Users\18199\AppData\Roaming\.minecraft\runtime\java-runtime-epsilon\bin\jar.exe"
$repoRoot = $PSScriptRoot
$outDir = "$repoRoot\dist"
$binDir = "$repoRoot\bin"

if (-not (Test-Path $outDir)) { New-Item -ItemType Directory -Path $outDir -Force | Out-Null }

$targetJar = "$outDir\lanserverproperties-26.2-1.14.0-fabric.jar"
if (Test-Path $targetJar) { Remove-Item $targetJar -Force }

Push-Location $binDir
& $jarExe -cMf "$targetJar" *
Pop-Location

Write-Host "Build complete: $targetJar ($((Get-Item $targetJar).Length) bytes)"
