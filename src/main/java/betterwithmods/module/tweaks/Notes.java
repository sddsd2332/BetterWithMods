package betterwithmods.module.tweaks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class Notes extends Feature {

    @SubscribeEvent
    public static void onNotePlay(NoteBlockEvent.Play event) {
        BlockPos pos = event.getPos();
        if (event.getWorld().getBlockState(pos).getBlock() != Blocks.NOTEBLOCK)
            return;

        int type = getType(event.getWorld(), pos);
        if (type > 0) {
            event.setCanceled(true);

            SoundEvent sound = null;
            switch (type) {
                case 1:
                    sound = SoundEvents.ENTITY_WOLF_AMBIENT;

            }

            if (sound != null) {
                float pitch = (float) Math.pow(2.0, (event.getVanillaNoteId() - 12) / 12.0);
                event.getWorld().playSound(null, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, sound, SoundCategory.BLOCKS, 1F, pitch);
            }
        }
    }

    private static int getType(World world, BlockPos pos) {
        IBlockState down = world.getBlockState(pos.down());
        if (down.getBlock() == BWMBlocks.WOLF)
            return 1;
        return 0;
    }

    @Override
    public String getDescription() {
        return "Add some notes to the Note Block. Try a companion cube under one";
    }
}
