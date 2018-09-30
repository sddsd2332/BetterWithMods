package betterwithmods.proxy;

import betterwithmods.lib.ModLib;
import betterwithmods.library.modularity.impl.proxy.ClientProxy;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BWMClientProxy extends ClientProxy {

    @SideOnly(Side.CLIENT)
    public static final BannerTextures.Cache WINDMILLS = new BannerTextures.Cache("betterwithmods:W", new ResourceLocation(ModLib.MODID, "textures/blocks/windmills/banner.png"), "betterwithmods:textures/blocks/windmills/");


}
