package net.povstalec.sgjourney.common.init;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.blocks.ATAGeneDetectorBlock;
import net.povstalec.sgjourney.common.blocks.ArcheologyTableBlock;
import net.povstalec.sgjourney.common.blocks.CartoucheBlock;
import net.povstalec.sgjourney.common.blocks.ChevronBlock;
import net.povstalec.sgjourney.common.blocks.ClassicStargateBaseBlock;
import net.povstalec.sgjourney.common.blocks.ExplosiveBlock;
import net.povstalec.sgjourney.common.blocks.FirePitBlock;
import net.povstalec.sgjourney.common.blocks.GoldenIdolBlock;
import net.povstalec.sgjourney.common.blocks.RingPanelBlock;
import net.povstalec.sgjourney.common.blocks.SecretSwitchBlock;
import net.povstalec.sgjourney.common.blocks.SymbolBlock;
import net.povstalec.sgjourney.common.blocks.dhd.AbstractDHDBlock;
import net.povstalec.sgjourney.common.blocks.dhd.ClassicDHDBlock;
import net.povstalec.sgjourney.common.blocks.dhd.MilkyWayDHDBlock;
import net.povstalec.sgjourney.common.blocks.dhd.PegasusDHDBlock;
import net.povstalec.sgjourney.common.blocks.stargate.ClassicStargateBlock;
import net.povstalec.sgjourney.common.blocks.stargate.ClassicStargateRingBlock;
import net.povstalec.sgjourney.common.blocks.stargate.MilkyWayStargateBlock;
import net.povstalec.sgjourney.common.blocks.stargate.MilkyWayStargateRingBlock;
import net.povstalec.sgjourney.common.blocks.stargate.PegasusStargateBlock;
import net.povstalec.sgjourney.common.blocks.stargate.PegasusStargateRingBlock;
import net.povstalec.sgjourney.common.blocks.stargate.TollanStargateBlock;
import net.povstalec.sgjourney.common.blocks.stargate.TollanStargateRingBlock;
import net.povstalec.sgjourney.common.blocks.stargate.UniverseStargateBlock;
import net.povstalec.sgjourney.common.blocks.stargate.UniverseStargateRingBlock;
import net.povstalec.sgjourney.common.blocks.tech.AdvancedCrystalInterfaceBlock;
import net.povstalec.sgjourney.common.blocks.tech.BasicInterfaceBlock;
import net.povstalec.sgjourney.common.blocks.tech.CrystalInterfaceBlock;
import net.povstalec.sgjourney.common.blocks.tech.TransportRingsBlock;
import net.povstalec.sgjourney.common.items.blocks.CartoucheBlockItem;
import net.povstalec.sgjourney.common.items.blocks.DHDItem;
import net.povstalec.sgjourney.common.items.blocks.StargateBlockItem;
import net.povstalec.sgjourney.common.items.blocks.TransporterBlockItem;

public class BlockInit
{
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(StargateJourney.MODID);
	
