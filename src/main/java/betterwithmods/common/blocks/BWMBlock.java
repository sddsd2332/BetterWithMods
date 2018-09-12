package betterwithmods.common.blocks;

import betterwithmods.BWMod;
import betterwithmods.client.BWParticleDigging;
import betterwithmods.client.baking.IStateParticleBakedModel;
import betterwithmods.common.tile.TileBasic;
import betterwithmods.network.BWNetwork;
import betterwithmods.network.messages.MessageCustomDust;
import betterwithmods.util.CapabilityUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.item.ToolsManager;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BWMBlock extends Block implements IRotate {

    public BWMBlock(Material material) {
        super(material);

        if (material == Material.WOOD) {
            ToolsManager.setAxesAsEffectiveAgainst(this);
            this.setSoundType(SoundType.WOOD);
            this.setHarvestLevel("axe", 0);
            this.setHardness(3.5f);
        } else if (material == Material.ROCK) {
            this.setSoundType(SoundType.STONE);
            setHarvestLevel("pickaxe", 1);
            ToolsManager.setPickaxesAsEffectiveAgainst(this);
        }
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileBasic) {
                ((TileBasic) tile).onBreak();
            }
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        onBlockPlacedBy(world, pos, state, placer, stack, null, 0.5f, 0.5f, 0.5f);
    }

    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, @Nullable EnumFacing face, float hitX, float hitY, float hitZ) {
        if (hasTileEntity(state)) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileBasic) {
                ((TileBasic) tile).onPlacedBy(placer, face, stack, hitX, hitY, hitZ);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(IBlockState state) {
        return hasTileEntity(state);
    }


    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        if (hasTileEntity(blockState)) {
            TileEntity tile = worldIn.getTileEntity(pos);
            return CapabilityUtils.getInventory(tile, EnumFacing.UP).map(InvUtils::calculateComparatorLevel).orElse(0);
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        IBlockState state = world.getBlockState(pos);
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model instanceof IStateParticleBakedModel) {
            state = getExtendedState(state.getActualState(world, pos), world, pos);
            TextureAtlasSprite sprite = ((IStateParticleBakedModel) model).getParticleTexture(state, null);
            if (sprite != null) {
                for (int j = 0; j < 4; ++j) {
                    for (int k = 0; k < 4; ++k) {
                        for (int l = 0; l < 4; ++l) {
                            double d0 = ((double) j + 0.5D) / 4.0D;
                            double d1 = ((double) k + 0.5D) / 4.0D;
                            double d2 = ((double) l + 0.5D) / 4.0D;
                            manager.addEffect(new BWParticleDigging(world, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, state, pos, sprite, getParticleTintIndex()));
                        }
                    }
                }

                return true;
            }
        }

        return super.addDestroyEffects(world, pos, manager);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager) {
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model instanceof IStateParticleBakedModel) {
            BlockPos pos = target.getBlockPos();
            EnumFacing side = target.sideHit;

            state = getExtendedState(state.getActualState(world, pos), world, pos);
            TextureAtlasSprite sprite = ((IStateParticleBakedModel) model).getParticleTexture(state, side);
            if (sprite != null) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
                double d0 = (double) i + RANDOM.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
                double d1 = (double) j + RANDOM.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
                double d2 = (double) k + RANDOM.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;

                if (side == EnumFacing.DOWN) {
                    d1 = (double) j + axisalignedbb.minY - 0.10000000149011612D;
                }

                if (side == EnumFacing.UP) {
                    d1 = (double) j + axisalignedbb.maxY + 0.10000000149011612D;
                }

                if (side == EnumFacing.NORTH) {
                    d2 = (double) k + axisalignedbb.minZ - 0.10000000149011612D;
                }

                if (side == EnumFacing.SOUTH) {
                    d2 = (double) k + axisalignedbb.maxZ + 0.10000000149011612D;
                }

                if (side == EnumFacing.WEST) {
                    d0 = (double) i + axisalignedbb.minX - 0.10000000149011612D;
                }

                if (side == EnumFacing.EAST) {
                    d0 = (double) i + axisalignedbb.maxX + 0.10000000149011612D;
                }

                Particle particle = new BWParticleDigging(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state, pos, sprite, getParticleTintIndex())
                        .multiplyVelocity(0.2F)
                        .multipleParticleScaleBy(0.6F);
                manager.addEffect(particle);

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean addLandingEffects(IBlockState state, WorldServer world, BlockPos pos, IBlockState stateAgain, EntityLivingBase entity, int numberOfParticles) {
        MessageCustomDust packet = new MessageCustomDust(world, pos, entity.posX, entity.posY, entity.posZ, numberOfParticles, 0.15f);
        BWNetwork.sendToAllAround(packet, world, pos);
        return true;

    }

    @Override
    public boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity) {
        return BWMod.proxy.addRunningParticles(state, world, pos, entity);
    }

    public int getParticleTintIndex() {
        return -1;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        IRotate.super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return IRotate.super.onBlockActivated(this, worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
