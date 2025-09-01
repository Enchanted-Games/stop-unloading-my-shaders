package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.shader.gl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.opengl.GlProgram;
import games.enchanted.eg_stop_unloading_my_shaders.common.config.ConfigManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(value = GlProgram.class, remap = false)
public class GlProgramMixin {
    @WrapOperation(
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/opengl/GlStateManager;glGetProgramInfoLog(II)Ljava/lang/String;")
        ),
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;[Ljava/lang/Object;)V"),
        method = "link"
    )
    private static void eg_sumr$conditionallyPreventLinkerInfoLogs(Logger instance, String s, Object[] objects, Operation<Void> original) {
        if(!ConfigManager.disableLinkerLogs) {
            original.call(instance, s, objects);
        }
    }
}
