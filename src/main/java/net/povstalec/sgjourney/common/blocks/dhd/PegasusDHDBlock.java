package net.povstalec.sgjourney.common.blocks.dhd;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.povstalec.sgjourney.common.block_entities.dhd.AbstractDHDEntity;
import net.povstalec.sgjourney.common.block_entities.dhd.PegasusDHDEntity;
import net.povstalec.sgjourney.common.init.BlockEntityInit;
import net.povstalec.sgjourney.common.init.BlockInit;
import net.povstalec.sgjourney.common.init.ItemInit;
import net.povstalec.sgjourney.common.items.crystals.CommunicationCrystalItem;
import net.povstalec.sgjourney.common.items.crystals.EnergyCrystalItem;
import net.povstalec.sgjourney.common.items.crystals.TransferCrystalItem;
import net.povstalec.sgjourney.common.menu.PegasusDHDMenu;
import net.povstalec.sgjourney.common.misc.InventoryHelper;
import net.povstalec.sgjourney.common.misc.InventoryUtil;

public class PegasusDHDBlock extends AbstractDHDBlock implements SimpleWaterloggedBlock
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public PegasusDHDBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(PegasusDHDBlock::new);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state)
	{
		state.add(FACING).add(WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) 
	{
		return new PegasusDHDEntity(pos, state);
	}

	@Override
	public FluidState getFluidState(BlockState state)
	{
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState pState, Level level, BlockPos pos, Player player, BlockHitResult pHitResult) {
		if(!level.isClientSide())
		{
			BlockEntity blockEntity = level.getBlockEntity(pos);

			if(blockEntity instanceof AbstractDHDEntity dhd)
			{
				dhd.setStargate();

				if(player.isShiftKeyDown())
					this.openCrystalMenu(player, blockEntity);
				else
				{
					MenuProvider containerProvider = new MenuProvider()
					{
						@Override
						public Component getDisplayName()
						{
							return Component.translatable("screen.sgjourney.dhd");
						}

						@Override
						public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity)
						{
							return new PegasusDHDMenu(windowId, playerInventory, blockEntity);
						}
					};
					player.openMenu(containerProvider, blockEntity.getBlockPos());
				}
			}
			else
			{
				throw new IllegalStateException("Our named container provider is missing!");
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public Block getDHD()
	{
		return BlockInit.PEGASUS_DHD.get();
	}
	
	@Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, BlockEntityInit.PEGASUS_DHD.get(), AbstractDHDEntity::tick);
    }
	
	public static ItemStack pegasusCrystalSetup()
	{
		ItemStack stack = new ItemStack(BlockInit.PEGASUS_DHD.get());
        CompoundTag blockEntityTag = new CompoundTag();
        CompoundTag inventory = new CompoundTag();
        
        blockEntityTag.putString("id", "sgjourney:pegasus_dhd");
        blockEntityTag.putLong("Energy", 0);
        
        inventory.putInt("Size", 9);
        inventory.put("Items", setupPegasusInventory());
        
        blockEntityTag.put("Inventory", inventory);
		stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityTag));
		
		return stack;
	}
	
	private static ListTag setupPegasusInventory()
	{
		ListTag nbtTagList = new ListTag();
		
		nbtTagList.add(InventoryHelper.addItem(0, ItemInit.ADVANCED_CONTROL_CRYSTAL.get().asItem().toString(), 1, null));
		nbtTagList.add(InventoryHelper.addItem(1, ItemInit.ADVANCED_ENERGY_CRYSTAL.get().asItem().toString(), 1, null));
		nbtTagList.add(InventoryHelper.addItem(2, ItemInit.ADVANCED_COMMUNICATION_CRYSTAL.get().asItem().toString(), 1, null));
		nbtTagList.add(InventoryHelper.addItem(3, ItemInit.ADVANCED_ENERGY_CRYSTAL.get().asItem().toString(), 1, null));
		nbtTagList.add(InventoryHelper.addItem(6, ItemInit.ADVANCED_COMMUNICATION_CRYSTAL.get().asItem().toString(), 1, null));
		nbtTagList.add(InventoryHelper.addItem(7, ItemInit.ADVANCED_TRANSFER_CRYSTAL.get().asItem().toString(), 1, null));
		
		return nbtTagList;
	}

	@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
    {
		if(stack.getComponents().has(DataComponents.BLOCK_ENTITY_DATA))
		{
			CompoundTag blockEntityTag = stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag();
			ListTag tagList = blockEntityTag.getCompound("Inventory").getList("Items", Tag.TAG_COMPOUND);
			
			if(tagList.size() > 0)
			{
				CompoundTag list1 = tagList.getCompound(0);
				
				if(list1.contains("id", Tag.TAG_STRING) && list1.getString("id").equals(ItemInit.ADVANCED_CONTROL_CRYSTAL.get().asItem().toString()) && list1.contains("Count", Tag.TAG_BYTE) && list1.getByte("Count") > 0)
			        tooltipComponents.add(Component.translatable("tooltip.sgjourney.has_control_crystal").withStyle(ChatFormatting.AQUA));
			}
		}
		
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
    }
}
