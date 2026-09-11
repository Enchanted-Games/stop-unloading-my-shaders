package games.enchanted.eg_stop_unloading_my_shaders.common;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.backend.api.GpuDeviceBackend;
import games.enchanted.eg_stop_unloading_my_shaders.common.duck.GpuDeviceAdditions;
import games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor.GpuDeviceAccessor;
import games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor.ShaderManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;

import java.nio.file.Path;
import java.util.List;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
import org.jspecify.annotations.Nullable;
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
    public static boolean isBackendHandled() {
        GpuDeviceBackend backend = ((GpuDeviceAccessor) RenderSystem.getDevice()).eg_sumr$getBackend();
        return backend instanceof GpuDeviceAdditions;
    }

    private static ShaderManager.Configs vanillaShaderConfigs;

    public static ShaderManager.Configs getVanillaShaderConfigs() {
        if(vanillaShaderConfigs != null) return vanillaShaderConfigs;

        VanillaPackResources vanillaPack = Minecraft.getInstance().getVanillaPackResources();
        ResourceManager resourceManager = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, List.of(vanillaPack.fullResources()));
        vanillaShaderConfigs = ShaderManagerAccessor.eg_sumr$loadConfigs(resourceManager);
        return vanillaShaderConfigs;
    }

    public static ShaderSource getFallbackShaderSource() {
        ShaderManager.Configs configs = getVanillaShaderConfigs();
        return new ShaderSource() {
            @Override
            public @Nullable String getShader(Identifier identifier, ShaderType type) {
                return configs.shaderSources().get(new ShaderManager.ShaderSourceKey(identifier, type));
            }

            @Override
            public @Nullable CachedIncludeSource getInclude(Identifier id) {
                return null;
            }

            @Override
            public void close() {

            }
        };
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
