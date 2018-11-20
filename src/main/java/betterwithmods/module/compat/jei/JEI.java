package betterwithmods.module.compat.jei;

import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.api.recipe.output.impl.ChanceOutput;
import betterwithmods.api.recipe.output.impl.RandomOutput;
import betterwithmods.api.recipe.output.impl.StackOutput;
import betterwithmods.client.gui.GuiSteelAnvil;
import betterwithmods.client.gui.bulk.GuiCauldron;
import betterwithmods.client.gui.bulk.GuiCrucible;
import betterwithmods.client.gui.bulk.GuiFilteredHopper;
import betterwithmods.client.gui.bulk.GuiMill;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.container.anvil.ContainerSteelAnvil;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.anvil.AnvilRecipe;
import betterwithmods.common.registry.block.recipe.KilnRecipe;
import betterwithmods.common.registry.block.recipe.SawRecipe;
import betterwithmods.common.registry.block.recipe.TurntableRecipe;
import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import betterwithmods.common.registry.crafting.ToolBaseRecipe;
import betterwithmods.common.registry.crafting.ToolDamageRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.common.registry.hopper.recipes.HopperRecipe;
import betterwithmods.module.compat.jei.category.*;
import betterwithmods.module.compat.jei.ingredient.OutputHelper;
import betterwithmods.module.compat.jei.ingredient.OutputRenderer;
import betterwithmods.module.compat.jei.wrapper.*;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.miniblocks.MiniBlocks;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import mezz.jei.Internal;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.gui.Focus;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import mezz.jei.startup.StackHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IShapedRecipe;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

@mezz.jei.api.JEIPlugin
public class JEI implements IModPlugin {
    public static final List<Class<? extends IOutput>> ALL_OUTPUTS = Lists.newArrayList();
    public static IJeiHelpers HELPER;
    public static IJeiRuntime JEI_RUNTIME;

    static {
        ALL_OUTPUTS.add(IOutput.class);
        ALL_OUTPUTS.add(StackOutput.class);
        ALL_OUTPUTS.add(RandomOutput.class);
        ALL_OUTPUTS.add(ChanceOutput.class);
    }

    public static void doAllOutputs(Consumer<Class<? extends IOutput>> consumer) {
        ALL_OUTPUTS.forEach(consumer);
    }

    public static void showRecipe(Ingredient ingredient) {
        ItemStack stack = Lists.newArrayList(ingredient.getMatchingStacks()).stream().findFirst().orElse(ItemStack.EMPTY);
        if (stack.isEmpty())
            return;
        IFocus<?> focus = new Focus<Object>(IFocus.Mode.OUTPUT, stack);
        JEI.JEI_RUNTIME.getRecipesGui().show(focus);
    }

