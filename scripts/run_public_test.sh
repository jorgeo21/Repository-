#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
if [[ "${SKIP_COMPILE:-0}" != "1" ]]; then
  bash "${ROOT_DIR}/scripts/compile.sh"
fi
BUILD_DIR="${ROOT_DIR}/build/classes"

if [[ $# -eq 1 && "$1" == "null-args" ]]; then
  # Scored null-argument behavior check.
  java -cp "${BUILD_DIR}" PublicTestRunner null-args
  exit $?
fi

if [[ $# -ne 4 ]]; then
  echo "Usage:
  (Windows PowerShell alternative: scripts/run_public_test.ps1)
 $0 <impl> <case> <op> <mode>"
  echo "  impl: RA | L"
  echo "  case: typical | empty"
  echo "  op  : union | intersection | difference"
  echo "  mode: same | mixed"
  exit 2
fi

java -cp "${BUILD_DIR}" PublicTestRunner "$1" "$2" "$3" "$4"