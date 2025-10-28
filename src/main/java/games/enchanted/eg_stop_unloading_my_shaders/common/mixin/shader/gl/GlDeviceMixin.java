package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.shader.gl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.shaders.ShaderType;
import games.enchanted.eg_stop_unloading_my_shaders.common.Logging;
import games.enchanted.eg_stop_unloading_my_shaders.common.ModConstants;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
//? if minecraft: > 1.21.10 {
import com.mojang.blaze3d.opengl.GlShaderModule;
import com.mojang.blaze3d.shaders.ShaderSource;
import org.spongepowered.asm.mixin.injection.Coerce;
//?} else {
/*import com.mojang.blaze3d.opengl.GlRenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.ShaderManager;
import java.util.function.BiFunction;
*///?}

import java.util.Map;
import java.util.function.Function;

@Mixin(GlDevice.class)
public class GlDeviceMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"),
        method = "getOrCompileShader"
    )
    private <K, V> Object eg_sumr$bypassShaderCacheToForceVanillaFallback(Map<K, V> shaderCache, K compilationKey, Function<? super K, ? extends V> mapping, Operation<V> original) {
        if(ShaderReloadManager.shouldLoadVanillaFallback()) return mapping.apply(compilationKey);

        return original.call(shaderCache, compilationKey, mapping);
    }

    //? if minecraft: <= 1.21.10 {
    /*@WrapMethod(
        method = "compilePipeline"
    )
    private GlRenderPipeline eg_sumr$checkForFailedShaderCompilationAndTriggerVanillaFallback(RenderPipeline pipeline, BiFunction<ResourceLocation, ShaderType, String> shaderSource, Operation<GlRenderPipeline> original) {

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

    // wraps the error call after attempting to link gl programs
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false),
        slice = @Slice(from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/opengl/GlProgram;link(Lcom/mojang/blaze3d/opengl/GlShaderModule;Lcom/mojang/blaze3d/opengl/GlShaderModule;Lcom/mojang/blaze3d/vertex/VertexFormat;Ljava/lang/String;)Lcom/mojang/blaze3d/opengl/GlProgram;")),
        method = "compilePipeline"
    )
    private void eg_sumr$logShaderLinkerError(Logger instance, String string, Object oLocation, Object oCompilationException, Operation<Void> original) {
        original.call(instance, string, oLocation, oCompilationException);
        if(oLocation instanceof ResourceLocation location1 && oCompilationException instanceof ShaderManager.CompilationException compilationException) {
            ShaderReloadManager.showShaderErrorMessage(
                Messages.getFailedToLinkMessage(location1.toString()),
                Component.literal(compilationException.getMessage())
            );
        } else {
            ShaderReloadManager.showShaderErrorMessage(
                Messages.getFailedToLinkMessage(oLocation.toString()),
                Messages.getCouldntGetFullErrorMessage()
            );
            Logging.error("eg_sumr$logShaderLinkerError did not receive the correct parameters, please report this! Got '%s' and '%s'".formatted(
                oLocation.getClass().getName(),
                oCompilationException.getClass().getName()
            ));
        }
    }
    *///?} else {
    @WrapMethod(
        method = "compileShader"
    )
    private GlShaderModule eg_sumr$checkForFailedShaderCompilationAndTriggerVanillaFallback(@Coerce Object shaderCompilationKey, ShaderSource shaderSource, Operation<GlShaderModule> original) {
        ShaderReloadManager.setShouldLoadVanillaFallback(false);
        GlShaderModule shaderModule = original.call(shaderCompilationKey, shaderSource);
        if(shaderModule != GlShaderModule.INVALID_SHADER) return shaderModule;

        ShaderReloadManager.setShouldLoadVanillaFallback(true);
        return original.call(
            shaderCompilationKey,
            ModConstants.getVanillaShaderSource()
        );
    }
    //?}

    // wraps the first error call after checking if the shader source exists
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 0, remap = false),
        method = "compileShader"
    )
    private void eg_sumr$showFailedToFindShaderMessage(Logger instance, String string, Object oShaderType, Object oLocation, Operation<Void> original) {
        if(oLocation instanceof ResourceLocation location && oShaderType instanceof ShaderType shaderType) {
            ShaderReloadManager.showShaderErrorMessage(
                Messages.getCouldntFindSourceMessage(shaderType.getName(), location.toString()),
                null
            );
        } else {
            ShaderReloadManager.showShaderErrorMessage(
                Messages.getCouldntFindSourceMessage(oShaderType.toString(), oLocation.toString()),
                null
            );
            Logging.error("eg_sumr$showFailedToFindShaderMessage did not receive the correct parameters, please report this! Got '%s' and '%s'".formatted(
                oShaderType.getClass().getName(),
                oLocation.getClass().getName()
            ));
        }
        original.call(instance, string, oShaderType, oLocation);
    }

    // wraps the second error call after getting the shader compilation info
    @WrapOperation(
        slice = @Slice(from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/opengl/GlStateManager;glGetShaderInfoLog(II)Ljava/lang/String;", remap = false)),
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;error(Ljava/lang/String;[Ljava/lang/Object;)V",
            ordinal = 0,
            remap = false
        ),
        method = "compileShader"
    )
    private void eg_sumr$showFailedToCompileShaderMessage(Logger instance, String string, Object[] objects, Operation<Void> original) {
        if(objects[1] instanceof ResourceLocation location && objects[0] instanceof String typeName && objects[2] instanceof String compilationMessage) {
            ShaderReloadManager.showShaderErrorMessage(
                Messages.getCouldntCompileShaderMessage(typeName, location.toString()),
                Component.literal(compilationMessage)
            );
        } else {
            ShaderReloadManager.showShaderErrorMessage(
                Messages.getCouldntCompileShaderMessage(objects[0].toString(), objects[1].toString()),
                Messages.getCouldntGetFullErrorMessage()
            );
            Logging.error("eg_sumr$showFailedToCompileShaderMessage did not receive the correct parameters, please report this! Got '%s', '%s', and '%s'".formatted(
                objects[0].getClass().getName(),
                objects[1].getClass().getName(),
                objects[2].getClass().getName()
            ));
        }
        original.call(instance, string, objects);
    }
}
