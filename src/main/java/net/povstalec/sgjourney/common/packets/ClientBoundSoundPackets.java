package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.sound.SoundAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public abstract class ClientBoundSoundPackets implements NetworkMessage
{
	public final BlockPos pos;
    public final boolean stop;

    public ClientBoundSoundPackets(BlockPos pos, boolean stop)
    {
        this.pos = pos;
        this.stop = stop;
    }

    public ClientBoundSoundPackets(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readBoolean());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeBoolean(this.stop);
    }

    public static class OpenWormhole extends ClientBoundSoundPackets
    {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSoundPackets.OpenWormhole> STREAM_CODEC = StreamCodec.ofMember(ClientBoundSoundPackets.OpenWormhole::encode, ClientBoundSoundPackets.OpenWormhole::new);

		public OpenWormhole(BlockPos pos)
    	{
    		super(pos, false);
    	}
    	public OpenWormhole(RegistryFriendlyByteBuf buffer)
    	{
    		super(buffer);
    	}

		@Override
		public void handle() {
			SoundAccess.playWormholeOpenSound(pos);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return PacketHandlerInit.SOUND_OPEN_WORMHOLE;
		}
	}
    
    public static class IdleWormhole extends ClientBoundSoundPackets
    {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSoundPackets.IdleWormhole> STREAM_CODEC = StreamCodec.ofMember(ClientBoundSoundPackets.IdleWormhole::encode, ClientBoundSoundPackets.IdleWormhole::new);

		public IdleWormhole(BlockPos pos)
    	{
    		super(pos, false);
    	}
    	public IdleWormhole(RegistryFriendlyByteBuf buffer)
    	{
    		super(buffer);
    	}

		@Override
		public void handle() {
			SoundAccess.playWormholeIdleSound(pos);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return PacketHandlerInit.SOUND_IDLE_WORMHOLE;
		}
	}
    
    public static class CloseWormhole extends ClientBoundSoundPackets
    {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSoundPackets.CloseWormhole> STREAM_CODEC = StreamCodec.ofMember(ClientBoundSoundPackets.CloseWormhole::encode, ClientBoundSoundPackets.CloseWormhole::new);

		public CloseWormhole(BlockPos pos)
    	{
    		super(pos, false);
    	}
    	public CloseWormhole(RegistryFriendlyByteBuf buffer)
    	{
    		super(buffer);
    	}

		@Override
		public void handle() {
			SoundAccess.playWormholeCloseSound(pos);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return PacketHandlerInit.SOUND_CLOSE_WORMHOLE;
		}
	}

    public static class Chevron implements NetworkMessage
    {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSoundPackets.Chevron> STREAM_CODEC = StreamCodec.ofMember(ClientBoundSoundPackets.Chevron::encode, ClientBoundSoundPackets.Chevron::new);

		public final BlockPos pos;
    	public final boolean primary;
    	public final boolean incoming;
    	public final boolean open;
    	public final boolean encode;
    	
    	public Chevron(BlockPos pos, boolean primary, boolean incoming, boolean open, boolean encode)
    	{
    		this.pos = pos;
    		this.primary = primary;
    		this.incoming = incoming;
    		this.open = open;
    		this.encode = encode;
    	}
    	public Chevron(RegistryFriendlyByteBuf buffer)
    	{
    		 this(buffer.readBlockPos(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    	}

        public void encode(RegistryFriendlyByteBuf buffer)
        {
            buffer.writeBlockPos(this.pos);
            buffer.writeBoolean(this.primary);
            buffer.writeBoolean(this.incoming);
            buffer.writeBoolean(this.open);
            buffer.writeBoolean(this.encode);
        }

		@Override
		public void handle() {
			SoundAccess.playChevronSound(pos, primary, incoming, open, encode);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return PacketHandlerInit.SOUND_CHEVRON;
		}
	}

    public static class Fail extends ClientBoundSoundPackets
    {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSoundPackets.Fail> STREAM_CODEC = StreamCodec.ofMember(ClientBoundSoundPackets.Fail::encode, ClientBoundSoundPackets.Fail::new);

		public Fail(BlockPos pos)
    	{
    		super(pos, false);
    	}
    	public Fail(RegistryFriendlyByteBuf buffer)
    	{
    		super(buffer);
    	}

		@Override
		public void handle() {
			SoundAccess.playFailSound(pos);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return PacketHandlerInit.SOUND_FAIL;
		}
	}
    
    public static class StargateRotation extends ClientBoundSoundPackets
    {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSoundPackets.StargateRotation> STREAM_CODEC = StreamCodec.ofMember(ClientBoundSoundPackets.StargateRotation::encode, ClientBoundSoundPackets.StargateRotation::new);

		public StargateRotation(BlockPos pos, boolean stop)
    	{
    		super(pos, stop);
    	}
    	public StargateRotation(RegistryFriendlyByteBuf buffer)
    	{
    		super(buffer);
    	}

		@Override
		public void handle() {
			SoundAccess.playRotationSound(pos, stop);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return PacketHandlerInit.SOUND_STARGATE_ROTATION;
		}
	}
    
    public static class UniverseStart extends ClientBoundSoundPackets
    {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSoundPackets.UniverseStart> STREAM_CODEC = StreamCodec.ofMember(ClientBoundSoundPackets.UniverseStart::encode, ClientBoundSoundPackets.UniverseStart::new);

		public UniverseStart(BlockPos pos)
    	{
    		super(pos, false);
    	}
    	public UniverseStart(RegistryFriendlyByteBuf buffer)
    	{
    		super(buffer);
    	}

		@Override
		public void handle() {
			SoundAccess.playUniverseStartSound(pos);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return PacketHandlerInit.SOUND_STARGATE_UNIVERSE_START;
		}
	}
    
    public static class MilkyWayBuildup extends ClientBoundSoundPackets
    {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSoundPackets.MilkyWayBuildup> STREAM_CODEC = StreamCodec.ofMember(ClientBoundSoundPackets.MilkyWayBuildup::encode, ClientBoundSoundPackets.MilkyWayBuildup::new);

		public MilkyWayBuildup(BlockPos pos)
    	{
    		super(pos, false);
    	}
    	public MilkyWayBuildup(RegistryFriendlyByteBuf buffer)
    	{
    		super(buffer);
    	}

		@Override
		public void handle() {
			SoundAccess.playMilkyWayBuildupSound(pos);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return PacketHandlerInit.SOUND_STARGATE_MILKYWAY_BUILDUP;
		}
	}
    
    public static class MilkyWayStop extends ClientBoundSoundPackets
    {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSoundPackets.MilkyWayStop> STREAM_CODEC = StreamCodec.ofMember(ClientBoundSoundPackets.MilkyWayStop::encode, ClientBoundSoundPackets.MilkyWayStop::new);

		public MilkyWayStop(BlockPos pos)
    	{
    		super(pos, false);
    	}
    	public MilkyWayStop(RegistryFriendlyByteBuf buffer)
    	{
    		super(buffer);
    	}

		@Override
		public void handle() {
			SoundAccess.playMilkyWayStopSound(pos);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return PacketHandlerInit.SOUND_STARGATE_MILKYWAY_STOP;
		}
	}
}


