package net.povstalec.sgjourney.common.block_entities.stargate;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.povstalec.sgjourney.client.sound.SoundWrapper;
import net.povstalec.sgjourney.common.blockstates.StargatePart;
import net.povstalec.sgjourney.common.compatibility.cctweaked.CCTweakedCompatibility;
import net.povstalec.sgjourney.common.compatibility.cctweaked.StargatePeripheralWrapper;
import net.povstalec.sgjourney.common.config.CommonStargateConfig;
import net.povstalec.sgjourney.common.config.StargateJourneyConfig;
import net.povstalec.sgjourney.common.init.BlockEntityInit;
import net.povstalec.sgjourney.common.init.SoundInit;
import net.povstalec.sgjourney.common.packets.ClientBoundSoundPackets;
import net.povstalec.sgjourney.common.packets.ClientboundMilkyWayStargateUpdatePacket;
import net.povstalec.sgjourney.common.stargate.Stargate;
import net.povstalec.sgjourney.common.stargate.Stargate.ChevronLockSpeed;

public class MilkyWayStargateEntity extends AbstractStargateEntity
{
	public static final int MAX_ROTATION = 156;
	public static final int ROTATION_INCREASE = 1;
	
	public static final int SYMBOL_NUMBER = 39;
	public static final int RING_SEGMENTS = 3;
	public static final int SYMBOLS_PER_SEGMENT = SYMBOL_NUMBER / RING_SEGMENTS;

	public static final int STEPS_PER_SYMBOL = MAX_ROTATION / SYMBOL_NUMBER;
	public static final int SYMBOL_ADDITION = STEPS_PER_SYMBOL / 2;
	
	private int rotation = 0;
	public int oldRotation = 0;
	public boolean isChevronOpen = false;
	private Map<StargatePart, Integer> signalMap = Maps.newHashMap();
	
	public int previousSignalStrength = 0;
	public int signalStrength = 0;
	
	public boolean computerRotation = false;
	public int desiredSymbol = 0;
	public boolean rotateClockwise = true;
	
	public SoundWrapper buildupSound = null;

	public MilkyWayStargateEntity(BlockPos pos, BlockState state)
	{
		super(BlockEntityInit.MILKY_WAY_STARGATE.get(), pos, state, Stargate.Gen.GEN_2, 2);
	}

	@Override
    public void onLoad()
	{
        //Rotate the ring randomly
        if(!this.level.isClientSide() && !addToNetwork)
        {
        	Random random = new Random();
        	setRotation(2 * random.nextInt(0, MAX_ROTATION / 2 + 1));
        }

        super.onLoad();

        if(this.level.isClientSide())
        	return;

        if(!isPointOfOriginValid(this.getLevel()))
        	setPointOfOriginFromDimension(this.getLevel().dimension());

        if(!areSymbolsValid(this.getLevel()))
        	setSymbolsFromDimension(this.getLevel().dimension());
    }

	@Override
	public CompoundTag serializeStargateInfo(CompoundTag tag, HolderLookup.Provider pRegistries)
	{
		super.serializeStargateInfo(tag, pRegistries);
		
		tag.putString("PointOfOrigin", pointOfOrigin);
		tag.putString("Symbols", symbols);
		tag.putInt("Rotation", rotation);
		
		return tag;
	}
	
	@Override
	public void deserializeStargateInfo(CompoundTag tag, HolderLookup.Provider pRegistries, boolean isUpgraded)
	{
		if(tag.contains("PointOfOrigin"))
			this.pointOfOrigin = tag.getString("PointOfOrigin");
		
		if(tag.contains("Symbols"))
			this.symbols = tag.getString("Symbols");
		
        if(tag.contains("Rotation"))
        	rotation = tag.getInt("Rotation");
		this.oldRotation = this.rotation;
    	
    	super.deserializeStargateInfo(tag, pRegistries, isUpgraded);
	}
	
	public boolean isChevronOpen()
	{
		return this.isChevronOpen;
	}
	
	public SoundEvent getRingRotationBuildupSound()
	{
		return SoundInit.MILKY_WAY_RING_SPIN_START.get();
	}
	
	@Override
	public SoundEvent getRotationSound()
	{
		return SoundInit.MILKY_WAY_RING_SPIN.get();
	}
	
	public SoundEvent getRingRotationStopSound()
	{
		return SoundInit.MILKY_WAY_RING_SPIN_STOP.get();
	}

	@Override
	public SoundEvent getChevronEngageSound()
	{
		return SoundInit.MILKY_WAY_CHEVRON_ENGAGE.get();
	}

	@Override
	public SoundEvent getPrimaryChevronEngageSound()
	{
		return SoundInit.MILKY_WAY_PRIMARY_CHEVRON_ENGAGE.get();
	}

	@Override
	public SoundEvent getChevronIncomingSound()
	{
		return SoundInit.MILKY_WAY_CHEVRON_INCOMING.get();
	}

