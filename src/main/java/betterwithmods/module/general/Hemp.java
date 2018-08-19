package betterwithmods.module.general;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockHemp;
import betterwithmods.module.Feature;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class Hemp extends Feature {

    @Override
    public void onInit(FMLInitializationEvent event) {
        boolean addSeed = loadProperty("Drop Hemp Seed from grass", true).setCategoryComment(getDescription()).get();
        if (addSeed) {
            MinecraftForge.addGrassSeed(new ItemStack(BWMBlocks.HEMP, 1), 5);
        }
        BlockHemp.growthChance = loadProperty("Growth Chance", 15D).setComment("Hemp has a 1/X chance of growing where X is this value, the following modifiers divide this value").get();
        BlockHemp.fertileModifier = loadProperty("Fertile Modifer", 1.33d).setComment("Modifies Hemp Growth Chance when planted on Fertile Farmland").get();
        BlockHemp.lampModifier = loadProperty("Light Block Modifier", 1.5d).setComment("Modifies Hemp Growth Chance when a Light Block is two blocks above the Hemp").get();
        BlockHemp.neighborModifier = loadProperty("Neighbor Modifier", 1.1d).setComment("Modifies Hemp Growth Chance for each other crop next to it").get();
    }

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Control how hemp is obtained or grows";
    }


}
