package me.theabab2333.headtap.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dubhe.anvilcraft.recipe.anvil.builder.AbstractRecipeBuilder;
import dev.dubhe.anvilcraft.recipe.transform.TagModification;
import dev.dubhe.anvilcraft.util.CodecUtil;
import dev.dubhe.anvilcraft.util.Util;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.init.ModRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GolemCraftRecipe implements Recipe<GolemCraftRecipe.Input> {

    //虽说是个配方，但是是为了方便利用TagModification修改tag和方便魔改搞出来的
    //实际上它甚至不太需要JEI支持

    public final ItemStack itemInput;
    public final EntityType<?> result;
    public final List<TagModification> tagModifications;

    public static final Codec<GolemCraftRecipe> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ItemStack.CODEC.fieldOf("input").forGetter(o -> o.itemInput),
            CodecUtil.ENTITY_CODEC.fieldOf("result").forGetter(o -> o.result),
            TagModification.CODEC
                .listOf()
                .optionalFieldOf("tagModifications")
                .forGetter(o -> Util.intoOptional(o.tagModifications)))
        .apply(ins, GolemCraftRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GolemCraftRecipe> STREAM_CODEC = StreamCodec.of(
        (buf, recipe) -> buf.writeNbt(intoTag(recipe)), friendlyByteBuf -> fromTag(friendlyByteBuf.readNbt()));

    public GolemCraftRecipe(
        ItemStack itemInput, EntityType<?> result,
        Optional<List<TagModification>> tagModifications) {
        this.itemInput = itemInput;
        this.result = result;
        this.tagModifications = tagModifications.orElse(List.of());
    }

    public boolean testItem(ItemStack item) {
        return item.is(itemInput.getItem()) && item.getCount() >= itemInput.getCount();
    }

    @Override
    public boolean matches(Input input, @NotNull Level level) {
        return testItem(input.item);
    }

    @Override
    public ItemStack assemble(Input input, HolderLookup.Provider provider) {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.GOLEM_CRAFTBOW_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.GOLEM_CRAFTBOW.get();
    }

    public static GolemCraftRecipe.Builder builder() {
        return new Builder();
    }

    @Nullable
    public Entity apply(LivingEntity user, ServerLevel level) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(result).toString());
        Entity newEntity = EntityType.loadEntityRecursive(tag, level, (e) -> {
            e.moveTo(
                user.position().x,
                user.position().y,
                user.position().z,
                e.getYRot(),
                e.getXRot());
            return e;
        });
        if (newEntity == null) return null;
        CompoundTag compoundTag = newEntity.saveWithoutId(new CompoundTag());
        for (TagModification tagModification : tagModifications) {
            tagModification.accept(compoundTag);
        }
        UUID uuid = newEntity.getUUID();
        newEntity.load(compoundTag);
        newEntity.setUUID(uuid);
        return newEntity;
    }

    public record Input(ItemStack item) implements RecipeInput {
        @Override
        public @NotNull ItemStack getItem(int i) {
            return item;
        }

        @Override
        public int size() {
            return 1;
        }
    }

    public static GolemCraftRecipe fromTag(Tag tag) {
        return CODEC.decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst();
    }

    public static Tag intoTag(GolemCraftRecipe recipe) {
        return CODEC.encodeStart(NbtOps.INSTANCE, recipe).getOrThrow();
    }

    public static final class Serializer implements RecipeSerializer<GolemCraftRecipe> {
        public static final MapCodec<GolemCraftRecipe> MAP_CODEC =
            RecordCodecBuilder.mapCodec(ins -> ins.group(
                    ItemStack.CODEC.fieldOf("input").forGetter(o -> o.itemInput),
                    CodecUtil.ENTITY_CODEC.fieldOf("result").forGetter(o -> o.result),
                    TagModification.CODEC
                        .listOf()
                        .optionalFieldOf("tagModifications")
                        .forGetter(o -> Util.intoOptional(o.tagModifications)))
                .apply(ins, GolemCraftRecipe::new));

        @Override
        public MapCodec<GolemCraftRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GolemCraftRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Builder extends AbstractRecipeBuilder<GolemCraftRecipe> {
        private ItemStack itemInput = null;
        private EntityType<?> result = null;
        private List<TagModification> tagModifications = new ArrayList<>();

        public GolemCraftRecipe.Builder requires(ItemLike pItem) {
            itemInput = pItem.asItem().getDefaultInstance();
            return this;
        }

        public GolemCraftRecipe.Builder requires(Item item, int count) {
            ItemStack stack = item.getDefaultInstance();
            stack.setCount(count);
            itemInput = stack;
            return this;
        }

        public GolemCraftRecipe.Builder result(EntityType<?> entityType) {
            result = entityType;
            return this;
        }

        public GolemCraftRecipe.Builder tagModification(@NotNull Consumer<TagModification.Builder> predicateBuilder) {
            if (tagModifications == null) tagModifications = new ArrayList<>();
            TagModification.Builder builder = TagModification.builder();
            predicateBuilder.accept(builder);
            tagModifications.add(builder.build());
            return this;
        }

        @Override
        public void validate(ResourceLocation pId) {
            if (itemInput == null)
                throw new IllegalArgumentException("Recipe has no ingredient, RecipeId: " + pId);
            if (result == null)
                throw new IllegalArgumentException("Recipe has no result, RecipeId: " + pId);
        }

        @Override
        public String getType() {
            return "golem_craftbow";
        }

        @Override
        public GolemCraftRecipe buildRecipe() {
            return new GolemCraftRecipe(itemInput, result, Util.intoOptional(tagModifications));
        }

        @Override
        public Item getResult() {
            SpawnEggItem spawnEggItem = SpawnEggItem.byId(result);
            return Objects.requireNonNullElse(spawnEggItem, Items.AIR);
        }

        @Override
        public void save(RecipeOutput recipeOutput) {
            save(
                recipeOutput,
                HeadTap.of(BuiltInRegistries.ITEM.getKey(itemInput.getItem()).getPath())
                    .withPrefix(getType() + "/")
            );
        }
    }

}
