package me.theabab2333.headtap.integration.jei;

import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import dev.dubhe.anvilcraft.integration.jei.util.JeiSlotUtil;
import dev.dubhe.anvilcraft.integration.jei.util.TextureConstants;
import dev.dubhe.anvilcraft.util.RenderHelper;
import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModRecipeTypes;
import me.theabab2333.headtap.recipe.StoneGeneratorRecipe;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StoneGeneratorCategory implements IRecipeCategory<RecipeHolder<StoneGeneratorRecipe>> {
    public static final int WIDTH = 162;
    public static final int HEIGHT = 64;

    private final IDrawable icon;
    private final IDrawable slot;
    private final Component title;
    private final ITickTimer timer;

    private final IDrawable arrowOut;

    public StoneGeneratorCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.STONE_GENERATOR));
        slot = guiHelper.getSlotDrawable();
        title = Component.translatable("gui.head_tap.category.stone_generator");
        timer = guiHelper.createTickTimer(30, 60, true);
        arrowOut = guiHelper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 40, 16, 10);
    }

    @Override
    public RecipeType<RecipeHolder<StoneGeneratorRecipe>> getRecipeType() {
        return HeadTapJeiPlugin.STONE_GENERATOR;
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Items.ANVIL), HeadTapJeiPlugin.STONE_GENERATOR);
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


    //拼劲全力我只能写成这样了()
    //个人感觉还算看得过去 就是如果2个输入都是流体会重叠在一起() 如果有人能优化下就好了
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<StoneGeneratorRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 24).addItemStack(recipe.value().result);
        for(FluidIngredient fluidIngredient : recipe.value().inputFluids) {
            FluidStack[] fluidStacks = fluidIngredient.getStacks();
            for(FluidStack fluidStack : fluidStacks) {
                builder.addInputSlot(11, 15).addFluidStack(fluidStack.getFluid());
            }
        }
        for(Block block : recipe.value().inputBlocks) {
            builder.addInputSlot(29, 15).addItemStack(new ItemStack(block));
        }
    }

    @Override
    public void draw(
        RecipeHolder<StoneGeneratorRecipe> recipe,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY) {
        float anvilYOffset = JeiRenderHelper.getAnvilAnimationOffset(timer);
        RenderHelper.renderBlock(
            guiGraphics,
            Blocks.ANVIL.defaultBlockState(),
            81,
            12 + anvilYOffset,
            20,
            12,
            RenderHelper.SINGLE_BLOCK);
        RenderHelper.renderBlock(
            guiGraphics,
            ModBlocks.STONE_GENERATOR.getDefaultState(),
            81,
            30,
            10,
            12,
            RenderHelper.SINGLE_BLOCK);
        arrowOut.draw(guiGraphics, 98, 28);

        JeiSlotUtil.drawOutputSlots(guiGraphics, slot, 1);
        JeiSlotUtil.drawInputSlots(guiGraphics, slot, 2);
    }

    @Override
    public void getTooltip(
        ITooltipBuilder tooltip,
        RecipeHolder<StoneGeneratorRecipe> recipe,
        IRecipeSlotsView recipeSlotsView,
        double mouseX,
        double mouseY
    ) {

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
}
