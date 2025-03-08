package me.theabab2333.skyland_extend.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.init.ModBlockTags;
import me.theabab2333.skyland_extend.Skyland_extend;
import me.theabab2333.skyland_extend.block.AmethystAnvilBlock;
import me.theabab2333.skyland_extend.block.AnvilTickerBlock;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static me.theabab2333.skyland_extend.Skyland_extend.REGISTRATE;

@SuppressWarnings("unused")
public class ModBlocks {

    static {
        REGISTRATE.defaultCreativeTab(ModItemGroups.SKYLAND_EXTEND_BLOCK.getKey());
    }

    public static final BlockEntry<? extends Block> AMETHYST_ANVIL = Skyland_extend.REGISTRATE
            .block("amethyst_anvil", AmethystAnvilBlock::new)
            .recipe((c, p) ->
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                            .pattern("AAA")
                            .pattern(" B ")
                            .pattern("BBB")
                            .define('A', Items.AMETHYST_BLOCK)
                            .define('B', Items.AMETHYST_SHARD)
                            .unlockedBy("has_amethyst_shard", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                            .unlockedBy("has_amethyst_block", RegistrateRecipeProvider.has(Items.AMETHYST_BLOCK))
                            .save(p)
            )
            .initialProperties(() -> Blocks.ANVIL)
            .initialProperties(() -> Blocks.AMETHYST_BLOCK)
            .blockstate((c, p) -> {
            })
            .item()
            .tag(ItemTags.ANVIL)
            .build()
            .tag(BlockTags.ANVIL,
                    ModBlockTags.HAMMER_REMOVABLE,
                    BlockTags.MINEABLE_WITH_PICKAXE,
                    BlockTags.NEEDS_STONE_TOOL)
            .register();

    public static final BlockEntry<? extends Block> ANVIL_TICKER = REGISTRATE
            .block("anvil_ticker", AnvilTickerBlock::new)
            .blockstate((ctx, provider) -> {
            })
            .simpleItem()
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .register();

    public static void register() {}
}