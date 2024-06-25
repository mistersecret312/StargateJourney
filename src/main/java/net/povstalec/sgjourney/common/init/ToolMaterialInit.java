package net.povstalec.sgjourney.common.init;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

public enum ToolMaterialInit implements Tier
{
	naquadah(4, 8600, 11.0F, 6.0F, 30, ItemInit.NAQUADAH.get());
	
	private float attackDamage, efficiency;
	private int durability, harvestLevel, enchantability;
	private Item repairMaterial;
	
	private ToolMaterialInit(int harvestLevel, int durability, float efficiency, float attackDamage, int enchantability, Item repairMaterial) 
	{
		this.harvestLevel = harvestLevel;
		this.durability = durability;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairMaterial = repairMaterial;
	}

	@Override
	public int getUses()
	{
		return this.durability;
	}

	@Override
	public float getSpeed()
	{
		return this.efficiency;
	}

	@Override
	public float getAttackDamageBonus()
	{
		return this.attackDamage;
	}

	@Override
	public TagKey<Block> getIncorrectBlocksForDrops() {
		return Tags.Blocks.GLASS_BLOCKS_CHEAP;
	}

	@Override
	public int getEnchantmentValue()
	{
		return this.enchantability;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return Ingredient.of(repairMaterial);
	}
}
