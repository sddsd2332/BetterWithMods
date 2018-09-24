package betterwithmods.module.internal;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMCreativeTabs;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.camo.BlockCamo;
import betterwithmods.lib.ModLib;
import betterwithmods.library.modularity.impl.RequiredFeature;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class BlockRegistry extends RequiredFeature {

    private static List<Block> REGISTRY = Lists.newArrayList();

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BWMBlocks.register();
    }

    public static void registerBlock(Block block, @Nullable Item item) {
        if (block.getRegistryName() != null) {

            if (block instanceof BlockCamo) {
                block.setCreativeTab(BWMCreativeTabs.MINI_BLOCKS);
            } else {
                block.setCreativeTab(BWMCreativeTabs.BLOCKS);
            }
            //TODO remove this in 1.13, it is done automatically
            if (block.getTranslationKey().equals("tile.null"))
                block.setTranslationKey(block.getRegistryName().toString());
            REGISTRY.add(block);
            if (item != null) {
                BWMItems.registerItem(item.setRegistryName(block.getRegistryName()));
            }
        }
    }

    public static Block registerBlock(Block block) {
        registerBlock(block, new ItemBlock(block));
        return block;
    }

    public static void registerBlocks(Collection<? extends Block> blocks) {
        blocks.forEach(BlockRegistry::registerBlock);
    }


    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        REGISTRY.forEach(block -> event.getRegistry().register(block));
    }
}
