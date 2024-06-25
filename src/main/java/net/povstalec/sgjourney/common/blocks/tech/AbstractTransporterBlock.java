package net.povstalec.sgjourney.common.blocks.tech;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.povstalec.sgjourney.common.block_entities.tech.AbstractTransporterEntity;
import net.povstalec.sgjourney.common.block_entities.tech.TransportRingsEntity;
import net.povstalec.sgjourney.common.init.BlockInit;

public abstract class AbstractTransporterBlock extends BaseEntityBlock
{
	protected String listName;
	
	protected AbstractTransporterBlock(Properties properties)
	{
		super(properties);
	}
	
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}
	
	@Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
        if (state.getBlock() != newState.getBlock())
        {
            BlockEntity entity = level.getBlockEntity(pos);
            
            if(entity instanceof AbstractTransporterEntity transporterEntity)
            	transporterEntity.removeTransporterFromNetwork();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
	
	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		BlockEntity blockentity = level.getBlockEntity(pos);
		if(blockentity instanceof TransportRingsEntity transportRings)
		{
			if(!level.isClientSide() && !player.isCreative())
			{
				ItemStack itemstack = new ItemStack(BlockInit.TRANSPORT_RINGS.get());
				
				blockentity.saveToItem(itemstack, VanillaRegistries.createLookup());
				if (transportRings.hasCustomName())
					itemstack.set(DataComponents.CUSTOM_NAME, transportRings.getCustomName());

				ItemEntity itementity = new ItemEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemstack);
				itementity.setDefaultPickUpDelay();
				level.addFreshEntity(itementity);
			}
		}

		return super.playerWillDestroy(level, pos, state, player);
	}
	
	@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
    {
		String id;
		if(stack.getComponents().has(DataComponents.BLOCK_ENTITY_DATA) && stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().contains("ID"))
			id = stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().getString("ID");
		else
			id = "";
		
        tooltipComponents.add(Component.literal("ID: " + id).withStyle(ChatFormatting.AQUA));

        if(stack.getComponents().has(DataComponents.BLOCK_ENTITY_DATA) && stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().contains("AddToNetwork") && !stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().getBoolean("AddToNetwork"))
            tooltipComponents.add(Component.translatable("tooltip.sgjourney.not_added_to_network").withStyle(ChatFormatting.YELLOW));

        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
    }
	
	public static ItemStack excludeFromNetwork(ItemStack stack)
	{
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putBoolean("AddToNetwork", false);
		stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundtag));
		
		return stack;
	}
}
