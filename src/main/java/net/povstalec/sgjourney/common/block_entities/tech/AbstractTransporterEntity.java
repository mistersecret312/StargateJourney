package net.povstalec.sgjourney.common.block_entities.tech;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.config.StargateJourneyConfig;
import net.povstalec.sgjourney.common.data.TransporterNetwork;

public abstract class AbstractTransporterEntity extends BlockEntity implements Nameable
{
	protected static final boolean requireEnergy = !StargateJourneyConfig.disable_energy_use.get();
	
	public static final String ADD_TO_NETWORK = "AddToNetwork";
	public static final String ID = "ID";
	public static final String CUSTOM_NAME = "CustomName";
	
	protected boolean addToNetwork = true;
	
	protected UUID id;
	protected String connectionID = StargateJourney.EMPTY;

	@Nullable
	private Component name;
	
	public AbstractTransporterEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
	
	@Override
    public void onLoad()
	{
		super.onLoad();
		
        if(level.isClientSide())
	        return;
        
        if(!addToNetwork)
    		addTransporterToNetwork();
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(tag, pRegistries);
		
    	addToNetwork = tag.getBoolean(ADD_TO_NETWORK);
    	
    	try
		{
    		if(tag.contains(ID))
        		id = UUID.fromString(tag.getString(ID));
		}
		catch(IllegalArgumentException e)
		{
			this.setID(this.generateID());
		}
    	
    	if(tag.contains("CustomName", 8))
	         this.name = Component.Serializer.fromJson(tag.getString("CustomName"), pRegistries);
	}
	
	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider pRegistries)
	{
		tag.putBoolean(ADD_TO_NETWORK, addToNetwork);
		
		if(id != null)
			tag.putString(ID, id.toString());
		
		super.saveAdditional(tag, pRegistries);
		
		if(this.name != null)
	         tag.putString("CustomName", Component.Serializer.toJson(this.name, pRegistries));
	}
	
	public UUID generateID()
	{
		return UUID.randomUUID();
	}
	
	public void setID(UUID id)
	{
    	this.id = id;
		setChanged();
		StargateJourney.LOGGER.info("Set ID to " + id);
	}
	
	public UUID getID()
	{
		return id;
	}
	
	public void addTransporterToNetwork()
	{
		if(this.id == null)
		{
			setID(generateID());
		}
		
		TransporterNetwork.get(level).addTransporter(this);
		
		addToNetwork = true;
		this.setChanged();
	}
	
	public void removeTransporterFromNetwork()
	{
		TransporterNetwork.get(level).removeTransporter(level, this.id);
	}

	
	public void setCustomName(Component name)
	{
		this.name = name;
	}
	
	public Component getName()
	{
		return this.name != null ? this.name : this.getDefaultName();
	}
	
	public Component getDisplayName()
	{
		return this.getName();
	}
	
	@Nullable
	public Component getCustomName()
	{
		return this.name;
	}
	
	protected abstract Component getDefaultName();
}
