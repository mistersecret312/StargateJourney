package net.povstalec.sgjourney;

import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.povstalec.sgjourney.common.capabilities.AncientGene;
import net.povstalec.sgjourney.common.capabilities.BloodstreamNaquadah;
import net.povstalec.sgjourney.common.data.StargateNetwork;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.povstalec.sgjourney.client.Layers;
import net.povstalec.sgjourney.client.render.block_entity.CartoucheRenderer;
import net.povstalec.sgjourney.client.render.block_entity.ClassicStargateRenderer;
import net.povstalec.sgjourney.client.render.block_entity.MilkyWayStargateRenderer;
import net.povstalec.sgjourney.client.render.block_entity.PegasusStargateRenderer;
import net.povstalec.sgjourney.client.render.block_entity.SymbolBlockRenderer;
import net.povstalec.sgjourney.client.render.block_entity.TollanStargateRenderer;
import net.povstalec.sgjourney.client.render.block_entity.TransportRingsRenderer;
import net.povstalec.sgjourney.client.render.block_entity.UniverseStargateRenderer;
import net.povstalec.sgjourney.client.render.entity.PlasmaProjectileRenderer;
import net.povstalec.sgjourney.client.render.level.SGJourneyDimensionSpecialEffects;
import net.povstalec.sgjourney.client.screens.ClassicDHDScreen;
import net.povstalec.sgjourney.client.screens.DHDCrystalScreen;
import net.povstalec.sgjourney.client.screens.InterfaceScreen;
import net.povstalec.sgjourney.client.screens.MilkyWayDHDScreen;
import net.povstalec.sgjourney.client.screens.PegasusDHDScreen;
import net.povstalec.sgjourney.client.screens.RingPanelScreen;
import net.povstalec.sgjourney.common.config.StargateJourneyConfig;
import net.povstalec.sgjourney.common.init.BlockEntityInit;
import net.povstalec.sgjourney.common.init.BlockInit;
import net.povstalec.sgjourney.common.init.EntityInit;
import net.povstalec.sgjourney.common.init.FeatureInit;
import net.povstalec.sgjourney.common.init.FluidInit;
import net.povstalec.sgjourney.common.init.FluidTypeInit;
import net.povstalec.sgjourney.common.init.ItemInit;
import net.povstalec.sgjourney.common.init.MenuInit;
import net.povstalec.sgjourney.common.init.MiscInit;
import net.povstalec.sgjourney.common.init.SoundInit;
import net.povstalec.sgjourney.common.init.StatisticsInit;
import net.povstalec.sgjourney.common.init.StructureInit;
import net.povstalec.sgjourney.common.init.TabInit;
import net.povstalec.sgjourney.common.init.VillagerInit;
import net.povstalec.sgjourney.common.items.properties.WeaponStatePropertyFunction;
import net.povstalec.sgjourney.common.stargate.AddressTable;
import net.povstalec.sgjourney.common.stargate.Galaxy;
import net.povstalec.sgjourney.common.stargate.PointOfOrigin;
import net.povstalec.sgjourney.common.stargate.SolarSystem;
import net.povstalec.sgjourney.common.stargate.StargateVariant;
import net.povstalec.sgjourney.common.stargate.SymbolSet;
import net.povstalec.sgjourney.common.stargate.Symbols;

@Mod(StargateJourney.MODID)
public class StargateJourney
{
    public static final String MODID = "sgjourney";
    public static final String EMPTY = MODID + ":empty";

    public static final String OCULUS_MODID = "oculus";
    
    private static Optional<Boolean> isOculusLoaded = Optional.empty();
    
    public static final Logger LOGGER = LogUtils.getLogger();

