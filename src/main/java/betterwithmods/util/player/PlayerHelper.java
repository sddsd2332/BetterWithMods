package betterwithmods.util.player;

import betterwithmods.BWMod;
import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.registry.BrokenToolRegistry;
import betterwithmods.module.hardcore.needs.HCArmor;
import betterwithmods.module.hardcore.needs.hunger.HCHunger;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Set of methods dealing with EntityPlayer
 *
 * @author Koward
 */
public final class PlayerHelper {
    public final static UUID PENALTY_SPEED_UUID = UUID.fromString("c5595a67-9410-4fb2-826a-bcaf432c6a6f");

    private PlayerHelper() {

    }

    public static Entity getEntityById(int id) {
        World world = Minecraft.getMinecraft().world;
        if (world == null)
            return null;
        return world.getEntityByID(id);
    }

    public static EntityPlayer getPlayerById(String id) {
        World world = Minecraft.getMinecraft().world;
        if (world == null)
            return null;
        return world.getPlayerEntityByUUID(UUID.fromString(id));
    }


    public static boolean areHandsEmpty(EntityPlayer player) {
        return getHolding(player, EnumHand.MAIN_HAND).isEmpty() && getHolding(player, EnumHand.OFF_HAND).isEmpty();
    }

    public static ItemStack getHolding(EntityPlayer player, EnumHand hand) {
        if (hand != null) {
            return player.getHeldItem(hand);
        }
        return player.getHeldItem(EnumHand.MAIN_HAND);
    }

    public static Set<ItemStack> getHolding(@Nullable EntityPlayer player) {
        if (player == null)
            return Sets.newHashSet();
        return Sets.newHashSet(player.getHeldItemMainhand(), player.getHeldItemOffhand()).stream().filter(s -> !s.isEmpty()).collect(Collectors.toSet());
    }

    public static boolean isHolding(@Nullable EntityPlayer player, Ingredient ingredient) {
        if (player == null)
            return false;

        Set<ItemStack> held = getHolding(player);
        if (held.isEmpty())
            return false;
        for (ItemStack h : held)
            if (ingredient.apply(h))
                return true;
        return false;
    }

    public static boolean isSurvival(EntityPlayer player) {
        return player != null && !player.isCreative() && !player.isSpectator() && !player.isSpectator();
    }

    public static boolean isSitting(EntityPlayer player) {
        return player.getRidingEntity() != null && player.getRidingEntity().getName().equals("sit_mount");
    }

    /**
     * This will at least keep players from sticking to the bottom of a pool.
     *
     * @param player The swimming player.
     * @return Whether the player is near the bottom of the pool or not.
     */
    public static boolean isNearBottom(EntityPlayer player) {
        World world = player.getEntityWorld();
        BlockPos a = player.getPosition().down(1);
        BlockPos b = player.getPosition().down(2);
        return !world.getBlockState(a).getMaterial().isReplaceable() && !world.getBlockState(b).getMaterial().isReplaceable();
    }

    public static void changeAttack(EntityLivingBase entity,
                                    UUID attackModifierUUID, String name, double multiplier) {
        AttributeModifier attackModifier = (new AttributeModifier(
                attackModifierUUID, name, multiplier - 1, 2));
        IAttributeInstance iattributeinstance = entity
                .getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

        if (iattributeinstance.getModifier(attackModifierUUID) != null) {
            iattributeinstance.removeModifier(attackModifier);
        }
        iattributeinstance.applyModifier(attackModifier);
    }

    /**
     * Edit the speed of an entity.
     *
     * @param entity           The entity whose speed will be changed.
     * @param name             Unique name for easier debugging
     * @param modifier         The speed will be multiplied by this number
     * @param penaltySpeedUuid uuid for speed attribute
     */
    public static void changeSpeed(EntityLivingBase entity,
                                   String name, double modifier, UUID penaltySpeedUuid) {
        //2 operator multiples the current value by 1+x, thus modifier-1 neutralizes the extra 1
        AttributeModifier speedModifier = (new AttributeModifier(penaltySpeedUuid, name, modifier - 1, 2));
        IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (iattributeinstance.getModifier(penaltySpeedUuid) != null) {
            iattributeinstance.removeModifier(speedModifier);
        }
        iattributeinstance.applyModifier(speedModifier);
    }

    public static void removeModifier(EntityLivingBase entityLivingBase, IAttribute attribute, UUID uuid) {
        entityLivingBase.getEntityAttribute(attribute).removeModifier(uuid);
    }

    public static int getMinimumHunger() {
        return BWMod.MODULE_LOADER.isFeatureEnabled(HCHunger.class) ? 20 : 6;
    }

    public static int getWornArmorWeight(EntityPlayer player) {
        int weight = 0;
        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack != null)
                weight += HCArmor.getWeight(stack);
        }
        return weight;
    }

    public static boolean isCurrentToolEffectiveOnBlock(EntityPlayer player, BlockPos pos, IBlockState state) {
        ItemStack stack = BrokenToolRegistry.findItem(player, state);
        if (player == null || state == null)
            return false;
        return isCurrentToolEffectiveOnBlock(stack, state, Sets.newHashSet()) || ForgeHooks.isToolEffective(player.getEntityWorld(), pos, stack);
    }

    public static boolean isCurrentToolEffectiveOnBlock(ItemStack stack, IBlockState state, Set<Material> effective_materials) {

        if (stack == null) return false;
        if (stack.hasTagCompound()) {
            NBTTagCompound stats = stack.getSubCompound("Stats");
            if (stats != null) {
                return stats.getByte("Broken") != 1;
            }
        }

        ItemStack block = BWMRecipes.getStackFromState(state);

        Material material = state.getMaterial();
        if (effective_materials.contains(material))
            return true;
        for (String type : stack.getItem().getToolClasses(stack)) {
            if (Objects.equals(type, "mattock"))
                return state.getBlock().isToolEffective("shovel", state) || state.getBlock().isToolEffective("axe", state);
            if (Objects.equals(type, "bwmmattock")) {
                return state.getBlock().isToolEffective("shovel", state) || state.getBlock().isToolEffective("pickaxe", state);
            }
            if (state.getBlock().isToolEffective(type, state) || BWMOreDictionary.isToolForOre(type, block))
                return true;
        }
        return false;
    }

    public static ItemStack getPlayerHead(EntityPlayer player) {
        ItemStack head = new ItemStack(Items.SKULL, 1, 3);
        NBTTagCompound name = new NBTTagCompound();
        name.setString("SkullOwner", player.getDisplayNameString());
        head.setTagCompound(name);
        return head;
    }

    public static boolean hasFullSet(EntityPlayer player, Class<? extends ItemArmor> armor) {
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (!armor.isAssignableFrom(stack.getItem().getClass()))
                return false;
        }
        return true;
    }

    public static boolean hasPart(EntityLivingBase living, EntityEquipmentSlot type, Class<? extends ItemArmor> armor) {
        return armor.isAssignableFrom(living.getItemStackFromSlot(type).getItem().getClass());
    }

    public static boolean isMoving(EntityPlayer player) {
        return player.motionX != 0 || player.motionZ != 0 || player.motionY != 0;
    }
}