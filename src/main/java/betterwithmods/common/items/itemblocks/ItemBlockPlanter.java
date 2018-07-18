package betterwithmods.common.items.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.ColorizerGrass;

public class ItemBlockPlanter extends ItemBlock {
    public ItemBlockPlanter(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public int getColorFromItemStack(int tintIndex) {
        return tintIndex > -1 ? ColorizerGrass.getGrassColor(0.5D, 1.0D) : -1;
    }
}
