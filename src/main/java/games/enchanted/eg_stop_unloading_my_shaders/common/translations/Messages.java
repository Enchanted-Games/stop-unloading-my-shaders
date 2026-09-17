package games.enchanted.eg_stop_unloading_my_shaders.common.translations;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
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
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.post_chain_load_error", "_Failed to load post_effect '%s':", location);
    }

    public static Component getRequestedPostEffectDoesNotExistMessage(String location) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.post_chain_does_not_exist", "_Post effect '%s' does not exist", location);
    }

    public static Component getPostChainInvalidTargetsMessage(String location, String targets) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.post_chain_invalid_targets", "_Requested post effect '%s' can not be used because it uses inaccessible targets: %s", location, targets);
    }

    public static Component getCouldntFindSourceMessage(String shaderType, Identifier shaderId) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.no_source_error", "_Couldn't find source for %s shader: %s", shaderType, shaderId);
    }

    public static Component getCouldntCompilePipelineMessage(Identifier pipelineId) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.pipeline_compilation_error", "_Couldn't compile shaders for pipeline '%s'", pipelineId);
    }

    public static Component getCouldntCompilePipelineWithFallbackMessage(Identifier pipelineId) {
        return Component.translatableWithFallback("debug.eg_stop_unloading_my_shaders.pipeline_fallback_compilation_error", "_Couldn't compile fallback shaders for pipeline '%s'. Resourcepacks will be unloaded", pipelineId);
    }

    public static Component colourMessageGrey(Component message) {
        return message.copy().withColor(CommonColors.LIGHT_GRAY);
    }

    public enum MessagePrefix {
        INFO("prefix.eg_stop_unloading_my_shaders.info", "_[SUMR Info]:", Style.EMPTY.withBold(true).withColor(ChatFormatting.YELLOW)),
        WARN("prefix.eg_stop_unloading_my_shaders.warn", "_[SUMR Warn]:", Style.EMPTY.withBold(true).withColor(ChatFormatting.GOLD)),
        ERROR("prefix.eg_stop_unloading_my_shaders.error", "_[SUMR Error]:", Style.EMPTY.withBold(true).withColor(ChatFormatting.RED)),
        ERROR_CONTINUATION("prefix.eg_stop_unloading_my_shaders.error_continuation", "_└", Style.EMPTY.withBold(true).withColor(ChatFormatting.RED)),
        SUMR("prefix.eg_stop_unloading_my_shaders.sumr", "_[SUMR]:", Style.EMPTY.withBold(true).withColor(ChatFormatting.YELLOW)),
        NONE();

        private final String translationKey;
        private final String fallback;
        private final Style style;

        MessagePrefix(String translationKey, String fallback, Style style) {
            this.translationKey = translationKey;
            this.fallback = fallback;
            this.style = style;
        }

        MessagePrefix() {
            this.translationKey = null;
            this.fallback = null;
            this.style = null;
        }

        Component getAsComponent() {
            if(this == NONE) return Component.empty();
            return Component.translatableWithFallback(translationKey, fallback).withStyle(style);
        }
    }
}
