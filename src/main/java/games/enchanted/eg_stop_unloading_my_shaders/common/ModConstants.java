package games.enchanted.eg_stop_unloading_my_shaders.common;

import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.systems.GpuDeviceBackend;
import com.mojang.blaze3d.systems.RenderSystem;
import games.enchanted.eg_stop_unloading_my_shaders.common.duck.GpuDeviceAdditions;
import games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor.GpuDeviceAccessor;
import games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor.ShaderManagerAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.InactiveProfiler;

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
    public static boolean isBackendHandled() {
        GpuDeviceBackend backend = ((GpuDeviceAccessor) RenderSystem.getDevice()).eg_sumr$getBackend();
        return backend instanceof GpuDeviceAdditions;
    }

    private static ShaderManager.Configs vanillaShaderConfigs;

    public static ShaderManager.Configs getVanillaShaderConfigs() {
        if(vanillaShaderConfigs != null) return vanillaShaderConfigs;

        VanillaPackResources vanillaPack = Minecraft.getInstance().getVanillaPackResources();
        ResourceManager resourceManager = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, List.of(vanillaPack));
        vanillaShaderConfigs = ((ShaderManagerAccessor) Minecraft.getInstance().getShaderManager()).eg_sumr$invokePrepare(resourceManager, InactiveProfiler.INSTANCE);
        return vanillaShaderConfigs;
    }

    public static ShaderSource getFallbackShaderSource() {
        ShaderManager.Configs configs = getVanillaShaderConfigs();
        return (identifier, type) -> configs.shaderSources().get(new ShaderManager.ShaderSourceKey(identifier, type));
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
