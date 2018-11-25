package betterwithmods.common.container.other;

import betterwithmods.client.gui.GuiInfernalEnchanter;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemArcaneScroll;
import betterwithmods.common.tile.TileInfernalEnchanter;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.container.ContainerTile;
import betterwithmods.library.common.container.SlotItemHandlerFiltered;
import betterwithmods.library.common.container.SlotTransformation;
import betterwithmods.library.common.inventory.SimpleStackHandler;
import betterwithmods.library.utils.GuiUtils;
import betterwithmods.module.general.InfernalEnchanting;
import betterwithmods.module.internal.AdvancementRegistry;
import betterwithmods.util.InfernalEnchantment;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by primetoxinz on 9/11/16.
 */

public class ContainerInfernalEnchanter extends ContainerTile<TileInfernalEnchanter> {

    private static final ResourceLocation PROPERTY_BOOKCASES = new ResourceLocation(ModLib.MODID, "bookcases");
    private static final ResourceLocation SLOT_SCROLLS = new ResourceLocation(ModLib.MODID, "scrolls");
    private static final ResourceLocation SLOT_ENCHANT = new ResourceLocation(ModLib.MODID, "enchant");
    private static Ingredient SCROLLS = Ingredient.fromItem(BWMItems.ARCANE_SCROLL);
    private int[] enchantLevels;

    public ContainerInfernalEnchanter(TileInfernalEnchanter tile, EntityPlayer player) {
        super(tile, player);

        this.enchantLevels = new int[5];
        Arrays.fill(enchantLevels, -1);

        addIntProperty(PROPERTY_BOOKCASES, tile::getBookcaseCount);
        for (int i = 0; i < this.enchantLevels.length; i++) {
            int finalI = i;
            addIntProperty(getLevelName(i), () -> this.enchantLevels[finalI]);
        }

        SimpleStackHandler inventory = tile.inventory.listener((handler, slot) -> onContextChanged(handler));

        GuiUtils.createPlayerSlots(player, this, 8, 129, 8, 187);
        GuiUtils.createContainerSlots(SLOT_SCROLLS, this, inventory, 1, 1, 0, 17, 37, (inv, i, x, y) -> new SlotItemHandlerFiltered(inv, i, x, y, SCROLLS));
        GuiUtils.createContainerSlots(SLOT_ENCHANT, this, inventory, 1, 1, 1, 17, 75);

        addSlotTransformations(new SlotTransformation(GuiUtils.SLOTS_FULL_PLAYER_INVENTORY, SLOT_ENCHANT));
        addSlotTransformations(new SlotTransformation(GuiUtils.SLOTS_FULL_PLAYER_INVENTORY, SLOT_SCROLLS, SCROLLS));
        addSlotTransformations(new SlotTransformation(SLOT_ENCHANT, GuiUtils.SLOTS_FULL_PLAYER_INVENTORY));
        addSlotTransformations(new SlotTransformation(SLOT_SCROLLS, GuiUtils.SLOTS_FULL_PLAYER_INVENTORY));

        onContextChanged(inventory);
    }

    private static ResourceLocation getLevelName(int i) {
        return new ResourceLocation(ModLib.MODID, "enchant_level_" + i);
    }

    public int getEnchantLevel(int i) {
        ResourceLocation l = getLevelName(i);
        return getPropertyValue(l, 0);
    }

    private boolean areValidItems(ItemStack scroll, ItemStack item) {
        if (!scroll.isEmpty() && !item.isEmpty()) {

            Enchantment enchantment = ItemArcaneScroll.getEnchantment(scroll);
            if (enchantment == null)
                return false;

            Set<Enchantment> enchantments = EnchantmentHelper.getEnchantments(item).keySet();
            if (enchantments.contains(enchantment))
                return false;
            for (Enchantment e : enchantments) {
                if (e != null && !e.isCompatibleWith(enchantment))
                    return false;
            }
            enchantment = new InfernalEnchantment(enchantment);
            return item.getItem().canApplyAtEnchantingTable(item, enchantment);
        }
        return false;
    }

    private void onContextChanged(IItemHandler handler) {
        ItemStack scroll = handler.getStackInSlot(0);
        ItemStack item = handler.getStackInSlot(1);
        if (areValidItems(scroll, item)) {
            Enchantment enchantment = ItemArcaneScroll.getEnchantment(scroll);
            int enchantCount = EnchantmentHelper.getEnchantments(item).size();
            for (int levelIndex = 1; levelIndex <= enchantLevels.length; levelIndex++) {
                enchantLevels[levelIndex - 1] = getEnchantCost(levelIndex, enchantment, enchantCount);
            }
        } else {
            Arrays.fill(enchantLevels, -1);
        }
        detectAndSendChanges();
    }

    private int getEnchantCost(int levelIndex, Enchantment enchantment, int enchantCount) {
        if (enchantment == null || levelIndex > InfernalEnchanting.getMaxLevel(enchantment)) {
            return -1;
        } else {
            double max = Math.min(InfernalEnchanting.getMaxLevel(enchantment), enchantLevels.length);
            double multiplier = levelIndex / max;
            return (int) Math.ceil(30.0 * multiplier) + (30 * enchantCount);
        }
    }

    @Override
    public GuiContainer createGui() {
        return new GuiInfernalEnchanter(this);
    }

    public boolean hasLevels(EntityPlayer player, int levelIndex) {
        return player.capabilities.isCreativeMode || player.experienceLevel >= this.enchantLevels[levelIndex];
    }

    public boolean hasBooks(int levelIndex) {
        return getTile().getBookcaseCount() >= this.enchantLevels[levelIndex];
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int levelIndex) {
        if (hasLevels(player, levelIndex) && hasBooks(levelIndex)) {
            if (!player.world.isRemote) {
                ItemStack item = getTile().inventory.getStackInSlot(1);
                ItemStack scroll = getTile().inventory.getStackInSlot(0);
                Enchantment enchantment = ItemArcaneScroll.getEnchantment(scroll);
                if (enchantment != null) {
                    if (!EnchantmentHelper.getEnchantments(item).containsKey(enchantment)) {
                        scroll.shrink(1);
                        item.addEnchantment(enchantment, levelIndex + 1);
                        player.onEnchant(item, this.enchantLevels[levelIndex]);
                        AdvancementRegistry.INFERNAL_ENCHANTED.trigger((EntityPlayerMP) player, item, this.enchantLevels[levelIndex]);
                        TileEntity tile = getTile();
                        tile.getWorld().addWeatherEffect(new EntityLightningBolt(tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), true));
                        tile.getWorld().playSound(null, getTile().getPos(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.BLOCKS, 1.0F, getTile().getWorld().rand.nextFloat() * 0.1F + 0.9F);
                        onContextChanged(getTile().inventory);
                    }
                }
            }
            return true;
        }
        return false;
    }


}
