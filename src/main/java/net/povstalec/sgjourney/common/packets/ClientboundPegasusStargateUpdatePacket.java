package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundPegasusStargateUpdatePacket implements NetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPegasusStargateUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundPegasusStargateUpdatePacket::encode, ClientboundPegasusStargateUpdatePacket::new);

    public final BlockPos pos;
    public final int symbolBuffer;
    public final int[] addressBuffer;
    public final int currentSymbol;

    public ClientboundPegasusStargateUpdatePacket(BlockPos pos, int symbolBuffer, int[] addressBuffer, int currentSymbol)
    {
        this.pos = pos;
        this.symbolBuffer = symbolBuffer;
        this.addressBuffer = addressBuffer;
        this.currentSymbol = currentSymbol;
    }

    public ClientboundPegasusStargateUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readVarIntArray(), buffer.readInt());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.symbolBuffer);
        buffer.writeVarIntArray(this.addressBuffer);
        buffer.writeInt(this.currentSymbol);
    }

    @Override
    public void handle() {
        ClientAccess.updatePegasusStargate(this.pos, this.symbolBuffer, this.addressBuffer, this.currentSymbol);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.PG;
    }
}


