package net.povstalec.sgjourney.common.init;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.povstalec.sgjourney.StargateJourney;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterialInit {
    public static final Holder<ArmorMaterial> NAQUADAH;

    private ArmorMaterialInit()
    {
    }

    static
    {
        NAQUADAH = register("naquadah", Util.make(new EnumMap<>(ArmorItem.Type.class), (attribute) -> {
            attribute.put(ArmorItem.Type.BOOTS, 20);
            attribute.put(ArmorItem.Type.LEGGINGS, 30);
            attribute.put(ArmorItem.Type.CHESTPLATE, 40);
            attribute.put(ArmorItem.Type.HELMET, 15);
            attribute.put(ArmorItem.Type.BODY, 25);
        }), 25, 9F, 0.25F, ItemInit.NAQUADAH.get());
    }

    /**
     * @param name                  Name of the armor material
     * @param typeProtections       The amount of protection per slot
     * @param enchantability        The higher the number, the more likely better enchantments will be applied when using the enchanting table
     * @param toughness             Toughness for netherite armor
     * @param knockbackResistance   The knockback resistance for armor
     * @param ingredientItem        Item used in anvil to repair the armor piece
     * @return Registered armor material
     */
    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> typeProtections, int enchantability, float toughness, float knockbackResistance, Item ingredientItem)
    {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, name);
        Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_NETHERITE;
        Supplier<Ingredient> ingredient = () -> Ingredient.of(ingredientItem);
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(loc));

        EnumMap<ArmorItem.Type, Integer> typeMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values())
        {
            typeMap.put(type, typeProtections.get(type));
        }

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, loc, new ArmorMaterial(typeProtections, enchantability, equipSound, ingredient, layers, toughness, knockbackResistance));
    }
}
