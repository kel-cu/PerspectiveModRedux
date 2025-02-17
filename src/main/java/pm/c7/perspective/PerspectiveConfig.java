package pm.c7.perspective;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.gui.ConfigScreenProvider;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;

@Config(name = "perspectivemod")
public class PerspectiveConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("main")
    @ConfigEntry.Gui.TransitiveObject
    public CategoryMain main = new CategoryMain();

    @Config(name = "main")
    public static class CategoryMain implements ConfigData {
        public boolean holdMode = false;
    }

    // https://github.com/shedaniel/RoughlyEnoughItems/blob/3.x/src/main/java/me/shedaniel/rei/impl/ConfigManagerImpl.java
    // using this just to change localization strings apposed to using it for an extra button like blanket does
    @SuppressWarnings("deprecation")
    public static Screen getConfigScreen(Screen parent) {
        try {
            ConfigScreenProvider<PerspectiveConfig> provider = (ConfigScreenProvider<PerspectiveConfig>) AutoConfig.getConfigScreen(PerspectiveConfig.class, parent);
            provider.setI13nFunction(manager -> "config.perspectivemod");
            provider.setOptionFunction((baseI13n, field) -> String.format("%s.%s", baseI13n, field.getName()));
            provider.setCategoryFunction((baseI13n, categoryName) -> String.format("%s.%s", baseI13n, categoryName));

            return provider.get();
        } catch (Exception e) {
            e.printStackTrace();
            ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
            toastManager.add(new SystemToast(SystemToast.Type.PACK_LOAD_FAILURE, Text.literal("Error loading screen"), Text.literal("Check console for details.")));
        }

        return new Screen(Text.literal("")) {
            @Override
            protected void init() {
                this.client.setScreen(parent);
            }
        };
    }
}
