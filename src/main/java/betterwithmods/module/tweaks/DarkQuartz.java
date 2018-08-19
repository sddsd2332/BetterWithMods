package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class DarkQuartz extends Feature {

    @Override
    public String getDescription() {
        return "Makes Nether Quartz a dark material to give it a more hellish feel, as well as make it more unique from Whitestone";
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        config().overrideBlock("quartz_ore");
        config().overrideBlock("quartz_block_bottom");
        config().overrideBlock("quartz_block_top");
        config().overrideBlock("quartz_block_side");
        config().overrideBlock("quartz_block_chiseled_top");
        config().overrideBlock("quartz_block_chiseled");
        config().overrideBlock("quartz_block_lines_top");
        config().overrideBlock("quartz_block_lines");
        config().overrideItem("quartz");
    }

}
