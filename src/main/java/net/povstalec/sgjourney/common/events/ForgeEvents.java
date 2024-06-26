package net.povstalec.sgjourney.common.events;

import dan200.computercraft.impl.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ClientPayloadHandler;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateBlock;
import net.povstalec.sgjourney.common.capabilities.AncientGene;
import net.povstalec.sgjourney.common.capabilities.BloodstreamNaquadah;
import net.povstalec.sgjourney.common.capabilities.IAncientGene;
import net.povstalec.sgjourney.common.capabilities.IBloodstreamNaquadah;
import net.povstalec.sgjourney.common.config.CommonGeneticConfig;
import net.povstalec.sgjourney.common.data.StargateNetwork;
import net.povstalec.sgjourney.common.data.TransporterNetwork;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;
import net.povstalec.sgjourney.common.packets.ClientNetworkContext;
import net.povstalec.sgjourney.common.packets.ClientboundCartoucheUpdatePacket;
import net.povstalec.sgjourney.common.packets.NetworkMessage;
import net.povstalec.sgjourney.common.packets.ServerNetworkContext;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = StargateJourney.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeEvents {



	@SubscribeEvent
	public static void onServerStarting(ServerStartingEvent event) {
		MinecraftServer server = event.getServer();

		StargateNetwork.get(server).updateNetwork(server);
		StargateNetwork.get(server).addStargates(server);

		TransporterNetwork.get(server).updateNetwork(server);
	}

	private static AbstractStargateEntity getStargateAtPos(Level level, BlockPos pos, BlockState blockstate) {
		if (blockstate.getBlock() instanceof AbstractStargateBlock stargateBlock) {
			AbstractStargateEntity stargate = stargateBlock.getStargate(level, pos, blockstate);

			return stargate;
		}

		return null;
	}

	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		Entity entity = event.getEntity();

		if (level.isClientSide())
			return;

		if (event.getEntity() instanceof AbstractVillager villager)
			AncientGene.inheritGene(villager, CommonGeneticConfig.villager_player_ata_gene_inheritance_chance.get());
	}

	@SubscribeEvent
	public static void onTick(ServerTickEvent.Pre event) {
		MinecraftServer server = event.getServer();
		StargateNetwork.get(server).handleConnections();
	}

	@SubscribeEvent
	public static void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();

		long seed = ((ServerLevel) player.level()).getSeed();
		seed += player.getUUID().hashCode();

		AncientGene.inheritGene(seed, player, CommonGeneticConfig.player_ata_gene_inheritance_chance.get());
	}

	@SubscribeEvent
	public static void onLivingTick(EntityTickEvent.Post event) {
		Entity entity = event.getEntity();
		Level level = entity.level();

		//TODO Make this into something you can edit with Datapacks
		if (!level.dimension().location().equals(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "cavum_tenebrae")))
			return;

		if (entity instanceof Player player) {
			if (player.isCreative() && player.getAbilities().flying)
				return;
			else if (player.isSpectator() && player.getAbilities().flying)
				return;
		}

		long daytime = (level.getDayTime() + 6000) % 24000;
		double percentage = (double) daytime / 12000;

		double sin = Math.sin(percentage * Math.PI - Math.PI / 2);
		double cos = Math.cos(percentage * Math.PI - Math.PI / 2);
		Vec3 gravityVector = new Vec3(Math.abs(cos) > 0.2 ? 0.07 * cos : 0, sin < 0 ? 0 : 0.07 * sin, 0);

		Vec3 movementVector = entity.getDeltaMovement();
		movementVector = movementVector.add(gravityVector);
		entity.setDeltaMovement(movementVector);

		entity.fallDistance = entity.fallDistance * (float) (-sin + 1);
	}

	@SubscribeEvent
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		Player original = event.getOriginal();
		Player clone = event.getEntity();

		IBloodstreamNaquadah oldCap = original.getCapability(BloodstreamNaquadah.BLOODSTREAM_NAQUADAH);
		clone.getCapability(BloodstreamNaquadah.BLOODSTREAM_NAQUADAH).copyFrom(oldCap);

		IAncientGene oldGene = original.getCapability(AncientGene.ANCIENT_GENE);
		clone.getCapability(AncientGene.ANCIENT_GENE).copyFrom(oldGene);
	}


}
