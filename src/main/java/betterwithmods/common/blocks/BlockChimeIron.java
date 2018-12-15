package betterwithmods.common.blocks;

import betterwithmods.module.internal.SoundRegistry;
import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import net.minecraft.block.material.Material;

public class BlockChimeIron extends BlockChime {
    public BlockChimeIron(Material material, ISubtypeProvider subtypes) {
        super(() -> SoundRegistry.BLOCK_CHIME_METAL, material, subtypes);
    }
}
