package net.povstalec.sgjourney.common.items.tools;

import net.minecraft.advancements.critereon.ItemAttributeModifiersPredicate;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SGJourneyAxeItem extends AxeItem
{

	public SGJourneyAxeItem(Tier tier, float attackDamageIn, float attackSpeedIn, Properties properties) 
	{
		super(tier, properties.attributes(AxeItem.createAttributes(tier, attackDamageIn, attackSpeedIn)));
	}

}
