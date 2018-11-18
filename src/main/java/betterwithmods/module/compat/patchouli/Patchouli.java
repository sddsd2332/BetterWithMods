package betterwithmods.module.compat.patchouli;

import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class Patchouli extends Feature {
    public Patchouli() {
    }

    @Override
    public String getDescription() {
        return "Patchouli API registration";
    }

    public static IMultiblock kilnTube, kilnCorner;


    @Override
    public void onInit(FMLInitializationEvent event) {


        PatchouliAPI.IPatchouliAPI api = PatchouliAPI.instance;


        Object[] kilnMatchers = new Object[]{
                'E', api.airMatcher(),
                ' ', api.anyMatcher(),
                'B', api.predicateMatcher(Blocks.BRICK_BLOCK, KilnStructureManager::isKilnBlock)
        };

        kilnTube = api.registerMultiblock(
                new ResourceLocation(ModLib.MODID, "kiln_tube"),
                api.makeMultiblock(new String[][]{
                        {"   ", " B ", "   "},
                        {"   ", "BEB", "   "},
                        {"   ", " B ", "   "},
                        {"   ", " 0 ", "   "},
                }, kilnMatchers))
                .setSymmetrical(true);

        kilnCorner = api.registerMultiblock(
                new ResourceLocation(ModLib.MODID, "kiln_corner"),
                api.makeMultiblock(new String[][]{
                        {"   ", " B ", "   "},
                        {"   ", "BE ", " B "},
                        {"   ", " B ", "   "},
                        {"   ", " 0 ", "   "}
                }, kilnMatchers))
                .setSymmetrical(true);
    }


}

