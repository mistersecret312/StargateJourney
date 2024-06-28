package net.povstalec.sgjourney.common.blocks.dhd;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.registries.VanillaRegistries;
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
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.povstalec.sgjourney.common.block_entities.dhd.AbstractDHDEntity;
import net.povstalec.sgjourney.common.block_entities.dhd.MilkyWayDHDEntity;
import net.povstalec.sgjourney.common.init.BlockEntityInit;
import net.povstalec.sgjourney.common.init.BlockInit;
import net.povstalec.sgjourney.common.init.ItemInit;
import net.povstalec.sgjourney.common.items.crystals.CommunicationCrystalItem;
import net.povstalec.sgjourney.common.items.crystals.EnergyCrystalItem;
import net.povstalec.sgjourney.common.items.crystals.TransferCrystalItem;
import net.povstalec.sgjourney.common.menu.MilkyWayDHDMenu;
import net.povstalec.sgjourney.common.misc.InventoryHelper;
import net.povstalec.sgjourney.common.misc.InventoryUtil;

public class MilkyWayDHDBlock extends AbstractDHDBlock implements SimpleWaterloggedBlock
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public MilkyWayDHDBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(MilkyWayDHDBlock::new);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state)
	{
		state.add(FACING).add(WATERLOGGED);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) 
	{
		return new MilkyWayDHDEntity(pos, state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state)
	{
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
		if(!pLevel.isClientSide())
		{
			BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

			if(blockEntity instanceof AbstractDHDEntity dhd)
			{
				dhd.setStargate();

				if(pPlayer.isShiftKeyDown())
					this.openCrystalMenu(pPlayer, blockEntity);
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
							return new MilkyWayDHDMenu(windowId, playerInventory, blockEntity);
						}
					};
					pPlayer.openMenu(containerProvider, blockEntity.getBlockPos());
				}
			}
			else
			{
				throw new IllegalStateException("Our named container provider is missing!");
			}
		}
		return InteractionResult.SUCCESS;	}

	@Override
	public Block getDHD()
	{
		return BlockInit.MILKY_WAY_DHD.get();
	}
	
	@Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, BlockEntityInit.MILKY_WAY_DHD.get(), AbstractDHDEntity::tick);
    }
	
	public static ItemStack milkyWayCrystalSetup()
	{
		ItemStack stack = new ItemStack(BlockInit.MILKY_WAY_DHD.get());
        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.putString("id", "sgjourney:milky_way_dhd");

		ItemStackHandler handler = new ItemStackHandler(9);
		setupMilkyWayInventory(handler);
        blockEntityTag.put("Inventory", handler.serializeNBT(VanillaRegistries.createLookup()));
		stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityTag));
		
		return stack;
	}
	
	private static void setupMilkyWayInventory(ItemStackHandler handler)
	{

		handler.setStackInSlot(0, new ItemStack(ItemInit.LARGE_CONTROL_CRYSTAL.get()));
		handler.setStackInSlot(1, new ItemStack(ItemInit.COMMUNICATION_CRYSTAL.get()));
		handler.setStackInSlot(3, new ItemStack(ItemInit.ENERGY_CRYSTAL.get()));
		handler.setStackInSlot(5, new ItemStack(ItemInit.ENERGY_CRYSTAL.get()));
		handler.setStackInSlot(7, new ItemStack(ItemInit.TRANSFER_CRYSTAL.get()));
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext pContext, List<Component> tooltipComponents, TooltipFlag pTooltipFlag) {
		if(stack.getComponents().has(DataComponents.BLOCK_ENTITY_DATA))
		{
			CompoundTag blockEntityTag = stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag();
			ListTag tagList = blockEntityTag.getCompound("Inventory").getList("Items", Tag.TAG_COMPOUND);

			if(tagList.size() > 0)
			{
				CompoundTag list1 = tagList.getCompound(0);

				if(list1.contains("id", Tag.TAG_STRING) && list1.getString("id").equals(ItemInit.LARGE_CONTROL_CRYSTAL.get().asItem().toString()) && list1.contains("Count", Tag.TAG_BYTE) && list1.getByte("Count") > 0)
					tooltipComponents.add(Component.translatable("tooltip.sgjourney.has_control_crystal").withStyle(ChatFormatting.DARK_RED));
			}
		}

		super.appendHoverText(stack, pContext, tooltipComponents, pTooltipFlag);
	}
}
