package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import betterwithmods.util.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SilverfishClay extends Feature {

    @SubscribeEvent
    public void onDeath(LivingDropsEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntitySilverfish) {
            if (entity.world.provider.getDimensionType() == DimensionType.THE_END)
                WorldUtils.addDrop(event, new ItemStack(Items.CLAY_BALL, entity.getRNG().nextInt(3)));
        }
    }

    @Override
    public String getDescription() {
        return "Silverfish that die in the End will drop clay balls. This combined with HCBeacons allows easy farming of clay.";
    }
}
