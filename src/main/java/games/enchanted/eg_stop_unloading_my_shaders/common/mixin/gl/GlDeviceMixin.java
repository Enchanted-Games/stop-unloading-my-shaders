package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.gl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.opengl.GlRenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.ShaderType;
import games.enchanted.eg_stop_unloading_my_shaders.common.ModConstants;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(GlDevice.class)
public class GlDeviceMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"),
        method = "getOrCompileShader"
    )
    private <K, V> Object eg_sumy$bypassShaderCacheToForceVanillaFallback(Map<K, V> shaderCache, K compilationKey, Function<? super K, ? extends V> mapping, Operation<V> original) {
        if(ShaderReloadManager.shouldLoadVanillaFallback()) return mapping.apply(compilationKey);

        return original.call(shaderCache, compilationKey, mapping);
    }

    @WrapMethod(
        method = "compilePipeline"
    )
    private GlRenderPipeline eg_sumy$checkForFailedShaderCompilationAndTriggerVanillaFallback(RenderPipeline pipeline, BiFunction<ResourceLocation, ShaderType, String> shaderSource, Operation<GlRenderPipeline> original) {
        ShaderReloadManager.setShouldLoadVanillaFallback(false);
        GlRenderPipeline compiledPipeline = original.call(pipeline, shaderSource);
        if(compiledPipeline.isValid()) return compiledPipeline;

        ShaderReloadManager.setShouldLoadVanillaFallback(true);
        return original.call(
            pipeline,
            (BiFunction<ResourceLocation, ShaderType, String>)(ResourceLocation location, ShaderType shaderType) -> {
                return ModConstants.getVanillaShaderConfigs().shaderSources().get(new ShaderManager.ShaderSourceKey(location, shaderType));
            }
        );
    }

    // wraps the first error call after checking if the shader source exists
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 0),
        method = "compileShader"
    )
    private void eg_sumy$showFailedToFindShaderMessage(Logger instance, String string, Object shaderType, Object id, Operation<Void> original) {
        ShaderReloadManager.showShaderErrorMessage(new ShaderReloadManager.ShaderInfo((ResourceLocation) id, ((ShaderType) shaderType).getName()), "Couldn't find source for %s shader: %s".formatted(shaderType, id), null);
    }

    // wraps the second error call after getting the shader compilation info
    @WrapOperation(
        slice = @Slice(from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/opengl/GlStateManager;glGetShaderInfoLog(II)Ljava/lang/String;")),
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;error(Ljava/lang/String;[Ljava/lang/Object;)V",
            ordinal = 0
        ),
        method = "compileShader"
    )
    private void eg_sumy$showFailedToCompileShaderMessage(Logger instance, String string, Object[] objects, Operation<Void> original) {
        ShaderReloadManager.showShaderErrorMessage(new ShaderReloadManager.ShaderInfo((ResourceLocation) objects[1], objects[0].toString()), "Couldn't compile %s shader: %s".formatted(objects[0], objects[1]), objects[2].toString());
    }
}
