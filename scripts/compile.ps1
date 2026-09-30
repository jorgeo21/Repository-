Param()

$ErrorActionPreference = "Stop"

$Root = Resolve-Path (Join-Path $PSScriptRoot "..")
$BuildDir = Join-Path $Root "build\classes"

if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
  Write-Host "Recommended JDK download: https://adoptium.net/temurin/releases/?version=17&package=jdk"
  Write-Error "javac not found. Install a JDK version 17 or newer and ensure javac is on PATH. If you are using WSL, install openjdk-17-jdk inside WSL."
}

if (Test-Path $BuildDir) { Remove-Item -Recurse -Force $BuildDir }
New-Item -ItemType Directory -Force -Path $BuildDir | Out-Null

# Collect sources
$src = Get-ChildItem -Path (Join-Path $Root "src") -Filter *.java -File
$auto = Get-ChildItem -Path (Join-Path $Root "autograder") -Filter *.java -File

if ($src.Count -eq 0) { Write-Error "No Java files found under src/." }
if ($auto.Count -eq 0) { Write-Error "No Java files found under autograder/." }

$files = @()
$files += $src.FullName
$files += $auto.FullName

javac --release 17 -encoding UTF-8 -Xlint:unchecked -d $BuildDir $files
if ($LASTEXITCODE -ne 0) {
  Write-Error "Compilation failed with javac exit code $LASTEXITCODE."
  exit $LASTEXITCODE
}
Write-Host "Compiled to $BuildDir"