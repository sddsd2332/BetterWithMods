package betterwithmods.module.hardcore.world.structures;

import betterwithmods.library.common.event.structure.StructureLootEvent;
import betterwithmods.library.common.event.structure.StructureSetBlockEvent;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.Set;
import java.util.function.BiPredicate;

public class StructureChanger {


    public Set<IChanger> changers = Sets.newHashSet();
    private BiPredicate<World, BlockPos> predicate;

    private StructureChanger(BiPredicate<World, BlockPos> predicate) {
        this.predicate = predicate;
    }

    public static IBlockState getBlockConversion(Set<StructureChanger> changers, StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState state) {
        for (StructureChanger changer : changers) {
            if (changer.canConvert(world, pos)) {
                return changer.getBlockConversion(structure, world, pos, relativePos, state);
            }
        }
        return null;
    }

    public static ResourceLocation getLootConversion(Set<StructureChanger> changers, StructureComponent structure, World world, BlockPos pos, ResourceLocation lootTable) {
        for (StructureChanger changer : changers) {
            if (changer.canConvert(world, pos)) {
                return changer.getLootConverstion(structure, world, pos, lootTable);
            }
        }
        return lootTable;
    }

    public static StructureChanger create(Set<StructureChanger> set, BiPredicate<World, BlockPos> predicate) {
        StructureChanger c = new StructureChanger(predicate);
        set.add(c);
        return c;
    }

    public static void convert(Set<StructureChanger> set, StructureSetBlockEvent event) {
        IBlockState state = getBlockConversion(set, event.getComponent(), event.getWorld(), event.getPos(), event.getRelativePos(), event.getState());
        if (state != null) {
            event.setState(state);
        }
    }

    public static void convert(Set<StructureChanger> set, StructureLootEvent event) {
        ResourceLocation loot = getLootConversion(set, event.getComponent(), event.getWorld(), event.getPos(), event.getLootTable());
        event.setLootTable(loot);
    }

    public StructureChanger addChanger(IChanger changer) {
        this.changers.add(changer);
        return this;
    }

    private boolean canConvert(World world, BlockPos pos) {
        return predicate.test(world, pos);
    }

    public IBlockState getBlockConversion(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState state) {
        for (IChanger changer : changers) {
            if (changer.canChangeState(structure, world, pos, relativePos, state)) {
                IBlockState newState = changer.changeState(structure, world, pos, relativePos, state);
                if (newState != null) {
                    return newState;
                }
            }
        }
        return null;
    }


    public ResourceLocation getLootConverstion(StructureComponent structure, World world, BlockPos pos, ResourceLocation lootTable) {
        for (IChanger changer : changers) {
            if (changer.canChangeLoot(structure, world, pos, lootTable)) {
                return changer.changeLootTable(structure, world, pos, lootTable);
            }
        }
        return lootTable;
    }


}
