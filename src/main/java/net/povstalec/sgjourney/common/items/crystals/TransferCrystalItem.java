package net.povstalec.sgjourney.common.items.crystals;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class TransferCrystalItem extends AbstractCrystalItem
{
	//TODO Change it from a placeholder values
	public static final int DEFAULT_MAX_TRANSFER = 2500;
	public static final int ADVANCED_MAX_TRANSFER = 5000;
	
	public TransferCrystalItem(Properties properties)
	{
		super(properties);
	}
	
	public static int getMaxTransfer(ItemStack stack)
	{
		return DEFAULT_MAX_TRANSFER;
	}

	@Override
	public Optional<Component> descriptionInDHD(ItemStack stack)
	{
		return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_dhd.transfer").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
	{
		int maxEnergyTransfer = 2500;
		
    	tooltipComponents.add(Component.translatable("tooltip.sgjourney.energy_transfer").append(Component.literal(": " + maxEnergyTransfer + " FE")).withStyle(ChatFormatting.RED));
        
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
    }
	
	public static class Advanced extends TransferCrystalItem
	{
		public Advanced(Properties properties)
		{
			super(properties);
		}

		public static int getMaxTransfer(ItemStack stack)
		{
			return ADVANCED_MAX_TRANSFER;
		}
		
		@Override
		public boolean isAdvanced()
		{
			return true;
		}

		@Override
		public Optional<Component> descriptionInDHD(ItemStack stack)
		{
			return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_dhd.transfer.advanced").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
		}
	}
}
