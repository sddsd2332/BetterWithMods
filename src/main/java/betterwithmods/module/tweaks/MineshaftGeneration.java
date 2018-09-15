package betterwithmods.module.tweaks;

import betterwithmods.bwl.event.StructureSetBlockEvent;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.common.registry.block.recipe.BlockIngredientSpecial;
import betterwithmods.module.Feature;
import betterwithmods.module.hardcore.world.structures.IngredientChanger;
import betterwithmods.module.hardcore.world.structures.StructureChanger;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

public class MineshaftGeneration extends Feature {
    public static Set<StructureChanger> MINESHAFT = Sets.newHashSet();
    public static StructureChanger MINESHAFT_CHANGER = StructureChanger.create(MINESHAFT, (w, p) -> true);

    @Override
    public String getFeatureDescription() {
        return "Mineshafts now generate with logs instead of fences";
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public boolean hasTerrainSubscriptions() {
        return true;
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        BlockIngredient fence = new BlockIngredientSpecial((world, blockPos) -> world.getBlockState(blockPos).getBlock() instanceof BlockFence);
        MINESHAFT_CHANGER.addChanger(new MineshaftIngredientChanger(fence, Blocks.LOG.getDefaultState(), MapGenMineshaft.Type.MESA));
        MINESHAFT_CHANGER.addChanger(new MineshaftIngredientChanger(fence, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK), MapGenMineshaft.Type.MESA));
    }


    @SubscribeEvent
    public void onStructureSetBlock(StructureSetBlockEvent event) {
        if (event.getComponent() instanceof StructureMineshaftPieces.Peice) {
            StructureChanger.convert(MINESHAFT, event);
        }
    }

    public static class MineshaftIngredientChanger extends IngredientChanger {
        private MapGenMineshaft.Type type;

        public MineshaftIngredientChanger(BlockIngredient ingredient, IBlockState state, MapGenMineshaft.Type type) {
            super(ingredient, state);
            this.type = type;
        }

        @Override
        public boolean canChange(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState original) {
            return (structure instanceof StructureMineshaftPieces.Peice && ((StructureMineshaftPieces.Peice) structure).mineShaftType == this.type) && super.canChange(structure, world, pos, relativePos, original);
        }
    }
}
