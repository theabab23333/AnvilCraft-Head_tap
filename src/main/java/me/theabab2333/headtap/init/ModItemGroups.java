package me.theabab2333.headtap.init;

import me.theabab2333.headtap.HeadTap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static me.theabab2333.headtap.HeadTap.REGISTRATE;

@SuppressWarnings("unused")
public class ModItemGroups {
    private static final DeferredRegister<CreativeModeTab> ET =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HeadTap.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ANVILCRAFT_HEAD_TAP =
            ET.register("block", () -> CreativeModeTab.builder()
                    .icon(ModBlocks.AMETHYST_ANVIL::asStack)
                    .title(REGISTRATE.addLang("itemGroup", HeadTap.of("block"), "AnvilCraft: HeadTap"))
                    .build());
    public static void register(IEventBus modEventBus) {
        ET.register(modEventBus);
    }
}
