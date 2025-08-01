package games.enchanted.eg_stop_unloading_my_shaders.common.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.texture.Tickable;

import java.util.List;

public class CustomOverlayManager implements Renderable, Tickable {
    public static final ShaderMessageOverlay SHADER_MESSAGE_OVERLAY = new ShaderMessageOverlay();

    public static final CustomOverlayManager INSTANCE = new CustomOverlayManager();

    private static final List<CustomOverlay> customOverlays = List.of(SHADER_MESSAGE_OVERLAY);

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        customOverlays.forEach(customOverlay -> customOverlay.render(guiGraphics, mouseX, mouseY, partialTick));
    }

    @Override
    public void tick() {
        customOverlays.forEach(Tickable::tick);
    }
}
