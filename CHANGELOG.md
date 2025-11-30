# 1.2.0
- Updated to 1.21.11
- Added better errors for invalid import directives, these now show the full file path instead of just the file name
- Prevented shaders with a missing `#version` from causing a reload fail
- Prevented shader pre-processing from causing a reload if an error somehow occurs
- Made some improvements to the error message box
   - Messages are now hidden instead of removed after 30 seconds (60 if using the hot reload keybind)
   - Hidden messages can be shown by clicking the info message in the box
- Hopefully fixed some rare crashes caused by race conditions when trying to load fallback shaders