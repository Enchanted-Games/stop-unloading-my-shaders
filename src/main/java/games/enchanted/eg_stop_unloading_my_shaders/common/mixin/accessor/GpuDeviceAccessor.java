package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor;

import com.mojang.renderpearl.backend.api.GpuDeviceBackend;
import com.mojang.renderpearl.frontend.FrontendGpuDevice;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FrontendGpuDevice.class)
public interface GpuDeviceAccessor {
    @Accessor("backend")
    GpuDeviceBackend eg_sumr$getBackend();
}
