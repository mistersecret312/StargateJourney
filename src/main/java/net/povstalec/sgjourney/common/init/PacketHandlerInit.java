package net.povstalec.sgjourney.common.init;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.packets.*;

import java.util.*;

public final class PacketHandlerInit
{
	private static final Set<String> seenChannel = new HashSet<>();
	private static final List<CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, ? extends NetworkMessage<ServerNetworkContext>>> serverMessages = new ArrayList<>();
	private static final List<CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, ? extends NetworkMessage<ClientNetworkContext>>> clientMessages = new ArrayList<>();


	public static final CustomPacketPayload.Type<ServerboundDHDUpdatePacket> DHD_UPDATE = registerServerbound("dhd_update", ServerboundDHDUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ServerboundRingPanelUpdatePacket> RING_UPDATE = registerServerbound("ring_update", ServerboundRingPanelUpdatePacket.STREAM_CODEC);

	public static final CustomPacketPayload.Type<ClientboundCartoucheUpdatePacket> CARTOUCHE = registerClientbound("cartouche", ClientboundCartoucheUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundCrystallizerUpdatePacket> CRYSTALLIZER = registerClientbound("crystallizer", ClientboundCrystallizerUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundDHDUpdatePacket> DHD = registerClientbound("dhd", ClientboundDHDUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundDialerUpdatePacket> DIALER = registerClientbound("dialer", ClientboundDialerUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundInterfaceUpdatePacket> INTERFACE = registerClientbound("interface", ClientboundInterfaceUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundMilkyWayStargateUpdatePacket> MW = registerClientbound("mw", ClientboundMilkyWayStargateUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundNaquadahGeneratorUpdatePacket> NAQUADAH_GENERATOR = registerClientbound("naquadah_generator", ClientboundNaquadahGeneratorUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundNaquadahLiquidizerUpdatePacket> NAQUADAH_LIQUIDIZER = registerClientbound("naquadah_liquidizer", ClientboundNaquadahLiquidizerUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundPegasusStargateUpdatePacket> PG = registerClientbound("pg", ClientboundPegasusStargateUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundRingPanelUpdatePacket> RING_PANEL = registerClientbound("ring_panel", ClientboundRingPanelUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundRingsUpdatePacket> RINGS = registerClientbound("rings", ClientboundRingsUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundStargateUpdatePacket> STARGATE_UPDATE = registerClientbound("stargate_update", ClientboundStargateUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundSymbolUpdatePacket> SYMBOLS = registerClientbound("symbols", ClientboundSymbolUpdatePacket.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientboundUniverseStargateUpdatePacket> UNIVERSE_STARGATE_UPDATE = registerClientbound("universe_stargate_update", ClientboundUniverseStargateUpdatePacket.STREAM_CODEC);

	public static final CustomPacketPayload.Type<ClientBoundSoundPackets.OpenWormhole> SOUND_OPEN_WORMHOLE = registerClientbound("sound_open_wormhole", ClientBoundSoundPackets.OpenWormhole.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientBoundSoundPackets.IdleWormhole> SOUND_IDLE_WORMHOLE = registerClientbound("sound_idle_wormhole", ClientBoundSoundPackets.IdleWormhole.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientBoundSoundPackets.CloseWormhole> SOUND_CLOSE_WORMHOLE = registerClientbound("sound_close_wormhole", ClientBoundSoundPackets.CloseWormhole.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientBoundSoundPackets.Chevron> SOUND_CHEVRON = registerClientbound("sound_chevron", ClientBoundSoundPackets.Chevron.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientBoundSoundPackets.Fail> SOUND_FAIL = registerClientbound("sound_fail", ClientBoundSoundPackets.Fail.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientBoundSoundPackets.StargateRotation> SOUND_STARGATE_ROTATION = registerClientbound("sound_stargate_rotation", ClientBoundSoundPackets.StargateRotation.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientBoundSoundPackets.UniverseStart> SOUND_STARGATE_UNIVERSE_START = registerClientbound("sound_stargate_universe_start", ClientBoundSoundPackets.UniverseStart.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientBoundSoundPackets.MilkyWayBuildup> SOUND_STARGATE_MILKYWAY_BUILDUP = registerClientbound("sound_stargate_milkyway_buildup", ClientBoundSoundPackets.MilkyWayBuildup.STREAM_CODEC);
	public static final CustomPacketPayload.Type<ClientBoundSoundPackets.MilkyWayStop> SOUND_STARGATE_MILKYWAY_STOP = registerClientbound("sound_stargate_milkyway_stop", ClientBoundSoundPackets.MilkyWayStop.STREAM_CODEC);


	public PacketHandlerInit(){}

	private static <C, T extends NetworkMessage<C>> CustomPacketPayload.Type<T> register(
			List<CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, ? extends NetworkMessage<C>>> messages,
			String channel, StreamCodec<RegistryFriendlyByteBuf, T> codec
	) {
		if (!seenChannel.add(channel)) throw new IllegalArgumentException("Duplicate channel " + channel);
		var type = new CustomPacketPayload.Type<T>(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, channel));
		messages.add(new CustomPacketPayload.TypeAndCodec<>(type, codec));
		return type;
	}

	private static <T extends NetworkMessage<ServerNetworkContext>> CustomPacketPayload.Type<T> registerServerbound(String id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
		return register(serverMessages, id, codec);
	}

	private static <T extends NetworkMessage<ClientNetworkContext>> CustomPacketPayload.Type<T> registerClientbound(String id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
		return register(clientMessages, id, codec);
	}

	/**
	 * Get all serverbound message types.
	 *
	 * @return An unmodifiable sequence of all serverbound message types.
	 */
	public static Collection<CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, ? extends NetworkMessage<ServerNetworkContext>>> getServerbound() {
		return Collections.unmodifiableCollection(serverMessages);
	}

	/**
	 * Get all clientbound message types.
	 *
	 * @return An unmodifiable sequence of all clientbound message types.
	 */
	public static Collection<CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, ? extends NetworkMessage<ClientNetworkContext>>> getClientbound() {
		return Collections.unmodifiableCollection(clientMessages);
	}

	public static void sendToPlayer(NetworkMessage<ClientNetworkContext> message, ServerPlayer player) {
		player.connection.send(createPacket(message));
	}

	/**
	 * Send a message to a set of players.
	 *
	 * @param message The message to send.
	 * @param players The players to send it to.
	 */
	public static void sendToPlayers(NetworkMessage<ClientNetworkContext> message, Collection<ServerPlayer> players) {
		if (players.isEmpty()) return;
		var packet = createPacket(message);
		for (var player : players) player.connection.send(packet);
	}

	/**
	 * Send a message to all players.
	 *
	 * @param message The message to send.
	 * @param server  The current server.
	 */
	public static void sendToAllPlayers(NetworkMessage<ClientNetworkContext> message, MinecraftServer server) {
		server.getPlayerList().broadcastAll(createPacket(message));
	}

	/**
	 * Send a message to all players around a point.
	 *
	 * @param message  The message to send.
	 * @param level    The level the point is in.
	 * @param pos      The centre position.
	 * @param distance The distance to the centre players must be within.
	 */
	public static void sendToAllAround(NetworkMessage<ClientNetworkContext> message, ServerLevel level, Vec3 pos, float distance) {
		level.getServer().getPlayerList().broadcast(null, pos.x, pos.y, pos.z, distance, level.dimension(), createPacket(message));
	}

	/**
	 * Send a message to all players tracking a chunk.
	 *
	 * @param message The message to send.
	 * @param chunk   The chunk players must be tracking.
	 */
	public static void sendToAllTracking(NetworkMessage<ClientNetworkContext> message, LevelChunk chunk) {
		var packet = createPacket(message);
		for (var player : ((ServerChunkCache) chunk.getLevel().getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false)) {
			player.connection.send(packet);
		}
	}

	/**
	 * Convert a clientbound {@link NetworkMessage} to a Minecraft {@link Packet}.
	 *
	 * @param message The message to convert.
	 * @return The converted message.
	 */
	private static Packet<ClientCommonPacketListener> createPacket(NetworkMessage<ClientNetworkContext> message) {
		return new ClientboundCustomPayloadPacket(message);
	}
}
