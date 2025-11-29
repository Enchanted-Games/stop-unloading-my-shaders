package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.shader.gl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.shaders.ShaderType;
import games.enchanted.eg_stop_unloading_my_shaders.common.Logging;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import games.enchanted.eg_stop_unloading_my_shaders.common.duck.GpuDeviceAdditions;
import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Map;
import java.util.function.Function;

@Mixin(GlDevice.class)
class GlDeviceMixin implements GpuDeviceAdditions {
    @Unique
    private boolean eg_sumr$bypassPipelineCache = false;

    @Override
    public void eg_sumr$setBypassPipelineCache(boolean bypass) {
        this.eg_sumr$bypassPipelineCache = bypass;
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"),
        method = {"precompilePipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lcom/mojang/blaze3d/shaders/ShaderSource;)Lcom/mojang/blaze3d/opengl/GlRenderPipeline;", "getOrCompileShader"}
    )
    private <K, V> V eg_sumr$loadFallbacksIfNecessary(Map<K, V> instance, K key, Function<? super K, ? extends V> theThingToComputeIfAbsent, Operation<V> original) {
        if(eg_sumr$bypassPipelineCache) {
            V val = theThingToComputeIfAbsent.apply(key);
            instance.put(key, val);
            return val;
        }
        return original.call(instance, key, theThingToComputeIfAbsent);
    }

    // wraps the first error call after checking if the shader source exists
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 0, remap = false),
        method = "compileShader"
    )
    private void eg_sumr$showFailedToFindShaderMessage(Logger instance, String string, Object oShaderType, Object oLocation, Operation<Void> original) {
        if(oLocation instanceof Identifier location && oShaderType instanceof ShaderType shaderType) {
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
        if(objects[1] instanceof Identifier location && objects[0] instanceof String typeName && objects[2] instanceof String compilationMessage) {
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
