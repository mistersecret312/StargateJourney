package net.povstalec.sgjourney.common.blocks.stargate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.blockstates.Orientation;
import net.povstalec.sgjourney.common.blockstates.StargatePart;
import net.povstalec.sgjourney.common.config.CommonStargateConfig;
import net.povstalec.sgjourney.common.stargate.Address;
import net.povstalec.sgjourney.common.stargate.Stargate;
import net.povstalec.sgjourney.common.stargate.StargateConnection;

public abstract class AbstractStargateBaseBlock extends AbstractStargateBlock implements EntityBlock
{
	public static final String EMPTY = StargateJourney.EMPTY;
	public static final String LOCAL_POINT_OF_ORIGIN = "LocalPointOfOrigin";
	
	public AbstractStargateBaseBlock(Properties properties, double width, double horizontalOffset)
	{
		super(properties, width, horizontalOffset);
	}
	
	public abstract AbstractStargateRingBlock getRing();

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		Player player = context.getPlayer();
		Orientation orientation = Orientation.getOrientationFromXRot(player);
		
		if(orientation == Orientation.REGULAR && blockpos.getY() > level.getMaxBuildHeight() - 6)
			return null;
		
		for(StargatePart part : getParts())
		{
			if(!part.equals(StargatePart.BASE) && !level.getBlockState(part.getRingPos(blockpos, context.getHorizontalDirection().getOpposite(), orientation)).canBeReplaced(context))
			{
				if(player != null)
					player.displayClientMessage(Component.translatable("block.sgjourney.stargate.not_enough_space"), true);
				return null;
			}
		}
		
