package games.enchanted.eg_stop_unloading_my_shaders.common;

import games.enchanted.eg_stop_unloading_my_shaders.common.config.ConfigManager;
import games.enchanted.eg_stop_unloading_my_shaders.common.screen.CustomOverlayManager;
import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class ShaderReloadManager {
    private static boolean isHotReloading = false;
    private static boolean shouldLoadVanillaFallback = false;
    private static final List<ShaderLogMessage> knownErrorsThisReload = new ArrayList<>();

    public static void triggerReload() {
        isHotReloading = true;
        CustomOverlayManager.SHADER_MESSAGE_OVERLAY.clear();
        showReloadingShadersMessage();
        Minecraft minecraft = Minecraft.getInstance();
        SimpleReloadInstance.create(
            minecraft.getResourceManager(),
            List.of(minecraft.getShaderManager()),
            Util.backgroundExecutor(),
            minecraft,
            CompletableFuture.completedFuture(Unit.INSTANCE),
            false
        ).done()
        .whenComplete(((result, exception) -> {
            isHotReloading = false;
            clearKnownErrors();
            if(exception == null) return;
            Logging.error("Error while reloading shaders: {}", exception);
        }));
    }

    public static void showReloadingShadersMessage() {
        showMessage(Messages.getReloadingShadersMessage(), 200);
    }

    public static void showShaderErrorMessage(Component shortMessage, @Nullable Component longMessage) {
        ShaderLogMessage shaderLogMessage = new ShaderLogMessage(shortMessage.getString(), longMessage == null ? null : longMessage.getString());
        if(knownErrorsThisReload.contains(shaderLogMessage)) {
            return;
        } else {
            knownErrorsThisReload.add(shaderLogMessage);
        }
        showErrorMessage(shortMessage);
        if(longMessage != null) {
            showContinuationErrorMessage(longMessage);
        }
    }

    public static void showErrorMessage(Component message) {
        showMessage(Messages.appendMessagePrefix(Messages.MessagePrefix.ERROR, message));
    }

    public static void showContinuationErrorMessage(Component message) {
        showMessage(Messages.appendMessagePrefix(Messages.MessagePrefix.ERROR_CONTINUATION, Messages.colourMessageGrey(message)));
    }

    public static void showMessage(Component message) {
        showMessage(message, -1);
    }

    /**
     * Shows a message to the shader message overlay and/or Minecraft chat.
     *
     * @param message      the message
     * @param ticksVisible clears message after x ticks, if set will add this message as the first one.
     *                     Set to -1 to add a message normally. Only applies to the shader message
     *                     overlay
     */
    public static void showMessage(Component message, int ticksVisible) {
        if(ConfigManager.loggingMode.showInChat()) {
            Minecraft.getInstance().gui.getChat().addMessage(message);
        }
        if(!ConfigManager.loggingMode.showInBox()) return;
        if(ticksVisible > 0) {
            CustomOverlayManager.SHADER_MESSAGE_OVERLAY.addPinnedMessage(message, ticksVisible);
            return;
        }
        CustomOverlayManager.SHADER_MESSAGE_OVERLAY.addMessage(message);
        CustomOverlayManager.SHADER_MESSAGE_OVERLAY.setRemoveAllAfterTicks(isHotReloading ? 2400 : 1200);
    }

    private static void clearKnownErrors() {
        knownErrorsThisReload.clear();
    }

    public static boolean shouldLoadVanillaFallback() {
        return shouldLoadVanillaFallback;
    }
    public static void setShouldLoadVanillaFallback(boolean newValue) {
        shouldLoadVanillaFallback = newValue;
    }

    public static void finishedVanillaReload() {
        if(isHotReloading) return;
        clearKnownErrors();
    }

    public static void startedVanillaReload() {
        if(!Minecraft.getInstance().isGameLoadFinished()) return;
        if(isHotReloading) return;
        CustomOverlayManager.SHADER_MESSAGE_OVERLAY.clear();
    }

    public record ShaderLogMessage(String shortMessage, String longMessage) {}
}
