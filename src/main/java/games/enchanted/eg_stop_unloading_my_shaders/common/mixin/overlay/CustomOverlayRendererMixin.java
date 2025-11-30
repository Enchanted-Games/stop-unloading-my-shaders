package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.overlay;

import com.mojang.blaze3d.platform.Window;
import games.enchanted.eg_stop_unloading_my_shaders.common.screen.CustomOverlayManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class CustomOverlayRendererMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private GuiRenderState guiRenderState;

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"),
        method = "render"
    )
    private void eg_sumr$renderCustomOverlay(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if(!this.minecraft.isGameLoadFinished()) return;

        Window window = this.minecraft.getWindow();
        MouseHandler mouseHandler = this.minecraft.mouseHandler;
        int mouseX = (int)mouseHandler.getScaledXPos(window);
        int mouseY = (int)mouseHandler.getScaledYPos(window);
        GuiGraphics graphics = new GuiGraphics(this.minecraft, this.guiRenderState, mouseX, mouseY);

        CustomOverlayManager.INSTANCE.render(graphics, mouseX, mouseY, deltaTracker.getGameTimeDeltaTicks());
    }
}
