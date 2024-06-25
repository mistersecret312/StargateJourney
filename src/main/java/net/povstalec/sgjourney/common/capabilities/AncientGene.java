package net.povstalec.sgjourney.common.capabilities;

import java.util.Random;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.povstalec.sgjourney.StargateJourney;
import org.jetbrains.annotations.UnknownNullability;

public class AncientGene implements IAncientGene, INBTSerializable<CompoundTag>
{
	public static final EntityCapability<IAncientGene, Void> ANCIENT_GENE = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "ancient_gene"), IAncientGene.class);

	public enum ATAGene
	{
		ANCIENT(true),
		INHERITED(true),
		ARTIFICIAL(true),
		NONE(false);
		
		private boolean canActivate;
		
		private ATAGene(boolean canActivate)
		{
			this.canActivate = canActivate;
		}
		
		private boolean canActivate()
		{
			return this.canActivate;
		}
	}
	
	private boolean firstJoin = true;
	private ATAGene gene = ATAGene.NONE;
	
	public ATAGene getGeneType()
	{
		return this.gene;
	}
	
	public boolean canUseAncientTechnology()
	{
		return this.gene.canActivate();
	}
	
	public boolean isAncient()
	{
		return this.gene.equals(ATAGene.ANCIENT);
	}
	
	public boolean isInherited()
	{
		return this.gene.equals(ATAGene.INHERITED);
	}
	
	public boolean isArtificial()
	{
		return this.gene.equals(ATAGene.ARTIFICIAL);
	}
	
	public boolean isLacking()
	{
		return this.gene.equals(ATAGene.NONE);
	}
	
	public void giveGene()
	{
		this.gene = ATAGene.ANCIENT;
	}
	
	public void inheritGene()
	{
		this.gene = ATAGene.INHERITED;
	}
	
	public void implantGene()
	{
		this.gene = ATAGene.ARTIFICIAL;
	}
	
	public void removeGene()
	{
		this.gene = ATAGene.NONE;
	}
	
	public static void addAncient(Entity entity)
	{
		//entity.getCapability(AncientGeneProvider.ANCIENT_GENE)ifPresent(cap -> cap.giveGene());
	}
	
	public static void inheritGene(long seed, Entity entity, int inheritanceChance)
	{
		Random random = new Random(seed);
		int chance = random.nextInt(1, 101);
		
		inheritGene(entity, inheritanceChance, chance);
	}
	
	public static void inheritGene(Entity entity, int inheritanceChance)
	{
		Random random = new Random();
		int chance = random.nextInt(1, 101);
		
		inheritGene(entity, inheritanceChance, chance);
	}
	
	private static void inheritGene(Entity entity, int inheritanceChance, int chance)
	{
		IAncientGene gene = entity.getCapability(AncientGene.ANCIENT_GENE);
		if(gene != null) {
			if (gene.firstJoin()) {
				if (chance <= inheritanceChance)
					gene.inheritGene();

				gene.markJoined();
			}
		}
	}
	
	
	public boolean firstJoin()
	{
		return this.firstJoin;
	}
	
	public void markJoined()
	{
		this.firstJoin = false;
	}
	
	public void copyFrom(IAncientGene source)
	{
		this.gene = source.getGeneType();
	}

	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("FirstJoin", firstJoin);
		tag.putString("AncientGene", this.gene.toString().toUpperCase());
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
		this.firstJoin = tag.getBoolean("FirstJoin");
		this.gene = ATAGene.valueOf(tag.getString("AncientGene"));
	}
}
