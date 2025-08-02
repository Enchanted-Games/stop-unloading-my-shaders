package games.enchanted.eg_stop_unloading_my_shaders.common.config;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JavaOps;
import games.enchanted.eg_stop_unloading_my_shaders.common.Logging;
import games.enchanted.eg_stop_unloading_my_shaders.common.ModConstants;
import games.enchanted.eg_stop_unloading_my_shaders.common.ShaderReloadManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;

public class ConfigManager {
    public static ErrorLoggingMode LOGGING_MODE = ErrorLoggingMode.BOX;

    private static final String CONFIG_FILE_NAME = "eg_stop_unloading_my_shaders.properties";

    private static Path getConfigFilePath() {
        return ModConstants.getConfigPath().resolve(CONFIG_FILE_NAME);
    }

    public static void loadFromFile() {
        Properties modProperties = new Properties();
        try {
            modProperties.load(new FileInputStream(getConfigFilePath().toFile()));
        } catch (IOException e) {
            boolean success = saveFile();
            if(!success) {
                ShaderReloadManager.showErrorMessage(Component.translatableWithFallback("config.eg_stop_unloading_my_shaders.couldnt_open_file", "_Couldn't open config file: %s", CONFIG_FILE_NAME));
                ShaderReloadManager.showContinuationErrorMessage(Component.literal(e.getMessage()));
                Logging.error(e.getMessage());
                Logging.error(Arrays.toString(e.getStackTrace()));
            }
        }

        String loggingMode = modProperties.getProperty("logging_mode");
        if(loggingMode != null) {
            DataResult<Pair<ErrorLoggingMode, Object>> result = StringRepresentable.fromValues(ErrorLoggingMode::values).decode(JavaOps.INSTANCE, loggingMode);
            if(result.error().isPresent()) {
                ShaderReloadManager.showErrorMessage(Component.translatableWithFallback("config.eg_stop_unloading_my_shaders.couldnt_parse_file_prop", "_Couldn't parse config property: %s", "logging_mode"));
                ShaderReloadManager.showContinuationErrorMessage(Component.literal(result.error().get().message()));
                Logging.error(result.error().get().message());
            } else {
                LOGGING_MODE = result.getOrThrow().getFirst();
            }
        }
        saveFile();
    }

    public static boolean saveFile() {
        Properties modProperties = new Properties();

        modProperties.setProperty("logging_mode", LOGGING_MODE.getSerializedName());

        try {
            modProperties.store(new FileWriter(getConfigFilePath().toFile()), "store to properties file");
        } catch (IOException e) {
            ShaderReloadManager.showErrorMessage(Component.translatableWithFallback("config.eg_stop_unloading_my_shaders.couldnt_save_file", "_Couldn't save config file: %s", CONFIG_FILE_NAME));
            ShaderReloadManager.showContinuationErrorMessage(Component.literal(e.getMessage()));
            Logging.error(e.getMessage());
            Logging.error(Arrays.toString(e.getStackTrace()));
            return false;
        }

        return true;
    }

    static {
        loadFromFile();
    }
}
