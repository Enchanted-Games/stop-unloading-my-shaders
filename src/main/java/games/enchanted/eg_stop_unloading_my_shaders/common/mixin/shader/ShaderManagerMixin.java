package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.shader;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.systems.GpuDevice;
import games.enchanted.eg_stop_unloading_my_shaders.common.Logging;
import games.enchanted.eg_stop_unloading_my_shaders.common.ModConstants;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import games.enchanted.eg_stop_unloading_my_shaders.common.duck.GpuDeviceAdditions;
import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import games.enchanted.eg_stop_unloading_my_shaders.common.util.PostChainUtil;
import net.minecraft.client.renderer.PostChainConfig;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ShaderManager.class)
public class ShaderManagerMixin {
    @Inject(
        at = @At("HEAD"),
        method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/client/renderer/ShaderManager$Configs;"
    )
    private void eg_sumr$onShaderManagerStart(ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfoReturnable<ShaderManager.Configs> cir) {
        ShaderReloadManager.startedVanillaReload();
    }

    @Inject(
        at = @At("TAIL"),
        method = "apply(Lnet/minecraft/client/renderer/ShaderManager$Configs;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V"
    )
    private void eg_sumr$onShaderManagerFinish(ShaderManager.Configs object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        ShaderReloadManager.finishedVanillaReload();
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;precompilePipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lcom/mojang/blaze3d/shaders/ShaderSource;)Lcom/mojang/blaze3d/pipeline/CompiledRenderPipeline;"),
        method = "apply(Lnet/minecraft/client/renderer/ShaderManager$Configs;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V"
    )
    private CompiledRenderPipeline eg_sumr$wrapPipelineCompilation(GpuDevice device, RenderPipeline renderPipeline, ShaderSource shaderSource, Operation<CompiledRenderPipeline> original) {
        CompiledRenderPipeline compiled = original.call(device, renderPipeline, shaderSource);
        if(compiled.isValid()) return compiled;

        ((GpuDeviceAdditions) device).eg_sumr$setBypassPipelineCache(true);
        CompiledRenderPipeline vanillaCompiled = original.call(device, renderPipeline, ModConstants.getVanillaShaderSource());
        ((GpuDeviceAdditions) device).eg_sumr$setBypassPipelineCache(false);
        return vanillaCompiled;
    }

    // Identifier local is the result of POST_CHAIN_ID_CONVERTER.fileToId
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false),
        method = "loadPostChain(Lnet/minecraft/resources/Identifier;Lnet/minecraft/server/packs/resources/Resource;Lcom/google/common/collect/ImmutableMap$Builder;)V"
    )
    private static void eg_sumr$addDummyPostChainConfigIfFailedToParse(Logger instance, String string, Object o, Object exception, Operation<Void> original, Identifier rawLocation, Resource postChain, ImmutableMap.Builder<Identifier, PostChainConfig> output, @Local(ordinal = 1) Identifier name) {
        original.call(instance, string, o, exception);
        output.put(name, PostChainUtil.createDummyPostChainConfig());
        ShaderReloadManager.showErrorMessage(Messages.getFailedToLoadPostChainMessage(name.toString()));
        ShaderReloadManager.showContinuationErrorMessage(Component.literal(((Exception) exception).getMessage()));
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/preprocessor/GlslPreprocessor;process(Ljava/lang/String;)Ljava/util/List;"),
        method = "loadShader"
    )
    private static List<String> eg_sumr$wrapShaderPreprocessError(GlslPreprocessor instance, String shaderData, Operation<List<String>> original, Identifier shaderID) {
        try {
            return original.call(instance, shaderData);
        } catch (Exception e) {
            ShaderReloadManager.showErrorMessage(
                Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.preprocessor_error", "_Pre-processor error in %s:", shaderID.toString())
            );
            if(e instanceof IndexOutOfBoundsException) {
                ShaderReloadManager.showContinuationErrorMessage(Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.preprocessor_error.probably_no_version", "_#version directive may be missing"));
            } else {
                ShaderReloadManager.showContinuationErrorMessage(Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.preprocessor_error.check_log", "_check output log for full exception"));
                Logging.error("Pre-processor error in {}:\n{}", shaderID.toString(), e.getMessage());
            }
            return List.of("");
        }
    }
}
