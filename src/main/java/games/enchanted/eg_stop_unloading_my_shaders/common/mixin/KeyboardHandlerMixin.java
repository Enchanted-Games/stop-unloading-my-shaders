package games.enchanted.eg_stop_unloading_my_shaders.common.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import games.enchanted.eg_stop_unloading_my_shaders.common.ModEntry;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    @Shadow @Final private Minecraft minecraft;

    @Shadow protected abstract boolean handleDebugKeys(int key);

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getWindow()Lcom/mojang/blaze3d/platform/Window;", ordinal = 0),
        method = "keyPress"
    )
    private void eg_sumy$handleReloadShaderKeys(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if(ModEntry.debugKeysAnywhereEnabled) return;
        if(this.minecraft.screen instanceof PauseScreen pause && !pause.showsPauseMenu()) return;
        if(this.minecraft.screen instanceof GameModeSwitcherScreen) return;

        if(action != 0 && (this.minecraft.screen != null)) {
            if(!InputConstants.isKeyDown(windowPointer, GLFW.GLFW_KEY_F3) && !InputConstants.isKeyDown(windowPointer, GLFW.GLFW_KEY_R)) return;
            handleDebugKeys(key);
        }
    }

    @Inject(
        at = @At("RETURN"),
        method = "handleDebugKeys"
    )
    private void eg_sumy$addReloadShadersKey(int key, CallbackInfoReturnable<Boolean> cir) {
        if(key == GLFW.GLFW_KEY_R) {
            ShaderReloadManager.triggerReload();
        }
    }
}
