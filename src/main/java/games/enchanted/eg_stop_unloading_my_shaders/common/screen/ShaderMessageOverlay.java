package games.enchanted.eg_stop_unloading_my_shaders.common.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;

public class ShaderMessageOverlay extends CustomOverlay {
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ResourceLocation.withDefaultNamespace("widget/button"), 0, 0, 129, 129);
    }

    @Override
    public void tick() {

    }
}
