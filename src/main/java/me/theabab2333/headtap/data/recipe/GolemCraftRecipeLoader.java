package me.theabab2333.headtap.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.recipe.transform.TagModification;
import me.theabab2333.headtap.recipe.GolemCraftRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

public class GolemCraftRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        GolemCraftRecipe.builder()
            .requires(Items.IRON_BLOCK, 4)
            .result(EntityType.IRON_GOLEM)
            .tagModification(b -> {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("PlayerCreated", true);
                b.operation(TagModification.ModifyOperation.SET)
                    .path("")
                    .value(tag);
            })
            .save(provider);
        GolemCraftRecipe.builder()
            .requires(Items.SNOW_BLOCK, 2)
            .result(EntityType.SNOW_GOLEM)
            .tagModification(b -> {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("Pumpkin", false);
                b.operation(TagModification.ModifyOperation.SET)
                    .path("")
                    .value(tag);
            })
            .save(provider);
    }
}
