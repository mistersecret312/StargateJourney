package net.povstalec.sgjourney.common.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.PacketDistributor;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.block_entities.stargate.MilkyWayStargateEntity;
import net.povstalec.sgjourney.common.blocks.TimerInterfaceBlock;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateRingBlock;
import net.povstalec.sgjourney.common.blocks.tech.AbstractInterfaceBlock;
import net.povstalec.sgjourney.common.capabilities.CCTweakedCapabilities;
import net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals.InterfacePeripheralWrapper;
import net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals.TimerInterfacePeripheralWrapper;
import net.povstalec.sgjourney.common.init.BlockEntityInit;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;
import net.povstalec.sgjourney.common.packets.ClientboundInterfaceUpdatePacket;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TimerInterfaceEntity extends BlockEntity {

	protected TimerInterfacePeripheralWrapper peripheralWrapper;

	public TimerInterfaceEntity(BlockPos pos, BlockState state)
	{
		super(BlockEntityInit.TIMER_INTERFACE.get(), pos, state);
		
		if(ModList.get().isLoaded("computercraft"))
			peripheralWrapper = new TimerInterfacePeripheralWrapper(this);
	}

	@Override
	public void onLoad()
	{
		Level level = this.getLevel();
		BlockPos pos = this.getBlockPos();
		BlockState state = this.getLevel().getBlockState(pos);
		if(level.getBlockState(pos).getBlock() instanceof TimerInterfaceBlock ccInterface)
			ccInterface.updateInterface(state, level, pos);
		
		super.onLoad();
	}
	
	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
	}
	
	@Override
	protected void saveAdditional(@NotNull CompoundTag tag)
	{
		super.saveAdditional(tag);
	}
	
	//============================================================================================
	//****************************************Capabilities****************************************
	//============================================================================================
	
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(ModList.get().isLoaded("computercraft") && cap == CCTweakedCapabilities.CAPABILITY_PERIPHERAL)
			return peripheralWrapper.newPeripheral().cast();
			
		return super.getCapability(cap, side);
	}
	
	public boolean updateInterface(Level level, BlockPos pos, Block block, BlockState state)
	{
		if(peripheralWrapper != null)
			return peripheralWrapper.resetInterface();
		
		if(level.getBlockState(pos).getBlock() instanceof TimerInterfaceBlock ccInterface)
			ccInterface.updateInterface(state, level, pos);
		
		return true;
	}
	
	public Direction getDirection()
	{
		BlockPos gatePos = this.getBlockPos();
		BlockState gateState = this.level.getBlockState(gatePos);
		
		if(gateState.getBlock() instanceof TimerInterfaceBlock)
			return gateState.getValue(TimerInterfaceBlock.FACING);

		StargateJourney.LOGGER.error("Couldn't find Direction");
		return null;
	}

	@Nullable
	public DestinyTimerEntity findBlockEntity()
	{
		Direction direction = getDirection();
		if(direction == null)
			return null;

		BlockPos realPos = getBlockPos().relative(direction, 2);

		return level.getBlockEntity(realPos) instanceof DestinyTimerEntity blockEntity ? blockEntity : null;
	}
	
	//============================================================================================
	//*****************************************CC: Tweaked****************************************
	//============================================================================================
	
	public TimerInterfacePeripheralWrapper getPeripheralWrapper()
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
	
	public static void tick(Level level, BlockPos pos, BlockState state, TimerInterfaceEntity interfaceEntity)
	{

	}
}