    private static String getHeatUID(String base, int heat) {
        if (heat == BWMHeatRegistry.UNSTOKED_HEAT) {
            return base;
        } else if (heat == BWMHeatRegistry.STOKED_HEAT) {
            return String.format("%s.%s", base, "stoked");
        } else {
            return String.format("%s.%s", base, heat);
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        if (JEI_RUNTIME == null) {
            JEI_RUNTIME = jeiRuntime;
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        final IJeiHelpers helpers = reg.getJeiHelpers();
        final IGuiHelper guiHelper = helpers.getGuiHelper();

        for (int heat : BWMHeatRegistry.allHeatLevels()) {
            reg.addRecipeCategories(
                    new CookingPotRecipeCategory(guiHelper, getHeatUID(JEILib.CAULDRON_UID, heat)),
                    new CookingPotRecipeCategory(guiHelper, getHeatUID(JEILib.CRUCIBLE_UID, heat)),
                    new KilnRecipeCategory(guiHelper, getHeatUID(JEILib.KILN_UID, heat))
            );
        }

        reg.addRecipeCategories(
                new MillRecipeCategory(guiHelper),
                new SawRecipeCategory(guiHelper),
                new TurntableRecipeCategory(guiHelper),
                new HopperRecipeCategory(guiHelper),
                new SteelAnvilRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(getAllMiniBlocks());
        subtypeRegistry.useNbtForSubtypes(BWMItems.BARK);
    }

    private Item[] getAllMiniBlocks() {
        return MiniBlocks.MINI_MATERIAL_BLOCKS.values().stream().map(HashMap::values).flatMap(Collection::stream).map(Item::getItemFromBlock).toArray(Item[]::new);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
        StackHelper stackHelper = Internal.getStackHelper();
        doAllOutputs(clazz -> registry.register(clazz, Collections.emptySet(), new OutputHelper<>(stackHelper), new OutputRenderer<>()));
    }

    @Override
    public void register(@Nonnull IModRegistry reg) {
        HELPER = reg.getJeiHelpers();

        registerHeatBasedRecipes(reg);

        reg.handleRecipes(MillRecipe.class, r -> new BulkRecipeWrapper<>(HELPER, r, 3), JEILib.MILLSTONE_UID);
        reg.handleRecipes(SawRecipe.class, r -> new BlockRecipeWrapper<>(HELPER, r, 3), JEILib.SAW_UID);
        reg.handleRecipes(TurntableRecipe.class, recipe -> new TurntableRecipeWrapper(HELPER, recipe), JEILib.TURNTABLE_UID);
        reg.handleRecipes(HopperRecipe.class, recipe -> new HopperRecipeWrapper(HELPER, recipe), JEILib.HOPPER_UID);
        reg.handleRecipes(AnvilRecipe.class, recipe -> {
            IRecipe r = recipe.getRecipe();
            if (r instanceof IShapedRecipe) {
                return new ShapedAnvilRecipeWrapper(HELPER, (IShapedRecipe) r);
            } else {
                return new ShapelessRecipeWrapper<>(HELPER, r);
            }
        }, JEILib.ANVIL_UID);
        reg.handleRecipes(ToolBaseRecipe.class, recipe -> new ShapelessRecipeWrapper<>(HELPER, recipe), VanillaRecipeCategoryUid.CRAFTING);
        reg.handleRecipes(ToolDamageRecipe.class, recipe -> new ShapelessRecipeWrapper<>(HELPER, recipe), VanillaRecipeCategoryUid.CRAFTING);

        reg.addRecipes(RecipeRegistry.MILLSTONE.getValuesCollection(), JEILib.MILLSTONE_UID);
        reg.addRecipes(RecipeRegistry.WOOD_SAW.getDisplayRecipes(), JEILib.SAW_UID);
        reg.addRecipes(RecipeRegistry.TURNTABLE.getDisplayRecipes(), JEILib.TURNTABLE_UID);
        reg.addRecipes(RecipeRegistry.FILTERED_HOPPER.getDisplayRecipes(), JEILib.HOPPER_UID);
        reg.addRecipes(RecipeRegistry.ANVIL.getDisplayRecipes(), JEILib.ANVIL_UID);

        reg.addRecipeCatalyst(new ItemStack(BWMBlocks.MILLSTONE), JEILib.MILLSTONE_UID);
        reg.addRecipeCatalyst(new ItemStack(BWMBlocks.FILTERED_HOPPER), JEILib.HOPPER_UID);
        reg.addRecipeCatalyst(new ItemStack(BWMBlocks.TURNTABLE), JEILib.TURNTABLE_UID);

        reg.addRecipeCatalyst(new ItemStack(BWMBlocks.SAW), JEILib.SAW_UID);
        reg.addRecipeCatalyst(new ItemStack(BWMBlocks.STEEL_ANVIL), JEILib.ANVIL_UID);

        reg.addRecipeClickArea(GuiMill.class, 81, 19, 14, 14, JEILib.MILLSTONE_UID);
        reg.addRecipeClickArea(GuiSteelAnvil.class, 88, 41, 28, 23, JEILib.ANVIL_UID);
        reg.addRecipeClickArea(GuiFilteredHopper.class, 81, 19, 14, 14, JEILib.HOPPER_UID);

        registerAnvil(reg);

        IRecipeTransferRegistry recipeTransferRegistry = reg.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(ContainerSteelAnvil.class, JEILib.ANVIL_UID, 1, 16, 17, 36);
        recipeTransferRegistry.addRecipeTransferHandler(new AnvilCraftingTransfer());

    }

    private void registerAnvil(IModRegistry reg) {
        List<ItemStack> tools = Lists.newArrayList(new ItemStack(BWMItems.STEEL_AXE), new ItemStack(BWMItems.STEEL_BATTLEAXE), new ItemStack(BWMItems.STEEL_BOOTS), new ItemStack(BWMItems.STEEL_CHEST), new ItemStack(BWMItems.STEEL_HELMET), new ItemStack(BWMItems.STEEL_HOE), new ItemStack(BWMItems.STEEL_MATTOCK),
                new ItemStack(BWMItems.STEEL_PANTS), new ItemStack(BWMItems.STEEL_PICKAXE), new ItemStack(BWMItems.STEEL_SHOVEL), new ItemStack(BWMItems.STEEL_SWORD));
        IVanillaRecipeFactory v = reg.getJeiHelpers().getVanillaRecipeFactory();
        for (ItemStack stack : tools) {
            ItemStack dam1 = stack.copy();
            dam1.setItemDamage(dam1.getMaxDamage());
            ItemStack dam2 = stack.copy();
            dam2.setItemDamage(dam2.getMaxDamage() * 3 / 4);
            ItemStack dam3 = stack.copy();
            dam3.setItemDamage(dam3.getMaxDamage() * 2 / 4);

            v.createAnvilRecipe(dam1, Collections.singletonList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT)), Collections.singletonList(dam2));
            v.createAnvilRecipe(dam2, Collections.singletonList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT)), Collections.singletonList(dam3));
        }

    }

    private void registerHeatBasedRecipes(@Nonnull IModRegistry reg) {
        Set<String> cauldron = Sets.newHashSet(), crucible = Sets.newHashSet(), kiln = Sets.newHashSet();
        for (int heat : BWMHeatRegistry.allHeatLevels()) {
            String cauldronUID = getHeatUID(JEILib.CAULDRON_UID, heat);
            String crucibleUID = getHeatUID(JEILib.CRUCIBLE_UID, heat);
            String kilnUID = getHeatUID(JEILib.KILN_UID, heat);
            cauldron.add(cauldronUID);
            crucible.add(crucibleUID);
            kiln.add(kilnUID);

            reg.handleRecipes(CauldronRecipe.class, recipe -> new BulkRecipeWrapper<>(HELPER, recipe, 9), cauldronUID);
            reg.handleRecipes(CrucibleRecipe.class, recipe -> new BulkRecipeWrapper<>(HELPER, recipe, 9), crucibleUID);
            reg.handleRecipes(KilnRecipe.class, recipe -> new KilnRecipeWrapper(HELPER, recipe), kilnUID);

            reg.addRecipes(RecipeRegistry.CAULDRON.getRecipesForHeat(heat), cauldronUID);
            reg.addRecipes(RecipeRegistry.CRUCIBLE.getRecipesForHeat(heat), crucibleUID);
            reg.addRecipes(RecipeRegistry.KILN.getRecipesForHeat(heat), kilnUID);

        }

        reg.addRecipeCatalyst(new ItemStack(BWMBlocks.CAULDRON), cauldron.toArray(new String[0]));
        reg.addRecipeCatalyst(new ItemStack(BWMBlocks.CRUCIBLE), crucible.toArray(new String[0]));
        reg.addRecipeCatalyst(new ItemStack(Blocks.BRICK_BLOCK), kiln.toArray(new String[0]));

        reg.addRecipeClickArea(GuiCauldron.class, 81, 19, 14, 14, cauldron.toArray(new String[0]));
        reg.addRecipeClickArea(GuiCrucible.class, 81, 19, 14, 14, crucible.toArray(new String[0]));

    }


}

