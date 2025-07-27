package games.enchanted.eg_stop_unloading_my_shaders.common.mixin;

import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderManager.class)
public class ShaderManagerMixin {
    @Inject(
        at = @At("RETURN"),
        method = "apply(Lnet/minecraft/client/renderer/ShaderManager$Configs;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V"
    )
    private void eg_sumy$onShaderManagerFinish(ShaderManager.Configs object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        ShaderReloadManager.finishedVanillaReload();
    }
}
