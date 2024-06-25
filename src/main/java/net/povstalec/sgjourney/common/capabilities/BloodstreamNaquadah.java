package net.povstalec.sgjourney.common.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.povstalec.sgjourney.StargateJourney;
import org.jetbrains.annotations.UnknownNullability;

public class BloodstreamNaquadah implements IBloodstreamNaquadah, INBTSerializable<CompoundTag>
{
	public static final EntityCapability<IBloodstreamNaquadah, Void> BLOODSTREAM_NAQUADAH = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "bloodstream_naquadah"), IBloodstreamNaquadah.class);

	private boolean hasNaquadah;
	
	public boolean hasNaquadahInBloodstream()
	{
		return this.hasNaquadah;
	}
	
	public void addNaquadahToBloodstream()
	{
		this.hasNaquadah = true;
	}
	
	public void removeNaquadahFromBloodstream()
	{
		this.hasNaquadah = false;
	}
	
	public void copyFrom(IBloodstreamNaquadah source)
	{
		this.hasNaquadah = source.hasNaquadahInBloodstream();
	}

	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("HasNaquadah", hasNaquadah);
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
		this.hasNaquadah = tag.getBoolean("HasNaquadah");
	}
}
