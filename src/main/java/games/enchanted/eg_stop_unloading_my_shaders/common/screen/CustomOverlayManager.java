package games.enchanted.eg_stop_unloading_my_shaders.common.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.texture.TickableTexture;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomOverlayManager implements Renderable, TickableTexture {
    public static final ShaderMessageOverlay SHADER_MESSAGE_OVERLAY = new ShaderMessageOverlay();

    public static final CustomOverlayManager INSTANCE = new CustomOverlayManager();

    private static final List<CustomOverlay> customOverlays = List.of(SHADER_MESSAGE_OVERLAY);

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        customOverlays.forEach(customOverlay -> customOverlay.render(guiGraphics, mouseX, mouseY, partialTick));
    }

    @Override
    public void tick() {
        customOverlays.forEach(TickableTexture::tick);
    }

    public boolean scrollOverlays(double mouseX, double mouseY, double scrollX, double scrollY) {
        for (CustomOverlay customOverlay : customOverlays) {
            boolean shouldCancel = customOverlay.onScroll(mouseX, mouseY, scrollX, scrollY);
            if (shouldCancel) return true;
        }
        return false;
    }

    public boolean clickOverlays(MouseButtonEvent mouseButtonEvent) {
        for (CustomOverlay customOverlay : customOverlays) {
            boolean shouldCancel = customOverlay.onClick(mouseButtonEvent);
            if (shouldCancel) return true;
        }
        return false;
    }
}
