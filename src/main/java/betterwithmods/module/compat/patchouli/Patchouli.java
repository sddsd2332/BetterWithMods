package betterwithmods.module.compat.patchouli;

import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.CapabilityUtils;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.ingredient.NBTIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.UUID;

public class Patchouli extends Feature {
    public Patchouli() {
    }

    @Override
    public String getDescription() {
        return "Patchouli API registration";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }


    private static IMultiblock kilnTube, kilnCorner;

    @Override
    public void onInit(FMLInitializationEvent event) {
        PatchouliAPI.IPatchouliAPI api = PatchouliAPI.instance;


        Object[] kilnMatchers = new Object[]{
                'E', api.airMatcher(),
                ' ', api.anyMatcher(),
                'B', api.predicateMatcher(Blocks.BRICK_BLOCK, KilnStructureManager::isKilnBlock)
        };

        kilnTube = api.registerMultiblock(
                new ResourceLocation(ModLib.MODID, "kiln_tube"),
                api.makeMultiblock(new String[][]{
                        {"   ", " B ", "   "},
                        {"   ", "BEB", "   "},
                        {"   ", " B ", "   "},
                        {"   ", " 0 ", "   "},
                }, kilnMatchers))
                .setSymmetrical(true);

        kilnCorner = api.registerMultiblock(
                new ResourceLocation(ModLib.MODID, "kiln_corner"),
                api.makeMultiblock(new String[][]{
                        {"   ", " B ", "   "},
                        {"   ", "BE ", " B "},
                        {"   ", " B ", "   "},
                        {"   ", " 0 ", "   "}
                }, kilnMatchers))
                .setSymmetrical(true);

    }

    private static boolean isJournal(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().getRegistryName().equals(new ResourceLocation("patchouli:guide_book"))) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                String book = tag.getString("patchouli:book");
                return book.equals("betterwithmods:journal");
            }
        }
        return false;
    }

    private static UUID getOwner(ItemStack stack) {
        if (isJournal(stack)) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                return UUID.fromString(tag.getString("betterwithmods:owner"));
            }
        }
        return null;
    }

    private static ItemStack getJournal(EntityPlayer player) {
        Item BOOK = ForgeRegistries.ITEMS.getValue(new ResourceLocation("patchouli:guide_book"));
        ItemStack stack = new ItemStack(BOOK);
        NBTTagCompound tag = new NBTTagCompound();
        String uuid = player.getUniqueID().toString();
        tag.setString("betterwithmods:owner", uuid);
        tag.setString("patchouli:book", "betterwithmods:journal");
        stack.setTagCompound(tag);
        return stack;
    }

    @SubscribeEvent
    public void onItemClicked(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (isJournal(stack)) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && !tag.hasKey("betterwithmods:owner")) {
                tag.setString("betterwithmods:owner", event.getEntityPlayer().getUniqueID().toString());
            }
        }
    }

    @SubscribeEvent
    public void onEntityItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        Ingredient journal = new NBTIngredient(getJournal(player));

        if (InventoryUtils.containsIngredient(journal, CapabilityUtils.getFullPlayerInventory(player))) {
            event.getItem().getItem().shrink(1);
        }

    }


}

