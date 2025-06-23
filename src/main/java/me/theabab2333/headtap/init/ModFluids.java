package me.theabab2333.headtap.init;

import dev.dubhe.anvilcraft.util.ModClientFluidTypeExtensionImpl;
import me.theabab2333.headtap.HeadTap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, HeadTap.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, HeadTap.MODID);

    public static final DeferredHolder<FluidType, FluidType> RESIN_FLUID_TYPE = FLUID_TYPES.register(
        "resin_fluid",
        () -> new FluidType(FluidType.Properties.create()
            .descriptionId("block.headtap.resin_fluid")
            .canSwim(false)
            .canDrown(false)
            .pathType(PathType.LAVA)
            .adjacentPathType(null)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
            .lightLevel(15)
            .density(3000)
            .viscosity(6000)
            .temperature(1300)
        ));
    public static final DeferredHolder<Fluid, BaseFlowingFluid> RESIN_FLUID = FLUIDS.register(
        "resin_fluid",
        () -> new BaseFlowingFluid.Source(ModFluids.RESIN_FLUID_PROPERTIES)
    );
    public static final DeferredHolder<Fluid, BaseFlowingFluid> FLOWING_RESIN_FLUID = FLUIDS.register(
        "flowing_resin_fluid",
        () -> new BaseFlowingFluid.Flowing(ModFluids.RESIN_FLUID_PROPERTIES)
    );
    public static final BaseFlowingFluid.Properties RESIN_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(RESIN_FLUID_TYPE, RESIN_FLUID, FLOWING_RESIN_FLUID)
        .block(ModBlocks.RESIN_FLUID)
        .bucket(ModItems.RESIN_FLUID_BUCKET)
        .tickRate(20)
        .explosionResistance(100);

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }

    public static void onRegisterFluidType(RegisterClientExtensionsEvent e) {
        e.registerFluidType(new ModClientFluidTypeExtensionImpl(
            HeadTap.of("block/resin_fluid"),
            HeadTap.of("block/resin_fluid_flow"),
            0xB7EEDE,
            2.0f
        ), RESIN_FLUID_TYPE);
    }
}
