package betterwithmods.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockWicker extends BlockPane {

    public BlockWicker() {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
    }

}
