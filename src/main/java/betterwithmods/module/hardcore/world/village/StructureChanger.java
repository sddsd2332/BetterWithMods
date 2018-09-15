package betterwithmods.module.hardcore.world.village;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.function.BiPredicate;

public class StructureChanger {


    public static IBlockState getConversion(Set<StructureChanger> changers, World world, BlockPos pos, IBlockState state) {
        for (StructureChanger changer : changers) {
            if (changer.canConvert(world, pos)) {
                return changer.getConversion(world, pos, state);
            }
        }
        return null;
    }

    private BiPredicate<World, BlockPos> predicate;

    public static StructureChanger create(Set<StructureChanger> set, BiPredicate<World, BlockPos> predicate) {
        StructureChanger c = new StructureChanger(predicate);
        set.add(c);
        return c;
    }

    private Set<IChanger> changers = Sets.newHashSet();

    private StructureChanger(BiPredicate<World, BlockPos> predicate) {
        this.predicate = predicate;
    }

    public StructureChanger addChanger(IChanger changer) {
        this.changers.add(changer);
        return this;
    }

    private boolean canConvert(World world, BlockPos pos) {
        return predicate.test(world, pos);
    }

    public IBlockState getConversion(World world, BlockPos pos, IBlockState state) {
        for (IChanger changer : changers) {
            if (changer.canChange(world, pos, state)) {
                IBlockState newState = changer.change(world, pos, state);
                if (newState != null) {
                    return newState;
                }
            }
        }
        return null;
    }


}
