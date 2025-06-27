package me.theabab2333.headtap.integration.jei;

import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.recipe.StoneGeneratorRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HeadTapJeiPlugin implements IModPlugin {

    public static final RecipeType<RecipeHolder<StoneGeneratorRecipe>> STONE_GENERATOR =
        createRecipeHolderType("stone_generator");

    @Override
    public ResourceLocation getPluginUid() {
        return HeadTap.of("jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        StoneGeneratorCategory.registerRecipes(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        StoneGeneratorCategory.registerRecipeCatalysts(registration);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AMETHYST_ANVIL), RecipeTypes.ANVIL);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(new StoneGeneratorCategory(guiHelper));
    }

    public static <R extends Recipe<?>> RecipeType<RecipeHolder<R>> createRecipeHolderType(String name) {
        return RecipeType.createRecipeHolderType(HeadTap.of(name));
    }
}