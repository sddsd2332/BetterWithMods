package betterwithmods.common.blocks.mechanical.mech_machine;

import betterwithmods.BWMod;
import betterwithmods.common.tile.TileMill;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockMillstone extends BlockMechMachine {
    public static final ResourceLocation MILLSTONE = LootTableList.register(new ResourceLocation(BWMod.MODID, "block/mill"));


    public BlockMillstone() {
        super(Material.ROCK, MILLSTONE);
    }

    @Override
    public int tickRate(World worldIn) {
        return 1;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileMill();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (isActive(state)) {
            emitSmoke(world, pos, rand);
        }
    }

    private void emitSmoke(World world, BlockPos pos, Random rand) {
        for (int i = 0; i < 5; i++) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            float fX = x + rand.nextFloat();
            float fY = y + rand.nextFloat() * 0.5F + 1.0F;
            float fZ = z + rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fX, fY, fZ, 0.0D, 0.0D, 0.0D);
        }
    }
}
