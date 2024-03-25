package net.povstalec.sgjourney.common.compatibility.cctweaked;

import net.povstalec.sgjourney.common.block_entities.tech.AbstractInterfaceEntity;
import net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals.StargatePeripheral;
import net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals.TimerPeripheral;

public class TimerPeripheralWrapper
{
	protected TimerPeripheral peripheral;

	public TimerPeripheralWrapper(TimerPeripheral peripheral)
	{
		this.peripheral = peripheral;
	}
	
	public TimerPeripheral getPeripheral()
	{
		return this.peripheral;
	}
}
