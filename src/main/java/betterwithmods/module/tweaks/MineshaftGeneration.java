package betterwithmods.module.tweaks;

import betterwithmods.library.common.event.structure.StructureSetBlockEvent;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.library.utils.ingredient.collections.BlockStateIngredientSet;
import betterwithmods.module.hardcore.world.structures.IngredientChanger;
import betterwithmods.module.hardcore.world.structures.StructureChanger;
import com.google.common.collect.Sets;
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
    public String getDescription() {
        return "Mineshafts now generate with logs instead of fences";
    }


    @Override
    public void onInit(FMLInitializationEvent event) {
        BlockStateIngredient fence = new BlockStateIngredientSet(Blocks.OAK_FENCE, Blocks.DARK_OAK_FENCE);
        MINESHAFT_CHANGER.addChanger(new MineshaftIngredientChanger(fence, Blocks.LOG.getDefaultState(), MapGenMineshaft.Type.NORMAL));
        MINESHAFT_CHANGER.addChanger(new MineshaftIngredientChanger(fence, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK), MapGenMineshaft.Type.MESA));
    }


    @SubscribeEvent
    public void onStructureSetBlock(StructureSetBlockEvent event) {
        if (event.getComponent() instanceof StructureMineshaftPieces.Peice) {
            System.out.printf("/tp %s ~ %s\n", event.getPos().getX(), event.getPos().getZ());
            StructureChanger.convert(MINESHAFT, event);
        }
    }


    public static class MineshaftIngredientChanger extends IngredientChanger {
        protected MapGenMineshaft.Type type;

        public MineshaftIngredientChanger(BlockStateIngredient ingredient, IBlockState state, MapGenMineshaft.Type type) {
            super(ingredient, state);
            this.type = type;
        }

        @Override
        public boolean canChangeState(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState original) {
            return (structure instanceof StructureMineshaftPieces.Peice && ((StructureMineshaftPieces.Peice) structure).mineShaftType == this.type) && super.canChangeState(structure, world, pos, relativePos, original);
        }
    }

    @Override
    public boolean hasTerrainGen() {
        return true;
    }
}
