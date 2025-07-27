package games.enchanted.eg_stop_unloading_my_shaders.common.util;

import net.minecraft.client.renderer.PostChainConfig;

import java.util.List;
import java.util.Map;

public class PostChainUtil {
    public static PostChainConfig createDummyPostChainConfig() {
        return new PostChainConfig(Map.of(), List.of());
    }
}