    public StargateJourney(IEventBus eventBus, ModContainer container)
    {
    	ItemInit.register(eventBus);
        BlockInit.register(eventBus);
        FluidInit.register(eventBus);
        FluidTypeInit.register(eventBus);
        BlockEntityInit.register(eventBus);
        MenuInit.register(eventBus);
        VillagerInit.register(eventBus);
        FeatureInit.register(eventBus);
        StructureInit.register(eventBus);
        EntityInit.register(eventBus);
        SoundInit.register(eventBus);
        TabInit.register(eventBus);
        StatisticsInit.register(eventBus);
        DataComponents.register(eventBus);

        eventBus.addListener((DataPackRegistryEvent.NewRegistry event) ->
        {
        	//TODO Move Galaxy above Point of Origin
        	// DON'T DELETE THIS COMMENT UNTIL I APPLY THE CHANGE TO OTHER VERSIONS OR I MIGHT FORGET
            event.dataPackRegistry(SymbolSet.REGISTRY_KEY, SymbolSet.CODEC, SymbolSet.CODEC);
            event.dataPackRegistry(Symbols.REGISTRY_KEY, Symbols.CODEC, Symbols.CODEC);
            event.dataPackRegistry(Galaxy.REGISTRY_KEY, Galaxy.CODEC, Galaxy.CODEC);
            event.dataPackRegistry(PointOfOrigin.REGISTRY_KEY, PointOfOrigin.CODEC, PointOfOrigin.CODEC);
            event.dataPackRegistry(SolarSystem.REGISTRY_KEY, SolarSystem.CODEC, SolarSystem.CODEC);
            event.dataPackRegistry(AddressTable.REGISTRY_KEY, AddressTable.CODEC, AddressTable.CODEC);
            event.dataPackRegistry(StargateVariant.REGISTRY_KEY, StargateVariant.CODEC, StargateVariant.CODEC);
        });
        
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(Layers::registerLayers);
        eventBus.addListener(TabInit::addCreative);
		
		container.registerConfig(ModConfig.Type.CLIENT, StargateJourneyConfig.CLIENT_CONFIG, "sgjourney-client.toml");
		container.registerConfig(ModConfig.Type.COMMON, StargateJourneyConfig.COMMON_CONFIG, "sgjourney-common.toml");

        //NeoForge.EVENT_BUS.register(StargateJourney.GameEvents.class);
		NeoForge.EVENT_BUS.addListener(MiscInit::registerCommands);
    }

    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
    	event.enqueueWork(() -> 
    	{
            StatisticsInit.register();
    		//VillagerInit.registerPOIs();
    	});
    }
    
    // BECAUSE OCULUS MESSES WITH RENDERING TOO MUCH
    public static boolean isOculusLoaded()
    {
    	if(isOculusLoaded.isEmpty())
    		isOculusLoaded = Optional.of(ModList.get().isLoaded(OCULUS_MODID));
    	
    	return isOculusLoaded.get();	
    }

    @EventBusSubscriber(modid = StargateJourney.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class GameEvents
    {
        @SubscribeEvent
        public static void onAttachCapabilitiesEvent(RegisterCapabilitiesEvent event)
        {
            event.registerEntity(AncientGene.ANCIENT_GENE, EntityType.PLAYER, (player, voi) -> new AncientGene());
            event.registerEntity(BloodstreamNaquadah.BLOODSTREAM_NAQUADAH, EntityType.PLAYER, (player, voi) -> new BloodstreamNaquadah());

            event.registerEntity(AncientGene.ANCIENT_GENE, EntityType.VILLAGER, (player, voi) -> new AncientGene());
            event.registerEntity(BloodstreamNaquadah.BLOODSTREAM_NAQUADAH, EntityType.VILLAGER, (player, voi) -> new BloodstreamNaquadah());
        }
    }

    @EventBusSubscriber(modid = StargateJourney.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event)
        {
            event.register(MenuInit.INTERFACE.get(), InterfaceScreen::new);
            event.register(MenuInit.RING_PANEL.get(), RingPanelScreen::new);
            event.register(MenuInit.DHD_CRYSTAL.get(), DHDCrystalScreen::new);
            event.register(MenuInit.MILKY_WAY_DHD.get(), MilkyWayDHDScreen::new);
            event.register(MenuInit.PEGASUS_DHD.get(), PegasusDHDScreen::new);
            event.register(MenuInit.CLASSIC_DHD.get(), ClassicDHDScreen::new);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        	ItemProperties.register(ItemInit.MATOK.get(), ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "open"), new WeaponStatePropertyFunction());
        	
            ItemBlockRenderTypes.setRenderLayer(FluidInit.LIQUID_NAQUADAH_SOURCE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(FluidInit.LIQUID_NAQUADAH_FLOWING.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(FluidInit.HEAVY_LIQUID_NAQUADAH_SOURCE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(FluidInit.HEAVY_LIQUID_NAQUADAH_FLOWING.get(), RenderType.translucent());
        	
        	EntityRenderers.register(EntityInit.JAFFA_PLASMA.get(), PlasmaProjectileRenderer::new);
        	
        	//EntityRenderers.register(EntityInit.GOAULD.get(), GoauldRenderer::new);
        	
        	BlockEntityRenderers.register(BlockEntityInit.SANDSTONE_CARTOUCHE.get(), CartoucheRenderer.Sandstone::new);
        	BlockEntityRenderers.register(BlockEntityInit.STONE_CARTOUCHE.get(), CartoucheRenderer.Stone::new);
        	
        	BlockEntityRenderers.register(BlockEntityInit.SANDSTONE_SYMBOL.get(), SymbolBlockRenderer.Sandstone::new);
        	BlockEntityRenderers.register(BlockEntityInit.STONE_SYMBOL.get(), SymbolBlockRenderer.Stone::new);
        	
        	BlockEntityRenderers.register(BlockEntityInit.TRANSPORT_RINGS.get(), TransportRingsRenderer::new);

        	BlockEntityRenderers.register(BlockEntityInit.UNIVERSE_STARGATE.get(), UniverseStargateRenderer::new);
        	BlockEntityRenderers.register(BlockEntityInit.MILKY_WAY_STARGATE.get(), MilkyWayStargateRenderer::new);
        	BlockEntityRenderers.register(BlockEntityInit.PEGASUS_STARGATE.get(), PegasusStargateRenderer::new);
        	BlockEntityRenderers.register(BlockEntityInit.CLASSIC_STARGATE.get(), ClassicStargateRenderer::new);
        	BlockEntityRenderers.register(BlockEntityInit.TOLLAN_STARGATE.get(), TollanStargateRenderer::new);
        }
        
        @SubscribeEvent
        public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event)
        {
            SGJourneyDimensionSpecialEffects.registerStargateJourneyEffects(event);
        }
    }

    public static final class DataComponents {
        public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.createDataComponents(StargateJourney.MODID);

        public static final Supplier<DataComponentType<ResourceLocation>> UPGRADE = REGISTRY.register("upgrade", () -> new DataComponentType<>() {
            @Nullable
            @Override
            public Codec<ResourceLocation> codec() {
                return ResourceLocation.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ResourceLocation> streamCodec() {
                return ResourceLocation.STREAM_CODEC;
            }
        });

        public static void register(IEventBus bus)
        {
            REGISTRY.register(bus);
        }
    }
    
}
