package games.enchanted.eg_stop_unloading_my_shaders.common.config;

import games.enchanted.eg_stop_unloading_my_shaders.common.translations.Messages;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class ConfigScreen extends Screen {
    private static final Component TITLE = Component.translatableWithFallback("gui.eg_stop_unloading_my_shaders.config.title", "Stop Unloading My Resourcepacks Config").withStyle(Style.EMPTY.withBold(true));
    private static final String LOG_MODE_KEY = "gui.eg_stop_unloading_my_shaders.button.log_mode";
    private static final String LOG_MODE_FALLBACK = "_Log messages to: %s";
    private static final String DISABLE_LINKER_LOGS_KEY = "gui.eg_stop_unloading_my_shaders.button.disable_linker_logs";
    private static final String DISABLE_LINKER_LOGS_FALBACK = "_Hide linker logs: %s";

    private final HeaderAndFooterLayout headerAndFooterLayout = new HeaderAndFooterLayout(this);
    private final LinearLayout contentsFlow = LinearLayout.vertical().spacing(8);
    private final Screen parent;

    protected ConfigScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        this.headerAndFooterLayout.addTitleHeader(TITLE, this.font);
        this.headerAndFooterLayout.addToFooter(Button.builder(CommonComponents.GUI_DONE, (widget) -> this.onClose()).width(200).build());
        this.contentsFlow.setY(64);

        addConfigOptions();

        this.headerAndFooterLayout.visitWidgets(this::addRenderableWidget);
        this.contentsFlow.visitWidgets(this::addRenderableWidget);

        this.repositionElements();
    }

    protected void addConfigOptions() {
        contentsFlow.addChild(
            Button.builder(Messages.translateWithFallback(LOG_MODE_KEY, ConfigManager.loggingMode.getTranslated(), LOG_MODE_FALLBACK), (widget) -> {
                    ConfigManager.loggingMode = ErrorLoggingMode.getNext(ConfigManager.loggingMode);
                    widget.setMessage(Messages.translateWithFallback(LOG_MODE_KEY, ConfigManager.loggingMode.getTranslated(), LOG_MODE_FALLBACK));
                    ConfigManager.saveFile();
                })
                .tooltip(Tooltip.create(Component.translatable(LOG_MODE_KEY + ".tooltip")))
                .bounds(this.width / 2 - (Button.BIG_WIDTH / 2), this.height / 2 - 20 - (Button.DEFAULT_HEIGHT + 4), Button.BIG_WIDTH, Button.DEFAULT_HEIGHT)
                .build()
        );

        contentsFlow.addChild(
            Button.builder(Messages.translateWithFallback(DISABLE_LINKER_LOGS_KEY, getToggledComponent(CommonComponents.OPTION_ON, CommonComponents.OPTION_OFF, ConfigManager.disableLinkerLogs), DISABLE_LINKER_LOGS_FALBACK), (widget) -> {
                    ConfigManager.disableLinkerLogs = !ConfigManager.disableLinkerLogs;
                    widget.setMessage(Messages.translateWithFallback(DISABLE_LINKER_LOGS_KEY, getToggledComponent(CommonComponents.OPTION_ON, CommonComponents.OPTION_OFF, ConfigManager.disableLinkerLogs), DISABLE_LINKER_LOGS_FALBACK));
                    ConfigManager.saveFile();
                })
                .tooltip(Tooltip.create(Component.translatable(DISABLE_LINKER_LOGS_KEY + ".tooltip")))
                .bounds(this.width / 2 - (Button.BIG_WIDTH / 2), this.height / 2 - 20 - (Button.DEFAULT_HEIGHT + 3), Button.BIG_WIDTH, Button.DEFAULT_HEIGHT)
                .build()
        );

        this.repositionElements();
    }

    public static Component getToggledComponent(Component first, Component second, boolean toggle) {
        return toggle ? first : second;
    }

    @Override
    protected void repositionElements() {
        this.headerAndFooterLayout.arrangeElements();

        this.contentsFlow.setX((this.width / 2) - (contentsFlow.getWidth() / 2));
        this.contentsFlow.arrangeElements();
    }

    @Override
    public void onClose() {
        ConfigManager.logCurrentConfig();
        assert this.minecraft != null;
        this.minecraft.setScreen(parent);
    }

    public static Screen createConfigScreen(Screen parent) {
        return new ConfigScreen(parent);
    }
}