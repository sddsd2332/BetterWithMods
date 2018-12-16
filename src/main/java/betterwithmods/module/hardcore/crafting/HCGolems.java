package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.entity.EntityUrn;
import betterwithmods.library.common.event.block.SpawnGolemEvent;
import betterwithmods.library.common.modularity.impl.Feature;
import com.google.common.base.Predicate;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class HCGolems extends Feature {

    @Override
    public String getDescription() {
        return "Change the spawn of Golems and the Wither Boss";
    }


    @SubscribeEvent
    public void onSpawnGolem(SpawnGolemEvent event) {
        event.setCanceled(true);
    }

    @Override
    public boolean hasEvent() {
        return true;
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        BlockPattern wither = FactoryBlockPattern.start().aisle("^^^", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND))).where('^', IS_WITHER_SKELETON).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        BlockPattern snowGolem = FactoryBlockPattern.start().aisle("^", "#", "#").where('^', BlockWorldState.hasState(IS_PUMPKIN)).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW))).build();
        BlockPattern ironGolem = FactoryBlockPattern.start().aisle("~^~", "###", "~#~").where('^', BlockWorldState.hasState(IS_PUMPKIN)).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();

        EntityUrn.addHandler((world, pos, random) -> formWitherBoss(world, pos, random, wither));
        EntityUrn.addHandler((world, pos, random) -> formGolem(world, pos, random, snowGolem, HCGolems::spawnSnowGolem));
        EntityUrn.addHandler((world, pos, random) -> formGolem(world, pos, random, ironGolem, HCGolems::spawnIronGolem));
    }


    public static boolean formWitherBoss(World world, BlockPos pos, Random random, BlockPattern pattern) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntitySkull) {
            if (((TileEntitySkull) tile).getSkullType() == 1 && pos.getY() >= 2 && world.getDifficulty() != EnumDifficulty.PEACEFUL && !world.isRemote) {
                formGolem(world, pos, random, pattern, HCGolems::spawnWither);
                return true;
            }
        }
        return false;
    }

    public static boolean formGolem(World world, BlockPos pos, Random random, BlockPattern pattern, GolemSpawner spawner) {
        BlockPattern.PatternHelper match = pattern.match(world, pos);
        if (match != null) {
            spawner.onMatch(world, pos, random, pattern, match);
            return true;
        }
        return false;
    }

    @FunctionalInterface
    private interface GolemSpawner {
        void onMatch(World world, BlockPos pos, Random random, BlockPattern pattern, BlockPattern.PatternHelper helper);
    }


    private static final Predicate<IBlockState> IS_PUMPKIN = state -> state != null && (state.getBlock() == Blocks.PUMPKIN || state.getBlock() == Blocks.LIT_PUMPKIN);
    private static final Predicate<BlockWorldState> IS_WITHER_SKELETON = state -> state.getBlockState() != null && state.getBlockState().getBlock() == Blocks.SKULL && state.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull) state.getTileEntity()).getSkullType() == 1;


    private static void spawnSnowGolem(World world, BlockPos pos, Random random, BlockPattern pattern, BlockPattern.PatternHelper match) {
        for (int i = 0; i < pattern.getThumbLength(); ++i) {
            BlockWorldState blockworldstate = match.translateOffset(0, i, 0);
            world.setBlockState(blockworldstate.getPos(), Blocks.AIR.getDefaultState(), 2);
        }

        EntitySnowman entitysnowman = new EntitySnowman(world);
        BlockPos blockpos1 = match.translateOffset(0, 2, 0).getPos();
        entitysnowman.setLocationAndAngles((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.05D, (double) blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
        world.spawnEntity(entitysnowman);

        for (EntityPlayerMP entityplayermp : world.getEntitiesWithinAABB(EntityPlayerMP.class, entitysnowman.getEntityBoundingBox().grow(5.0D))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitysnowman);
        }

        for (int l = 0; l < 120; ++l) {
            world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double) blockpos1.getX() + world.rand.nextDouble(), (double) blockpos1.getY() + world.rand.nextDouble() * 2.5D, (double) blockpos1.getZ() + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        }

        for (int i1 = 0; i1 < pattern.getThumbLength(); ++i1) {
            BlockWorldState blockworldstate2 = match.translateOffset(0, i1, 0);
            world.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR, false);
        }
    }


    private static void spawnIronGolem(World world, BlockPos pos, Random random, BlockPattern pattern, BlockPattern.PatternHelper match) {

        for (int j = 0; j < pattern.getPalmLength(); ++j) {
            for (int k = 0; k < pattern.getThumbLength(); ++k) {
                world.setBlockState(match.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
            }
        }

        BlockPos blockpos = match.translateOffset(1, 2, 0).getPos();
        EntityIronGolem entityirongolem = new EntityIronGolem(world);
        entityirongolem.setPlayerCreated(true);
        entityirongolem.setLocationAndAngles((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.05D, (double) blockpos.getZ() + 0.5D, 0.0F, 0.0F);
        world.spawnEntity(entityirongolem);

        for (EntityPlayerMP entityplayermp1 : world.getEntitiesWithinAABB(EntityPlayerMP.class, entityirongolem.getEntityBoundingBox().grow(5.0D))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp1, entityirongolem);
        }

        for (int j1 = 0; j1 < 120; ++j1) {
            world.spawnParticle(EnumParticleTypes.SNOWBALL, (double) blockpos.getX() + world.rand.nextDouble(), (double) blockpos.getY() + world.rand.nextDouble() * 3.9D, (double) blockpos.getZ() + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        }

        for (int k1 = 0; k1 < pattern.getPalmLength(); ++k1) {
            for (int l1 = 0; l1 < pattern.getThumbLength(); ++l1) {
                BlockWorldState blockworldstate1 = match.translateOffset(k1, l1, 0);
                world.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
            }
        }

    }


    private static void spawnWither(World world, BlockPos pos, Random random, BlockPattern pattern, BlockPattern.PatternHelper match) {
        for (int i = 0; i < 3; ++i) {
            BlockWorldState blockworldstate = match.translateOffset(i, 0, 0);
            world.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().withProperty(BlockSkull.NODROP, Boolean.valueOf(true)), 2);
        }

        for (int j = 0; j < pattern.getPalmLength(); ++j) {
            for (int k = 0; k < pattern.getThumbLength(); ++k) {
                BlockWorldState blockworldstate1 = match.translateOffset(j, k, 0);
                world.setBlockState(blockworldstate1.getPos(), Blocks.AIR.getDefaultState(), 2);
            }
        }

        BlockPos blockpos = match.translateOffset(1, 0, 0).getPos();
        EntityWither entitywither = new EntityWither(world);
        BlockPos blockpos1 = match.translateOffset(1, 2, 0).getPos();
        entitywither.setLocationAndAngles((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.55D, (double) blockpos1.getZ() + 0.5D, match.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
        entitywither.renderYawOffset = match.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
        entitywither.ignite();

        for (EntityPlayerMP entityplayermp : world.getEntitiesWithinAABB(EntityPlayerMP.class, entitywither.getEntityBoundingBox().grow(50.0D))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitywither);
        }

        world.spawnEntity(entitywither);

        for (int l = 0; l < 120; ++l) {
            world.spawnParticle(EnumParticleTypes.SNOWBALL, (double) blockpos.getX() + world.rand.nextDouble(), (double) (blockpos.getY() - 2) + world.rand.nextDouble() * 3.9D, (double) blockpos.getZ() + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        }

        for (int i1 = 0; i1 < pattern.getPalmLength(); ++i1) {
            for (int j1 = 0; j1 < pattern.getThumbLength(); ++j1) {
                BlockWorldState blockworldstate2 = match.translateOffset(i1, j1, 0);
                world.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR, false);
            }
        }
    }
}

