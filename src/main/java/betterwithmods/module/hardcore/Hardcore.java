package betterwithmods.module.hardcore;

import betterwithmods.library.common.modularity.impl.Module;
import betterwithmods.module.general.MoreCobble;
import betterwithmods.module.hardcore.crafting.*;
import betterwithmods.module.hardcore.crafting.brewing.HCBrewing;
import betterwithmods.module.hardcore.creatures.*;
import betterwithmods.module.hardcore.creatures.chicken.HCChickens;
import betterwithmods.module.hardcore.needs.*;
import betterwithmods.module.hardcore.world.*;
import betterwithmods.module.hardcore.world.buckets.HCBuckets;
import betterwithmods.module.hardcore.world.saplings.HCSapling;
import betterwithmods.module.hardcore.world.spawn.HCSpawn;
import betterwithmods.module.hardcore.world.strata.HCStrata;
import betterwithmods.module.hardcore.world.structures.HCStructures;
import betterwithmods.module.hardcore.world.structures.HCVillages;
import betterwithmods.module.hardcore.world.stumping.HCStumping;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class Hardcore extends Module {

    @Override
    public void setup() {


        addFeature(HCMobEquipment.class, "betterwithlib");

        addFeature(new HCDiamond()).recipes();
        addFeature(new HCRedstone()).recipes();
        addFeature(new HCBoating()).recipes();
        addFeature(new HCFishing()).recipes();
        addFeature(new HCTorches()).recipes();
        addFeature(new PowderKegs()).recipes();
        addFeature(new HCStructures()).recipes();
        addFeature(new HCOres()).recipes();

        addFeatures(
                new HCArmor(),

                new HCBeds(),
                new HCBonemeal(),
                new HCBrewing(),
                new HCBuckets(),
                new HCCooking(),
                new HCChickens(),
                new HCEndermen(),
                new HCGloom(),
                new HCGunpowder(),
                new HCHardness(),
                new HCHunting(),
                new HCInfo(),
                new HCInjury(),
                new HCJumping(),
                new HCLumber(),
                new HCMelon(),
                new HCNames(),
                new HCPiles(),
                new HCSeeds(),
                new HCSaw(),
                new HCSheep(),
                new HCSpawn(),
                new HCStumping(),
                new HCTools(),
                new HCVillages(),
                new HCMovement(),
                new MoreCobble(),
                new HCStrata(),
                new HCSapling(),
                new ExplosiveRecipes(),
                new HCBabyZombies(),
                new HCNetherBrick(),
                new HCFighting()
        );


    }

    //Force module to load second to last
    @Override
    public int priority() {
        return Integer.MIN_VALUE + 1;
    }


}
