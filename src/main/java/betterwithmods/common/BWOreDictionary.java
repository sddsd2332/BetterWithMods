package betterwithmods.common;

import betterwithmods.api.util.IBlockVariants;
import betterwithmods.api.util.IVariantProvider;
import betterwithmods.common.blocks.*;
import betterwithmods.common.items.ItemBark;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.variants.BlockVariant;
import betterwithmods.common.registry.variants.WoodVariant;
import betterwithmods.util.InvUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static betterwithmods.api.util.IBlockVariants.EnumBlock.*;

/**
 * Created by primetoxinz on 5/10/17.
 */
public class BWOreDictionary {

    public static List<ItemStack> cropNames;
    public static List<Ore> nuggetNames;
    public static List<Ore> dustNames;
    public static List<Ore> oreNames;
    public static List<Ore> ingotNames;

    public static final List<IBlockVariants> blockVariants = new ArrayList<>();
    public static final List<IVariantProvider> variantProviders = new ArrayList<>();


    public static List<ItemStack> planks;
    public static List<ItemStack> logs;
    public static final Set<IRecipe> logRecipes = Sets.newHashSet();

    public static final HashMultimap<String, String> toolEffectiveOre = HashMultimap.create();


    public static void registerOres() {

        toolEffectiveOre.putAll("axe", Lists.newArrayList("logWood", "plankWood"));
        toolEffectiveOre.putAll("mattock", Lists.newArrayList("stone", "cobblestone"));

        registerOre("book", BWMItems.MANUAL);
        registerOre("dung", ItemMaterial.getStack(ItemMaterial.EnumMaterial.DUNG));
        registerOre("padding", ItemMaterial.getStack(ItemMaterial.EnumMaterial.PADDING));
        registerOre("soap", ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOAP));
        registerOre("archimedesScrew", ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCREW));
        registerOre("filament", ItemMaterial.getStack(ItemMaterial.EnumMaterial.FILAMENT));
        registerOre("element", ItemMaterial.getStack(ItemMaterial.EnumMaterial.ELEMENT));
        registerOre("latchRedstone", ItemMaterial.getStack(ItemMaterial.EnumMaterial.REDSTONE_LATCH));
        registerOre("plateSoulforgedSteel", ItemMaterial.getStack(ItemMaterial.EnumMaterial.PLATE_STEEL));
        registerOre("arrowSoulforgedSteel", ItemMaterial.getStack(ItemMaterial.EnumMaterial.BROADHEAD));
        registerOre("springSoulforgedSteel", ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_SPRING));
        registerOre("gearSoulforgedSteel", ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_GEAR));
        registerOre("gearWood", ItemMaterial.getStack(ItemMaterial.EnumMaterial.WOOD_GEAR));
        registerOre("cropHemp", ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_LEAF));
        registerOre("dyeBrown", ItemMaterial.getStack(ItemMaterial.EnumMaterial.DUNG));
        registerOre("dung", ItemMaterial.getStack(ItemMaterial.EnumMaterial.DUNG));
        registerOre("slimeball", ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE));
        registerOre("glue", ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE));
        registerOre("ingotSoulforgedSteel", ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT));
        registerOre("dustNetherrack", ItemMaterial.getStack(ItemMaterial.EnumMaterial.GROUND_NETHERRACK));
        registerOre("dustHellfire", ItemMaterial.getStack(ItemMaterial.EnumMaterial.HELLFIRE_DUST));
        registerOre("dustSoul", ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_DUST));
        registerOre("ingotConcentratedHellfire", ItemMaterial.getStack(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE));
        registerOre("dustCoal", ItemMaterial.getStack(ItemMaterial.EnumMaterial.COAL_DUST));
        registerOre("dustPotash", ItemMaterial.getStack(ItemMaterial.EnumMaterial.POTASH));
        registerOre("dustWood", ItemMaterial.getStack(ItemMaterial.EnumMaterial.SAWDUST));
        registerOre("dustSulfur", ItemMaterial.getStack(ItemMaterial.EnumMaterial.BRIMSTONE));
        registerOre("dustSaltpeter", ItemMaterial.getStack(ItemMaterial.EnumMaterial.NITER));
        registerOre("nuggetSoulforgedSteel", ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_NUGGET));
        registerOre("foodFlour", BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        registerOre("dustCharcoal", ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        registerOre("foodCocoapowder", ItemMaterial.getStack(ItemMaterial.EnumMaterial.COCOA_POWDER));
        registerOre("dustCarbon", ItemMaterial.getStack(ItemMaterial.EnumMaterial.COAL_DUST), ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        registerOre("dustCoal", ItemMaterial.getStack(ItemMaterial.EnumMaterial.COAL_DUST));
        registerOre("dustCharcoal", ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        registerOre("gemNetherCoal", ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHERCOAL));
        registerOre("coal", new ItemStack(Items.COAL, 1, 0), new ItemStack(Items.COAL, 1, 1));
        registerOre("materialNetherSludge", ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHER_SLUDGE));
        registerOre("foodChocolatebar", new ItemStack(BWMItems.CHOCOLATE));
        registerOre("chainmail", ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHAIN_MAIL));

        registerOre("blockHardenedNetherClay", BlockAesthetic.getStack(BlockAesthetic.EnumType.NETHERCLAY));
        registerOre("blockConcentratedHellfire", BlockAesthetic.getStack(BlockAesthetic.EnumType.HELLFIRE));
        registerOre("blockBarrel", new ItemStack(BWMBlocks.BARREL));
        //Added bark subtype entries for Roots compatibility
        registerOre("barkWood", ItemBark.getBarks(1));
        registerOre("barkWood", new ItemStack(BWMItems.BARK, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("barkOak", new ItemStack(BWMItems.BARK, 1, 0));
        registerOre("barkSpruce", new ItemStack(BWMItems.BARK, 1, 1));
        registerOre("barkBirch", new ItemStack(BWMItems.BARK, 1, 2));
        registerOre("barkJungle", new ItemStack(BWMItems.BARK, 1, 3));
        registerOre("barkAcacia", new ItemStack(BWMItems.BARK, 1, 4));
        registerOre("barkDarkOak", new ItemStack(BWMItems.BARK, 1, 5));
        registerOre("barkBlood", new ItemStack(BWMItems.BARK, 1, 6));

        registerOre("hideTanned", ItemMaterial.getStack(ItemMaterial.EnumMaterial.TANNED_LEATHER));
        registerOre("hideTanned", ItemMaterial.getStack(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT));
        registerOre("hideBelt", ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_BELT));
        registerOre("hideScoured", ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER));
        registerOre("hideStrap", ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_STRAP));
        registerOre("leather", ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_CUT));

        registerOre("fiberHemp", ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_FIBERS));
        registerOre("fabricHemp", ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_CLOTH));

        registerOre("ingotDiamond", ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT));
        registerOre("nuggetDiamond", ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_NUGGET));

        registerOre("listAllmeat", Items.PORKCHOP, Items.BEEF, Items.CHICKEN, Items.FISH, Items.MUTTON, BWMItems.MYSTERY_MEAT);
        registerOre("listAllmeat", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
        registerOre("listAllmeatcooked", Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_FISH, Items.COOKED_MUTTON, Items.COOKED_RABBIT, BWMItems.COOKED_MYSTERY_MEAT);
        registerOre("listAllmeatcooked", new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
        registerOre("foodStewMeat", Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.COOKED_FISH, Items.COOKED_MUTTON, BWMItems.COOKED_MYSTERY_MEAT);
        registerOre("foodStewMeat", new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));

        registerOre("tallow", ItemMaterial.getStack(ItemMaterial.EnumMaterial.TALLOW));

        registerOre("pile", new ItemStack(BWMItems.DIRT_PILE), new ItemStack(BWMItems.SAND_PILE), new ItemStack(BWMItems.RED_SAND_PILE), new ItemStack(BWMItems.GRAVEL_PILE));
        registerOre("pileDirt", new ItemStack(BWMItems.DIRT_PILE));
        registerOre("pileSand", new ItemStack(BWMItems.SAND_PILE), new ItemStack(BWMItems.RED_SAND_PILE));
        registerOre("pileRedSand", new ItemStack(BWMItems.RED_SAND_PILE));
        registerOre("pileGravel", new ItemStack(BWMItems.GRAVEL_PILE));


        registerOre("treeSapling", new ItemStack(BWMBlocks.BLOOD_SAPLING));
        registerOre("treeLeaves", new ItemStack(BWMBlocks.BLOOD_LEAVES));
        registerOre("logWood", new ItemStack(BWMBlocks.BLOOD_LOG));
        registerOre("blockNetherSludge", new ItemStack(BWMBlocks.NETHER_CLAY));

        registerOre("slats", new ItemStack(BWMBlocks.OAK_SLATS), new ItemStack(BWMBlocks.SPRUCE_SLATS), new ItemStack(BWMBlocks.BIRCH_SLATS), new ItemStack(BWMBlocks.JUNGLE_SLATS), new ItemStack(BWMBlocks.ACACIA_SLATS), new ItemStack(BWMBlocks.DARK_OAK_SLATS));
        registerOre("grates", new ItemStack(BWMBlocks.OAK_GRATE), new ItemStack(BWMBlocks.SPRUCE_GRATE), new ItemStack(BWMBlocks.BIRCH_GRATE), new ItemStack(BWMBlocks.JUNGLE_GRATE), new ItemStack(BWMBlocks.ACACIA_GRATE), new ItemStack(BWMBlocks.DARK_OAK_GRATE));
        registerOre("wicker", new ItemStack(BWMBlocks.WICKER));

        registerOre("blockPlanter", BlockPlanter.BLOCKS.values());
        registerOre("blockVase", BlockVase.BLOCKS.values());
        registerOre("blockCandle", BlockCandle.BLOCKS.values());
        registerOre("cobblestone", BlockCobble.BLOCKS);

        registerOre("stickWood", new ItemStack(BWMBlocks.SHAFT));
        registerOre("blockWindChime", BlockChime.BLOCKS);
        registerOre("blockSoulUrn", new ItemStack(BWMBlocks.SOUL_URN));

        registerOre("dustBlaze", new ItemStack(Items.BLAZE_POWDER));

        registerOre("foodDonut", BWMItems.DONUT);

        registerOre("meatPork", Items.PORKCHOP, Items.COOKED_PORKCHOP);
        registerOre("meatBeef", Items.BEEF, Items.COOKED_BEEF);
        registerOre("meatMutton", Items.MUTTON, Items.COOKED_MUTTON);
        registerOre("meatChicken", Items.CHICKEN, Items.COOKED_CHICKEN);
        registerOre("meatRotten", Items.ROTTEN_FLESH);
        registerOre("meatFish",
                new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()),
                new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
                new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.COD.getMetadata()),
                new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.SALMON.getMetadata())
        );

        registerOre("cookedPotato", Items.BAKED_POTATO);
        registerOre("cookedCarrot", Items.CARROT);

        registerOre("listAllExplosives", new ItemStack(Blocks.TNT));
        registerOre("listAllExplosives", new ItemStack(Items.GUNPOWDER));
        registerOre("listAllExplosives", new ItemStack(BWMItems.DYNAMITE));
        registerOre("listAllExplosives", new ItemStack(BWMBlocks.MINING_CHARGE));
        registerOre("listAllExplosives", BlockAesthetic.getStack(BlockAesthetic.EnumType.HELLFIRE));
        registerOre("listAllExplosives", ItemMaterial.getStack(ItemMaterial.EnumMaterial.BLASTING_OIL));
        registerOre("listAllExplosives", ItemMaterial.getStack(ItemMaterial.EnumMaterial.HELLFIRE_DUST));
        registerOre("listAllExplosives", ItemMaterial.getStack(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE));


        registerOre("seed", new ItemStack(BWMBlocks.HEMP), new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.MELON_SEEDS), new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Items.BEETROOT_SEEDS));

    }


    private static IRecipe getLogPlankRecipe(ItemStack log) {
        if (log.isEmpty())
            return null;
        for (IRecipe recipe : CraftingManager.REGISTRY) {
            if (InvUtils.applyIngredients(recipe.getIngredients(), log)) {
                if (isPlank(recipe.getRecipeOutput())) {
                    return recipe;
                }
            }
        }
        return null;
    }

    private static boolean isPlank(ItemStack output) {
        return BWOreDictionary.listContains(output, OreDictionary.getOres("plankWood"));
    }

    public static void registerOre(String ore, Collection<? extends Block> stacks) {
        stacks.forEach(s -> OreDictionary.registerOre(ore, s));
    }

    public static void registerOre(String ore, List<ItemStack> stacks) {
        stacks.forEach(s -> OreDictionary.registerOre(ore, s));
    }

    public static void registerOre(String ore, ItemStack... items) {
        for (ItemStack i : items)
            OreDictionary.registerOre(ore, i);
    }

    public static void registerOre(String ore, Item... items) {
        for (Item item : items)
            registerOre(ore, new ItemStack(item));
    }

    public static void oreGathering() {
        nuggetNames = getOreIngredients("nugget");
        dustNames = getOreIngredients("dust");
        oreNames = getOreIngredients("ore");
        ingotNames = getOreIngredients("ingot");
        cropNames = getOreNames("crop");
        blockVariants.addAll(Lists.newArrayList(
                BlockVariant.builder()
                        .addVariant(BLOCK, new ItemStack(Blocks.COBBLESTONE))
                        .addVariant(STAIR, new ItemStack(Blocks.STONE_STAIRS))
                        .addVariant(WALL, new ItemStack(Blocks.COBBLESTONE_WALL)),
                BlockVariant.builder()
                        .addVariant(BLOCK, new ItemStack(Blocks.BRICK_BLOCK))
                        .addVariant(STAIR, new ItemStack(Blocks.BRICK_STAIRS)),
                BlockVariant.builder()
                        .addVariant(BLOCK, new ItemStack(Blocks.QUARTZ_BLOCK))
                        .addVariant(STAIR, new ItemStack(Blocks.QUARTZ_STAIRS)),
                BlockVariant.builder()
                        .addVariant(BLOCK, new ItemStack(Blocks.SANDSTONE))
                        .addVariant(STAIR, new ItemStack(Blocks.SANDSTONE_STAIRS)),
                BlockVariant.builder()
                        .addVariant(BLOCK, new ItemStack(Blocks.RED_SANDSTONE))
                        .addVariant(STAIR, new ItemStack(Blocks.RED_SANDSTONE_STAIRS)),
                BlockVariant.builder()
                        .addVariant(BLOCK, new ItemStack(Blocks.PURPUR_BLOCK))
                        .addVariant(STAIR, new ItemStack(Blocks.PURPUR_STAIRS)),
                BlockVariant.builder()
                        .addVariant(BLOCK, new ItemStack(Blocks.NETHER_BRICK))
                        .addVariant(STAIR, new ItemStack(Blocks.NETHER_BRICK_STAIRS))
                        .addVariant(FENCE, new ItemStack(Blocks.NETHER_BRICK_FENCE)),
                BlockVariant.builder()
                        .addVariant(LOG, new ItemStack(Blocks.LOG, 1, 0))
                        .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, 0))
                        .addVariant(FENCE, new ItemStack(Blocks.OAK_FENCE))
                        .addVariant(FENCE_GATE, new ItemStack(Blocks.OAK_FENCE_GATE))
                        .addVariant(STAIR, new ItemStack(Blocks.OAK_STAIRS)),
                WoodVariant.builder()
                        .addVariant(LOG, new ItemStack(Blocks.LOG, 1, 1))
                        .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, 1))
                        .addVariant(FENCE, new ItemStack(Blocks.SPRUCE_FENCE))
                        .addVariant(FENCE_GATE, new ItemStack(Blocks.SPRUCE_FENCE_GATE))
                        .addVariant(STAIR, new ItemStack(Blocks.SPRUCE_STAIRS)),
                WoodVariant.builder()
                        .addVariant(LOG, new ItemStack(Blocks.LOG, 1, 2))
                        .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, 2))
                        .addVariant(FENCE, new ItemStack(Blocks.BIRCH_FENCE))
                        .addVariant(FENCE_GATE, new ItemStack(Blocks.BIRCH_FENCE_GATE))
                        .addVariant(STAIR, new ItemStack(Blocks.BIRCH_STAIRS)),
                WoodVariant.builder()
                        .addVariant(LOG, new ItemStack(Blocks.LOG, 1, 3))
                        .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, 3))
                        .addVariant(FENCE, new ItemStack(Blocks.JUNGLE_FENCE))
                        .addVariant(FENCE_GATE, new ItemStack(Blocks.JUNGLE_FENCE_GATE))
                        .addVariant(STAIR, new ItemStack(Blocks.JUNGLE_STAIRS)),
                WoodVariant.builder()
                        .addVariant(LOG, new ItemStack(Blocks.LOG2, 1, 0))
                        .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, 4))
                        .addVariant(FENCE, new ItemStack(Blocks.ACACIA_FENCE))
                        .addVariant(FENCE_GATE, new ItemStack(Blocks.ACACIA_FENCE_GATE))
                        .addVariant(STAIR, new ItemStack(Blocks.ACACIA_STAIRS)),
                WoodVariant.builder()
                        .addVariant(LOG, new ItemStack(Blocks.LOG2, 1, 1))
                        .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, 5))
                        .addVariant(FENCE, new ItemStack(Blocks.DARK_OAK_FENCE))
                        .addVariant(FENCE_GATE, new ItemStack(Blocks.DARK_OAK_FENCE_GATE))
                        .addVariant(STAIR, new ItemStack(Blocks.DARK_OAK_STAIRS)),
                WoodVariant.builder()
                        .addVariant(LOG, new ItemStack(BWMBlocks.BLOOD_LOG))
                        .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, 3))
                        .addVariant(IBlockVariants.EnumBlock.SAWDUST, ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_DUST))
        ));
    }

    public static void findLogRecipes() {
        BWOreDictionary.logs = OreDictionary.getOres("logWood").stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        for (ItemStack log : logs) {
            Item logItem = log.getItem();
            NonNullList<ItemStack> listSubItems = NonNullList.create();
            log.getItem().getSubItems(logItem.getCreativeTab(), listSubItems);
            for (ItemStack subLog : listSubItems) {
                IRecipe recipe = getLogPlankRecipe(subLog);
                if (recipe != null) {
                    ItemStack plank = recipe.getRecipeOutput();
                    if (!plank.isEmpty()) {
                        logRecipes.add(recipe);
                        if (!isWoodRegistered(log)) {
                            blockVariants.add(WoodVariant.builder().addVariant(LOG, subLog).addVariant(BLOCK, plank));
                        }
                    }
                }
            }
        }
    }

    public static boolean isWoodRegistered(ItemStack stack) {
        return blockVariants.stream().anyMatch(wood -> wood.getVariant(LOG, 1).isItemEqual(stack));
    }

    public static List<ItemStack> getOreNames(String prefix) {
        return Arrays.stream(OreDictionary.getOreNames()).filter(Objects::nonNull).filter(n -> n.startsWith(prefix)).map(OreDictionary::getOres).filter(o -> !o.isEmpty()).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static List<ItemStack> getItems(List<Ore> ores) {
        return ores.stream().flatMap(o -> o.getOres().stream()).collect(Collectors.toList());
    }

    public static List<Ore> getOreIngredients(String prefix) {
        return Arrays.stream(OreDictionary.getOreNames()).filter(Objects::nonNull).filter(n -> n.startsWith(prefix)).map(n -> new Ore(prefix, n)).collect(Collectors.toList());
    }

    public static boolean isOre(ItemStack stack, String ore) {
        return listContains(stack, OreDictionary.getOres(ore));
    }

    public static boolean listContains(ItemStack check, List<ItemStack> list) {
        if (list != null) {
            if (list.isEmpty()) return false;
            for (ItemStack item : list) {
                if (ItemStack.areItemsEqual(check, item) || (check.getItem() == item.getItem() && item.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                    return !item.hasTagCompound() || ItemStack.areItemStackTagsEqual(check, item);
                }
            }
        }
        return false;
    }

    public static List<String> getOres(ItemStack stack) {
        return IntStream.of(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName).collect(Collectors.toList());
    }

    public static boolean hasPrefix(ItemStack stack, String suffix) {
        return listContains(stack, getOreNames(suffix));
    }

    public static boolean isToolForOre(String tool, ItemStack stack) {
        return toolEffectiveOre.get(tool).stream().anyMatch(getOres(stack)::contains);
    }

    public static IBlockVariants getVariantFromState(IBlockVariants.EnumBlock variant, IBlockState state) {
        ItemStack stack = BWMRecipes.getStackFromState(state);
        IBlockVariants blockVariant = null;
        if (!stack.isEmpty()) {
            blockVariant = blockVariants.stream().filter(w -> InvUtils.matches(w.getVariant(variant, 1), stack)).findFirst().orElse(null);
        }
        if (blockVariant == null) {
            for (IVariantProvider provider : variantProviders) {
                if (provider.match(state)) {
                    blockVariant = provider.getVariant(variant, state);
                    break;
                }
            }
        }
        return blockVariant;
    }

    public static class Ore extends OreIngredient {
        private final String prefix;
        private final String ore;

        public Ore(String prefix, String ore) {
            super(ore);
            this.prefix = prefix;
            this.ore = ore;
        }

        public List<ItemStack> getOres() {
            return OreDictionary.getOres(ore);
        }

        public String getOre() {
            return ore;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getSuffix() {
            return ore.substring(getPrefix().length());
        }

    }
}
