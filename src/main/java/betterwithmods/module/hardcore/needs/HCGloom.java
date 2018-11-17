package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWMDamageSource;
import betterwithmods.common.penalties.GloomPenalties;
import betterwithmods.common.penalties.GloomPenalty;
import betterwithmods.common.penalties.attribute.BWMAttributes;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.TimeUtils;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.module.internal.MiscRegistry;
import betterwithmods.network.BWMNetwork;
import betterwithmods.network.messages.MessageGloom;
import betterwithmods.util.PlayerUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Created by primetoxinz on 5/13/17.
 */


public class HCGloom extends Feature {
    private static final List<SoundEvent> sounds = Lists.newArrayList(SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundEvents.ENTITY_ENDERMEN_SCREAM, SoundEvents.ENTITY_SILVERFISH_AMBIENT, SoundEvents.ENTITY_WOLF_GROWL);
    private static final ResourceLocation PLAYER_GLOOM = new ResourceLocation(ModLib.MODID, "gloom");
    @CapabilityInject(Gloom.class)
    public static Capability<Gloom> GLOOM_CAPABILITY;
    public static GloomPenalties PENALTIES;
    private static Set<Integer> dimensionWhitelist;
    private static Ingredient gloomOverrideItems;

    public static int getGloomTime(EntityPlayer player) {
        Gloom gloom = getGloom(player);
        if (gloom != null)
            return gloom.getGloom();
        return 0;
    }

    public static void incrementGloomTime(EntityPlayerMP player) {
        int time = getGloomTime(player);
        setGloomTick(player, time + 1);
    }

    public static void setGloomTick(EntityPlayerMP player, int value) {
        Gloom gloom = getGloom(player);
        if (gloom != null) {
            gloom.setGloom(value);
            BWMNetwork.INSTANCE.sendTo(new MessageGloom(player.getUniqueID().toString(), value), player);
        }
    }

    public static Gloom getGloom(Entity entity) {
        if (entity.hasCapability(GLOOM_CAPABILITY, null)) {
            return entity.getCapability(GLOOM_CAPABILITY, null);
        }
        return null;
    }

    @SubscribeEvent
    public void clone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        Gloom o = getGloom(event.getOriginal());
        Gloom n = getGloom(event.getEntityPlayer());
        if (o != null && n != null) {
            n.deserializeNBT(o.serializeNBT());
        }
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer && !event.getCapabilities().containsKey(PLAYER_GLOOM)) {
            event.addCapability(PLAYER_GLOOM, new Gloom());
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
        if (e.player instanceof EntityPlayerMP)
            setGloomTick((EntityPlayerMP) e.player, 0);
    }

    @SubscribeEvent
    public void inDarkness(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.START)
            return;


        EntityPlayer player = e.player;
        World world = player.getEntityWorld();

        if (!PlayerUtils.isSurvival(player) || !dimensionWhitelist.contains(world.provider.getDimension()))
            return;


        if (e.player instanceof EntityPlayerMP) {
            EntityPlayerMP playermp = (EntityPlayerMP) player;

            if (!world.isRemote) {
                BlockPos head = playermp.getPosition().up();
                int light = world.getLight(head, true);

                if (Loader.isModLoaded("hardcoredarkness") && TimeUtils.isMoonPhase(world, TimeUtils.MoonPhase.New)) {
                    light -= 4;
                }

                int tick = getGloomTime(playermp);
                if (PlayerUtils.isHolding(playermp, gloomOverrideItems))
                    light = 15;
                if (player.isPotionActive(MobEffects.NIGHT_VISION))
                    light = 15;
                if (light <= 0) {
                    incrementGloomTime(playermp);
                } else if (tick != 0) {
                    setGloomTick(playermp, 0);
                }
            }

            if (world.getTotalWorldTime() % 40 == 0) {
                if (world.rand.nextInt(2) == 0) {
                    if (MiscRegistry.PENALTY_HANDLERS.attackedByGrue(player)) {
                        player.attackEntityFrom(BWMDamageSource.gloom, 2);
                    }
                }
            }
        }

        //Client Side
        //Random sounds
        if (world.isRemote) {
            float spooked = MiscRegistry.PENALTY_HANDLERS.getSpooked(player);
            GloomPenalty most = PENALTIES.getMostSevere();

            if (world.rand.nextDouble() <= spooked) {
                player.playSound(SoundEvents.AMBIENT_CAVE, 0.7F, 0.8F + world.rand.nextFloat() * 0.2F);

                if (most != null && (spooked >= (most.getFloat(BWMAttributes.SPOOKED).getValue()))) {
                    if (world.rand.nextInt(5) == 0)
                        player.playSound(sounds.get(world.rand.nextInt(sounds.size())), 0.7F, 0.8F + world.rand.nextFloat() * 0.2F);
                    player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 1, false, false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onFOVUpdate(FOVUpdateEvent event) {
        float spooked = MiscRegistry.PENALTY_HANDLERS.getSpooked(event.getEntity());
        GloomPenalty most = PENALTIES.getMostSevere();
        if (most != null && (spooked >= (most.getFloat(BWMAttributes.SPOOKED).getValue()))) {
            float change = -(getGloomTime(event.getEntity()) / 100000f);
            event.setNewfov(event.getFov() + change);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void syncGloom(String entityId, int gloom) {
        EntityPlayer e = PlayerUtils.getPlayerById(entityId);
        if (e != null) {
            HCGloom.Gloom g = HCGloom.getGloom(e);
            if (g != null) {
                g.setGloom(gloom);
            }
        }
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        dimensionWhitelist = Sets.newHashSet(ArrayUtils.toObject(loadProperty("Gloom Dimension Whitelist", new int[]{0}).setComment("Gloom is only available in these dimensions").get()));
        gloomOverrideItems = StackIngredient.fromStacks(config().loadItemStackArray("Gloom Override Items", getCategory(), "Items in this list will override the gloom effect while held in your hand, this allows support for Dynamic Lightning and similar. Add one item per line  (ex minecraft:torch:0)", new ItemStack[0]));
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        MiscRegistry.PENALTY_HANDLERS.add(PENALTIES = new GloomPenalties(this));
        CapabilityManager.INSTANCE.register(Gloom.class, new CapabilityGloom(), Gloom::new);

    }

    @Override
    public String getDescription() {
        return "Be afraid of the dark...";
    }

    public static class CapabilityGloom implements Capability.IStorage<Gloom> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<Gloom> capability, Gloom instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<Gloom> capability, Gloom instance, EnumFacing side, NBTBase nbt) {
            instance.deserializeNBT((NBTTagCompound) nbt);
        }
    }

    public static class Gloom implements ICapabilitySerializable<NBTTagCompound> {

        private int gloom;

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == GLOOM_CAPABILITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (hasCapability(capability, facing))
                return GLOOM_CAPABILITY.cast(this);
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("gloom", gloom);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            if (nbt.hasKey("gloom")) {
                gloom = nbt.getInteger("gloom");
            }
        }

        public int getGloom() {
            return gloom;
        }

        public void setGloom(int gloom) {
            this.gloom = gloom;
        }
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
