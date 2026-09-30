Param()

$ErrorActionPreference = "Stop"

if ($args.Count -ne 0) {
  Write-Host "Usage: powershell -ExecutionPolicy Bypass -File scripts\check_submission.ps1"
  exit 2
}

$Root = Resolve-Path (Join-Path $PSScriptRoot "..")
$CompileScript = Join-Path $Root "scripts\compile.ps1"
$BuildDir = Join-Path $Root "build\classes"

Write-Host "Compiling Project 1..."
& $CompileScript
if ($LASTEXITCODE -ne 0) {
  Write-Error "READINESS CHECK FAILED: compilation did not succeed."
  exit 1
}

$failures = 0
$checks = 0

foreach ($impl in @("RA", "L")) {
  foreach ($caseName in @("typical", "empty")) {
    foreach ($operation in @("union", "intersection", "difference")) {
      foreach ($mode in @("same", "mixed")) {
        $checks++
        Write-Host ""
        Write-Host "[$checks/25] $impl $caseName $operation $mode"
        & java -cp $BuildDir PublicTestRunner $impl $caseName $operation $mode
        if ($LASTEXITCODE -ne 0) {
          $failures++
        }
      }
    }
  }
}

$checks++
Write-Host ""
Write-Host "[$checks/25] null-args"
& java -cp $BuildDir PublicTestRunner null-args
if ($LASTEXITCODE -ne 0) {
  $failures++
}

Write-Host ""
if ($failures -eq 0) {
  Write-Host "READINESS CHECK PASSED: all 25 public checks passed."
  exit 0
}

Write-Error "READINESS CHECK FAILED: $failures of 25 public checks failed."
exit 1