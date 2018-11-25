package betterwithmods.module.general.moreheads;

import betterwithmods.common.BWMCreativeTabs;
import betterwithmods.library.common.block.creation.BlockEntryBuilderFactory;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.module.general.moreheads.client.TESRHead;
import betterwithmods.module.general.moreheads.common.BlockHead;
import betterwithmods.module.general.moreheads.common.HeadType;
import betterwithmods.module.general.moreheads.common.ItemHead;
import betterwithmods.module.general.moreheads.common.TileHead;
import betterwithmods.module.internal.BlockRegistry;
import betterwithmods.module.tweaks.HeadDrops;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class MoreHeads extends Feature {

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BlockRegistry.registerBlocks(BlockEntryBuilderFactory.create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .id("head")
                .tile(TileHead.class)
                .builder()
                .block(new BlockHead()).itemblock(ItemHead::new)
                .build()
                .complete());
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileHead.class, new TESRHead());

    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        for(HeadType type: HeadType.VALUES) {
            HeadDrops.addHeadDrop(type.getRegistryName(), type::getStack);
        }
    }

    @Override
    public String getDescription() {
        return "Add more skulls and heads for mobs";
    }
}
