package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.accessor;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyboardHandler.class)
public interface KeyboardHandlerAccessor {
    @Invoker("decorateDebugComponent")
    static Component eg_sumy$invokeDecorateDebugComponent(ChatFormatting formatting, Component component) {
        throw new AssertionError("KeyboardHandlerAccessor not asserted");
    }
}
