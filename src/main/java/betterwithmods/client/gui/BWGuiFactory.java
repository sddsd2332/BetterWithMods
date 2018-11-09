package betterwithmods.client.gui;

import betterwithmods.library.client.gui.config.GuiConfigBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

/**
 * Created by primetoxinz on 9/14/16.
 */
public class BWGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
        // NO-OP
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
       return new GuiConfigBase("bwm.config.title"); //TODO return new BWGuiConfig(parentScreen);
    }


    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

//TODO CONFIG SCREEN
//    public static class BWGuiConfig extends GuiConfig {
//
//        public BWGuiConfig(GuiScreen parentScreen) {
//            super(parentScreen, getAllElements(), BetterWithMods.MODID, false, false, GuiConfig.getAbridgedConfigPath(BetterWithMods.MODULE_LOADER.configHelper.config.toString()));
//        }
//
//        public static List<IConfigElement> getAllElements() {
//            List<IConfigElement> list = new ArrayList<>();
//
//            Set<String> categories = BetterWithMods.MODULE_LOADER.configHelper.config.getCategoryNames();
//            for (String s : categories)
//                if (!s.contains("."))
//                    list.add(new DummyConfigElement.DummyCategoryElement(s, s, new ConfigElement(BetterWithMods.MODULE_LOADER.configHelper.config.getCategory(s)).getChildElements()));
//
//            return list;
//        }
//
//    }

}
