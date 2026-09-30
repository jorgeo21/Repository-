@echo off
REM Windows convenience wrapper for PowerShell.
powershell -ExecutionPolicy Bypass -File "%~dp0run_public_test.ps1" %*
exit /b %errorlevel%
