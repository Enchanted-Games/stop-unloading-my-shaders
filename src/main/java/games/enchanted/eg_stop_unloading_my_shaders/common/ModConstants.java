package games.enchanted.eg_stop_unloading_my_shaders.common;

import games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor.ShaderManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;

import org.jspecify.annotations.Nullable;
import java.nio.file.Path;
import java.util.List;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//? } else {
/*import net.neoforged.fml.loading.FMLPaths;
*///? }

public class ModConstants {
    public static final String MOD_NAME = "Stop Unloading My Shaders";
    public static final String MOD_ID = "eg_stop_unloading_my_shaders";

    public static final String TARGET_PLATFORM =
    //? if fabric {
        "fabric"
    //?} else {
        /*"neoforge"
     *///?}
    ;

    private static ShaderManager.@Nullable Configs vanillaShaderConfigs = null;

    public static ShaderManager.Configs getVanillaShaderConfigs() {
        if(vanillaShaderConfigs != null) return vanillaShaderConfigs;

        VanillaPackResources vanillaPack = Minecraft.getInstance().getVanillaPackResources();
        ResourceManager resourceManager = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, List.of(vanillaPack.fullResources()));
        vanillaShaderConfigs = ShaderManagerAccessor.eg_sumr$loadConfigs(resourceManager);
        return vanillaShaderConfigs;
    }

    /**
     * Returns the path where configuration files are stored within the .minecraft directory
     */
    public static Path getConfigPath() {
        //? if fabric {
        return FabricLoader.getInstance().getConfigDir();
        //?} else {
        /*return FMLPaths.CONFIGDIR.get();
         *///?}
    }
}
