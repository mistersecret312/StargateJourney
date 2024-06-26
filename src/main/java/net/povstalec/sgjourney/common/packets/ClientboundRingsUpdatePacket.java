package net.povstalec.sgjourney.common.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundRingsUpdatePacket implements NetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRingsUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundRingsUpdatePacket::encode, ClientboundRingsUpdatePacket::new);


    public final BlockPos pos;
    public final int emptySpace;
    public final int transportHeight;
    public final int transportLight;

    public ClientboundRingsUpdatePacket(BlockPos pos, int emptySpace, int transportHeight, int transportLight)
    {
        this.pos = pos;
        this.emptySpace = emptySpace;
        this.transportHeight = transportHeight;
        this.transportLight = transportLight;
    }

    public ClientboundRingsUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(pos);
        buffer.writeInt(emptySpace);
        buffer.writeInt(transportHeight);
        buffer.writeInt(transportLight);
    }

    @Override
    public void handle() {
        ClientAccess.updateRings(pos, emptySpace, transportHeight, transportLight);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.RINGS;
    }
}


