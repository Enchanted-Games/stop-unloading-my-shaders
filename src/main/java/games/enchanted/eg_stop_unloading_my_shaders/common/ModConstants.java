package games.enchanted.eg_stop_unloading_my_shaders.common;

import games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor.ShaderManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.InactiveProfiler;

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
        vanillaShaderConfigs = ((ShaderManagerAccessor) Minecraft.getInstance().getShaderManager()).eg_sumy$invokePrepare(resourceManager, InactiveProfiler.INSTANCE);
        return vanillaShaderConfigs;
    }

    public static Component getFailedToLoadPostChainMessage(String name) {
        // TODO: proper translation keys
        return Component.translatableWithFallback("", "Failed to load post_effect %s:", name);
    }
}
