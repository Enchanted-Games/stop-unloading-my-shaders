package games.enchanted.eg_stop_unloading_my_shaders.common.translations;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.CommonColors;

public class Messages {
    public static Component appendMessagePrefix(MessagePrefix messagePrefix, Component message) {
        return Component.empty().append(messagePrefix.getAsComponent()).append(CommonComponents.SPACE).append(message);
    }

    public static Component getReloadingShadersMessage() {
        return appendMessagePrefix(Messages.MessagePrefix.INFO, Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.reloading_shaders", "Reloading shaders"));
    }

    public static Component getFailedToLoadPostChainMessage(String location) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.post_chain_load_error", "Failed to load post_effect %s:", location);
    }

    public static Component getFailedToLinkMessage(String pipelineLocation) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.linkage_error", "Failed to link programs for pipeline %s:", pipelineLocation);
    }

    public static Component getCouldntFindSourceMessage(String shaderType, String shaderLocation) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.no_source_error", "Couldn't find source for %s shader: %s", shaderType, shaderLocation);
    }

    public static Component getCouldntCompileShaderMessage(String shaderType, String shaderLocation) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.compilation_error", "Couldn't compile %s shader: %s", shaderType, shaderLocation);
    }

    public static Component getCouldntGetFullErrorMessage() {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.no_full_error", "Couldn't get full error message");
    }

    public static Component colourMessageGrey(Component message) {
        return message.copy().withColor(CommonColors.LIGHT_GRAY);
    }

    public enum MessagePrefix {
        INFO("prefix.eg_stop_unloading_my_shaders.info", Style.EMPTY.withBold(true).withColor(ChatFormatting.YELLOW)),
        ERROR("prefix.eg_stop_unloading_my_shaders.error", Style.EMPTY.withBold(true).withColor(ChatFormatting.RED)),
        ERROR_CONTINUATION("prefix.eg_stop_unloading_my_shaders.error_continuation", Style.EMPTY.withBold(true).withColor(ChatFormatting.RED));

        private final String translationKey;
        private final Style style;

        MessagePrefix(String translationKey, Style style) {
            this.translationKey = translationKey;
            this.style = style;
        }

        Component getAsComponent() {
            return Component.translatable(translationKey).withStyle(style);
        }
    }
}
