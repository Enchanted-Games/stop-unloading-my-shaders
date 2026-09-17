package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.gl;

import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.backend.opengl.GlProgram;
import com.mojang.renderpearl.backend.opengl.GlShaderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(GlProgram.class)
public class GlProgramMixin {
    @ModifyVariable(
        at = @At("HEAD"),
        method = "link",
        argsOnly = true
    )
    private static List<GlShaderModule> removeDuplicateCompiledShaders(List<GlShaderModule> compiledShaders) {
        List<ShaderType> addedTypes = new ArrayList<>();
        List<GlShaderModule> newShaders = new ArrayList<>();

        for (GlShaderModule shader : compiledShaders) {
            if(addedTypes.contains(shader.getType())) continue;
            newShaders.add(shader);
            addedTypes.add(shader.getType());
        }
        return newShaders;
    }
}
