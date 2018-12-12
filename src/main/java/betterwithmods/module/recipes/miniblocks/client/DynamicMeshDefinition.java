package betterwithmods.module.recipes.miniblocks.client;

import betterwithmods.lib.ModLib;
import betterwithmods.module.recipes.miniblocks.DynamicType;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class DynamicMeshDefinition implements ItemMeshDefinition {
    private DynamicType type;

    public DynamicMeshDefinition(DynamicType type) {
        this.type = type;
    }

    @Nonnull
    @Override
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
        return new ModelResourceLocation(new ResourceLocation(ModLib.MODID , this.type.getName()), "inventory") ;
    }
}
