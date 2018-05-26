package betterwithmods.module.tweaks;

import betterwithmods.common.blocks.BlockRailDetectorBase;
import betterwithmods.module.Feature;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Objects;

import static betterwithmods.common.BWMBlocks.registerBlock;

public class DetectorRail extends Feature {

    public static final Block DETECTOR_RAIL_STONE = new BlockRailDetectorBase(cart -> !(cart instanceof EntityMinecartEmpty) || BlockRailDetectorBase.isRider(cart, Objects::nonNull)).setRegistryName("minecraft:detector_rail").setUnlocalizedName("detectorRail");

    @Override
    public String getFeatureDescription() {
        return "Change the Vanilla Detector Rail to only get triggered by carts containing something, be it a block or entity. This feature replaces the vanilla version of the block.";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registerBlock(DETECTOR_RAIL_STONE);
    }
}
