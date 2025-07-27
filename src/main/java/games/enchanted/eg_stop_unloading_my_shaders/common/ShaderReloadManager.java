package games.enchanted.eg_stop_unloading_my_shaders.common;

import games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor.KeyboardHandlerAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class ShaderReloadManager {
    private static boolean isHotReloading = false;
    private static boolean shouldLoadVanillaFallback = false;
    private static final List<ShaderInfo> knownErrorsThisReload = new ArrayList<>();
    private static final List<ShaderInfo> shownHiddenMessagesWarning = new ArrayList<>();

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

    public static void showShaderErrorMessage(ShaderInfo shaderInfo, String shortMessage, @Nullable String fullMessage) {
        if(knownErrorsThisReload.contains(shaderInfo)) {
            if(shownHiddenMessagesWarning.contains(shaderInfo)) return;
            showMessage(Component.literal("This shader has multiple warnings, see log for more info").withColor(0xbbbbbb));
            shownHiddenMessagesWarning.add(shaderInfo);
            return;
        } else {
            knownErrorsThisReload.add(shaderInfo);
        }
        showMessage(Component.literal(shortMessage));
        if(fullMessage != null) {
            showMessage(Component.literal(fullMessage));
        }
    }

    public static void showReloadingShadersMessage() {
        showMessage(KeyboardHandlerAccessor.eg_sumy$invokeDecorateDebugComponent(ChatFormatting.YELLOW, Component.literal("Reloading Shaders")));
    }

    public static void showMessage(Component message) {
        Minecraft.getInstance().gui.getChat().addMessage(message);
    }

    private static void clearKnownErrors() {
        knownErrorsThisReload.clear();
        shownHiddenMessagesWarning.clear();
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

    public record ShaderInfo(ResourceLocation location, String type) {}
}
