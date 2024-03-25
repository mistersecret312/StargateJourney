package net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraftforge.common.util.LazyOptional;
import net.povstalec.sgjourney.common.block_entities.DestinyTimerEntity;
import net.povstalec.sgjourney.common.block_entities.TimerInterfaceEntity;

import java.util.LinkedList;
import java.util.List;

public class TimerInterfacePeripheralWrapper
{
	private TimerInterfaceEntity interfaceEntity;
	private TimerInterfacePeripheral basicInterfacePeripheral;
	private LazyOptional<IPeripheral> peripheral;
    protected final List<IComputerAccess> computerList = new LinkedList<>();

	public TimerInterfacePeripheralWrapper(TimerInterfaceEntity interfaceEntity)
	{
		this.interfaceEntity = interfaceEntity;
	}
	
	public static TimerPeripheral createPeripheral(TimerInterfaceEntity interfaceEntity, DestinyTimerEntity destinyTimer)
	{
		return new TimerPeripheral(interfaceEntity, destinyTimer);
	}
	
	public boolean resetInterface()
	{
		TimerInterfacePeripheral newPeripheral = createPeripheral(interfaceEntity, interfaceEntity.findBlockEntity());
		if(basicInterfacePeripheral != null && basicInterfacePeripheral.equals(newPeripheral))
		{
			// Peripheral is same as before, no changes needed.
			return false;
		}

		// Peripheral has changed, invalidate the capability and trigger a block update.
		basicInterfacePeripheral = newPeripheral;
		if(peripheral != null)
		{
			peripheral.invalidate();
			peripheral = LazyOptional.of(() -> newPeripheral);
		}
		return true;
	}
	
	public LazyOptional<IPeripheral> newPeripheral()
	{
		basicInterfacePeripheral = createPeripheral(interfaceEntity, interfaceEntity.findBlockEntity());
		peripheral = LazyOptional.of(() -> basicInterfacePeripheral);
		
		if(peripheral == null)
		{
			basicInterfacePeripheral = createPeripheral(interfaceEntity, interfaceEntity.findBlockEntity());
			peripheral = LazyOptional.of(() -> basicInterfacePeripheral);
		}
		return peripheral;
	}
	
	public void queueEvent(String eventName, Object... objects)
	{
		if(this.basicInterfacePeripheral instanceof TimerPeripheral timerPeripheral)
		{
			timerPeripheral.queueEvent(eventName, objects);
		}
	}
	
	public TimerInterfacePeripheral getPeripheral()
	{
		return this.basicInterfacePeripheral;
	}
}
