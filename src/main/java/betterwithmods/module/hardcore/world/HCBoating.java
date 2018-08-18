package betterwithmods.module.hardcore.world;

import betterwithmods.module.Feature;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber
public class HCBoating extends Feature {

    //Quark Boat Sail Compat
    private static final String TAG_BANNER = "quark:banner";
    public static HashMap<Ingredient, Integer> SPEED_ITEMS;
    public static List<ResourceLocation> BOAT_ENTRIES;
    public static int defaultSpeed;

    @Override
    public String getDescription() {
        return "Boats are much slower as simple oars are not very good for speed. To go faster you must hold a Wind Sail.";
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        config().loadRecipeCondition("boatshovel", getCategory(), "Boat Requires Oar", "Make boat recipe require a wooden shovel for the oars", true);
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        SPEED_ITEMS = config().loadItemStackIntMap("Speed Items", getCategory(), "Items which speed up a boat when held, value is a percentage of the vanilla speed", new String[]{
                "betterwithmods:wind_sail=100",
                "minecraft:banner:*=100"
        });
        defaultSpeed = loadProperty("Default Speed modifier", 50).setComment("Speed modifier when not holding any sail type item").get();
        BOAT_ENTRIES = config().loadResouceLocations("Boat List", getCategory(), "Registry name for entities which are considered boats", new String[]{"minecraft:boat"});
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.world.isRemote)
            return;
        EntityPlayer player = event.player;
        Entity riding = player.getRidingEntity();
        if (riding != null && BOAT_ENTRIES.stream().anyMatch(r -> EntityList.isMatchingName(riding, r))) {
            Set<ItemStack> stacks = PlayerHelper.getHolding(player);
            int speed = defaultSpeed;
            for (ItemStack stack : stacks) {
                if (speed <= defaultSpeed) {
                    if (!stack.isEmpty()) {
                        speed = SPEED_ITEMS.entrySet().stream().filter(e -> e.getKey().apply(stack)).mapToInt(Map.Entry::getValue).findAny().orElse(defaultSpeed);
                    }
                }
            }

            if (Loader.isModLoaded("quark")) {
                int quarkCompat = quarkCompatSpeed((EntityBoat) riding);
                if (quarkCompat > 0)
                    speed = quarkCompat;
            }

            riding.motionX *= (speed / 100f);
            riding.motionZ *= (speed / 100f);
        }
    }

    private static int quarkCompatSpeed(EntityBoat boat) {
        NBTTagCompound tag = boat.getEntityData();
        if (tag.hasKey(TAG_BANNER)) {
            NBTTagCompound cmp = boat.getEntityData().getCompoundTag(TAG_BANNER);
            ItemStack stack = new ItemStack(cmp);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBanner) {
                return SPEED_ITEMS.entrySet().stream().filter(e -> e.getKey().apply(stack)).findFirst().map(Map.Entry::getValue).orElse(0);
            }
        }
        return 0;
    }

}
