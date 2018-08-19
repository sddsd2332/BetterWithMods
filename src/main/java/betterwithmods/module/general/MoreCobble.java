package betterwithmods.module.general;

import betterwithmods.common.blocks.BlockCobble;
import betterwithmods.module.Feature;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class MoreCobble extends Feature {

    @SubscribeEvent
    public static void dropCobble(BlockEvent.HarvestDropsEvent event) {
        if (!event.isSilkTouching() && !event.getWorld().isRemote) {
            IBlockState state = event.getState();
            if (state.getBlock() == Blocks.STONE) {
                BlockStone.EnumType type = state.getValue(BlockStone.VARIANT);
                switch (type) {
                    case GRANITE:
                    case DIORITE:
                    case ANDESITE:
                        event.getDrops().clear();
                        event.getDrops().add(BlockCobble.getStack(BlockCobble.EnumType.convert(type)));
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "Makes stone variants drop into cobblestone.";
    }
}
