package betterwithmods.common.dynamic;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.camo.BlockCamo;
import betterwithmods.module.recipes.miniblocks.DynamicVariant;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DynamicBlockEntry {

    private DynamicVariant variant;
    private Map<Material, BlockCamo> blocks = Maps.newHashMap();
    private Function<Material, ? extends BlockCamo> factory;
    private Set<Material> materials;

    DynamicBlockEntry(DynamicVariant variant, Set<Material> materials) {
        this.variant = variant;
        this.materials = materials;
        this.init();
    }

    protected void init() {
        this.factory = new ConstructorFactory<BlockCamo>(this.variant.getBlock()) {
            @Override
            protected String describeBlock() {
                //TODO
                return "";
            }
        };
    }

    public DynamicBlockEntry populate() {
        for (Material material : materials) {
            BlockCamo block = (BlockCamo) this.factory.apply(material).setRegistryName(new ResourceLocation(BWMod.MODID, String.format("%s_%s", variant.getName(), BWMDynamicBlocks.getMaterialName(material))));
            blocks.put(material, block);
        }
        return this;
    }

    public void registerBlocks() {
        for (BlockCamo camo : blocks.values()) {
            BWMBlocks.registerBlock(camo, camo.createItemBlock(camo));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> registry, IBakedModel model) {
        registerModels(registry,model, Sets.newHashSet("normal", "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> registry, IBakedModel model, Set<String> variants) {
        Preconditions.checkNotNull(model, "model");

        for (BlockCamo camo : blocks.values()) {
            ResourceLocation rl = camo.getRegistryName();
            if (rl != null) {
                for (String variant : variants) {
                    registry.putObject(new ModelResourceLocation(rl, variant), model);
                }
            }
        }
    }

    static abstract class ConstructorFactory<T extends BlockCamo> implements Function<Material, T> {
        private final Constructor<? extends T> constructor;

        ConstructorFactory(final Class<? extends T> blockClazz) {
            this.constructor = ReflectionHelper.findConstructor(blockClazz, Material.class, Function.class);
        }

        @Override
        public T apply(final Material material) {
            try {
                return this.constructor.newInstance(material, BWMDynamicBlocks.MATERIALS_GETTER);
            } catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
                FMLLog.log.error("Encountered an exception while constructing block '{}'", this.describeBlock(), e);
                return null;
            }
        }

        protected abstract String describeBlock();
    }
}
