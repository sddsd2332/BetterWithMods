package betterwithmods.module.compat.bop;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.mini.*;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.hardcore.needs.HCPiles;
import betterwithmods.module.hardcore.needs.HCSeeds;
import betterwithmods.module.tweaks.MobSpawning;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

@SuppressWarnings("unused")
public class BiomesOPlenty extends CompatFeature {
    public static Item PILES = new ItemBOPPile().setRegistryName("bop_piles");
    public static Block SIDING = new BlockSiding(BlockMini.MINI) {
        @Override
        public int getUsedTypes() {
            return 16;
        }
    }.setRegistryName("bop_compat_siding");
    public static Block MOULDING = new BlockMoulding(BlockMini.MINI) {
        @Override
        public int getUsedTypes() {
            return 16;
        }

    }.setRegistryName("bop_compat_moulding");

    public static Block CORNER = new BlockCorner(BlockMini.MINI) {
        @Override
        public int getUsedTypes() {
            return 16;
        }

    }.setRegistryName("bop_compat_corner");
    public final String[] woods = new String[]{"sacred_oak", "cherry", "umbran", "fir", "ethereal", "magic", "mangrove", "palm", "redwood", "willow", "pine", "hellbark", "jacaranda", "mahogany", "ebony", "eucalyptus",};

    public BiomesOPlenty() {
        super("biomesoplenty");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

        BWMItems.registerItem(PILES);

        BWMBlocks.registerBlock(SIDING, new ItemBlockMini(SIDING));
        BWMBlocks.registerBlock(MOULDING, new ItemBlockMini(MOULDING));
        BWMBlocks.registerBlock(CORNER, new ItemBlockMini(CORNER));

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        BWMItems.setInventoryModel(PILES);
        BWMBlocks.setInventoryModel(SIDING);
        BWMBlocks.setInventoryModel(MOULDING);
        BWMBlocks.setInventoryModel(CORNER);
    }

    @SubscribeEvent
    public void registerRecipe(RegistryEvent.Register<IRecipe> recipes) {
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass")), 5, new ItemStack(BWMItems.DIRT_PILE, 3));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass")), 7, new ItemStack(BWMItems.DIRT_PILE, 3));

        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "farmland_0")), 0, new ItemStack(PILES, 3, 0));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "farmland_0")), 1, new ItemStack(PILES, 3, 1));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "farmland_1")), 0, new ItemStack(PILES, 3, 2));


        for (int i = 2; i <= 4; i++) {
            HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass")), i, new ItemStack(PILES, 3, i - 2));
        }
        for (int i = 0; i <= 2; i++) {
            Block dirt = getBlock(new ResourceLocation(modid, "dirt"));
            HCPiles.registerPile(dirt, i, new ItemStack(PILES, 3, i));
            HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass_path")), i, new ItemStack(PILES, 3, i));
            recipes.getRegistry().register(new ShapedOreRecipe(null, new ItemStack(dirt, 1, i), "PP", "PP", 'P', new ItemStack(PILES, 1, i)).setRegistryName(new ResourceLocation("betterwithmods", "bop_pile." + i)));
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MobSpawning.NETHER.addBlock(new ItemStack(getBlock(new ResourceLocation(modid, "grass")), 1,1));
        MobSpawning.NETHER.addBlock(new ItemStack(getBlock(new ResourceLocation(modid, "grass")), 1,6));
        MobSpawning.NETHER.addBlock(getBlock(new ResourceLocation(modid, "flesh")));
        MobSpawning.NETHER.addBlock(getBlock(new ResourceLocation(modid, "ash_block")));

        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(0));
        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(1));
        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(7));
        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(8));

    }

}
