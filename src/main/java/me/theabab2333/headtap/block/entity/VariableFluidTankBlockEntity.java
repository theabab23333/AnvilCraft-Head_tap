package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.init.ModBlocks;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VariableFluidTankBlockEntity extends BlockEntity implements IPowerConsumer {
    public int power = 16;
    public int hasVoid = 0;
    private PowerGrid grid;
    public FluidTank tank = new FluidTank(0);

    public static final Map<Block, Integer> MULTIPLICATION = new HashMap<>();

    static {
        MULTIPLICATION.put(Blocks.IRON_BLOCK, 1);
        MULTIPLICATION.put(Blocks.GOLD_BLOCK, 3);
        MULTIPLICATION.put(Blocks.DIAMOND_BLOCK, 4);
        MULTIPLICATION.put(ModBlocks.MAGNET_BLOCK.get(), 2);
        MULTIPLICATION.put(ModBlocks.ROYAL_STEEL_BLOCK.get(), 8);
        MULTIPLICATION.put(ModBlocks.FROST_METAL_BLOCK.get(), 16);
        MULTIPLICATION.put(ModBlocks.EMBER_METAL_BLOCK.get(), 32);
        MULTIPLICATION.put(ModBlocks.TRANSCENDIUM_BLOCK.get(), 128);

        MULTIPLICATION.put(ModBlocks.MULTIPHASE_MATTER_BLOCK.get(), 0);
        MULTIPLICATION.put(ModBlocks.VOID_MATTER_BLOCK.get(), 0);
    }

    public void gridTick() {
        if (level == null || level.isClientSide()) return;
        int amount = countBlocksInRange();
        if (!grid.isWorking()) return;
        power = amount / 8 + 16;
        int tankCapacity = amount * 1000 * 4;
        tank.setCapacity(tankCapacity);
        if (tank.getFluidAmount() >= tankCapacity) tank.setCapacity(tankCapacity);
        if (hasVoid  != 0 && tank.getFluidAmount() >= tankCapacity ) {
            FluidStack fluidTank = tank.getFluid();
            tank.setCapacity(tankCapacity + 16000);
            fluidTank.setAmount(tankCapacity);
        }
    }

    public int countBlocksInRange() {
        if (level == null || level.isClientSide()) return 0;
        int blockCount = 0;
        int multiplication = 0;
        int hasVoid = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    BlockPos thisPos = this.getBlockPos();
                    BlockPos otherPos = new BlockPos(thisPos.getX() + i, thisPos.getY() + j, thisPos.getZ() + k);
                    BlockState otherState = level.getBlockState(otherPos);
                    Integer otherBlock = MULTIPLICATION.get(otherState.getBlock());
                    if (otherBlock != null) {
                        blockCount++;
                        multiplication = multiplication + otherBlock;
                        if (otherState.is(ModBlocks.VOID_MATTER_BLOCK.get())) {
                            hasVoid++;
                        }
                    }
                }
            }
        }
        this.hasVoid = hasVoid;
        return blockCount * multiplication;
    }


    public VariableFluidTankBlockEntity(BlockPos pos, BlockState blockState) {
        this(ModBlockEntities.VARIABLE_FLUID_TANK.get(), pos, blockState);
    }

    private VariableFluidTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static VariableFluidTankBlockEntity createBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        return new VariableFluidTankBlockEntity(type, pos, blockState);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK,
            ModBlockEntities.VARIABLE_FLUID_TANK.get(),
            (be, context) -> be.tank
        );
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tank.readFromNBT(registries, tag);
        this.power = tag.getInt("Power");
    }

    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tank.writeToNBT(registries, tag);
        tag.putInt("Power", this.power);
    }

    public int getInputPower() {
        return power;
    }

    @Nullable
    public Level getCurrentLevel() {
        return level;
    }

    public BlockPos getPos() {
        return this.getBlockPos();
    }

    @Override
    public void setGrid(@Nullable PowerGrid grid) {
        this.grid = grid;
    }

    public PowerGrid getGrid() {
        return this.grid;
    }
}
