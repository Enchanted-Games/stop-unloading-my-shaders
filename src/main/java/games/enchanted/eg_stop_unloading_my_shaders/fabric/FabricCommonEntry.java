//? if fabric {
package games.enchanted.eg_stop_unloading_my_shaders.fabric;

import games.enchanted.eg_stop_unloading_my_shaders.common.ModEntry;
import net.fabricmc.api.ModInitializer;

public class FabricCommonEntry implements ModInitializer {
    @Override
    public void onInitialize() {
        ModEntry.init();
    }
}
//?}