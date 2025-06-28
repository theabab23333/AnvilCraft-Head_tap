package me.theabab2333.headtap.block;

import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.block.entity.AccelerationRingBlockEntity;
import dev.dubhe.anvilcraft.block.multipart.FlexibleMultiPartBlock;
import dev.dubhe.anvilcraft.block.state.DirectionCube3x3PartHalf;
import me.theabab2333.headtap.block.entity.VariableFluidTankBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VariableFluidTankBlock extends FlexibleMultiPartBlock<DirectionCube3x3PartHalf, DirectionProperty, Direction>
    implements EntityBlock, IHammerRemovable {

    public static final EnumProperty<DirectionCube3x3PartHalf> HALF = EnumProperty.create("half", DirectionCube3x3PartHalf.class);
    public static final BooleanProperty OVERLOAD = IPowerComponent.OVERLOAD;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<IPowerComponent.Switch> SWITCH = IPowerComponent.SWITCH;

    public VariableFluidTankBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition
            .any()
            .setValue(HALF, DirectionCube3x3PartHalf.BOTTOM_CENTER)
            .setValue(OVERLOAD, true)
            .setValue(FACING, Direction.NORTH)
            .setValue(SWITCH, IPowerComponent.Switch.ON)
        );
    }

    @Override
    public Property<DirectionCube3x3PartHalf> getPart() {
        return HALF;
    }

    @Override
    public DirectionCube3x3PartHalf[] getParts() {
        return DirectionCube3x3PartHalf.values();
    }

    @Override
    public DirectionProperty getAdditionalProperty() {
        return FACING;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, OVERLOAD, FACING, SWITCH);
    }

    @Override
    protected BlockState placedState(DirectionCube3x3PartHalf part, BlockState state) {
        return state
            .setValue(this.getPart(), part);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
            .setValue(FACING, context.getPlayer() != null && context.getPlayer().isShiftKeyDown() ? context.getNearestLookingDirection().getOpposite() : context.getNearestLookingDirection());
    }

    @Override
    public void neighborChanged(
        BlockState state,
        Level level,
        BlockPos pos,
        Block neighborBlock,
        BlockPos neighborPos,
        boolean movedByPiston
    ) {
        boolean isSignal = Arrays.stream(getParts()).anyMatch(it -> level.hasNeighborSignal(pos.subtract(state.getValue(getPart()).getOffset()).offset(it.getOffset())));
        if (isSignal && state.getValue(SWITCH) == IPowerComponent.Switch.ON) {
            updateState(level, pos, SWITCH, IPowerComponent.Switch.OFF, 3);
        } else if (!isSignal && state.getValue(SWITCH) == IPowerComponent.Switch.OFF) {
            updateState(level, pos, SWITCH, IPowerComponent.Switch.ON, 3);
            BlockPos centerPos = pos.subtract(state.getValue(HALF).getOffset()).offset(0, 1, 0);
            if (level.getBlockEntity(centerPos) instanceof IPowerConsumer powerConsumer) {
                if (powerConsumer.getGrid() == null) return;
                powerConsumer.getGrid().flush();
            }
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0,0,0,16,16,16);
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new VariableFluidTankBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (level1, pos, state1, entity) -> {
            if (entity instanceof AccelerationRingBlockEntity be) be.tick();
        };
    }

    @Override
    protected float getShadeBrightness(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos) {
        return 1.0F;
    }
}
