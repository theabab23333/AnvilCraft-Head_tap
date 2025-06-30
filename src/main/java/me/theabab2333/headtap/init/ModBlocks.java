package me.theabab2333.headtap.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.block.GiantAnvilBlock;
import dev.dubhe.anvilcraft.block.multipart.FlexibleMultiPartBlock;
import dev.dubhe.anvilcraft.block.multipart.SimpleMultiPartBlock;
import dev.dubhe.anvilcraft.block.state.Cube3x3PartHalf;
import dev.dubhe.anvilcraft.block.state.DirectionCube3x3PartHalf;
import dev.dubhe.anvilcraft.data.AnvilCraftDatagen;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.FlexibleMultiPartBlockItem;
import dev.dubhe.anvilcraft.item.SimpleMultiPartBlockItem;
import dev.dubhe.anvilcraft.util.DataGenUtil;
import dev.dubhe.anvilcraft.util.registrater.ModelProviderUtil;
import me.theabab2333.headtap.block.AmethystAnvilBlock;
import me.theabab2333.headtap.block.ResinExtractorBlock;
import me.theabab2333.headtap.block.ResinFluidCauldronBlock;
import me.theabab2333.headtap.block.StoneGeneratorBlock;
import me.theabab2333.headtap.block.VariableFluidTankBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import static me.theabab2333.headtap.HeadTap.REGISTRATE;

@SuppressWarnings("unused")
public class ModBlocks {

    static {
        REGISTRATE.defaultCreativeTab(ModItemGroups.ANVILCRAFT_HEAD_TAP.getKey());
    }

    public static final BlockEntry<? extends Block> AMETHYST_ANVIL = REGISTRATE
        .block("amethyst_anvil", AmethystAnvilBlock::new)
        .recipe((c, p) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                    .pattern("AAA")
                    .pattern(" B ")
                    .pattern("BBB")
                    .define('A', Items.AMETHYST_BLOCK)
                    .define('B', Items.AMETHYST_SHARD)
                    .unlockedBy(AnvilCraftDatagen.hasItem(Items.AMETHYST_SHARD), RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                    .unlockedBy(AnvilCraftDatagen.hasItem(Items.AMETHYST_BLOCK), RegistrateRecipeProvider.has(Items.AMETHYST_BLOCK))
                    .save(p)
        )
        .tag(
            BlockTags.ANVIL,
            BlockTags.MINEABLE_WITH_PICKAXE)
        .initialProperties(() -> Blocks.ANVIL)
        .initialProperties(() -> Blocks.AMETHYST_BLOCK)
        .properties(p -> p.lightLevel(p_152632_ -> 5))
        .properties(p -> p.sound(SoundType.AMETHYST))
        .blockstate((c, p) -> {})
        .item()
        .build()
        .blockstate(DataGenUtil::noExtraModelOrState)
        .register();

    public static final BlockEntry<StoneGeneratorBlock> STONE_GENERATOR = REGISTRATE
        .block("stone_generator", StoneGeneratorBlock::new)
        .blockstate(DataGenUtil::noExtraModelOrState)
        .simpleItem()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .properties(p -> p.sound(SoundType.COPPER))
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
            .pattern("ABA")
            .pattern("CDC")
            .pattern("AEA")
            .define('A', Items.IRON_INGOT)
            .define('B', dev.dubhe.anvilcraft.init.ModBlocks.CRUSHING_TABLE)
            .define('C', ModItems.MAGNET_INGOT)
            .define('D', Items.AMETHYST_SHARD)
            .define('E', Items.STONECUTTER)
            .unlockedBy(AnvilCraftDatagen.hasItem(Items.IRON_INGOT), RegistrateRecipeProvider.has(Items.IRON_INGOT))
            .unlockedBy(AnvilCraftDatagen.hasItem(dev.dubhe.anvilcraft.init.ModBlocks.CRUSHING_TABLE),
                RegistrateRecipeProvider.has(dev.dubhe.anvilcraft.init.ModBlocks.CRUSHING_TABLE))
            .unlockedBy(AnvilCraftDatagen.hasItem(ModItems.MAGNET_INGOT), RegistrateRecipeProvider.has(ModItems.MAGNET_INGOT))
            .unlockedBy(AnvilCraftDatagen.hasItem(Items.AMETHYST_SHARD), RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
            .unlockedBy(AnvilCraftDatagen.hasItem(Items.STONECUTTER), RegistrateRecipeProvider.has(Items.STONECUTTER))
            .save(provider))
        .register();

    public static final BlockEntry<ResinExtractorBlock> RESIN_EXTRACTOR = REGISTRATE
        .block("resin_extractor", ResinExtractorBlock::new)
        .blockstate(DataGenUtil::noExtraModelOrState)
        .simpleItem()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .properties(p -> p.sound(SoundType.COPPER))
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("AAA")
            .define('A', Items.IRON_INGOT)
            .define('B', ModItems.RESIN)
            .define('C', dev.dubhe.anvilcraft.init.ModBlocks.CRUSHING_TABLE)
            .unlockedBy(AnvilCraftDatagen.hasItem(Items.IRON_INGOT), AnvilCraftDatagen.has(Items.IRON_INGOT))
            .unlockedBy(AnvilCraftDatagen.hasItem(ModItems.RESIN), AnvilCraftDatagen.has(ModItems.RESIN))
            .unlockedBy(
                AnvilCraftDatagen.hasItem(dev.dubhe.anvilcraft.init.ModBlocks.CRUSHING_TABLE),
                AnvilCraftDatagen.has(dev.dubhe.anvilcraft.init.ModBlocks.CRUSHING_TABLE))
            .save(provider))
        .register();

    public static final BlockEntry<VariableFluidTankBlock> VARIABLE_FLUID_TANK = REGISTRATE
        .block("variable_fluid_tank", VariableFluidTankBlock::new)
        .blockstate(DataGenUtil::noExtraModelOrState)
        .simpleItem()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .properties(p -> p.sound(SoundType.COPPER))
        .register();

    public static final BlockEntry<ResinFluidCauldronBlock> RESIN_FLUID_CAULDRON = REGISTRATE
        .block("resin_fluid_cauldron", ResinFluidCauldronBlock::new)
        .initialProperties(() -> Blocks.CAULDRON)
        .blockstate(DataGenUtil::noExtraModelOrState)
        .loot((tables, block) -> tables.dropOther(block, Items.CAULDRON))
        .tag(BlockTags.MINEABLE_WITH_PICKAXE,
            BlockTags.CAULDRONS)
        .onRegister(block -> Item.BY_BLOCK.put(block, Items.CAULDRON))
        .register();

    public static BlockEntry<LiquidBlock> RESIN_FLUID = REGISTRATE
        .block("resin_fluid", p -> new LiquidBlock(ModFluids.RESIN_FLUID.get(), p))
        .properties(it -> it
            .mapColor(MapColor.EMERALD)
            .replaceable()
            .noCollission()
            .pushReaction(PushReaction.DESTROY)
            .noLootTable()
            .liquid()
            .sound(SoundType.EMPTY)
            .strength(100.0F)
        )
        .blockstate(ModelProviderUtil::liquid)
        .register();
    public static void register() {}

    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entity) {
        return false;
    }

    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }
}