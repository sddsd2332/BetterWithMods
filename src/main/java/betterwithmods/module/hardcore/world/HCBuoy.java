package betterwithmods.module.hardcore.world;

import betterwithmods.module.CompatFeature;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.HashMap;

/**
 * @author Koward
 */
public class HCBuoy extends CompatFeature {

    public static final HashMap<Ingredient, Float> buoyancy = Maps.newHashMap();

    public HCBuoy() {
        super("hardcorebuoy");
    }

    public static float getBuoyancy(ItemStack stack) {
        return buoyancy.keySet().stream().filter(i -> i.apply(stack)).map(buoyancy::get).findFirst().orElse(-1f);
    }


    public static void initBuoyancy() {
        //Blocks
//        //TODO make this an ingredient registry in the main mod.
//        buoyancy.put(BWMBlocks.WOODEN_AXLE, 1.0F);
//        buoyancy.put(BWMBlocks.PUMP, 0.0F);
////        buoyancy.put(BWMBlocks.WOOD_SIDING, 1.0F);
////        buoyancy.put(BWMBlocks.WOOD_MOULDING, 1.0F);
////        buoyancy.put(BWMBlocks.WOOD_CORNER, 1.0F);
//        buoyancy.put(BWMBlocks.SAW, 1.0F);
//        buoyancy.put(BWMBlocks.PLATFORM, 1.0F);
//        buoyancy.put(BWMBlocks.WOLF, 1.0F);
//        buoyancy.put(BWMBlocks.HEMP, 1.0F);
//        buoyancy.put(BWMBlocks.ROPE, 1.0F);
//        buoyancy.put(BWMBlocks.WOODEN_GEARBOX, 1.0F);
//        buoyancy.put(BWMBlocks.BELLOWS, 1.0F);
//
//        buoyancy.put(BWMBlocks.URN, 1.0F);
//        buoyancy.put(BWMBlocks.FILTERED_HOPPER, 1.0F);
//        buoyancy.put(BWMBlocks.PULLEY, 1.0F);
//
//        //Items
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_LEAF), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_FIBERS), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DUNG), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.TANNED_LEATHER), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_STRAP), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_BELT), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.WOOD_BLADE), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE), 0.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.TALLOW), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.HAFT), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_CUT), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_FLUX), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SAWDUST), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_DUST), 1.0F);
//        buoyancy.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHER_SLUDGE), 1.0F);
//        buoyancy.put(BWMItems.DYNAMITE, 1.0F);
//        buoyancy.put(BWMItems.STUMP_REMOVER, 1.0F);
//        buoyancy.put(BWMItems.CREEPER_OYSTER, 1.0F);
//        buoyancy.put(BWMItems.DONUT, 1.0F);


    }

    @Override
    public String getFeatureDescription() {
        return "Add values for BWM items to the Hardcore Buoy mod.";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        initBuoyancy();

        for (Ingredient ingredient : buoyancy.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            float buoy = buoyancy.get(ingredient);
            for (ItemStack stack : ingredient.getMatchingStacks()) {
                tag.setTag("stack", stack.serializeNBT());
                tag.setFloat("value", buoy);
                FMLInterModComms.sendMessage("hardcorebuoy", "buoy", tag);
            }
        }
    }


    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
