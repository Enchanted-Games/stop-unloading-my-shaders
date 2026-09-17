package games.enchanted.eg_stop_unloading_my_shaders.common;

import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class DummyShaderSource implements ShaderSource {
    public static final DummyShaderSource INSTANCE = new DummyShaderSource();

    @Override
    public @Nullable String getShader(Identifier id, ShaderType type) {
        if(type == ShaderType.VERTEX) {
            return """
#version 330
#extension GL_ARB_separate_shader_objects : require

void main() {
    gl_Position = vec4(0.0, 0.0, 0.0, 1.0);
}
""";
        } else if(type == ShaderType.FRAGMENT) {
            return """
#version 330
#extension GL_ARB_separate_shader_objects : require

layout(location = 0) out vec4 fragColor;

void main() {
    fragColor = vec4(1.0, 0.0, 1.0, 1.0);
}
""";
        }
        return null;
    }

    @Override
    public @Nullable CachedIncludeSource getInclude(Identifier id) {
        return null;
    }

    @Override
    public void close() {

    }
}
