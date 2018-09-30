package betterwithmods.module.hardcore.world.saplings;

import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.ingredient.blockstate.BlockDropIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.lib.ModLib;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.module.internal.BlockRegistry;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class HCSapling extends Feature {

    public static List<SaplingConversion> SAPLING_CONVERSIONS = Lists.newArrayList();

    public static IBlockState getSapling(BlockPlanks.EnumType type) {
        return Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, type);
    }

    @Override
    public String getDescription() {
        return "Change saplings to grow in stages before becoming a tree";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            IBlockState sapling = getSapling(type);
            Block crop = new BlockSaplingCrop(sapling).setRegistryName(ModLib.MODID, String.format("sapling_crop_%s", type.getName()));
            BlockRegistry.registerBlock(crop, null);
            SAPLING_CONVERSIONS.add(new SaplingConversion(new BlockDropIngredient(GlobalUtils.getStackFromState(sapling)), crop));
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.PlaceEvent event) {
        if (event.getPlacedBlock().getBlock() instanceof BlockSapling) {
            IBlockState state = event.getPlacedBlock();
            IBlockState replaced = event.getBlockSnapshot().getReplacedBlock();
            if(replaced.getBlock().isReplaceable(event.getWorld(), event.getBlockSnapshot().getPos())) {
                for (SaplingConversion conversion : SAPLING_CONVERSIONS) {
                    if (conversion.ingredient.apply(event.getWorld(), event.getPos(), state)) {
                        event.getWorld().setBlockState(event.getPos(), conversion.getReplacement().getDefaultState());
                    }
                }
            }
        }
    }

    public class SaplingConversion {
        private final BlockStateIngredient ingredient;
        private final Block replacement;

        public SaplingConversion(BlockStateIngredient ingredient, Block replacement) {
            this.ingredient = ingredient;
            this.replacement = replacement;
        }

        public BlockStateIngredient getIngredient() {
            return ingredient;
        }

        public Block getReplacement() {
            return replacement;
        }
    }
}
