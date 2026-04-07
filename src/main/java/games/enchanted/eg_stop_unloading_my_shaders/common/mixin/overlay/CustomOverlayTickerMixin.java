package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.overlay;

import games.enchanted.eg_stop_unloading_my_shaders.common.screen.CustomOverlayManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class CustomOverlayTickerMixin {
    @Inject(
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Gui;screen:Lnet/minecraft/client/gui/screens/Screen;", opcode = Opcodes.GETFIELD),
        method = "tick"
    )
    private void eg_sumr$tickCustomOverlay(CallbackInfo ci) {
        CustomOverlayManager.INSTANCE.tick();
    }
}
