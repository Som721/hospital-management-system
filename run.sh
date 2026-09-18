#!/bin/bash
# Compile and run Hospital Management System
set -e
cd "$(dirname "$0")"
mkdir -p out
echo "[1/2] Compiling (backend + frontend)..."
javac -d out $(find backend/src/main/java frontend/src/main/java -name "*.java")
echo "[2/2] Running..."
java -cp out com.hospital.Main
