package me.theabab2333.headtap.anvil;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import dev.dubhe.anvilcraft.api.event.anvil.AnvilFallOnLandEvent;
import me.theabab2333.headtap.block.entity.StoneGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HitStoneGeneratorBehavior implements IAnvilBehavior {
    @Override
    public boolean handle(
        Level level,
        BlockPos hitBlockPos,
        BlockState hitBlockState,
        float fallDistance,
        AnvilFallOnLandEvent event
    ) {
        if (!hitBlockState.hasBlockEntity()) return false;
        StoneGeneratorBlockEntity blockEntity = (StoneGeneratorBlockEntity) level.getBlockEntity(hitBlockPos);
        if (blockEntity == null) return false;
        int count = (int) fallDistance + 1;
        count = count <= 0 ? 1 : count;
        return blockEntity.tryGenerateStone(count);
    }
}
