package net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals;

import java.util.LinkedList;
import java.util.List;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.Lazy;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.block_entities.tech.AbstractInterfaceEntity;

public class InterfacePeripheralWrapper
{
	private AbstractInterfaceEntity interfaceEntity;
	private InterfacePeripheral basicInterfacePeripheral;
	private Lazy<IPeripheral> peripheral;
    protected final List<IComputerAccess> computerList = new LinkedList<>();
	
	public InterfacePeripheralWrapper(AbstractInterfaceEntity interfaceEntity)
	{
		this.interfaceEntity = interfaceEntity;
	}
	
	public static InterfacePeripheral createPeripheral(AbstractInterfaceEntity interfaceEntity)
	{
		if(interfaceEntity.findBlockEntity() instanceof AbstractStargateEntity stargate)
			return new StargatePeripheral(interfaceEntity, stargate);

		return new InterfacePeripheral(interfaceEntity);
	}
	
	public boolean resetInterface()
	{
		InterfacePeripheral newPeripheral = createPeripheral(interfaceEntity);
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
			peripheral = Lazy.of(() -> newPeripheral);
		}
		return true;
	}
	
	public Lazy<IPeripheral> newPeripheral()
	{
		basicInterfacePeripheral = createPeripheral(interfaceEntity);
		peripheral = Lazy.of(() -> basicInterfacePeripheral);
		
		if(peripheral == null)
		{
			basicInterfacePeripheral = createPeripheral(interfaceEntity);
			peripheral = Lazy.of(() -> basicInterfacePeripheral);
		}
		return peripheral;
	}
	
	public void queueEvent(String eventName, Object... objects)
	{
		if(this.basicInterfacePeripheral instanceof StargatePeripheral stargatePeripheral)
		{
			stargatePeripheral.queueEvent(eventName, objects);
		}
	}
	
	public InterfacePeripheral getPeripheral()
	{
		return this.basicInterfacePeripheral;
	}
}
