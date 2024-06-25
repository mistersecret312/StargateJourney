package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundInterfaceUpdatePacket implements NetworkMessage<ClientNetworkContext>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundInterfaceUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundInterfaceUpdatePacket::encode, ClientboundInterfaceUpdatePacket::new);

    public final BlockPos pos;
    public final long energy;

    public ClientboundInterfaceUpdatePacket(BlockPos pos, long energy)
    {
        this.pos = pos;
        this.energy = energy;
    }

    public ClientboundInterfaceUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readLong());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeLong(this.energy);
    }

    @Override
    public void handle(ClientNetworkContext context) {
        //ClientAccess.updateInterface(this.pos, this.energy);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.INTERFACE;
    }
}


