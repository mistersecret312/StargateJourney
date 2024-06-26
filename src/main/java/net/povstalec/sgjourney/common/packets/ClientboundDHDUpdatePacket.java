package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundDHDUpdatePacket implements NetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDHDUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundDHDUpdatePacket::encode, ClientboundDHDUpdatePacket::new);


    public final BlockPos pos;
    public final String symbols;
    public final int[] address;
    boolean isCenterButtonEngaged;

    public ClientboundDHDUpdatePacket(BlockPos pos, String symbols, int[] address, boolean isCenterButtonEngaged)
    {
        this.pos = pos;
        this.symbols = symbols;
        this.address = address;
        this.isCenterButtonEngaged = isCenterButtonEngaged;
    }

    public ClientboundDHDUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readUtf(), buffer.readVarIntArray(), buffer.readBoolean());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeUtf(this.symbols);
        buffer.writeVarIntArray(this.address);
        buffer.writeBoolean(this.isCenterButtonEngaged);
    }

    @Override
    public void handle() {
        ClientAccess.updateDHD(pos, symbols, address, isCenterButtonEngaged);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.DHD;
    }
}


