package net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals;

import java.util.HashMap;

import javax.annotation.Nonnull;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.block_entities.tech.AbstractInterfaceEntity;
import net.povstalec.sgjourney.common.compatibility.cctweaked.StargatePeripheralWrapper;
import net.povstalec.sgjourney.common.compatibility.cctweaked.methods.InterfaceMethod;
import net.povstalec.sgjourney.common.stargate.Stargate;

public class StargatePeripheral extends InterfacePeripheral implements IDynamicPeripheral
{
	protected AbstractStargateEntity stargate;
	private HashMap<String, InterfaceMethod<AbstractStargateEntity>> methods = new HashMap<String,InterfaceMethod<AbstractStargateEntity>>();
	
	public StargatePeripheral(AbstractInterfaceEntity interfaceEntity, AbstractStargateEntity stargate)
	{
		super(interfaceEntity);
		this.stargate = stargate;
		
		stargate.registerInterfaceMethods(new StargatePeripheralWrapper(this, interfaceEntity.getInterfaceType()));
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
		
		return methods.get(methodName).use(computer, context, this.stargate, arguments);
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
	public final int getStargateGeneration()
	{
		return stargate.getGeneration().getGen();
	}
	
	@LuaFunction
	public final String getStargateType()
	{
		return BlockEntityType.getKey(stargate.getType()).toString();
	}
	
	@LuaFunction
	public final int getRecentFeedback()
	{
		return stargate.getRecentFeedback().getCode();
	}
	
	@LuaFunction
	public final boolean isStargateConnected()
	{
		return stargate.isConnected();
	}
	
	@LuaFunction
	public final long getStargateEnergy()
	{
		return stargate.getEnergyStored();
	}
	
	@LuaFunction
	public final int getChevronsEngaged()
	{
		return stargate.getChevronsEngaged();
	}
	
	@LuaFunction
	public final int getOpenTime()
	{
		return stargate.getOpenTime();
	}
	
	@LuaFunction
	public final MethodResult disconnectStargate(ILuaContext context) throws LuaException
	{
		MethodResult result = context.executeMainThreadTask(() ->
		{
			boolean wasConnected = stargate.isConnected();
			
			stargate.disconnectStargate(Stargate.Feedback.CONNECTION_ENDED_BY_DISCONNECT);

			boolean isConnected = stargate.isConnected();
					
			return new Object[] {!isConnected && (wasConnected != isConnected)};
		});
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <StargateEntity extends AbstractStargateEntity> void registerStargateMethod(InterfaceMethod<StargateEntity> function)
	{
		methods.put(function.getName(), (InterfaceMethod<AbstractStargateEntity>) function);
	}
}