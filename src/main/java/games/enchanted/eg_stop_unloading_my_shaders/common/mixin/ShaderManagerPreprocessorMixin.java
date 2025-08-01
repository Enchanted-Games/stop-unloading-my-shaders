package games.enchanted.eg_stop_unloading_my_shaders.common.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import games.enchanted.eg_stop_unloading_my_shaders.common.Logging;
import org.spongepowered.asm.mixin.Mixin;

// targets the inner GlslPreprocessor class in createPreprocessor
@Mixin(targets = "net.minecraft.client.renderer.ShaderManager$1")
public class ShaderManagerPreprocessorMixin {
    @WrapMethod(
        method = "applyImport"
    )
    public String eg_sumr$catchImportErrors(boolean useFullPath, String directory, Operation<String> original) {
        String originalImport;
        try {
            originalImport = original.call(useFullPath, directory);
        } catch (NullPointerException e) {
            Logging.error("Invalid glsl import directive {}: Could not find file to import", directory);
            return "#error Invalid import directive: file [%s] was not found".formatted(directory);
        } catch (Exception e) {
            Logging.error("Invalid glsl import directive {}: Unknown error occurred\n{}", directory, e.getMessage());
            return "#error Invalid import directive [%s], unknown error occurred: %s".formatted(directory, e.getMessage());
        }
        return originalImport;
    }
}
