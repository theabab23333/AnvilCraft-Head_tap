package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import me.theabab2333.headtap.init.ModBlockEntities;
import me.theabab2333.headtap.init.ModRecipeTypes;
import me.theabab2333.headtap.recipe.StoneGeneratorRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StoneGeneratorBlockEntity extends BlockEntity implements IItemHandlerHolder {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1);

    public StoneGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inventory", itemHandler.serializeNBT(provider));
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Inventory"));
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.STONE_GENERATOR.get(),
            (be, context) -> be.itemHandler
        );
    }

    public boolean tryGenerateStone(int count) {
        if (this.level == null || this.level.isClientSide()) return false;
        Item stoneType = GetStoneType();
        if (stoneType == Items.AIR) return false;
        ItemStack item = new ItemStack(stoneType, count);
        ItemStack remaining = ItemHandlerHelper.insertItem(itemHandler, item, false);
        return !remaining.isEmpty();
    }

    private boolean testAvailableInventory(Item item) {
        return itemHandler.getStackInSlot(0).is(item) || itemHandler.getStackInSlot(0).isEmpty();
    }

    public Item GetStoneType() {
        if (level == null) return Items.AIR;
        StoneGeneratorRecipe.Input input = StoneGeneratorRecipe.Input.of(level, this.getBlockPos());
        RecipeManager manager = Objects.requireNonNull(level.getServer()).getRecipeManager();
        List<RecipeHolder<StoneGeneratorRecipe>> recipeHolders = manager.getAllRecipesFor(ModRecipeTypes.STONE_GENERATING.get());
        if (recipeHolders.isEmpty()) return Items.AIR;
        int currentPriority = -1;
        StoneGeneratorRecipe currentRecipe = null;
        for (RecipeHolder<StoneGeneratorRecipe> recipeHolder : recipeHolders) {
            StoneGeneratorRecipe recipe = recipeHolder.value();
            if (recipe.matches(input, level)) {
                if (recipe.priority >= currentPriority && testAvailableInventory(recipe.result.getItem())) {
                    currentRecipe = recipe;
                    currentPriority = recipe.priority;
                }
            }
        }
        if (currentRecipe != null) {
            //其实只用类型，数量是由铁砧高度决定的
            return currentRecipe.result.getItem();
        }
        //不满足任何配方的时候不生成
        return Items.AIR;
    }

}
