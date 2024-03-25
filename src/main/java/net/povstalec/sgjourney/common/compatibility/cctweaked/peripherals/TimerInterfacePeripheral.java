package net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.povstalec.sgjourney.common.block_entities.TimerInterfaceEntity;
import net.povstalec.sgjourney.common.block_entities.tech.AbstractInterfaceEntity;
import net.povstalec.sgjourney.common.compatibility.cctweaked.methods.InterfaceMethod;
import net.povstalec.sgjourney.common.compatibility.cctweaked.methods.InterfaceMethods;
import net.povstalec.sgjourney.common.compatibility.cctweaked.methods.TimerInterfaceMethod;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class TimerInterfacePeripheral implements IPeripheral, IDynamicPeripheral
{
	protected TimerInterfaceEntity interfaceEntity;
	protected HashMap<String, TimerInterfaceMethod<BlockEntity>> methods = new HashMap<String,TimerInterfaceMethod<BlockEntity>>();

	public TimerInterfacePeripheral(TimerInterfaceEntity interfaceEntity)
	{
		this.interfaceEntity = interfaceEntity;

	}

	@Override
	public String getType()
	{
		return "timer_interface";
	}

	@Override
	public boolean equals(IPeripheral other)
	{
		if(this == other)
			return true;
		
		return this.getClass() == other.getClass() && this.interfaceEntity == ((TimerInterfacePeripheral) other).interfaceEntity;
	}

    @Override
    public void attach(@Nonnull IComputerAccess computer)
    {
    	interfaceEntity.getPeripheralWrapper().computerList.add(computer);
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer)
    {
    	interfaceEntity.getPeripheralWrapper().computerList.removeIf(computerAccess -> (computerAccess.getID() == computer.getID()));
    }

	@Override
	public String[] getMethodNames()
	{
		return methods.keySet().toArray(new String[0]);
	}

	@Override
	public MethodResult callMethod(IComputerAccess computer, ILuaContext context, int method, IArguments arguments)
			throws LuaException
	{
		String methodName = getMethodNames()[method];
		
		return methods.get(methodName).use(computer, context, this.interfaceEntity, this.interfaceEntity.findBlockEntity(), arguments);
	}
	
	@SuppressWarnings("unchecked")
	public <ConnectedBlockEntity extends BlockEntity> void registerMethod(TimerInterfaceMethod<ConnectedBlockEntity> function)
	{
		methods.put(function.getName(), (TimerInterfaceMethod<BlockEntity>) function);
	}
}
