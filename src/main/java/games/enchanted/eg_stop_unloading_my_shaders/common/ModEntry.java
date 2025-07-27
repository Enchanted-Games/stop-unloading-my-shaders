package games.enchanted.eg_stop_unloading_my_shaders.common;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} else {
/*import net.neoforged.fml.ModList;
*///?}

/**
 * This is the entry point for your mod's common side, called by each modloader specific side.
 */
public class ModEntry {
    private static final String DEBUG_KEYS_ANYWHERE_ID = "eg_f3_t_everywhere";
    public static final boolean debugKeysAnywhereEnabled =
        //? if fabric {
        FabricLoader.getInstance().isModLoaded(DEBUG_KEYS_ANYWHERE_ID)
        //?} else {
        /*ModList.get().isLoaded(DEBUG_KEYS_ANYWHERE_ID);
        *///?}
    ;

    public static void init() {
        Logging.info("Mod is loading on a {} environment!", ModConstants.TARGET_PLATFORM);
    }
}
