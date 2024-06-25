package net.povstalec.sgjourney.common.items.tools;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class SGJourneySwordItem extends SwordItem
{

	public SGJourneySwordItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties properties) 
	{
		super(tier, properties.attributes(SwordItem.createAttributes(tier, attackDamageIn, attackSpeedIn)));
	}
	
}
