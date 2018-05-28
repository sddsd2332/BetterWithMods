package betterwithmods.module.hardcore.creatures;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.entity.ai.AIFoodEggLayer;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import betterwithmods.util.WorldUtils;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by primetoxinz on 5/13/17.
 */
public class HCChickens extends Feature {

    public static final ResourceLocation EGG_LAYER = new ResourceLocation(BWMod.MODID, "egglayer");
    public static Set<ItemStack> SEEDS;
    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(IEggLayer.class)
    public static Capability<IEggLayer> EGG_LAYER_CAP = null;

    @Override
    public void setupConfig() {
    }

    @Override
    public String getFeatureDescription() {
        return "Rework chicken breeding. Chickens don't breed in pairs. You feed a single chicken 1 seed, and it craps out an egg that can be thrown. The egg either makes a chicken, or drops raw egg.";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(HCChickens.IEggLayer.class, new HCChickens.CapabilityEggLayer(), EggLayer::new);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        SEEDS = Sets.newHashSet(new ItemStack(BWMBlocks.HEMP), new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.MELON_SEEDS), new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Items.BEETROOT_SEEDS));
    }

    @SubscribeEvent
    public void onAttachCap(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityChicken) {
            event.addCapability(EGG_LAYER, new ChickenProvider(new ItemStack(Items.EGG), SEEDS));
        }
    }

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityAnimal && event.getEntity().hasCapability(EGG_LAYER_CAP, EnumFacing.DOWN)) {
            EntityAnimal animal = (EntityAnimal) event.getEntity();
            animal.tasks.addTask(3, new AIFoodEggLayer(animal));
        }
    }

    @SubscribeEvent
    public void onEntityTick(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving.world.isRemote)
            return;
        if (entityLiving instanceof EntityChicken) {
            EntityChicken chicken = (EntityChicken) entityLiving;
            //Stops vanilla egg dropping mechanic
            chicken.timeUntilNextEgg = 6000000;
        }
        if (entityLiving.hasCapability(EGG_LAYER_CAP, EnumFacing.DOWN)) {
            World world = entityLiving.getEntityWorld();
            IEggLayer layer = entityLiving.getCapability(EGG_LAYER_CAP, EnumFacing.DOWN);
            if (layer != null) {
                if (layer.canLayEgg(world)) {
                    entityLiving.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (entityLiving.getRNG().nextFloat() - entityLiving.getRNG().nextFloat()) * 0.2F + 1.0F);
                    InvUtils.ejectStackWithOffset(entityLiving.world, event.getEntityLiving().getPosition(), layer.getEggItem());
                    layer.setFeed(false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (SEEDS.stream().anyMatch(s -> s.isItemEqual(event.getItemStack())) && event.getTarget() instanceof EntityLiving && event.getTarget().hasCapability(EGG_LAYER_CAP, EnumFacing.DOWN)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            IEggLayer layer = event.getTarget().getCapability(EGG_LAYER_CAP, EnumFacing.DOWN);
            layer.feed((EntityLiving) event.getTarget(), event.getItemStack());
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }


    public interface IEggLayer {

        boolean canLayEgg(World world);

        boolean isFeed();

        void setFeed(boolean feed);

        int getLayTime();

        void setLayTime(int tick);

        ItemStack getEggItem();

        Set<ItemStack> getFeedItems();

        default boolean isBreedingItem(ItemStack stack) {
            return getFeedItems().stream().anyMatch(s -> s.isItemEqual(stack));
        }

        default void feed(EntityLiving entity, ItemStack stack) {
            if (!isFeed()) {
                if (isBreedingItem(stack)) {
                    setFeed(true);
                    World world = entity.world;

                    int nextDay;
                    int day = WorldUtils.getCurrentDay(world);

                    if (WorldUtils.isTimeFrame(entity.world, WorldUtils.TimeFrame.NIGHT)) {
                        nextDay = (day + 2);
                    } else {
                        nextDay = (day + 1);
                    }

                    setLayTime((int) ((nextDay * WorldUtils.Time.DAY.getTicks()) + WorldUtils.TimeFrame.DAWN.randomBetween()));
                    stack.shrink(1);
                    entity.attackEntityFrom(new DamageSource("feed"), 0);
                }
            }
        }

    }

    public static class CapabilityEggLayer implements Capability.IStorage<IEggLayer> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IEggLayer> capability, IEggLayer instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("ticks", instance.getLayTime());
            tag.setBoolean("feed", instance.isFeed());
            return tag;

        }

        @Override
        public void readNBT(Capability<IEggLayer> capability, IEggLayer instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tag = (NBTTagCompound) nbt;
            instance.setLayTime(tag.getInteger("ticks"));
            instance.setFeed(tag.getBoolean("feed"));
        }
    }


    public static class EggLayer implements IEggLayer {
        private int ticks;
        private boolean feed;
        private ItemStack egg;
        private Set<ItemStack> feedItems;

        public EggLayer(ItemStack egg, Set<ItemStack> feedItems) {
            this.egg = egg;
            this.feedItems = feedItems;
        }

        public EggLayer() {
        }

        @Override
        public boolean canLayEgg(World world) {
            return isFeed() && getLayTime() <= world.getTotalWorldTime();
        }

        public boolean isFeed() {
            return feed;
        }

        public void setFeed(boolean feed) {
            this.feed = feed;
        }

        @Override
        public int getLayTime() {
            return this.ticks;
        }

        @Override
        public void setLayTime(int tick) {
            this.ticks = tick;
        }

        @Override
        public ItemStack getEggItem() {
            return egg;
        }

        @Override
        public Set<ItemStack> getFeedItems() {
            return feedItems;
        }
    }

    public static class ChickenProvider implements ICapabilityProvider {

        private final IEggLayer cap;

        public ChickenProvider(ItemStack egg, Set<ItemStack> feedItems) {
            this.cap = new EggLayer(egg, feedItems);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            if (capability == EGG_LAYER_CAP)
                return true;
            return false;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == EGG_LAYER_CAP)
                return EGG_LAYER_CAP.cast(cap);
            return null;
        }
    }
}
