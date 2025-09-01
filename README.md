# Stop Unloading My Resourcepacks

Prevents the game from disabling all resourcepacks if a shader-related error occurs. Instead, errors will be displayed in-game, can be configured to show errors in chat, a box in the top left, or both

This mod also adds a few QOL features for developing shaders, read more below

## Things this mod prevents from disabling resourcepacks

The following things in vanilla will forcibly disable all resourcepacks, with this mod installed these will use a fallback and show any errors in-game and the output log:

- Shader program compilation errors
- Missing shader programs
- Shader linking errors
- Invalid or missing post_effect jsons
- post_effects that reference invalid targets

For minecrafts builtin shaders, the mod will attempt to load the default ones as a fallback if an error occurs. For modded shaders, a dummy shader will be used as a fallback which will make any objects using that shader invisible

## Additional features

- Shader hot-reload keybind (F3 + R) which just reloads shaders, much quicker than a full resource reload
   - Works in the title screen, not just when in a world!
- Adds better errors for some things. For example, when importing a non-existent include shader the gane will produce a proper error rather than throwing a null pointer exception
- Added an option to disable shader linker info logs
    - Off by default but can be enabled if it causes a ton of log spam (like it does on my machine)
