package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.overlay;

import com.mojang.blaze3d.platform.Window;
import games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor.GuiRendererAccessor;
import games.enchanted.eg_stop_unloading_my_shaders.common.screen.CustomOverlayManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.GuiRenderer;
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
    @Shadow @Final private GuiRenderer guiRenderer;

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"),
        method = "render"
    )
    private void eg_sumr$renderCustomOverlay(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if(!this.minecraft.isGameLoadFinished()) return;

        Window window = this.minecraft.getWindow();
        MouseHandler mouseHandler = this.minecraft.mouseHandler;
        int mouseX = (int)mouseHandler.getScaledXPos(window);
        int mouseY = (int)mouseHandler.getScaledYPos(window);
        GuiGraphicsExtractor graphics = new GuiGraphicsExtractor(this.minecraft, ((GuiRendererAccessor) this.guiRenderer).eg_sumr$getRenderState(), mouseX, mouseY);

        CustomOverlayManager.INSTANCE.extractRenderState(graphics, mouseX, mouseY, deltaTracker.getGameTimeDeltaTicks());
    }
}
