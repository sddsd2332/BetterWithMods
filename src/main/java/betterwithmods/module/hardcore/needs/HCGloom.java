package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWRegistry;
import betterwithmods.common.damagesource.BWDamageSource;
import betterwithmods.common.penalties.GloomPenalties;
import betterwithmods.module.Feature;
import betterwithmods.util.StackIngredient;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by primetoxinz on 5/13/17.
 */
public class HCGloom extends Feature {
    private static final DataParameter<Integer> GLOOM_TICK = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
    private static final List<SoundEvent> sounds = Lists.newArrayList(SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundEvents.ENTITY_ENDERMEN_SCREAM, SoundEvents.ENTITY_SILVERFISH_AMBIENT, SoundEvents.ENTITY_WOLF_GROWL);
    private static Set<Integer> dimensionWhitelist;
    private static Ingredient gloomOverrideItems;

    public static int getGloomTime(EntityPlayer player) {
        try {
            return player.getDataManager().get(GLOOM_TICK);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static void incrementGloomTime(EntityPlayer player) {
        int time = getGloomTime(player);
        setGloomTick(player, time + 1);
    }

    public static void setGloomTick(EntityPlayer player, int value) {
        player.getDataManager().set(GLOOM_TICK, value);
    }

    @Override
    public void setupConfig() {
        dimensionWhitelist = Sets.newHashSet(ArrayUtils.toObject(loadPropIntList("Gloom Dimension Whitelist", "Gloom is only available in these dimensions", new int[]{0})));
        BWRegistry.PENALTY_HANDLERS.add(new GloomPenalties());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        gloomOverrideItems = StackIngredient.fromStacks(loadItemStackArray("Gloom Override Items", "Items in this list will override the gloom effect while held in your hand, this allows support for Dynamic Lightning and similar. Add one item per line  (ex minecraft:torch:0)", new ItemStack[0]));
    }

    @SubscribeEvent
    public void onEntityInit(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof EntityPlayer) {
            event.getEntity().getDataManager().register(GLOOM_TICK, 0);
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
        setGloomTick(e.player, 0);
    }

    @SubscribeEvent
    public void inDarkness(TickEvent.PlayerTickEvent e) {

        EntityPlayer player = e.player;
        World world = player.getEntityWorld();

        if (!PlayerHelper.isSurvival(player) || !dimensionWhitelist.contains(world.provider.getDimension()))
            return;

        if (!world.isRemote) {

            int light = world.getLight(player.getPosition().up());
            int tick = getGloomTime(player);
            if (PlayerHelper.isHolding(player, gloomOverrideItems))
                light = 15;
            if (player.isPotionActive(MobEffects.NIGHT_VISION))
                light = 15;
            if (light <= 0) {
                incrementGloomTime(player);
            } else if (tick != 0) {
                setGloomTick(player, 0);
            }
        }

        //TODO nausea & more sounds


        //Random sounds
        if (world.isRemote) {
            float spooked = BWRegistry.PENALTY_HANDLERS.getSpooked(player);
            if (world.rand.nextDouble() <= spooked) {
                player.playSound(SoundEvents.AMBIENT_CAVE, 0.7F, 0.8F + world.rand.nextFloat() * 0.2F);
                if (spooked > 0.5)
                    player.playSound(sounds.get(world.rand.nextInt(sounds.size())), 0.7F, 0.8F + world.rand.nextFloat() * 0.2F);
            }
        }

        if (world.getTotalWorldTime() % 40 == 0) {
            if (world.rand.nextInt(2) == 0) {
                if (BWRegistry.PENALTY_HANDLERS.attackedByGrue(player)) {
                    player.attackEntityFrom(BWDamageSource.gloom, 1);
                }
            }
        }

    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event) {
//        GloomPenalty penalty = PlayerHelper.getGloomPenalty(event.getEntity());
//        if (penalty != GloomPenalty.NO_PENALTY) {
//            float change;
//            if (penalty != GloomPenalty.TERROR)
//                change = (getGloomTime(event.getEntity()) / 2400f);
//            else
//                change = -(getGloomTime(event.getEntity()) / 100000f);
//            event.setNewfov(event.getFov() + change);
//        }
    }

    @Override
    public String getFeatureDescription() {
        return "Be afraid of the dark...";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

}
