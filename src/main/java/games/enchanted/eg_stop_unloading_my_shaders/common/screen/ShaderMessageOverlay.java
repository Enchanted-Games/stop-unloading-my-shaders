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
    private static final ResourceLocation LINE_BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "error_box/line_background");
    private static final ResourceLocation LINE_BACKGROUND_HOVER_LOCATION = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "error_box/line_background_hover");
    private static final int ARROW_SIZE = 6;
    private static final ResourceLocation ARROW_DOWN_LOCATION = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "error_box/arrow_down");
    private static final ResourceLocation ARROW_UP_LOCATION = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "error_box/arrow_up");

    private int removePinnedAtAge = -1;
    private int removeAllAtAge = -1;
    private final ArrayList<Component> rawMessages = new ArrayList<>();
    private final ArrayList<FormattedCharSequence> splitMessageLines = new ArrayList<>();

    private int currentScrollIndex = 0;

    private final float scale = 1.0f;
    private final int lineWidth = 320;
    private final int lineHeight = 9;
    private final int visibleLines = 9;

    private int age = 0;

    public void addMessage(Component message) {
        addPinnedMessage(message, -1);
    }

    public void addPinnedMessage(Component message, int ticksVisible) {
        List<FormattedCharSequence> wrapped = ComponentRenderUtils.wrapComponents(message, lineWidth, Minecraft.getInstance().font);
        if(ticksVisible > -1) {
            this.splitMessageLines.addAll(0, wrapped);
            this.rawMessages.addFirst(message);
            this.removePinnedAtAge = this.age + ticksVisible;
        } else {
            this.splitMessageLines.addAll(wrapped);
            this.rawMessages.add(message);
        }
        scrollByLines(0);
    }

    private void scrollByLines(int lines) {
        this.currentScrollIndex += lines;
        int totalLines = this.splitMessageLines.size();
        int maxScroll = totalLines - visibleLines;
        if (this.currentScrollIndex > maxScroll) {
            this.currentScrollIndex = maxScroll;
        }
        if (this.currentScrollIndex <= 0) {
            this.currentScrollIndex = 0;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.age++;
        if(this.removePinnedAtAge > -1 && this.age > this.removePinnedAtAge && !this.rawMessages.isEmpty()) {
            this.rawMessages.removeFirst();
            resplitMessages();
            this.removePinnedAtAge = -1;
        }
        if(this.removeAllAtAge > -1 && this.age > this.removeAllAtAge) {
            this.clear();
            this.removeAllAtAge = -1;
        }
    }

    private void resplitMessages() {
        this.splitMessageLines.clear();
        for (Component message : rawMessages) {
            List<FormattedCharSequence> wrapped = ComponentRenderUtils.wrapComponents(message, lineWidth, Minecraft.getInstance().font);
            this.splitMessageLines.addAll(wrapped);
        }
    }

    public void clear() {
        this.splitMessageLines.clear();
        this.rawMessages.clear();
        this.currentScrollIndex = 0;
    }

    public void setRemoveAllAfterTicks(int ticks) {
        int newRemoveAtAge = this.age + ticks;
        if(newRemoveAtAge > this.removeAllAtAge) this.removeAllAtAge = newRemoveAtAge;
    }

    private boolean isHoveringScrollBox(double mouseX, double mouseY) {
        if(mouseX > this.lineWidth + (PADDING_INLINE * 2)) return false;
        return !(mouseY > (this.lineHeight * Math.min(this.visibleLines, this.splitMessageLines.size())) + (PADDING_BLOCK * 2));
    }

    @Override
    public boolean onScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
        if(this.splitMessageLines.isEmpty()) return false;
        if(!isHoveringScrollBox(mouseX, mouseY)) return false;
        scrollByLines((int) Math.clamp(scrollY * -1.0, -1.0, 1.0));
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(this.splitMessageLines.isEmpty()) return;
        int linesToRender = Math.min(this.splitMessageLines.size() - this.currentScrollIndex, visibleLines);
        int width = lineWidth + (PADDING_INLINE * 2);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(this.scale);

        for (int i = 0; i < linesToRender; i++) {
            boolean isLastVisible = i == linesToRender - 1;
            boolean isFirstVisible = i == 0;
            int x = PADDING_INLINE;
            int y = PADDING_BLOCK + (i * lineHeight);
            int height = isLastVisible ? lineHeight + PADDING_BLOCK : lineHeight;



            guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                isHoveringScrollBox(mouseX, mouseY) ? LINE_BACKGROUND_HOVER_LOCATION : LINE_BACKGROUND_LOCATION,
                0,
                isFirstVisible ? 0 : y,
                width,
                isFirstVisible ? y + height : height
            );
            guiGraphics.drawString(
                Minecraft.getInstance().font,
                this.splitMessageLines.get(i + this.currentScrollIndex),
                x,
                y,
                -1
            );

        }

        boolean isAtTop = this.currentScrollIndex == 0;
        boolean isAtBottom = this.currentScrollIndex >= this.splitMessageLines.size() - this.visibleLines;

        if(!isAtTop) {
            guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                ARROW_UP_LOCATION,
                width / 2 - ARROW_SIZE / 2,
                0,
                ARROW_SIZE,
                ARROW_SIZE
            );
        }

        if(!isAtBottom) {
            guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                ARROW_DOWN_LOCATION,
                width / 2 - ARROW_SIZE / 2,
                linesToRender * lineHeight + PADDING_BLOCK,
                ARROW_SIZE,
                ARROW_SIZE
            );
        }

        guiGraphics.pose().popMatrix();
    }
}
