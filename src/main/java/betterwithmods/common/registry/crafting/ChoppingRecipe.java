package betterwithmods.common.registry.crafting;

import betterwithmods.BWMod;
import betterwithmods.api.util.IBlockVariants;
import betterwithmods.module.hardcore.crafting.HCLumber;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Set;

/**
 * Created by blueyu2 on 12/12/16.
 */
public class ChoppingRecipe extends ToolDamageRecipe {
    private final IBlockVariants wood;

    public ChoppingRecipe(IBlockVariants wood, int planks) {
        super(new ResourceLocation(BWMod.MODID, "chopping"), wood.getVariant(IBlockVariants.EnumBlock.BLOCK, planks), Ingredient.fromStacks(wood.getVariant(IBlockVariants.EnumBlock.LOG, 1)), ChoppingRecipe::isAxe);
        this.wood = wood;
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static boolean isAxe(ItemStack stack) {
        if (stack != null) {
            Item item = stack.getItem();
            Set<String> classes = item.getToolClasses(stack);
            if (classes.contains("axe") || classes.contains("mattock")) {
                ResourceLocation loc = item.getRegistryName();
                return loc == null || !loc.getResourceDomain().equals("tconstruct") || stack.getItemDamage() < stack.getMaxDamage();
            }
        }
        return false;
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD;
    }

    @Override
    public Pair<Float, Float> getSoundValues() {
        return Pair.of(0.25F, 2.5F);
    }

    @Override
    public ItemStack getExampleStack() {
        return new ItemStack(Items.IRON_AXE);
    }

    public boolean shouldDamage(ItemStack stack, EntityPlayer player, IBlockState state) {
        if (isAxe(stack)) {
            Item item = stack.getItem();
            int level = Math.max(item.getHarvestLevel(stack, "axe", player, state), item.getHarvestLevel(stack, "mattock", player, state));
            return level < 2;
        }
        return super.shouldDamage(stack, player, state);
    }

    @SubscribeEvent
    public void dropExtra(PlayerEvent.ItemCraftedEvent event) {
        if (event.player == null)
            return;
        if (isMatch(event.craftMatrix, event.player.world)) {
            if (!event.player.getEntityWorld().isRemote) {
                event.player.entityDropItem(wood.getVariant(IBlockVariants.EnumBlock.SAWDUST, HCLumber.axeSawDustAmount), 0);
                event.player.entityDropItem(wood.getVariant(IBlockVariants.EnumBlock.BARK, HCLumber.axeBarkAmount), 0);
            }
        }
    }
}

