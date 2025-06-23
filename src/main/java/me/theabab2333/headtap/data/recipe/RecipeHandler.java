package me.theabab2333.headtap.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;

public class RecipeHandler {
    public static void init(RegistrateRecipeProvider provider) {
        StoneGeneratorRecipeLoader.init(provider);
        GolemCraftRecipeLoader.init(provider);
    }
}
