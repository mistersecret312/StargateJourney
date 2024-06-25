package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundSymbolUpdatePacket implements NetworkMessage<ClientNetworkContext>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSymbolUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundSymbolUpdatePacket::encode, ClientboundSymbolUpdatePacket::new);

    public final BlockPos pos;
    public final int symbolNumber;
    public final String pointOfOrigin;
    public final String symbols;

    public ClientboundSymbolUpdatePacket(BlockPos pos, int symbolNumber, String pointOfOrigin, String symbols)
    {
        this.pos = pos;
        this.symbolNumber = symbolNumber;
        this.pointOfOrigin = pointOfOrigin;
        this.symbols = symbols;
    }

    public ClientboundSymbolUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readUtf(), buffer.readUtf());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.symbolNumber);
        buffer.writeUtf(this.pointOfOrigin);
        buffer.writeUtf(this.symbols);
    }

    @Override
    public void handle(ClientNetworkContext context) {
        ClientAccess.updateSymbol(this.pos, this.symbolNumber, this.pointOfOrigin, this.symbols);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.SYMBOLS;
    }
}


