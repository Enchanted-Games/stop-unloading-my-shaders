package games.enchanted.eg_stop_unloading_my_shaders.common;

import games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor.ShaderManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.InactiveProfiler;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} else {
/*import net.neoforged.fml.loading.FMLPaths;
*///?}

//? if minecraft: > 1.21.10 {
import com.mojang.blaze3d.shaders.ShaderSource;
//?}

import java.nio.file.Path;
import java.util.List;

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

    private static ShaderManager.Configs vanillaShaderConfigs;

    public static ShaderManager.Configs getVanillaShaderConfigs() {
        if(vanillaShaderConfigs != null) return vanillaShaderConfigs;

        VanillaPackResources vanillaPack = Minecraft.getInstance().getVanillaPackResources();
        ResourceManager resourceManager = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, List.of(vanillaPack));
        vanillaShaderConfigs = ((ShaderManagerAccessor) Minecraft.getInstance().getShaderManager()).eg_sumr$invokePrepare(resourceManager, InactiveProfiler.INSTANCE);
        return vanillaShaderConfigs;
    }

    //? if minecraft: > 1.21.10 {
    public static ShaderSource getVanillaShaderSource() {
        ShaderManager.Configs configs = getVanillaShaderConfigs();
        return (Identifier, shaderType) -> configs.shaderSources().get(new ShaderManager.ShaderSourceKey(Identifier, shaderType));
    }
    //?}

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
