#!/bin/bash

# Navigate to project root
cd "$(dirname "$0")/.."

# Set paths relative to project root
JFX_PATH="jfx/linux/lib"
OUT_DIR="out"
SRC_DIR="src"
RES_DIR="resources"

# Clean output
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# Copy resources
mkdir -p "$OUT_DIR/gui"
cp "$SRC_DIR/gui"/*.fxml "$OUT_DIR/gui/"
cp "$SRC_DIR/gui"/*.css "$OUT_DIR/gui/"
[ -d "$SRC_DIR/gui/img" ] && cp -r "$SRC_DIR/gui/img" "$OUT_DIR/gui/"
cp -r "$RES_DIR" "$OUT_DIR/"

# Compile all Java sources
find "$SRC_DIR" -name "*.java" | grep -v /test/ > sources.txt
javac --module-path "$JFX_PATH" --add-modules javafx.controls,javafx.fxml -d "$OUT_DIR" @sources.txt
rm -f sources.txt

# Create executable JAR
jar cvfe TaxiApp.jar gui.App -C "$OUT_DIR" .

echo "Build successful! Run with: ./run"