package betterwithmods.module.hardcore;

import betterwithmods.BWMod;
import betterwithmods.client.gui.GuiStatus;
import betterwithmods.module.Module;
import betterwithmods.module.general.MoreCobble;
import betterwithmods.module.hardcore.beacons.HCBeacons;
import betterwithmods.module.hardcore.crafting.*;
import betterwithmods.module.hardcore.creatures.*;
import betterwithmods.module.hardcore.creatures.chicken.HCChickens;
import betterwithmods.module.hardcore.needs.*;
import betterwithmods.module.hardcore.needs.hunger.HCHunger;
import betterwithmods.module.hardcore.world.*;
import betterwithmods.module.hardcore.world.saplings.HCSapling;
import betterwithmods.module.hardcore.world.spawn.HCSpawn;
import betterwithmods.module.hardcore.world.strata.HCStrata;
import betterwithmods.module.hardcore.world.structures.HCStructures;
import betterwithmods.module.hardcore.world.structures.HCVillages;
import betterwithmods.module.hardcore.world.stumping.HCStumping;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class Hardcore extends Module {
//
//    @Override
//    public void addCompatFeatures() {
//        registerCompatFeature("applecore", HCHunger.class.getName());
//        registerCompatFeature("hardcorebuoy", HCBuoy.class.getName());
//        registerCompatFeature("betterwithlib", HCMobEquipment.class.getName());
//    }


    @Override
    public void setup() {

        addFeature(HCHunger.class, "applecore");
        addFeature(HCMobEquipment.class, "betterwithlib");

        addFeature(new HCDiamond().recipes());
        addFeature(new HCRedstone().recipes());
        addFeature(new HCBoating().recipes());
        addFeature(new HCFishing().recipes());
        addFeature(new HCTorches().recipes());
        addFeature(new PowderKegs().recipes());

        addFeature(new HCArmor());
        addFeature(new HCBeacons());
        addFeature(new HCBeds());
        addFeature(new HCBonemeal());
        addFeature(new HCBrewing());
        addFeature(new HCBuckets());
        addFeature(new HCCooking());
        addFeature(new HCChickens());
        addFeature(new HCEndermen());
        addFeature(new HCGloom());
        addFeature(new HCGunpowder());
        addFeature(new HCHardness());
        addFeature(new HCHunting());
        addFeature(new HCInfo());
        addFeature(new HCInjury());
        addFeature(new HCJumping());
        addFeature(new HCLumber());
        addFeature(new HCMelon());
        addFeature(new HCNames());
        addFeature(new HCOres());
        addFeature(new HCPiles());
        addFeature(new HCSeeds());
        addFeature(new HCSaw());
        addFeature(new HCSheep());
        addFeature(new HCSpawn());
        addFeature(new HCStructures());
        addFeature(new HCStumping());
        addFeature(new HCTools());

        addFeature(new HCVillages());
        addFeature(new HCMovement());
        addFeature(new MoreCobble());
        addFeature(new HCDeadweight());
        addFeature(new HCEnchanting());
        addFeature(new HCStrata());
        addFeature(new HCFurnace());
        addFeature(new HCSapling());
        addFeature(new ExplosiveRecipes());
        addFeature(new HCBabyZombies());
        addFeature(new HCNetherBrick());
        addFeature(new HCFighting());

        //Disabled by default
        addFeature(new HCHopper());
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);
    }


    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        GuiStatus.isGloomLoaded = BWMod.MODULE_LOADER.isFeatureEnabled(HCGloom.class);
        GuiStatus.isHungerLoaded = BWMod.MODULE_LOADER.isFeatureEnabled(HCHunger.class);
        GuiStatus.isInjuryLoaded = BWMod.MODULE_LOADER.isFeatureEnabled(HCInjury.class);
    }

}
