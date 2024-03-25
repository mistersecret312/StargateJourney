package net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.povstalec.sgjourney.common.block_entities.DestinyTimerEntity;
import net.povstalec.sgjourney.common.block_entities.TimerInterfaceEntity;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.block_entities.tech.AbstractInterfaceEntity;
import net.povstalec.sgjourney.common.compatibility.cctweaked.StargatePeripheralWrapper;
import net.povstalec.sgjourney.common.compatibility.cctweaked.TimerPeripheralWrapper;
import net.povstalec.sgjourney.common.stargate.Stargate;

public class TimerPeripheral extends TimerInterfacePeripheral
{
	protected DestinyTimerEntity timer;

	public TimerPeripheral(TimerInterfaceEntity interfaceEntity, DestinyTimerEntity timer)
	{
		super(interfaceEntity);
		this.timer = timer;
		
		timer.registerInterfaceMethods(new TimerPeripheralWrapper(this));
	}

	@Override
	public MethodResult callMethod(IComputerAccess computer, ILuaContext context, int method, IArguments arguments)
			throws LuaException
	{
		String methodName = getMethodNames()[method];
		
		return methods.get(methodName).use(computer, context, this.interfaceEntity, this.timer, arguments);
	}
	
	//============================================================================================
	//*************************************CC: Tweaked Events*************************************
	//============================================================================================
	
	public void queueEvent(String eventName, Object... objects)
	{
		for(IComputerAccess computer : interfaceEntity.getPeripheralWrapper().computerList)
		{
			computer.queueEvent(eventName, objects);
		}
	}
	
	//============================================================================================
	//*****************************************CC: Tweaked****************************************
	//============================================================================================
	

	@LuaFunction
	public final String getTimerState()
	{
		return timer.getState().getStateName();
	}

	@LuaFunction
	public final int getTimePassed() {
		return timer.getTimePassed();
	}

}
