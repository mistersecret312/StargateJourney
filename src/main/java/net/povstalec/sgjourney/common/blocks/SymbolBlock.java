package net.povstalec.sgjourney.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.povstalec.sgjourney.common.block_entities.SymbolBlockEntity;
import net.povstalec.sgjourney.common.blocks.dhd.ClassicDHDBlock;
import net.povstalec.sgjourney.common.blockstates.Orientation;
import net.povstalec.sgjourney.common.init.BlockInit;
import net.povstalec.sgjourney.common.sgjourney.PointOfOrigin;
import net.povstalec.sgjourney.common.sgjourney.Symbols;

public abstract class SymbolBlock extends DirectionalBlock implements EntityBlock
{
	public static final EnumProperty<Orientation> ORIENTATION = EnumProperty.create("orientation", Orientation.class);
	
	protected SymbolBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ORIENTATION, Orientation.REGULAR));
	}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
    	builder.add(FACING).add(ORIENTATION);
	}

    @Override
	public BlockState rotate(BlockState state, Rotation rotation)
	{
	      return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

    @Override
	public BlockState getStateForPlacement(BlockPlaceContext context) 
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(ORIENTATION, Orientation.getOrientationFromXRot(context.getPlayer()));
	}
	
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}
	
	@Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
        if (!level.isClientSide())
        {
            return (localLevel, pos, blockState, entity) -> {
                if (entity instanceof SymbolBlockEntity symbol) 
                {
                	symbol.tick(localLevel, pos, blockState);
                }
            };
        }
        return null;
    }

	public boolean use(Level level, BlockPos pos, Player player)
	{
		if(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
		{
			if(!level.isClientSide())
			{
				BlockEntity blockEntity = level.getBlockEntity(pos);
				
				if(blockEntity instanceof SymbolBlockEntity symbolBlock)
				{
					int symbolNumber = symbolBlock.getSymbolNumber();
					MutableComponent text;
					
					player.sendSystemMessage(Component.translatable("info.sgjourney.symbol_number").append(Component.literal(": " + symbolNumber)).withStyle(ChatFormatting.YELLOW));
					
					if(symbolNumber == 0)
					{
						MutableComponent pointOfOrigin = Component.literal(symbolBlock.getPointOfOrigin().toString());
						text = Component.translatable("info.sgjourney.point_of_origin").append(Component.literal(": ")).append(pointOfOrigin).withStyle(ChatFormatting.DARK_PURPLE);
					}
					else
					{
						MutableComponent symbols = Component.literal(symbolBlock.getSymbols().toString());
						text = Component.translatable("info.sgjourney.symbols").append(Component.literal(": ")).append(symbols).withStyle(ChatFormatting.LIGHT_PURPLE);
					}
					
					player.sendSystemMessage(text);
				}
			}
			return true;
		}
        else
			return false;
    }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
	{
		return use(level, pos, player) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		return use(level, pos, player) ? ItemInteractionResult.SUCCESS : ItemInteractionResult.FAIL;
	}
	
	public abstract ItemLike getItem();
    
    @Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		BlockEntity blockentity = level.getBlockEntity(pos);
		if(!level.isClientSide() && !player.isCreative() && player.hasCorrectToolForDrops(state))
		{
			ItemStack itemstack = new ItemStack(getItem());
			
			blockentity.saveToItem(itemstack, level.registryAccess());

			ItemEntity itementity = new ItemEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemstack);
			itementity.setDefaultPickUpDelay();
			level.addFreshEntity(itementity);
		}

		return super.playerWillDestroy(level, pos, state, player);
	}

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
    	Minecraft minecraft = Minecraft.getInstance();
		ClientPacketListener clientPacketListener = minecraft.getConnection();
		RegistryAccess registries = clientPacketListener.registryAccess();

    	int symbolNumber = 0;
		String symbol = "";
    	String symbols = "";

		if(stack.has(DataComponents.BLOCK_ENTITY_DATA))
		{
			CompoundTag blockEntityTag = stack.get(DataComponents.BLOCK_ENTITY_DATA).getUnsafe();
        	
        	if(blockEntityTag.contains(SymbolBlockEntity.SYMBOL_NUMBER))
            	symbolNumber = blockEntityTag.getInt(SymbolBlockEntity.SYMBOL_NUMBER);

        	if(symbolNumber == 0 && blockEntityTag.contains(SymbolBlockEntity.SYMBOL))
        	{
            	String pointOfOrigin = blockEntityTag.getString(SymbolBlockEntity.SYMBOL);
        		ResourceLocation location = ResourceLocation.tryParse(pointOfOrigin);
            	
            	if(location == null)
            		symbol = "Invalid Path"; //TODO make translatable
            	else
            	{
            		Registry<PointOfOrigin> pointOfOriginRegistry = registries.registryOrThrow(PointOfOrigin.REGISTRY_KEY);
            		
            		if(pointOfOriginRegistry.get(ResourceLocation.parse(pointOfOrigin)) != null)
                		symbol = pointOfOriginRegistry.get(ResourceLocation.parse(pointOfOrigin)).getName();
                	else
                		symbol = "Error";
            	}
        	}

        	if(symbolNumber != 0 && blockEntityTag.contains(SymbolBlockEntity.SYMBOLS))
        	{
        		ResourceLocation location = ResourceLocation.tryParse(blockEntityTag.getString(SymbolBlockEntity.SYMBOLS));
            	
            	if(location == null)
            		symbols = "Invalid Path";
            	else
            	{
            		Registry<Symbols> symbolsRegistry = registries.registryOrThrow(Symbols.REGISTRY_KEY);
            		
            		if(symbolsRegistry.get(ResourceLocation.parse(symbols)) != null)
            			symbols = symbolsRegistry.get(ResourceLocation.parse(symbols)).getName();
                	else
                		symbols = "Error";
            	}
        	}
    	}
		
		if(symbolNumber == 0)
			tooltipComponents.add(Component.translatable("tooltip.sgjourney.symbol").append(Component.literal(": ").append(Component.translatable(symbol))).withStyle(ChatFormatting.DARK_PURPLE));
		else
		{
			tooltipComponents.add(Component.translatable("tooltip.sgjourney.symbol_number").append(Component.literal(": ").append("" + symbolNumber)).withStyle(ChatFormatting.YELLOW));
			tooltipComponents.add(Component.translatable("tooltip.sgjourney.symbols").append(Component.literal(": ").append(Component.translatable(symbols))).withStyle(ChatFormatting.LIGHT_PURPLE));
		}
    	
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
    
    public static class Stone extends SymbolBlock
    {
		public static final MapCodec<Stone> CODEC = simpleCodec(Stone::new);

		public Stone(Properties properties)
		{
			super(properties);
		}

		protected MapCodec<Stone> codec() {
			return CODEC;
		}

		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new SymbolBlockEntity.Stone(pos, state);
		}

		@Override
		public ItemLike getItem()
		{
			return BlockInit.STONE_SYMBOL.get();
		}
    	
    }
    
    public static class Sandstone extends SymbolBlock
    {
		public static final MapCodec<Sandstone> CODEC = simpleCodec(Sandstone::new);

		public Sandstone(Properties properties)
		{
			super(properties);
		}

		protected MapCodec<Sandstone> codec() {
			return CODEC;
		}

		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new SymbolBlockEntity.Sandstone(pos, state);
		}

		@Override
		public ItemLike getItem()
		{
			return BlockInit.SANDSTONE_SYMBOL.get();
		}
    	
    }
	
	public static class RedSandstone extends SymbolBlock
	{
		public static final MapCodec<RedSandstone> CODEC = simpleCodec(RedSandstone::new);

		public RedSandstone(Properties properties)
		{
			super(properties);
		}

		protected MapCodec<RedSandstone> codec() {
			return CODEC;
		}
		
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new SymbolBlockEntity.RedSandstone(pos, state);
		}
		
		@Override
		public ItemLike getItem()
		{
			return BlockInit.RED_SANDSTONE_SYMBOL.get();
		}
		
	}
}
