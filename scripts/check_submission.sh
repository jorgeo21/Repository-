#!/usr/bin/env bash
set -uo pipefail

if [[ $# -ne 0 ]]; then
  echo "Usage: bash scripts/check_submission.sh" >&2
  exit 2
fi

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

echo "Compiling Project 1..."
if ! bash "${ROOT_DIR}/scripts/compile.sh"; then
  echo "READINESS CHECK FAILED: compilation did not succeed." >&2
  exit 1
fi

failures=0
checks=0

for impl in RA L; do
  for case_name in typical empty; do
    for operation in union intersection difference; do
      for mode in same mixed; do
        checks=$((checks + 1))
        echo
        echo "[$checks/25] $impl $case_name $operation $mode"
        if ! SKIP_COMPILE=1 bash "${ROOT_DIR}/scripts/run_public_test.sh" \
          "$impl" "$case_name" "$operation" "$mode"; then
          failures=$((failures + 1))
        fi
      done
    done
  done
done

checks=$((checks + 1))
echo
echo "[$checks/25] null-args"
if ! SKIP_COMPILE=1 bash "${ROOT_DIR}/scripts/run_public_test.sh" null-args; then
  failures=$((failures + 1))
fi

echo
if [[ $failures -eq 0 ]]; then
  echo "READINESS CHECK PASSED: all 25 public checks passed."
  exit 0
fi

echo "READINESS CHECK FAILED: $failures of 25 public checks failed." >&2
exit 1