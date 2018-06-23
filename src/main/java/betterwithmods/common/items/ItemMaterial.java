package betterwithmods.common.items;

import betterwithmods.client.BWCreativeTabs;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class ItemMaterial extends Item {
    public static final HashMap<EnumMaterial, ItemMaterial> MATERIALS = Maps.newHashMap();

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
        return MATERIALS.get(material);
    }

    public static void init() {
        for (EnumMaterial material : EnumMaterial.VALUES) {
            new ItemMaterial(material);
        }
    }

    private final EnumMaterial material;

    public ItemMaterial(EnumMaterial material) {
        super();
        MATERIALS.put(material, this);
        this.material = material;
        this.setRegistryName(material.getName());
        this.setCreativeTab(BWCreativeTabs.BWTAB);
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


    public enum EnumMaterial {
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
        STEEL_GEAR,
        STEEL_SPRING,
        SOAP,
        PLATE_STEEL,
        WITCH_WART,
        MYSTERY_GLAND,
        POISON_SAC;


        public final static EnumMaterial[] VALUES = values();

        String getName() {
            return this.name().toLowerCase();
        }
    }
}
