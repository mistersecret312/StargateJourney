package net.povstalec.sgjourney.common.items.tools;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class SGJourneyShovelItem extends ShovelItem
{

	public SGJourneyShovelItem(Tier tier, float attackDamageIn, float attackSpeedIn, Properties properties) 
	{
		super(tier, properties.attributes(ShovelItem.createAttributes(tier, attackDamageIn, attackSpeedIn)));
	}

}
