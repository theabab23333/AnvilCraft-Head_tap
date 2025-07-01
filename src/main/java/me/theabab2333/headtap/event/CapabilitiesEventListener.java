package me.theabab2333.headtap.event;


import dev.dubhe.anvilcraft.init.ModBlockEntities;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.api.itemhandler.ResinCauldronWrapper;
import me.theabab2333.headtap.block.entity.ResinExtractorBlockEntity;
import me.theabab2333.headtap.block.entity.StoneGeneratorBlockEntity;
import me.theabab2333.headtap.block.entity.VariableFluidTankBlockEntity;
import me.theabab2333.headtap.init.ModBlocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = HeadTap.MODID)
public class CapabilitiesEventListener {

    // 从Create那边学的 个人感觉好看
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        StoneGeneratorBlockEntity.registerCapabilities(event);
        ResinExtractorBlockEntity.registerCapabilities(event);
        VariableFluidTankBlockEntity.registerCapabilities(event);

        // Other
        event.registerBlock(
            Capabilities.ItemHandler.BLOCK,
            ((level, pos, state, blockEntity, side) -> new ResinCauldronWrapper(level, pos)),
            ModBlocks.RESIN_FLUID_CAULDRON.get());

        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.CRAB_TRAP.get(),
            (be, side) -> be.getItemHandler()
        );
    }
}
