Param(
  [Parameter(ValueFromRemainingArguments=$true)]
  [string[]] $Args
)

$ErrorActionPreference = "Stop"

$Root = Resolve-Path (Join-Path $PSScriptRoot "..")
$BuildDir = Join-Path $Root "build\classes"

if ($env:SKIP_COMPILE -ne "1") {
  & (Join-Path $Root "scripts\compile.ps1")
  if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
  }
}

if ($Args.Count -eq 1 -and $Args[0] -eq "null-args") {
  java -cp $BuildDir PublicTestRunner null-args
  exit $LASTEXITCODE
}

if ($Args.Count -ne 4) {
  Write-Host "Usage:"
  Write-Host "  powershell -ExecutionPolicy Bypass -File scripts\run_public_test.ps1 <impl> <case> <op> <mode>"
  Write-Host "  powershell -ExecutionPolicy Bypass -File scripts\run_public_test.ps1 null-args"
  exit 2
}

java -cp $BuildDir PublicTestRunner $Args[0] $Args[1] $Args[2] $Args[3]
exit $LASTEXITCODE