package betterwithmods.common.items;

import betterwithmods.library.common.item.IItemType;
import betterwithmods.library.common.item.creation.ItemBuilder;
import betterwithmods.library.common.item.creation.ItemTypeBuilder;
import betterwithmods.library.common.item.creation.ItemTypeBuilderGenerator;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;

public class ItemMaterial extends Item {

    private final EnumMaterial material;

    public ItemMaterial(EnumMaterial material) {
        super();
        this.material = material;
    }

    public static Ingredient getIngredient(EnumMaterial material) {
        return Ingredient.fromStacks(getStack(material));
    }

    public static ItemStack getStack(EnumMaterial material) {
        return getStack(material, 1);
    }

    public static ItemStack getStack(EnumMaterial material, int count) {
        return new ItemStack(getItem(material), count);
    }

    public static Item getItem(EnumMaterial material) {
        return ForgeRegistries.ITEMS.getValue(material.getRegistryName());
    }

    public static void init() {
        for (EnumMaterial material : EnumMaterial.VALUES) {
            new ItemMaterial(material);
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        if (material == EnumMaterial.DIAMOND_INGOT && playerIn != null) {
            BlockPos pos = playerIn.getPosition().up();
            worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            worldIn.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 0.1f, false);
        }
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public int getItemBurnTime(ItemStack stack) {
        switch (material) {
            case WOOD_GEAR:
                return 18;
            case NETHERCOAL:
                return 3200;
            case SAWDUST:
                return 25;
            case WIND_SAIL:
                return 75;
            case WOOD_BLADE:
                return 37;
            case HAFT:
                return 150;

        }
        return -1;
    }


    public enum EnumMaterial implements IItemType {
        WOOD_GEAR,
        NETHERCOAL,
        HEMP_LEAF,
        HEMP_FIBERS,
        HEMP_CLOTH,
        DUNG,
        TANNED_LEATHER,
        SCOURED_LEATHER,
        LEATHER_STRAP,
        LEATHER_BELT,
        WOOD_BLADE,
        WIND_SAIL,
        GLUE,
        TALLOW,
        STEEL_INGOT,
        GROUND_NETHERRACK,
        HELLFIRE_DUST,
        CONCENTRATED_HELLFIRE,
        COAL_DUST,
        FILAMENT,
        POLISHED_LAPIS,
        POTASH,
        SAWDUST,
        SOUL_DUST,
        SCREW,
        BRIMSTONE,
        NITER,
        ELEMENT,
        FUSE,
        BLASTING_OIL,
        STEEL_NUGGET,
        LEATHER_CUT,
        TANNED_LEATHER_CUT,
        SCOURED_LEATHER_CUT,
        REDSTONE_LATCH,
        NETHER_SLUDGE,
        HAFT,
        CHARCOAL_DUST,
        SOUL_FLUX,
        ENDER_SLAG,
        ENDER_OCULAR,
        PADDING,
        ARMOR_PLATE,
        BROADHEAD,
        COCOA_POWDER,
        DIAMOND_INGOT,
        DIAMOND_NUGGET,
        CHAIN_MAIL,
        SOAP,
        WITCH_WART,
        MYSTERY_GLAND,
        POISON_SAC;


        public final static EnumMaterial[] VALUES = values();

        @Nonnull
        @Override
        public ResourceLocation getRegistryName() {
            return GameData.checkPrefix(name().toLowerCase());
        }

        @Override
        public IItemType maxStack(int stack) {
            return this;
        }

        @Override
        public int getMaxStack() {
            return 64;
        }
    }


    public static class Generator extends ItemTypeBuilderGenerator<EnumMaterial, ItemMaterial> {

        public Generator() {
            super(Lists.newArrayList(EnumMaterial.VALUES));
        }

        @Override
        public ItemBuilder<EnumMaterial, ItemMaterial> create(EnumMaterial variant) {
            return new ItemTypeBuilder<EnumMaterial, ItemMaterial>(variant) {
                @Override
                protected ItemMaterial create() {
                    return new ItemMaterial(variant);
                }
            };
        }

    }
}
