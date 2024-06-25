package net.povstalec.sgjourney.common.blocks.stargate;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.povstalec.sgjourney.common.block_entities.stargate.ClassicStargateEntity;
import net.povstalec.sgjourney.common.config.ClientStargateConfig;
import net.povstalec.sgjourney.common.init.BlockEntityInit;
import net.povstalec.sgjourney.common.init.BlockInit;
import net.povstalec.sgjourney.common.stargate.PointOfOrigin;
import net.povstalec.sgjourney.common.stargate.Symbols;

public class ClassicStargateBlock extends AbstractStargateBaseBlock
{
	public ClassicStargateBlock(Properties properties)
	{
		super(properties, 8.0D, 0.0D);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) 
	{
		 ClassicStargateEntity stargate = new ClassicStargateEntity(pos, state);
		
		 return stargate;
	}
	
	@Override
	public AbstractStargateRingBlock getRing()
	{
		return BlockInit.CLASSIC_RING.get();
	}

	@Override
	public BlockState ringState()
	{
		return BlockInit.CLASSIC_RING.get().defaultBlockState();
	}

	@Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, BlockEntityInit.CLASSIC_STARGATE.get(), ClassicStargateEntity::tick);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
    {
    	Minecraft minecraft = Minecraft.getInstance();
		ClientPacketListener clientPacketListener = minecraft.getConnection();
		
		if(clientPacketListener != null)
		{
			RegistryAccess registries = clientPacketListener.registryAccess();
			Registry<PointOfOrigin> pointOfOriginRegistry = registries.registryOrThrow(PointOfOrigin.REGISTRY_KEY);
			Registry<Symbols> symbolsRegistry = registries.registryOrThrow(Symbols.REGISTRY_KEY);
	    	
	    	String pointOfOrigin = "";
			if(stack.getComponents().has(DataComponents.BLOCK_ENTITY_DATA) && stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().contains("PointOfOrigin"))
			{
				ResourceLocation location = ResourceLocation.parse(stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().getString("PointOfOrigin"));
				if(location.toString().equals("sgjourney:empty"))
					pointOfOrigin = "Empty";
				else if(pointOfOriginRegistry.containsKey(location))
					pointOfOrigin = pointOfOriginRegistry.get(location).getName();
				else
					pointOfOrigin = "Error";
			}
			String symbols = "";
			if(stack.getComponents().has(DataComponents.BLOCK_ENTITY_DATA) && stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().contains("Symbols"))
			{
				ResourceLocation location = ResourceLocation.parse(stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().getString("Symbols"));
				if(location.toString().equals("sgjourney:empty"))
					symbols = "Empty";
				else if(symbolsRegistry.containsKey(location))
					symbols = symbolsRegistry.get(location).getTranslationName(!ClientStargateConfig.unique_symbols.get());
				else
					symbols = "Error";
			}
			
	        tooltipComponents.add(Component.translatable("tooltip.sgjourney.point_of_origin").append(Component.literal(": ")).append(Component.translatable(pointOfOrigin)).withStyle(ChatFormatting.DARK_PURPLE));
	        tooltipComponents.add(Component.translatable(Symbols.symbolsOrSet()).append(Component.literal(": ")).append(Component.translatable(symbols)).withStyle(ChatFormatting.LIGHT_PURPLE));
		}
		
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
    }
}
