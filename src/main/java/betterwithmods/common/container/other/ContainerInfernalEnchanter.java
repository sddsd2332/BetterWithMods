package betterwithmods.common.container.other;

import betterwithmods.client.gui.GuiInfernalEnchanter;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemArcaneScroll;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.container.ContainerTile;
import betterwithmods.library.common.container.SlotItemHandlerFiltered;
import betterwithmods.library.common.container.SlotTransformation;
import betterwithmods.library.common.inventory.FilteredStackHandler;
import betterwithmods.library.common.inventory.SimpleStackHandler;
import betterwithmods.common.tile.TileInfernalEnchanter;
import betterwithmods.library.utils.GuiUtils;
import betterwithmods.library.utils.ingredient.PredicateIngredient;
import betterwithmods.module.hardcore.creatures.HCEnchanting;
import betterwithmods.module.internal.AdvancementRegistry;
import betterwithmods.util.InfernalEnchantment;
import betterwithmods.library.utils.InventoryUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by primetoxinz on 9/11/16.
 */
public class ContainerInfernalEnchanter extends ContainerTile<TileInfernalEnchanter> {

    private int[] enchantLevels;

    private static final ResourceLocation PROPERTY_BOOKCASES = new ResourceLocation(ModLib.MODID, "bookcases");

    private static final ResourceLocation SLOT_SCROLLS = new ResourceLocation(ModLib.MODID, "scrolls");
    private static final ResourceLocation SLOT_ENCHANT = new ResourceLocation(ModLib.MODID, "enchant");

    private static Ingredient SCROLLS = Ingredient.fromItem(BWMItems.ARCANE_SCROLL);

    private static ResourceLocation getLevelName(int i) {
        return new ResourceLocation(ModLib.MODID, "enchant_level_" + i);
    }

    public void onContentsChanged(IItemHandler handler) {

    }

    public ContainerInfernalEnchanter(TileInfernalEnchanter tile, EntityPlayer player) {
        super(tile, player);

        this.enchantLevels = new int[5];
        Arrays.fill(enchantLevels, -1);

        addIntProperty(PROPERTY_BOOKCASES, tile::getBookcaseCount);
        for (int i = 0; i < this.enchantLevels.length; i++) {
            int finalI = i;
            addIntProperty(getLevelName(i), () -> this.enchantLevels[finalI]);
        }

        GuiUtils.createPlayerSlots(player, this, 8, 129, 8, 187);
        GuiUtils.createContainerSlots(SLOT_SCROLLS, this, tile.inventory, 1, 1, 0, 17, 37, (inv, i, x, y) -> new SlotItemHandlerFiltered(inv, i, x, y, SCROLLS));
        GuiUtils.createContainerSlots(SLOT_ENCHANT, this, tile.inventory, 1, 1, 1, 17, 75);

        addSlotTransformations(new SlotTransformation(GuiUtils.SLOTS_FULL_PLAYER_INVENTORY, SLOT_ENCHANT));
        addSlotTransformations(new SlotTransformation(GuiUtils.SLOTS_FULL_PLAYER_INVENTORY, SLOT_SCROLLS, SCROLLS));
    }

    public int getEnchantLevel(int i) {
        return getPropertyValue(getLevelName(i));
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
        if (enchantment == null || levelIndex > HCEnchanting.getMaxLevel(enchantment)) {
            return -1;
        } else {
            double max = Math.min(HCEnchanting.getMaxLevel(enchantment), enchantLevels.length);
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
                        getTile().getWorld().playSound(null, getTile().getPos(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.BLOCKS, 1.0F, getTile().getWorld().rand.nextFloat() * 0.1F + 0.9F);
                        onContextChanged(getTile().inventory);
                    }
                }
            }
            return true;
        }
        return false;
    }


}
