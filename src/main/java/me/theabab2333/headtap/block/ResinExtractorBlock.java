package me.theabab2333.headtap.block;

import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import dev.dubhe.anvilcraft.block.better.BetterBaseEntityBlock;
import me.theabab2333.headtap.block.entity.ResinExtractorBlockEntity;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResinExtractorBlock extends BetterBaseEntityBlock implements IHammerRemovable {

    public ResinExtractorBlock(Properties Properties) {
        super(Properties);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ResinExtractorBlock::new);
    }

    public static VoxelShape SHAPE =
        Shapes.or(
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(2, 2, 2, 14, 14, 14),
            Block.box(0, 0, 0, 16, 2, 16)
        );

    public BlockEntity newBlockEntity(BlockPos blockpos, BlockState blockstate) {
        return new ResinExtractorBlockEntity(ModBlockEntities.RESIN_EXTRACTOR.get(), blockpos, blockstate);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(
        BlockState state,
        BlockGetter level,
        BlockPos pos,
        CollisionContext context) {
        return SHAPE;
    }

    public ItemInteractionResult useItemOn(
        ItemStack stack,
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hitResult
    ) {
        if (super.useItemOn(
            stack,
            state,
            level,
            pos,
            player,
            hand,
            hitResult
        ).result() == InteractionResult.PASS) {
            if (level.getBlockEntity(pos) instanceof ResinExtractorBlockEntity tank && tank.onPlayerUse(player, hand)) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}