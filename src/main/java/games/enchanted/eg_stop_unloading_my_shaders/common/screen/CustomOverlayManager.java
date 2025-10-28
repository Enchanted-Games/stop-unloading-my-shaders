package games.enchanted.eg_stop_unloading_my_shaders.common.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
//? if minecraft: <= 1.21.10 {
/*import net.minecraft.client.renderer.texture.Tickable;
 *///?} else {
import net.minecraft.client.renderer.texture.TickableTexture;
//?}

import java.util.List;

public class CustomOverlayManager implements Renderable,
//? if minecraft: <= 1.21.10 {
    /*Tickable
*///?} else {
    TickableTexture
//?}
{
    public static final ShaderMessageOverlay SHADER_MESSAGE_OVERLAY = new ShaderMessageOverlay();

    public static final CustomOverlayManager INSTANCE = new CustomOverlayManager();

    private static final List<CustomOverlay> customOverlays = List.of(SHADER_MESSAGE_OVERLAY);

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        customOverlays.forEach(customOverlay -> customOverlay.render(guiGraphics, mouseX, mouseY, partialTick));
    }

    @Override
    public void tick() {
        customOverlays.forEach(
            //? if minecraft: <= 1.21.10 {
            /*Tickable::tick
             *///?} else {
            TickableTexture::tick
            //?}
        );
    }

    public boolean scrollOverlays(double mouseX, double mouseY, double scrollX, double scrollY) {
        for (CustomOverlay customOverlay : customOverlays) {
            boolean shouldCancel = customOverlay.onScroll(mouseX, mouseY, scrollX, scrollY);
            if (shouldCancel) return true;
        }
        return false;
    }
}
