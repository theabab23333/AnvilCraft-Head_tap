package me.theabab2333.headtap.item;

import dev.dubhe.anvilcraft.item.AnvilHammerItem;
import me.theabab2333.headtap.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AmethystAnvilHammer extends AnvilHammerItem {
    /**
     * 初始化铁砧锤
     *
     * @param properties 物品属性
     */
    public AmethystAnvilHammer(Properties properties) {
        super(properties);
    }

    public void appendHoverText(
        ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatable("item.headtap.amethyst_hammer.tooltip")
            .withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected float getAttackDamageModifierAmount() {
        return 3;
    }
    @Override
    public Block getAnvil(){
        return ModBlocks.AMETHYST_ANVIL.get();
    }

    @Override
    protected float calculateFallDamageBonus(float fallDistance) {
        return Math.min(40, fallDistance * 4);
    }
}
