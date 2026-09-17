package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.shader;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.device.GpuDevice;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
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

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"),
        method = "loadPostChain"
    )
    private static void logPostChainLoadingFailure(Logger instance, String s, Object o, Object o2, Operation<Void> original, Identifier id) {
        ShaderReloadManager.showErrorMessage(Messages.getFailedToLoadPostChainMessage(id.toString()));
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"),
        method = "isPostEffectValid"
    )
    private static void logPostEffectDoesNotExist(Logger instance, String s, Object o2, Operation<Void> original, Identifier id) {
        if(id.equals(Identifier.withDefaultNamespace("end_of_frame"))) return;
        ShaderReloadManager.showWarnMessage(Messages.getRequestedPostEffectDoesNotExistMessage(id.toString()));
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"),
        method = "isPostEffectValid"
    )
    private static void logPostEffectInvalidTargets(Logger instance, String s, Object o, Object o2, Operation<Void> original) {
        ShaderReloadManager.showWarnMessage(Messages.getPostChainInvalidTargetsMessage(o.toString(), o2.toString()));
    }
}
