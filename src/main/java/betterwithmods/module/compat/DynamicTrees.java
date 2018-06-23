package betterwithmods.module.compat;

import betterwithmods.api.util.IBlockVariants;
import betterwithmods.api.util.IVariantProvider;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.variants.WoodVariant;
import betterwithmods.module.CompatFeature;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class DynamicTrees extends CompatFeature {
    @GameRegistry.ObjectHolder("dynamictrees:oakbranch")
    private static Block oakbranch;
    @GameRegistry.ObjectHolder("dynamictrees:sprucebranch")
    private static Block sprucebranch;
    @GameRegistry.ObjectHolder("dynamictrees:birchbranch")
    private static Block birchbranch;
    @GameRegistry.ObjectHolder("dynamictrees:junglebranch")
    private static Block junglebranch;
    @GameRegistry.ObjectHolder("dynamictrees:acaciabranch")
    private static Block acaciabranch;
    @GameRegistry.ObjectHolder("dynamictrees:darkoakabranch")
    private static Block darkoakabranch;

    public DynamicTrees() {
        super("dynamictrees");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        BWOreDictionary.variantProviders.add(new DynamicWoodProvider(oakbranch, new ItemStack(Blocks.LOG, 1, 0), new ItemStack(Blocks.PLANKS, 1, 0)));
        BWOreDictionary.variantProviders.add(new DynamicWoodProvider(sprucebranch, new ItemStack(Blocks.LOG, 1, 1), new ItemStack(Blocks.PLANKS, 1, 1)));
        BWOreDictionary.variantProviders.add(new DynamicWoodProvider(birchbranch, new ItemStack(Blocks.LOG, 1, 2), new ItemStack(Blocks.PLANKS, 1, 2)));
        BWOreDictionary.variantProviders.add(new DynamicWoodProvider(junglebranch, new ItemStack(Blocks.LOG, 1, 3), new ItemStack(Blocks.PLANKS, 1, 3)));
        BWOreDictionary.variantProviders.add(new DynamicWoodProvider(acaciabranch, new ItemStack(Blocks.LOG2, 1, 0), new ItemStack(Blocks.PLANKS, 1, 4)));
        BWOreDictionary.variantProviders.add(new DynamicWoodProvider(darkoakabranch, new ItemStack(Blocks.LOG2, 1, 1), new ItemStack(Blocks.PLANKS, 1, 5)));
    }


    public static class DynamicWoodProvider implements IVariantProvider {

        private final Block block;
        private final ItemStack log;
        private final ItemStack planks;

        DynamicWoodProvider(Block block, ItemStack log, ItemStack planks) {
            this.block = block;
            this.log = log;
            this.planks = planks;
        }

        @Override
        public boolean match(IBlockState state) {
            return state.getBlock() == block;
        }

        @Override
        public IBlockVariants getVariant(IBlockVariants.EnumBlock variant, IBlockState state) {
            PropertyInteger radius = (PropertyInteger) state.getPropertyKeys().stream().filter(p -> p.getName().equalsIgnoreCase("radius")).findFirst().orElse(null);
            int r = 1;
            if (radius != null) {
                r = state.getValue(radius);
            }
            int finalR = r;
            return new WoodVariant() {
                {
                    addVariant(EnumBlock.LOG, log);
                    addVariant(EnumBlock.BLOCK, planks);
                }

                @Override
                public ItemStack getVariant(EnumBlock type, int count) {
                    if (type == EnumBlock.BLOCK) {
                        count = (finalR * count) / 4;
                    }
                    return super.getVariant(type, count);
                }
            };
        }
    }
}
