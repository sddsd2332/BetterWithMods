package betterwithmods.module.tweaks;

import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class DarkQuartz extends Feature {

    @Override
    public String getDescription() {
        return "Makes Nether Quartz a dark material to give it a more hellish feel, as well as make it more unique from Whitestone";
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        //TODO
        config().overrideBlockResource("quartz_ore");
        config().overrideBlockResource("quartz_block_bottom");
        config().overrideBlockResource("quartz_block_top");
        config().overrideBlockResource("quartz_block_side");
        config().overrideBlockResource("quartz_block_chiseled_top");
        config().overrideBlockResource("quartz_block_chiseled");
        config().overrideBlockResource("quartz_block_lines_top");
        config().overrideBlockResource("quartz_block_lines");
        config().overrideItemResource("quartz");
    }

}
