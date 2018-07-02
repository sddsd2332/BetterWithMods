package betterwithmods.module.hardcore.world.saplings;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.module.Feature;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class HCSapling extends Feature {

    public static final Map<BlockPlanks.EnumType, Block> SAPLING_CROPS = Maps.newHashMap();

    public static IBlockState getSapling(BlockPlanks.EnumType type) {
        return Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, type);
    }

    @Override
    public String getFeatureDescription() {
        return "Change saplings to grow in stages before becoming a tree";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            Block crop = new BlockSaplingCrop(getSapling(type)).setRegistryName(BWMod.MODID, String.format("sapling_crop_%s", type.getName()));
            BWMBlocks.registerBlock(crop, null);
            SAPLING_CROPS.put(type, crop);
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent event) {
        if (event.getPlayer() != null && event.getPlacedBlock().getBlock() instanceof BlockSapling) {
            IBlockState state = event.getPlacedBlock();

            BlockPlanks.EnumType type = state.getValue(BlockSapling.TYPE);
            Block crop = SAPLING_CROPS.get(type);
            if (crop != null && event.getBlockSnapshot().getReplacedBlock().getBlock() == Blocks.AIR) {
                event.getWorld().setBlockState(event.getPos(), crop.getDefaultState());
            }
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
