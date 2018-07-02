package betterwithmods.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class BWStateMapper extends StateMapperBase {
    private final ResourceLocation registerName;

    public BWStateMapper(ResourceLocation registerName) {
        this.registerName = registerName;
    }

    @Nonnull
    @Override
    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
        return new ModelResourceLocation(registerName, this.getPropertyString(state.getProperties()));
    }
}
