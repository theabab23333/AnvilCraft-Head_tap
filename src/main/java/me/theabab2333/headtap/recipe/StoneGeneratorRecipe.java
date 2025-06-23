package me.theabab2333.headtap.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dubhe.anvilcraft.recipe.anvil.builder.AbstractRecipeBuilder;
import dev.dubhe.anvilcraft.util.CodecUtil;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.init.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class StoneGeneratorRecipe implements Recipe<StoneGeneratorRecipe.Input> {
    public final List<Block> inputBlocks;
    public final List<FluidIngredient> inputFluids;
    public final ItemStack result;
    public final int priority;

    public static final Codec<StoneGeneratorRecipe> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            CodecUtil.BLOCK_CODEC.listOf(0, 9).fieldOf("inputBlocks").forGetter(o -> o.inputBlocks),
            FluidIngredient.CODEC.listOf(0, 9).fieldOf("inputFluids").forGetter(o -> o.inputFluids),
            ItemStack.CODEC.fieldOf("result").forGetter(o -> o.result),
            Codec.INT.fieldOf("priority").forGetter(o -> o.priority))
        .apply(ins, StoneGeneratorRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StoneGeneratorRecipe> STREAM_CODEC = StreamCodec.of(
        (buf, recipe) -> buf.writeNbt(intoTag(recipe)), friendlyByteBuf -> fromTag(friendlyByteBuf.readNbt()));

    public StoneGeneratorRecipe(List<Block> inputBlocks, List<FluidIngredient> inputFluids, ItemStack result, int priority) {
        this.inputBlocks = inputBlocks;
        this.inputFluids = inputFluids;
        this.result = result;
        this.priority = priority;
    }

    @Override
    public boolean matches(Input input, Level level) {
        for (Block block : inputBlocks) {
            if (!input.inputBlocks.contains(block)) return false;
        }
        for (FluidIngredient fluidIngredient : inputFluids) {
            FluidStack[] fluidStacks = fluidIngredient.getStacks();
            boolean containFlag = false;
            for (FluidStack fluidStack : fluidStacks) {
                //对于单个FluidIngredient里面的多个流体种类（其实是同tag不同流体），是或的关系
                if (input.inputFluids.contains(fluidStack.getFluid())) {
                    containFlag = true; break;
                }
            }
            //对于inputFluids里的多个FluidIngredient种类，是且的关系（方块输入之间也是且的关系）
            if (!containFlag) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(Input input, HolderLookup.Provider provider) {
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.STONE_GENERATING_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.STONE_GENERATING.get();
    }

    public static StoneGeneratorRecipe fromTag(Tag tag) {
        return CODEC.decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst();
    }

    public static Tag intoTag(StoneGeneratorRecipe recipe) {
        return CODEC.encodeStart(NbtOps.INSTANCE, recipe).getOrThrow();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Input implements RecipeInput {
        public List<Block> inputBlocks;
        public List<Fluid> inputFluids;

        public static Input of(@Nullable List<Block> blockList, @Nullable List<Fluid> fluidList) {
            Input input = new StoneGeneratorRecipe.Input();
            input.inputBlocks = blockList;
            input.inputFluids = fluidList;
            return input;
        }

        public static Input of(Level level, BlockPos pos) {
            List<Block> blockList = new ArrayList<>();
            List<Fluid> fluidList = new ArrayList<>();
            for (Direction face : Direction.values()) {
                BlockPos pos1 = pos.relative(face);
                blockList.add(level.getBlockState(pos1).getBlock());
                fluidList.add(level.getFluidState(pos1).getType());
            }
            return Input.of(blockList, fluidList);
        }

        @Override
        public @NotNull ItemStack getItem(int i) {
            int x = inputBlocks.size();
            if (i < x)
                return inputBlocks.get(i).asItem().getDefaultInstance();
            else {
                return inputFluids.get(i - x).getBucket().getDefaultInstance();
            }
        }

        @Override
        public int size() {
            return inputBlocks.size() + inputFluids.size();
        }

        @Override
        public boolean isEmpty() {
            return RecipeInput.super.isEmpty();
        }
    }

    public static final class Serializer implements RecipeSerializer<StoneGeneratorRecipe> {
        public static final MapCodec<StoneGeneratorRecipe> MAP_CODEC =
            RecordCodecBuilder.mapCodec(ins -> ins.group(
                    CodecUtil.BLOCK_CODEC.listOf(0, 4).fieldOf("inputBlocks").forGetter(o -> o.inputBlocks),
                    FluidIngredient.CODEC.listOf(0, 4).fieldOf("inputFluids").forGetter(o -> o.inputFluids),
                    ItemStack.CODEC.fieldOf("result").forGetter(o -> o.result),
                    Codec.INT.fieldOf("priority").forGetter(o -> o.priority))
                .apply(ins, StoneGeneratorRecipe::new));

        @Override
        public @NotNull MapCodec<StoneGeneratorRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, StoneGeneratorRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Builder extends AbstractRecipeBuilder<StoneGeneratorRecipe> {
        private final List<Block> blockList;
        private final List<FluidIngredient> fluidList;
        private ItemStack result = null;
        private int priority = 0;

        public Builder() {
            super();
            blockList = new ArrayList<>();
            fluidList = new ArrayList<>();
        }

        public StoneGeneratorRecipe.Builder requires(Block block) {
            blockList.add(block);
            return this;
        }

        public StoneGeneratorRecipe.Builder requires(Fluid fluid) {
            fluidList.add(FluidIngredient.single(fluid));
            return this;
        }

        public StoneGeneratorRecipe.Builder requires(TagKey<Fluid> fluidTagKey) {
            fluidList.add(FluidIngredient.tag(fluidTagKey));
            return this;
        }

        public StoneGeneratorRecipe.Builder result(ItemLike pItem) {
            result = pItem.asItem().getDefaultInstance();
            return this;
        }

        public StoneGeneratorRecipe.Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        @Override
        public @NotNull StoneGeneratorRecipe buildRecipe() {
            return new StoneGeneratorRecipe(blockList, fluidList, result, priority);
        }

        @Override
        public void validate(ResourceLocation pId) {
            if (result == null)
                throw new IllegalArgumentException("Recipe has no result, RecipeId: " + pId);
            if (blockList.size() > 6 || fluidList.size() > 6)
                throw new IllegalArgumentException("Recipe has too much requirements, RecipeId: " + pId);
        }

        @Override
        public @NotNull String getType() {
            return "stone_generating";
        }

        @Override
        public @NotNull Item getResult() {
            return result.getItem();
        }

        @Override
        public void save(RecipeOutput recipeOutput) {
            save(
                recipeOutput,
                HeadTap.of(BuiltInRegistries.ITEM.getKey(getResult()).getPath())
                    .withPrefix(getType() + "/")
            );
        }
    }
}
