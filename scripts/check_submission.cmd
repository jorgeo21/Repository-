@echo off
setlocal

if not "%~1"=="" (
  echo Usage: scripts\check_submission.cmd
  exit /b 2
)

powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0check_submission.ps1"
exit /b %ERRORLEVEL%