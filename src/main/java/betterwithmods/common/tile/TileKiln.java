package betterwithmods.common.tile;

import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.registry.block.recipe.KilnRecipe;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TileKiln extends TileDynamic implements ITickable {

    private int cookTicks;
    private int prevProgress;
    private int cookSpeed;

    @Override
    public void update() {

        if (!KilnStructureManager.isValidKiln(world, pos)) {
            world.setBlockState(pos, this.state);
            return;
        }

        BlockPos cookPos = pos.up();
        IBlockState cookState = world.getBlockState(cookPos);
        KilnRecipe recipe = RecipeRegistry.KILN.findRecipe(world, cookPos, cookState).orElse(null);
        if (recipe != null) {
            int progress = (int) ((((double) cookTicks) / ((double) recipe.getCookTime())) * 10);
            if (prevProgress != progress) {
                prevProgress = progress;
                world.sendBlockBreakProgress(0, cookPos, prevProgress);
                cookSpeed = calculateSpeed();
            }
            if (recipe.craftRecipe(world, cookPos, world.rand, cookState)) {
                world.sendBlockBreakProgress(0, cookPos, -1);
                cookTicks = 0;
                prevProgress = -1;
            } else {
                cookTicks += getCookSpeed();
            }
        } else {
            cookTicks = 0;
            prevProgress = -1;
        }

    }

    public int getCookTicks() {
        return cookTicks;
    }

    public int getCookSpeed() {
        return cookSpeed;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        compound.setInteger("cookTicks", cookTicks);
        compound.setInteger("prevProgress", prevProgress);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        cookTicks = compound.getInteger("cookTicks");
        prevProgress = compound.getInteger("prevProgress");
        super.readFromNBT(compound);
    }

    private int calculateSpeed() {
        int speed = 0;
        int centerFire = KilnStructureManager.getKiln().getHeat(world, pos);

        for (int xP = -1; xP < 2; xP++) {
            for (int zP = -1; zP < 2; zP++) {
                BlockPos bPos = pos.add(xP, 0, zP);
                int currentFire = KilnStructureManager.getKiln().getHeat(world, bPos);
                if (currentFire == centerFire)
                    speed += currentFire;
            }
        }
        return speed / centerFire;
    }


}