package net.povstalec.sgjourney.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.items.CallForwardingDevice;
import net.povstalec.sgjourney.common.items.NaquadahArmorItem;
import net.povstalec.sgjourney.common.items.PDAItem;
import net.povstalec.sgjourney.common.items.StaffWeaponItem;
import net.povstalec.sgjourney.common.items.crystals.*;
import net.povstalec.sgjourney.common.items.tools.SGJourneyAxeItem;
import net.povstalec.sgjourney.common.items.tools.SGJourneyHoeItem;
import net.povstalec.sgjourney.common.items.tools.SGJourneyPickaxeItem;
import net.povstalec.sgjourney.common.items.tools.SGJourneyShovelItem;
import net.povstalec.sgjourney.common.items.tools.SGJourneySwordItem;

import java.util.function.Supplier;

public class ItemInit
{
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StargateJourney.MODID);

	// Materials
	public static final Supplier<Item> RAW_NAQUADAH = ITEMS.registerItem("raw_naquadah",
			Item::new, new Item.Properties());
	public static final Supplier<Item> NAQUADAH_ALLOY = ITEMS.register("naquadah_alloy", 
			() -> new Item(new Item.Properties().fireResistant()));
	public static final Supplier<Item> PURE_NAQUADAH = ITEMS.register("pure_naquadah", 
			() -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant()));
	public static final Supplier<Item> NAQUADAH = ITEMS.register("naquadah", 
			() -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant()));
	
	// Crafting Items
	public static final Supplier<Item> NAQUADAH_ROD = ITEMS.register("naquadah_rod", 
			() -> new Item(new Item.Properties().fireResistant()));
	public static final Supplier<Item> REACTION_CHAMBER = ITEMS.register("reaction_chamber", 
			() -> new Item(new Item.Properties().fireResistant()));
	public static final Supplier<Item> PLASMA_CONVERTER = ITEMS.register("plasma_converter", 
			() -> new Item(new Item.Properties().fireResistant()));

	public static final Supplier<Item> CRYSTAL_BASE = ITEMS.register("crystal_base", 
			() -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(16)));
	public static final Supplier<Item> ADVANCED_CRYSTAL_BASE = ITEMS.register("advanced_crystal_base", 
			() -> new Item(new Item.Properties().rarity(Rarity.RARE).stacksTo(16)));
	
	// Food
	
	// Useful Items
	public static final Supplier<Item> LIQUID_NAQUADAH_BUCKET = ITEMS.register("liquid_naquadah_bucket", 
			() -> new BucketItem(FluidInit.LIQUID_NAQUADAH_SOURCE.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
	public static final Supplier<Item> HEAVY_LIQUID_NAQUADAH_BUCKET = ITEMS.register("heavy_liquid_naquadah_bucket", 
			() -> new BucketItem(FluidInit.HEAVY_LIQUID_NAQUADAH_SOURCE.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
	
	public static final Supplier<Item> MATOK = ITEMS.register("matok", 
			() -> new StaffWeaponItem(new StaffWeaponItem.Properties().stacksTo(1)));
	
	public static final Supplier<Item> PDA = ITEMS.register("pda", 
			() -> new PDAItem(new PDAItem.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant()));
	
	/*public static final Supplier<Item> UNIVERSE_DIALER = ITEMS.register("universe_dialer", 
			() -> new DialerItem(new PDAItem.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));*/

	public static final DeferredItem<StargateChangeCrystal> CHANGE_CRYSTAL = ITEMS.register("stargate_change_crystal",
			() -> new StargateChangeCrystal(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));
	// Crystals
	public static final Supplier<ControlCrystalItem> CONTROL_CRYSTAL = ITEMS.register("control_crystal", 
			() -> new ControlCrystalItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
	public static final Supplier<ControlCrystalItem> LARGE_CONTROL_CRYSTAL = ITEMS.register("large_control_crystal", 
			() -> new ControlCrystalItem.Large(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
	public static final Supplier<MemoryCrystalItem> MEMORY_CRYSTAL = ITEMS.register("memory_crystal", 
			() -> new MemoryCrystalItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
	public static final Supplier<MaterializationCrystalItem> MATERIALIZATION_CRYSTAL = ITEMS.register("materialization_crystal", 
			() -> new MaterializationCrystalItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
	public static final Supplier<EnergyCrystalItem> ENERGY_CRYSTAL = ITEMS.register("energy_crystal", 
			() -> new EnergyCrystalItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
	public static final Supplier<TransferCrystalItem> TRANSFER_CRYSTAL = ITEMS.register("transfer_crystal", 
			() -> new TransferCrystalItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
	public static final Supplier<CommunicationCrystalItem> COMMUNICATION_CRYSTAL = ITEMS.register("communication_crystal", 
			() -> new CommunicationCrystalItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));

	public static final Supplier<ControlCrystalItem> ADVANCED_CONTROL_CRYSTAL = ITEMS.register("advanced_control_crystal", 
			() -> new ControlCrystalItem.Advanced(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));
	public static final Supplier<MemoryCrystalItem> ADVANCED_MEMORY_CRYSTAL = ITEMS.register("advanced_memory_crystal", 
			() -> new MemoryCrystalItem.Advanced(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));
	public static final Supplier<MaterializationCrystalItem> ADVANCED_MATERIALIZATION_CRYSTAL = ITEMS.register("advanced_materialization_crystal", 
			() -> new MaterializationCrystalItem.Advanced(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));
	public static final Supplier<EnergyCrystalItem> ADVANCED_ENERGY_CRYSTAL = ITEMS.register("advanced_energy_crystal", 
			() -> new EnergyCrystalItem.Advanced(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));
	public static final Supplier<TransferCrystalItem> ADVANCED_TRANSFER_CRYSTAL = ITEMS.register("advanced_transfer_crystal", 
			() -> new TransferCrystalItem.Advanced(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));
	public static final Supplier<CommunicationCrystalItem> ADVANCED_COMMUNICATION_CRYSTAL = ITEMS.register("advanced_communication_crystal", 
			() -> new CommunicationCrystalItem.Advanced(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));

	// Tools
	public static final Supplier<SGJourneySwordItem> NAQUADAH_SWORD = ITEMS.register("naquadah_sword", 
			() -> new SGJourneySwordItem(ToolMaterialInit.naquadah, 6, -2.4f, new Item.Properties().fireResistant()));
	public static final Supplier<SGJourneyPickaxeItem> NAQUADAH_PICKAXE = ITEMS.register("naquadah_pickaxe", 
			() -> new SGJourneyPickaxeItem(ToolMaterialInit.naquadah, 2, -2.8f, new Item.Properties().fireResistant()));
	public static final Supplier<SGJourneyAxeItem> NAQUADAH_AXE = ITEMS.register("naquadah_axe", 
			() -> new SGJourneyAxeItem(ToolMaterialInit.naquadah, 8.0f, -3.0f, new Item.Properties().fireResistant()));
	public static final Supplier<SGJourneyShovelItem> NAQUADAH_SHOVEL = ITEMS.register("naquadah_shovel", 
			() -> new SGJourneyShovelItem(ToolMaterialInit.naquadah, 2.5f, -3.0f, new Item.Properties().fireResistant()));
	public static final Supplier<SGJourneyHoeItem> NAQUADAH_HOE = ITEMS.register("naquadah_hoe", 
			() -> new SGJourneyHoeItem(ToolMaterialInit.naquadah, -2, 0.0f, new Item.Properties().fireResistant()));

	public static final Supplier<NaquadahArmorItem> NAQUADAH_HELMET = ITEMS.register("naquadah_helmet",
			() -> new NaquadahArmorItem(ArmorMaterialInit.NAQUADAH, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().durability(9200).stacksTo(1)));
	public static final Supplier<NaquadahArmorItem> NAQUADAH_CHESTPLATE = ITEMS.register("naquadah_chestplate",
			() -> new NaquadahArmorItem(ArmorMaterialInit.NAQUADAH, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().durability(12600).stacksTo(1)));
	public static final Supplier<NaquadahArmorItem> NAQUADAH_LEGGINGS = ITEMS.register("naquadah_leggings",
			() -> new NaquadahArmorItem(ArmorMaterialInit.NAQUADAH, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant().durability(9600).stacksTo(1)));
	public static final Supplier<NaquadahArmorItem> NAQUADAH_BOOTS = ITEMS.register("naquadah_boots",
			() -> new NaquadahArmorItem(ArmorMaterialInit.NAQUADAH, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant().durability(8600).stacksTo(1)));


	public static final Supplier<CallForwardingDevice> CALL_FORWARDING_DEVICE = ITEMS.register("call_forwarding_device", 
			() -> new CallForwardingDevice(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant()));
		
	
	public static void register(IEventBus eventBus)
	{
		ITEMS.register(eventBus);
	}
}
