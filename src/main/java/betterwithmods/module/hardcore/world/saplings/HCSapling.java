package betterwithmods.module.hardcore.world.saplings;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.creation.BlockEntryBuilderFactory;
import betterwithmods.library.common.block.creation.BlockEntryBuilderGenerator;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.ingredient.blockstate.BlockDropIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.module.internal.BlockRegistry;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.function.Function;

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
       BlockRegistry.registerBlocks(BlockEntryBuilderFactory.<BlockPlanks.EnumType>create().blockGenerator(new Generator()).complete());
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.PlaceEvent event) {
        if (event.getPlacedBlock().getBlock() instanceof BlockSapling) {
            IBlockState state = event.getPlacedBlock();
            IBlockState replaced = event.getBlockSnapshot().getReplacedBlock();
            if (replaced.getBlock().isReplaceable(event.getWorld(), event.getBlockSnapshot().getPos())) {
                for (SaplingConversion conversion : SAPLING_CONVERSIONS) {
                    if (conversion.ingredient.apply(event.getWorld(), event.getPos(), state)) {
                        event.getWorld().setBlockState(event.getPos(), conversion.getReplacement().getDefaultState());
                    }
                }
            }
        }
    }

    public static class SaplingConversion {
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

    public static class Generator extends BlockEntryBuilderGenerator<BlockPlanks.EnumType> {

        public Generator() {
            super(Lists.newArrayList(BlockPlanks.EnumType.values()));
        }

        @Override
        public Block createBlock(BlockPlanks.EnumType variant) {
            IBlockState sapling = getSapling(variant);
            Block crop = new BlockSaplingCrop(sapling);
            SAPLING_CONVERSIONS.add(new SaplingConversion(new BlockDropIngredient(GlobalUtils.getStackFromState(sapling)), crop));
            return crop;
        }

        @Override
        public Function<Block, ItemBlock> itemBlock(BlockPlanks.EnumType variant) {
            return b -> null;
        }

        @Override
        public ResourceLocation id(BlockPlanks.EnumType variant) {
            return new ResourceLocation(ModLib.MODID, String.format("sapling_crop_%s", variant.getName()));
        }
    }
}
