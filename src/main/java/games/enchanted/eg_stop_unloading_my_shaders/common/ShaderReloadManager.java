package games.enchanted.eg_stop_unloading_my_shaders.common;

import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.Unit;
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
        showMessage(Messages.getReloadingShadersMessage());
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
        Minecraft.getInstance().gui.getChat().addMessage(message);
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

    public record ShaderLogMessage(String shortMessage, String longMessage) {}
}
