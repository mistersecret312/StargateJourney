package net.povstalec.sgjourney.common.block_entities.stargate;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.povstalec.sgjourney.common.compatibility.cctweaked.CCTweakedCompatibility;
import net.povstalec.sgjourney.common.compatibility.cctweaked.StargatePeripheralWrapper;
import net.povstalec.sgjourney.common.config.CommonStargateConfig;
import net.povstalec.sgjourney.common.init.BlockEntityInit;
import net.povstalec.sgjourney.common.init.SoundInit;
import net.povstalec.sgjourney.common.packets.ClientBoundSoundPackets;
import net.povstalec.sgjourney.common.packets.ClientboundPegasusStargateUpdatePacket;
import net.povstalec.sgjourney.common.stargate.Address;
import net.povstalec.sgjourney.common.stargate.Stargate;
import net.povstalec.sgjourney.common.stargate.Stargate.ChevronLockSpeed;

public class PegasusStargateEntity extends AbstractStargateEntity
{
	public static final String ADDRESS_BUFFER = "AddressBuffer";
	public static final String SYMBOL_BUFFER = "SymbolBuffer";
	public static final String CURRENT_SYMBOL = "CurrentSymbol";
	
	public static final String DYNAMC_SYMBOLS = "DynamicSymbols";
	
	public int currentSymbol = 0;
	public Address addressBuffer = new Address(true);
	public int symbolBuffer = 0;
	private boolean passedOver = false;
	
	protected boolean dynamicSymbols = true;
	
	public PegasusStargateEntity(BlockPos pos, BlockState state) 
	{
		super(BlockEntityInit.PEGASUS_STARGATE.get(), pos, state, Stargate.Gen.GEN_3, 3);
		this.setOpenSoundLead(13);
		this.symbolBounds = 47;
	}
	
	@Override
    public void onLoad()
	{
        super.onLoad();

        if(this.level.isClientSide())
        	return;

        if(!isPointOfOriginValid(this.getLevel()))
        	setPointOfOriginFromDimension(this.getLevel().dimension());

        if(!areSymbolsValid(this.getLevel()))
        	setSymbolsFromDimension(this.getLevel().dimension());
    }
	
