package betterwithmods.common.registry.hopper.filters;

import betterwithmods.api.tile.IHopperFilter;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 11/13/16
 */
public class HopperFilters {

    private final Map<ResourceLocation, IHopperFilter> FILTERS = Maps.newHashMap();

    public void addFilter(IHopperFilter filter) {
        FILTERS.put(filter.getName(), filter);
    }

    public IHopperFilter getFilter(ResourceLocation name) {
        return FILTERS.getOrDefault(name,HopperFilter.NONE);
    }

    public IHopperFilter getFilter(String name) {
        return getFilter(new ResourceLocation(name));
    }

    public IHopperFilter getFilter(ItemStack stack) {
        if (stack.isEmpty())
            return HopperFilter.NONE;
        return FILTERS.values().stream().filter(filter -> filter.getFilter().apply(stack)).findFirst().orElse(new SelfHopperFilter(stack));
    }

}
