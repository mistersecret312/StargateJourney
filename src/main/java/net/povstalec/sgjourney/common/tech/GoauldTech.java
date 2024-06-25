package net.povstalec.sgjourney.common.tech;

import net.minecraft.world.entity.Entity;
import net.povstalec.sgjourney.common.capabilities.BloodstreamNaquadah;
import net.povstalec.sgjourney.common.capabilities.IBloodstreamNaquadah;

public interface GoauldTech
{
	default boolean canUseGoauldTech(Entity user)
	{
		IBloodstreamNaquadah naquadah = user.getCapability(BloodstreamNaquadah.BLOODSTREAM_NAQUADAH);
		if(naquadah != null)
		{
			return naquadah.hasNaquadahInBloodstream();
		} else return false;
	}
	
	/**
	 * 
	 * @param requirementsDisabled Whether or not the requirements for having Naquadah in the bloodstream to use this have been disabled
	 * @param user
	 * @return
	 */
	default boolean canUseGoauldTech(boolean requirementsDisabled, Entity user)
	{
		return requirementsDisabled ? true : canUseGoauldTech(user);
	}
}
