package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor;

import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShaderManager.class)
public interface ShaderManagerAccessor {
    @Invoker("loadConfigs")
    static ShaderManager.Configs eg_sumr$loadConfigs(final ResourceManager manager) {
        throw new AssertionError("Accessor not applied (SUMR ShaderManagerAccessor)");
    };
}
