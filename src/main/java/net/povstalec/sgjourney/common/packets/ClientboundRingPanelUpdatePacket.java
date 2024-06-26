package net.povstalec.sgjourney.common.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundRingPanelUpdatePacket implements NetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRingPanelUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundRingPanelUpdatePacket::encode, ClientboundRingPanelUpdatePacket::new);

    public final BlockPos pos;
    public final int ringsFound;
    public final BlockPos rings1Pos;
    public final BlockPos rings2Pos;
    public final BlockPos rings3Pos;
    public final BlockPos rings4Pos;
    public final BlockPos rings5Pos;
    public final BlockPos rings6Pos;

    public ClientboundRingPanelUpdatePacket(BlockPos pos, int ringsFound, BlockPos rings1Pos, BlockPos rings2Pos, BlockPos rings3Pos, BlockPos rings4Pos, BlockPos rings5Pos, BlockPos rings6Pos)
    {
        this.pos = pos;
        this.ringsFound = ringsFound;
        this.rings1Pos = rings1Pos;
        this.rings2Pos = rings2Pos;
        this.rings3Pos = rings3Pos;
        this.rings4Pos = rings4Pos;
        this.rings5Pos = rings5Pos;
        this.rings6Pos = rings6Pos;
    }

    public ClientboundRingPanelUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readBlockPos(), buffer.readBlockPos(), buffer.readBlockPos(), buffer.readBlockPos(), buffer.readBlockPos(), buffer.readBlockPos());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.ringsFound);
        buffer.writeBlockPos(this.rings1Pos);
        buffer.writeBlockPos(this.rings2Pos);
        buffer.writeBlockPos(this.rings3Pos);
        buffer.writeBlockPos(this.rings4Pos);
        buffer.writeBlockPos(this.rings5Pos);
        buffer.writeBlockPos(this.rings6Pos);
    }

    @Override
    public void handle() {
        BlockPos ringsPos[] = {rings1Pos, rings2Pos, rings3Pos, rings4Pos, rings5Pos, rings6Pos};
        ClientAccess.updateRingPanel(this.pos, this.ringsFound, ringsPos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.RING_PANEL;
    }
}


