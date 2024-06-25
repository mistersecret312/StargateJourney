package net.povstalec.sgjourney.common.init;

import dan200.computercraft.ComputerCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.block_entities.CartoucheEntity;
import net.povstalec.sgjourney.common.block_entities.RingPanelEntity;
import net.povstalec.sgjourney.common.block_entities.SymbolBlockEntity;
import net.povstalec.sgjourney.common.block_entities.dhd.ClassicDHDEntity;
import net.povstalec.sgjourney.common.block_entities.dhd.MilkyWayDHDEntity;
import net.povstalec.sgjourney.common.block_entities.dhd.PegasusDHDEntity;
import net.povstalec.sgjourney.common.block_entities.stargate.ClassicStargateEntity;
import net.povstalec.sgjourney.common.block_entities.stargate.MilkyWayStargateEntity;
import net.povstalec.sgjourney.common.block_entities.stargate.PegasusStargateEntity;
import net.povstalec.sgjourney.common.block_entities.stargate.TollanStargateEntity;
import net.povstalec.sgjourney.common.block_entities.stargate.UniverseStargateEntity;
import net.povstalec.sgjourney.common.block_entities.tech.AdvancedCrystalInterfaceEntity;
import net.povstalec.sgjourney.common.block_entities.tech.BasicInterfaceEntity;
import net.povstalec.sgjourney.common.block_entities.tech.CrystalInterfaceEntity;
import net.povstalec.sgjourney.common.block_entities.tech.TransportRingsEntity;

import java.util.function.Supplier;

public class BlockEntityInit
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, StargateJourney.MODID);
	
	public static final Supplier<BlockEntityType<UniverseStargateEntity>> UNIVERSE_STARGATE = BLOCK_ENTITIES.register("universe_stargate",
            () -> BlockEntityType.Builder.of(UniverseStargateEntity::new, BlockInit.UNIVERSE_STARGATE.get()).build(null));
	
	public static final Supplier<BlockEntityType<MilkyWayStargateEntity>> MILKY_WAY_STARGATE = BLOCK_ENTITIES.register("milky_way_stargate",
            () -> BlockEntityType.Builder.of(MilkyWayStargateEntity::new, BlockInit.MILKY_WAY_STARGATE.get()).build(null));
	public static final Supplier<BlockEntityType<MilkyWayDHDEntity>> MILKY_WAY_DHD = BLOCK_ENTITIES.register("milky_way_dhd",
            () -> BlockEntityType.Builder.of(MilkyWayDHDEntity::new, BlockInit.MILKY_WAY_DHD.get()).build(null));
	
	public static final Supplier<BlockEntityType<PegasusStargateEntity>> PEGASUS_STARGATE = BLOCK_ENTITIES.register("pegasus_stargate",
            () -> BlockEntityType.Builder.of(PegasusStargateEntity::new, BlockInit.PEGASUS_STARGATE.get()).build(null));
	public static final Supplier<BlockEntityType<PegasusDHDEntity>> PEGASUS_DHD = BLOCK_ENTITIES.register("pegasus_dhd",
            () -> BlockEntityType.Builder.of(PegasusDHDEntity::new, BlockInit.PEGASUS_DHD.get()).build(null));

	public static final Supplier<BlockEntityType<ClassicStargateEntity>> CLASSIC_STARGATE = BLOCK_ENTITIES.register("classic_stargate",
            () -> BlockEntityType.Builder.of(ClassicStargateEntity::new, BlockInit.CLASSIC_STARGATE.get()).build(null));
	public static final Supplier<BlockEntityType<TollanStargateEntity>> TOLLAN_STARGATE = BLOCK_ENTITIES.register("tollan_stargate",
			() -> BlockEntityType.Builder.of(TollanStargateEntity::new, BlockInit.TOLLAN_STARGATE.get()).build(null));
	public static final Supplier<BlockEntityType<ClassicDHDEntity>> CLASSIC_DHD = BLOCK_ENTITIES.register("classic_dhd",
            () -> BlockEntityType.Builder.of(ClassicDHDEntity::new, BlockInit.CLASSIC_DHD.get()).build(null));
	
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TransportRingsEntity>> TRANSPORT_RINGS = BLOCK_ENTITIES.register("transport_rings",
            () -> BlockEntityType.Builder.of(TransportRingsEntity::new, new Block[]{BlockInit.TRANSPORT_RINGS.get()}).build(null));

	ComputerCraft

	public static final Supplier<BlockEntityType<RingPanelEntity>> RING_PANEL = BLOCK_ENTITIES.register("ring_panel",
            () -> BlockEntityType.Builder.of(RingPanelEntity::new, BlockInit.RING_PANEL.get()).build(null));
	
	public static final Supplier<BlockEntityType<CartoucheEntity.Sandstone>> SANDSTONE_CARTOUCHE = BLOCK_ENTITIES.register("sandstone_cartouche",
            () -> BlockEntityType.Builder.of(CartoucheEntity.Sandstone::new, BlockInit.SANDSTONE_CARTOUCHE.get()).build(null));
	public static final Supplier<BlockEntityType<CartoucheEntity.Stone>> STONE_CARTOUCHE = BLOCK_ENTITIES.register("stone_cartouche",
            () -> BlockEntityType.Builder.of(CartoucheEntity.Stone::new, BlockInit.STONE_CARTOUCHE.get()).build(null));
	
	public static final Supplier<BlockEntityType<SymbolBlockEntity.Stone>> STONE_SYMBOL = BLOCK_ENTITIES.register("stone_symbol",
            () -> BlockEntityType.Builder.of(SymbolBlockEntity.Stone::new, BlockInit.STONE_SYMBOL.get()).build(null));
	public static final Supplier<BlockEntityType<SymbolBlockEntity.Sandstone>> SANDSTONE_SYMBOL = BLOCK_ENTITIES.register("sandstone_symbol",
            () -> BlockEntityType.Builder.of(SymbolBlockEntity.Sandstone::new, BlockInit.SANDSTONE_SYMBOL.get()).build(null));

	public static final Supplier<BlockEntityType<BasicInterfaceEntity>> BASIC_INTERFACE = BLOCK_ENTITIES.register("basic_interface",
            () -> BlockEntityType.Builder.of(BasicInterfaceEntity::new, BlockInit.BASIC_INTERFACE.get()).build(null));
	public static final Supplier<BlockEntityType<CrystalInterfaceEntity>> CRYSTAL_INTERFACE = BLOCK_ENTITIES.register("crystal_interface",
            () -> BlockEntityType.Builder.of(CrystalInterfaceEntity::new, BlockInit.CRYSTAL_INTERFACE.get()).build(null));
	public static final Supplier<BlockEntityType<AdvancedCrystalInterfaceEntity>> ADVANCED_CRYSTAL_INTERFACE = BLOCK_ENTITIES.register("advanced_crystal_interface",
            () -> BlockEntityType.Builder.of(AdvancedCrystalInterfaceEntity::new, BlockInit.ADVANCED_CRYSTAL_INTERFACE.get()).build(null));

	public static void register(IEventBus eventBus)
	{
		BLOCK_ENTITIES.register(eventBus);
	}
}
