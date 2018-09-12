package betterwithmods.common.dynamic;

import betterwithmods.module.recipes.miniblocks.DynamicVariant;
import com.google.common.base.Preconditions;
import net.minecraft.block.material.Material;

import javax.annotation.Nonnull;
import java.util.Set;

public class DynamicBlockEntryBuilder {

    private DynamicVariant variant;
    private Set<Material> materials;

    @Nonnull
    public static DynamicBlockEntryBuilder create() {
        return new DynamicBlockEntryBuilder();
    }

    private DynamicBlockEntryBuilder() {
    }

    public DynamicBlockEntryBuilder variant(DynamicVariant variant) {
        this.variant = Preconditions.checkNotNull(variant, "variant");
        return this;
    }

    public DynamicBlockEntryBuilder materials(Set<Material> materials) {
        this.materials = Preconditions.checkNotNull(materials, "materials");
        return this;
    }


    public DynamicBlockEntry build() {
        Preconditions.checkNotNull(variant, "variant");
        Preconditions.checkNotNull(materials, "materials");

        return new DynamicBlockEntry(variant, materials);
    }


}
