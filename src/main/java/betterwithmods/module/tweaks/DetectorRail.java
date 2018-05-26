package betterwithmods.module.tweaks;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.blocks.BlockRailDetectorBase;
import betterwithmods.module.Feature;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Objects;

import static betterwithmods.common.BWMBlocks.registerBlock;

public class DetectorRail extends Feature {

    public static final Block DETECTOR_RAIL_STONE = new BlockRailDetectorBase(cart -> !(cart instanceof EntityMinecartEmpty) || BlockRailDetectorBase.isRider(cart, Objects::nonNull)).setRegistryName("detector_rail_stone");



    @Override
    public String getFeatureDescription() {
        return "Change what detector rails detect; Wooden:all minecarts; Stone: carts containing something, SFS: carts with players.";
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        overrideBlock("rail_detector");
        overrideBlock("rail_detector_powered");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registerBlock(DETECTOR_RAIL_STONE);
        Blocks.DETECTOR_RAIL.setUnlocalizedName("detector_rail_wood");

        BWMRecipes.removeRecipe(Blocks.DETECTOR_RAIL.getRegistryName());
    }
}
