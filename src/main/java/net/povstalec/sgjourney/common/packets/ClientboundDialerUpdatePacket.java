package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundDialerUpdatePacket implements NetworkMessage<ClientNetworkContext>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDialerUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundDialerUpdatePacket::encode, ClientboundDialerUpdatePacket::new);

    public final BlockPos pos;

    public ClientboundDialerUpdatePacket(BlockPos pos)
    {
        this.pos = pos;
    }

    public ClientboundDialerUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public void handle(ClientNetworkContext context) {
        //ClientAccess.updateDialer(pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.DIALER;
    }
}


