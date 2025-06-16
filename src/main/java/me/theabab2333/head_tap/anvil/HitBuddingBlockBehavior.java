package me.theabab2333.head_tap.anvil;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import dev.dubhe.anvilcraft.api.event.anvil.AnvilFallOnLandEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

import static net.minecraft.world.level.block.BuddingAmethystBlock.canClusterGrowAtState;

public class HitBuddingBlockBehavior implements IAnvilBehavior {

    @Override
    public boolean handle(
        Level level,
        BlockPos hitBlockPos,
        BlockState hitBlockState,
        float fallDistance,
        AnvilFallOnLandEvent event
    ) {
        Direction[] DIRECTIONS = Direction.values();
        RandomSource random = level.getRandom();
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        BlockPos blockpos = hitBlockPos.relative(direction);
        BlockState blockstate = level.getBlockState(blockpos);
        Block block = null;
        if (canClusterGrowAtState(blockstate)) {
            block = Blocks.SMALL_AMETHYST_BUD;
        } else if (blockstate.is(Blocks.SMALL_AMETHYST_BUD) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
            block = Blocks.MEDIUM_AMETHYST_BUD;
        } else if (blockstate.is(Blocks.MEDIUM_AMETHYST_BUD) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
            block = Blocks.LARGE_AMETHYST_BUD;
        } else if (blockstate.is(Blocks.LARGE_AMETHYST_BUD) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
            block = Blocks.AMETHYST_CLUSTER;
        }

        if (block != null) {
            BlockState blockstate1 = (BlockState)((BlockState)block
                .defaultBlockState()
                .setValue(AmethystClusterBlock.FACING, direction))
                .setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
            level.setBlockAndUpdate(blockpos, blockstate1);
        }

        return false;
    }
}