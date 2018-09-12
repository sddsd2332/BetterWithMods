package betterwithmods.module.tweaks;

import betterwithmods.module.Module;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class Tweaks extends Module {

    @Override
    public void addFeatures() {
        addFeature(new FastStick().recipes());
        addFeature(new CheaperAxes().recipes());
        addFeature(new DetectorRail().recipes());
        addFeature(new SaddleRecipe().recipes());
        addFeature(new WoolArmor().recipes());
        addFeature(new AxeLeaves());
        addFeature(new CreeperShearing());
        addFeature(new Dung());
        addFeature(new EasyBreeding());
        addFeature(new MoreTempting());
        addFeature(new EggDrops());
        addFeature(new EquipmentDrop());
        addFeature(new ImprovedFlee());
        addFeature(new HeadDrops());
        addFeature(new KilnCharcoal());
        addFeature(new KilnSmelting());
        addFeature(new MobSpawning());
        addFeature(new MossGeneration());
        addFeature(new RenewableEndstone());
        addFeature(new RSBlockGlow());
        addFeature(new Sinkholes());
        addFeature(new MysteryMeat());
        addFeature(new GrassPath());
        addFeature(new DarkQuartz());
        addFeature(new CactusSkeleton());
        addFeature(new BatWings());
        addFeature(new FoodPoisoning());
        addFeature(new Notes());
        addFeature(new MineshaftGeneration());
        addFeature(new VisibleStorms());
        addFeature(new LongBoi());
        addFeature(new MobEating());
        addFeature(new LlamaDrops());
        addFeature(new BabyJumping());
        addFeature(new EnchantmentTooltip());
        addFeature(new NoSkeletonTrap());
        addFeature(new SilverfishClay());
        addFeature(new AnimalBirth());
        addFeature(new HopperMinecarts());
        addFeature(new FeedWolfchop());

    }

}
