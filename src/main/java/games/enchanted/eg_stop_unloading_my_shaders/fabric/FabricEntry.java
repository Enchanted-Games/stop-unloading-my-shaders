//? if fabric {
package games.enchanted.eg_stop_unloading_my_shaders.fabric;

import games.enchanted.eg_stop_unloading_my_shaders.common.SUMRMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class FabricEntry implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SUMRMod.init();
    }
}
//?}