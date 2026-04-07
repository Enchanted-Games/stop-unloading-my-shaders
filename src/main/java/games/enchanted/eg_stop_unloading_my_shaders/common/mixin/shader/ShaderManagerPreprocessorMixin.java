package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.shader;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import games.enchanted.eg_stop_unloading_my_shaders.common.Logging;
import net.minecraft.IdentifierException;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FileUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

// targets the inner GlslPreprocessor class in createPreprocessor
@Mixin(targets = "net.minecraft.client.renderer.ShaderManager$1")
public class ShaderManagerPreprocessorMixin {
    @Shadow @Final Identifier val$parentLocation;

    @WrapMethod(
        method = "applyImport"
    )
    public String eg_sumr$catchImportErrors(boolean useFullPath, String directory, Operation<String> original) {
        Identifier absoluteShaderLocation;
        if (useFullPath) {
            absoluteShaderLocation = val$parentLocation.withPath(parent -> FileUtil.normalizeResourcePath(parent + directory));
        } else {
            try {
                absoluteShaderLocation = Identifier.parse(directory).withPrefix("shaders/include/");
            } catch (IdentifierException var8) {
                absoluteShaderLocation = null;
            }
        }

        String originalImport;
        String shaderLocation = absoluteShaderLocation == null ? directory : absoluteShaderLocation.getNamespace() + "/" + absoluteShaderLocation.getPath();
        try {
            originalImport = original.call(useFullPath, directory);
        } catch (NullPointerException e) {
            Logging.error("Invalid glsl import directive {}: Could not find file to import", shaderLocation);
            return "#error Invalid import directive: file [%s] was not found".formatted(shaderLocation);
        } catch (Exception e) {
            Logging.error("Invalid glsl import directive {}: Unknown error occurred\n{}", shaderLocation, e.getMessage());
            return "#error Invalid import directive [%s], unknown error occurred: %s".formatted(shaderLocation, e.getMessage());
        }
        return originalImport;
    }
}
