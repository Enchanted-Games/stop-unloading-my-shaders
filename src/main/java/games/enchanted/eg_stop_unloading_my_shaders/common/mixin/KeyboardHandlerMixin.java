package games.enchanted.eg_stop_unloading_my_shaders.common.mixin;

import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    @Inject(
        at = @At("RETURN"),
        method = "handleDebugKeys",
        cancellable = true
    )
    private void eg_sumr$addReloadShadersKey(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if(keyEvent.key() == GLFW.GLFW_KEY_R) {
            ShaderReloadManager.triggerReload();
            cir.setReturnValue(true);
        }
    }
}
