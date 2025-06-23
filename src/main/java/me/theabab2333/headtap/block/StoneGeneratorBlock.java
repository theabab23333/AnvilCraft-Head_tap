package me.theabab2333.headtap.block;

import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.block.better.BetterBaseEntityBlock;
import me.theabab2333.headtap.block.entity.StoneGeneratorBlockEntity;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StoneGeneratorBlock extends BetterBaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public StoneGeneratorBlock(Properties Properties) {
        super(Properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(StoneGeneratorBlock::new);
    }

    public BlockEntity newBlockEntity(BlockPos blockpos, BlockState blockstate) {
        return new StoneGeneratorBlockEntity(ModBlockEntities.STONE_GENERATOR.get(), blockpos, blockstate);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean movedByPiston
    ) {
        if (state.is(newState.getBlock())) return;
        if (level.getBlockEntity(pos) instanceof StoneGeneratorBlockEntity entity) {
            Vec3 vec3 = entity.getBlockPos().getCenter();
            IItemHandler itemHandler = entity.getItemHandler();
            for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                Containers.dropItemStack(level, vec3.x, vec3.y, vec3.z, itemHandler.getStackInSlot(slot));
            }
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (!level.isClientSide()) {
            StoneGeneratorBlockEntity blockEntity = (StoneGeneratorBlockEntity) level.getBlockEntity(pos);
            if (blockEntity != null) {
                IItemHandler itemHandler = blockEntity.getItemHandler();
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    ItemStack stack = itemHandler.getStackInSlot(i);
                    if (stack.isEmpty()) continue;
                    Vec3 center = pos.relative(Direction.UP).getCenter();
                    ItemEntity itemEntity = new ItemEntity(level, center.x(), center.y(), center.z(), stack, 0, 0.0, 0);
                    itemEntity.setDefaultPickUpDelay();
                    level.addFreshEntity(itemEntity);
                    itemHandler.extractItem(i, stack.getCount(), false);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
