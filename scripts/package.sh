#!/bin/bash

# Navigate to project root
cd "$(dirname "$0")/.."

# Clean previous distributions
rm -rf dist
mkdir -p dist/{linux,windows}

# Copy common files
cp InsTax!.jar dist/linux/
cp InsTax!.jar dist/windows/
cp -r resources dist/linux/
cp -r resources dist/windows/

# Copy platform-specific run scripts
mkdir -p dist/linux/scripts
mkdir -p dist/windows/scripts
cp scripts/run dist/linux/scripts/
cp scripts/run.bat dist/windows/scripts/

# Copy JavaFX libraries
cp -r jfx/linux/lib dist/linux/
cp -r jfx/windows/lib dist/windows/

# Create top-level run scripts
echo '#!/bin/bash' > dist/linux/run
echo 'cd "$(dirname "$0")/scripts"' >> dist/linux/run
echo './run' >> dist/linux/run
chmod +x dist/linux/run

echo '@echo off' > dist/windows/run.bat
echo 'cd /d %~dp0scripts' >> dist/windows/run.bat
echo 'call run.bat' >> dist/windows/run.bat

# Create ZIP packages
cd dist
zip -r InsTax_Linux.zip linux/*
zip -r InsTax_Windows.zip windows/*
cd ..

echo "Distribution packages created:"
echo "  dist/InsTax_Linux.zip"
echo "  dist/InsTax_Windows.zip"
