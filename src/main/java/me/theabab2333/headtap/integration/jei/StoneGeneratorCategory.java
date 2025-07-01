package me.theabab2333.headtap.integration.jei;

import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import dev.dubhe.anvilcraft.integration.jei.util.TextureConstants;
import dev.dubhe.anvilcraft.util.RenderHelper;
import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModRecipeTypes;
import me.theabab2333.headtap.recipe.StoneGeneratorRecipe;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StoneGeneratorCategory implements IRecipeCategory<RecipeHolder<StoneGeneratorRecipe>> {
    public static final int WIDTH = 162;
    public static final int HEIGHT = 64;

    private final IDrawable progress;
    private final IDrawable icon;
    private final IDrawable slot;
    private final ITickTimer timer;
    private final Component title;


    public StoneGeneratorCategory(IGuiHelper helper) {
        this.progress = helper.drawableBuilder(TextureConstants.PROGRESS, 0, 0, 24, 16)
            .setTextureSize(24, 16)
            .build();
        slot = helper.getSlotDrawable();
        icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.STONE_GENERATOR));
        title = Component.translatable("jei.headtap.category.stone_generator");
        timer = helper.createTickTimer(30, 60, true);
    }

    @Override
    public RecipeType<RecipeHolder<StoneGeneratorRecipe>> getRecipeType() {
        return HeadTapJeiPlugin.STONE_GENERATOR;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
        IRecipeLayoutBuilder builder,
        RecipeHolder<StoneGeneratorRecipe> recipeHolder,
        IFocusGroup focuses
    ) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 26).addItemStack(recipeHolder.value().result);
    }
    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Items.ANVIL), HeadTapJeiPlugin.STONE_GENERATOR);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AMETHYST_ANVIL), HeadTapJeiPlugin.STONE_GENERATOR);
        registration.addRecipeCatalyst(new ItemStack(dev.dubhe.anvilcraft.init.ModBlocks.ROYAL_ANVIL), HeadTapJeiPlugin.STONE_GENERATOR);
        registration.addRecipeCatalyst(new ItemStack(dev.dubhe.anvilcraft.init.ModBlocks.EMBER_ANVIL), HeadTapJeiPlugin.STONE_GENERATOR);
        registration.addRecipeCatalyst(new ItemStack(dev.dubhe.anvilcraft.init.ModBlocks.GIANT_ANVIL), HeadTapJeiPlugin.STONE_GENERATOR);
        registration.addRecipeCatalyst(new ItemStack(dev.dubhe.anvilcraft.init.ModBlocks.SPECTRAL_ANVIL), HeadTapJeiPlugin.STONE_GENERATOR);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.STONE_GENERATOR), HeadTapJeiPlugin.STONE_GENERATOR);
    }

    public static void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
            HeadTapJeiPlugin.STONE_GENERATOR,
            JeiRecipeUtil.getRecipeHoldersFromType(ModRecipeTypes.STONE_GENERATING.get())
        );
    }

    @Override
    public void draw(
        RecipeHolder<StoneGeneratorRecipe> recipeHolder,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY) {
        StoneGeneratorRecipe recipe = recipeHolder.value();
        float anvilYOffset = JeiRenderHelper.getAnvilAnimationOffset(timer);
        progress.draw(guiGraphics, 75, 26);
        slot.draw(guiGraphics, 109, 25);

        RenderHelper.renderBlock(
            guiGraphics,
            Blocks.ANVIL.defaultBlockState(),
            50,
            12 + anvilYOffset,
            20,
            12,
            RenderHelper.SINGLE_BLOCK);

        RenderHelper.renderBlock(
            guiGraphics,
            ModBlocks.STONE_GENERATOR.getDefaultState(),
            50,
            30,
            10,
            12,
            RenderHelper.SINGLE_BLOCK);

        if (!recipe.inputFluids.isEmpty() && !recipe.inputBlocks.isEmpty()) {
            for (int b = recipe.inputBlocks.size() - 1; b >= 0; b--) {
                List.of(
                    recipe.inputBlocks.get(b)
                ).forEach(block -> RenderHelper.renderBlock(
                    guiGraphics,
                    block.defaultBlockState(),
                    42,
                    26,
                    3,
                    12,
                    RenderHelper.SINGLE_BLOCK)
                );
                List.of(
                    recipe.inputFluids.get(b)
                ).forEach(fluidIngredient -> {
                    fluidIngredient.getStacks()[0].getFluid().defaultFluidState().createLegacyBlock();
                    RenderHelper.renderBlock(
                        guiGraphics,
                        fluidIngredient.getStacks()[0].getFluid().defaultFluidState().createLegacyBlock(),
                        58,
                        34,
                        17,
                        12,
                        RenderHelper.SINGLE_BLOCK);
                });
            }
        } else if (recipe.inputBlocks.isEmpty()) {
            for (int f = recipe.inputFluids.size() - 1; f >= 0; f--){
                AtomicReference<BlockState> renderedFluidState = new AtomicReference<>();
                FluidIngredient fluidState = recipe.inputFluids.get(f);
                FluidStack fluidStack = fluidState.getStacks()[0];
                renderedFluidState.set(fluidStack.getFluid().defaultFluidState().createLegacyBlock());
                if (f != 0) {
                    RenderHelper.renderBlock(
                        guiGraphics,
                        renderedFluidState.get(),
                        58,
                        34,
                        17,
                        12,
                        RenderHelper.SINGLE_BLOCK);
                } else {
                    RenderHelper.renderBlock(
                        guiGraphics,
                        renderedFluidState.get(),
                        42,
                        26,
                        3,
                        12,
                        RenderHelper.SINGLE_BLOCK); break;
                }
            }
        } else {
            for (int b = recipe.inputBlocks.size() - 1; b >= 0; b--) {
                AtomicReference<BlockState> renderedBlockState = new AtomicReference<>();
                Block block = recipe.inputBlocks.get(b);
                BlockState blockState = block.defaultBlockState();
                renderedBlockState.set(blockState);
                if (b != 0) {
                    RenderHelper.renderBlock(
                        guiGraphics,
                        renderedBlockState.get(),
                        58,
                        34,
                        17,
                        12,
                        RenderHelper.SINGLE_BLOCK);
                } else {
                    RenderHelper.renderBlock(
                        guiGraphics,
                        renderedBlockState.get(),
                        42,
                        26,
                        3,
                        12,
                        RenderHelper.SINGLE_BLOCK); break;
                }
            }
        }
    }
}
