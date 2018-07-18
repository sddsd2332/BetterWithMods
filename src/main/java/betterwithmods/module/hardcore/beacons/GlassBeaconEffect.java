package betterwithmods.module.hardcore.beacons;

import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.util.ColorUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GlassBeaconEffect extends BeaconEffect {

    public GlassBeaconEffect() {
        super(new BlockIngredient("blockGlass"), EntityLivingBase.class);
        this.setBaseBeamColor(Color.white);
    }

    @Override
    public NonNullList<EntityLivingBase> getEntitiesInRange(World world, BlockPos pos, int beaconLevel) {
        return NonNullList.create();
    }

    @Override
    public void onBeaconCreate(@Nonnull World world, @Nonnull BlockPos pos, int beaconLevel) {
        List<float[]> colors = Lists.newArrayList();

        float[] color = new float[]{1, 1, 1};
        for (int r = 1; r <= beaconLevel; r++) {
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos glassPos = new BlockPos(pos.getX() + x, pos.getY() - r, pos.getZ() + z);
                    colors.add(ColorUtils.getColorFromBlock(world, glassPos, pos));
                }
            }
        }

        color = ColorUtils.average(colors.toArray(new float[colors.size()][3]));


        this.setBaseBeamColor(color);
    }

    @Override
    public void apply(NonNullList<EntityLivingBase> entitiesInRange, @Nonnull World world, @Nonnull BlockPos pos, int beaconLevel) {

    }

    @Override
    public boolean onPlayerInteracted(World world, BlockPos pos, int level, EntityPlayer player, EnumHand hand, ItemStack stack) {
        return false;
    }

    @Override
    public void onBeaconBreak(World world, BlockPos pos, int level) {

    }
}
