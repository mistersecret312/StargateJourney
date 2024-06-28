package net.povstalec.sgjourney.common.items;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.povstalec.sgjourney.StargateJourney;

public class NaquadahArmorItem extends ArmorItem {
    private ItemAttributeModifiers attributeModifiers;

    public NaquadahArmorItem(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
        this.attributeModifiers = setupModifiers(pType);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return this.attributeModifiers;
    }

    public ItemAttributeModifiers setupModifiers(Type type)
    {
        attributeModifiers = super.getDefaultAttributeModifiers();
        if(type.getSlot().equals(EquipmentSlot.HEAD)){
            return attributeModifiers
                    .withModifierAdded(Attributes.OXYGEN_BONUS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "oxygen_bonus"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.HEAD)
                    .withModifierAdded(Attributes.WATER_MOVEMENT_EFFICIENCY, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "water_movement_efficiency"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.HEAD);
        }
        if(type.getSlot().equals(EquipmentSlot.CHEST)){
            return attributeModifiers
                    .withModifierAdded(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "block_range"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                    .withModifierAdded(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "entity_range"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST);
        }
        if(type.getSlot().equals(EquipmentSlot.LEGS)){
           return attributeModifiers
                    .withModifierAdded(Attributes.JUMP_STRENGTH, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "jump_strength"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
                    .withModifierAdded(Attributes.BURNING_TIME, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "burning_time"), -0.9, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.LEGS);
        }
        if(type.getSlot().equals(EquipmentSlot.FEET)){
           return attributeModifiers
                    .withModifierAdded(Attributes.MOVEMENT_EFFICIENCY, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "movement_efficiency"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .withModifierAdded(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "safe_fall_distance"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.FEET);
        }
        return attributeModifiers;
    }
}
