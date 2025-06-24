package me.theabab2333.headtap.event;

import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModFluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;

@EventBusSubscriber(modid = HeadTap.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CauldronFluidContentEventListener {

    @SubscribeEvent
    public static void registerCauldronFluidContent(RegisterCauldronFluidContentEvent event) {
        event.register(ModBlocks.RESIN_FLUID_CAULDRON.get(), ModFluids.RESIN_FLUID.get(), 1000, null);
    }
}