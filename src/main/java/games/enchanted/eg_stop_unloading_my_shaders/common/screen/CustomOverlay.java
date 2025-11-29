package games.enchanted.eg_stop_unloading_my_shaders.common.screen;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.texture.TickableTexture;

public abstract class CustomOverlay implements Renderable, TickableTexture {
    boolean onScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
        return false;
    }

    @Override
    public void tick() {
    }
}
