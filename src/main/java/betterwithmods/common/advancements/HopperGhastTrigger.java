package betterwithmods.common.advancements;

import betterwithmods.BWMod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by michaelepps on 7/19/18.
 */
public class HopperGhastTrigger implements ICriterionTrigger<HopperGhastTrigger.Instance> {

    private static final ResourceLocation ID = new ResourceLocation(BWMod.MODID,"spawn_hopper_friend");

    private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    public void addListener(@Nonnull PlayerAdvancements playerAdvancementsIn, @Nonnull ICriterionTrigger.Listener<HopperGhastTrigger.Instance> listener) {
        HopperGhastTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners == null) {
            listeners = new HopperGhastTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, listeners);
        }
        listeners.add(listener);
    }

    public void removeListener(@Nonnull PlayerAdvancements playerAdvancementsIn, @Nonnull ICriterionTrigger.Listener<HopperGhastTrigger.Instance> listener) {
        HopperGhastTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners != null) {
            listeners.remove(listener);

            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(@Nonnull PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }


    @Nonnull
    public HopperGhastTrigger.Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
        return new HopperGhastTrigger.Instance();
    }


    public void trigger(EntityPlayerMP player) {
        HopperGhastTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger();
        }
    }


    public static class Instance extends AbstractCriterionInstance {
        public Instance() {
            super(HopperGhastTrigger.ID);
        }

        public boolean test() {
            return true;
        }
    }


    static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<HopperGhastTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<HopperGhastTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger() {
            List<Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<HopperGhastTrigger.Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test()) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }

            if (list != null) {
                for (ICriterionTrigger.Listener<HopperGhastTrigger.Instance> listener1 : list) {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}