	@Override
	public SoundEvent getPrimaryChevronIncomingSound()
	{
		return SoundInit.MILKY_WAY_PRIMARY_CHEVRON_INCOMING.get();
	}
	
	public SoundEvent getChevronOpenSound()
	{
		return SoundInit.MILKY_WAY_CHEVRON_OPEN.get();
	}
	
	public SoundEvent getPrimaryChevronOpenSound()
	{
		return SoundInit.MILKY_WAY_PRIMARY_CHEVRON_OPEN.get();
	}
	
	public SoundEvent getChevronEncodeSound()
	{
		return SoundInit.MILKY_WAY_CHEVRON_ENCODE.get();
	}

	@Override
	public SoundEvent getWormholeOpenSound()
	{
		return SoundInit.MILKY_WAY_WORMHOLE_OPEN.get();
	}

	@Override
	public SoundEvent getWormholeIdleSound()
	{
		return SoundInit.MILKY_WAY_WORMHOLE_IDLE.get();
	}

	@Override
	public SoundEvent getWormholeCloseSound()
	{
		return SoundInit.MILKY_WAY_WORMHOLE_CLOSE.get();
	}

	@Override
	public SoundEvent getFailSound()
	{
		return SoundInit.MILKY_WAY_DIAL_FAIL.get();
	}

	private void manualDialing()
	{
		if(this.signalStrength > 0)
		{
			if(this.signalStrength == 15 && (getCurrentSymbol() != 0 || getAddress().getLength() > 0))
			{
				if(!isConnected())
					openChevron();
				else
					disconnectStargate(Stargate.Feedback.CONNECTION_ENDED_BY_POINT_OF_ORIGIN, true);
			}
		}
		else if(this.signalStrength == 0 && this.previousSignalStrength == 15)
			closeChevron();

		if(!this.level.isClientSide())
			synchronizeWithClient(this.level);
	}
	
	private boolean hadBestRedstoneSignalChanged()
	{
		this.previousSignalStrength = this.signalStrength;
		this.signalStrength = 0;
		this.signalMap.forEach((stargatePart, signal) -> 
		{
			if(signal > this.signalStrength)
				this.signalStrength = signal;
		});

		return previousSignalStrength != signalStrength;
	}
	
	public void updateSignal(StargatePart part, int signal)
	{
		if(!CommonStargateConfig.enable_redstone_dialing.get())
			return;
		
		if(this.signalMap.containsKey(part))
			this.signalMap.remove(part);
		this.signalMap.put(part, signal);

		if(hadBestRedstoneSignalChanged())
			manualDialing();
	}
	
	public int getRotation()
	{
		return this.rotation;
	}
	
	public float getRotation(float partialTick)
	{
		return StargateJourneyConfig.disable_smooth_animations.get() ?
				(float) getRotation() : Mth.lerp(partialTick, this.oldRotation, this.rotation);
	}
	
	public void setRotation(int rotation)
	{
		this.rotation = rotation;
	}
	
	public boolean isRotating()
	{
		return this.rotation != this.oldRotation;
	}
	