	//Block Tab
	public static final Supplier<UniverseStargateBlock> UNIVERSE_STARGATE = registerStargateBlock("universe_stargate", 
			() -> new UniverseStargateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F)
					.sound(SoundType.METAL).noOcclusion()), Rarity.EPIC);
	public static final Supplier<UniverseStargateRingBlock> UNIVERSE_RING = BLOCKS.registerBlock("universe_ring",
			UniverseStargateRingBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F)
					.sound(SoundType.METAL).noOcclusion());
	
	public static final Supplier<MilkyWayStargateBlock> MILKY_WAY_STARGATE = registerStargateBlock("milky_way_stargate", 
			() -> new MilkyWayStargateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F, 1200.0F)
					.sound(SoundType.METAL).noOcclusion()), Rarity.EPIC);
	public static final Supplier<MilkyWayStargateRingBlock> MILKY_WAY_RING = BLOCKS.register("milky_way_ring", 
			() -> new MilkyWayStargateRingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F, 1200.0F)
					.sound(SoundType.METAL).noOcclusion()));
	
	public static final Supplier<PegasusStargateBlock> PEGASUS_STARGATE = registerStargateBlock("pegasus_stargate", 
			() -> new PegasusStargateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F, 1200.0F)
					.sound(SoundType.METAL).noOcclusion()), Rarity.EPIC);
	public static final Supplier<PegasusStargateRingBlock> PEGASUS_RING = BLOCKS.register("pegasus_ring", 
			() -> new PegasusStargateRingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F, 1200.0F)
					.sound(SoundType.METAL).noOcclusion()));
	
	public static final Supplier<ClassicStargateBlock> CLASSIC_STARGATE = registerStargateBlock("classic_stargate", 
			() -> new ClassicStargateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F)
					.sound(SoundType.METAL).noOcclusion()), Rarity.UNCOMMON);
	public static final Supplier<ClassicStargateRingBlock> CLASSIC_RING = BLOCKS.register("classic_ring", 
			() -> new ClassicStargateRingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F)
					.sound(SoundType.METAL).noOcclusion()));
	public static final DeferredBlock<ClassicStargateBaseBlock> CLASSIC_STARGATE_BASE_BLOCK = registerBlock("classic_stargate_base_block", ClassicStargateBaseBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F), Rarity.UNCOMMON, 64);
	public static final DeferredBlock<Block> CLASSIC_STARGATE_CHEVRON_BLOCK = registerBlock("classic_stargate_chevron_block",
			Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F), Rarity.UNCOMMON, 64);
	public static final DeferredBlock<Block> CLASSIC_STARGATE_RING_BLOCK = registerBlock("classic_stargate_ring_block",
			Block::new, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F), Rarity.UNCOMMON, 64);
	
	public static final Supplier<TollanStargateBlock> TOLLAN_STARGATE = registerStargateBlock("tollan_stargate",
			() -> new TollanStargateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F, 1200.0F)
					.sound(SoundType.METAL).noOcclusion()), Rarity.EPIC);
	public static final Supplier<TollanStargateRingBlock> TOLLAN_RING = BLOCKS.register("tollan_ring",
			() -> new TollanStargateRingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F, 1200.0F)
					.sound(SoundType.METAL).noOcclusion()));
	
	public static final Supplier<AbstractDHDBlock> MILKY_WAY_DHD = registerDHDBlock("milky_way_dhd", 
			() -> new MilkyWayDHDBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F)
					.sound(SoundType.METAL).noOcclusion()), Rarity.EPIC);
	
	public static final Supplier<AbstractDHDBlock> PEGASUS_DHD = registerDHDBlock("pegasus_dhd", 
			() -> new PegasusDHDBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F)
					.sound(SoundType.METAL).noOcclusion()), Rarity.EPIC);
	
	public static final Supplier<AbstractDHDBlock> CLASSIC_DHD = registerDHDBlock("classic_dhd", 
			() -> new ClassicDHDBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F)
					.sound(SoundType.METAL).noOcclusion()), Rarity.UNCOMMON);
	
	public static final DeferredBlock<ChevronBlock> UNIVERSE_STARGATE_CHEVRON = registerBlock("universe_stargate_chevron",
			ChevronBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F)
					.requiresCorrectToolForDrops().noOcclusion().noCollission()
					.lightLevel((state) -> state.getValue(ChevronBlock.LIT) ? 7 : 0), Rarity.UNCOMMON, 64);
	
	public static final DeferredBlock<TransportRingsBlock> TRANSPORT_RINGS = registerTransporterBlock("transport_rings",
			TransportRingsBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F)
					.sound(SoundType.METAL).noOcclusion(), Rarity.RARE);
	public static final DeferredBlock<RingPanelBlock> RING_PANEL = registerBlock("ring_panel",
			RingPanelBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F)
                    .sound(SoundType.METAL).noOcclusion(), Rarity.RARE, 1);
	
	public static final Supplier<ExplosiveBlock> NAQUADAH_ORE = registerBlock("naquadah_ore", 
			() -> new ExplosiveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F).requiresCorrectToolForDrops(), 4.0F));
	public static final Supplier<ExplosiveBlock> NETHER_NAQUADAH_ORE = registerBlock("nether_naquadah_ore", 
			() -> new ExplosiveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F).requiresCorrectToolForDrops(), 4.0F));
	public static final Supplier<ExplosiveBlock> DEEPSLATE_NAQUADAH_ORE = registerBlock("deepslate_naquadah_ore", 
			() -> new ExplosiveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).requiresCorrectToolForDrops(), 4.0F));
	
	public static final Supplier<ExplosiveBlock> RAW_NAQUADAH_BLOCK = registerBlock("raw_naquadah_block", 
			() -> new ExplosiveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(5.0F, 6.0F).requiresCorrectToolForDrops(), 10.0F));
	
	public static final Supplier<LiquidBlock> LIQUID_NAQUADAH_BLOCK = registerBlock("liquid_naquadah", 
			() -> new LiquidBlock(FluidInit.LIQUID_NAQUADAH_SOURCE.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WATER).noCollission().strength(100.0F).noLootTable()));
	public static final Supplier<LiquidBlock> HEAVY_LIQUID_NAQUADAH_BLOCK = registerBlock("heavy_liquid_naquadah", 
			() -> new LiquidBlock(FluidInit.HEAVY_LIQUID_NAQUADAH_SOURCE.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WATER).noCollission().strength(100.0F).noLootTable()));
	
	public static final Supplier<Block> NAQUADAH_BLOCK = registerBlock("naquadah_block", 
			() -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F).requiresCorrectToolForDrops()));
	public static final Supplier<Block> NAQUADAH_STAIRS = registerBlock("naquadah_stairs", 
			() -> new StairBlock(NAQUADAH_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F).requiresCorrectToolForDrops()));
	public static final Supplier<Block> NAQUADAH_SLAB = registerBlock("naquadah_slab", 
			() -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F).requiresCorrectToolForDrops()));
	
	public static final Supplier<Block> CUT_NAQUADAH_BLOCK = registerBlock("cut_naquadah_block", 
			() -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F).requiresCorrectToolForDrops()));
	public static final Supplier<Block> CUT_NAQUADAH_STAIRS = registerBlock("cut_naquadah_stairs", 
			() -> new StairBlock(NAQUADAH_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F).requiresCorrectToolForDrops()));
	public static final Supplier<Block> CUT_NAQUADAH_SLAB = registerBlock("cut_naquadah_slab", 
			() -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 600.0F).requiresCorrectToolForDrops()));
	
	public static final Supplier<GoldenIdolBlock> GOLDEN_IDOL = registerBlock("golden_idol", 
			GoldenIdolBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).strength(3.0F, 6.0F)
                    .sound(SoundType.METAL).requiresCorrectToolForDrops(), Rarity.UNCOMMON, 16);

	
	public static final Supplier<FirePitBlock> FIRE_PIT = registerBlock("fire_pit", 
			() -> new FirePitBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).instabreak()
			.sound(SoundType.STONE).lightLevel((state) -> state.getValue(FirePitBlock.LIT) ? 15 : 0), ParticleTypes.FLAME));
	
	public static final Supplier<Block> SANDSTONE_WITH_LAPIS = registerBlock("sandstone_with_lapis", 
			() -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(0.8F).requiresCorrectToolForDrops()));
	public static final Supplier<Block> SANDSTONE_WITH_GOLD = registerBlock("sandstone_with_gold",
			() -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(0.8F).requiresCorrectToolForDrops()));
	public static final Supplier<Block> SANDSTONE_HIEROGLYPHS = registerBlock("sandstone_hieroglyphs", 
			() -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(0.8F).requiresCorrectToolForDrops()));
	public static final Supplier<SecretSwitchBlock> SANDSTONE_SWITCH = registerBlock("sandstone_switch", 
			() -> new SecretSwitchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(0.8F).requiresCorrectToolForDrops()));

	public static final Supplier<CartoucheBlock> SANDSTONE_CARTOUCHE = registerCartoucheBlock("sandstone_cartouche", 
			() -> new CartoucheBlock.Sandstone(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(0.8F).requiresCorrectToolForDrops()));
	public static final Supplier<CartoucheBlock> STONE_CARTOUCHE = registerCartoucheBlock("stone_cartouche", 
			() -> new CartoucheBlock.Stone(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F).requiresCorrectToolForDrops()));
	
	public static final Supplier<SymbolBlock> STONE_SYMBOL = registerBlock("stone_symbol", 
			() -> new SymbolBlock.Stone(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(0.8F).requiresCorrectToolForDrops()));
	public static final Supplier<SymbolBlock> SANDSTONE_SYMBOL = registerBlock("sandstone_symbol", 
			() -> new SymbolBlock.Sandstone(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F).requiresCorrectToolForDrops()));
	

	public static final Supplier<BasicInterfaceBlock> BASIC_INTERFACE = registerBlock("basic_interface", 
			() -> new BasicInterfaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F)), 1);
	public static final DeferredBlock<CrystalInterfaceBlock> CRYSTAL_INTERFACE = registerBlock("crystal_interface",
			CrystalInterfaceBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F), Rarity.UNCOMMON, 1);
	public static final DeferredBlock<AdvancedCrystalInterfaceBlock> ADVANCED_CRYSTAL_INTERFACE = registerBlock("advanced_crystal_interface",
			AdvancedCrystalInterfaceBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F), Rarity.RARE, 1);
	
	public static final Supplier<ATAGeneDetectorBlock> ANCIENT_GENE_DETECTOR = registerBlock("ancient_gene_detector", 
			ATAGeneDetectorBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F), Rarity.RARE, 1);
	
	private static <T extends Block>Supplier<T> registerBlock(String name, Supplier<T> block)
	{
		Supplier<T> toReturn = BLOCKS.register(name, block);
		
		registerBlockItem(name, toReturn);
		
		return toReturn;
	}
	private static <T extends Block>Supplier<T> registerBlock(String name, Supplier<T> block, int stacksTo)
	{
		Supplier<T> toReturn = BLOCKS.register(name, block);
		
		registerBlockItem(name, toReturn, stacksTo);
		
		return toReturn;
	}
	private static <T extends Block>DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> func, BlockBehaviour.Properties props, Rarity rarity, int stacksTo)
	{
		DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, func, props);
		
		registerBlockItem(name, toReturn, rarity, stacksTo);
		
		return toReturn;
	}
	
	private static <T extends Block>Supplier<T> registerStargateBlock(String name, Supplier<T> block, Rarity rarity)
	{
		Supplier<T> toReturn = BLOCKS.register(name, block);

		registerStargateBlockItem(name, toReturn, rarity, 1);
		
		return toReturn;
	}
	
	private static <T extends Block>DeferredBlock<T> registerTransporterBlock(String name, Function<BlockBehaviour.Properties, ? extends T> func, BlockBehaviour.Properties props, Rarity rarity)
	{
		DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, func, props);

		registerTransporterBlockItem(name, toReturn, rarity, 1);
		
		return toReturn;
	}
	
	private static <T extends Block>Supplier<T> registerDHDBlock(String name, Supplier<T> block, Rarity rarity)
	{
		Supplier<T> toReturn = BLOCKS.register(name, block);

		registerDHDItem(name, toReturn, rarity, 1);
		
		return toReturn;
	}
	
	private static <T extends Block>Supplier<T> registerCartoucheBlock(String name, Supplier<T> block)
	{
		Supplier<T> toReturn = BLOCKS.register(name, block);
		
		registerCartoucheBlockItem(name, toReturn, 1);
		
		return toReturn;
	}
	
	private static <T extends Block>Supplier<Item> registerBlockItem(String name, Supplier<T> block)
	{
		return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}
	
	private static <T extends Block>Supplier<Item> registerBlockItem(String name, Supplier<T> block, int stacksTo)
	{
		return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().stacksTo(stacksTo)));
	}
	
	private static <T extends Block>Supplier<Item> registerBlockItem(String name, Supplier<T> block, Rarity rarity, int stacksTo)
	{
		return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().rarity(rarity).stacksTo(stacksTo)));
	}
	
	private static <T extends Block>Supplier<Item> registerStargateBlockItem(String name, Supplier<T> block, Rarity rarity, int stacksTo)
	{
		return ItemInit.ITEMS.register(name, () -> new StargateBlockItem(block.get(), new Item.Properties().rarity(rarity).stacksTo(stacksTo).fireResistant()));
	}
	
	private static <T extends Block>Supplier<Item> registerTransporterBlockItem(String name, Supplier<T> block, Rarity rarity, int stacksTo)
	{
		return ItemInit.ITEMS.register(name, () -> new TransporterBlockItem(block.get(), new Item.Properties().rarity(rarity).stacksTo(stacksTo).fireResistant()));
	}
	
	private static <T extends Block>Supplier<Item> registerDHDItem(String name, Supplier<T> block, Rarity rarity, int stacksTo)
	{
		return ItemInit.ITEMS.register(name, () -> new DHDItem(block.get(), new Item.Properties().rarity(rarity).stacksTo(stacksTo)));
	}
	
	private static <T extends Block>Supplier<Item> registerCartoucheBlockItem(String name, Supplier<T> block, int stacksTo)
	{
		return ItemInit.ITEMS.register(name, () -> new CartoucheBlockItem(block.get(), new Item.Properties().stacksTo(stacksTo)));
	}
	
	public static void register(IEventBus eventBus)
	{
		BLOCKS.register(eventBus);
	}
}