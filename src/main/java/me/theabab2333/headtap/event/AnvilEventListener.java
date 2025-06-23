package me.theabab2333.headtap.event;

import dev.dubhe.anvilcraft.api.event.anvil.AnvilFallOnLandEvent;
import dev.dubhe.anvilcraft.block.PiezoelectricCrystalBlock;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.init.ModAnvilBehaviors;
import me.theabab2333.headtap.init.ModBlocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@EventBusSubscriber(modid = HeadTap.MODID)
public class AnvilEventListener {
    private static boolean behaviorRegistered = false;
    @SubscribeEvent
    public static void onLand(@NotNull AnvilFallOnLandEvent event){
        if (!behaviorRegistered) {
            ModAnvilBehaviors.register();
            PiezoelectricCrystalBlock.ANVIL_TYPES.put(ModBlocks.AMETHYST_ANVIL.get(), List.of(2, 4, 6, 8));
            behaviorRegistered = true;
        }
    }
}