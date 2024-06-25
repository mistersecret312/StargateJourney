package net.povstalec.sgjourney.common.items.tools;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class SGJourneyPickaxeItem extends PickaxeItem
{
	public SGJourneyPickaxeItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties properties) 
	{
		super(tier, properties.attributes(PickaxeItem.createAttributes(tier, attackDamageIn, attackSpeedIn)));
	}
}
