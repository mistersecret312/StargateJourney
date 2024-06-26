package net.povstalec.sgjourney.common.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundStargateUpdatePacket implements NetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundStargateUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundStargateUpdatePacket::encode, ClientboundStargateUpdatePacket::new);

    public final BlockPos pos;
    public final int[] address;
    public final int[] engagedChevrons;
    public final int kawooshTick;
    public final int tick;
    public final String pointOfOrigin;
    public final String symbols;
    public final String variant;

    public ClientboundStargateUpdatePacket(BlockPos pos, int[] address, int[] engagedChevrons, int kawooshTick, int tick, String pointOfOrigin, String symbols, String variant)
    {
        this.pos = pos;
        this.address = address;
        this.engagedChevrons = engagedChevrons;
        this.kawooshTick = kawooshTick;
        this.tick = tick;
        this.pointOfOrigin = pointOfOrigin;
        this.symbols = symbols;
        this.variant = variant;
    }

    public ClientboundStargateUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readVarIntArray(), buffer.readVarIntArray(), buffer.readInt(), buffer.readInt(), buffer.readUtf(), buffer.readUtf(), buffer.readUtf());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeVarIntArray(this.address);
        buffer.writeVarIntArray(this.engagedChevrons);
        buffer.writeInt(this.kawooshTick);
        buffer.writeInt(this.tick);
        buffer.writeUtf(this.pointOfOrigin);
        buffer.writeUtf(this.symbols);
        buffer.writeUtf(this.variant);
    }

    @Override
    public void handle() {
        ClientAccess.updateStargate(this.pos, this.address, this.engagedChevrons, this.kawooshTick, this.tick, this.pointOfOrigin, this.symbols, this.variant);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.STARGATE_UPDATE;
    }
}


