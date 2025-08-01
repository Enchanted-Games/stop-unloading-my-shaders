package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor;

import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShaderManager.class)
public interface ShaderManagerAccessor {
    @Invoker("prepare")
    ShaderManager.Configs eg_sumr$invokePrepare(ResourceManager resourceManager, ProfilerFiller profiler);
}
