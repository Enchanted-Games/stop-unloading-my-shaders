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

    public static Component translateWithFallback(String key, Component value, String fallback) {
        return Component.translatableWithFallback(key, fallback, value.getString());
    }

    public static Component getReloadingShadersMessage() {
        return appendMessagePrefix(Messages.MessagePrefix.INFO, Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.reloading_shaders", "_Reloading shaders"));
    }

    public static Component getFailedToLoadPostChainMessage(String location) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.post_chain_load_error", "_Failed to load post_effect %s:", location);
    }

    public static Component getCouldntFindPostChainSource(String location) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.no_post_chain_source_error", "_Couldn't find config .json for post_effect: %s", location);
    }

    public static Component getFailedToLinkMessage(String pipelineLocation) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.linkage_error", "_Failed to link programs for pipeline %s:", pipelineLocation);
    }

    public static Component getCouldntFindSourceMessage(String shaderType, String shaderLocation) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.no_source_error", "_Couldn't find source for %s shader: %s", shaderType, shaderLocation);
    }

    public static Component getCouldntCompileShaderMessage(String shaderType, String shaderLocation) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.compilation_error", "_Couldn't compile %s shader: %s", shaderType, shaderLocation);
    }

    public static Component getCouldntGetFullErrorMessage() {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.no_full_error", "_Couldn't get full error message");
    }

    public static Component colourMessageGrey(Component message) {
        return message.copy().withColor(CommonColors.LIGHT_GRAY);
    }

    public enum MessagePrefix {
        INFO("prefix.eg_stop_unloading_my_shaders.info", "_[SUMR Info]:", Style.EMPTY.withBold(true).withColor(ChatFormatting.YELLOW)),
        ERROR("prefix.eg_stop_unloading_my_shaders.error", "_[SUMR Error]:", Style.EMPTY.withBold(true).withColor(ChatFormatting.RED)),
        ERROR_CONTINUATION("prefix.eg_stop_unloading_my_shaders.error_continuation", "_└", Style.EMPTY.withBold(true).withColor(ChatFormatting.RED));

        private final String translationKey;
        private final String fallback;
        private final Style style;

        MessagePrefix(String translationKey, String fallback, Style style) {
            this.translationKey = translationKey;
            this.fallback = fallback;
            this.style = style;
        }

        Component getAsComponent() {
            return Component.translatableWithFallback(translationKey, fallback).withStyle(style);
        }
    }
}
