package games.enchanted.eg_stop_unloading_my_shaders.common;

import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class FallbackShaderSource implements ShaderSource {
    ShaderManager.Configs configs = ModConstants.getVanillaShaderConfigs();

    public static ShaderSource getFallbackShaderSource() {
        return new FallbackShaderSource();
    }

    @Override
    public @Nullable String getShader(Identifier id, ShaderType type) {
        return configs.getShader(id, type);
    }

    @Override
    public @Nullable CachedIncludeSource getInclude(Identifier id) {
        return configs.getInclude(id);
    }

    @Override
    public void close() {

    }
}
