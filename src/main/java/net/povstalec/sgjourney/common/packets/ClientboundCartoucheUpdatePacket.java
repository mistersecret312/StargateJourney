package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundCartoucheUpdatePacket implements NetworkMessage<ClientNetworkContext>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCartoucheUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundCartoucheUpdatePacket::encode, ClientboundCartoucheUpdatePacket::new);

    public final BlockPos pos;
    public final String symbols;
    public final int[] address;

    public ClientboundCartoucheUpdatePacket(BlockPos pos, String symbols, int[] address)
    {
        this.pos = pos;
        this.symbols = symbols;
        this.address = address;
    }

    public ClientboundCartoucheUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readUtf(), buffer.readVarIntArray());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeUtf(this.symbols);
        buffer.writeVarIntArray(this.address);
    }

    @Override
    public void handle(ClientNetworkContext context) {
        ClientAccess.updateCartouche(pos, symbols, address);

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.CARTOUCHE;
    }
}


