package net.povstalec.sgjourney.common.block_entities.tech;

import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.fixes.ChunkPalettedStorageFix;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.block_entities.stargate.MilkyWayStargateEntity;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateRingBlock;
import net.povstalec.sgjourney.common.blocks.tech.AbstractInterfaceBlock;
import net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals.InterfacePeripheralWrapper;
import net.povstalec.sgjourney.common.packets.ClientboundInterfaceUpdatePacket;

public abstract class AbstractInterfaceEntity extends BlockEntity
{
	public static final String ENERGY_TARGET = "EnergyTarget";
	private static final long DEFAULT_ENERGY_TARGET = 200000;
	
	private int desiredSymbol = 0;
	private int currentSymbol = 0;
	private boolean rotate = false;
	private boolean rotateClockwise = true;
	
	private long energyTarget = DEFAULT_ENERGY_TARGET;

	public BlockEntity blockEntity = null;
	protected InterfacePeripheralWrapper peripheralWrapper;
	
	public enum InterfaceType
	{
		BASIC("basic_interface"),
		CRYSTAL("crystal_interface"),
		ADVANCED_CRYSTAL("advanced_crystal_interface");
		
		private String typeName;
		
		InterfaceType(String typeName)
		{
			this.typeName = typeName;
		}
		
		public String getName()
		{
			return this.typeName;
		}
		
		public boolean hasCrystalMethods()
		{
			return this == CRYSTAL || this == ADVANCED_CRYSTAL;
		}
		
		public boolean hasAdvancedCrystalMethods()
		{
			return this == ADVANCED_CRYSTAL;
		}
	}
	
	protected InterfaceType interfaceType;
	
	public AbstractInterfaceEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, InterfaceType interfaceType)
	{
		super(type, pos, state);
		
		this.interfaceType = interfaceType;
		
		if(ModList.get().isLoaded("computercraft"))
			peripheralWrapper = new InterfacePeripheralWrapper(this);
	}
	
	@Override
	public void onLoad()
	{
		Level level = this.getLevel();
		BlockPos pos = this.getBlockPos();
		BlockState state = this.getLevel().getBlockState(pos);
		if(level.getBlockState(pos).getBlock() instanceof AbstractInterfaceBlock ccInterface)
			ccInterface.updateInterface(state, level, pos);
		
		super.onLoad();
	}
	
	//============================================================================================
	//****************************************Capabilities****************************************
	//============================================================================================
	
	public boolean updateInterface(Level level, BlockPos pos, Block block, BlockState state)
	{
		if(peripheralWrapper != null)
			return peripheralWrapper.resetInterface();
		
		if(level.getBlockState(pos).getBlock() instanceof AbstractInterfaceBlock ccInterface)
			ccInterface.updateInterface(state, level, pos);
		
		return true;
	}
	
	public Direction getDirection()
	{
		BlockPos gatePos = this.getBlockPos();
		BlockState gateState = this.level.getBlockState(gatePos);
		
		if(gateState.getBlock() instanceof AbstractInterfaceBlock)
			return gateState.getValue(AbstractInterfaceBlock.FACING);

		StargateJourney.LOGGER.error("Couldn't find Direction " + this.getBlockPos().toString());
		return null;
	}

	@Nullable
	public BlockEntity findBlockEntity()
	{
		Direction direction = getDirection();
		if(direction == null)
			return null;

		BlockPos realPos = getBlockPos().relative(direction);
		BlockState state = level.getBlockState(realPos);

		if(level.getBlockState(realPos).getBlock() instanceof AbstractStargateRingBlock)
			realPos = state.getValue(AbstractStargateRingBlock.PART)
					.getBaseBlockPos(realPos, state.getValue(AbstractStargateRingBlock.FACING), state.getValue(AbstractStargateRingBlock.ORIENTATION));

		return level.getBlockEntity(realPos) instanceof BlockEntity energyBlockEntity ? energyBlockEntity : null;
	}

	public InterfaceType getInterfaceType()
	{
		return this.interfaceType;
	}
	
	//============================================================================================
	//*******************************************Energy*******************************************
	//============================================================================================
	
	//============================================================================================
	//*****************************************CC: Tweaked****************************************
	//============================================================================================
	
	public InterfacePeripheralWrapper getPeripheralWrapper()
	{
		if(!ModList.get().isLoaded("computercraft"))
			return null;
		
		return this.peripheralWrapper;
	}
	
	public void queueEvent(String eventName, Object... objects)
	{
		if(!ModList.get().isLoaded("computercraft"))
			return;
		if(this.peripheralWrapper != null)
			this.peripheralWrapper.queueEvent(eventName, objects);
	}
	
	//============================================================================================
	//******************************************Ticking*******************************************
	//============================================================================================
	
	public static void tick(Level level, BlockPos pos, BlockState state, AbstractInterfaceEntity interfaceEntity)
	{
		interfaceEntity.blockEntity = interfaceEntity.findBlockEntity();
		
		if(interfaceEntity.blockEntity != null)
		{
			int lastSymbol = interfaceEntity.currentSymbol;

			if(interfaceEntity.blockEntity instanceof MilkyWayStargateEntity stargate)
				interfaceEntity.rotateStargate(stargate);

			if(lastSymbol != interfaceEntity.currentSymbol)
			{
				if(!level.isClientSide())
				{
					setChanged(level, pos, state);
					level.updateNeighborsAtExceptFromFacing(pos, state.getBlock(), state.getValue(AbstractInterfaceBlock.FACING));
				}
			}
		}
		
		if(level.isClientSide())
			return;
		PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(interfaceEntity.worldPosition).getPos(), new ClientboundInterfaceUpdatePacket(interfaceEntity.worldPosition, 0L));
	}
	
	private void rotateStargate(MilkyWayStargateEntity stargate)
	{
		if(this.rotate)
		{
			if(stargate.isCurrentSymbol(this.desiredSymbol))
				this.rotate = false;
			else
				stargate.rotate(rotateClockwise);
		}

		this.currentSymbol = stargate.getCurrentSymbol();
	}
}
