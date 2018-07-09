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

public class ConstructKilnTrigger implements ICriterionTrigger<ConstructKilnTrigger.Instance> {

    private static final ResourceLocation ID = new ResourceLocation(BWMod.MODID,"construct_kiln");

    private final Map<PlayerAdvancements, ConstructKilnTrigger.Listeners> listeners = Maps.newHashMap();

    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    public void addListener(@Nonnull PlayerAdvancements playerAdvancementsIn, @Nonnull ICriterionTrigger.Listener<ConstructKilnTrigger.Instance> listener) {
        ConstructKilnTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners == null) {
            listeners = new ConstructKilnTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, listeners);
        }
        listeners.add(listener);
    }

    public void removeListener(@Nonnull PlayerAdvancements playerAdvancementsIn, @Nonnull ICriterionTrigger.Listener<ConstructKilnTrigger.Instance> listener) {
        ConstructKilnTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

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
    public ConstructKilnTrigger.Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
        return new ConstructKilnTrigger.Instance();
    }


    public void trigger(EntityPlayerMP player) {
        ConstructKilnTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger();
        }
    }


    public static class Instance extends AbstractCriterionInstance {
        public Instance() {
            super(ConstructKilnTrigger.ID);
        }

        public boolean test() {
            return true;
        }
    }


    static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set<ICriterionTrigger.Listener<ConstructKilnTrigger.Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<ConstructKilnTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<ConstructKilnTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger() {
            List<ICriterionTrigger.Listener<ConstructKilnTrigger.Instance>> list = null;
            for (ICriterionTrigger.Listener<ConstructKilnTrigger.Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test()) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }

            if (list != null) {
                for (ICriterionTrigger.Listener<ConstructKilnTrigger.Instance> listener1 : list) {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}