	@Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries)
	{
        super.loadAdditional(tag, pRegistries);
        
        addressBuffer.fromArray(tag.getIntArray(ADDRESS_BUFFER));
        symbolBuffer = tag.getInt(SYMBOL_BUFFER);
        currentSymbol = tag.getInt(CURRENT_SYMBOL);
        
        dynamicSymbols = tag.getBoolean(DYNAMC_SYMBOLS);

        if(!dynamicSymbols)
        {
    		pointOfOrigin = tag.getString(POINT_OF_ORIGIN);
    		symbols = tag.getString(SYMBOLS);
        }
    }
	
	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries)
	{
		super.saveAdditional(tag, pRegistries);
		
		tag.putIntArray(ADDRESS_BUFFER, addressBuffer.toArray());
		tag.putInt(SYMBOL_BUFFER, symbolBuffer);
		tag.putInt(CURRENT_SYMBOL, currentSymbol);
		
		tag.putBoolean(DYNAMC_SYMBOLS, dynamicSymbols);

        if(!dynamicSymbols)
        {
    		tag.putString(POINT_OF_ORIGIN, pointOfOrigin);
    		tag.putString(SYMBOLS, symbols);
        }
	}
	
	@Override
	public void updateDHD()
	{
		if(hasDHD())
			this.dhd.get().updateDHD(!this.isConnected() || (this.isConnected() && this.isDialingOut()) ? 
					addressBuffer : new Address(), addressBuffer.hasPointOfOrigin() || this.isConnected());
	}
	
	public void dynamicSymbols(boolean dynamicSymbols)
	{
		this.dynamicSymbols = dynamicSymbols;
		this.setChanged();
	}
	
	public boolean useDynamicSymbols()
	{
		return this.dynamicSymbols;
	}
	
	@Override
	public SoundEvent getRotationSound()
	{
		return SoundInit.PEGASUS_RING_SPIN.get();
	}

	@Override
	public SoundEvent getChevronEngageSound()
	{
		return SoundInit.PEGASUS_CHEVRON_ENGAGE.get();
	}

	@Override
	public SoundEvent getPrimaryChevronEngageSound()
	{
		return SoundInit.PEGASUS_PRIMARY_CHEVRON_ENGAGE.get();
	}

	@Override
	public SoundEvent getChevronIncomingSound()
	{
		return SoundInit.PEGASUS_CHEVRON_INCOMING.get();
	}

	@Override
	public SoundEvent getPrimaryChevronIncomingSound()
	{
		return SoundInit.PEGASUS_PRIMARY_CHEVRON_INCOMING.get();
	}

	@Override
	public SoundEvent getWormholeOpenSound()
	{
		return SoundInit.PEGASUS_WORMHOLE_OPEN.get();
	}

	@Override
	public SoundEvent getWormholeIdleSound()
	{
		return SoundInit.PEGASUS_WORMHOLE_IDLE.get();
	}

	@Override
	public SoundEvent getWormholeCloseSound()
	{
		return SoundInit.PEGASUS_WORMHOLE_CLOSE.get();
	}
	
	public SoundEvent getFailSound()
	{
		return SoundInit.PEGASUS_DIAL_FAIL.get();
	}
	
	@Override
	public Stargate.Feedback engageSymbol(int symbol)
	{
		if(level.isClientSide())
			return Stargate.Feedback.NONE;
		
		if(isSymbolOutOfBounds(symbol))
			return Stargate.Feedback.SYMBOL_OUT_OF_BOUNDS;
		
		if(isConnected())
		{
			if(symbol == 0)
				return disconnectStargate(Stargate.Feedback.CONNECTION_ENDED_BY_DISCONNECT, true);
			else
				return setRecentFeedback(Stargate.Feedback.ENCODE_WHEN_CONNECTED);
		}
		
		if(addressBuffer.containsSymbol(symbol))
			return setRecentFeedback(Stargate.Feedback.SYMBOL_IN_ADDRESS);
		
		if(addressBuffer.getLength() == getAddress().getLength())
		{
			if(!this.level.isClientSide())
				PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(this.worldPosition).getPos(), new ClientBoundSoundPackets.StargateRotation(worldPosition, false));
		}
		addressBuffer.addSymbol(symbol);
		return setRecentFeedback(Stargate.Feedback.SYMBOL_ENCODED);
	}
	
	@Override
	protected Stargate.Feedback lockPrimaryChevron()
	{
		if(!this.level.isClientSide())
			PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(this.worldPosition).getPos(), new ClientBoundSoundPackets.StargateRotation(worldPosition, true));
		return super.lockPrimaryChevron();
	}
	
	@Override
	public Stargate.Feedback encodeChevron(int symbol, boolean incoming, boolean encode)
	{
		symbolBuffer++;
		passedOver = false;
		
		if(!this.level.isClientSide())
			PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(this.worldPosition).getPos(), new ClientBoundSoundPackets.StargateRotation(worldPosition, true));
		Stargate.Feedback feedback = super.encodeChevron(symbol, incoming, encode);
		
		if(addressBuffer.getLength() > getAddress().getLength())
		{
			if(!this.level.isClientSide())
				PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(this.worldPosition).getPos(), new ClientBoundSoundPackets.StargateRotation(worldPosition, false));
		}
		
		return setRecentFeedback(feedback);
	}
	
	public int getChevronPosition(int chevron)
	{
		if(chevron < 0 || chevron > 8)
			return 0;
		
		return 4 * getEngagedChevrons()[chevron - 1];
	}
	
	public int getCurrentSymbol()
	{
		return this.currentSymbol;
	}
	
	private void animateSpin()
	{
		if(!isConnected() && addressBuffer.getLength() > symbolBuffer)
		{
			int symbol = addressBuffer.getSymbol(symbolBuffer);
			if(symbol == 0)
			{
				if(currentSymbol == getChevronPosition(9))
					lockPrimaryChevron();
				else
					symbolWork();
			}
			else if(currentSymbol == getChevronPosition(symbolBuffer + 1))
			{
				if(symbolBuffer % 2 != 0 && !passedOver)
				{
					passedOver = true;
					symbolWork();
				}
				else
					encodeChevron(symbol, false, false);
			}
			else
				symbolWork();
		}
	}
	
	public static void tick(Level level, BlockPos pos, BlockState state, PegasusStargateEntity stargate)
	{
		if(level.isClientSide())
			return;
		
		stargate.animateSpin();
		
		AbstractStargateEntity.tick(level, pos, state, (AbstractStargateEntity) stargate);
		stargate.updateClient();
	}
	
	@Override
	public void updateClient()
	{
		super.updateClient();
		
		if(this.level.isClientSide())
			return;
		PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(this.worldPosition).getPos(), new ClientboundPegasusStargateUpdatePacket(this.worldPosition, this.symbolBuffer, this.addressBuffer.toArray(), this.currentSymbol));
	}
	
	private void symbolWork()
	{
		if(symbolBuffer % 2 == 0)
			currentSymbol--;
		else
			currentSymbol++;

		if(currentSymbol > 35)
			currentSymbol = 0;
		else if(currentSymbol < 0)
			currentSymbol = 35;
	}

	@Override
	protected void resetAddress(boolean updateInterfaces)
	{
		currentSymbol = 0;
		symbolBuffer = 0;
		addressBuffer.reset();
		super.resetAddress(updateInterfaces);
	}

	@Override
	public void playRotationSound()
	{
		this.stopRotationSound();
		this.spinSound.playSound();
	}

	@Override
	public void stopRotationSound()
	{
		this.spinSound.stopSound();
	}

	@Override
	public ChevronLockSpeed getChevronLockSpeed()
	{
		return CommonStargateConfig.pegasus_chevron_lock_speed.get();
	}

	@Override
	public void registerInterfaceMethods(StargatePeripheralWrapper wrapper)
	{
		CCTweakedCompatibility.registerPegasusStargateMethods(wrapper);
	}
	
	@Override
	public void doWhileDialed(int openTime, Stargate.ChevronLockSpeed chevronLockSpeed)
	{
		if(this.level.isClientSide())
			return;
		
		if(this.currentSymbol >= 36)
			return;
		
		this.currentSymbol = openTime / chevronLockSpeed.getMultiplier();
		this.updateClient();
	}
}