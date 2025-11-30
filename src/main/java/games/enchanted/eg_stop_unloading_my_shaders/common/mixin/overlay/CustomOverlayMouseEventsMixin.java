package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.overlay;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.Window;
import games.enchanted.eg_stop_unloading_my_shaders.common.screen.CustomOverlayManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class CustomOverlayMouseEventsMixin {
    @Shadow public abstract double getScaledXPos(Window window);

    @Shadow public abstract double getScaledYPos(Window window);

    @Shadow @Final private Minecraft minecraft;

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;"),
        method = "onScroll",
        cancellable = true
    )
    private void eg_sumr$scrollCustomOverlays(long windowPointer, double xOffset, double yOffset, CallbackInfo ci, @Local(ordinal = 3) double xScroll, @Local(ordinal = 4) double yScroll) {
        double xPos = this.getScaledXPos(this.minecraft.getWindow());
        double yPos = this.getScaledYPos(this.minecraft.getWindow());
        boolean shouldCancel = CustomOverlayManager.INSTANCE.scrollOverlays(xPos, yPos, xScroll, yScroll);
        if(shouldCancel) ci.cancel();
    }

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;"),
        method = "onButton",
        cancellable = true
    )
    private void eg_sumr$clickCustomOverlays(long window, MouseButtonInfo buttonInfo, int action, CallbackInfo ci) {
        double xPos = this.getScaledXPos(this.minecraft.getWindow());
        double yPos = this.getScaledYPos(this.minecraft.getWindow());
        boolean shouldCancel = CustomOverlayManager.INSTANCE.clickOverlays(new MouseButtonEvent(xPos, yPos, buttonInfo));
        if(shouldCancel) ci.cancel();
    }
}
