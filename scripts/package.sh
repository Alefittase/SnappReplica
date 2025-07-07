#!/bin/bash

# Navigate to project root
cd "$(dirname "$0")/.."

# Clean previous distributions
rm -rf dist
mkdir -p dist/jfx/{linux,windows}

# Copy common files
cp InsTax!.jar dist/jfx/linux/
cp InsTax!.jar dist/jfx/windows/
cp -r resources dist/jfx/linux/
cp -r resources dist/jfx/windows/

# Copy platform-specific run scripts
mkdir -p dist/jfx/linux/scripts
mkdir -p dist/jfx/windows/scripts
cp scripts/run dist/jfx/linux/scripts/
cp scripts/run.bat dist/jfx/windows/scripts/

# Copy JavaFX libraries
cp -r jfx/linux/lib dist/jfx/linux/
cp -r jfx/windows/lib dist/jfx/windows/

# Create top-level run scripts
echo '#!/bin/bash' > dist/jfx/linux/run
echo 'cd "$(dirname "$0")/scripts"' >> dist/jfx/linux/run
echo './run' >> dist/jfx/linux/run
chmod +x dist/jfx/linux/run

echo '@echo off' > dist/jfx/windows/run.bat
echo 'cd /d %~dp0scripts' >> dist/jfx/windows/run.bat
echo 'call run.bat' >> dist/jfx/windows/run.bat

# Create ZIP packages
cd dist
zip -r InsTax.zip -i dist/*
cd ..

echo "done"