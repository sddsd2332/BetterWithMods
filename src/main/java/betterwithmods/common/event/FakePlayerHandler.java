package betterwithmods.common.event;

import betterwithmods.common.BWMItems;
import betterwithmods.lib.ModLib;
import betterwithmods.util.player.Profiles;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class FakePlayerHandler {
    @GameRegistry.ObjectHolder("minecraft:looting")
    public static final Enchantment looting = null;
    private static FakePlayer sword, shovel;

    public static FakePlayer getShovel() {
        return shovel;
    }

    public static FakePlayer getSword() {
        return sword;
    }

    public static void reset() {
        sword = null;
        shovel = null;
    }

    //Initializing a static fake sword for saws, so spawn isn't flooded with sword equipping sounds when mobs hit the saw.
    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load evt) {
        if (evt.getWorld() instanceof WorldServer) {
            //Use for the saw
            sword = FakePlayerFactory.get((WorldServer) evt.getWorld(), Profiles.BWMSAW);
            ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
            sword.addEnchantment(looting, 2);
            FakePlayerHandler.sword.setHeldItem(EnumHand.MAIN_HAND, sword);


            //Used by mining charges to overcome HCPiles
            shovel = FakePlayerFactory.get((WorldServer) evt.getWorld(), Profiles.BWMSSHOVELER);
            ItemStack shovel = new ItemStack(BWMItems.STEEL_MATTOCK);
            FakePlayerHandler.shovel.setHeldItem(EnumHand.MAIN_HAND, shovel);
        }
    }
}
