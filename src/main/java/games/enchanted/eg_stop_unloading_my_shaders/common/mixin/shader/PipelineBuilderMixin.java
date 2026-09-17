package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.shader;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline;
import com.mojang.renderpearl.frontend.shaders.PipelineBuilder;
import com.mojang.renderpearl.util.ShaderCompileException;
import games.enchanted.eg_stop_unloading_my_shaders.common.DummyShaderSource;
import games.enchanted.eg_stop_unloading_my_shaders.common.FallbackShaderSource;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PipelineBuilder.class)
public abstract class PipelineBuilderMixin {
    @Shadow
    protected abstract BackendRenderPipeline.@Nullable CreateInfo generateBackendCreateInfo(RenderPipeline pipeline, ShaderSource shaderSource, ReferenceArrayList<BackendRenderPipeline.CreateInfo.Shader> shaderCreateInfos, Object2IntOpenHashMap<String> uniformBindings);

    @Inject(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 1),
        method = "generateBackendCreateInfo",
        cancellable = true
    )
    private void tryAgainWithFallbackOnFailedCompilation(
        RenderPipeline pipeline,
        ShaderSource shaderSource,
        ReferenceArrayList<BackendRenderPipeline.CreateInfo.Shader> shaderCreateInfos,
        Object2IntOpenHashMap<String> uniformBindings,
        CallbackInfoReturnable<BackendRenderPipeline.CreateInfo> cir,
        @Local ShaderCompileException e
    ) {
        if(!(shaderSource instanceof FallbackShaderSource) && !(shaderSource instanceof DummyShaderSource)) {
            // failed compilation, but not using fallback or dummy so try again
            ShaderReloadManager.showShaderErrorMessage(
                // TODO: better messages
                Messages.getCouldntCompilePipelineMessage(pipeline.getLocation()),
                Component.literal(e.getMessage())
            );
            cir.setReturnValue(generateBackendCreateInfo(pipeline, FallbackShaderSource.getFallbackShaderSource(), shaderCreateInfos, uniformBindings));
        } else if(shaderSource instanceof FallbackShaderSource) {
            // failed compilation with fallback shaders, so try one last time with dummy shaders
            ShaderReloadManager.showShaderErrorMessage(
                // TODO: better messages
                Messages.getCouldntCompilePipelineWithFallbackMessage(pipeline.getLocation()),
                Component.literal(e.getMessage())
            );
            cir.setReturnValue(generateBackendCreateInfo(pipeline, DummyShaderSource.INSTANCE, shaderCreateInfos, uniformBindings));
        }
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/pipeline/ShaderSource;getShader(Lnet/minecraft/resources/Identifier;Lcom/mojang/renderpearl/api/pipeline/ShaderType;)Ljava/lang/String;"),
        method = "loadShaderSource"
    )
    private static String tryLoadingDummyShaderIfEmpty(ShaderSource instance, Identifier identifier, ShaderType shaderType, Operation<String> original) {
        var orig = original.call(instance, identifier, shaderType);
        if(orig == null) {
            orig = original.call(DummyShaderSource.INSTANCE, identifier, shaderType);
            ShaderReloadManager.showShaderErrorMessage(
                Messages.getCouldntFindSourceMessage(shaderType.getName(), identifier),
                null
            );
        }
        return orig;
    }
}
