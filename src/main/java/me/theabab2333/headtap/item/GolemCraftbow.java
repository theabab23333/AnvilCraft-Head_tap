package me.theabab2333.headtap.item;

import me.theabab2333.headtap.init.ModRecipeTypes;
import me.theabab2333.headtap.recipe.GolemCraftRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class GolemCraftbow extends Item {

    public static final int MIN_CHARGE_TICKS = 10;
    public static final int FIX_RADIUS = 6;

    public GolemCraftbow(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.isCrouching()) {
            player.startUsingItem(usedHand);
        }
        else {
            AABB box = player.getBoundingBox().inflate(FIX_RADIUS);
            List<IronGolem> ironGolems = level.getEntitiesOfClass(IronGolem.class, box);
            if (ironGolems.isEmpty()) return InteractionResultHolder.pass(stack);
            List<ItemStack> items = player.getInventory().items;
            boolean flag = false;
            for (ItemStack item : items) {
                if (item.is(Items.IRON_INGOT)) {
                    for (IronGolem golem : ironGolems) {
                        float x = golem.getHealth();
                        if (x <= 85.0f) {
                            golem.heal(25.0f);
                            if (golem.getHealth() != x) {
                                item.shrink(1);
                                flag = true;
                            }
                        }
                    }
                }
            }
            if (flag) {
                float f1 = 1.0F + (player.getRandom().nextFloat() - 0.5f) * 0.2F;
                player.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
            } else {
                return InteractionResultHolder.pass(stack);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public static InteractionResult applyGolemCraftRecipe(Level level, Player player) {
        if(!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;
        RecipeManager manager = Objects.requireNonNull(level.getServer()).getRecipeManager();
        List<RecipeHolder<GolemCraftRecipe>> recipeHolders = manager.getAllRecipesFor(ModRecipeTypes.GOLEM_CRAFTBOW.get());
        if (recipeHolders.isEmpty()) return InteractionResult.FAIL;
        ItemStack item = player.getOffhandItem();
        Entity result = null;
        boolean flag = false;
        for (RecipeHolder<GolemCraftRecipe> recipeHolder : recipeHolders) {
            GolemCraftRecipe recipe = recipeHolder.value();
            if (recipe.testItem(item)) {
                result = recipe.apply(player, serverLevel);
                if (result != null) {
                    item.shrink(recipe.itemInput.getCount());
                    flag = true;
                    break;
                }
            }
        }
        List<ItemStack> items = player.getInventory().items;
        for (int i = 0; !flag && i < items.size(); i++) {
            ItemStack inventoryItem = items.get(i);
            for (RecipeHolder<GolemCraftRecipe> recipeHolder : recipeHolders) {
                GolemCraftRecipe recipe = recipeHolder.value();
                if (recipe.testItem(inventoryItem)) {
                    result = recipe.apply(player, serverLevel);
                    if (result != null) {
                        inventoryItem.shrink(recipe.itemInput.getCount());
                        flag = true;
                        break;
                    }
                }
            }
        }
        if (result == null) return InteractionResult.FAIL;
        if (result instanceof ZombieHorse || result instanceof SkeletonHorse) {
            ((AbstractHorse) result).setTamed(true);
        }
        serverLevel.tryAddFreshEntityWithPassengers(result);
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeCharged) {
        if (!(livingEntity instanceof Player player)) return;
        if (timeCharged >= MIN_CHARGE_TICKS && !player.getCooldowns().isOnCooldown(this)) {
            InteractionResult interactionResult = applyGolemCraftRecipe(level, player);
            if (interactionResult == InteractionResult.SUCCESS) {
                stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
                player.getCooldowns().addCooldown(this, 20);
            }
            else if (interactionResult == InteractionResult.FAIL){
                player.displayClientMessage(
                    Component.translatable("item.head_tap.golem_craftbow.golem_fail")
                        .withStyle(ChatFormatting.GRAY),
                    false);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.head_tap.golem_craftbow.tooltip")
            .withStyle(ChatFormatting.GRAY));
    }
}
