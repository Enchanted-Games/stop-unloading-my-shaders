package games.enchanted.eg_stop_unloading_my_shaders.common.screen;

import games.enchanted.eg_stop_unloading_my_shaders.common.ModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class ShaderMessageOverlay extends CustomOverlay {
    private static final int PADDING_BLOCK = 6;
    private static final int PADDING_INLINE = 4;
    private static final int LINE_HEIGHT = 9;
    private static final int LINE_WIDTH = 320;
    private static final ResourceLocation LINE_BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "line_background");

    private final ArrayList<Component> rawMessages = new ArrayList<>();
    private final ArrayList<FormattedCharSequence> splitMessageLines = new ArrayList<>();
    private float scale = 1.0f;

    public void addMessage(Component message) {
        List<FormattedCharSequence> wrapped = ComponentRenderUtils.wrapComponents(message, LINE_WIDTH, Minecraft.getInstance().font);
        splitMessageLines.addAll(wrapped);
        rawMessages.add(message);
    }

    public void clear() {
        splitMessageLines.clear();
        rawMessages.clear();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(splitMessageLines.isEmpty()) return;
        for (int i = 0; i < splitMessageLines.size(); i++) {
            boolean isLast = i == splitMessageLines.size() - 1;
            boolean isFirst = i == 0;
            int x = PADDING_INLINE;
            int y = PADDING_BLOCK + (i * LINE_HEIGHT);
            int width = LINE_WIDTH + (PADDING_INLINE * 2);
            int height = isLast ? LINE_HEIGHT + PADDING_BLOCK : LINE_HEIGHT;
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().scale(scale);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, LINE_BACKGROUND_LOCATION, 0, isFirst ? 0 : y, width, isFirst ? y + height : height);
            guiGraphics.drawString(Minecraft.getInstance().font, splitMessageLines.get(i), x, y, -1);
            guiGraphics.pose().popMatrix();
        }
    }

    @Override
    public void tick() {

    }
}
