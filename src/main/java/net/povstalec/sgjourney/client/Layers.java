package net.povstalec.sgjourney.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.client.models.FalconArmorModel;
import net.povstalec.sgjourney.client.models.JackalArmorModel;
import net.povstalec.sgjourney.client.models.TransportRingsModel;

public class Layers
{
	// Transport Rings
	public static final ModelLayerLocation TRANSPORT_RING_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "transport_ring"), "main");
	
	// Armor
	public static final ModelLayerLocation FALCON_HEAD = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "falcon_head"), "main");
	public static final ModelLayerLocation JACKAL_HEAD = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "jackal_head"), "main");
	
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		// Transport Rings
		event.registerLayerDefinition(TRANSPORT_RING_LAYER, () -> TransportRingsModel.createRingLayer());
		
		// Armor
		event.registerLayerDefinition(FALCON_HEAD, FalconArmorModel::createBodyLayer);
		event.registerLayerDefinition(JACKAL_HEAD, JackalArmorModel::createBodyLayer);
	}

}