		return this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER))
				.setValue(ORIENTATION, orientation);
	}
	 
	@Nullable
	@Override
	public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

	public abstract BlockState ringState();
	
	@Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
	{


		super.setPlacedBy(level, pos, state, placer, stack);
		
		for(StargatePart part : getParts())
		{
			if(!part.equals(StargatePart.BASE))
			{
				level.setBlock(part.getRingPos(pos,  state.getValue(FACING), state.getValue(ORIENTATION)), 
						ringState()
						.setValue(AbstractStargateRingBlock.PART, part)
						.setValue(AbstractStargateRingBlock.FACING, level.getBlockState(pos).getValue(FACING))
						.setValue(AbstractStargateRingBlock.ORIENTATION, level.getBlockState(pos).getValue(ORIENTATION))
						.setValue(WATERLOGGED,  Boolean.valueOf(level.getFluidState(part.getRingPos(pos, state.getValue(FACING), state.getValue(ORIENTATION))).getType() == Fluids.WATER)), 2);
			}
		}
	}
	
	public static void destroyStargate(Level level, BlockPos pos, ArrayList<StargatePart> parts, Direction direction, Orientation orientation)
	{
		if(direction == null)
		{
			StargateJourney.LOGGER.error("Failed to destroy Stargate because direction is null");
			return;
		}
		
		if(orientation == null)
		{
			StargateJourney.LOGGER.error("Failed to destroy Stargate because orientation is null");
			return;
		}
		
		for(StargatePart part : parts)
		{
			BlockPos ringPos = part.getRingPos(pos, direction, orientation);
			BlockState state = level.getBlockState(ringPos);
			
			if(state.getBlock() instanceof AbstractStargateBlock)
			{
				boolean waterlogged = state.getBlock() instanceof AbstractStargateRingBlock ? state.getValue(AbstractStargateRingBlock.WATERLOGGED) : false;
				
				level.setBlock(ringPos, waterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 3);
			}
		}
	}
	
	@Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(oldState.getBlock() != newState.getBlock())
        {
    		BlockEntity blockentity = level.getBlockEntity(pos);
    		if(blockentity instanceof AbstractStargateEntity stargate)
    		{
    			stargate.bypassDisconnectStargate(Stargate.Feedback.STARGATE_DESTROYED, false);
    			stargate.unsetDHD(true);
    			stargate.removeStargateFromNetwork();
    		}
    		
    		/*for(StargatePart part : getParts())
    		{
    			if(!part.equals(StargatePart.BASE))
    			{
    				BlockPos ringPos = part.getRingPos(pos, oldState.getValue(FACING), oldState.getValue(ORIENTATION));
        			BlockState state = level.getBlockState(ringPos);
        			
        			if(state.getBlock() instanceof AbstractStargateBlock)
        			{
        				boolean waterlogged = state.getBlock() instanceof AbstractStargateRingBlock ? state.getValue(AbstractStargateRingBlock.WATERLOGGED) : false;
        				
        				level.setBlock(ringPos, waterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 3);
        			}
    			}
    		}*/
    		destroyStargate(level, pos, getParts(), oldState.getValue(FACING), oldState.getValue(ORIENTATION));
    		
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
	
	public void updateStargate(Level level, BlockPos pos, BlockState state, StargateConnection.State connectionState, int chevronsActive)
	{
		level.setBlock(pos, state.setValue(AbstractStargateBaseBlock.CONNECTION_STATE, connectionState).setValue(AbstractStargateBaseBlock.CHEVRONS_ACTIVE, chevronsActive), 2);
		
		for(StargatePart part : getParts())
		{
			if(!part.equals(StargatePart.BASE))
			{
				BlockPos ringPos = part.getRingPos(pos,  state.getValue(FACING), state.getValue(ORIENTATION));
				if(level.getBlockState(ringPos).getBlock() instanceof AbstractStargateBlock)
				{
					level.setBlock(part.getRingPos(pos,  state.getValue(FACING), state.getValue(ORIENTATION)), 
							ringState()
							.setValue(AbstractStargateRingBlock.PART, part)
							.setValue(AbstractStargateRingBlock.CONNECTION_STATE, level.getBlockState(pos).getValue(CONNECTION_STATE))
							.setValue(AbstractStargateRingBlock.CHEVRONS_ACTIVE, level.getBlockState(pos).getValue(CHEVRONS_ACTIVE))
							.setValue(AbstractStargateRingBlock.FACING, level.getBlockState(pos).getValue(FACING))
							.setValue(AbstractStargateRingBlock.ORIENTATION, level.getBlockState(pos).getValue(ORIENTATION))
							.setValue(AbstractStargateRingBlock.WATERLOGGED,  Boolean.valueOf(level.getFluidState(part.getRingPos(pos, state.getValue(FACING), state.getValue(ORIENTATION))).getType() == Fluids.WATER)), 3);
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		long energy = 0;
		String id = "";

		if(stack.getComponents().has(DataComponents.BLOCK_ENTITY_DATA))
		{
			CompoundTag blockEntityTag = stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag();

			if(blockEntityTag.contains(AbstractStargateEntity.VARIANT))
			{
				String variant = blockEntityTag.getString(AbstractStargateEntity.VARIANT);

				if(!variant.equals(EMPTY))
					tooltipComponents.add(Component.translatable("tooltip.sgjourney.variant").append(Component.literal(": " + variant)).withStyle(ChatFormatting.GREEN));
			}
		}

		tooltipComponents.add(Component.translatable("tooltip.sgjourney.energy").append(Component.literal(": " + energy + " FE")).withStyle(ChatFormatting.DARK_RED));


		if(stack.getComponents().has(DataComponents.BLOCK_ENTITY_DATA))
		{
			CompoundTag blockEntityTag = stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag();

			if((blockEntityTag.contains(AbstractStargateEntity.DISPLAY_ID) && blockEntityTag.getBoolean(AbstractStargateEntity.DISPLAY_ID)) || CommonStargateConfig.always_display_stargate_id.get())
			{
				if(blockEntityTag.contains(AbstractStargateEntity.ID))
				{
					id = blockEntityTag.getString(AbstractStargateEntity.ID);
					tooltipComponents.add(Component.translatable("tooltip.sgjourney.9_chevron_address").append(Component.literal(": " + id)).withStyle(ChatFormatting.AQUA));
				}
				else if(blockEntityTag.contains(AbstractStargateEntity.ID_9_CHEVRON_ADDRESS))
				{

					id = new Address(blockEntityTag.getIntArray(AbstractStargateEntity.ID_9_CHEVRON_ADDRESS)).toString();
					tooltipComponents.add(Component.translatable("tooltip.sgjourney.9_chevron_address").append(Component.literal(": " + id)).withStyle(ChatFormatting.AQUA));
				}

			}

			if((blockEntityTag.contains(AbstractStargateEntity.UPGRADED) && blockEntityTag.getBoolean(AbstractStargateEntity.UPGRADED)))
				tooltipComponents.add(Component.translatable("tooltip.sgjourney.upgraded").withStyle(ChatFormatting.DARK_GREEN));

			if((blockEntityTag.contains(LOCAL_POINT_OF_ORIGIN)))
				tooltipComponents.add(Component.translatable("tooltip.sgjourney.local_point_of_origin").withStyle(ChatFormatting.GREEN));
		}

		if(stack.getComponents().has(DataComponents.BLOCK_ENTITY_DATA) && stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().getCompound("BlockEntityTag").contains(AbstractStargateEntity.ADD_TO_NETWORK) && !stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().getCompound("BlockEntityTag").getBoolean(AbstractStargateEntity.ADD_TO_NETWORK))
			tooltipComponents.add(Component.translatable("tooltip.sgjourney.not_added_to_network").withStyle(ChatFormatting.YELLOW));

		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
	}
	
	public static ItemStack excludeFromNetwork(ItemStack stack)
	{
        CompoundTag compoundtag = new CompoundTag();
		compoundtag.putString("id", stack.getItem().toString());
		compoundtag.putBoolean(AbstractStargateEntity.ADD_TO_NETWORK, false);
		stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundtag));
		
		return stack;
	}
	
	public static ItemStack localPointOfOrigin(ItemStack stack)
	{
        CompoundTag compoundtag = new CompoundTag();
		compoundtag.putString("id", stack.getItem().toString());
		compoundtag.putBoolean(LOCAL_POINT_OF_ORIGIN, true);
		stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundtag));

		return stack;
	}

	public static ItemStack addToNetworkTrue(ItemStack stack)
	{
		CompoundTag compoundTag = new CompoundTag();
		compoundTag.putString("id", stack.getItem().toString());
		stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundTag));

		return stack;
	}

	@Override
	public AbstractStargateEntity getStargate(Level level, BlockPos pos, BlockState state)
	{
		BlockEntity blockentity = level.getBlockEntity(pos);
		
		if(blockentity instanceof AbstractStargateEntity stargate)
			return stargate;
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> typeA, BlockEntityType<E> typeB, BlockEntityTicker<? super E> ticker)
	{
		return typeB == typeA ? (BlockEntityTicker<A>)ticker : null;
	}
}
