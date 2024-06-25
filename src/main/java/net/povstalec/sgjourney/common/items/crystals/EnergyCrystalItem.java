package net.povstalec.sgjourney.common.items.crystals;

import java.util.List;
import java.util.Optional;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class EnergyCrystalItem extends AbstractCrystalItem
{
	public static final int DEFAULT_CAPACITY = 50000;
	public static final int DEFAULT_ENERGY_TRANSFER = 1500;

	public static final int ADVANCED_CAPACITY = 100000;
	public static final int ADVANCED_ENERGY_TRANSFER = 3000;
	
	public EnergyCrystalItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack)
	{
		return Math.round(13.0F * (float) 0 / getCapacity());
	}

	@Override
	public int getBarColor(ItemStack stack)
	{
		float f = Math.max(0.0F, (float) 0 / getCapacity());
		return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}
	
	public int getCapacity()
	{
		return DEFAULT_CAPACITY;
	}
	
	public int getTransfer()
	{
		return DEFAULT_ENERGY_TRANSFER;
	}

	@Override
	public Optional<Component> descriptionInDHD(ItemStack stack)
	{
		return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_dhd.energy").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
	{
		int energy = 0;
		
		tooltipComponents.add(Component.translatable("tooltip.sgjourney.energy").append(Component.literal(": " + energy +  "/" + getCapacity() + " FE")).withStyle(ChatFormatting.DARK_RED));
		
		super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
	}
	
	public static final class Advanced extends EnergyCrystalItem
	{
		public Advanced(Properties properties)
		{
			super(properties);
		}
		
		@Override
		public int getCapacity()
		{
			return ADVANCED_CAPACITY;
		}

		@Override
		public int getTransfer()
		{
			return ADVANCED_ENERGY_TRANSFER;
		}
		
		@Override
		public boolean isAdvanced()
		{
			return true;
		}

		@Override
		public Optional<Component> descriptionInDHD(ItemStack stack)
		{
			return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_dhd.energy.advanced").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
		}
	}
}
