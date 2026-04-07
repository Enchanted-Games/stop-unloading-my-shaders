//? if neoforge {
/*package games.enchanted.eg_stop_unloading_my_shaders.neoforge;

import games.enchanted.eg_stop_unloading_my_shaders.common.SUMRMod;
import games.enchanted.eg_stop_unloading_my_shaders.common.config.ConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/^*
 * This is the entry point for your mod's forge side.
 ^/
@Mod(value = "eg_stop_unloading_my_shaders", dist = Dist.CLIENT)
public class NeoForgeEntry {
    public NeoForgeEntry() {
        SUMRMod.init();

        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> ConfigScreen.createConfigScreen(parent));
    }
}
*///?}