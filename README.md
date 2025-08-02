# Stop Unloading My Resourcepacks

Prevents the game from disabling all resourcepacks if a shader-related error occurs. Instead, errors will be displayed in-game, can be configured to show errors in chat, a box in the top left, or both

Additionally, this mod adds a hotkey (F3 + R) which will hot-reload all shaders and post_effects, much faster than a full resource reload

## Things this mod prevents from disabling resourcepacks

The following things in vanilla will forcibly disable all resourcepacks, with this mod installed these will use a fallback and show any errors in-game and the output log:

- Shader program compilation errors
- Missing shader programs
- Shader linking errors
- Invalid or missing post_effect jsons
- post_effects that reference invalid targets

For minecrafts builtin shaders, the mod will attempt to load the default ones as a fallback if an error occurs. For modded shaders, a dummy shader will be used as a fallback which will make any objects using that shader invisible
