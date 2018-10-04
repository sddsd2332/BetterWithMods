package betterwithmods.network.handler;

import betterwithmods.library.common.network.MessageHandler;
import betterwithmods.module.recipes.animal_restraint.AnimalRestraint;
import betterwithmods.module.recipes.animal_restraint.Harness;
import betterwithmods.network.messages.MessageHarness;
import betterwithmods.util.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHarnessHandler extends MessageHandler<MessageHarness> {
    @Override
    public void handleMessage(MessageHarness message, MessageContext context) {
        syncHarness(message.entity, message.stack);
    }

    public void syncHarness(int entityId, ItemStack harness) {
        Entity entity = PlayerUtils.getEntityById(entityId);
        if (entity != null) {
            Harness cap = AnimalRestraint.getHarnessCapability(entity);
            if (cap != null) {
                cap.setHarness(harness);
            }
        }
    }
}
