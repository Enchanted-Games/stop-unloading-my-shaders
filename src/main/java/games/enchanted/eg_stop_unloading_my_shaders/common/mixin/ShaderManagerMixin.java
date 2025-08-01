package games.enchanted.eg_stop_unloading_my_shaders.common.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import games.enchanted.eg_stop_unloading_my_shaders.common.util.PostChainUtil;
import net.minecraft.client.renderer.PostChainConfig;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
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
    private void eg_sumr$onShaderManagerFinish(ShaderManager.Configs object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        ShaderReloadManager.finishedVanillaReload();
    }

    // ResourceLocation local is the result of POST_CHAIN_ID_CONVERTER.fileToId
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false),
        method = "loadPostChain(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/server/packs/resources/Resource;Lcom/google/common/collect/ImmutableMap$Builder;)V"
    )
    private static void eg_sumr$addDummyPostChainConfigIfFailedToParse(Logger instance, String string, Object o, Object exception, Operation<Void> original, ResourceLocation rawLocation, Resource postChain, ImmutableMap.Builder<ResourceLocation, PostChainConfig> output, @Local(ordinal = 1) ResourceLocation name) {
        original.call(instance, string, o, exception);
        output.put(name, PostChainUtil.createDummyPostChainConfig());
        ShaderReloadManager.showErrorMessage(Messages.getFailedToLoadPostChainMessage(name.toString()));
        ShaderReloadManager.showContinuationErrorMessage(Component.literal(((Exception) exception).getMessage()));
    }
}
