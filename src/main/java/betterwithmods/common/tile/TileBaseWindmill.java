package betterwithmods.common.tile;

import betterwithmods.api.tile.IBannerInfo;
import betterwithmods.library.utils.BannerUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.DimensionType;

import javax.annotation.Nonnull;

public abstract class TileBaseWindmill extends TileAxleGenerator implements IBannerInfo {

    protected int bannerIndex;
    protected BannerUtils.BannerData[] banners;
    private int blades;

    public TileBaseWindmill(int blades) {
        this.blades = blades;
        this.banners = new BannerUtils.BannerData[this.blades];
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        bannerIndex = tag.getInteger("BannerIndex");
        BannerUtils.readArray(banners, tag);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        tag.setInteger("BannerIndex", bannerIndex);
        BannerUtils.writeArray(banners, t);
        return t;
    }

    @Override
    public void calculatePower() {
        byte power = 0;
        if (isValid()) {
            if (world.provider.getDimensionType() == DimensionType.OVERWORLD) {
                if (world.isThundering())
                    power = 3;
                else if (world.isRaining())
                    power = 2;
                else
                    power = 1;
            } else {
                power = 1;
            }
        }

        if (power != this.power) {
            setPower(power);
        }
    }


    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getSelected() {
        return bannerIndex;
    }

    @Override
    public void next() {
        bannerIndex = (bannerIndex + 1) % blades;
    }


    @Override
    public void setBannerData(int selected, BannerUtils.BannerData data) {
        banners[selected] = data;
        markDirty();
    }

    @Override
    public BannerUtils.BannerData[] getData() {
        return banners;
    }


}


