package betterwithmods.module.tweaks;

import betterwithmods.client.render.RenderLongboi;
import betterwithmods.common.blocks.BlockWolf;
import betterwithmods.common.entity.EntityLongboi;
import betterwithmods.library.common.block.creation.BlockEntryBuilderFactory;
import betterwithmods.library.utils.ingredient.EntityIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.common.registry.block.recipe.TurntableRecipe;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.module.internal.BlockRegistry;
import betterwithmods.module.internal.EntityRegistry;
import betterwithmods.library.utils.EntityUtils;
import betterwithmods.module.internal.RecipeRegistry;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import javax.annotation.Nullable;
import java.util.Optional;

public class LongBoi extends Feature {

    @Override
    public String getDescription() {
        return "Long Bois!";
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityLongboi.class, RenderLongboi::new);
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BlockRegistry.registerBlocks(BlockEntryBuilderFactory.<Void>create()
                .builder().block(new BlockWolf(new ResourceLocation(ModLib.MODID, "longboi"))).id("long_friend").build()
                .complete());

        EntityRegistry.registerEntity(
                EntityEntryBuilder.create()
                        .entity(EntityLongboi.class)
                        .name(EntityRegistry.entityName("longboi"))
                        .id(new ResourceLocation(ModLib.MODID, "longboi"), EntityRegistry.TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .egg(0xe4d3d0, 0xfd742b)
                        .build()
        );
        RecipeRegistry.TURNTABLE.addRecipe(new LongRecipe());
    }

    private static class LongRecipe extends TurntableRecipe {

        public LongRecipe() {
            super(new EntityIngredient(new ResourceLocation("wolf")), Lists.newArrayList(), Blocks.AIR.getDefaultState(), 8);
        }


        @Override
        public NonNullList<ItemStack> onCraft(World world, BlockPos pos) {

            Optional<EntityLivingBase> entity = EntityIngredient.getEntity(world, pos);
            if (entity.isPresent()) {
                EntityLongboi longboi = new EntityLongboi(world);
                EntityUtils.copyEntityInfo(entity.get(), longboi);
                world.spawnEntity(longboi);
                world.playSound(null, pos, SoundEvents.ENTITY_WOLF_HURT, SoundCategory.NEUTRAL, 0.75F, 1.0F);
                entity.get().setDead();
            }

            return super.onCraft(world, pos);
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        @Override
        public boolean isHidden() {
            return true;
        }
    }

}
