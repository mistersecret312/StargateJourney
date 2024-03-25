package net.povstalec.sgjourney.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.povstalec.sgjourney.common.block_entities.DestinyTimerEntity;
import net.povstalec.sgjourney.common.block_entities.TimerInterfaceEntity;
import net.povstalec.sgjourney.common.blocks.tech.BasicInterfaceBlock;
import org.jetbrains.annotations.Nullable;

public class TimerInterfaceBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public TimerInterfaceBlock(Properties prop) {
        super(prop);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
        state.add(FACING);
    }

    public boolean hasTimer(BlockState state, BlockPos pos, Level level){
        return level.getBlockState(pos.relative(state.getValue(FACING), 2)).getBlock() instanceof DestinyTimerBlock;
    }

    public DestinyTimerEntity getTimerEntity(BlockState state, BlockPos pos, Level level){
        if(this.hasTimer(state, pos, level)){
            return (DestinyTimerEntity) level.getBlockEntity(pos.relative(state.getValue(FACING), 2));
        } else return null;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos posthis, Block block, BlockPos otherpos, boolean bool) {
        if(level.getBestNeighborSignal(posthis) > 0){
            if(level.getBestNeighborSignal(posthis) == 15){
                this.getTimerEntity(state, posthis, level).resetTimer();
            }
            this.getTimerEntity(state, posthis, level).setState(DestinyTimerEntity.TimerState.PAUSED);

        } else this.getTimerEntity(state, posthis, level).setState(DestinyTimerEntity.TimerState.COUNTING);
    }

    public void updateInterface(BlockState state, Level level, BlockPos pos)
    {
        level.setBlock(pos, state.setValue(BasicInterfaceBlock.UPDATE, true), 3);
        level.scheduleTick(pos, this, 2);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TimerInterfaceEntity(pos, state);
    }
}
