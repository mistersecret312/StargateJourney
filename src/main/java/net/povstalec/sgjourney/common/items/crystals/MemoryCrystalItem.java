package net.povstalec.sgjourney.common.items.crystals;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.init.ItemInit;
import net.povstalec.sgjourney.common.stargate.Address;

public class MemoryCrystalItem extends AbstractCrystalItem
{
	public static final int DEFAULT_MEMORY_CAPACITY = 4;
	public static final int ADVANCED_MEMORY_CAPACITY = 6;

	private static final String MEMORY_TYPE = "MemoryType";
	private static final String MEMORY_LIST = "MemoryList";

	private static final String ID = "ID";
	private static final String COORDINATES = "Coordinates";
	private static final String ADDRESS = "Address";

	public MemoryCrystalItem(Properties properties)
	{
		super(properties);
	}

	public enum MemoryType
	{
		ID,
		COORDINATES,
		ADDRESS
	}

	public int getMemoryCapacity()
	{
		return DEFAULT_MEMORY_CAPACITY;
	}

	public static class Advanced extends MemoryCrystalItem
	{
		public Advanced(Properties properties)
		{
			super(properties);
		}

		@Override
		public int getMemoryCapacity()
		{
			return DEFAULT_MEMORY_CAPACITY;
		}
		
		/*@Override
		public Optional<Component> descriptionInDHD()
		{
			return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_dhd.memory.advanced").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
		}

		@Override
		public Optional<Component> descriptionInRing()
		{
			return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_ring.memory.advanced").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
		}*/

		@Override
		public boolean isAdvanced()
		{
			return true;
		}
	}
}
