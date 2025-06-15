package me.theabab2333.head_tap.mixin;

import dev.dubhe.anvilcraft.event.CapabilitiesEventListener;
import dev.dubhe.anvilcraft.init.ModBlockEntities;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CapabilitiesEventListener.class)
public class CapabilitiesEventListenerMixin {
    @Inject(method = "registerCapabilities", at =@At(value = "HEAD"))
    private static void registerCapabilities(RegisterCapabilitiesEvent event, CallbackInfo ci) {
        List.of(ModBlockEntities.CRAB_TRAP.get())
            .forEach(type -> event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                type,
                (be, side) -> be.getItemHandler())
            );
    }
}
