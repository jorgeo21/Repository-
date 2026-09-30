#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD_DIR="${ROOT_DIR}/build/classes"

rm -rf "${BUILD_DIR}"
mkdir -p "${BUILD_DIR}"

# Fail fast with a helpful error if Java is not installed.
if ! command -v javac >/dev/null 2>&1; then
  echo "ERROR: 'javac' not found. Install a JDK version 17 or newer (not just a JRE)." >&2
  echo "" >&2
  echo "Recommended download:" >&2
  echo "  https://adoptium.net/temurin/releases/?version=17&package=jdk" >&2
  echo "" >&2
  echo "Package-manager alternatives:" >&2
  echo "  • macOS (Homebrew):    brew install --cask temurin@17" >&2
  echo "  • Ubuntu/WSL:          sudo apt-get update && sudo apt-get install -y openjdk-17-jdk" >&2
  echo "  • Windows (native):    install the Temurin 17 JDK and add it to PATH" >&2
  echo "" >&2
  echo "If you are running this from WSL (paths like /mnt/c/...), installing Java on Windows is NOT enough." >&2
  echo "Install the JDK inside WSL (Ubuntu) using the command above." >&2
  exit 127
fi

# Compile student source + local public-test harness.
# NOTE: Do not add package statements to starter code unless you also update the scripts.
javac --release 17 -encoding UTF-8 -Xlint:unchecked -d "${BUILD_DIR}" \
  "${ROOT_DIR}"/src/*.java \
  "${ROOT_DIR}"/autograder/*.java