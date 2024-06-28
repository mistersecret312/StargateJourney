package net.povstalec.sgjourney.common.items.crystals;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateBaseBlock;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateBlock;
import net.povstalec.sgjourney.common.init.ItemInit;
import net.povstalec.sgjourney.common.stargate.StargateVariant;

import java.util.List;
import java.util.Optional;

public class StargateChangeCrystal extends Item {
    public StargateChangeCrystal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> tooltipComponents, TooltipFlag pTooltipFlag) {
        if(pStack.getComponents().has(StargateJourney.DataComponents.VARIANT.get()))
        {
            String variant = pStack.get(StargateJourney.DataComponents.VARIANT.get());
            tooltipComponents.add(Component.translatable("tooltip.sgjourney.variant").append(Component.literal(": " + variant)).withStyle(ChatFormatting.GREEN));
        }
        if(pStack.getComponents().has(StargateJourney.DataComponents.UPGRADE.get()))
        {
            String variant = pStack.get(StargateJourney.DataComponents.UPGRADE.get()).toString();

            tooltipComponents.add(Component.translatable("tooltip.sgjourney.stargate_type").append(Component.literal(": " + variant)).withStyle(ChatFormatting.GREEN));

            tooltipComponents.add(Component.translatable("tooltip.sgjourney.stargate_upgrade.description").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }

        super.appendHoverText(pStack, pContext, tooltipComponents, pTooltipFlag);
    }

    public static Optional<String> getVariantString(ItemStack stack)
    {
        Optional<String> variant = Optional.empty();

        if(stack.getItem() instanceof StargateChangeCrystal)
        {
            if(stack.getComponents().has(StargateJourney.DataComponents.VARIANT.get()))
            {
                String variantString = stack.get(StargateJourney.DataComponents.VARIANT.get());
                variant = Optional.of(variantString);
            }
        }

        return variant;
    }

    public Optional<AbstractStargateBaseBlock> getStargateBaseBlock(ItemStack stack)
    {
        Optional<AbstractStargateBaseBlock> stargate = Optional.empty();

        Optional<ResourceLocation> rl = getStargateString(stack);

        if(rl.isPresent())
        {
            Block block = BuiltInRegistries.BLOCK.get(rl.get());
            if(block instanceof AbstractStargateBaseBlock stargateBlock)
                stargate = Optional.of(stargateBlock);
        }

        return stargate;
    }

    public static Optional<ResourceLocation> getStargateString(ItemStack stack)
    {
        Optional<ResourceLocation> stargate = Optional.empty();

        if(stack.getItem() instanceof StargateChangeCrystal)
        {
            if(stack.getComponents().has(StargateJourney.DataComponents.UPGRADE.get()))
            {
                ResourceLocation stargateString = stack.get(StargateJourney.DataComponents.UPGRADE.get());
                stargate = Optional.of(stargateString);
            }
        }

        return stargate;
    }

    public static <StargateBlock extends AbstractStargateBlock> ItemStack stargateType(StargateBlock stargate)
    {
        ItemStack stack = new ItemStack(ItemInit.CHANGE_CRYSTAL.get());
        stack.set(StargateJourney.DataComponents.UPGRADE.get(), BuiltInRegistries.BLOCK.getKey(stargate));

        return stack;
    }

    public static ItemStack stargateVariant(String variant)
    {
        ItemStack stack = new ItemStack(ItemInit.CHANGE_CRYSTAL.get());
        stack.set(StargateJourney.DataComponents.VARIANT.get(), variant);

        return stack;
    }
}
