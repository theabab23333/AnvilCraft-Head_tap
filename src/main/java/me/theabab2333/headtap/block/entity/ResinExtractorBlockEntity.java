package me.theabab2333.headtap.block.entity;

import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModFluids;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResinExtractorBlockEntity extends BlockEntity {
    public final FluidTank tank = new FluidTank(4000) {
        protected void onContentsChanged() {
            setChanged();
        }
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == ModFluids.RESIN_FLUID.get();
        }
    };

    public ResinExtractorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tank.writeToNBT(provider, tag);
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        tank.readFromNBT(provider, tag);
    }

    public boolean onPlayerUse(Player player, InteractionHand hand) {
        return FluidUtil.interactWithFluidHandler(player, hand, tank);
    }

    public IFluidHandler getFluidHandler() {
        return tank;
    }

    public boolean tryGenerateResin(int count) {
        if (this.level == null || this.level.isClientSide()) return false;
        Fluid resinType = getFluidType();
        if (resinType == Fluids.EMPTY) return false;
        int fluidAmount = this.tank.getFluidAmount();
        int addAmount = count*100 + fluidAmount;
        //我很菜(
        if (addAmount <= tank.getCapacity()) {
            tank.setFluid(new FluidStack(resinType, addAmount));
        } else {
            tank.setFluid(new FluidStack(resinType, tank.getCapacity()));
        }
        return tryGiveCauldronFluid();
    }

    public Fluid getFluidType() {
        boolean isTree = false;
        int logCount = 1;
        int leafCount = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos logPos = this.getBlockPos().relative(direction);
            if (level != null && !level.getBlockState(logPos).is(BlockTags.LOGS)) continue;

            BlockPos logHigh = logPos;
            for (int i = 0; i < 10; i++) {
                BlockPos x = logHigh.above();
                if (level != null && level.getBlockState(x).is(BlockTags.LOGS)) {
                    logHigh = x;
                    logCount = logCount + 1;
                    for (Direction direction1 : Direction.values()) {
                        BlockPos y = x.relative(direction1);
                        BlockState blockState = level.getBlockState(y);
                        if (blockState.is(BlockTags.LEAVES)) leafCount = leafCount + 1;
                    }
                }
            }
            if (logCount>= 4 && leafCount >= 8) isTree = true;
        }
        if (isTree) return ModFluids.RESIN_FLUID.get();
        return Fluids.EMPTY;
    }

    public boolean tryGiveCauldronFluid() {
        if (level == null || level.isClientSide()) return false;
        if (tank.getFluidAmount() <= 1000) return false;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos cauldronPos = this.getBlockPos().relative(direction);
            if (level.getBlockState(cauldronPos).is(Blocks.CAULDRON)) {
                level.setBlockAndUpdate(cauldronPos, ModBlocks.RESIN_FLUID_CAULDRON.get().defaultBlockState());
                tank.setFluid(new FluidStack(ModFluids.RESIN_FLUID.get(), tank.getFluidAmount() - 1000));
                break;
            }
        }
        return true;
    }
}
