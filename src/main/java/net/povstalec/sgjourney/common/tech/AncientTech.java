package net.povstalec.sgjourney.common.tech;

import java.util.Optional;

import net.minecraft.world.entity.Entity;
import net.povstalec.sgjourney.common.capabilities.AncientGene;
import net.povstalec.sgjourney.common.capabilities.IAncientGene;

public interface AncientTech
{
	default boolean canUseAncientTech(Entity user)
	{
		IAncientGene gene = user.getCapability(AncientGene.ANCIENT_GENE);
		if(gene != null)
		{
            return gene.canUseAncientTechnology();
		} else return false;
	}
	
	/**
	 * 
	 * @param requirementsDisabled Whether or not the requirements for having Ancient Gene to use this have been disabled
	 * @param user
	 * @return
	 */
	default boolean canUseAncientTech(boolean requirementsDisabled, Entity user)
	{
		return requirementsDisabled ? true : canUseAncientTech(user);
	}
	
	default AncientGene.ATAGene getGeneType(Entity user)
	{
		IAncientGene gene = user.getCapability(AncientGene.ANCIENT_GENE);
		if(gene != null)
		{
			AncientGene.ATAGene geneType = gene.getGeneType();
			return geneType;
		}
		return AncientGene.ATAGene.NONE;
	}
	
}
