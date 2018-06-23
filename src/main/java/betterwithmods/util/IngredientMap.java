package betterwithmods.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

import java.util.HashMap;

public class IngredientMap<V> extends HashMap<Ingredient, V> {
    private final V defaultValue;

    public IngredientMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    public V findValue(ItemStack stack) {
        return this.keySet().stream().filter(i -> i.apply(stack)).map(this::get).findFirst().orElse(defaultValue);
    }

    public void put(ItemStack stack, V value) {
        super.put(Ingredient.fromStacks(stack), value);
    }

    public void put(Item item, V value) {
        put(Ingredient.fromItem(item), value);
    }

    public void put(String ore, V value) {
        put(new OreIngredient(ore), value);
    }

    @Override
    public V get(Object o) {
        return getOrDefault(o, defaultValue);
    }
}
