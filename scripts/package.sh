#!/bin/bash

# Navigate to project root
cd "$(dirname "$0")/.."

# Clean previous distributions
rm -rf dist
mkdir -p dist/{linux,windows,macos}

# Copy common files
cp TaxiApp.jar dist/linux/
cp TaxiApp.jar dist/windows/
cp TaxiApp.jar dist/macos/
cp -r resources dist/linux/
cp -r resources dist/windows/
cp -r resources dist/macos/

# Copy platform-specific run scripts
mkdir -p dist/linux/scripts
mkdir -p dist/windows/scripts
mkdir -p dist/macos/scripts
cp scripts/run dist/linux/scripts/
cp scripts/run.bat dist/windows/scripts/
cp scripts/run dist/macos/scripts/

# Copy JavaFX libraries
cp -r jfx/linux/lib dist/linux/
cp -r jfx/windows/lib dist/windows/
cp -r jfx/macos/lib dist/macos/

# Create top-level run scripts
echo '#!/bin/bash' > dist/linux/run
echo 'cd "$(dirname "$0")/scripts"' >> dist/linux/run
echo './run' >> dist/linux/run
chmod +x dist/linux/run

echo '@echo off' > dist/windows/run.bat
echo 'cd /d %~dp0scripts' >> dist/windows/run.bat
echo 'call run.bat' >> dist/windows/run.bat

echo '#!/bin/bash' > dist/macos/run
echo 'cd "$(dirname "$0")/scripts"' >> dist/macos/run
echo './run' >> dist/macos/run
chmod +x dist/macos/run

# Create ZIP packages
cd dist
zip -r InsTax_Linux.zip linux/*
zip -r InsTax_Windows.zip windows/*
zip -r InsTax_macOS.zip macos/*
cd ..

echo "Distribution packages created:"
echo "  dist/InsTax_Linux.zip"
echo "  dist/InsTax_Windows.zip"
echo "  dist/InsTax_macOS.zip"