	public Stargate.Feedback openChevron()
	{
		if(!this.isChevronOpen)
		{
			if(!getAddress().containsSymbol(getCurrentSymbol()))
			{
				if(!level.isClientSide())
					PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(this.worldPosition).getPos(), new ClientBoundSoundPackets.Chevron(this.worldPosition, getCurrentSymbol() == 0, false, true, false));

				this.isChevronOpen = true;
				
				if(!level.isClientSide())
					synchronizeWithClient(level);
				
				return setRecentFeedback(Stargate.Feedback.CHEVRON_RAISED);
			}
			else
				return setRecentFeedback(Stargate.Feedback.SYMBOL_IN_ADDRESS);
		}
		return setRecentFeedback(Stargate.Feedback.CHEVRON_ALREADY_OPENED);
	}
	
	public Stargate.Feedback encodeChevron()
	{
		if(!this.isChevronOpen)
			return setRecentFeedback(Stargate.Feedback.CHEVRON_NOT_RAISED);
		
		if(!level.isClientSide())
			synchronizeWithClient(level);
		
		int symbol = getCurrentSymbol();
		
		if(symbol == 0)
			return setRecentFeedback(Stargate.Feedback.CANNOT_ENCODE_POINT_OF_ORIGIN);
		
		return setRecentFeedback(encodeChevron(symbol, false, true));
	}
	
	public Stargate.Feedback closeChevron()
	{
		if(this.isChevronOpen)
		{
			this.isChevronOpen = false;
			
			Stargate.Feedback feedback = engageSymbol(getCurrentSymbol());
			
			// This is a dumb way to make sure the sound plays even after the chevron is engaged 
			if(feedback == Stargate.Feedback.SYMBOL_IN_ADDRESS)
				chevronSound(getCurrentSymbol() == 0, false, false, false);
			
			return setRecentFeedback(feedback);
		}
		
		if(!level.isClientSide())
			synchronizeWithClient(level);
		
		return setRecentFeedback(Stargate.Feedback.CHEVRON_ALREADY_CLOSED);
	}
	
	public int getCurrentSymbol()
	{
		int symbolPosition = this.rotation + SYMBOL_ADDITION;
		
		int currentSymbol = (symbolPosition / STEPS_PER_SYMBOL) % SYMBOL_NUMBER;
		
		return currentSymbol;
	}
	
	@Override
	public int getRedstoneSymbolOutput()
	{
		return (getCurrentSymbol() % SYMBOLS_PER_SEGMENT) + 1;
	}

	@Override
	public int getRedstoneSegmentOutput()
	{
		return (getCurrentSymbol() / SYMBOLS_PER_SEGMENT + 1) * 5;
	}
	
	public static void tick(Level level, BlockPos pos, BlockState state, MilkyWayStargateEntity stargate)
	{
		stargate.rotate();
		if(stargate.isRotating() && !level.isClientSide())
			PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(stargate.worldPosition).getPos(), new ClientBoundSoundPackets.StargateRotation(stargate.worldPosition, false));

		AbstractStargateEntity.tick(level, pos, state, stargate);
	}
	
	private void rotate()
	{
		if(!isConnected() && !this.isChevronOpen)
		{
			if(this.computerRotation)
			{
				if(isCurrentSymbol(this.desiredSymbol))
					endRotation(false);
				else
					rotate(this.rotateClockwise);
			}
			else if(this.signalStrength > 0 && this.signalStrength < 15)
			{
				if(this.signalStrength > 7)
					rotate(false);
				else
					rotate(true);
			}
			else
				syncRotation();
		}
		else
			syncRotation();
		setChanged();
	}
	
	public void rotate(boolean clockwise)
	{
		this.oldRotation = this.rotation;
		
		if(clockwise)
			this.rotation -= ROTATION_INCREASE;
		else
			this.rotation += ROTATION_INCREASE;
		
		if(this.rotation >= MAX_ROTATION)
		{
			this.rotation -= MAX_ROTATION;
			this.oldRotation -= MAX_ROTATION;
		}
		else if(this.rotation < 0)
		{
			this.rotation += MAX_ROTATION;
			this.oldRotation += MAX_ROTATION;
		}
		setChanged();
	}
	
	public boolean isCurrentSymbol(int desiredSymbol)
	{
		if(desiredSymbol * STEPS_PER_SYMBOL == this.rotation)
			return true;
		
		return false;
	}
	
	private void synchronizeWithClient(Level level)
	{
		if(level.isClientSide())
			return;

		PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(this.worldPosition).getPos(), new ClientboundMilkyWayStargateUpdatePacket(this.worldPosition, this.rotation, this.oldRotation, this.isChevronOpen, this.signalStrength, this.computerRotation, this.rotateClockwise, this.desiredSymbol));

	}
	
	private void syncRotation()
	{
		this.oldRotation = this.rotation;
		if(!this.level.isClientSide())
			synchronizeWithClient(this.level);
	}
	
	public Stargate.Feedback startRotation(int desiredSymbol, boolean rotateClockwise)
	{
		if(this.isChevronOpen)
			return Stargate.Feedback.ROTATION_BLOCKED;
		
		this.computerRotation = true;
		this.desiredSymbol = desiredSymbol;
		this.rotateClockwise = rotateClockwise;
		if(!this.level.isClientSide())
			PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(worldPosition).getPos(), new ClientBoundSoundPackets.MilkyWayBuildup(worldPosition));

		synchronizeWithClient(this.level);
		
		return Stargate.Feedback.ROTATING;
	}
	
	public Stargate.Feedback endRotation(boolean playSound)
	{
		
		if(!this.level.isClientSide() && playSound)
			PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(worldPosition).getPos(), new ClientBoundSoundPackets.MilkyWayStop(worldPosition));

		if(!this.computerRotation)
			return Stargate.Feedback.NOT_ROTATING;
		
		this.computerRotation = false;
		
		synchronizeWithClient(this.level);
		
		return Stargate.Feedback.ROTATION_STOPPED;
	}
	
	public void playBuildupSound()
	{
		if(this.buildupSound.isPlaying())
			this.buildupSound.stopSound();
		this.buildupSound.playSound();
	}

	@Override
	public void playRotationSound()
	{
		if(!this.spinSound.isPlaying())
		{
			this.spinSound.stopSound();
			this.spinSound.playSound();
		}
	}

	@Override
	public void stopRotationSound(){}

	@Override
	public ChevronLockSpeed getChevronLockSpeed()
	{
		return CommonStargateConfig.milky_way_chevron_lock_speed.get();
	}

	@Override
	public void registerInterfaceMethods(StargatePeripheralWrapper wrapper)
	{
		CCTweakedCompatibility.registerMilkyWayStargateMethods(wrapper);
	}
}
