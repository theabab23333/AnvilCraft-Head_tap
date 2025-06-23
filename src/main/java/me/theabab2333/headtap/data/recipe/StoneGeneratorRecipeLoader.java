package me.theabab2333.headtap.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import me.theabab2333.headtap.recipe.StoneGeneratorRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

public class StoneGeneratorRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        StoneGeneratorRecipe.builder()
            .requires(Fluids.WATER)
            .requires(Fluids.LAVA)
            .result(Items.COBBLESTONE)
            .priority(1).save(provider);
        StoneGeneratorRecipe.builder()
            .requires(Blocks.ICE)
            .requires(Fluids.LAVA)
            .result(Items.ANDESITE)
            .priority(2).save(provider);
        StoneGeneratorRecipe.builder()
            .requires(Blocks.PACKED_ICE)
            .requires(Fluids.LAVA)
            .result(Items.DIORITE)
            .priority(3).save(provider);
        StoneGeneratorRecipe.builder()
            .requires(Blocks.BLUE_ICE)
            .requires(Fluids.LAVA)
            .result(Items.GRANITE)
            .priority(4).save(provider);
    }
}
