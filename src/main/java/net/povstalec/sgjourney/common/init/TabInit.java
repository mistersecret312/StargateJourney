package net.povstalec.sgjourney.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.blocks.dhd.MilkyWayDHDBlock;
import net.povstalec.sgjourney.common.blocks.dhd.PegasusDHDBlock;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateBaseBlock;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateBlock;
import net.povstalec.sgjourney.common.blocks.stargate.PegasusStargateBlock;
import net.povstalec.sgjourney.common.blocks.tech.AbstractTransporterBlock;
import net.povstalec.sgjourney.common.config.CommonStargateConfig;
import net.povstalec.sgjourney.common.items.crystals.StargateChangeCrystal;
import net.povstalec.sgjourney.common.stargate.StargateVariant;

import java.util.function.Supplier;

public class TabInit
{
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =  DeferredRegister.create(Registries.CREATIVE_MODE_TAB, StargateJourney.MODID);
	
	public static Supplier<CreativeModeTab> STARGATE_ITEMS = CREATIVE_MODE_TABS.register("stargate_items", () ->
		CreativeModeTab.builder().icon(() -> new ItemStack(ItemInit.NAQUADAH.get()))
		.title(Component.translatable("itemGroup.stargate_items")).build());

	public static Supplier<CreativeModeTab> STARGATE_STUFF = CREATIVE_MODE_TABS.register("stargate_stuff", () ->
		CreativeModeTab.builder().icon(() -> new ItemStack(BlockInit.MILKY_WAY_STARGATE.get()))
		.title(Component.translatable("itemGroup.stargate_stuff"))
		.withTabsBefore(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "stargate_blocks")).withTabsAfter(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "stargate_items"))
		.build());

	public static Supplier<CreativeModeTab> STARGATE_BLOCKS = CREATIVE_MODE_TABS.register("stargate_blocks", () ->
		CreativeModeTab.builder().icon(() -> new ItemStack(BlockInit.NAQUADAH_BLOCK.get()))
		.title(Component.translatable("itemGroup.stargate_blocks")).build());
	
	@SubscribeEvent
	public static void addCreative(final BuildCreativeModeTabContentsEvent event)
	{
		if(event.getTab() == STARGATE_ITEMS.get())
		{
			event.accept(ItemInit.RAW_NAQUADAH.get());
			event.accept(ItemInit.NAQUADAH_ALLOY.get());
			event.accept(ItemInit.PURE_NAQUADAH.get());
			event.accept(ItemInit.NAQUADAH.get());
			event.accept(ItemInit.LIQUID_NAQUADAH_BUCKET.get());
			event.accept(ItemInit.HEAVY_LIQUID_NAQUADAH_BUCKET.get());

			event.accept(ItemInit.NAQUADAH_ROD.get());
			event.accept(ItemInit.REACTION_CHAMBER.get());
			event.accept(ItemInit.PLASMA_CONVERTER.get());
			
			event.accept(ItemInit.PDA.get());
			
			event.accept(ItemInit.NAQUADAH_SWORD.get());
			event.accept(ItemInit.NAQUADAH_PICKAXE.get());
			event.accept(ItemInit.NAQUADAH_AXE.get());
			event.accept(ItemInit.NAQUADAH_SHOVEL.get());
			event.accept(ItemInit.NAQUADAH_HOE.get());

			event.accept(ItemInit.NAQUADAH_HELMET.get());
			event.accept(ItemInit.NAQUADAH_CHESTPLATE.get());
			event.accept(ItemInit.NAQUADAH_LEGGINGS.get());
			event.accept(ItemInit.NAQUADAH_BOOTS.get());

			event.accept(ItemInit.CRYSTAL_BASE.get());
			event.accept(ItemInit.ADVANCED_CRYSTAL_BASE.get());
			
			event.accept(ItemInit.LARGE_CONTROL_CRYSTAL.get());
			event.accept(ItemInit.CONTROL_CRYSTAL.get());
			event.accept(ItemInit.ADVANCED_CONTROL_CRYSTAL.get());
			event.accept(ItemInit.MEMORY_CRYSTAL.get());
			event.accept(ItemInit.ADVANCED_MEMORY_CRYSTAL.get());
			//event.accept(MemoryCrystalItem.atlantisAddress());
			//event.accept(MemoryCrystalItem.abydosAddress());
			event.accept(ItemInit.MATERIALIZATION_CRYSTAL.get());
			event.accept(ItemInit.ADVANCED_MATERIALIZATION_CRYSTAL.get());
			event.accept(ItemInit.ENERGY_CRYSTAL.get());
			event.accept(ItemInit.ADVANCED_ENERGY_CRYSTAL.get());
			event.accept(ItemInit.TRANSFER_CRYSTAL.get());
			event.accept(ItemInit.ADVANCED_TRANSFER_CRYSTAL.get());
			event.accept(ItemInit.COMMUNICATION_CRYSTAL.get());
			event.accept(ItemInit.ADVANCED_COMMUNICATION_CRYSTAL.get());
		}
		else if(event.getTab() == STARGATE_STUFF.get())
		{
			event.accept(AbstractStargateBaseBlock.addToNetworkTrue(new ItemStack(BlockInit.UNIVERSE_STARGATE.get())));
			event.accept(AbstractStargateBaseBlock.addToNetworkTrue(new ItemStack(BlockInit.MILKY_WAY_STARGATE.get())));
			event.accept(AbstractStargateBaseBlock.localPointOfOrigin(new ItemStack(BlockInit.MILKY_WAY_STARGATE.get())));
			event.accept(MilkyWayDHDBlock.milkyWayCrystalSetup());
			event.accept(AbstractStargateBaseBlock.addToNetworkTrue(new ItemStack(BlockInit.PEGASUS_STARGATE.get())));
			event.accept(PegasusStargateBlock.localSymbols(new ItemStack(BlockInit.PEGASUS_STARGATE.get())));
			event.accept(PegasusDHDBlock.pegasusCrystalSetup());
			event.accept(AbstractStargateBaseBlock.addToNetworkTrue(new ItemStack(BlockInit.CLASSIC_STARGATE.get())));
			event.accept(AbstractStargateBaseBlock.localPointOfOrigin(new ItemStack(BlockInit.CLASSIC_STARGATE.get())));
			event.accept(BlockInit.CLASSIC_STARGATE_BASE_BLOCK.get());
			event.accept(BlockInit.CLASSIC_STARGATE_CHEVRON_BLOCK.get());
			event.accept(BlockInit.CLASSIC_STARGATE_RING_BLOCK.get());
			event.accept(BlockInit.CLASSIC_DHD.get());
			event.accept(AbstractStargateBaseBlock.addToNetworkTrue(new ItemStack(BlockInit.TOLLAN_STARGATE.get())));
			
			event.accept(BlockInit.UNIVERSE_STARGATE_CHEVRON.get());
			
			event.accept(BlockInit.BASIC_INTERFACE.get());
			event.accept(BlockInit.CRYSTAL_INTERFACE.get());
			event.accept(BlockInit.ADVANCED_CRYSTAL_INTERFACE.get());
			event.accept(ItemInit.CALL_FORWARDING_DEVICE.get());

			event.accept(StargateChangeCrystal.stargateType(BlockInit.UNIVERSE_STARGATE.get()));
			event.accept(StargateChangeCrystal.stargateType(BlockInit.MILKY_WAY_STARGATE.get()));
			event.accept(StargateChangeCrystal.stargateType(BlockInit.PEGASUS_STARGATE.get()));
			event.accept(StargateChangeCrystal.stargateType(BlockInit.TOLLAN_STARGATE.get()));

			event.getParameters().holders().lookup(StargateVariant.REGISTRY_KEY).ifPresent(reg -> reg.listElementIds().forEach(id -> event.accept(StargateChangeCrystal.stargateVariant(id.location().toString()))));

		}
		else if(event.getTab() == STARGATE_BLOCKS.get())
		{
			event.accept(BlockInit.UNIVERSE_STARGATE_CHEVRON.get());
			
			event.accept(AbstractStargateBaseBlock.addToNetworkTrue(new ItemStack(BlockInit.TRANSPORT_RINGS.get())));
			event.accept(AbstractStargateBaseBlock.addToNetworkTrue(new ItemStack(BlockInit.RING_PANEL.get())));

			event.accept(BlockInit.GOLDEN_IDOL.get());

			event.accept(BlockInit.NAQUADAH_ORE.get());
			event.accept(BlockInit.DEEPSLATE_NAQUADAH_ORE.get());
			event.accept(BlockInit.NETHER_NAQUADAH_ORE.get());
			event.accept(BlockInit.RAW_NAQUADAH_BLOCK.get());
			
			event.accept(BlockInit.NAQUADAH_BLOCK.get());
			event.accept(BlockInit.NAQUADAH_STAIRS.get());
			event.accept(BlockInit.NAQUADAH_SLAB.get());
			event.accept(BlockInit.CUT_NAQUADAH_BLOCK.get());
			event.accept(BlockInit.CUT_NAQUADAH_STAIRS.get());
			event.accept(BlockInit.CUT_NAQUADAH_SLAB.get());
			
			event.accept(BlockInit.SANDSTONE_HIEROGLYPHS.get());
			event.accept(BlockInit.SANDSTONE_SWITCH.get());
			event.accept(BlockInit.SANDSTONE_WITH_LAPIS.get());
			event.accept(BlockInit.SANDSTONE_WITH_GOLD.get());
			event.accept(BlockInit.SANDSTONE_SYMBOL.get());
			event.accept(BlockInit.STONE_SYMBOL.get());
			
			event.accept(BlockInit.SANDSTONE_CARTOUCHE.get());
			event.accept(BlockInit.STONE_CARTOUCHE.get());
			
			event.accept(BlockInit.FIRE_PIT.get());
			
			event.accept(BlockInit.BASIC_INTERFACE.get());
			event.accept(BlockInit.CRYSTAL_INTERFACE.get());
			event.accept(BlockInit.ADVANCED_CRYSTAL_INTERFACE.get());

			event.accept(BlockInit.ANCIENT_GENE_DETECTOR.get());
		}
		else if(event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions())
		{
			event.accept(AbstractTransporterBlock.excludeFromNetwork(new ItemStack(BlockInit.UNIVERSE_STARGATE.get())));
			event.accept(AbstractTransporterBlock.excludeFromNetwork(new ItemStack(BlockInit.MILKY_WAY_STARGATE.get())));
			event.accept(AbstractTransporterBlock.excludeFromNetwork(new ItemStack(BlockInit.PEGASUS_STARGATE.get())));
			event.accept(AbstractTransporterBlock.excludeFromNetwork(new ItemStack(BlockInit.CLASSIC_STARGATE.get())));
			event.accept(AbstractTransporterBlock.excludeFromNetwork(new ItemStack(BlockInit.TOLLAN_STARGATE.get())));
			event.accept(AbstractTransporterBlock.excludeFromNetwork(new ItemStack(BlockInit.TRANSPORT_RINGS.get())));
		}
	}
	
	public static void register(IEventBus eventBus)
	{
		CREATIVE_MODE_TABS.register(eventBus);
	}
}
