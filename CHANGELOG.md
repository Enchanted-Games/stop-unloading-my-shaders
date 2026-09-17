# 1.6.0

- Ported to 26.3
- Many things have changed internally and as such some error messages look different compared to previous versions:
  - Post effect related errors are now displayed as warnings and generally have more information than before
  - Shader compilation errors are now logged per pipeline instead of per shader type (in practice this just means you see 1 error even if both the fragment and vertex shaders failed to compile)
- On OpenGL, some specific cases may still cause resourcepacks to be unloaded. This doesn't seem to be easily fixable for the time being
- Removed the 'Hide linker logs' config option