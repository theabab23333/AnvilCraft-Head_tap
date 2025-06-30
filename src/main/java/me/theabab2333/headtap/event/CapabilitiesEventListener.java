package me.theabab2333.headtap.event;


import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.api.itemhandler.ResinCauldronWrapper;
import me.theabab2333.headtap.init.ModBlockEntities;
import me.theabab2333.headtap.init.ModBlocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;

@EventBusSubscriber(modid = HeadTap.MODID)
public class CapabilitiesEventListener {
    @SubscribeEvent
    public static void registerCapabilities(final RegisterCapabilitiesEvent event) {

        // ItemHandler
        List.of(
            ModBlockEntities.STONE_GENERATOR.get(),
            dev.dubhe.anvilcraft.init.ModBlockEntities.CRAB_TRAP.get()
        ).forEach(type -> event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            type,
            (be, side) -> be.getItemHandler())
        );

        // FluidHandler
        // 我不会(指合在一起
        List.of(
            ModBlockEntities.RESIN_EXTRACTOR.get()
        ).forEach(type -> event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK,
            type,
            (be, side) -> be.getFluidHandler())
        );

        List.of(
            ModBlockEntities.VARIABLE_FLUID_TANK.get()
        ).forEach(type -> event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK,
            type,
            (be, side) -> be.getFluidHandler())
        );

        // Other
        event.registerBlock(
            Capabilities.ItemHandler.BLOCK,
            ((level, pos, state, blockEntity, side) -> new ResinCauldronWrapper(level, pos)),
            ModBlocks.RESIN_FLUID_CAULDRON.get()
        );
    }
}
