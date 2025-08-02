package games.enchanted.eg_stop_unloading_my_shaders.common.config;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ErrorLoggingMode implements StringRepresentable {
    BOX("box", true, false),
    CHAT("chat", false, true),
    BOTH("both", true, true);

    private final String name;
    private final boolean showInBox;
    private final boolean showInChat;

    private static final ErrorLoggingMode[] values = values();

    ErrorLoggingMode(String name, boolean showInBox, boolean showInChat) {
        this.name = name;
        this.showInBox = showInBox;
        this.showInChat = showInChat;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public Component getTranslated() {
        return Component.translatableWithFallback("config.eg_stop_unloading_my_shaders.log_mode." + name, name);
    }

    public boolean showInChat() {
        return showInChat;
    }

    public boolean showInBox() {
        return showInBox;
    }

    public static ErrorLoggingMode getNext(ErrorLoggingMode mode) {
        return values[(mode.ordinal() + 1) % values.length];
    }
}
