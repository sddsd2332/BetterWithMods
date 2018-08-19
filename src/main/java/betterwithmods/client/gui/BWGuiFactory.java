package betterwithmods.client.gui;

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
       return null; //TODO return new BWGuiConfig(parentScreen);
    }


    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

//TODO CONFIG SCREEN
//    public static class BWGuiConfig extends GuiConfig {
//
//        public BWGuiConfig(GuiScreen parentScreen) {
//            super(parentScreen, getAllElements(), BWMod.MODID, false, false, GuiConfig.getAbridgedConfigPath(BWMod.MODULE_LOADER.configHelper.config.toString()));
//        }
//
//        public static List<IConfigElement> getAllElements() {
//            List<IConfigElement> list = new ArrayList<>();
//
//            Set<String> categories = BWMod.MODULE_LOADER.configHelper.config.getCategoryNames();
//            for (String s : categories)
//                if (!s.contains("."))
//                    list.add(new DummyConfigElement.DummyCategoryElement(s, s, new ConfigElement(BWMod.MODULE_LOADER.configHelper.config.getCategory(s)).getChildElements()));
//
//            return list;
//        }
//
//    }

}
