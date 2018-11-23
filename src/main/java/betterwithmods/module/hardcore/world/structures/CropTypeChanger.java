package betterwithmods.module.hardcore.world.structures;

import betterwithmods.library.utils.ListUtils;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CropTypeChanger implements IChanger {

    private static HashMap<Vec3i, Block> CROPS = Maps.newHashMap();

    private final List<Block> crops;

    public CropTypeChanger(List<Block> crops) {
        this.crops = crops;
    }


    @Override
    public boolean canChangeState(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState original) {
        return original.getBlock() instanceof IGrowable;
    }

    private static StructureVillagePieces.Start getStart(StructureVillagePieces.Village piece) {
        return ReflectionHelper.getPrivateValue(StructureVillagePieces.Village.class, piece, "startPiece");
    }

    private Block pickRandomCrop() {
        return ListUtils.getRandomItem(crops);
    }

    private IBlockState getVillageCrop(StructureComponent component) {
        if (component instanceof StructureVillagePieces.Village) {
            StructureVillagePieces.Start start = getStart((StructureVillagePieces.Village) component);
            StructureBoundingBox box = start.getBoundingBox();
            //Use this vec as the bb is not hashable. Really hacky lol
            Vec3i vec = new Vec3i(box.minX, 64, box.minZ);
            if (!CROPS.containsKey(vec)) {
                CROPS.put(vec, pickRandomCrop());
            }
            return randomAge(CROPS.get(vec));
        }
        return Blocks.AIR.getDefaultState();
    }

    private static final Random RANDOM = new Random();

    private static IBlockState randomAge(Block crop) {
        if (crop instanceof BlockCrops) {
            int max = ((BlockCrops) crop).getMaxAge();
            //TODO META
            return crop.getStateFromMeta(MathHelper.getInt(RANDOM, max / 3, max));
        }
        return crop.getDefaultState();
    }

    @Override
    public IBlockState changeState(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState original) {
        return getVillageCrop(structure);
    }
}
