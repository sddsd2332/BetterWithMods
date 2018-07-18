package betterwithmods.module.compat.immersiveengineering;

import betterwithmods.module.CompatFeature;
import betterwithmods.module.hardcore.needs.HCSeeds;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by primetoxinz on 7/26/17.
 */
@SuppressWarnings("unused")
public class ImmersiveEngineering extends CompatFeature {

    @GameRegistry.ObjectHolder("immersiveengineering:seed")
    public static final Item HEMP_SEED = null;

    public ImmersiveEngineering() {
        super("immersiveengineering");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        HCSeeds.SEED_BLACKLIST.add(new ItemStack(HEMP_SEED));
    }
}
