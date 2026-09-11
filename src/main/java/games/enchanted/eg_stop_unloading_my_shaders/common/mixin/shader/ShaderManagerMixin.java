package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.shader;

import com.mojang.renderpearl.api.device.GpuDevice;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShaderManager.class)
public class ShaderManagerMixin {
    @Inject(
        at = @At("HEAD"),
        method = "loadConfigs"
    )
    private static void eg_sumr$onShaderManagerStart(ResourceManager manager, CallbackInfoReturnable<ShaderManager.Configs> cir) {
        ShaderReloadManager.startedVanillaReload();
    }

    @Inject(
        at = @At("TAIL"),
        method = "apply"
    )
    private void eg_sumr$onShaderManagerFinish(GpuDevice device, @Coerce Object compilations, CallbackInfo ci) {
        ShaderReloadManager.finishedVanillaReload();
    }
}
