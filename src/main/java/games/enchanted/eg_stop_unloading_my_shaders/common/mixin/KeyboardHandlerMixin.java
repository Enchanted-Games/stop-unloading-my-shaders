package games.enchanted.eg_stop_unloading_my_shaders.common.mixin;

import games.enchanted.eg_stop_unloading_my_shaders.common.SUMRMod;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    @Inject(
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/Minecraft;options:Lnet/minecraft/client/Options;",
            opcode = Opcodes.GETFIELD,
            ordinal = 0
        ),
        method = "handleDebugKeys",
        cancellable = true
    )
    private void eg_sumr$addReloadShadersKey(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if(SUMRMod.HOT_RELOAD_KEYBIND.matches(keyEvent)) {
            ShaderReloadManager.triggerReload();
            cir.setReturnValue(true);
        }
    }
}
