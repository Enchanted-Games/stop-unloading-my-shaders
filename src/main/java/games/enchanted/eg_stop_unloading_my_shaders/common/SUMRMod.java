package games.enchanted.eg_stop_unloading_my_shaders.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

//? if fabric {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//? }

public class SUMRMod {
    public static final KeyMapping HOT_RELOAD_KEYBIND = new KeyMapping(
        "key.eg_stop_unloading_my_shaders.reload_shaders",
        InputConstants.Type.KEYSYM,
        InputConstants.KEY_R,
        KeyMapping.Category.DEBUG
    );

    public static void init() {
        Logging.info("Mod init started. Compiled for {}", ModConstants.TARGET_PLATFORM);
        //? if fabric {
        registerKeybinds();
        //? }
    }

    public static void registerKeybinds() {
        //? if fabric {
        // TODO: reenable when fapi is available
//        KeyMappingHelper.registerKeyMapping(HOT_RELOAD_KEYBIND);
        //? }
    }
}
