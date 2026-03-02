package games.enchanted.eg_stop_unloading_my_shaders.common.mixin.shader;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_stop_unloading_my_shaders.common.Logging;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import games.enchanted.eg_stop_unloading_my_shaders.common.util.PostChainUtil;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostChainConfig;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.Set;

@Mixin(targets = "net/minecraft/client/renderer/ShaderManager$CompilationCache")
public class CompilationCacheMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;load(Lnet/minecraft/client/renderer/PostChainConfig;Lnet/minecraft/client/renderer/texture/TextureManager;Ljava/util/Set;Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/renderer/Projection;Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;)Lnet/minecraft/client/renderer/PostChain;"),
        method = "loadPostChain"
    )
    private PostChain eg_sumr$loadDummyPostChainIfError(PostChainConfig pass, TextureManager textureManager, Set<Identifier> config, Identifier name, Projection allowedExternalTargets, ProjectionMatrixBuffer id, Operation<PostChain> original) {
        PostChain originalChain;
        try {
            originalChain = original.call(pass, textureManager, config, name, allowedExternalTargets, id);
        } catch (Exception e) {
            ShaderReloadManager.showErrorMessage(Messages.getFailedToLoadPostChainMessage(name.toString()));
            ShaderReloadManager.showContinuationErrorMessage(Component.literal(e.getMessage()));
            Logging.error("Failed to load post chain {}:\n{}", name, e);
            return original.call(PostChainUtil.createDummyPostChainConfig(), textureManager, config, name, allowedExternalTargets, id);
        }
        return originalChain;
    }
    
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"),
        method = "loadPostChain"
    )
    private <V> V eg_sumr$loadDummyPostChainIfNoConfig(Map<Identifier, PostChain> instance, Object key, Operation<V> original) {
        V originalConfig = original.call(instance, key);
        if(originalConfig == null) {
            ShaderReloadManager.showErrorMessage(Messages.getCouldntFindPostChainSource(key.toString()));
            //noinspection unchecked
            return (V) PostChainUtil.createDummyPostChainConfig();
        }
        return originalConfig;
    }
}
